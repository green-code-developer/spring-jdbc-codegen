# 変更履歴

このファイルは利用者に影響する変更を記録します。内部のリファクタリングやテストの追加は含みません。

3.0.0 より前の履歴は `git log` を参照してください。

## 3.1.0

### 追加

- **`RepositoryHelper` に `escapeLike()` を追加しました**（[REPO-061](docs/spec/31-repository.md)）

  LIKE 検索のパターンに含まれる `%` `_` とエスケープ文字自身をエスケープします。
  バインド変数は SQL の構文としての安全性を守るだけで、値が LIKE パターンとして
  解釈されることは防げません。`like concat('%', :keyword, '%')` に `search_word` を
  渡すと `_` が任意の 1 文字として働きます。

  ```java
  helper.list(sql, Map.of("keyword", RepositoryHelper.escapeLike(keyword)), AccountEntity.class);
  ```

  エスケープ文字は既定で `\` です。PostgreSQL の LIKE は `escape` 句を省略したときの
  エスケープ文字が `\` のため、既定を使う限り SQL 側に `escape` 句は要りません。
  第 2 引数で変更でき、その場合は SQL に `escape` 句を書いてください。

  使い方は [README 8.8](README.md#88-like-検索を行う) を参照してください。

## 3.0.0

param.yml の設定を2つ廃止しています。バージョンアップの際は移行作業が必要です。

### 破壊的変更

- **`setNowColumnsByTable` を廃止**し、`returningColumnsByTable` を導入しました

  SQL へ `now()` を書き込むのをやめ、値の決定はデータベースのトリガーに任せます。
  このツールは `returning` 句で結果を取得してentity へ書き戻すだけになります。
  移行方法は [README 5.3](README.md#53-トリガーが決めた値をentity-へ反映したい) を参照してください。

- **`excludeUpdateColumnsByTable` を廃止**しました

  更新させたくないカラムは、データベースのトリガーで元の値へ戻してください。
  `helper.exec()` で手書きしたSQL には効かず、保証にならなかったためです。
  移行方法は [README 5.2](README.md#52-作成者カラム作成日時カラムをupdate-させたくない) を参照してください。

- **生成される `ColumnDefinition` のシグネチャが変わりました**

  コンストラクタ引数と getter から `shouldSkipInUpdate` を削除し、`isSetNow` を `isReturning` へ変更しました。

- **`insert()` / `update()` の戻り値が `int`（処理件数）になりました**

  従来はentity を返していましたが、引数と同一のインスタンスで情報を持たないため、
  件数を返すよう変更しました。DB が決めた値は引き続き引数のentity へ書き戻されます。
  `deleteByPk()` と戻り値の型が揃います。

  該当するレコードが存在しなくても例外は発生しません。戻り値が 0 になるので
  呼び出し側で判断してください。楽観ロックのように「0件が正常な結果」となる場合が
  あるためです。

- **`param.yml` に有効でない設定があると終了コード 2 を返します**

  存在しないテーブル名やカラム名を指定した場合、警告を表示して終了コード 2 になります。
  コードの生成自体は完了します。

### 追加

- **`insertNotNull()`** — 値がnull のカラムをすべてInsert 対象から外します。null 許可かつ初期値を持つカラムで、初期値を使いたい場合に利用します
- **`updateNotNull()`** — 値がnull のカラムをset 句から外します。部分更新に利用します
- **`useNullMarked`** — 導入するプロジェクトが `@NullMarked` を使っている場合に `true` にします。
  Entity のパッケージに `@NullUnmarked` を付けた package-info.java を生成し、
  `ColumnDefinition` の null になりうる項目に `@Nullable` を付与します

### 修正

- enum 型のカラムに null を設定できませんでした。パラメータ変換で文字列 `"null"` に変換され、型キャストに失敗していました
- enum 型をプライマリーキーに含むテーブルで `update()` が失敗しました
- `update()` の `returning` 句でカラム名がクォートされず、大文字を含むカラム名で実行時エラーになりました
- プライマリーキーを持たないテーブルに明示的マッピングを指定すると、生成されたコードがコンパイルできませんでした
- `enumJavaTypeMappings` に大文字を含む型名を登録しても認識されませんでした
- 生成コードから `java.lang` パッケージの import を出力しないようにしました

### その他

- 動作確認済みの構成を Spring Boot 4.0.2 に更新しました
- README に「[8. 手書きSQL の書き方](README.md#8-手書きsql-の書き方)」を追加しました
