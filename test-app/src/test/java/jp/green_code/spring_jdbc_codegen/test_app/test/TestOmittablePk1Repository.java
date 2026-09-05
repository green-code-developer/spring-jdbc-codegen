package jp.green_code.spring_jdbc_codegen.test_app.test;

import jp.green_code.spring_jdbc_codegen.test_app.entity.OmittablePk1Entity;
import jp.green_code.spring_jdbc_codegen.test_app.repository.OmittablePk1Repository;
import jp.green_code.spring_jdbc_codegen.test_app.repository.base.BaseOmittablePk1Repository.Columns;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class TestOmittablePk1Repository {

    @Autowired
    OmittablePk1Repository repository;

    @Test
    void test() {
        var data = new OmittablePk1Entity();
        // 全カラムを除外してDB の採番と既定値に任せる
        // 戻り値は件数。DB が決めた値は引数のentity に書き戻される
        assertEquals(1, repository.insertExcept(data, Columns.PK, Columns.COL_TEXT_NOT_NULL_DEFAULT_X));
        assertNotNull(data.getPk());
        assertNotNull(data.getColTextNotNullDefaultX());
    }

    /**
     * 該当レコードがなくても例外は送出せず 0 を返す。
     * returning がないテーブルは helper.exec() で実行する経路。
     */
    @Test
    void 存在しないPKへのupdateは0件を返す() {
        var data = new OmittablePk1Entity();
        data.setPk(Long.MAX_VALUE);
        data.setColTextNotNullDefaultX("x");
        assertEquals(0, repository.updateAllColumns(data));
    }
}