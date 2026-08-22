package jp.green_code.spring_jdbc_codegen.test_app.repository;

import jp.green_code.spring_jdbc_codegen.test_app.repository.base.TestBaseNormalPk1Repository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestNormalPk1Repository extends TestBaseNormalPk1Repository {

    @Autowired
    NormalPk1Repository repository;

    @Test
    void test() {
        super.test(repository);
    }
}