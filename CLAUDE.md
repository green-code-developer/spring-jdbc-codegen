# CLAUDE.md

## プロジェクト概要

spring-jdbc-codegen は、PostgreSQL のテーブル定義から Spring JDBC 用の
Entity / Repository / TestRepository を生成する CLI ツール。
`param.yml` を引数に取り、指定パッケージへ Java コードを出力する。

## 構成

| ディレクトリ | 役割 |
| --- | --- |
| `generator/` | 本体。ここが実装対象 |
| `test-app/` | 生成結果の検証用 Spring Boot アプリ。生成物は git 管理外 |
| `docker_spring_jdbc_codegen/` | 検証用 PostgreSQL と DDL |
| `docs/spec/` | 仕様書（単一の真実） |
| `docs/backlog.md` | 検討中の機能のメモ。未実装。仕様ではない |
| `generator/src/test/resources/golden/` | golden テストの入力(param.yml)と期待値(expected) |

generator の主要クラス:

- `Runner` — 生成処理の全体フロー
- `Parameter` — `param.yml` のスキーマ
- `db/DbDefinitionReader`, `db/DbTypeMapper` — DB 定義の読み取りと型マッピング
- `generator/*Generator` — 出力ファイル種別ごとの生成ロジック

## 開発フロー（spec ドリブン）

生成物の挙動を変える変更は、必ずこの順序で行う。

1. `docs/spec/` の該当仕様を先に更新し、diff をユーザーに提示して承認を得る
2. 承認後に `generator/` を実装する
3. `make verify` で検証する。golden の差分が出たら意図通りか確認し、
   `make golden-update` で更新する。**この差分が仕様変更の実体なので必ずレビューする**
4. コミットメッセージに対応する仕様 ID を含める

ルール:

- spec にない挙動を実装しない
- 検討中の機能は `docs/backlog.md` に書く。`docs/spec/` に未実装の記述を置かない。
  spec 化するときは backlog から該当項目を削除し、同じコミットに含める
- 実装に合わせて spec を後追いで書き換えない。仕様変更なら 1 に戻る
- README.md はユーザー向けの入口。仕様の記述は `docs/spec/` を正とする
- リファクタリング・バグ修正（spec 通りに動いていない箇所の修正）は spec 更新不要

## コマンド

```bash
make docker         # 検証用 DB の起動（他のコマンドの前提）
make verify         # golden 比較 + test-app のコンパイルとテスト
make golden-update  # golden を現在の生成結果で更新する
make jar            # fat jar のビルド
```

`cd generator && ../gradlew test` は 2 つのテストを実行する。

- `TestMain` — `src/main/resources/param.yml` で test-app へ生成する。検証はしない
- `TestGolden` — `src/test/resources/golden/param.yml` で `build/golden-actual/` へ生成し、
  `golden/expected/` と完全一致するか検証する

どちらも入力が DB なので Gradle の up-to-date 判定が効かない。
`test` タスクには `outputs.upToDateWhen { false }` を指定して常に実行させている。

## 注意点

- Java 21 必須
- generator / test-app とも ErrorProne + NullAway が有効。`jp.green_code` 配下は
  `@NullMarked` 扱いになるため、null 許容箇所には `@Nullable` を付ける
- Base クラス（`base` パッケージ）は実行のたび削除・再生成される。
  実体クラスは初回のみ生成され、以降は上書きしない
  （`forceOverwriteImplementation: true` で強制上書き）
- test-app の生成物は `.gitignore` 済み。生成コードを直接編集しない
- golden の `expected/` は git 管理下に置く。生成コードの差分をレビュー可能にするため
- `golden/param.yml` は `src/main/resources/param.yml` のコピー。
  出力先と `forceOverwriteImplementation` のみ異なるので、main 側を変更したら追随させる
- `Parameter.param` が static のためテストは逐次実行が前提。`maxParallelForks` を上げない
