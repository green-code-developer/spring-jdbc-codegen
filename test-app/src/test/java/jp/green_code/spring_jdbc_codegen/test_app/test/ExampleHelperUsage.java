package jp.green_code.spring_jdbc_codegen.test_app.test;

import jp.green_code.spring_jdbc_codegen.test_app.entity.NormalPk1Entity;
import jp.green_code.spring_jdbc_codegen.test_app.repository.NormalPk1Repository;
import jp.green_code.spring_jdbc_codegen.test_app.repository.RepositoryHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * RepositoryHelper を使った手書きSQL の書き方。
 * <p>
 * README「8. 手書きSQL の書き方」から参照される実例。
 * ドキュメントのコード例が実装と乖離しないよう、ここで動作を保証する。
 * <p>
 * 実際の利用では、これらのメソッドは各Repository の実体クラスへ追加する。
 * 実体クラスは初回のみ生成され以降は上書きされないため、手書きのコードが消えることはない。
 */
@SpringBootTest
@Transactional // テスト終了時にロールバックするためデータが残らない
public class ExampleHelperUsage {

    @Autowired
    NormalPk1Repository repository;

    @Autowired
    RepositoryHelper helper;

    /**
     * 検証用に3件登録する。col_text は "A", "A", "B"
     */
    @BeforeEach
    void setUp() {
        insert("A", "first");
        insert("A", "second");
        insert("B", "third");
    }

    void insert(String colText, String notNull) {
        var entity = new NormalPk1Entity();
        entity.setColText(colText);
        entity.setColTextNotNull(notNull);
        repository.insert(entity);
    }

    /**
     * 条件を指定して複数件取得する。
     * <p>
     * SQL をList&lt;String&gt; で組み立てると、条件の有無で行を足し引きできる。
     * select 句には Columns.selectAster() を使う。interval のように
     * SELECT 時の型変換が必要なカラムを正しく取得するため。
     */
    @Test
    void 条件を指定して複数件取得する() {
        String colText = "A"; // null なら条件に含めない想定

        var sql = new ArrayList<String>();
        sql.add("""
                select %s
                from normal_pk1
                where 1 = 1
                """.formatted(NormalPk1Repository.Columns.selectAster()));
        var param = new HashMap<String, Object>();
        if (colText != null) {
            sql.add("and col_text = :colText");
            param.put("colText", colText);
        }
        sql.add("order by pk");

        List<NormalPk1Entity> list = helper.list(sql, param, NormalPk1Entity.class);

        assertEquals(2, list.size());
        assertEquals("first", list.get(0).getColTextNotNull());
    }

    /**
     * 条件を指定して複数件を更新する。
     * <p>
     * exec() は更新件数を返す。想定件数と一致するか確認できる。
     */
    @Test
    void 条件を指定して複数件更新する() {
        var count = helper.exec("""
                update normal_pk1
                set col_text = :newValue
                where col_text = :oldValue
                """, Map.of("newValue", "C", "oldValue", "A"));

        assertEquals(2, count);
    }

    /**
     * 更新前の値を条件に含めて、他者が更新していないことを確認する。
     * <p>
     * 楽観ロックはこの形で実現する。更新件数が0 であれば、
     * 読み込んでから更新するまでの間に他者が更新したことになる。
     */
    @Test
    void 更新前の値を条件にして更新する() {
        var sql = """
                update normal_pk1
                set col_text = :newValue
                where pk = :pk
                  and col_text = :expectedValue
                """;

        // 期待どおりの値であれば1件更新される
        var pk = helper.single("select min(pk) from normal_pk1 where col_text = :colText",
                Map.of("colText", "B"), Long.class);
        var updated = helper.exec(sql, Map.of("newValue", "D", "pk", pk, "expectedValue", "B"));
        assertEquals(1, updated);

        // すでに値が変わっているため0件。競合を検知できる
        var conflicted = helper.exec(sql, Map.of("newValue", "E", "pk", pk, "expectedValue", "B"));
        assertEquals(0, conflicted);
    }

    /**
     * 条件を指定して複数件を削除する
     */
    @Test
    void 条件を指定して複数件削除する() {
        var count = helper.exec("""
                delete from normal_pk1
                where col_text = :colText
                """, Map.of("colText", "A"));

        assertEquals(2, count);
    }

