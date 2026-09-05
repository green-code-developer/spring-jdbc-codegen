# 31 Repository

テーブル 1 つにつき Repository を 1 つ生成する。実装は `BaseRepositoryGenerator` と
`RepositoryGenerator`。

## REPO-001 クラス構成

| クラス | パッケージ | 内容 |
| --- | --- | --- |
| `Base{テーブル}Repository` | `{repositoryPackage}.{base}` | 抽象クラス。CRUD とカラム定義 |
| `{テーブル}Repository` | `{repositoryPackage}` | `@Repository` を付けた実体クラス |

実体クラスはコンストラクタで `RepositoryHelper` を受け取り、Base へ委譲する。
利用者が SQL を手書きするメソッドを追加する場所。

Base クラスは `RepositoryHelper` を `protected final` フィールドとして保持する。

## REPO-002 メソッドの生成条件

テーブルの構造によって生成されるメソッドが変わる。

| メソッド | 生成条件 |
| --- | --- |
| `insertAllColumns` / `insertExcept` | 常に生成する |
| `insertExceptPk` | PK があり、PK を構成する全カラムが自動採番（[REPO-014](#repo-014-insertexceptpk)） |
| `updateAllColumns` / `updateInclude` | PK がある |
| `findByPk` | PK がある |
| `deleteByPk` | PK がある |
| `ROW_MAPPER` と Mapper クラス | 命名マッピングを持つカラムがある |

PK を持たないテーブルには、上の表のうち `insertAllColumns` / `insertExcept` と
RowMapper しか生成されない。

表に挙げたメソッドのほかに、テーブルの構造によらず次を毎回生成する。
利用者が override して挙動を変えることを想定している。

| メソッド | 用途 |
| --- | --- |
| `entityToParam()` | entity を SQL パラメータの Map へ変換する。`public static` のため override できない |
| `toInsertColumns()` | INSERT 対象カラムを決める（[REPO-011](#repo-011-insert-対象カラムの決定)） |
| `toInsertValues()` | INSERT の値を決める |
| `toInsertReturning()` | INSERT の returning 対象を決める |
| `copyReturningValues()` | `returning` で取得した値を entity へ書き戻す。insert / update 共通 |
| `execWithReturning()` | `returning` 句の組み立てと実行。insert / update 共通 |
| `validateColumns()` | カラム指定を検証する（[REPO-003](#repo-003-カラム指定の検証)）。insert / update 共通 |
| `doInsert()` / `doUpdate()` | insert / update の本体。対象カラムの集合を受け取る |

## REPO-003 カラム指定の検証

`insertExcept` と `updateInclude` は `ColumnDefinition` を可変長引数で受け取る。
次を実行時に検証し、違反した場合は `IllegalArgumentException` を送出する。

| 検証 | 対象 | 理由 |
| --- | --- | --- |
| 自テーブルの `Columns.MAP` に存在するカラムか | 両方 | `ColumnDefinition` の型は全テーブル共通のため、他テーブルの定数を渡してもコンパイルが通る |
| 同じカラムを重複指定していないか | 両方 | 列リストや set 句が重複し、SQL エラーになる |
| PK を指定していないか | `updateInclude` | PK は where 句で使う。set 句に含める用途がない（[REPO-020](#repo-020-updateallcolumns)） |

引数を `(ColumnDefinition first, ColumnDefinition... rest)` の形にすることで、
**指定が 0 件のケースはコンパイル時に防ぐ。**

## REPO-010 insertAllColumns

```java
public int insertAllColumns({テーブル}Entity entity)
```

**全カラムを INSERT 対象とする。** entity の値をそのまま送るため、値が null の
カラムには null が入る。

**戻り値は処理された件数。** DB 側で決まった値は**引数の entity に書き戻す**
（[REPO-012](#repo-012-insert-後の書き戻し)）。生成されるメソッドはいずれも
件数を返し、entity への反映は引数を通じて行う。

SQL は次の形で組み立てる。

```sql
insert into "テーブル名" ("col1", "col2") values (:col1, :col2) returning ...
```

テーブル名とカラム名は常にダブルクォートで囲む。

**自動採番の PK を持つテーブルでこのメソッドを使うと、PK に null が送られて
NOT NULL 違反になる。** DB に採番させる場合は
[`insertExceptPk`](#repo-014-insertexceptpk) を使う。このメソッドは、PK の値を
明示して投入したい場合（データ移行、初期データ投入、自然キーのテーブル）に用いる。

## REPO-011 insert 対象カラムの決定

**INSERT 対象カラムはメソッドと引数だけで決まる。entity の値は判定に使わない。**

| メソッド | INSERT 対象カラム |
| --- | --- |
| `insertAllColumns` | 全カラム |
| `insertExcept` | 全カラムから、引数で指定されたカラムを除いたもの |
| `insertExceptPk` | 全カラムから PK を除いたもの |

**除外したカラムには DB の既定値が使われる。**

対象カラムが 1 つもない場合は `insert into "t" DEFAULT VALUES` を発行する。

v3 までは entity の値が null かどうかで対象を決めていた。この規則では
「null を入れたい」と「既定値を使いたい」を区別できないため廃止した。

## REPO-012 insert 後の書き戻し

次のカラムを `returning` 句で取得し、entity へ書き戻す。

- `returningColumnsByTable` 対象のカラム（[PARAM-006](10-param.md)）
- **INSERT 対象から除外されたカラム**（[REPO-011](#repo-011-insert-対象カラムの決定)）

除外したカラムは「値を DB に決めさせたカラム」であるため、その結果を取得する。
`insertExceptPk` で採番された PK が entity へ入るのはこの規則による。

両者が重複する場合は 1 つにまとめる。

insert 対象カラムが 1 つもなかった場合は**全カラム**を `returning` の対象とする。

`returning` の対象が 1 つもない場合は `returning` 句を出力せず、書き戻しも行わない。

## REPO-013 insertExcept

```java
public int insertExcept({テーブル}Entity entity, ColumnDefinition first, ColumnDefinition... rest)
```

**指定したカラムを INSERT 対象から外す。** DB の既定値を使いたいカラムを指定する。

引数の検証は [REPO-003](#repo-003-カラム指定の検証)。対象カラムの決め方
（[REPO-011](#repo-011-insert-対象カラムの決定)）以外の振る舞いは
`insertAllColumns` と同じ。

除外したカラムは `returning` で取得され、entity へ書き戻される
（[REPO-012](#repo-012-insert-後の書き戻し)）。既定値を知る手段がこれになる。

## REPO-014 insertExceptPk

```java
public int insertExceptPk({テーブル}Entity entity)
```

**PK を構成する全カラムを INSERT 対象から外す。** 内部で `insertExcept` を呼ぶ。
DB に PK を採番させる、最も頻度の高い形の短縮形。

**PK を構成する全カラムが自動採番のときだけ生成する。** 1 つでも自動採番でない
カラムが含まれる場合、そこへ null が送られて必ず失敗するため。

自動採番とは、JDBC メタデータの `IS_AUTOINCREMENT` が `YES` のカラムを指す。
`smallserial` / `serial` / `bigserial` と `generated as identity` の両方が該当する。

既定値式（`COLUMN_DEF`）は判定に使わない。`identity` 列は `COLUMN_DEF` が空で
判別できず、また既定値を持つだけのカラム（`now()` や `'NEW'`）と区別するために
文字列マッチが必要になるため。`is_identity` は JDBC の `getColumns()` が返さないので、
自前で判定するには `information_schema` への別クエリが要る。

## REPO-020 updateAllColumns

```java
public int updateAllColumns({テーブル}Entity entity)
```

entity の PK を条件に 1 レコードを更新する。

**set 句には PK を除く全カラムを含める。** PK は where 句で使うため、set 句に
含める意味がない。

v3 までの `updateByPk`（entity とは別に PK 値を受け取り、**PK 自体を更新する**）は
廃止した。PK を変更する場面が想定しにくく、set 句に PK を含める唯一の理由で
あったため。必要な場合は `helper.exec()` で手書きする。

更新させたくないカラムは、[`updateInclude`](#repo-022-updateinclude) で対象から
外すか、DB のトリガーで元の値へ戻す。

where 句のプレースホルダは `__pk1` から始まる連番を使う。entity 側のプレースホルダ名
（Java プロパティ名）と衝突させないため。

**PK 値も含め、すべての値は [TYPE-002](20-type-mapping.md) のパラメータ変換を適用してから
JDBC へ渡す。** enum 型の PK でこれを怠ると実行時エラーになる。

## REPO-021 update の結果判定

**該当レコードがなくても例外は送出しない。** 戻り値が 0 になるので、呼び出し側で
判断する。楽観ロックのように「0 件が正常な結果」となる場合があるため、
例外ではなく件数で伝える。

`returningColumnsByTable` 対象のカラムがある場合、`returning` 句で更新後の値を
取得して entity へ書き戻す。取得は `helper.optional()` で行い、該当レコードが
なければ書き戻しをせず 0 を返す。

**set 句に含まれるかどうかは書き戻しの対象に影響しない。** insert とは異なり、
set 句から外したカラムを `returning` に加えることはしない。更新対象から外した
カラムは「触らなかった」だけであり、値を知る必要がないため。

## REPO-022 updateInclude

```java
public int updateInclude({テーブル}Entity entity, ColumnDefinition first, ColumnDefinition... rest)
```

**指定したカラムだけを set 句に含める。** 部分更新に用いる。

| メソッド | set 句に含めるカラム |
| --- | --- |
| `updateAllColumns` | PK を除く全カラム |
| `updateInclude` | 引数で指定されたカラム |

**対象カラムは引数だけで決まり、entity の値は判定に使わない。** 指定したカラムの
値が null なら NULL で更新する。**nullable なカラムを NULL に戻せる。**

引数の検証は [REPO-003](#repo-003-カラム指定の検証)。PK を指定した場合は
`IllegalArgumentException` を送出する。

v3 までの `updateNotNull`（値が null のカラムを set 句から外す）は廃止した。
null が「NULL にしたい」と「更新しない」の 2 つの意味を持ち、区別できなかったため。

結果の判定と書き戻しは [REPO-021](#repo-021-update-の結果判定) と同じ。

## REPO-030 findByPk

```java
public Optional<{テーブル}Entity> findByPk({PK の型} pk1, ...)
```

PK を引数に取り、該当レコードを `Optional` で返す。SELECT 句には
`Columns.selectAster()`（[REPO-050](#repo-050-columns-クラス)）を使う。

## REPO-040 deleteByPk

```java
public int deleteByPk({PK の型} pk1, ...)
```

削除した件数を返す。該当レコードがなくても例外は送出せず 0 を返す。

## REPO-041 PK 引数の順序

`findByPk` / `deleteByPk` の PK 引数は、**主キー制約における順序**
（`KEY_SEQ`）で並べる。カラムの定義順ではない。`updateAllColumns` /
`updateInclude` は entity から PK を取り出すため引数を持たない。

## REPO-050 Columns クラス

Base クラスの内部に `public static class Columns` を生成する。

- カラムごとに `public static final ColumnDefinition {カラム名の大文字}` を定義する
- `MAP` — カラム名から `ColumnDefinition` を引く `LinkedHashMap`。カラム順を保つ
- `selectAster()` — 全カラムを SELECT 変換付きでカンマ連結した文字列を返す

`selectAster()` は `select * from t` と書きたい場面で `*` の代わりに使う。
`interval` 型のように SELECT 時の変換が必要なカラムを正しく取得するため。

`ColumnDefinition` が持つ情報は次のとおり。

| 項目 | 内容 |
| --- | --- |
| `columnName` | DB のカラム名 |
| `javaPropertyName` | Java のプロパティ名 |
| `javaFqcn` | Java の型の FQCN |
| `dbTypeName` | DB の型名 |
| `jdbcType` / `columnSize` | JDBC メタデータの値 |
| `dbParamTemplate` | バインド変換のテンプレート。変換しない場合は null |
| `dbSelectTemplate` | SELECT 変換のテンプレート。変換しない場合は null |
| `primaryKeySeq` | PK の順序。PK でなければ null |
| `nullable` | null 許可か |
| `hasDefault` | 既定値を持つか |
| `isReturning` | `returning` で取得する対象か（[PARAM-006](10-param.md)） |
| `hasNameMapping` | 命名マッピングを明示指定したか |
| `toParamColumn()` | バインド変換を適用したプレースホルダ |
| `toSelectColumn()` | SELECT 変換を適用したカラム指定 |
| `toUpdateSetClause()` | UPDATE の set 句 1 項目 |

null になりうる `primaryKeySeq` / `dbParamTemplate` / `dbSelectTemplate` には
`@Nullable` を付与する。対象はフィールド、コンストラクタ引数、getter の 3 箇所。
アノテーションは `org.jspecify.annotations.Nullable` で固定とし、差し替えられない。

**この付与は `useNullMarked: true` のときだけ行う。**
既定（`false`）では `@Nullable` も import も出力しない。

## REPO-051 RowMapper

`columnName2javaPropertyMap`（[PARAM-008](10-param.md)）で命名マッピングを指定した
カラムを持つテーブルにのみ生成する。

- `public static final {テーブル}Mapper ROW_MAPPER`
- `BeanPropertyRowMapper` を継承した内部クラス

指定がないテーブルは `BeanPropertyRowMapper` の既定変換（スネークケース →
キャメルケース）で足りるため生成しない。

このクラスには `@NullMarked` を付与し、`underscoreName()` の引数に `@Nullable` を付与する。

## REPO-060 RepositoryHelper

テーブルに依存しない共通クラス。`{basePackageName の先頭大文字}{repositoryHelperClassName}`
と実体クラスを 1 つずつ生成する（[NAMING-003](40-naming.md)）。**実体クラスも毎回上書きする**（[CORE-004](00-overview.md)）。

`NamedParameterJdbcTemplate` を薄くラップし、SQL を `List<String>` でも
`String` でも渡せるようにする。

| メソッド | 内容 |
| --- | --- |
| `list()` | 複数件取得 |
| `optional()` | 先頭 1 件を `Optional` で取得 |
| `single()` | 1 件取得。取得できなければ例外 |
| `exec()` | 更新系。更新件数を返す |
| `count()` | `long` 1 カラムを取得する |
| `pickBySeed()` | enum の定数を seed で選ぶ。テスト用 |
| `escapeLike()` | LIKE パターンの特殊文字をエスケープする（[REPO-061](#repo-061-escapelike)） |

`list()` / `optional()` / `single()` は、戻り値の型を `Class<T>` で受け取るものと
`RowMapper<T>` で受け取るものの 2 系統を持つ。`exec()` / `count()` / `pickBySeed()` /
`escapeLike()` にこの区別はない。

`Class<T>` を受け取る系統は、`Class` がプリミティブ・`Number` のサブクラス・`String`
のいずれかであれば単一カラムとして扱い、それ以外は `BeanPropertyRowMapper` で
マッピングする。Entity 以外の任意のクラスにも対応できる。

## REPO-061 escapeLike

```java
public static String escapeLike(String s)
public static String escapeLike(String s, char escapeChar)
```

引数の文字列を LIKE パターンの一部として安全に使える形へ変換する。
`escapeChar` を省略した場合は `\` を使う。

| 入力 | 出力（既定） |
| --- | --- |
| `%` | `\%` |
| `_` | `\_` |
| `\` | `\\` |

**置換はエスケープ文字自身 → `%` → `_` の順で行う。** 逆順にすると、先の置換が
付けたエスケープ文字を後の置換が再びエスケープしてしまう。

バインド変数は SQL の構文としての安全性を守るだけで、値が LIKE パターンとして
解釈されることは防げない。`concat('%', :keyword, '%')` に `search_word` を渡すと
`_` が任意の 1 文字として働く。呼び出し側で明示的にこのメソッドを通す。

前後の `%` は付けない。前方一致・後方一致・部分一致のどれを求めるかは呼び出し側の
判断であるため。

## REPO-062 escapeLike のエスケープ文字

既定は `\`。PostgreSQL の LIKE は `escape` 句を省略したときのエスケープ文字が
`\` であるため、**既定を使う限り SQL 側に `escape` 句は要らない。**

`escapeChar` を指定した場合は、**呼び出し側が SQL に `escape` 句を書く責任を持つ。**
生成コードは SQL を組み立てないため、両者の対応は検証できない。

```java
helper.list("select ... where note like concat('%', :keyword, '%') escape '$'",
        Map.of("keyword", RepositoryHelper.escapeLike(keyword, '$')), ...);
```

指定を可能にしているのは、`\` が扱いにくい場面があるため。

- `standard_conforming_strings` が off の環境では文字列リテラル中の `\` の解釈が変わる
- Java のテキストブロックに `escape '\'` と書くには `\\` とエスケープが必要で読みにくい
- SQL をログや psql へ貼って確認するとき、`\` は目視で追いにくい

`escapeChar` に `%` または `_` を渡した場合は `IllegalArgumentException` を送出する。
ワイルドカード自身をエスケープ文字にすると、エスケープが自己矛盾するため。
それ以外の文字は検証しない。妥当性は SQL の `escape` 句との対応で決まり、
生成コードからは判断できないため。
