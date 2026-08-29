package jp.green_code.spring_jdbc_codegen.test_app.test;

import jp.green_code.spring_jdbc_codegen.test_app.entity.OnlyPk3NowEntity;
import jp.green_code.spring_jdbc_codegen.test_app.repository.OnlyPk3NowRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.OffsetDateTime;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class TestOnlyPk3NowRepository {

    @Autowired
    OnlyPk3NowRepository repository;

    @Test
    void test() {
        var entity = new OnlyPk3NowEntity();
        // pk2_now は not null かつ既定値がないため値が必要
        //   pk1 とpk3 は既定値があるので省略できる
        entity.setPk2Now(OffsetDateTime.now(ZoneId.systemDefault()));
        repository.insert(entity);

        // 存在しないpk をupdate しても例外は発生しない
        //   returning があっても helper.optional() で受けるため、更新件数は確認しない
        entity.setPk1(OffsetDateTime.now(ZoneId.systemDefault()));
        repository.update(entity);
        assertTrue(repository.findByPk(entity.getPk1(), entity.getPk2Now(), entity.getPk3()).isEmpty());
    }
}
