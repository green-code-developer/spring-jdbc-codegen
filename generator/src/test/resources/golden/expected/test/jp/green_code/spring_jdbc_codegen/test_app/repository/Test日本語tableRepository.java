package jp.green_code.spring_jdbc_codegen.test_app.repository;

import jp.green_code.spring_jdbc_codegen.test_app.repository.base.TestBase日本語tableRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class Test日本語tableRepository extends TestBase日本語tableRepository {

    @Autowired
    日本語tableRepository repository;

    @Test
    void test() {
        super.test(repository);
    }
}