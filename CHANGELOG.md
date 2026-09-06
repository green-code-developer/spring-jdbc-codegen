# 変更履歴

このファイルは利用者に影響する変更を記録します。内部のリファクタリングやテストの追加は含みません。

3.0.0 より前の履歴は `git log` を参照してください。

## 4.0.0

INSERT / UPDATE のメソッドをすべて置き換えています。バージョンアップの際は移行作業が必要です。

対象カラムを **entity の値が null かどうか**で決める方式をやめ、**引数で明示する**方式に
変えました。旧方式では null が「NULL を入れたい」と「DB に任せたい」の 2 つの意味を持ち、
区別できませんでした。nullable なカラムを NULL へ戻せない、という制約もここから来ていました。

### 破壊的変更

- **`insert` / `insertNotNull` を廃止**し、`insertAllColumns` / `insertExcept` /
  `insertExceptPk` を導入しました（[REPO-010](docs/spec/31-repository.md)〜[REPO-014](docs/spec/31-repository.md)）

  | 旧 | 新 |
  | --- | --- |
  | `insert(entity)`（自動採番 PK のテーブル） | `insertExceptPk(entity)` |
  | `insert(entity)`（PK を明示、PK なしテーブル） | `insertAllColumns(entity)` |
  | `insertNotNull(entity)` | `insertExcept(entity, 既定値を使いたいカラム...)` |

  `insertAllColumns` は全カラムを送るため、**自動採番の PK を持つテーブルで使うと
  NOT NULL 違反になります。** DB に採番させる場合は `insertExceptPk` を使ってください。

  `insertExceptPk` は、PK を構成する全カラムを DB 側で決められる（自動採番または
  既定値を持つ）テーブルにのみ生成されます。

- **`update` / `updateNotNull` / `updateByPk` を廃止**し、`updateAllColumns` /
  `updateInclude` を導入しました（[REPO-020](docs/spec/31-repository.md)、[REPO-022](docs/spec/31-repository.md)）

  | 旧 | 新 |
  | --- | --- |
  | `update(entity)` | `updateAllColumns(entity)` |
  | `updateNotNull(entity)` | `updateInclude(entity, 更新したいカラム...)` |
  | `updateByPk(entity, pk)` | 廃止。`helper.exec()` で手書き |

  `updateAllColumns` は **set 句に PK を含めません。** PK を変更する手段が
  なくなったため、set 句に含める理由がなくなりました。

  `updateInclude` は指定したカラムの値が null なら NULL で更新します。
  **nullable なカラムを NULL へ戻せるようになりました。**

- **メソッド名をすべて変更したため、移行漏れはコンパイルエラーになります。**
  意味が変わったメソッドを気付かずに使い続けることはありません。

- **Entity のフィールドがカラムごとにnull 許容を表すようになりました**（[ENTITY-010](docs/spec/30-entity.md)）

  entity パッケージへ `@NullUnmarked` の `package-info.java` を出力するのをやめ、
  カラムの定義から非null かどうかを決めます。**Entity の型が変わるため、
  利用側のコードに影響します。**

  | カラム | フィールド |
  | --- | --- |
  | not null・リテラルの既定値あり・数値 / 真偽 | `long` などのプリミティブ ＋ 既定値で初期化 |
  | not null・リテラルの既定値あり・その他 | 非null ＋ 既定値で初期化 |
  | それ以外 | `@Nullable` のラッパー型 |

  ```java
  protected String name = "";           // not null default ''
  protected long updatedBy = -1L;       // not null default -1
  protected @Nullable Long accountId;   // bigserial（nextval は変換対象外）
  protected @Nullable String note;      // null 許可
  ```

  **既定値を写すだけで値を捏造しません。** 初期値は、そのカラムをinsert の対象から
  外した場合と同じ値になります。変換に対応する型は [README 7.1](README.md#71-既定値の変換に対応する型) を参照してください。

  自動採番のPK は `nextval()` が変換対象外のため `@Nullable` のままです。
  「PK がnull なら新規」という判定は引き続き使えます。

- **`returningColumnsByTable` を `dbDeterminedColumnsByTable` へ改名しました**（[PARAM-006](docs/spec/10-param.md)）

  `returning` は実現手段の名前でした。Entity のフィールドを `@Nullable` にする判断にも
  使うようになったため、カラムの性質を表す名前に変えています。

  登録が必要なのは**値を送ってもDB 側で上書きされるカラム**だけです。insert の対象から
  外したカラムは、外した時点で `returning` の対象になるため登録は要りません。

- **`useNullMarked` を廃止しました**

  `@Nullable` を常に出力します。設定による分岐がなくなりました。
  生成コードが `@Nullable` を使うため `org.jspecify:jspecify` がクラスパスに必要ですが、
  Spring Boot が推移的に持ち込むため依存の追加は不要です
  （[README 3.2](README.md#32-導入するプロジェクト側に必要な構成)）。

- **Entity パッケージの `package-info.java` を生成しなくなりました**

  `@NullUnmarked` を付けるためのものでした。既存の `package-info.java` が残っていると
  カラムごとの `@Nullable` が無効になるため、**削除してください。**

### 変更

- **INSERT で除外したカラムは `returning` の対象になります**（[REPO-012](docs/spec/31-repository.md)）

  除外は「値を DB に決めさせる」という意思表示なので、その結果を取得します。
  `insertExceptPk` で採番された PK が entity へ入るのはこの規則によります。

  このため `returningColumnsByTable` には**毎回値が変わるカラム**（`updated_at` など）
  だけを書けば済みます。INSERT で既定値を使いたいだけのカラムを設定に書くと、
  UPDATE のたびにも取得することになります。

- **カラム指定を実行時に検証します**（[REPO-003](docs/spec/31-repository.md)）

  他テーブルの `ColumnDefinition`、同じカラムの重複指定、`updateInclude` への PK 指定は
  `IllegalArgumentException` になります。

- 生成される TestRepository が新しいメソッドを呼ぶようになりました
  （[TESTREPO-010](docs/spec/32-test-repository.md)）

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
