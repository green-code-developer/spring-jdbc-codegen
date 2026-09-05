package jp.green_code.spring_jdbc_codegen.test_app.repository.base;

import java.time.OffsetDateTime;
import java.util.UUID;
import jp.green_code.spring_jdbc_codegen.test_app.entity.NormalPk3Entity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class TestBaseNormalPk3Repository {

    protected void test(BaseNormalPk3Repository repository) {
        var seed = 1;
        var data = generateTestData(seed);

        // insert
        repository.insertExceptPk(data);

        // select 1回目
        var res = repository.findByPk(data.getPk1(), data.getPk2(), data.getPk3());
        assertTrue(res.isPresent());

        // insert 後の確認
        var stored = res.orElseThrow();
        assert4pk1(data.getPk1(), stored.getPk1());
        assert4pk2(data.getPk2(), stored.getPk2());
        assert4pk3(data.getPk3(), stored.getPk3());
        assert4colText(data.getColText(), stored.getColText());
        assert4colTextNotNull(data.getColTextNotNull(), stored.getColTextNotNull());
        assert4colTextNotNullDefaultX(data.getColTextNotNullDefaultX(), stored.getColTextNotNullDefaultX());
        assert4colTextDefaultY(data.getColTextDefaultY(), stored.getColTextDefaultY());

        // update
        seed++;
        var data2 = generateTestData(seed);
        data2.setPk1(data.getPk1());
        data2.setPk2(data.getPk2());
        data2.setPk3(data.getPk3());
        repository.updateAllColumns(data2);

        // select 2回目
        var res2 = repository.findByPk(data2.getPk1(), data2.getPk2(), data2.getPk3());
        assertTrue(res2.isPresent());

        // update 後の確認
        var stored2 = res2.orElseThrow();
        assert4pk1(data2.getPk1(), stored2.getPk1());
        assert4pk2(data2.getPk2(), stored2.getPk2());
        assert4pk3(data2.getPk3(), stored2.getPk3());
        assert4colText(data2.getColText(), stored2.getColText());
        assert4colTextNotNull(data2.getColTextNotNull(), stored2.getColTextNotNull());
        assert4colTextNotNullDefaultX(data2.getColTextNotNullDefaultX(), stored2.getColTextNotNullDefaultX());
        assert4colTextDefaultY(data2.getColTextDefaultY(), stored2.getColTextDefaultY());

        // delete
        var deleteCount = repository.deleteByPk(data2.getPk1(), data2.getPk2(), data2.getPk3());
        assertEquals(1, deleteCount);
        // select 3回目
        var stored3 = repository.findByPk(data2.getPk1(), data2.getPk2(), data2.getPk3());
        assertTrue(stored3.isEmpty());
    }


    public NormalPk3Entity generateTestData(int seed) {
        var entity = new NormalPk3Entity();
        entity.setPk1(generateTestData4pk1(seed++));
        entity.setPk2(generateTestData4pk2(seed++));
        entity.setPk3(generateTestData4pk3(seed++));
        entity.setColText(generateTestData4colText(seed++));
        entity.setColTextNotNull(generateTestData4colTextNotNull(seed++));
        entity.setColTextNotNullDefaultX(generateTestData4colTextNotNullDefaultX(seed++));
        entity.setColTextDefaultY(generateTestData4colTextDefaultY(seed));
        return entity;
    }

    protected Long generateTestData4pk1(int seed) {
        return (long) seed;
    }

    protected OffsetDateTime generateTestData4pk2(int seed) {
        return OffsetDateTime.of(2001, 1, 1, 0, 0, 0, 0, java.time.ZoneOffset.UTC).plusMinutes(seed);
    }

    protected UUID generateTestData4pk3(int seed) {
        return UUID.fromString("9529478b-20d7-4232-ba79-"+String.format("%012d", seed));
    }

    protected String generateTestData4colText(int seed) {
        return String.valueOf(seed);
    }

    protected String generateTestData4colTextNotNull(int seed) {
        return String.valueOf(seed);
    }

    protected String generateTestData4colTextNotNullDefaultX(int seed) {
        return String.valueOf(seed);
    }

    protected String generateTestData4colTextDefaultY(int seed) {
        return String.valueOf(seed);
    }


    protected void assert4pk1(Long expected, Long value) {
        assertEquals(expected, value);
    }

    protected void assert4pk2(OffsetDateTime expected, OffsetDateTime value) {
        assertEquals(expected, value);
    }

    protected void assert4pk3(UUID expected, UUID value) {
        assertEquals(expected, value);
    }

    protected void assert4colText(String expected, String value) {
        assertEquals(expected, value.trim());
    }

    protected void assert4colTextNotNull(String expected, String value) {
        assertEquals(expected, value.trim());
    }

    protected void assert4colTextNotNullDefaultX(String expected, String value) {
        assertEquals(expected, value.trim());
    }

    protected void assert4colTextDefaultY(String expected, String value) {
        assertEquals(expected, value.trim());
    }
}