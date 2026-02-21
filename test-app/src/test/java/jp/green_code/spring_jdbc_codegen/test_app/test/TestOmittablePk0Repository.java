package jp.green_code.spring_jdbc_codegen.test_app.test;

import jp.green_code.spring_jdbc_codegen.test_app.entity.OmittablePk0Entity;
import jp.green_code.spring_jdbc_codegen.test_app.repository.OmittablePk0Repository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class TestOmittablePk0Repository {

    @Autowired
    OmittablePk0Repository repository;

    @Test
    void test() {
        var data = new OmittablePk0Entity();
        // 全てnull をinsert
        var res = repository.insert(data);
        assertNotNull(res.getColTextNotNullDefaultX());
    }
}