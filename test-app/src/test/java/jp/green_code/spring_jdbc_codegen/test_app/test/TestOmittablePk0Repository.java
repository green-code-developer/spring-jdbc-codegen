package jp.green_code.spring_jdbc_codegen.test_app.test;

import jp.green_code.spring_jdbc_codegen.test_app.entity.OmittablePk0Entity;
import jp.green_code.spring_jdbc_codegen.test_app.repository.OmittablePk0Repository;
import jp.green_code.spring_jdbc_codegen.test_app.repository.base.BaseOmittablePk0Repository.Columns;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class TestOmittablePk0Repository {

    @Autowired
    OmittablePk0Repository repository;

    @Test
    void test() {
        var data = new OmittablePk0Entity();
        // 全カラムを除外してDB の既定値に任せる
        // 戻り値は件数。DB が決めた値は引数のentity に書き戻される
        assertEquals(1, repository.insertExcept(data, Columns.COL_TEXT_NOT_NULL_DEFAULT_X));
        assertNotNull(data.getColTextNotNullDefaultX());
    }
}