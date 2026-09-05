package jp.green_code.spring_jdbc_codegen.test_app.test;

import jp.green_code.spring_jdbc_codegen.test_app.entity.OnlyPk3NowEntity;
import jp.green_code.spring_jdbc_codegen.test_app.repository.OnlyPk3NowRepository;
import jp.green_code.spring_jdbc_codegen.test_app.repository.base.BaseOnlyPk3NowRepository.Columns;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * PK の一部だけ DB 側で値を決められるテーブルの insert を確認する。
 * <p>
 * only_pk3_now の pk2_now は既定値も自動採番も持たないため insertExceptPk は
 * 生成されない（REPO-014）。除外するカラムを明示して insertExcept を使う。
 */
@SpringBootTest
@Transactional
public class TestOnlyPk3NowRepository {

    @Autowired
    OnlyPk3NowRepository repository;

    @Test
    void test() {
        var entity = new OnlyPk3NowEntity();
        // pk2_now は not null かつ既定値がないため値が必要
        entity.setPk2Now(OffsetDateTime.now(ZoneId.systemDefault()));
        repository.insertExcept(entity, Columns.PK1, Columns.PK3);

        // 除外したカラムは returning で書き戻される
        assertNotNull(entity.getPk1());
        assertNotNull(entity.getPk3());
        assertTrue(repository.findByPk(entity.getPk1(), entity.getPk2Now(), entity.getPk3()).isPresent());
    }
}
