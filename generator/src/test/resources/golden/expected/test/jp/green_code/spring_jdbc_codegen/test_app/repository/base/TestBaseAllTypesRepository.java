package jp.green_code.spring_jdbc_codegen.test_app.repository.base;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.util.UUID;
import jp.green_code.spring_jdbc_codegen.test_app.StatusEnum;
import jp.green_code.spring_jdbc_codegen.test_app.entity.AllTypesEntity;
import static jp.green_code.spring_jdbc_codegen.test_app.repository.base.BaseRepositoryHelper.pickBySeed;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class TestBaseAllTypesRepository {

    protected void test(BaseAllTypesRepository repository) {
        var seed = 1;
        var data = generateTestData(seed);

        // insert
        data.setPk(null);
        repository.insert(data);

        // select 1回目
        var res = repository.findByPk(data.getPk());
        assertTrue(res.isPresent());

        // insert 後の確認
        var stored = res.orElseThrow();
        assert4pk(data.getPk(), stored.getPk());
        assert4colSmallint(data.getColSmallint(), stored.getColSmallint());
        assert4colSmallserial(data.getColSmallserial(), stored.getColSmallserial());
        assert4colInteger(data.getColInteger(), stored.getColInteger());
        assert4colSerial(data.getColSerial(), stored.getColSerial());
        assert4colBigint(data.getColBigint(), stored.getColBigint());
        assert4colBigserial(data.getColBigserial(), stored.getColBigserial());
        assert4colReal(data.getColReal(), stored.getColReal());
        assert4colDoublePrecision(data.getColDoublePrecision(), stored.getColDoublePrecision());
        assert4colNumeric(data.getColNumeric(), stored.getColNumeric());
        assert4colBoolean(data.getColBoolean(), stored.getColBoolean());
        assert4colChar(data.getColChar(), stored.getColChar());
        assert4colVarchar(data.getColVarchar(), stored.getColVarchar());
        assert4colText(data.getColText(), stored.getColText());
        assert4colDate(data.getColDate(), stored.getColDate());
        assert4colTime(data.getColTime(), stored.getColTime());
        assert4colTimeTz(data.getColTimeTz(), stored.getColTimeTz());
        assert4colTimestamp(data.getColTimestamp(), stored.getColTimestamp());
        assert4colTimestampTz(data.getColTimestampTz(), stored.getColTimestampTz());
        assert4colInterval(data.getColInterval(), stored.getColInterval());
        assert4colBytea(data.getColBytea(), stored.getColBytea());
        assert4colUuid(data.getColUuid(), stored.getColUuid());
        assert4colJson(data.getColJson(), stored.getColJson());
        assert4colJsonb(data.getColJsonb(), stored.getColJsonb());
        assert4colXml(data.getColXml(), stored.getColXml());
        assert4colInet(data.getColInet(), stored.getColInet());
        assert4colCidr(data.getColCidr(), stored.getColCidr());
        assert4colMacaddr(data.getColMacaddr(), stored.getColMacaddr());
        assert4colBox(data.getColBox(), stored.getColBox());
        assert4colPoint(data.getColPoint(), stored.getColPoint());
        assert4colLine(data.getColLine(), stored.getColLine());
        assert4colLseg(data.getColLseg(), stored.getColLseg());
        assert4colPath(data.getColPath(), stored.getColPath());
        assert4colPolygon(data.getColPolygon(), stored.getColPolygon());
        assert4colCircle(data.getColCircle(), stored.getColCircle());
        assert4colStatusEnum(data.getColStatusEnum(), stored.getColStatusEnum());

        // update
        seed++;
        var data2 = generateTestData(seed);
        data2.setPk(data.getPk());
        repository.update(data2);

        // select 2回目
        var res2 = repository.findByPk(data2.getPk());
        assertTrue(res2.isPresent());

        // update 後の確認
        var stored2 = res2.orElseThrow();

        assert4pk(data2.getPk(), stored2.getPk());

        assert4colSmallint(data2.getColSmallint(), stored2.getColSmallint());

        assert4colSmallserial(data2.getColSmallserial(), stored2.getColSmallserial());

        assert4colInteger(data2.getColInteger(), stored2.getColInteger());

        assert4colSerial(data2.getColSerial(), stored2.getColSerial());

        assert4colBigint(data2.getColBigint(), stored2.getColBigint());

        assert4colBigserial(data2.getColBigserial(), stored2.getColBigserial());

        assert4colReal(data2.getColReal(), stored2.getColReal());

        assert4colDoublePrecision(data2.getColDoublePrecision(), stored2.getColDoublePrecision());

        assert4colNumeric(data2.getColNumeric(), stored2.getColNumeric());

        assert4colBoolean(data2.getColBoolean(), stored2.getColBoolean());

        assert4colChar(data2.getColChar(), stored2.getColChar());

        assert4colVarchar(data2.getColVarchar(), stored2.getColVarchar());

        assert4colText(data2.getColText(), stored2.getColText());

        assert4colDate(data2.getColDate(), stored2.getColDate());

        assert4colTime(data2.getColTime(), stored2.getColTime());

        assert4colTimeTz(data2.getColTimeTz(), stored2.getColTimeTz());

        assert4colTimestamp(data2.getColTimestamp(), stored2.getColTimestamp());

        assert4colTimestampTz(data2.getColTimestampTz(), stored2.getColTimestampTz());

        assert4colInterval(data2.getColInterval(), stored2.getColInterval());

        assert4colBytea(data2.getColBytea(), stored2.getColBytea());

        assert4colUuid(data2.getColUuid(), stored2.getColUuid());

        assert4colJson(data2.getColJson(), stored2.getColJson());

        assert4colJsonb(data2.getColJsonb(), stored2.getColJsonb());

        assert4colXml(data2.getColXml(), stored2.getColXml());

        assert4colInet(data2.getColInet(), stored2.getColInet());

        assert4colCidr(data2.getColCidr(), stored2.getColCidr());

        assert4colMacaddr(data2.getColMacaddr(), stored2.getColMacaddr());

        assert4colBox(data2.getColBox(), stored2.getColBox());

        assert4colPoint(data2.getColPoint(), stored2.getColPoint());

        assert4colLine(data2.getColLine(), stored2.getColLine());

        assert4colLseg(data2.getColLseg(), stored2.getColLseg());

        assert4colPath(data2.getColPath(), stored2.getColPath());

        assert4colPolygon(data2.getColPolygon(), stored2.getColPolygon());

        assert4colCircle(data2.getColCircle(), stored2.getColCircle());

        assert4colStatusEnum(data2.getColStatusEnum(), stored2.getColStatusEnum());

        // delete
        var deleteCount = repository.deleteByPk(data2.getPk());
        assertEquals(1, deleteCount);
        // select 3回目
        var stored3 = repository.findByPk(data2.getPk());
        assertTrue(stored3.isEmpty());
    }


    public AllTypesEntity generateTestData(int seed) {
        var entity = new AllTypesEntity();
        entity.setPk(generateTestData4pk(seed++));
        entity.setColSmallint(generateTestData4colSmallint(seed++));
        entity.setColSmallserial(generateTestData4colSmallserial(seed++));
        entity.setColInteger(generateTestData4colInteger(seed++));
        entity.setColSerial(generateTestData4colSerial(seed++));
        entity.setColBigint(generateTestData4colBigint(seed++));
        entity.setColBigserial(generateTestData4colBigserial(seed++));
        entity.setColReal(generateTestData4colReal(seed++));
        entity.setColDoublePrecision(generateTestData4colDoublePrecision(seed++));
        entity.setColNumeric(generateTestData4colNumeric(seed++));
        entity.setColBoolean(generateTestData4colBoolean(seed++));
        entity.setColChar(generateTestData4colChar(seed++));
        entity.setColVarchar(generateTestData4colVarchar(seed++));
        entity.setColText(generateTestData4colText(seed++));
        entity.setColDate(generateTestData4colDate(seed++));
        entity.setColTime(generateTestData4colTime(seed++));
        entity.setColTimeTz(generateTestData4colTimeTz(seed++));
        entity.setColTimestamp(generateTestData4colTimestamp(seed++));
        entity.setColTimestampTz(generateTestData4colTimestampTz(seed++));
        entity.setColInterval(generateTestData4colInterval(seed++));
        entity.setColBytea(generateTestData4colBytea(seed++));
        entity.setColUuid(generateTestData4colUuid(seed++));
        entity.setColJson(generateTestData4colJson(seed++));
        entity.setColJsonb(generateTestData4colJsonb(seed++));
        entity.setColXml(generateTestData4colXml(seed++));
        entity.setColInet(generateTestData4colInet(seed++));
        entity.setColCidr(generateTestData4colCidr(seed++));
        entity.setColMacaddr(generateTestData4colMacaddr(seed++));
        entity.setColBox(generateTestData4colBox(seed++));
        entity.setColPoint(generateTestData4colPoint(seed++));
        entity.setColLine(generateTestData4colLine(seed++));
        entity.setColLseg(generateTestData4colLseg(seed++));
        entity.setColPath(generateTestData4colPath(seed++));
        entity.setColPolygon(generateTestData4colPolygon(seed++));
        entity.setColCircle(generateTestData4colCircle(seed++));
        entity.setColStatusEnum(generateTestData4colStatusEnum(seed));
        return entity;
    }

    protected Long generateTestData4pk(int seed) {
        return (long) seed;
    }

    protected Short generateTestData4colSmallint(int seed) {
        return (short) seed;
    }

    protected Short generateTestData4colSmallserial(int seed) {
        return (short) seed;
    }

    protected Integer generateTestData4colInteger(int seed) {
        return seed;
    }

    protected Integer generateTestData4colSerial(int seed) {
        return seed;
    }

    protected Long generateTestData4colBigint(int seed) {
        return (long) seed;
    }

    protected Long generateTestData4colBigserial(int seed) {
        return (long) seed;
    }

    protected Float generateTestData4colReal(int seed) {
        return (float) seed;
    }

    protected Double generateTestData4colDoublePrecision(int seed) {
        return (double) seed;
    }

    protected BigDecimal generateTestData4colNumeric(int seed) {
        return BigDecimal.valueOf(seed);
    }

    protected Boolean generateTestData4colBoolean(int seed) {
        return seed %2 == 0;
    }

    protected String generateTestData4colChar(int seed) {
        return String.valueOf(seed);
    }

    protected String generateTestData4colVarchar(int seed) {
        return String.valueOf(seed);
    }

    protected String generateTestData4colText(int seed) {
        return String.valueOf(seed);
    }

    protected LocalDate generateTestData4colDate(int seed) {
        return LocalDate.of(2001, 1, 1).plusDays(seed);
    }

    protected LocalTime generateTestData4colTime(int seed) {
        return LocalTime.of(0, 0, 0).plusMinutes(seed);
    }

    protected OffsetTime generateTestData4colTimeTz(int seed) {
        return OffsetTime.of(0, 0, 0, 0, java.time.ZoneOffset.UTC).plusMinutes(seed);
    }

    protected LocalDateTime generateTestData4colTimestamp(int seed) {
        return LocalDateTime.of(2001, 1, 1, 0, 0, 0).plusMinutes(seed);
    }

    protected OffsetDateTime generateTestData4colTimestampTz(int seed) {
        return OffsetDateTime.of(2001, 1, 1, 0, 0, 0, 0, java.time.ZoneOffset.UTC).plusMinutes(seed);
    }

    protected Long generateTestData4colInterval(int seed) {
        return (long) seed;
    }

    protected byte[] generateTestData4colBytea(int seed) {
        return new byte[]{(byte)seed, (byte)(seed >> 8), (byte)(seed >> 16), (byte)(seed >> 24)};
    }

    protected UUID generateTestData4colUuid(int seed) {
        return UUID.fromString("9529478b-20d7-4232-ba79-"+String.format("%012d", seed));
    }

    protected String generateTestData4colJson(int seed) {
        return "{\"id\": %d}".formatted(seed);
    }

    protected String generateTestData4colJsonb(int seed) {
        return "{\"id\": %d}".formatted(seed);
    }

    protected String generateTestData4colXml(int seed) {
        return "<xml>%s</xml>".formatted(seed);
    }

    protected String generateTestData4colInet(int seed) {
        return String.format("%d.%d.%d.%d", (seed >> 24) & 0xFF, (seed >> 16) & 0xFF, (seed >> 8) & 0xFF, seed & 0xFF);
    }

    protected String generateTestData4colCidr(int seed) {
        return String.format("%d.%d.%d.%d/32", (seed >> 24) & 0xFF, (seed >> 16) & 0xFF, (seed >> 8) & 0xFF, seed & 0xFF);
    }

    protected String generateTestData4colMacaddr(int seed) {
        return String.format("00:00:%02x:%02x:%02x:%02x", (seed >> 24) & 0xFF, (seed >> 16) & 0xFF, (seed >> 8) & 0xFF, seed & 0xFF);
    }

    protected String generateTestData4colBox(int seed) {
        return "(1,%d),(0,0)".formatted(seed);
    }

    protected String generateTestData4colPoint(int seed) {
        return "(0,%d)".formatted(seed);
    }

    protected String generateTestData4colLine(int seed) {
        return "{1,-1,%d}".formatted(seed);
    }

    protected String generateTestData4colLseg(int seed) {
        return "[(1,%d),(0,0)]".formatted(seed);
    }

    protected String generateTestData4colPath(int seed) {
        return "((2,%d),(1,1),(0,0))".formatted(seed);
    }

    protected String generateTestData4colPolygon(int seed) {
        return "((2,%d),(1,1),(0,0))".formatted(seed);
    }

    protected String generateTestData4colCircle(int seed) {
        return "<(0,0),%d>".formatted(seed);
    }

    protected StatusEnum generateTestData4colStatusEnum(int seed) {
        return pickBySeed(jp.green_code.spring_jdbc_codegen.test_app.StatusEnum.class, seed);
    }


    protected void assert4pk(Long expected, Long value) {
        assertEquals(expected, value);
    }

    protected void assert4colSmallint(Short expected, Short value) {
        assertEquals(expected, value);
    }

    protected void assert4colSmallserial(Short expected, Short value) {
        assertEquals(expected, value);
    }

    protected void assert4colInteger(Integer expected, Integer value) {
        assertEquals(expected, value);
    }

    protected void assert4colSerial(Integer expected, Integer value) {
        assertEquals(expected, value);
    }

    protected void assert4colBigint(Long expected, Long value) {
        assertEquals(expected, value);
    }

    protected void assert4colBigserial(Long expected, Long value) {
        assertEquals(expected, value);
    }

    protected void assert4colReal(Float expected, Float value) {
        assertEquals(expected, value);
    }

    protected void assert4colDoublePrecision(Double expected, Double value) {
        assertEquals(expected, value);
    }

    protected void assert4colNumeric(BigDecimal expected, BigDecimal value) {
        assertEquals(0, expected.compareTo(value));
    }

    protected void assert4colBoolean(Boolean expected, Boolean value) {
        assertEquals(expected, value);
    }

    protected void assert4colChar(String expected, String value) {
        assertEquals(expected, value.trim());
    }

    protected void assert4colVarchar(String expected, String value) {
        assertEquals(expected, value.trim());
    }

    protected void assert4colText(String expected, String value) {
        assertEquals(expected, value.trim());
    }

    protected void assert4colDate(LocalDate expected, LocalDate value) {
        assertEquals(expected, value);
    }

    protected void assert4colTime(LocalTime expected, LocalTime value) {
        assertEquals(expected, value);
    }

    protected void assert4colTimeTz(OffsetTime expected, OffsetTime value) {
        assertEquals(expected, value);
    }

    protected void assert4colTimestamp(LocalDateTime expected, LocalDateTime value) {
        assertEquals(expected, value);
    }

    protected void assert4colTimestampTz(OffsetDateTime expected, OffsetDateTime value) {
        assertEquals(expected, value);
    }

    protected void assert4colInterval(Long expected, Long value) {
        assertEquals(expected, value);
    }

    protected void assert4colBytea(byte[] expected, byte[] value) {
        org.junit.jupiter.api.Assertions.assertArrayEquals(expected, value);
    }

    protected void assert4colUuid(UUID expected, UUID value) {
        assertEquals(expected, value);
    }

    protected void assert4colJson(String expected, String value) {
        assertEquals(expected, value);
    }

    protected void assert4colJsonb(String expected, String value) {
        assertEquals(expected, value);
    }

    protected void assert4colXml(String expected, String value) {
        assertEquals(expected, value);
    }

    protected void assert4colInet(String expected, String value) {
        assertEquals(expected, value);
    }

    protected void assert4colCidr(String expected, String value) {
        assertEquals(expected, value);
    }

    protected void assert4colMacaddr(String expected, String value) {
        assertEquals(expected, value);
    }

    protected void assert4colBox(String expected, String value) {
        assertEquals(expected, value);
    }

    protected void assert4colPoint(String expected, String value) {
        assertEquals(expected, value);
    }

    protected void assert4colLine(String expected, String value) {
        assertEquals(expected, value);
    }

    protected void assert4colLseg(String expected, String value) {
        assertEquals(expected, value);
    }

    protected void assert4colPath(String expected, String value) {
        assertEquals(expected, value);
    }

    protected void assert4colPolygon(String expected, String value) {
        assertEquals(expected, value);
    }

    protected void assert4colCircle(String expected, String value) {
        assertEquals(expected, value);
    }

    protected void assert4colStatusEnum(StatusEnum expected, StatusEnum value) {
        assertEquals(expected, value);
    }
}