package jp.green_code.spring_jdbc_codegen.test_app.test;

import jp.green_code.spring_jdbc_codegen.test_app.entity.OnlyPk1Entity;
import jp.green_code.spring_jdbc_codegen.test_app.repository.OnlyPk1Repository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * PK しか持たないテーブルの insert 2 種類を確認する。
 * <p>
 * only_pk1 は PK 以外のカラムがないため update 系は生成されない（REPO-020）。
 */
@SpringBootTest
@Transactional
public class TestOnlyPk1Repository {

    @Autowired
    OnlyPk1Repository repository;

    /** DB に採番させる。採番された値は returning で書き戻される */
    @Test
    void insertExceptPk_はDBに採番させる() {
        var entity = new OnlyPk1Entity();
        repository.insertExceptPk(entity);

        assertNotNull(entity.getPk());
        assertTrue(repository.findByPk(entity.getPk()).isPresent());
    }

    /** PK の値を明示して投入する。データ移行や初期データ投入で使う */
    @Test
    void insertAllColumns_はPKを明示して登録できる() {
        var largeLong = Long.MAX_VALUE - System.currentTimeMillis();
        var entity = new OnlyPk1Entity();
        entity.setPk(largeLong);
        repository.insertAllColumns(entity);

        assertTrue(repository.findByPk(largeLong).isPresent());
    }
}
