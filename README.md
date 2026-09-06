# spring jdbc codegen

バージョンごとの変更点は [CHANGELOG.md](CHANGELOG.md) を参照してください。**v3.0 では param.yml の設定を2つ廃止しています。**

## 目次

- [1. 機能概要](#1-機能概要)
- [2. 導入と動かし方](#2-導入と動かし方)
  - [2.1 終了コード](#21-終了コード)
- [3. 制約](#3-制約)
  - [3.1 できないこと](#31-できないこと)
  - [3.2 導入するプロジェクト側に必要な構成](#32-導入するプロジェクト側に必要な構成)
  - [3.3 テーブルやカラムの命名](#33-テーブルやカラムの命名)
  - [3.4 動作確認済みの構成](#34-動作確認済みの構成)
  - [3.5 Lombok を使わない](#35-lombok-を使わない)
- [4. {テーブル名}Repository クラスの使い方](#4-テーブル名repository-クラスの使い方)
  - [4.1 int insertAllColumns(T entity)](#41-int-insertallcolumnst-entity)
  - [4.2 int insertExcept(T entity, ColumnDefinition... columns)](#42-int-insertexceptt-entity-columndefinition-columns)
  - [4.3 int insertExceptPk(T entity)](#43-int-insertexceptpkt-entity)
  - [4.4 int updateAllColumns(T entity)](#44-int-updateallcolumnst-entity)
  - [4.5 int updateInclude(T entity, ColumnDefinition... columns)](#45-int-updateincludet-entity-columndefinition-columns)
  - [4.6 `Optional<Entity> findByPk(pk)`](#46-optionalentity-findbypkpk)
  - [4.7 int deleteByPk(pk)](#47-int-deletebypkpk)
  - [4.8 class Columns](#48-class-columns)
  - [4.9 Columns.MAP<String, ColumnDefinition>](#49-columnsmapstring-columndefinition)
  - [4.10 Columns.selectAster()](#410-columnsselectaster)
  - [4.11 @Component RepositoryHelper](#411-component-repositoryhelper)
  - [4.12 MAPPER (RowMapper)](#412-mapper-rowmapper)
- [5. 便利な使い方](#5-便利な使い方)
  - [5.1 Enum 型を追加する](#51-enum-型を追加する)
  - [5.2 作成者カラム、作成日時カラムをUpdate させたくない](#52-作成者カラム作成日時カラムをupdate-させたくない)
  - [5.3 トリガーが決めた値をentity へ反映したい](#53-トリガーが決めた値をentity-へ反映したい)
  - [5.4 カラム名とJava プロパティ名の明示的なマッピング](#54-カラム名とjava-プロパティ名の明示的なマッピング)
  - [5.5 Base クラス](#55-base-クラス)
  - [5.6 Entity のnull 安全](#56-entity-のnull-安全)
- [6. TestRepository の使い方](#6-testrepository-の使い方)
  - [6.1 テストデータ作成で固定値を指定したい](#61-テストデータ作成で固定値を指定したい)
- [7. DB 型とJava 型の変換表](#7-db-型とjava-型の変換表)
  - [7.1 既定値の変換に対応する型](#71-既定値の変換に対応する型)
  - [7.2 対応外の型](#72-対応外の型)
- [8. 手書きSQL の書き方](#8-手書きsql-の書き方)
  - [8.1 どこに書くか](#81-どこに書くか)
  - [8.2 SQL の組み立て方](#82-sql-の組み立て方)
  - [8.3 select 句には Columns.selectAster() を使う](#83-select-句には-columnsselectaster-を使う)
  - [8.4 条件を指定して更新する、削除する](#84-条件を指定して更新する削除する)
  - [8.5 更新前の値を条件にする（楽観ロック）](#85-更新前の値を条件にする楽観ロック)
  - [8.6 件数を取得する、単一カラムを取得する](#86-件数を取得する単一カラムを取得する)
  - [8.7 集計やJOIN の結果を受け取る](#87-集計やjoin-の結果を受け取る)
  - [8.8 LIKE 検索を行う](#88-like-検索を行う)

## 1. 機能概要

spring-jdbc-codegen は、Spring JDBC + PostgreSQL 環境において
「SQL は手で書きたいが、定型的な Entity / Repository / Test を自動生成したい」
という前提で作られたコード生成用 CLI ツールです。

- コマンドライン実行型
- Entity, Repository, TestRepository 自動生成
- Spring JDBC 前提
- PostgreSQL のみ対応
- Enum 対応可能
- Insert 時にDB の既定値を使うか選択可能

## 2. 導入と動かし方

1. spring-jdbc-codegen-x.x.jar  をダウンロード

   [https://github.com/green-code-developer/spring-jdbc-codegen/releases](https://github.com/green-code-developer/spring-jdbc-codegen/releases)

2. param.yml を記載

   データベース接続情報、パッケージ名、最上位フォルダ、を指定

   ```yml
   # JDBC 接続情報（必須）
   jdbcUrl: jdbc:postgresql://localhost:56384/spring_jdbc_codegen
   jdbcUser: spring_jdbc_codegen
   jdbcPass: spring_jdbc_codegen
   jdbcSchema: spring_jdbc_codegen
   # Entity のパッケージ名（必須）
   entityPackage: jp.green_code.spring_jdbc_codegen.test_app.entity
   # Repository のパッケージ名（必須）
   repositoryPackage: jp.green_code.spring_jdbc_codegen.test_app.repository
   # Java 最上位フォルダ（必須）
   #   相対パスの場合はparam.yml のフォルダからの相対
   mainJavaDir: ../../../../test-app/src/main/java
   ```
   
　　全量は[こちら](https://github.com/green-code-developer/spring-jdbc-codegen/blob/main/generator/src/main/resources/param.yml)

3. Jar 実行

   ```bash
   java -jar spring_jdbc_codegen-x.x.jar /path/to/param.yml
   ```
   ※ Java 21 以上必須

4. 指定したパッケージにJava コードが作成される

### 2.1 終了コード

| 終了コード | 意味 |
| --- | --- |
| 0 | 正常終了 |
| 1 | DB へ接続できない、対応していない型があるなど。生成物が中途半端な状態で残る場合があります |
| 2 | コードは生成されたが、param.yml に有効でない設定があった |

param.yml のテーブル名やカラム名を打ち間違えた場合、あるいはテーブル定義の変更で
設定が実態と合わなくなった場合、その設定は**エラーにならず無効化されます**。
「設定したのに効かない」という状態に気付けるよう、実行の最後に警告を表示し、終了コード 2 を返します。
コードの生成自体は完了しているため、警告を承知の上で使い続けることもできます。
CI で確実に検知したい場合は終了コードを判定してください。
なお、テーブル名に `"*"` を指定した設定は、**どのテーブルにも 1 つも存在しない場合のみ**警告します。一部のテーブルにしか無いカラムを `"*"` で指定するのは正当な使い方のためです。

## 3. 制約

### 3.1 できないこと

- Postgres 以外のデータベース
- Spring JDBC がない環境での動作
- ORM (Object Relation Mapping)
  SQL を直接書かずJava でクエリーを構築すること

### 3.2 導入するプロジェクト側に必要な構成

- Spring JDBC
- Apache Commons Lang3
- Postgres JDBC driver
- JSpecify（Spring Boot に含まれるため記載は不要）

参考 build.gradle
```groovy
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-data-jdbc'
    implementation 'org.apache.commons:commons-lang3:3.18.0'
    runtimeOnly 'org.postgresql:postgresql:42.7.7'
    testImplementation 'org.springframework.boot:spring-boot-starter-data-jdbc-test'
    testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
}
```

### 3.3 テーブルやカラムの命名
- スネークケースであれば問題なく動作します
- 予約語や特殊記号を含む場合はエラーになる可能性があります
- テーブル名はJava のクラス名に変換可能であること。重複しないこと
- カラム名はJava のフィールド名に変換可能であること。重複しないこと

### 3.4 動作確認済みの構成

- Java 21
- Spring Boot 4.0.2
- Spring JDBC
- PostgreSQL 17

### 3.5 Lombok を使わない

生成されるコードは Lombok を使いません。Entity の getter / setter もすべて出力します。

導入するプロジェクト側で Lombok を使っていても問題ありませんが、生成されるコードには影響しません。

Lombok がなくてもそれほど困らない一方、IDE のバージョンアップにプラグインが追随せずビルドが通らなくなるなど、不調の原因になりやすいため採用していません。

生成コードは行数が増えますが、手で書くものではないため問題にならないと判断しています。

## 4. {テーブル名}Repository クラスの使い方

この章の例はすべて次のテーブルを使います。

```sql
create table account (
    account_id bigserial   primary key,
    name       text        not null default '',
    status     varchar     not null default 'NEW',
    created_at timestamptz not null default now(),
    note       text                                 -- null 許可
);
```

生成されるEntity は次のようになります（[5.6 Entity のnull 安全](#56-entity-のnull-安全)）。

```java
public abstract class BaseAccountEntity {
    protected @Nullable Long accountId;            // bigserial。採番前はnull
    protected String name = "";                    // 既定値で初期化
    protected String status = "NEW";               // 既定値で初期化
    protected @Nullable OffsetDateTime createdAt;  // now() は変換対象外
    protected @Nullable String note;               // null 許可
}
```

**戻り値はいずれも処理された件数です。** DB が決めた値は引数のentity へ書き戻されます。entity への反映は戻り値ではなく引数を通じて行います。

**SQL の実行に失敗した場合は例外が送出されます。** 実行はSpring JDBC に任せているため、制約違反などの例外はそちらから送出されたものがそのまま伝わります。

### 4.1 int insertAllColumns(T entity)

**全カラムをinsert の対象とします。** entity の値がそのまま送られるため、値がnull のカラムにはnull が入ります。

```java
var account = new AccountEntity();
account.setAccountId(100L);                  // PK の値を明示して登録する
account.setName("green-code-user");
account.setCreatedAt(OffsetDateTime.now());  // DB に任せず値を指定する
accountRepository.insertAllColumns(account);
// status は初期値の "NEW" がそのまま送られます
```

**全カラムを送るため、not null 制約のあるカラムはすべて値が必要です。** 既定値で初期化されるカラム（この例の `status`）はそのままで構いませんが、`created_at` のように初期値を持たないカラムは値をセットしてください。DB の既定値に任せたい場合は、次の `insertExcept()` で対象から外します。

**自動採番のPK を持つテーブルでこのメソッドを使うと、PK にnull が送られてnot null 制約違反になります。** DB に採番させる場合は [4.3 insertExceptPk()](#43-int-insertexceptpkt-entity) を使ってください。このメソッドは、PK の値を明示して投入したい場合（データ移行、初期データ投入、自然キーのテーブル）に使います。

### 4.2 int insertExcept(T entity, ColumnDefinition... columns)

**指定したカラムをinsert の対象から外します。** DB の既定値を使いたいカラムを指定してください。

外したカラムは`returning` で取得され、entity へ書き戻されます。DB が決めた値を知る手段がこれになります。

```java
var account = new AccountEntity();
account.setName("green-code-user");
// account_id の採番と created_at の now() をDB に決めさせる
accountRepository.insertExcept(account, Columns.ACCOUNT_ID, Columns.CREATED_AT);
var id = account.getAccountId();          // 採番された値
var createdAt = account.getCreatedAt();   // DB が入れた時刻
```
発行されるSQL
```sql
insert into "account" ("name", "status", "note") values (:name, :status, :note)
returning "account_id", "created_at";
```


### 4.3 int insertExceptPk(T entity)

**PK をinsert の対象から外します。** 内部で`insertExcept()` を呼ぶ短縮形で、最も使用頻度の高い形です。

```java
var account = new AccountEntity();
account.setName("green-code-user");
account.setCreatedAt(OffsetDateTime.now());  // PK 以外は値が必要
accountRepository.insertExceptPk(account);
var id = account.getAccountId(); // 採番された値
```
発行されるSQL
```sql
insert into "account" ("name", "status", "created_at", "note")
values (:name, :status, :createdAt, :note)
returning "account_id";
```

**外れるのはPK だけです。** `created_at` のようにDB へ値を決めさせたいカラムが他にもある場合は、[4.2 insertExcept()](#42-int-insertexceptt-entity-columndefinition-columns) でまとめて指定してください。

**PK を構成する全カラムをDB 側で決められるテーブルにのみ生成されます。** 具体的には、PK の各カラムが自動採番（`serial` / `bigserial` / `identity`）であるか、既定値を持つ場合です。1つでも当てはまらないカラムがPK に含まれる場合、PK を外すとnot null 制約違反になるため生成されません。

### 4.4 int updateAllColumns(T entity)

entity のプライマリーキーをキーとして、該当するレコードを1件更新します。

**PK を除く全カラムがUpdate の対象です。** entity にセットしなかったカラムはnull で上書きされます。`findByPk()` で取得したentity を変更して渡すか、[4.5 updateInclude()](#45-int-updateincludet-entity-columndefinition-columns) を使ってください。

**該当するレコードが存在しなくても例外は発生しません。** 戻り値が0 になるので、呼び出し側で判断してください。楽観ロックのように「0件が正常な結果」となる場合があるため、例外ではなく件数で伝えます。insert は1件を追加する文なので、成功すれば必ず1 になり0 にはなりません。

プライマリーキーを持たないテーブルと、**PK 以外のカラムを持たないテーブル**には生成されません。後者はset 句に含められるカラムが残らないためです。

### 4.5 int updateInclude(T entity, ColumnDefinition... columns)

**指定したカラムだけをset 句に含めます。部分更新**に利用します。

| メソッド | set 句に含めるカラム |
| --- | --- |
| `updateAllColumns` | PK を除く全カラム |
| `updateInclude` | 引数で指定したカラム |

```java
var account = new AccountEntity();
account.setAccountId(1L);
account.setName("green-code-user");
accountRepository.updateInclude(account, Columns.NAME); // name だけ更新する
```
発行されるSQL
```sql
update "account" set "name" = :name where "account_id" = :__pk1;
-- パラメータの :name は "green-code-user"
-- パラメータの :__pk1 は 1（entity のPK）
```

**対象カラムは引数だけで決まり、entity の値は判定に使いません。** 指定したカラムの値がnull ならNULL で更新します。null 許可のカラムをNULL へ戻せます。

```java
account.setNote(null);
accountRepository.updateInclude(account, Columns.NOTE); // note を NULL にする
```
発行されるSQL
```sql
update "account" set "note" = :note where "account_id" = :__pk1;
-- パラメータの :note は null
```

PK は指定できません（where 句で使うため）。指定すると`IllegalArgumentException` になります。

### 4.6 `Optional<Entity> findByPk(pk)`

プライマリーキーの1レコードを取得します。プライマリーキーを持たないテーブルには、このメソッドは生成されません。

### 4.7 int deleteByPk(pk)

プライマリーキーの1レコードを削除します。戻り値は削除された件数です。プライマリーキーを持たないテーブルには、このメソッドは生成されません。

### 4.8 class Columns

カラム定義に関する情報を持ったインスタンスが格納されています。

Columns.{カラム名大文字} でアクセスできます。（IDE の補完可）

主な情報
- columnName: カラム名
- javaPropertyName: Javaプロパティ名
- toParamColumn(): Javaプロパティ名とSQLの型キャスト。Update やInsert のSQL 中で使う。例) :colXml::xml
- toSelectColumn(): SQLのカラム名と型キャスト。Select 句のカラム指定で使う。例）col_xml::text 
- nullable: null許可判定
- hasDefault: DB カラムに初期値が定義されているか判定
- primaryKeySeq: プライマリーキーの順序。プライマリーキーでない場合はnull
- isReturning: returning で取得する対象か判定
- hasNameMapping: Java プロパティ名の明示的なマッピングを行ったカラムはtrue

### 4.9 Columns.MAP<String, ColumnDefinition>

そのテーブルが持つ全てのカラム（class Columns のインスタンス）が、カラム名とカラム定義の形式でマップとして保持されています。

### 4.10 Columns.selectAster()

全てのカラム名をカンマで区切ったものを返すメソッドです。
select * from table と書きたい時に、* の代わりにこのメソッドを使います。
カラム名に加えて型変換が付与されています。
例）col_xml::text

### 4.11 @Component RepositoryHelper

NamedParameterJdbcTemplate をラップして短く記載できるようにしたものです。

- List<T> helper.list(): 複数件取得（List&lt;Entity&gt;型）

- Optional<T> helper.optional(): 先頭1件取得（Optional&lt;Entity&gt;型）

- T helper.single(): 1件取得。1件取得できない場合は例外発生。(namedJdbc.queryForObject() を内部で使用)

- helper.exec(): namedJdbc.update() のラップ

- long helper.count(): 数値1カラムを取得するselect 文が対象。select count(*) ... を想定

- static helper.pickBySeed(): enum の定数をseed で選ぶ。生成されるテストコードが使用します

- static helper.escapeLike(): LIKE 検索のパターンに含まれる `%` `_` をエスケープします。詳しくは「8.8 LIKE 検索を行う」を参照ください

### 4.12 MAPPER (RowMapper)

明示的な命名を行なったテーブルのみ作成されます。詳しくは「5.4 カラム名とJava プロパティ名の明示的なマッピング」を参照ください。

## 5. 便利な使い方

### 5.1 Enum 型を追加する

param.yml のenumJavaTypeMappings に設定を入れてCI ツールを実行します。

設定例
- DB enum 名 : todo_status
- Java Enum クラス : jp.green_code.todo.enums.TodoStatusEnum

このように記載します。
```yml
# param.yml
enumJavaTypeMappings:
  todo_status: jp.green_code.todo.enums.TodoStatusEnum
```
テーブルを問わず、todo_status 型カラムは全てこのEnum クラスにマッピングされます。

ご参考（todo_status のDDL とJava のEnum クラス）
```sql
-- todo_status DDL 文
CREATE TYPE todo_status AS ENUM ('NEW', 'DOING', 'DONE', 'DELETED');
```
```java
// TodoStatusEnum.java
public enum TodoStatusEnum { NEW, DOING, DONE, DELETED; }
```

### 5.2 作成者カラム、作成日時カラムをUpdate させたくない

作成者カラム、作成日時カラムのように、初回Insert 時以外は更新を行わないカラムは、**データベースのトリガーで元の値へ戻してください。**

v2 までは param.yml の `excludeUpdateColumnsByTable` で Update 文の set 句から除外していましたが、v3 で廃止しました。生成された `update()` を通さずに `helper.exec()` で SQL を手書きすれば更新できてしまい、保証にならないためです。トリガーであれば経路を問わず必ず適用されます。

```sql
create or replace function refresh_meta_columns()
returns trigger as $$
begin
    -- 常に updated_at を現在時刻にする
    new.updated_at = now();
    if (tg_op = 'INSERT') then
        new.created_at = now();
    elsif (tg_op = 'UPDATE') then
        -- 更新時は変更前の値を維持して上書きを防ぐ
        new.created_at = old.created_at;
        new.created_by = old.created_by;
    end if;
    return new;
end;
$$ language plpgsql;

create trigger refresh_meta_columns_trigger
    before insert or update on account
    for each row execute function refresh_meta_columns();
```

[4.5 updateInclude()](#45-int-updateincludet-entity-columndefinition-columns) で更新対象を絞る方法もありますが、トリガーであれば経路を問わず必ず適用されます。両方を併用しても構いません。

トリガーが書き換えた値をentity へ反映したい場合は、`dbDeterminedColumnsByTable` に登録してください（「5.3 トリガーが決めた値をentity へ反映したい」を参照）。登録しない場合、entity は Java でセットした値を保持したままになります。

生成されるテストコードは全カラムについて「投入した値と取得した値が一致すること」を検証するため、トリガーで書き換わるカラムがあると失敗します。実体クラスで `assert4{プロパティ名}` を override してください。

### 5.3 トリガーが決めた値をentity へ反映したい

`updated_at` のように **DB 側で値が決まるカラム**は、param.yml の `dbDeterminedColumnsByTable` に登録します。Insert / Update の後、`returning` 句で取得した値がentity にセットされます。あわせてEntity のフィールドが `@Nullable` になります。

登録が必要なのは、**値を送ってもDB 側で上書きされるカラム**だけです。insert の対象から外したカラムは、外した時点で `returning` の対象になるため登録は要りません。

```yml
# param.yml
dbDeterminedColumnsByTable:
   "*":
      - updated_at
```

**値を決めるのはトリガーです。** このツールは結果を取得するだけで、SQL に `now()` を書き込むことはしません。関数は一度だけ作成し、テーブルごとにトリガーを 1 行ずつ追加します。

```sql
-- 関数は一度だけ
create or replace function set_updated_at() returns trigger as $$
begin
    new.updated_at = now();
    return new;
end;
$$ language plpgsql;

-- テーブルごとに1行
create trigger account_set_updated_at
    before insert or update on account
    for each row execute function set_updated_at();
```

Java から値をセットしてもトリガーが上書きしますが、**上書きされた結果がentity に入る**ため、`entity.getUpdatedAt()` で確認できます。

#### v2 からの移行

v2 までは `setNowColumnsByTable` に登録したカラムのSQL を `now()` に置き換えていましたが、v3 で廃止しました。

- 実質 `updated_at` 専用の機能でした。公開日時のように「特定の操作のときだけ現在時刻にしたい」カラムには使えません
- `helper.exec()` で手書きしたSQL には効きませんでした。トリガーなら経路を問わず適用されます
- `now()` 以外の加工をするトリガーにも対応できます

「壊れていたから」ではなく「**トリガーで代替でき、そちらの方が確実だから**」廃止しています。

移行の際は、設定名を置き換えたうえでトリガーを作成してください。あわせて **`setNow` に頼って値を省略していた箇所**にご注意ください。not null かつ既定値のないカラムは、`now()` が書き込まれなくなるためNOT NULL 制約違反になります。Java 側で値をセットするか、トリガーで補ってください。

### 5.4 カラム名とJava プロパティ名の明示的なマッピング

通常、Spring Jdbc BeanPropertyRowMapper を使っているため、スネークケースのカラム名をキャメルケースのJava のプロパティ名に自動変換されます。
デフォルト変換を使わずに、任意のカラム名とJava プロパティ名を直接対応付ける場合に設定します。
テーブル名とカラム名とJava プロパティ名を下の形式で指定します。

設定例
```yml
# param.yml
columnName2javaPropertyMap:
  table_name:
    column_name: javaPropertyName
```
※ この指定を利用した場合のみ、Repository 内にRowMapper が生成されます。

### 5.5 Base クラス

Entity, Repository, TestRepository 等、いずれも Base クラスとその実体クラスという構成となっています。

ツールを実行すると、Base クラスは毎回再作成されますが、実体クラスは初回以外変更しません。

param.yml のforceOverwriteImplementation をtrue にすると実体クラスも再作成されます。（デフォルトfalse）

### 5.6 Entity のnull 安全

生成コードはJSpecify の `@Nullable` を常に出力します。設定は要りません。

**Entity のフィールドが非null になるのは、次の3条件をすべて満たすカラムだけです。**

1. not null 制約がある
2. リテラルの既定値を持ち、Java の値へ変換できる（[7. DB 型とJava 型の変換表](#7-db-型とjava-型の変換表)）
3. `dbDeterminedColumnsByTable` に登録されていない

| カラム | フィールド |
| --- | --- |
| 3条件をすべて満たす・数値 / 真偽 | プリミティブ（`long` など）＋ 既定値で初期化 |
| 3条件をすべて満たす・文字列や日付時刻などの参照型 | 非null ＋ 既定値で初期化 |
| それ以外 | `@Nullable` のラッパー型 |

```java
// account テーブル
//   account_id bigserial primary key
//   name       text not null default ''
//   updated_by bigint not null default -1
//   note       text                        （null 許可）
public abstract class BaseAccountEntity {
    protected @Nullable Long accountId;   // nextval のため変換対象外
    protected String name = "";           // 非null ＋ 既定値
    protected long updatedBy = -1L;       // プリミティブ ＋ 既定値
    protected @Nullable String note;      // null 許可
}
```

**既定値を写すだけで、値を捏造することはありません。** 初期値は、そのカラムをinsert の対象から外した場合と同じ値になります。既定値を持たないカラムには書ける初期値がないため `@Nullable` になります。

`nextval()` や `now()` のような関数の既定値は、実行のたびに値が変わるため変換しません。自動採番のPK が `@Nullable` になるのはこのためで、採番前をnull で表せます。

#### 手書きSQL で使う場合の注意

Entity は**単一テーブルの1行**を表します。not null 制約のあるカラムがプリミティブや非null で生成されるため、外部結合などでそのカラムにNULL が返る問い合わせをEntity で受けると、プリミティブではマッピングで例外になり、参照型では型と実態が食い違います。そうした問い合わせは専用のクラスで受けてください（[8.7 集計やJOIN の結果を受け取る](#87-集計やjoin-の結果を受け取る)）。

## 6. TestRepository の使い方

param.yml testTargetTable にテスト対象のテーブル名を記載するとテストコードが生成されます。
insert, select, update, select, delete, select を順番に行います。

テストデータは generateTestData4{フィールド名}() にて作成されます。
必要に応じてoverride してください。

外部キー制約があるとテストが難しくなります。依存するレコードが必要な場合は@BeforeEach などを使って作成する必要があります。

データの確認は assert4{フィールド名}() にて行います。こちらも必要に応じてoverride してください。

### 6.1 テストデータ作成で固定値を指定したい

generateTestData4{フィールド名}() をoverride することで実現できます。

例）Base クラス
```java
// TestBaseAccountRepository.java
public AccountEntity generateTestData(int seed) {
    var entity = new AccountEntity();
    entity.setAccountId(generateTestData4accountId(seed++));
    // 中略
    return entity;
}
protected Long generateTestData4accountId(int seed) {
   return (long) seed;
}
```

Override した実体クラス
```java
// TestAccountRepository.java
@Override
protected Long generateTestData4updatedBy(int seed) {
    return -1L; // 固定値
}
```

## 7. DB 型とJava 型の変換表

| 区分     | PostgreSQL 型                | Java 型                   | 備考              |
|--------|-----------------------------|--------------------------|-----------------|
| 数値     | smallint                    | java.lang.Short          |                 |
| 数値     | int2                        | java.lang.Short          |                 |
| 数値     | smallserial                 | java.lang.Short          |                 |
| 数値     | integer                     | java.lang.Integer        |                 |
| 数値     | int4                        | java.lang.Integer        |                 |
| 数値     | serial                      | java.lang.Integer        |                 |
| 数値     | bigint                      | java.lang.Long           |                 |
| 数値     | int8                        | java.lang.Long           |                 |
| 数値     | bigserial                   | java.lang.Long           |                 |
| 数値     | real                        | java.lang.Float          |                 |
| 数値     | float4                      | java.lang.Float          |                 |
| 数値     | double precision            | java.lang.Double         |                 |
| 数値     | float8                      | java.lang.Double         |                 |
| 数値     | numeric                     | java.math.BigDecimal     |                 |
| 論理     | boolean                     | java.lang.Boolean        |                 |
| 論理     | bool                        | java.lang.Boolean        |                 |
| 文字列    | character                   | java.lang.String         |                 |
| 文字列    | bpchar                      | java.lang.String         |                 |
| 文字列    | character varying           | java.lang.String         |                 |
| 文字列    | varchar                     | java.lang.String         |                 |
| 文字列    | text                        | java.lang.String         |                 |
| 日付     | date                        | java.time.LocalDate      |                 |
| 時刻     | time                        | java.time.LocalTime      |                 |
| 時刻     | time without time zone      | java.time.LocalTime      |                 |
| 時刻     | time with time zone         | java.time.OffsetTime     |                 |
| 時刻     | timetz                      | java.time.OffsetTime     |                 |
| 時刻     | timestamp                   | java.time.LocalDateTime  |                 |
| 時刻     | timestamp without time zone | java.time.LocalDateTime  |                 |
| 時刻     | timestamp with time zone    | java.time.OffsetDateTime |                 |
| 時刻     | timestamptz                 | java.time.OffsetDateTime |                 |
| 時刻     | interval                    | java.lang.Long           | 秒（epoch）扱いが難しい  |
| バイナリ   | bytea                       | byte[]                   |                 |
| 文字列    | uuid                        | java.util.UUID           |                 |
| 文字列    | json                        | java.lang.String         | INSERT 時 ::jsonb |
| 文字列    | jsonb                       | java.lang.String         | INSERT 時 ::jsonb |
| 文字列    | xml                         | java.lang.String         | INSERT 時 ::xml  |
| ネットワーク | inet                        | java.lang.String         | INSERT 時 ::inet |
| ネットワーク | cidr                        | java.lang.String         | INSERT 時 ::cidr |
| ネットワーク | macaddr                     | java.lang.String         | INSERT 時 ::macaddr |
| 幾何     | point                       | java.lang.String         | SELECT 時 ::text |
| 幾何     | line                        | java.lang.String         | SELECT 時 ::text |
| 幾何     | lseg                        | java.lang.String         | SELECT 時 ::text |
| 幾何     | box                         | java.lang.String         | SELECT 時 ::text |
| 幾何     | path                        | java.lang.String         | SELECT 時 ::text |
| 幾何     | polygon                     | java.lang.String         | SELECT 時 ::text |
| 幾何     | circle                      | java.lang.String         | SELECT 時 ::text |

※ null を扱うため、primitive 型は使用しません。

### 7.1 既定値の変換に対応する型

DB の既定値をEntity のフィールドの初期値へ変換できるのは、次のJava 型だけです（[5.6 Entity のnull 安全](#56-entity-のnull-安全)）。

| Java 型 | 既定値の例 | 生成される初期値 |
| --- | --- | --- |
| `java.lang.String` | `'X'::text` | `"X"` |
| `java.lang.Short` / `Integer` / `Long` | `'-1'::integer` | `-1L` |
| `java.lang.Boolean` | `true` | `true` |
| `java.lang.Float` / `Double` | `1.5` | `1.5d` |
| `java.math.BigDecimal` | `0.5` | `new BigDecimal("0.5")` |
| enum | `'NEW'::status_enum` | `StatusEnum.NEW` |
| `java.time.LocalDate` / `LocalTime` / `LocalDateTime` | `'2000-01-01'::date` | `LocalDate.parse("2000-01-01")` |
| `java.time.OffsetTime` / `OffsetDateTime` | `'2000-01-01 00:00:00+09'::timestamptz` | `OffsetDateTime.parse("2000-01-01T00:00+09:00")` |

**`byte[]`（bytea）と `interval` は対象外です。** 既定値を持っていても初期化せず、そのカラムは `@Nullable` になります。

受け入れる既定値の形は「クォート済みリテラル（型キャストは任意）」と「数値・真偽のリテラル」の2つだけです。`nextval()` のような関数呼び出しは変換しません。形が合っていても変換に失敗する値（`'infinity'::timestamptz` など）は、生成時に検出して `@Nullable` に落とします。

**タイムゾーンを持つ型の初期値は、生成に使うDB のTimeZone 設定に依存します。** 同じDDL でも設定が違えば `+09:00` と `+00:00` のように表記が変わります（指す時刻は同じです）。

### 7.2 対応外の型

| 区分   | DB 型          | Java 型 |
|------|---------------|--------|
| 金額   | money         | 対応外    |
| 全文検索 | tsvector      | 対応外    |
| 全文検索 | tsquery       | 対応外    |
| ビット  | bit           | 対応外    |
| 内部   | pg_lsn        | 対応外    |
| 内部   | txid_snapshot | 対応外    |
| その他  | 記載のないもの       | 対応外    |

## 8. 手書きSQL の書き方

生成される Repository はプライマリーキーの操作しか持ちません。条件を指定した検索や更新、集計などは RepositoryHelper を使って手書きします。

このツールは「SQL は手で書く」前提のため、手書きが例外ではなく通常の使い方です。

動作する完全な例は [ExampleHelperUsage.java](test-app/src/test/java/jp/green_code/spring_jdbc_codegen/test_app/test/ExampleHelperUsage.java) にあります。以下の例はそこから抜粋したものです。

### 8.1 どこに書くか

Repository の実体クラスに追加します。実体クラスは初回のみ生成され、以降は上書きされないため、書いたコードが消えることはありません。

helper は Base クラスが `protected final` で保持しているため、そのまま使えます。

```java
// AccountRepository.java（実体クラス）
@Repository
public class AccountRepository extends BaseAccountRepository {
    public AccountRepository(RepositoryHelper helper) {
        super(helper);
    }

    // ここに手書きのメソッドを追加する
    public List<AccountEntity> findByStatus(String status) {
        return helper.list("""
                select %s
                from account
                where status = :status
                """.formatted(Columns.selectAster()),
                Map.of("status", status), AccountEntity.class);
    }
}
```

### 8.2 SQL の組み立て方

helper のメソッドは SQL を `String` と `List<String>` のどちらでも受け取ります。

- **固定の SQL** はテキストブロック（`"""`）で書きます。そのまま psql に貼れる形になります
- **条件によって変わる SQL** は `List<String>` で組み立てます。要素は半角スペースで連結されます

パラメータは `Map` で渡します。SQL 中では `:name` の形で参照します（Spring JDBC の名前付きパラメータ）。

両方を組み合わせることもできます。固定部分をテキストブロックで書き、条件だけを行として足す形です。

```java
var sql = new ArrayList<String>();
sql.add("""
        select %s
        from account
        where 1 = 1
        """.formatted(Columns.selectAster()));
var param = new HashMap<String, Object>();

if (name != null) {
    sql.add("and name like :name");
    param.put("name", RepositoryHelper.escapeLike(name) + "%");
}
sql.add("order by account_id");

var list = helper.list(sql, param, AccountEntity.class);
```

`where 1 = 1` を置いておくと、条件が 0 個でも 1 個でも `and` を足すだけで済みます。

### 8.3 select 句には Columns.selectAster() を使う

`select *` と書かず `Columns.selectAster()` を使ってください。`interval` 型のように SELECT 時の型変換が必要なカラムを正しく取得するためです。詳しくは「4.10 Columns.selectAster()」を参照してください。

### 8.4 条件を指定して更新する、削除する

`helper.exec()` を使います。戻り値は更新（削除）された件数です。

```java
var count = helper.exec("""
        update account
        set status = :newStatus
        where status = :oldStatus
        """, Map.of("newStatus", "DONE", "oldStatus", "DOING"));
```

```java
var count = helper.exec("""
        delete from account
        where status = :status
        """, Map.of("status", "DELETED"));
```

### 8.5 更新前の値を条件にする（楽観ロック）

`update()` は プライマリーキーだけを条件にするため、他者が先に更新したことを検知できません。楽観ロックが必要な場合は、更新前の値を where 句に含めて手書きします。

更新件数が 0 であれば、読み込んでから更新するまでの間に他者が更新したことになります。

```java
var sql = """
        update account
        set status = :newStatus, version = version + 1
        where account_id = :accountId
          and version = :expectedVersion
        """;
var count = helper.exec(sql, Map.of(
        "newStatus", "DONE",
        "accountId", accountId,
        "expectedVersion", expectedVersion));

if (count == 0) {
    throw new OptimisticLockException(); // 競合を検知
}
```

### 8.6 件数を取得する、単一カラムを取得する

数値 1 カラムの select 文は `helper.count()` で取得します。戻り値は `long` です。

```java
var count = helper.count("""
        select count(*) from account
        where status = :status
        """, Map.of("status", "DOING"));
```

Class 引数に数値型や String を渡すと、Entity ではなくその型のリストが返ります。

```java
List<Long> idList = helper.list("""
        select account_id from account
        where status = :status
        order by account_id
        """, Map.of("status", "DOING"), Long.class);
```

### 8.7 集計やJOIN の結果を受け取る

Entity に当てはまらない結果は、受け取り用のクラスを用意すれば取得できます。setter が必要です。

BeanPropertyRowMapper が動くため、スネークケースの列名はキャメルケースのプロパティへ自動で変換されます。列名が合わない場合は `as` で別名を付けてください。

```java
public class AccountSummary {
    private String status;
    private Long rowCount;   // row_count が入る
    // getter / setter
}
```

```java
List<AccountSummary> list = helper.list("""
        select a.status, count(t.todo_id) as row_count
        from account a
        left join todo t on t.account_id = a.account_id
        group by a.status
        """, Map.of(), AccountSummary.class);
```

### 8.8 LIKE 検索を行う

**バインド変数は LIKE のワイルドカードを無害化しません。** 値は SQL の構文としては安全に渡りますが、渡った先でパターンとして解釈されます。

```java
// 誤り。keyword に "search_word" が来ると _ が任意の 1 文字として働き、
// "search word" や "searchXword" までヒットする
helper.list("""
        select %s from account
        where note like concat('%%', :keyword, '%%')
        """.formatted(Columns.selectAster()),
        Map.of("keyword", keyword), AccountEntity.class);
```

`escapeLike()` を通してから渡してください。

```java
helper.list("""
        select %s from account
        where note like concat('%%', :keyword, '%%')
        """.formatted(Columns.selectAster()),
        Map.of("keyword", RepositoryHelper.escapeLike(keyword)), AccountEntity.class);
```

`escapeLike()` は前後の `%` を付けません。部分一致・前方一致のどちらにするかは呼び出し側で決めてください。

```java
param.put("keyword", RepositoryHelper.escapeLike(keyword) + "%");   // 前方一致
```

前方一致であれば `text_pattern_ops` のインデックスが効きます。`%` で始まる部分一致は常に全件走査になります。

#### エスケープ文字を変える

既定のエスケープ文字は `\` です。PostgreSQL の LIKE は `escape` 句を省略したときのエスケープ文字が `\` のため、既定を使う限り SQL 側に `escape` 句は要りません。

第 2 引数で変更できます。**その場合は SQL に `escape` 句を書いてください。** 書き忘れると PostgreSQL は既定の `\` で解釈するため、エスケープが効かず静かに誤った結果になります。

```java
helper.list("""
        select %s from account
        where note like concat('%%', :keyword, '%%') escape '$'
        """.formatted(Columns.selectAster()),
        Map.of("keyword", RepositoryHelper.escapeLike(keyword, '$')), AccountEntity.class);
```

`%` と `_` はエスケープ文字に指定できません。渡すと `IllegalArgumentException` が発生します。
