# spring jdbc codegen

## 1. 機能概要

spring-jdbc-codegen は、Spring JDBC + PostgreSQL 環境において
「SQL は手で書きたいが、定型的な Entity / Repository / Test を自動生成したい」
という前提で作られたコード生成用 CLI ツールです。

- コマンドライン実行型
- Entity, Repository, TestRepository 自動生成
- Spring JDBC 前提
- PostgreSQL のみ対応
- Enum 対応可能
- Update Insert 除外カラム指定可能

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

## 3.4 動作確認済みの構成

- Java 21
- Spring Boot 3.5.10
- Spring JDBC
- PostgreSQL 17

## 4. {テーブル名}Repository クラスの使い方

### 4.1 T insert(T entity)

1レコードのinsert を行います。

not null 制約ありかつ初期値を持つカラムに対して、entity 中のフィールドの値がnull であった場合は、 insert 対象から外されます。外されたカラムは、DB カラムに定義された初期値がセットされます。insert が終わるとその初期値が引数 entity へセットされます。プライマリーキーの自動採番などはinsert 後のentity から取得できます。

```java
// java
var account = new AccountEntity();
// account_id はbigserial 型なので、省略時は自動採番されます
account.setAccountId(null);
account.setName("green-code-user");
accountRepository.insert(account);
var id = account.getAccountId(); // 自動採番されたPK を取得
```
```sql
-- Spring JDBC に渡されるSQL
insert into account (name) values (":name");
-- パラメータの :name は "green-code-user" 
```

### 4.2 T update(T entity)

entity のプライマリーキーをキーとして、該当するレコードを1件更新します。 該当するレコードが存在しない場合は例外(EmptyResultDataAccessException)をスローします。プライマリーキーを持たないテーブルには、このメソッドは生成されません。

### 4.3 Optional&lt;Entity&gt; findByPk(pk)

プライマリーキーの1レコードを取得します。プライマリーキーを持たないテーブルには、このメソッドは生成されません。

### 4.4 int deleteByPk(pk)

プライマリーキーの1レコードを削除します。戻り値は削除された件数です。プライマリーキーを持たないテーブルには、このメソッドは生成されません。

### 4.5 class Columns

カラム定義に関する情報を持ったインスタンスが格納されています。

Columns.{カラム名大文字} でアクセスできます。（IDE の補完可）

主な情報
- columnName: カラム名
- javaPropertyName: Javaプロパティ名
- toParamColumn(): Javaプロパティ名とSQLの型キャスト。Update やInsert のSQL 中で使う。例) :colXml:xml
- toSelectColumn(): SQLのカラム名と型キャスト。Select 句のカラム指定で使う。例）col_xml::text 
- nullable: null許可判定
- isSetNow: now()を設定するか判定
- shouldSkipInInsert: Insert 対象外カラム判定
- shouldSkipInUpdate: Update 対象外カラム判定
- hasNameMapping: Java プロパティ名の明示的なマッピングを行ったカラムはtrue

### 4.6 Columns.MAP<String, ColumnDefinition>

そのテーブルが持つ全てのカラム（class Columns のインスタンス）が、カラム名とカラム定義の形式でマップとして保持されています。

### 4.7 Columns.selectAster()

全てのカラム名をカンマで区切ったものです。
select * from table と書きたい時に、* の代わりにこの定数を使います。
カラム名に加えて型変換が付与されています。
例）col_xml::text

### 4.8 @Component RepositoryHelper

NamedParameterJdbcTemplate をラップして短く記載できるようにしたものです。

- List<T> helper.list(): 複数件取得（List&lt;Entity&gt;型）

- Optional<T> helper.optional(): 先頭1件取得（Optional&lt;Entity&gt;型）

- T helper.single(): 1件取得。1件取得できない場合は例外発生。(namedJdbc.queryForObject() を内部で使用)

- helper.exec(): namedJdbc.update() のラップ

- helper.count(): Long 型の1カラムを取得するselect 文が対象。select count(*) ... を想定

### 4.9 MAPPER (RowMapper)

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

### 5.2 作成者カラム、作成日時カラムをUpdate から除外する

作成者カラム、作成日時カラムのように、初回Insert 時以外は更新を行わないカラムについては、param.yml excludeUpdateColumnsByTable に登録します。
登録されたカラムはUpdate 時に更新されなくなります。

設定例
- 作成者カラム : created_by
- 作成日時カラム : created_at
```yml
# param.yml
excludeUpdateColumnsByTable:
   "*":
      - created_at
      - created_by
```
"*" は全てのテーブルを意味します。個別のテーブルを指定する場合は、テーブル名を記載します。

### 5.3 Insert, Update 時にデータベースの時刻 now() を指定したい

param.yml のsetNowColumnsByTable に設定すると、そのカラムの値はSQL の now() に置き換わります。
指定されたカラムはrepository.insert() またはrepository.update() でJava で値を指定することができなくなります。
また、Insert やUpdate 完了時に、DB でセットされた時刻を引数のentity にセットします。

設定例
```yml
# param.yml
setNowColumnsByTable:
   "*":
      - updated_at
      - created_at
```
発行されるSQL
```sql
update account set updated_at = now(), created_at = now() where ...
```

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

## 6 TestRepository の使い方

param.yml testTargetTable にテスト対象のテーブル名を記載するとテストコードが生成されます。
insert, select, update, select, delete, select を順番に行います。

テストデータは generateTestData4{フィールド名}() にて作成されます。
必要に応じてoverride してください。

外部キー制約があるとテストが難しくなります。依存するレコードが必要な場合は@BeforeEach などを使って作成する必要があります。

データの確認は assert4{フィールド名}() にて行います。こちらも必要に応じてoverride してください。

### 6.2 テストデータ作成で固定値を指定したい

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
