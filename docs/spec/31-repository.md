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
| `insert` / `insertNotNull` | 常に生成する |
| `update` / `updateByPk` / `updateNotNull` | PK がある |
| `findByPk` | PK がある |
| `deleteByPk` | PK がある |
| `ROW_MAPPER` と Mapper クラス | 命名マッピングを持つカラムがある |

PK を持たないテーブルには、上の表のうち `insert` と RowMapper しか生成されない。

表に挙げたメソッドのほかに、テーブルの構造によらず次を毎回生成する。
利用者が override して挙動を変えることを想定している。

| メソッド | 用途 |
| --- | --- |
| `entityToParam()` | entity を SQL パラメータの Map へ変換する |
| `toInsertColumns()` | INSERT 対象カラムを決める（[REPO-011](#repo-011-insert-対象カラムの決定)） |
| `toInsertValues()` | INSERT の値を決める |
| `toInsertReturning()` | INSERT の returning 対象を決める |
| `copyReturningValuesInInsert()` | INSERT 後に entity へ書き戻す |
| `copyReturningValuesInUpdate()` | UPDATE 後に entity へ書き戻す。update を生成する場合のみ |

## REPO-010 insert

```java
public {テーブル}Entity insert({テーブル}Entity entity)
```

引数の entity を返す。DB 側で決まった値は entity に書き戻される。

SQL は次の形で組み立てる。

```sql
insert into "テーブル名" ("col1", "col2") values (:col1, :col2) returning ...
```

テーブル名とカラム名は常にダブルクォートで囲む。

## REPO-011 insert 対象カラムの決定

カラムごとに次の順で判定する。

2. **not null 制約があり、かつ既定値を持つ**カラム → entity の値が null のときだけ除外する
3. それ以外 → 常に含める

2 の条件を満たすカラムを省略した場合、DB の既定値が使われる。`bigserial` の
自動採番はこの規則で機能する。

対象カラムが 1 つもない場合は `insert into "t" DEFAULT VALUES` を発行する。

## REPO-012 insert 後の書き戻し

次のカラムを `returning` 句で取得し、entity へ書き戻す。

- `returningColumnsByTable` 対象のカラム（[PARAM-006](10-param.md)）
- [REPO-011](#repo-011-insert-対象カラムの決定) の 2 で除外されたカラム

insert 対象カラムが 1 つもなかった場合は**全カラム**を `returning` の対象とする。

`returning` の対象が 1 つもない場合は `returning` 句を出力せず、書き戻しも行わない。

## REPO-013 insertNotNull

```java
public {テーブル}Entity insertNotNull({テーブル}Entity entity)
```

**値が null のカラムをすべて INSERT 対象から外す。** DB の既定値を使いたい場合に用いる。

[REPO-011](#repo-011-insert-対象カラムの決定) との違いは除外の条件だけで、それ以外の
振る舞いは `insert` と同じ。

| | 除外するカラム |
| --- | --- |
| `insert` | not null かつ既定値を持つカラムのうち、値が null のもの |
| `insertNotNull` | 値が null のカラムすべて |

`insert` は「null を入れられないカラムだけ既定値に任せる」という判断をするため、
**nullable かつ既定値を持つカラム**では既定値が使われず null が入る。
そのカラムに既定値を使いたい場合に `insertNotNull` を用いる。

対象カラムが 1 つもない場合は `DEFAULT VALUES` を発行する。書き戻しの規則も
`insert` と同じで、除外したカラムを `returning` で取得して entity へ反映する
（[REPO-012](#repo-012-insert-後の書き戻し)）。

## REPO-020 update

```java
public {テーブル}Entity update({テーブル}Entity entity)
```

entity の PK を条件に 1 レコードを更新する。内部で `updateByPk` を呼ぶ。

```java
public {テーブル}Entity updateByPk({テーブル}Entity entity, {PK の型} pk1, ...)
```

`updateByPk` は entity とは別に PK 値を受け取る。**PK 自体を更新する**用途に使う。

set 句には全カラムを含める。

更新させたくないカラムは、DB のトリガーで元の値へ戻す。生成コードは
どのカラムを更新してよいかを判断しない。

where 句のプレースホルダは `__pk1` から始まる連番を使う。entity 側のプレースホルダ名
（Java プロパティ名）と衝突させないため。

**PK 値も含め、すべての値は [TYPE-002](20-type-mapping.md) のパラメータ変換を適用してから
JDBC へ渡す。** enum 型の PK でこれを怠ると実行時エラーになる。

## REPO-021 update の結果判定

**更新件数は確認しない。** 該当レコードがなくても例外は送出せず、更新件数も返さない。
件数が必要な場合は `helper.exec()` で SQL を手書きする。

`returningColumnsByTable` 対象のカラムがある場合、`returning` 句で更新後の値を
取得して entity へ書き戻す。取得は `helper.optional()` で行い、該当レコードが
なければ書き戻しをせず entity をそのまま返す。

## REPO-022 updateNotNull

```java
public {テーブル}Entity updateNotNull({テーブル}Entity entity)
```

**値が null のカラムを set 句から外す。** 部分更新に用いる。

| | set 句に含めるカラム |
| --- | --- |
| `update` | 全カラム。null なら null で更新する |
| `updateNotNull` | 値が null でないカラムのみ |

`update` は entity の状態をそのまま DB へ反映するため、変更したいカラムだけを
セットした entity を渡すと、他のカラムが null で上書きされる。部分更新には
`updateNotNull` を用いる。

逆に **nullable なカラムへ null を設定したい場合は `update` を使う。**
`updateNotNull` では「null にしたい」と「指定しなかった」を区別できない。

`returningColumnsByTable` 対象のカラムも、値が null なら set 句から外す。
DB 側で値が決まるカラムであっても、Java から値を送らないことに変わりはない。

set 句が空になる場合は `IllegalArgumentException` を送出する。更新対象のない
UPDATE 文は SQL として成立しないため。ただし PK も set 句に含まれ、呼び出し時点で
PK は非 null であるため、実際にこの状態になることはない。保険として残している。

PK を変更する用途の `updateNotNullByPk` は生成しない。部分更新と PK 変更を
同時に行う場面が想定しにくいため。必要な場合は `helper.exec()` で手書きする。

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

`updateByPk` / `findByPk` / `deleteByPk` の PK 引数は、**主キー制約における順序**
（`KEY_SEQ`）で並べる。カラムの定義順ではない。`update` は entity から PK を
取り出すため引数を持たない。

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
| `isSetNow` | `now()` を設定する対象か |
| `hasNameMapping` | 命名マッピングを明示指定したか |
| `toParamColumn()` | バインド変換を適用したプレースホルダ |
| `toSelectColumn()` | SELECT 変換を適用したカラム指定 |
| `toUpdateSetClause()` | UPDATE の set 句 1 項目 |

null になりうる `primaryKeySeq` / `dbParamTemplate` / `dbSelectTemplate` には
`@Nullable` を付与する。対象はフィールド、コンストラクタ引数、getter の 3 箇所。
アノテーションは `org.jspecify.annotations.Nullable` で固定とし、差し替えられない。

**この付与は `enableNullUnmarkedForEntityPackages: true` のときだけ行う。**
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

`list()` / `optional()` / `single()` は、戻り値の型を `Class<T>` で受け取るものと
`RowMapper<T>` で受け取るものの 2 系統を持つ。`exec()` / `count()` / `pickBySeed()` に
この区別はない。

`Class<T>` を受け取る系統は、`Class` がプリミティブ・`Number` のサブクラス・`String`
のいずれかであれば単一カラムとして扱い、それ以外は `BeanPropertyRowMapper` で
マッピングする。Entity 以外の任意のクラスにも対応できる。
