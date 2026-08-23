# 10 param.yml

生成の入力となる設定ファイル。第 1 引数で指定する。実装は `Parameter` クラス。

## PARAM-001 パスの解決

`mainJavaDir` と `testJavaDir` に相対パスを書いた場合、**param.yml が置かれた
ディレクトリからの相対**として解決する。カレントディレクトリ基準ではない。

## PARAM-002 必須パラメータ

未指定の場合の動作は保証しない。

| キー | 内容 |
| --- | --- |
| `jdbcUrl` | JDBC 接続 URL |
| `jdbcUser` | 接続ユーザー |
| `jdbcPass` | 接続パスワード |
| `jdbcSchema` | 対象スキーマ名 |
| `entityPackage` | Entity のパッケージ名 |
| `repositoryPackage` | Repository のパッケージ名 |
| `mainJavaDir` | 出力先の src/main/java |

`testJavaDir` は `testTargetTable` を指定する場合に必須。

## PARAM-003 生成対象の制御

| キー | 型 | 既定 | 内容 |
| --- | --- | --- | --- |
| `excludedTableNames` | リスト | 空 | 生成対象から除外するテーブル名 |
| `testTargetTable` | リスト | 空 | TestRepository を生成するテーブル名 |
| `forceOverwriteImplementation` | 真偽 | `false` | 実体クラスも毎回上書きするか |

`testTargetTable` に**実在しないテーブル名を書いてもエラーにならない**。
単に何も生成されない。

## PARAM-004 テーブル・カラム指定の共通形式

`excludeUpdateColumnsByTable` と `setNowColumnsByTable` は同じ形式をとる。

```yml
キー:
  テーブル名:
    - カラム名
  "*":
    - カラム名
```

テーブル名に `"*"` を指定すると全テーブルが対象になる。`"*"` の指定と
個別テーブルの指定は**どちらか一方に含まれれば該当**する（和集合）。

## PARAM-005 excludeUpdateColumnsByTable

指定したカラムを UPDATE 文の set 句から除外する。作成者・作成日時カラムを想定。

テーブルの**全カラムが除外対象**になった場合、そのテーブルには `update()` と
`updateByPk()` を生成しない（[REPO-020](31-repository.md)）。

## PARAM-006 setNowColumnsByTable

指定したカラムの値を INSERT / UPDATE 時に SQL の `now()` に置き換える。

- Java 側から値を指定できなくなる
- DB がセットした時刻を、実行後に引数の entity へ書き戻す（[REPO-012](31-repository.md)）

## PARAM-007 enumJavaTypeMappings

PostgreSQL の enum 型と Java の enum クラスを対応付ける。

```yml
enumJavaTypeMappings:
  todo_status: jp.green_code.todo.enums.TodoStatusEnum
```

テーブルを問わず、その DB 型のカラムすべてに適用する。詳細は [TYPE-010](20-type-mapping.md)。

## PARAM-008 columnName2javaPropertyMap

カラム名と Java プロパティ名を明示的に対応付ける。

```yml
columnName2javaPropertyMap:
  table_name:
    column_name: javaPropertyName
```

既定では [NAMING-002](40-naming.md) の自動変換を使う。この指定をした場合のみ、
そのテーブルの Repository に RowMapper を生成する（[REPO-050](31-repository.md)）。

テーブル名に `"*"` を指定でき、`"*"` が個別テーブル指定より優先される。

## PARAM-009 enableNullUnmarkedForEntityPackages

既定 `false`。`true` にすると Entity のパッケージに `@NullUnmarked` を付けた
`package-info.java` を生成する（[ENTITY-030](30-entity.md)）。

`@NullMarked` を使うプロジェクトで、Entity が null を保持できるようにするための設定。

## PARAM-010 命名のカスタマイズ

生成されるクラス名を変更する。既定値のままで運用することを推奨する。

| キー | 既定 | 用途 |
| --- | --- | --- |
| `basePackageName` | `base` | Base クラスを置くサブパッケージ名 |
| `entityClassNamePrefix` | 空 | Entity クラスの接頭辞 |
| `entityClassNameSuffix` | `Entity` | Entity クラスの接尾辞 |
| `repositoryClassNamePrefix` | 空 | Repository クラスの接頭辞 |
| `repositoryClassNameSuffix` | `Repository` | Repository クラスの接尾辞 |
| `testRepositoryClassNamePrefix` | `Test` | TestRepository クラスの接頭辞 |
| `testRepositoryClassNameSuffix` | 空 | TestRepository クラスの接尾辞 |
| `repositoryHelperClassName` | `RepositoryHelper` | ヘルパークラス名 |
| `columnDefinitionClassName` | `ColumnDefinition` | カラム定義クラス名 |
| `mapperClassNamePrefix` | 空 | RowMapper クラスの接頭辞 |
| `mapperClassNameSuffix` | `Mapper` | RowMapper クラスの接尾辞 |

## PARAM-011 アノテーションの差し替え

| キー | 既定 |
| --- | --- |
| `nullableFqcn` | `org.jspecify.annotations.Nullable` |
| `nullUnmarkedFqcn` | `org.jspecify.annotations.NullUnmarked` |

FQCN の最後のドット以降を単純名として取り出し、`@単純名` の形で出力する。

## PARAM-020 設定の検証

param.yml の設定が実在するテーブル・カラムを指しているかを検証し、
該当しないものを警告として出力する。

打ち間違いやテーブル定義の変更で設定が実態と合わなくなっても、その設定は
**エラーにならず単に無効化される**。「設定したのに効かない」状態は原因の
特定が難しいため、生成時に検知できるようにする。

### 検証の対象

| 設定 | 検証内容 |
| --- | --- |
| `excludedTableNames` | 列挙されたテーブルが実在するか |
| `testTargetTable` | 列挙されたテーブルが実在するか |
| `excludeUpdateColumnsByTable` | テーブルとカラムが実在するか |
| `setNowColumnsByTable` | テーブルとカラムが実在するか |
| `columnName2javaPropertyMap` | テーブルとカラムが実在するか |
| `enumJavaTypeMappings` | その DB 型を使っているカラムが存在するか |

### PARAM-021 ワイルドカード指定の扱い

テーブル名に `"*"` を指定した場合、**どのテーブルにも 1 つも存在しない場合のみ**
警告する。一部のテーブルにしか存在しないカラムを `"*"` で指定するのは
正当な使い方であり、それを警告すると実用に耐えないため。

個別のテーブル名を指定した場合は、そのテーブルに存在しなければ警告する。
テーブル自体が存在しない場合は、カラムを検証せずテーブルについてのみ警告する。

### PARAM-022 検証結果の扱い

- 生成は中断しない。すべて生成したうえで最後に警告を出力する
- 警告が 1 件以上あれば終了コード 1 を返す（[CORE-007](00-overview.md)）
- 出力は生成ログの末尾にまとめる。件数を明示し、区切りで囲む

```
========================================
param.yml に有効でない設定が 2 件あります
  警告: testTargetTable のテーブル "account_master" は存在しません
  警告: setNowColumnsByTable のカラム "updated_at" はどのテーブルにも存在しません
========================================
```
