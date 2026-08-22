package jp.green_code.spring_jdbc_codegen.test_app.repository;

import jp.green_code.spring_jdbc_codegen.test_app.repository.base.TestBaseNormalPk0Repository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestNormalPk0Repository extends TestBaseNormalPk0Repository {

    @Autowired
    NormalPk0Repository repository;

    @Test
    void test() {
        super.test(repository);
    }
}