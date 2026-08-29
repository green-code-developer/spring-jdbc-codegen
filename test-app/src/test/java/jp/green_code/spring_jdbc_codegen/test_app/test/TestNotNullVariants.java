package jp.green_code.spring_jdbc_codegen.test_app.test;

import jp.green_code.spring_jdbc_codegen.test_app.StatusEnum;
import jp.green_code.spring_jdbc_codegen.test_app.entity.CoverageTestEntity;
import jp.green_code.spring_jdbc_codegen.test_app.repository.CoverageTestRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * insertNotNull と updateNotNull の振る舞いを、insert / update と対比して確認する。
 * <p>
 * coverage_test の col_nullable_default は「nullable かつ既定値を持つ」カラムで、
 * insert と insertNotNull で結果が変わる唯一の条件を満たす。
 */
@SpringBootTest
@Transactional
public class TestNotNullVariants {

    @Autowired
    CoverageTestRepository repository;

    /** not null かつ既定値のないカラムだけ埋めた entity を作る */
    CoverageTestEntity newEntity() {
        var entity = new CoverageTestEntity();
        entity.setColNotnullNodefault("required");
        entity.setColEnumDefault(StatusEnum.NEW);
        // enum カラムはnull を入れると文字列 "null" として渡り型キャストに失敗するため値を入れる
        entity.setColEnumNullableDefault(StatusEnum.DONE);
        return entity;
    }

    /**
     * insert は nullable なカラムに null をそのまま入れる。
     * 既定値 'x' は使われない。
     */
    @Test
    void insert_はnullableなカラムにnullを入れる() {
        var entity = newEntity();
        entity.setColNullableDefault(null);

        repository.insert(entity);

        var stored = repository.findByPk(entity.getPk()).orElseThrow();
        assertNull(stored.getColNullableDefault());
    }

    /**
     * insertNotNull は null のカラムを INSERT 対象から外すため、
     * DB の既定値 'x' が入る。値は returning で entity へ書き戻される。
     */
    @Test
    void insertNotNull_はnullのカラムにDBの既定値を使う() {
        var entity = newEntity();
        entity.setColNullableDefault(null);

        repository.insertNotNull(entity);

        assertEquals("x", entity.getColNullableDefault()); // 書き戻されている
        var stored = repository.findByPk(entity.getPk()).orElseThrow();
        assertEquals("x", stored.getColNullableDefault());
    }

    /**
     * update は entity の状態をそのまま反映するため、
     * セットしなかったカラムは null で上書きされる。
     */
    @Test
    void update_はnullのカラムをnullで上書きする() {
        var entity = newEntity();
        entity.setColNullableDefault("keep");
        repository.insert(entity);

        var stored = repository.findByPk(entity.getPk()).orElseThrow();
        stored.setColNullableDefault(null);
        repository.update(stored);

        assertNull(repository.findByPk(entity.getPk()).orElseThrow().getColNullableDefault());
    }

    /**
     * updateNotNull は null のカラムを set 句から外すため、既存の値が残る。
     * PK と変更したいカラムだけをセットした entity で部分更新できる。
     */
    @Test
    void updateNotNull_はnullのカラムを更新しない() {
        var entity = newEntity();
        entity.setColNullableDefault("keep");
        repository.insert(entity);

        var partial = new CoverageTestEntity();
        partial.setPk(entity.getPk());
        partial.setColNotnullNodefault("updated");
        repository.updateNotNull(partial);

        var stored = repository.findByPk(entity.getPk()).orElseThrow();
        assertEquals("updated", stored.getColNotnullNodefault());
        assertEquals("keep", stored.getColNullableDefault()); // 更新されていない
    }
}
