# 30 Entity

テーブル 1 つにつき Entity を 1 つ生成する。実装は `BaseEntityGenerator` と
`EntityGenerator`。

## ENTITY-001 クラス構成

| クラス | パッケージ | 内容 |
| --- | --- | --- |
| `Base{テーブル}Entity` | `{entityPackage}.{base}` | 抽象クラス。フィールドと getter / setter |
| `{テーブル}Entity` | `{entityPackage}` | Base を継承したクラス。メンバーは持たない |

Base クラスは毎回再生成し、実体クラスは初回のみ生成する（[CORE-005](00-overview.md)）。
利用者が独自のメソッドを追加する場所は実体クラス。

**Entity は単一テーブルの 1 行を表す。** `findByPk` の戻り値がこれにあたる。

手書き SQL の受け皿にも使えるが、**not null 制約のあるカラムはプリミティブや
非 null で生成される**（[ENTITY-010](#entity-010-フィールドの型と-null-許容)）点に
注意が要る。外部結合などでそのカラムに NULL が返る問い合わせに使うと、
プリミティブではマッピングで例外になり、参照型では型と実態が食い違う。
そうした問い合わせは利用者が用意した専用のクラスで受ける
（README「手書き SQL の書き方」）。

## ENTITY-002 クラスコメント

Base クラスと実体クラスの双方に、元のテーブル名をコメントとして出力する。

```java
/**
 * Table: account
 */
public abstract class BaseAccountEntity {
```

## ENTITY-010 フィールドの型と null 許容

DB のカラム順（`ORDINAL_POSITION` 順）にフィールドを定義する。

- アクセス修飾子は `protected`
- 型は [20-type-mapping.md](20-type-mapping.md) に従う
- 名前は [NAMING-002](40-naming.md) に従う
- 直前に元のカラム名をコメントとして出力する

**非 null のフィールドにできるのは、次をすべて満たすカラムだけ。**

1. not null 制約がある
2. リテラルの既定値を持ち、Java の値へ変換できる（[ENTITY-012](#entity-012-既定値による初期化)）
3. `dbDeterminedColumnsByTable` に登録されていない（[PARAM-006](10-param.md)）

| カラム | フィールド |
| --- | --- |
| 3 条件をすべて満たす・**数値 / 真偽** | **プリミティブ**（`long` など）＋ 既定値で初期化 |
| 3 条件をすべて満たす・**文字列・日付時刻・enum などの参照型** | **非 null の参照型** ＋ 既定値で初期化 |
| それ以外 | **`@Nullable` のラッパー型** |

既定値を持つカラムは文字列が多いため、実際には 2 行目が大半を占める。

```java
/** updated_by */
protected long updatedBy = -1L;

/** deadline */
@Nullable
protected OffsetDateTime deadline;
```

各条件の意味は次のとおり。

- **not null 制約** — 読み出し側の保証。nullable なカラムを非 null にすると、
  NULL を持つ行を読んだ時点で型と実態が食い違う。プリミティブの場合は
  マッピングで例外になる
- **リテラルの既定値** — 初期化の手段。初期値を与えられないと、未初期化の
  非 null フィールドになる
- **`dbDeterminedColumnsByTable` 未登録** — 登録済みのカラムは DB 側が値を決めるため、
  Java 側の初期値に意味がない

**変換できる形の既定値だけを初期値にする**（[ENTITY-012](#entity-012-既定値による初期化)）。
`nextval()` や `now()` のような関数呼び出しは実行のたびに値が変わり Java 側で写せないため
対象外で、これらのカラムは `@Nullable` になる。serial / identity の PK もこれに該当し、
採番前を null で表せる。

## ENTITY-011 getter と setter

全フィールドについて `get{名前}` / `set{名前}` を生成する。名前は Java プロパティ名の
先頭を大文字にしたもの。

- 真偽値でも `is` ではなく `get` を使う
- 全フィールドの getter を出力した後に setter を出力するのではなく、
  フィールドごとに getter / setter の順で出力する

**フィールドが `@Nullable` のときは、getter の戻り値と setter の引数にも付与する**
（[ENTITY-030](#entity-030-null-安全)）。3 箇所すべてに必要で、欠けると次のようになる。

| 欠けている箇所 | 起きること |
| --- | --- |
| getter の戻り値 | `returning @Nullable expression from method with @NonNull return type` でコンパイルが通らない |
| setter の引数 | 呼び出し側が null を渡せない。nullable なカラムを NULL へ戻せなくなる |

## ENTITY-012 既定値による初期化

[ENTITY-010](#entity-010-フィールドの型と-null-許容) の 3 条件を満たすカラムは、
DB の既定値を Java の値へ変換してフィールドの初期値にする。

**DB の既定値をそのまま写す。値を捏造しない。** そのカラムを INSERT 対象から
外した場合（[REPO-013](31-repository.md)）と同じ値になる。

### 変換の対象とする型

次の型だけを変換する。ほかの型は変換せず、そのカラムは `@Nullable` になる。

| Java の型 | 既定値の例 | 初期値 |
| --- | --- | --- |
| `String` | `'X'::text` | `"X"` |
| `Short` / `Integer` / `Long` | `'-1'::integer` | `-1L` |
| `Boolean` | `true` | `true` |
| `Float` / `Double` | `1.5` | `1.5d` |
| `BigDecimal` | `0.5` | `new BigDecimal("0.5")` |
| `UUID` | `'9529478b-...'::uuid` | `UUID.fromString("9529478b-...")` |
| enum | `'NEW'::status_enum` | `StatusEnum.NEW` |
| `LocalDate` / `LocalTime` / `LocalDateTime` | `'2000-01-01'::date` | `LocalDate.parse("2000-01-01")` |
| `OffsetTime` / `OffsetDateTime` | `'2000-01-01 00:00:00+09'::timestamptz` | `OffsetDateTime.parse("2000-01-01T00:00:00+09:00")` |

`byte[]`（`bytea`）と `interval` は**対象外**とする。前者はリテラルの表現が煩雑で、
後者は `Long`（秒）へ変換する規則が既定値の文法と対応しないため。

### 受け入れる既定値の形

`COLUMN_DEF` は `'X'::text` のようにクォートと型キャストを伴う。
**次の 2 つの形だけを受け入れ、ほかは変換しない。**

| 形 | 例 |
| --- | --- |
| クォート済みリテラル（型キャストは任意） | `'X'::text` / `'-1'::integer` / `'2000-01-01'::date` |
| 数値・真偽のリテラル | `7` / `-1.5` / `true` |

クォート済みリテラルは、前後のクォートを外し `''` を `'` に戻した部分を値とする。

**「関数かどうか」では判定しない。** `nextval('seq'::regclass)` は内側に型キャストを
持つため、末尾の `::型名` を機械的に外す方法では値を誤って取り出す。受け入れる形を
列挙して一致しないものを変換しない方が、想定しない書き方に対して安全側に倒れる。

### 変換の検証

形が一致しても、**生成時に実際の変換を試し、失敗したものは変換しない。**
`'infinity'::timestamptz` のように形は正しくても `java.time` が解釈できない値があり、
そのまま生成すると実行時のクラス初期化で例外になる。原因が追いにくい壊れ方のため、
生成時に弾く。

enum は定数名を直接参照するため、値が Java の識別子として妥当かを確認する。

### 日付・時刻の正規化

日付・時刻は PostgreSQL の出力が ISO-8601 と 2 点異なるため、
`java.time` の `parse()` に渡す前に正規化する。

| 差異 | PostgreSQL | 正規化後 |
| --- | --- | --- |
| 日付と時刻の区切り | `2000-01-01 00:00:00` | `2000-01-01T00:00:00` |
| オフセットの桁 | `+09` | `+09:00` |

`date` と `time` はそのまま `parse()` に渡せる。

**タイムゾーンを持つ型の既定値の表記は、生成に使う DB の TimeZone 設定に依存する。**
同じ DDL でも設定が違えば `+09:00` と `+00:00` のように表記が変わる（指す時刻は同じ）。
生成結果を比較する場合は DB の設定を揃える。

**変換に失敗した場合もそのカラムを `@Nullable` として扱い、生成は続行する。**
既定値の書き方は PostgreSQL の出力形式に依存するため、想定外の形を
エラーにすると生成が止まってしまう。

### 変換の実装

型ごとの Java 式は `JavaType.defaultValueSnippet` に定義し、**生成時に解決する。**
実行時に既定値の文字列を解析しない。`BeanPropertyRowMapper` は 1 行につき
1 インスタンスを生成するため、解析を実行時に置くと行数分の負荷になる。

## ENTITY-020 import

フィールドの型のうち、パッケージを持つものだけを import する。
`@Nullable` のフィールドが 1 つでもある場合は `org.jspecify.annotations.Nullable`
も import する（[ENTITY-030](#entity-030-null-安全)）。
重複を除去し、辞書順に並べる。

`java.lang` パッケージの型、プリミティブ型、配列型（`byte[]`）は import しない
（[NAMING-004](40-naming.md)）。

## ENTITY-030 null 安全

`@Nullable` は `org.jspecify.annotations.Nullable` で固定とし、差し替えられない。
**設定によらず常に出力する。**

生成コードを使うプロジェクトのクラスパスに `org.jspecify:jspecify` が必要になる。
Spring Boot が推移的に持ち込むため、利用者が明示的に依存を追加する必要はない。

`@Nullable` を出力する対象は次の 3 つ。

| 対象 | 付与する箇所 |
| --- | --- |
| Entity のフィールド（[ENTITY-010](#entity-010-フィールドの型と-null-許容)） | フィールド・getter の戻り値・setter の引数 |
| `ColumnDefinition` と `BaseColumnDefinition`（[REPO-050](31-repository.md)） | null になりうる項目 |
| RowMapper を生成した Repository（[REPO-051](31-repository.md)） | `underscoreName()` の引数 |

**`package-info.java` は生成しない。** v3 までは entity パッケージへ
`@NullUnmarked` の `package-info.java` を出力し、null 許容をパッケージ単位で
表現していた。カラムごとに `@Nullable` を付けるようになったため不要になった。
