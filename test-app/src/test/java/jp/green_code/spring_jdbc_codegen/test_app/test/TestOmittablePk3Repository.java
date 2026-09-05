package jp.green_code.spring_jdbc_codegen.test_app.test;

import jp.green_code.spring_jdbc_codegen.test_app.entity.OmittablePk3Entity;
import jp.green_code.spring_jdbc_codegen.test_app.repository.OmittablePk3Repository;
import jp.green_code.spring_jdbc_codegen.test_app.repository.base.BaseOmittablePk3Repository.Columns;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class TestOmittablePk3Repository {

    @Autowired
    OmittablePk3Repository repository;

    @Test
    void test() {
        var data = new OmittablePk3Entity();
        // 全カラムを除外してDB の採番と既定値に任せる
        // 戻り値は件数。DB が決めた値は引数のentity に書き戻される
        assertEquals(1, repository.insertExcept(data, Columns.PK1, Columns.PK2, Columns.PK3, Columns.COL_TEXT_NOT_NULL_DEFAULT_X));
        assertNotNull(data.getPk1());
        assertNotNull(data.getPk2());
        assertNotNull(data.getPk3());
        assertNotNull(data.getColTextNotNullDefaultX());
    }
}