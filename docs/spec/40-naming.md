# 40 命名規則

DB の名前を Java の識別子へ変換する規則。実装は `Util.toCamelCase` と
`DbTableDefinition` / `DbColumnDefinition`。

## NAMING-001 変換の基本規則

`_` または `-` を区切りとして、次の文字を大文字にして連結する。区切り文字自体は
取り除く。区切り以外の文字はすべて小文字にする。

| 入力 | 先頭大文字 | 先頭小文字 |
| --- | --- | --- |
| `account_id` | `AccountId` | `accountId` |
| `TODO_STATUS` | `TodoStatus` | `todoStatus` |
| `col-name` | `ColName` | `colName` |

**元が大文字でも小文字化される。** `ID` は `Id` になる。

## NAMING-002 カラム名からプロパティ名

カラム名を先頭小文字で変換したものを Java プロパティ名とする。

`columnName2javaPropertyMap`（[PARAM-008](10-param.md)）に指定がある場合はそちらを
優先する。テーブル名 `"*"` の指定が、個別テーブルの指定より優先される。

getter / setter はプロパティ名の先頭を大文字にして `get` / `set` を付ける。

## NAMING-003 テーブル名からクラス名

テーブル名を先頭大文字で変換したものを基準に、接頭辞と接尾辞を付ける。
既定値は [PARAM-010](10-param.md) を参照。

| クラス | 組み立て |
| --- | --- |
| Entity | `{entityClassNamePrefix}` + 変換名 + `{entityClassNameSuffix}` |
| Base Entity | `{basePackageName の先頭大文字}` + Entity 名 |
| Repository | `{repositoryClassNamePrefix}` + 変換名 + `{repositoryClassNameSuffix}` |
| Base Repository | `{basePackageName の先頭大文字}` + Repository 名 |
| TestRepository | `{testRepositoryClassNamePrefix}` + Repository 名 + `{testRepositoryClassNameSuffix}` |
| TestBase Repository | `{testRepositoryClassNamePrefix}` + Base Repository 名 + `{testRepositoryClassNameSuffix}` |
| RowMapper | `{mapperClassNamePrefix}` + 変換名 + `{mapperClassNameSuffix}` |
| Helper | `{repositoryHelperClassName}` |
| Base Helper | `{basePackageName の先頭大文字}` + Helper 名 |
| カラム定義 | `{columnDefinitionClassName}` |
| Base カラム定義 | `{basePackageName の先頭大文字}` + カラム定義名 |

`Columns` クラスの定数名はカラム名を**そのまま大文字化**したもの。キャメルケース変換は
行わない（`account_id` → `ACCOUNT_ID`）。

## NAMING-004 import の規則

生成物によって import する型の範囲が異なる。それぞれが型を使う場所が違うため。

| 生成物 | import する型 | 型を使う場所 |
| --- | --- | --- |
| Entity | 全カラムの型 | フィールドと getter / setter |
| Repository | PK の型のみ | `findByPk` などのメソッド引数 |
| TestRepository | 全カラムの型 | `generateTestData4{名前}` と `assert4{名前}` |

次の型は生成物によらず import しない。

- プリミティブ型と配列型（`byte[]`）
- `java.lang` パッケージの型。Java が暗黙に import するため不要

## NAMING-005 SQL 中の識別子

生成する SQL では、テーブル名とカラム名を常にダブルクォートで囲む。
大文字や日本語を含む識別子でも動作する。

プレースホルダ名には Java プロパティ名を使う。`updateByPk` の where 句のみ
`__pk1` からの連番を使う（[REPO-020](31-repository.md)）。

## NAMING-010 命名に関する制約

次を満たさないテーブル・カラムは動作を保証しない。

- テーブル名が Java のクラス名として妥当であること
- カラム名が Java のフィールド名として妥当であること
- 変換後の名前が、同一テーブル内・同一スキーマ内で重複しないこと

スネークケースであれば問題なく動作する。

**変換規則の性質上、異なるカラム名が同じプロパティ名になりうる。**
`col_name` と `colName` はいずれも `colName` になる。この場合は
`columnName2javaPropertyMap` で明示的に指定して回避する。

予約語や特殊記号を含む名前はエラーになる可能性がある。
