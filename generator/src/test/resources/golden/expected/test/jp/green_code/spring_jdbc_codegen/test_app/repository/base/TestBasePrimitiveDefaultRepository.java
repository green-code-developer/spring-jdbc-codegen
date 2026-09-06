package jp.green_code.spring_jdbc_codegen.test_app.repository.base;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;
import jp.green_code.spring_jdbc_codegen.test_app.StatusEnum;
import jp.green_code.spring_jdbc_codegen.test_app.entity.PrimitiveDefaultEntity;
import static jp.green_code.spring_jdbc_codegen.test_app.repository.base.BaseRepositoryHelper.pickBySeed;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class TestBasePrimitiveDefaultRepository {

    protected void test(BasePrimitiveDefaultRepository repository) {
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
        assert4colLong(data.getColLong(), stored.getColLong());
        assert4colInt(data.getColInt(), stored.getColInt());
        assert4colShort(data.getColShort(), stored.getColShort());
        assert4colBool(data.getColBool(), stored.getColBool());
        assert4colDouble(data.getColDouble(), stored.getColDouble());
        assert4colNumeric(data.getColNumeric(), stored.getColNumeric());
        assert4colText(data.getColText(), stored.getColText());
        assert4colTimestamptz(data.getColTimestamptz(), stored.getColTimestamptz());
        assert4colDate(data.getColDate(), stored.getColDate());
        assert4colUuid(data.getColUuid(), stored.getColUuid());
        assert4colUuidFunc(data.getColUuidFunc(), stored.getColUuidFunc());
        assert4colEnum(data.getColEnum(), stored.getColEnum());
        assert4colNoDefault(data.getColNoDefault(), stored.getColNoDefault());

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
        assert4colLong(data2.getColLong(), stored2.getColLong());
        assert4colInt(data2.getColInt(), stored2.getColInt());
        assert4colShort(data2.getColShort(), stored2.getColShort());
        assert4colBool(data2.getColBool(), stored2.getColBool());
        assert4colDouble(data2.getColDouble(), stored2.getColDouble());
        assert4colNumeric(data2.getColNumeric(), stored2.getColNumeric());
        assert4colText(data2.getColText(), stored2.getColText());
        assert4colTimestamptz(data2.getColTimestamptz(), stored2.getColTimestamptz());
        assert4colDate(data2.getColDate(), stored2.getColDate());
        assert4colUuid(data2.getColUuid(), stored2.getColUuid());
        assert4colUuidFunc(data2.getColUuidFunc(), stored2.getColUuidFunc());
        assert4colEnum(data2.getColEnum(), stored2.getColEnum());
        assert4colNoDefault(data2.getColNoDefault(), stored2.getColNoDefault());

        // delete
        var deleteCount = repository.deleteByPk(data2.getPk());
        assertEquals(1, deleteCount);
        // select 3回目
        var stored3 = repository.findByPk(data2.getPk());
        assertTrue(stored3.isEmpty());
    }


    public PrimitiveDefaultEntity generateTestData(int seed) {
        var entity = new PrimitiveDefaultEntity();
        entity.setPk(generateTestData4pk(seed++));
        entity.setColLong(generateTestData4colLong(seed++));
        entity.setColInt(generateTestData4colInt(seed++));
        entity.setColShort(generateTestData4colShort(seed++));
        entity.setColBool(generateTestData4colBool(seed++));
        entity.setColDouble(generateTestData4colDouble(seed++));
        entity.setColNumeric(generateTestData4colNumeric(seed++));
        entity.setColText(generateTestData4colText(seed++));
        entity.setColTimestamptz(generateTestData4colTimestamptz(seed++));
        entity.setColDate(generateTestData4colDate(seed++));
        entity.setColUuid(generateTestData4colUuid(seed++));
        entity.setColUuidFunc(generateTestData4colUuidFunc(seed++));
        entity.setColEnum(generateTestData4colEnum(seed++));
        entity.setColNoDefault(generateTestData4colNoDefault(seed));
        return entity;
    }

    protected Long generateTestData4pk(int seed) {
        return (long) seed;
    }

    protected long generateTestData4colLong(int seed) {
        return (long) seed;
    }

    protected int generateTestData4colInt(int seed) {
        return seed;
    }

    protected short generateTestData4colShort(int seed) {
        return (short) seed;
    }

    protected boolean generateTestData4colBool(int seed) {
        return seed %2 == 0;
    }

    protected double generateTestData4colDouble(int seed) {
        return (double) seed;
    }

    protected BigDecimal generateTestData4colNumeric(int seed) {
        return BigDecimal.valueOf(seed);
    }

    protected String generateTestData4colText(int seed) {
        return String.valueOf(seed);
    }

    protected OffsetDateTime generateTestData4colTimestamptz(int seed) {
        return OffsetDateTime.of(2001, 1, 1, 0, 0, 0, 0, java.time.ZoneOffset.UTC).plusMinutes(seed);
    }

    protected LocalDate generateTestData4colDate(int seed) {
        return LocalDate.of(2001, 1, 1).plusDays(seed);
    }

    protected UUID generateTestData4colUuid(int seed) {
        return UUID.fromString("9529478b-20d7-4232-ba79-"+String.format("%012d", seed));
    }

    protected UUID generateTestData4colUuidFunc(int seed) {
        return UUID.fromString("9529478b-20d7-4232-ba79-"+String.format("%012d", seed));
    }

    protected StatusEnum generateTestData4colEnum(int seed) {
        return pickBySeed(jp.green_code.spring_jdbc_codegen.test_app.StatusEnum.class, seed);
    }

    protected String generateTestData4colNoDefault(int seed) {
        return String.valueOf(seed);
    }


    protected void assert4pk(Long expected, Long value) {
        assertEquals(expected, value);
    }

    protected void assert4colLong(long expected, long value) {
        assertEquals(expected, value);
    }

    protected void assert4colInt(int expected, int value) {
        assertEquals(expected, value);
    }

    protected void assert4colShort(short expected, short value) {
        assertEquals(expected, value);
    }

    protected void assert4colBool(boolean expected, boolean value) {
        assertEquals(expected, value);
    }

    protected void assert4colDouble(double expected, double value) {
        assertEquals(expected, value);
    }

    protected void assert4colNumeric(BigDecimal expected, BigDecimal value) {
        assertEquals(0, expected.compareTo(value));
    }

    protected void assert4colText(String expected, String value) {
        assertEquals(expected, value.trim());
    }

    protected void assert4colTimestamptz(OffsetDateTime expected, OffsetDateTime value) {
        assertEquals(expected, value);
    }

    protected void assert4colDate(LocalDate expected, LocalDate value) {
        assertEquals(expected, value);
    }

    protected void assert4colUuid(UUID expected, UUID value) {
        assertEquals(expected, value);
    }

    protected void assert4colUuidFunc(UUID expected, UUID value) {
        assertEquals(expected, value);
    }

    protected void assert4colEnum(StatusEnum expected, StatusEnum value) {
        assertEquals(expected, value);
    }

    protected void assert4colNoDefault(String expected, String value) {
        assertEquals(expected, value.trim());
    }
}