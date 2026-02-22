package jp.green_code.spring_jdbc_codegen.test_app.test;

import jp.green_code.spring_jdbc_codegen.test_app.entity.OnlyPk3NowEntity;
import jp.green_code.spring_jdbc_codegen.test_app.repository.OnlyPk3NowRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class TestOnlyPk3NowRepository {

    @Autowired
    OnlyPk3NowRepository repository;

    @Test
    void test() {
        var entity = new OnlyPk3NowEntity();
        repository.insert(entity);

        // 存在しないpk の場合は例外発生するはず
        //   内部でhelper.single() を使っているケース
        entity.setPk1(OffsetDateTime.now());
        assertThrows(EmptyResultDataAccessException.class, () -> {
            repository.update(entity);
        });
    }
}
