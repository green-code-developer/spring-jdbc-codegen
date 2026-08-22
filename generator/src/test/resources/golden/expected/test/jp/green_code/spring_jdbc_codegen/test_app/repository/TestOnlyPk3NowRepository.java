package jp.green_code.spring_jdbc_codegen.test_app.repository;

import jp.green_code.spring_jdbc_codegen.test_app.repository.base.TestBaseOnlyPk3NowRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestOnlyPk3NowRepository extends TestBaseOnlyPk3NowRepository {

    @Autowired
    OnlyPk3NowRepository repository;

    @Test
    void test() {
        super.test(repository);
    }
}