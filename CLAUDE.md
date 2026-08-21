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

generator の主要クラス:

- `Runner` — 生成処理の全体フロー
- `Parameter` — `param.yml` のスキーマ
- `db/DbDefinitionReader`, `db/DbTypeMapper` — DB 定義の読み取りと型マッピング
- `generator/*Generator` — 出力ファイル種別ごとの生成ロジック

## 開発フロー（spec ドリブン）

生成物の挙動を変える変更は、必ずこの順序で行う。

1. `docs/spec/` の該当仕様を先に更新し、diff をユーザーに提示して承認を得る
2. 承認後に `generator/` を実装する
3. コード生成を再実行し、test-app のコンパイルとテストで生成結果を検証する
4. コミットメッセージに対応する仕様 ID を含める

ルール:

- spec にない挙動を実装しない
- 実装に合わせて spec を後追いで書き換えない。仕様変更なら 1 に戻る
- README.md はユーザー向けの入口。仕様の記述は `docs/spec/` を正とする
- リファクタリング・バグ修正（spec 通りに動いていない箇所の修正）は spec 更新不要

## コマンド

```bash
# 検証用 DB の起動
cd docker_spring_jdbc_codegen && make docker

# fat jar のビルド
make jar

# コード生成（generator/src/main/resources/param.yml を使い test-app へ出力）
cd generator && ../gradlew test

# 生成結果のコンパイルとテスト
cd test-app && ../gradlew test
```

## 注意点

- Java 21 必須
- generator / test-app とも ErrorProne + NullAway が有効。`jp.green_code` 配下は
  `@NullMarked` 扱いになるため、null 許容箇所には `@Nullable` を付ける
- Base クラス（`base` パッケージ）は実行のたび削除・再生成される。
  実体クラスは初回のみ生成され、以降は上書きしない
  （`forceOverwriteImplementation: true` で強制上書き）
- test-app の生成物は `.gitignore` 済み。生成コードを直接編集しない
