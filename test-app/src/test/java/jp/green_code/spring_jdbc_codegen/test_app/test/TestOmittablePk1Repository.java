package jp.green_code.spring_jdbc_codegen.test_app.test;

import jp.green_code.spring_jdbc_codegen.test_app.entity.OmittablePk1Entity;
import jp.green_code.spring_jdbc_codegen.test_app.repository.OmittablePk1Repository;
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
        // 全てnull をinsert
        // 戻り値は件数。DB が決めた値は引数のentity に書き戻される
        assertEquals(1, repository.insert(data));
        assertNotNull(data.getPk());
        assertNotNull(data.getColTextNotNullDefaultX());
    }
}