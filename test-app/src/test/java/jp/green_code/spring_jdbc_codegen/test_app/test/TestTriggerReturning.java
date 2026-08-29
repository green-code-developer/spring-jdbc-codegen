package jp.green_code.spring_jdbc_codegen.test_app.test;

import jp.green_code.spring_jdbc_codegen.test_app.entity.TriggerTestEntity;
import jp.green_code.spring_jdbc_codegen.test_app.repository.TriggerTestRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * returningColumnsByTable の検証。
 * <p>
 * trigger_test の updated_at はトリガーで now() に書き換えられる。
 * param.yml で returning 対象に指定しているため、DB が確定させた値が
 * entity へ書き戻される。
 */
@SpringBootTest
@Transactional
public class TestTriggerReturning {

    static final OffsetDateTime OLD = OffsetDateTime.of(2000, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);

    @Autowired
    TriggerTestRepository repository;

    /** insert 時にトリガーが書き換えた値を取得して entity へ反映する */
    @Test
    void insert時にトリガーの値がentityへ反映される() {
        var entity = new TriggerTestEntity();
        entity.setColText("inserted");
        entity.setUpdatedAt(OLD); // Java から古い値を渡す

        repository.insert(entity);

        // トリガーが now() で上書きした値が returning で戻っている
        assertTrue(entity.getUpdatedAt().isAfter(OLD),
                "トリガーの値が entity へ反映されていない: " + entity.getUpdatedAt());
        var stored = repository.findByPk(entity.getPk()).orElseThrow();
        assertEquals(stored.getUpdatedAt(), entity.getUpdatedAt());
    }

    /** update 時も同じく書き戻される */
    @Test
    void update時にトリガーの値がentityへ反映される() {
        var entity = new TriggerTestEntity();
        entity.setColText("inserted");
        repository.insert(entity);
        var insertedAt = entity.getUpdatedAt();

        entity.setColText("updated");
        entity.setUpdatedAt(OLD); // 再び古い値を渡す
        repository.update(entity);

        assertTrue(entity.getUpdatedAt().isAfter(OLD),
                "トリガーの値が entity へ反映されていない: " + entity.getUpdatedAt());
        var stored = repository.findByPk(entity.getPk()).orElseThrow();
        assertEquals("updated", stored.getColText());
        assertEquals(stored.getUpdatedAt(), entity.getUpdatedAt());
        assertTrue(!entity.getUpdatedAt().isBefore(insertedAt));
    }
}
