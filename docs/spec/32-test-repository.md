# 32 TestRepository

`testTargetTable` に指定したテーブルについて、CRUD を一巡する JUnit テストを生成する。
実装は `TestBaseRepositoryGenerator` と `TestRepositoryGenerator`。

## TESTREPO-001 生成条件

`testTargetTable`（[PARAM-003](10-param.md)）に列挙されたテーブルのみが対象。
指定がなければ何も生成しない。

PK を持たないテーブルも生成する。ただし `findByPk` / `deleteByPk` が存在せず
検証できないため、`test()` の中身は insert のみになる。

実在しないテーブル名を指定しても生成は中断しないが、警告が出力され
終了コードは 2 になる（[PARAM-020](10-param.md)）。

## TESTREPO-002 クラス構成

| クラス | 出力先 | 内容 |
| --- | --- | --- |
| `TestBase{テーブル}Repository` | テスト側 `{repositoryPackage}.{base}` | 抽象クラス。テスト本体 |
| `Test{テーブル}Repository` | テスト側 `{repositoryPackage}` | `@SpringBootTest` を付けた実体クラス |

実体クラスが Repository を `@Autowired` し、Base の `test()` を呼ぶ。
利用者はここでテストデータや検証内容を override する。

## TESTREPO-010 テストの流れ

`test()` は 1 つのレコードに対して次を順に実行する。

1. seed 1 でテストデータを生成し、**insert 省略可能な PK**（[REPO-011](31-repository.md)）を
   null にして `insert`
2. `findByPk` で取得できることを確認
3. insert した値と取得した値を比較
4. seed 2 でテストデータを生成し、PK を 1 で採番された値に差し替えて `update`
5. `findByPk` で取得し、update した値と比較
6. `deleteByPk` で 1 件削除されることを確認
7. `findByPk` が空を返すことを確認

1 で null にするのは PK のうち「not null かつ既定値を持つ」カラムだけで、
DB の自動採番を働かせるため。それ以外の PK は生成した値のまま insert する。

次の場合は 1 の insert までで終わり、2 以降を行わない。

- PK を持たないテーブル（[TESTREPO-001](#testrepo-001-生成条件)）
- 全カラムが UPDATE 対象外のテーブル。`update` が生成されないため
  （[REPO-002](31-repository.md)）

## TESTREPO-011 検証の対象

**全カラムを検証する。** ただし update 後の比較対象がカラムによって異なる。

| カラム | insert 後の比較 | update 後の比較 |
| --- | --- | --- |
| 通常 | 投入した値 と 取得した値 | 投入した値 と 取得した値 |
| UPDATE 対象外 | 投入した値 と 取得した値 | **insert 後の値 と 取得した値** |

UPDATE 対象外カラム（`excludeUpdateColumnsByTable`）は、update しても値が変わらない
ことを確認する。

`setNowColumnsByTable` 対象のカラムも同じ規則で検証する。insert / update の実行時に
DB がセットした値が entity へ書き戻される（[REPO-012](31-repository.md)）ため、
投入した値との比較が成立する。

## TESTREPO-020 テストデータの生成

```java
public {テーブル}Entity generateTestData(int seed)
protected {型} generateTestData4{プロパティ名}(int seed)
```

`generateTestData()` が全フィールドについて `generateTestData4{プロパティ名}()` を
呼ぶ。seed はフィールドごとに 1 ずつ増える。

型ごとの生成式は [TYPE-002](20-type-mapping.md) の「テストデータ生成」に従う。
固定値を使いたい場合は実体クラスで `generateTestData4{プロパティ名}()` を override する。

## TESTREPO-021 検証メソッド

```java
protected void assert4{プロパティ名}({型} expected, {型} value)
```

フィールドごとに生成する。既定は `assertEquals`。型によって異なる検証を行う場合がある
（文字列は `trim()` 後に比較、`BigDecimal` は `compareTo`、`byte[]` は
`assertArrayEquals`）。

検証内容を変えたい場合は実体クラスで override する。

## TESTREPO-030 外部キー制約への対応

依存レコードの作成は行わない。外部キー制約を持つテーブルでは、実体クラスに
`@BeforeEach` を書いて依存レコードを用意する必要がある。

## TESTREPO-031 テスト後のデータ

生成されたテストは最後に `deleteByPk` するため、レコードを残さない。

ただし実体クラスを override して PK を変更した場合や、テストが途中で失敗した場合は
データが残る。検証用 DB は使い捨てとし、DDL を変更したときは作り直す。
