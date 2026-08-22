package jp.green_code.spring_jdbc_codegen.test_app.repository;

import jp.green_code.spring_jdbc_codegen.test_app.repository.base.TestBaseOnlyPk1Repository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestOnlyPk1Repository extends TestBaseOnlyPk1Repository {

    @Autowired
    OnlyPk1Repository repository;

    @Test
    void test() {
        super.test(repository);
    }
}