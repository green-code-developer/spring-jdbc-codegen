package jp.green_code.spring_jdbc_codegen.test_app.test;

import jp.green_code.spring_jdbc_codegen.test_app.StatusEnum;
import jp.green_code.spring_jdbc_codegen.test_app.entity.CoverageTestEntity;
import jp.green_code.spring_jdbc_codegen.test_app.repository.CoverageTestRepository;
import jp.green_code.spring_jdbc_codegen.test_app.repository.base.BaseCoverageTestRepository.Columns;
import jp.green_code.spring_jdbc_codegen.test_app.repository.base.BaseOmittablePk1Repository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * insertExcept と updateInclude の振る舞いを、insertAllColumns / updateAllColumns と
 * 対比して確認する。
 * <p>
 * 対象カラムは引数だけで決まり、entity の値は判定に使わない（REPO-011、REPO-022）。
 * coverage_test の col_nullable_default は「nullable かつ既定値を持つ」カラムで、
 * 除外するかどうかで結果が変わる。
 */
@SpringBootTest
@Transactional
public class TestNotNullVariants {

    @Autowired
    CoverageTestRepository repository;

    /**
     * 既定値に任せたいカラムを除外して登録する。
     * col_now_with_default は not null かつ既定値を持つため、値を入れないなら除外が要る。
     */
    int insert(CoverageTestEntity entity) {
        return repository.insertExcept(entity, Columns.PK, Columns.COL_NOW_WITH_DEFAULT);
    }

    /** not null かつ既定値のないカラムだけ埋めた entity を作る */
    CoverageTestEntity newEntity() {
        var entity = new CoverageTestEntity();
        entity.setColNotnullNodefault("required");
        entity.setColEnumDefault(StatusEnum.NEW);
        // enum カラムはnull を入れると文字列 "null" として渡り型キャストに失敗するため値を入れる
        entity.setColEnumNullableDefault(StatusEnum.DONE);
        return entity;
    }

    /** 除外しなかったカラムは entity の値がそのまま入る。既定値 'x' は使われない */
    @Test
    void insertExcept_は除外しなかったnullableカラムにnullを入れる() {
        var entity = newEntity();
        entity.setColNullableDefault(null);

        insert(entity);

        var stored = repository.findByPk(entity.getPk()).orElseThrow();
        assertNull(stored.getColNullableDefault());
    }

    /** 除外したカラムは DB の既定値 'x' が入り、returning で entity へ書き戻される */
    @Test
    void insertExcept_は除外したカラムにDBの既定値を使う() {
        var entity = newEntity();
        entity.setColNullableDefault(null);

        repository.insertExcept(entity, Columns.PK, Columns.COL_NOW_WITH_DEFAULT, Columns.COL_NULLABLE_DEFAULT);

        assertEquals("x", entity.getColNullableDefault()); // 書き戻されている
        var stored = repository.findByPk(entity.getPk()).orElseThrow();
        assertEquals("x", stored.getColNullableDefault());
    }

    /** updateAllColumns は entity の状態をそのまま反映するため null で上書きされる */
    @Test
    void updateAllColumns_はnullのカラムをnullで上書きする() {
        var entity = newEntity();
        entity.setColNullableDefault("keep");
        insert(entity);

        var stored = repository.findByPk(entity.getPk()).orElseThrow();
        stored.setColNullableDefault(null);
        repository.updateAllColumns(stored);

        assertNull(repository.findByPk(entity.getPk()).orElseThrow().getColNullableDefault());
    }

    /** updateInclude は指定しなかったカラムを set 句に含めないため既存の値が残る */
    @Test
    void updateInclude_は指定しなかったカラムを更新しない() {
        var entity = newEntity();
        entity.setColNullableDefault("keep");
        insert(entity);

        var partial = new CoverageTestEntity();
        partial.setPk(entity.getPk());
        partial.setColNotnullNodefault("updated");
        repository.updateInclude(partial, Columns.COL_NOTNULL_NODEFAULT);

        var stored = repository.findByPk(entity.getPk()).orElseThrow();
        assertEquals("updated", stored.getColNotnullNodefault());
        assertEquals("keep", stored.getColNullableDefault()); // 更新されていない
    }

    /**
     * 指定したカラムは値が null なら NULL で更新する。
     * v3 の updateNotNull ではできなかった操作。
     */
    @Test
    void updateInclude_はnullableカラムをNULLに戻せる() {
        var entity = newEntity();
        entity.setColNullableDefault("keep");
        insert(entity);

        var partial = new CoverageTestEntity();
        partial.setPk(entity.getPk());
        partial.setColNullableDefault(null);
        repository.updateInclude(partial, Columns.COL_NULLABLE_DEFAULT);

        assertNull(repository.findByPk(entity.getPk()).orElseThrow().getColNullableDefault());
    }

    /** updateInclude に PK は指定できない（REPO-003） */
    @Test
    void updateInclude_はPK指定を弾く() {
        var entity = newEntity();
        insert(entity);

        assertThrows(IllegalArgumentException.class,
                () -> repository.updateInclude(entity, Columns.PK));
    }

    /** 他テーブルのカラムは弾く。カラム名が同じでも別インスタンスなので検出できる（REPO-003） */
    @Test
    void updateInclude_は他テーブルのカラムを弾く() {
        var entity = newEntity();
        insert(entity);

        assertThrows(IllegalArgumentException.class,
                () -> repository.updateInclude(entity, BaseOmittablePk1Repository.Columns.COL_TEXT_NOT_NULL_DEFAULT_X));
    }

    /** 同じカラムの重複指定は弾く（REPO-003） */
    @Test
    void insertExcept_は重複指定を弾く() {
        var entity = newEntity();

        assertThrows(IllegalArgumentException.class,
                () -> repository.insertExcept(entity, Columns.PK, Columns.PK));
    }
}
