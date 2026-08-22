package jp.green_code.spring_jdbc_codegen.test_app.repository.base;

import java.lang.Long;
import java.lang.String;
import java.time.OffsetDateTime;
import java.util.UUID;
import jp.green_code.spring_jdbc_codegen.test_app.entity.NoUpdatePk3Entity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class TestBaseNoUpdatePk3Repository {

    protected void test(BaseNoUpdatePk3Repository repository) {
        var seed = 1;
        var data = generateTestData(seed);

        // insert
        data.setPk1(null);
        data.setPk2(null);
        data.setPk3(null);
        repository.insert(data);

        // select 1回目
        var res = repository.findByPk(data.getPk1(), data.getPk2(), data.getPk3());
        assertTrue(res.isPresent());

        // insert 後の確認
        var stored = res.orElseThrow();
        assert4pk1(data.getPk1(), stored.getPk1());
        assert4pk2(data.getPk2(), stored.getPk2());
        assert4pk3(data.getPk3(), stored.getPk3());
        assert4colNoUpdateTextNotNullDefaultX(data.getColNoUpdateTextNotNullDefaultX(), stored.getColNoUpdateTextNotNullDefaultX());

        // update
        seed++;
        var data2 = generateTestData(seed);
        data2.setPk1(data.getPk1());
        data2.setPk2(data.getPk2());
        data2.setPk3(data.getPk3());
        repository.update(data2);

        // select 2回目
        var res2 = repository.findByPk(data2.getPk1(), data2.getPk2(), data2.getPk3());
        assertTrue(res2.isPresent());

        // update 後の確認
        var stored2 = res2.orElseThrow();

        assert4pk1(data2.getPk1(), stored2.getPk1());

        assert4pk2(data2.getPk2(), stored2.getPk2());

        assert4pk3(data2.getPk3(), stored2.getPk3());

        // col_no_update_text_not_null_default_x はupdate 対象外のため変更前と変わらないことを確認
        assert4colNoUpdateTextNotNullDefaultX(stored.getColNoUpdateTextNotNullDefaultX(), stored2.getColNoUpdateTextNotNullDefaultX());

        // delete
        var deleteCount = repository.deleteByPk(data2.getPk1(), data2.getPk2(), data2.getPk3());
        assertEquals(1, deleteCount);
        // select 3回目
        var stored3 = repository.findByPk(data2.getPk1(), data2.getPk2(), data2.getPk3());
        assertTrue(stored3.isEmpty());
    }


    public NoUpdatePk3Entity generateTestData(int seed) {
        var entity = new NoUpdatePk3Entity();
        entity.setPk1(generateTestData4pk1(seed++));
        entity.setPk2(generateTestData4pk2(seed++));
        entity.setPk3(generateTestData4pk3(seed++));
        entity.setColNoUpdateTextNotNullDefaultX(generateTestData4colNoUpdateTextNotNullDefaultX(seed));
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

    protected String generateTestData4colNoUpdateTextNotNullDefaultX(int seed) {
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

    protected void assert4colNoUpdateTextNotNullDefaultX(String expected, String value) {
        assertEquals(expected, value.trim());
    }
}