# spring jdbc codegen

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
  - [4.1 T insert(T entity)](#41-t-insertt-entity)
  - [4.2 T update(T entity)](#42-t-updatet-entity)
  - [4.3 T updateByPk(T entity, pk)](#43-t-updatebypkt-entity-pk)
  - [4.4 `Optional<Entity> findByPk(pk)`](#44-optionalentity-findbypkpk)
  - [4.5 int deleteByPk(pk)](#45-int-deletebypkpk)
  - [4.6 class Columns](#46-class-columns)
  - [4.7 Columns.MAP<String, ColumnDefinition>](#47-columnsmapstring-columndefinition)
  - [4.8 Columns.selectAster()](#48-columnsselectaster)
  - [4.9 @Component RepositoryHelper](#49-component-repositoryhelper)
  - [4.10 MAPPER (RowMapper)](#410-mapper-rowmapper)
- [5. 便利な使い方](#5-便利な使い方)
  - [5.1 Enum 型を追加する](#51-enum-型を追加する)
  - [5.2 作成者カラム、作成日時カラムをUpdate から除外する](#52-作成者カラム作成日時カラムをupdate-から除外する)
  - [5.3 Insert, Update 時にデータベースの時刻 now() を指定したい](#53-insert-update-時にデータベースの時刻-now-を指定したい)
  - [5.4 カラム名とJava プロパティ名の明示的なマッピング](#54-カラム名とjava-プロパティ名の明示的なマッピング)
  - [5.5 Base クラス](#55-base-クラス)
  - [5.6 @NullMarked 対応](#56-nullmarked-対応)
- [6. TestRepository の使い方](#6-testrepository-の使い方)
  - [6.1 テストデータ作成で固定値を指定したい](#61-テストデータ作成で固定値を指定したい)
- [7. DB 型とJava 型の変換表](#7-db-型とjava-型の変換表)
  - [7.1 対応外の型](#71-対応外の型)
- [8. 手書きSQL の書き方](#8-手書きsql-の書き方)
  - [8.1 どこに書くか](#81-どこに書くか)
  - [8.2 SQL の組み立て方](#82-sql-の組み立て方)
  - [8.3 select 句には Columns.selectAster() を使う](#83-select-句には-columnsselectaster-を使う)
  - [8.4 条件を指定して更新する、削除する](#84-条件を指定して更新する削除する)
  - [8.5 更新前の値を条件にする（楽観ロック）](#85-更新前の値を条件にする楽観ロック)
  - [8.6 件数を取得する、単一カラムを取得する](#86-件数を取得する単一カラムを取得する)
  - [8.7 集計やJOIN の結果を受け取る](#87-集計やjoin-の結果を受け取る)

## 1. 機能概要

spring-jdbc-codegen は、Spring JDBC + PostgreSQL 環境において
「SQL は手で書きたいが、定型的な Entity / Repository / Test を自動生成したい」
という前提で作られたコード生成用 CLI ツールです。

- コマンドライン実行型
- Entity, Repository, TestRepository 自動生成
- Spring JDBC 前提
- PostgreSQL のみ対応
- Enum 対応可能
- Update Insert 除外カラム指定

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
| 1 | DB へ接続できない、対応していない型があるなど。コードは生成されない |
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
insert into "account" ("name") values (:name);
-- パラメータの :name は "green-code-user" 
```

### 4.2 T update(T entity)

entity のプライマリーキーをキーとして、該当するレコードを1件更新します。該当するレコードが存在しない場合は例外(EmptyResultDataAccessException)をスローします。プライマリーキーを持たないテーブルやUpdate 対象カラムが存在しないテーブルには、このメソッドは生成されません。

### 4.3 T updateByPk(T entity, pk)

pk をキーとして、該当するレコードを1件更新します。update() との違いは、entity 内のプライマリーキーをキーとしない点です。PK をUpdate する場合に使用します。その他の性質はupdate() と同じです。

### 4.4 `Optional<Entity> findByPk(pk)`

プライマリーキーの1レコードを取得します。プライマリーキーを持たないテーブルには、このメソッドは生成されません。

### 4.5 int deleteByPk(pk)

プライマリーキーの1レコードを削除します。戻り値は削除された件数です。プライマリーキーを持たないテーブルには、このメソッドは生成されません。

### 4.6 class Columns

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
- isSetNow: now()を設定するか判定
- shouldSkipInUpdate: param.yml のexcludeUpdateColumnsByTable でUpdate 対象外カラムと指定された場合true
- hasNameMapping: Java プロパティ名の明示的なマッピングを行ったカラムはtrue

### 4.7 Columns.MAP<String, ColumnDefinition>

そのテーブルが持つ全てのカラム（class Columns のインスタンス）が、カラム名とカラム定義の形式でマップとして保持されています。

### 4.8 Columns.selectAster()

全てのカラム名をカンマで区切ったものを返すメソッドです。
select * from table と書きたい時に、* の代わりにこのメソッドを使います。
カラム名に加えて型変換が付与されています。
例）col_xml::text

### 4.9 @Component RepositoryHelper

NamedParameterJdbcTemplate をラップして短く記載できるようにしたものです。

- List<T> helper.list(): 複数件取得（List&lt;Entity&gt;型）

- Optional<T> helper.optional(): 先頭1件取得（Optional&lt;Entity&gt;型）

- T helper.single(): 1件取得。1件取得できない場合は例外発生。(namedJdbc.queryForObject() を内部で使用)

- helper.exec(): namedJdbc.update() のラップ

- long helper.count(): 数値1カラムを取得するselect 文が対象。select count(*) ... を想定

### 4.10 MAPPER (RowMapper)

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

### 5.6 @NullMarked 対応

@NullMarked を使用しているプロジェクトの場合、Entity のパッケージに @NullUnmarked を付与する必要があります。

Entity クラスでは、データベースの値などを扱うためnull を許容するケースがあるためです。

こちらの設定を入れると、{entityPackage} と{entityPackage}.base にpackage-info.java ファイルを作成し、@NullUnmarked を付与します。

設定例
```yml
# param.yml
enableNullUnmarkedForEntityPackages: true
```

また、BaseRepository 内のカラム定義などに @Nullable をfウヨします。


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

### 7.1 対応外の型

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
    param.put("name", name + "%");
}
sql.add("order by account_id");

var list = helper.list(sql, param, AccountEntity.class);
```

`where 1 = 1` を置いておくと、条件が 0 個でも 1 個でも `and` を足すだけで済みます。

### 8.3 select 句には Columns.selectAster() を使う

`select *` と書かず `Columns.selectAster()` を使ってください。`interval` 型のように SELECT 時の型変換が必要なカラムを正しく取得するためです。詳しくは「4.8 Columns.selectAster()」を参照してください。

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
