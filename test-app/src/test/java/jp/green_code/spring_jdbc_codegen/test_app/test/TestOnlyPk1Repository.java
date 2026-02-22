package jp.green_code.spring_jdbc_codegen.test_app.test;

import jp.green_code.spring_jdbc_codegen.test_app.entity.OnlyPk1Entity;
import jp.green_code.spring_jdbc_codegen.test_app.repository.OnlyPk1Repository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class TestOnlyPk1Repository {

    @Autowired
    OnlyPk1Repository repository;

    @Test
    void test() {
        // updateByPk を試す
        var entity = new OnlyPk1Entity();
        repository.insert(entity);

        assertTrue(repository.findByPk(entity.getPk()).isPresent());

        // long max から少し引いた値にupdate
        //   テスト失敗が繰り返されないように
        var largeLong = Long.MAX_VALUE - System.currentTimeMillis();
        var largeLongEntity = new OnlyPk1Entity();
        largeLongEntity.setPk(largeLong);
        repository.updateByPk(largeLongEntity, entity.getPk());

        // 古い値は取得できないはず
        assertTrue(repository.findByPk(largeLong).isPresent());
        assertTrue(repository.findByPk(entity.getPk()).isEmpty());

        // 存在しないpk の場合は例外発生するはず
        //   内部でhelper.exec() を使っているケース
        assertThrows(EmptyResultDataAccessException.class, () -> {
            repository.update(entity);
        });
    }
}
