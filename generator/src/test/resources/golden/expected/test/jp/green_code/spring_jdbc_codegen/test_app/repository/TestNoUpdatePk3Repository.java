package jp.green_code.spring_jdbc_codegen.test_app.repository;

import jp.green_code.spring_jdbc_codegen.test_app.repository.base.TestBaseNoUpdatePk3Repository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestNoUpdatePk3Repository extends TestBaseNoUpdatePk3Repository {

    @Autowired
    NoUpdatePk3Repository repository;

    @Test
    void test() {
        super.test(repository);
    }
}