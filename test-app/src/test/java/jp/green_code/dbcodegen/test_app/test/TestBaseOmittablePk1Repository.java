package jp.green_code.dbcodegen.test_app.test;

import jp.green_code.dbcodegen.test_app.entity.OmittablePk1Entity;
import jp.green_code.dbcodegen.test_app.repository.OmittablePk1Repository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class TestBaseOmittablePk1Repository {

    @Autowired
    OmittablePk1Repository repository;

    @Test
    void test() {
        var data = new OmittablePk1Entity();
        // 全てnull をinsert
        var res = repository.insert(data);
        assertNotNull(res.getPk());
        assertNotNull(res.getColTextNotNullDefaultX());
    }
}