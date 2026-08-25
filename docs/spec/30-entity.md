# 30 Entity

テーブル 1 つにつき Entity を 1 つ生成する。実装は `BaseEntityGenerator` と
`EntityGenerator`。

## ENTITY-001 クラス構成

| クラス | パッケージ | 内容 |
| --- | --- | --- |
| `Base{テーブル}Entity` | `{entityPackage}.{base}` | 抽象クラス。フィールドと getter / setter |
| `{テーブル}Entity` | `{entityPackage}` | Base を継承した空のクラス |

Base クラスは毎回再生成し、実体クラスは初回のみ生成する（[CORE-005](00-overview.md)）。
利用者が独自のメソッドを追加する場所は実体クラス。

## ENTITY-002 クラスコメント

Base クラスには元のテーブル名をコメントとして出力する。

```java
/**
 * Table: account
 */
public abstract class BaseAccountEntity {
```

## ENTITY-010 フィールド

DB のカラム順（`ORDINAL_POSITION` 順）にフィールドを定義する。

- アクセス修飾子は `protected`
- 型は [20-type-mapping.md](20-type-mapping.md) に従う
- 名前は [NAMING-002](40-naming.md) に従う
- 直前に元のカラム名をコメントとして出力する

```java
/** account_id */
protected Long accountId;
```

**すべてのフィールドはラッパー型**を使う（`long` ではなく `Long`）。not null 制約の
有無にかかわらず null を保持できる。INSERT 時の省略判定（[REPO-011](31-repository.md)）が
null か否かで動作するため。

例外は `bytea` に対応する `byte[]` のみ。

## ENTITY-011 getter と setter

全フィールドについて `get{名前}` / `set{名前}` を生成する。名前は Java プロパティ名の
先頭を大文字にしたもの。

- 真偽値でも `is` ではなく `get` を使う
- 全フィールドの getter を出力した後に setter を出力するのではなく、
  フィールドごとに getter / setter の順で出力する

## ENTITY-020 import

フィールドの型のうち、パッケージを持つものだけを import する。
重複を除去し、辞書順に並べる。

`java.lang` パッケージの型も import する（`import java.lang.Long;`）。
プリミティブ型と配列型（`byte[]`）は import しない。

## ENTITY-030 @NullUnmarked

`enableNullUnmarkedForEntityPackages: true`（[PARAM-009](10-param.md)）のとき、
次の 2 つのパッケージに `package-info.java` を生成する。

- `{entityPackage}.{base}` — 毎回上書きする
- `{entityPackage}` — 初回のみ生成する

内容は次のとおり。`@NullUnmarked` の FQCN は `nullUnmarkedFqcn` で差し替えられる。

```java
@NullUnmarked
package jp.green_code.example.entity;

import org.jspecify.annotations.NullUnmarked;
```

**Entity のフィールドや getter / setter に `@Nullable` は付与しない。**
パッケージ単位で `@NullUnmarked` にすることで null 許容を表現する。

`@Nullable` を出力するのは Repository 側だけで、対象は次の 2 つ。
両者は出力条件が異なる。

| 対象 | 条件 |
| --- | --- |
| `ColumnDefinition` と `BaseColumnDefinition`（[REPO-050](31-repository.md)） | `enableNullUnmarkedForEntityPackages: true` のときだけ |
| RowMapper を生成した Repository（[REPO-051](31-repository.md)） | 命名マッピングがあれば無条件 |
