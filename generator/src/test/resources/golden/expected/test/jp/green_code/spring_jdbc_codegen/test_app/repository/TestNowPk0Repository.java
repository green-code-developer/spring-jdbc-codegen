package jp.green_code.spring_jdbc_codegen.test_app.repository;

import jp.green_code.spring_jdbc_codegen.test_app.repository.base.TestBaseNowPk0Repository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestNowPk0Repository extends TestBaseNowPk0Repository {

    @Autowired
    NowPk0Repository repository;

    @Test
    void test() {
        super.test(repository);
    }
}