    /**
     * 件数を取得する。
     * <p>
     * count() は数値1カラムのselect 文が対象。戻り値はlong。
     */
    @Test
    void 件数を取得する() {
        var count = helper.count("""
                select count(*) from normal_pk1
                where col_text = :colText
                """, Map.of("colText", "A"));

        assertEquals(2L, count);
    }

    /**
     * 単一カラムだけを取得する。
     * <p>
     * 数値型やString をClass 引数に渡すと、Entity ではなくその型のリストが返る。
     */
    @Test
    void 単一カラムだけ取得する() {
        List<Long> pkList = helper.list("""
                select pk from normal_pk1
                where col_text = :colText
                order by pk
                """, Map.of("colText", "A"), Long.class);

        assertEquals(2, pkList.size());
        assertTrue(pkList.get(0) < pkList.get(1));
    }

    /**
     * 集計やJOIN の結果を、Entity ではない任意のクラスで受け取る。
     * <p>
     * BeanPropertyRowMapper が動くため、スネークケースの列名は
     * キャメルケースのプロパティへ自動でマッピングされる。
     * 別名(as) を付けて列名を合わせる。
     */
    @Test
    void 集計結果を独自クラスで受け取る() {
        List<TextCount> list = helper.list("""
                select col_text as text_value, count(*) as row_count
                from normal_pk1
                group by col_text
                order by col_text
                """, Map.of(), TextCount.class);

        assertEquals(2, list.size());
        assertEquals("A", list.get(0).getTextValue());
        assertEquals(2L, list.get(0).getRowCount());
    }

    /**
     * LIKE 検索のワイルドカードはバインド変数では無害化されない。
     * <p>
     * escapeLike() を通さないと、値に含まれる _ が任意の1文字として働く。
     */
    @Test
    void LIKE検索でワイルドカードをエスケープする() {
        insert("a_c", "like1");
        insert("abc", "like2");

        // エスケープしないと _ がワイルドカードになり abc までヒットする
        assertEquals(2, countByLike("a_c", ""));

        // エスケープすれば a_c だけがヒットする
        assertEquals(1, countByLike(RepositoryHelper.escapeLike("a_c"), ""));
    }

    /**
     * エスケープ文字自身も置換の対象になる。
     * <p>
     * 置換の順序を誤ると二重にエスケープされ、この検索は 0 件になる。
     */
    @Test
    void エスケープ文字自身もエスケープされる() {
        insert("a\\_c", "like3");
        insert("a_c", "like4");

        assertEquals(1, countByLike(RepositoryHelper.escapeLike("a\\_c"), ""));
    }

    /**
     * エスケープ文字は第2引数で変更できる。
     * <p>
     * 変更した場合は SQL に escape 句を書く必要がある。書き忘れると
     * PostgreSQL は既定の \ で解釈するためエスケープが効かない。
     */
    @Test
    void エスケープ文字を変更する() {
        insert("a%c", "like5");
        insert("abc", "like6");

        assertEquals(1, countByLike(RepositoryHelper.escapeLike("a%c", '$'), "escape '$'"));
    }

    /**
     * ワイルドカード自身をエスケープ文字にはできない。
     */
    @Test
    void エスケープ文字にワイルドカードは指定できない() {
        assertThrows(IllegalArgumentException.class, () -> RepositoryHelper.escapeLike("a_c", '_'));
    }

    /**
     * col_text を LIKE 検索した件数を返す。escapeClause は "escape '$'" など。既定なら空文字
     */
    long countByLike(String pattern, String escapeClause) {
        return helper.count("""
                select count(*)
                from normal_pk1
                where col_text like :keyword %s
                """.formatted(escapeClause), Map.of("keyword", pattern));
    }

    /**
     * 集計結果の受け取り用クラス。setter が必要
     */
    public static class TextCount {
        private String textValue;
        private Long rowCount;

        public String getTextValue() {
            return textValue;
        }

        public void setTextValue(String textValue) {
            this.textValue = textValue;
        }

        public Long getRowCount() {
            return rowCount;
        }

        public void setRowCount(Long rowCount) {
            this.rowCount = rowCount;
        }
    }
}
