package jp.green_code.spring_jdbc_codegen.test_app.repository;

import jp.green_code.spring_jdbc_codegen.test_app.repository.base.TestBaseNowPk1Repository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestNowPk1Repository extends TestBaseNowPk1Repository {

    @Autowired
    NowPk1Repository repository;

    @Test
    void test() {
        super.test(repository);
    }
}