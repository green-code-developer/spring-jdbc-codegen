package jp.green_code.spring_jdbc_codegen.test_app.repository;

import jp.green_code.spring_jdbc_codegen.test_app.repository.base.TestBaseNoUpdatePk0Repository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestNoUpdatePk0Repository extends TestBaseNoUpdatePk0Repository {

    @Autowired
    NoUpdatePk0Repository repository;

    @Test
    void test() {
        super.test(repository);
    }
}