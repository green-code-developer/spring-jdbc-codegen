# 20 型マッピング

DB の型を Java の型へ対応付ける。実装は `DbTypeMapper` と `JavaType`。

## TYPE-001 マッピングの解決

JDBC メタデータが返す `TYPE_NAME` を小文字化してマッピング表を引く。
**DDL に書いた型名ではなく PostgreSQL が正規化した型名**が使われる点に注意する
（`character varying` → `varchar`、`timestamp with time zone` → `timestamptz` など）。

表にない型のカラムが存在した場合は例外を送出して異常終了する。特定のテーブルだけを
生成対象にして回避することはできない。`excludedTableNames` で除外する必要がある。

## TYPE-002 型ごとの変換規則

各 Java 型は次の 4 つの変換規則を持つ。いずれも省略可能で、省略時は変換しない。

| 規則 | 適用箇所 |
| --- | --- |
| パラメータ変換 | entity の値を JDBC へ渡す直前の Java コード。`{value}` を置換する |
| バインド変換 | INSERT / UPDATE の SQL 中のプレースホルダ。`{javaPropertyName}` を置換する |
| SELECT 変換 | SELECT 句のカラム指定。`{columnName}` を置換する |
| テストデータ生成 | TestRepository が使う値の生成式。`seed` を受け取る |

## TYPE-003 文字列・識別子

| DB 型 | Java 型 | バインド変換 | SELECT 変換 |
| --- | --- | --- | --- |
| `bpchar` | `String` | — | — |
| `varchar` | `String` | — | — |
| `text` | `String` | — | — |
| `uuid` | `java.util.UUID` | — | — |
| `xml` | `String` | `::xml` | — |
| `json` | `String` | `::jsonb` | — |
| `jsonb` | `String` | `::jsonb` | — |

`json` は `jsonb` と同じ扱いになる。`json` 列へも `::jsonb` でバインドする。

文字列型の assert は `value.trim()` を比較対象とする。`bpchar` の空白埋めを許容するため。

## TYPE-004 数値・真偽

| DB 型 | Java 型 |
| --- | --- |
| `bool` | `Boolean` |
| `int2`, `smallserial` | `Short` |
| `int4`, `serial` | `Integer` |
| `int8`, `bigserial` | `Long` |
| `float4` | `Float` |
| `float8` | `Double` |
| `numeric` | `java.math.BigDecimal` |

`BigDecimal` の assert は `compareTo` で比較する。スケールの違いを許容するため。

## TYPE-005 日付・時刻

| DB 型 | Java 型 | バインド変換 | SELECT 変換 |
| --- | --- | --- | --- |
| `date` | `java.time.LocalDate` | — | — |
| `time` | `java.time.LocalTime` | — | — |
| `timetz` | `java.time.OffsetTime` | — | — |
| `timestamp` | `java.time.LocalDateTime` | — | — |
| `timestamptz` | `java.time.OffsetDateTime` | — | — |
| `interval` | `Long` | `make_interval(secs => :x)` | `extract(epoch FROM col) AS col` |

`interval` は**秒数を表す `Long`** として扱う。`java.time.Duration` ではない。

## TYPE-006 ネットワーク

| DB 型 | Java 型 | バインド変換 |
| --- | --- | --- |
| `inet` | `String` | `::inet` |
| `cidr` | `String` | `::cidr` |
| `macaddr` | `String` | `::macaddr` |

## TYPE-007 幾何

すべて `String` にマップし、バインド時に該当型へ、SELECT 時に `::text` へ変換する。

対象は `point`, `line`, `box`, `lseg`, `path`, `polygon`, `circle`。

## TYPE-008 バイナリ

`bytea` を `byte[]` にマップする。assert は `assertArrayEquals` を使う。

`byte[]` はプリミティブ配列なので import を生成しない（[NAMING-004](40-naming.md)）。

## TYPE-010 enum 型の追加

`enumJavaTypeMappings`（[PARAM-007](10-param.md)）で登録した DB 型は、次の規則で
マッピング表へ追加される。既存の型名と同じキーを指定した場合は上書きする。

| 項目 | 値 |
| --- | --- |
| Java 型 | 指定した FQCN |
| パラメータ変換 | `String.valueOf({value})` |
| バインド変換 | `::{DB 型名}` |
| SELECT 変換 | — |
| テストデータ生成 | `pickBySeed({FQCN}.class, seed)` |

パラメータ変換で `String` へ変換するのは、PostgreSQL の JDBC ドライバが Java の
enum インスタンスから SQL 型を推測できないため。**enum 型の値を JDBC へ渡す箇所は
すべてこの変換を通す必要がある**。

`pickBySeed` は `BaseRepositoryHelper` が提供する static メソッドで、
enum の定数を seed で循環的に選ぶ。
