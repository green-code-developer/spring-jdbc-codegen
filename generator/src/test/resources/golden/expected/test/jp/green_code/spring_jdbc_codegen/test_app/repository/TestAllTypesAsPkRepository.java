package jp.green_code.spring_jdbc_codegen.test_app.repository;

import jp.green_code.spring_jdbc_codegen.test_app.repository.base.TestBaseAllTypesAsPkRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestAllTypesAsPkRepository extends TestBaseAllTypesAsPkRepository {

    @Autowired
    AllTypesAsPkRepository repository;

    @Test
    void test() {
        super.test(repository);
    }
}