# 00 概要

spring-jdbc-codegen の仕様書。**実装ではなくこの文書が仕様の正**とする。

## この文書群の位置付け

- 生成されるコードの仕様はここに書く。README.md は利用者向けの入口であり、仕様の正ではない
- 仕様を変える場合は必ずこの文書を先に更新する。手順は CLAUDE.md を参照
- 各項目には ID を振る。実装やコミットメッセージからこの ID を参照する

## ファイル構成

| ファイル | 内容 | ID 接頭辞 |
| --- | --- | --- |
| 00-overview.md | 全体像、スコープ、実行フロー | `CORE` |
| 10-param.md | param.yml の全パラメータ | `PARAM` |
| 20-type-mapping.md | DB 型と Java 型の対応 | `TYPE` |
| 30-entity.md | Entity の生成仕様 | `ENTITY` |
| 31-repository.md | Repository の生成仕様 | `REPO` |
| 32-test-repository.md | TestRepository の生成仕様 | `TESTREPO` |
| 40-naming.md | 命名変換規則と制約 | `NAMING` |

## CORE-001 目的とスコープ

SQL は手で書く前提で、定型的な Entity / Repository / TestRepository のみを生成する。

対応する。

- PostgreSQL
- Spring JDBC (`NamedParameterJdbcTemplate`)
- Java 21 以上

対応しない。

- PostgreSQL 以外のデータベース
- ORM（SQL を書かずに Java でクエリを構築すること）
- Spring JDBC のない環境

## CORE-002 実行方法

第 1 引数に param.yml のパスを取る CLI として動作する。

```bash
java -jar spring_jdbc_codegen-x.x.jar /path/to/param.yml
```

引数がない場合は使い方を表示して正常終了する。生成は行わない。

終了コードは [CORE-007](#core-007-終了コードとエラー時の扱い) を参照。

## CORE-003 実行フロー

`Runner.run()` は次の順序で処理する。

1. param.yml を読み込む
2. `enumJavaTypeMappings` の内容を型マッピングへ追加する（[TYPE-010](20-type-mapping.md)）
3. JDBC メタデータからテーブル定義を読み取る
4. param.yml の設定を検証する（[PARAM-020](10-param.md)）。結果の出力は 9 で行う
5. Base クラスのディレクトリを削除する（[CORE-005](#core-005-base-クラスと実体クラス)）
6. 全テーブルの Entity を生成する
7. Helper と ColumnDefinition を生成する
8. 全テーブルの Repository を生成する
9. `testTargetTable` に指定されたテーブルの TestRepository を生成する
10. 4 の検証結果を警告として出力する

検証（4）は生成を中断しない。生成をすべて終えたうえで警告を出力し、
終了コードで呼び出し元へ伝える。

## CORE-004 生成されるファイル

`{entityPackage}` / `{repositoryPackage}` は param.yml の指定、`{base}` は `basePackageName`（既定 `base`）を指す。

| 出力先 | ファイル | 単位 | 上書き |
| --- | --- | --- | --- |
| `{entityPackage}.{base}` | `Base{テーブル}Entity` | テーブルごと | 毎回 |
| `{entityPackage}` | `{テーブル}Entity` | テーブルごと | 初回のみ |
| `{repositoryPackage}.{base}` | `Base{テーブル}Repository` | テーブルごと | 毎回 |
| `{repositoryPackage}` | `{テーブル}Repository` | テーブルごと | 初回のみ |
| `{repositoryPackage}.{base}` | `BaseRepositoryHelper` | 1 つ | 毎回 |
| `{repositoryPackage}` | `RepositoryHelper` | 1 つ | 毎回 |
| `{repositoryPackage}.{base}` | `BaseColumnDefinition` | 1 つ | 毎回 |
| `{repositoryPackage}` | `ColumnDefinition` | 1 つ | 毎回 |
| テスト側 `{repositoryPackage}.{base}` | `TestBase{テーブル}Repository` | テスト対象のみ | 毎回 |
| テスト側 `{repositoryPackage}` | `Test{テーブル}Repository` | テスト対象のみ | 初回のみ |
| `{entityPackage}` と `.{base}` | `package-info` | 各 1 つ | 下記参照 |

`RepositoryHelper` と `ColumnDefinition` は実体クラスだが**毎回上書きされる**。
Entity / Repository / TestRepository の実体クラスとは扱いが異なる。

`package-info` は `enableNullUnmarkedForEntityPackages: true` のときのみ生成する（[ENTITY-030](30-entity.md)）。
`{base}` 側は毎回、`{entityPackage}` 側は初回のみ上書きする。

## CORE-005 Base クラスと実体クラス

生成物はすべて「Base クラスとそれを継承する実体クラス」の 2 層構成をとる。
Base クラスは `abstract` とする。ただし `BaseColumnDefinition` だけは
そのままインスタンス化するため `abstract` を付けない。

- **Base クラス** — 実行のたびにディレクトリごと削除して再生成する。手で編集してはならない
- **実体クラス** — 存在しない場合のみ生成する。利用者が自由に編集できる

`forceOverwriteImplementation: true` を指定すると実体クラスも毎回上書きする。

削除対象は `{entityPackage}.{base}`、`{repositoryPackage}.{base}`、テスト側の
`{repositoryPackage}.{base}` の 3 ディレクトリ。テスト側は `testJavaDir` が指定されている場合のみ。

## CORE-006 対象テーブルの決定

JDBC メタデータから `jdbcSchema` に属する `TABLE` 種別のオブジェクトをすべて読み取る。
ビューやシーケンスは対象外。

`excludedTableNames` に列挙されたテーブルは Entity / Repository / TestRepository の
いずれも生成しない。

## CORE-007 終了コードとエラー時の扱い

| 終了コード | 状態 | 生成物 |
| --- | --- | --- |
| 0 | 正常終了 | 生成される |
| 1 | 例外による中断 | 中途半端な状態で残る可能性がある |
| 2 | param.yml に有効でない設定があった（[PARAM-020](10-param.md)） | **生成される** |

終了コード 2 は生成が完了したうえで返す。設定の誤りは生成を妨げないが、
気付かないまま使い続けると意図しない結果になるため、呼び出し元へ伝える。

例外による中断は JVM の既定に従い終了コード 1 になる。設定不正を 2 とするのは、
**生成物が使える状態か否かを終了コードだけで判断できるようにする**ため。

次の場合は例外を送出して異常終了する。生成物を中途半端な状態で残す可能性がある。

- 対応する Java 型がない DB 型のカラムが存在する（[TYPE-001](20-type-mapping.md)）
- JDBC メタデータの読み取りに失敗した
