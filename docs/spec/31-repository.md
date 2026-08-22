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
| `insert` | 常に生成する |
| `update` / `updateByPk` | PK があり、かつ UPDATE 対象カラムが 1 つ以上ある |
| `findByPk` | PK がある |
| `deleteByPk` | PK がある |
| `ROW_MAPPER` と Mapper クラス | 命名マッピングを持つカラムがある |

PK を持たないテーブルには `insert` しか生成されない。UPDATE 対象カラムが 1 つもない
場合（全カラムが `excludeUpdateColumnsByTable` 対象）は `update` を生成しない。

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

1. `setNowColumnsByTable` 対象 → 常に含める。値は `now()`
2. **not null 制約があり、かつ既定値を持つ**カラム → entity の値が null のときだけ除外する
3. それ以外 → 常に含める

2 の条件を満たすカラムを省略した場合、DB の既定値が使われる。`bigserial` の
自動採番はこの規則で機能する。

対象カラムが 1 つもない場合は `insert into "t" DEFAULT VALUES` を発行する。

## REPO-012 insert 後の書き戻し

次のカラムを `returning` 句で取得し、entity へ書き戻す。

- `setNowColumnsByTable` 対象のカラム
- [REPO-011](#repo-011-insert-対象カラムの決定) の 2 で除外されたカラム

insert 対象カラムが 1 つもなかった場合は**全カラム**を `returning` の対象とする。

`returning` の対象が 1 つもない場合は `returning` 句を出力せず、書き戻しも行わない。

## REPO-020 update

```java
public {テーブル}Entity update({テーブル}Entity entity)
```

entity の PK を条件に 1 レコードを更新する。内部で `updateByPk` を呼ぶ。

```java
public {テーブル}Entity updateByPk({テーブル}Entity entity, {PK の型} pk1, ...)
```

`updateByPk` は entity とは別に PK 値を受け取る。**PK 自体を更新する**用途に使う。

set 句には `shouldSkipInUpdate` でないカラムをすべて含める。`setNowColumnsByTable`
対象のカラムは値が `now()` になる。

where 句のプレースホルダは `__pk1` から始まる連番を使う。entity 側のプレースホルダ名
（Java プロパティ名）と衝突させないため。

**PK 値も含め、すべての値は [TYPE-002](20-type-mapping.md) のパラメータ変換を適用してから
JDBC へ渡す。** enum 型の PK でこれを怠ると実行時エラーになる。

## REPO-021 update の結果判定

`setNowColumnsByTable` 対象のカラムがある場合、`returning` 句で更新後の値を取得して
entity へ書き戻す。該当レコードがなければ Spring JDBC が例外を送出する。

対象がない場合は更新件数を確認し、1 件でなければ `EmptyResultDataAccessException` を
送出する。

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

`update` / `findByPk` / `deleteByPk` の PK 引数は、**主キー制約における順序**
（`KEY_SEQ`）で並べる。カラムの定義順ではない。

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
| `primaryKeySeq` | PK の順序。PK でなければ null |
| `nullable` | null 許可か |
| `hasDefault` | 既定値を持つか |
| `isSetNow` | `now()` を設定する対象か |
| `shouldSkipInUpdate` | UPDATE 対象外か |
| `hasNameMapping` | 命名マッピングを明示指定したか |
| `toParamColumn()` | バインド変換を適用したプレースホルダ |
| `toSelectColumn()` | SELECT 変換を適用したカラム指定 |
| `toUpdateSetClause()` | UPDATE の set 句 1 項目 |

## REPO-051 RowMapper

`columnName2javaPropertyMap`（[PARAM-008](10-param.md)）で命名マッピングを指定した
カラムを持つテーブルにのみ生成する。

- `public static final {テーブル}Mapper ROW_MAPPER`
- `BeanPropertyRowMapper` を継承した内部クラス

指定がないテーブルは `BeanPropertyRowMapper` の既定変換（スネークケース →
キャメルケース）で足りるため生成しない。

このクラスにのみ `@Nullable` と `@NullMarked` を出力する。

## REPO-060 RepositoryHelper

テーブルに依存しない共通クラス。`Base{repositoryHelperClassName}` と実体クラスを
1 つずつ生成する。**実体クラスも毎回上書きする**（[CORE-004](00-overview.md)）。

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

各メソッドは戻り値の型を `Class<T>` または `RowMapper<T>` で受け取る 2 系統を持つ。
