package jp.green_code.spring_jdbc_codegen.test_app.repository.base;

import java.time.OffsetDateTime;
import jp.green_code.spring_jdbc_codegen.test_app.entity.QuotedColumnNowEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class TestBaseQuotedColumnNowRepository {

    protected void test(BaseQuotedColumnNowRepository repository) {
        var seed = 1;
        var data = generateTestData(seed);

        // insert
        repository.insertExceptPk(data);

        // select 1回目
        var res = repository.findByPk(data.getPk());
        assertTrue(res.isPresent());

        // insert 後の確認
        var stored = res.orElseThrow();
        assert4pk(data.getPk(), stored.getPk());
        assert4updated(data.getUpdated(), stored.getUpdated());
        assert4colText(data.getColText(), stored.getColText());

        // update
        seed++;
        var data2 = generateTestData(seed);
        data2.setPk(data.getPk());
        repository.updateAllColumns(data2);

        // select 2回目
        var res2 = repository.findByPk(data2.getPk());
        assertTrue(res2.isPresent());

        // update 後の確認
        var stored2 = res2.orElseThrow();

        assert4pk(data2.getPk(), stored2.getPk());

        assert4updated(data2.getUpdated(), stored2.getUpdated());

        assert4colText(data2.getColText(), stored2.getColText());

        // delete
        var deleteCount = repository.deleteByPk(data2.getPk());
        assertEquals(1, deleteCount);
        // select 3回目
        var stored3 = repository.findByPk(data2.getPk());
        assertTrue(stored3.isEmpty());
    }


    public QuotedColumnNowEntity generateTestData(int seed) {
        var entity = new QuotedColumnNowEntity();
        entity.setPk(generateTestData4pk(seed++));
        entity.setUpdated(generateTestData4updated(seed++));
        entity.setColText(generateTestData4colText(seed));
        return entity;
    }

    protected Long generateTestData4pk(int seed) {
        return (long) seed;
    }

    protected OffsetDateTime generateTestData4updated(int seed) {
        return OffsetDateTime.of(2001, 1, 1, 0, 0, 0, 0, java.time.ZoneOffset.UTC).plusMinutes(seed);
    }

    protected String generateTestData4colText(int seed) {
        return String.valueOf(seed);
    }


    protected void assert4pk(Long expected, Long value) {
        assertEquals(expected, value);
    }

    protected void assert4updated(OffsetDateTime expected, OffsetDateTime value) {
        assertEquals(expected, value);
    }

    protected void assert4colText(String expected, String value) {
        assertEquals(expected, value.trim());
    }
}