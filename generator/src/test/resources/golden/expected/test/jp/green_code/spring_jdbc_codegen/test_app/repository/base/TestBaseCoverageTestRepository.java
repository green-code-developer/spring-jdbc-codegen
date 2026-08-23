package jp.green_code.spring_jdbc_codegen.test_app.repository.base;

import java.lang.Long;
import java.lang.String;
import java.time.OffsetDateTime;
import jp.green_code.spring_jdbc_codegen.test_app.StatusEnum;
import jp.green_code.spring_jdbc_codegen.test_app.entity.CoverageTestEntity;
import static jp.green_code.spring_jdbc_codegen.test_app.repository.base.BaseRepositoryHelper.pickBySeed;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class TestBaseCoverageTestRepository {

    protected void test(BaseCoverageTestRepository repository) {
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
        assert4colNullableDefault(data.getColNullableDefault(), stored.getColNullableDefault());
        assert4colNotnullNodefault(data.getColNotnullNodefault(), stored.getColNotnullNodefault());
        assert4colNowWithDefault(data.getColNowWithDefault(), stored.getColNowWithDefault());
        assert4createdAt(data.getCreatedAt(), stored.getCreatedAt());
        assert4createdBy(data.getCreatedBy(), stored.getCreatedBy());
        assert4updatedAt(data.getUpdatedAt(), stored.getUpdatedAt());
        assert4colNoUpdateNullable(data.getColNoUpdateNullable(), stored.getColNoUpdateNullable());
        assert4colEnumDefault(data.getColEnumDefault(), stored.getColEnumDefault());
        assert4colEnumNullableDefault(data.getColEnumNullableDefault(), stored.getColEnumNullableDefault());
        assert4mappedNullableJavaName(data.getMappedNullableJavaName(), stored.getMappedNullableJavaName());

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

        assert4colNullableDefault(data2.getColNullableDefault(), stored2.getColNullableDefault());

        assert4colNotnullNodefault(data2.getColNotnullNodefault(), stored2.getColNotnullNodefault());

        assert4colNowWithDefault(data2.getColNowWithDefault(), stored2.getColNowWithDefault());

        // created_at はupdate 対象外のため変更前と変わらないことを確認
        assert4createdAt(stored.getCreatedAt(), stored2.getCreatedAt());

        // created_by はupdate 対象外のため変更前と変わらないことを確認
        assert4createdBy(stored.getCreatedBy(), stored2.getCreatedBy());

        assert4updatedAt(data2.getUpdatedAt(), stored2.getUpdatedAt());

        // col_no_update_nullable はupdate 対象外のため変更前と変わらないことを確認
        assert4colNoUpdateNullable(stored.getColNoUpdateNullable(), stored2.getColNoUpdateNullable());

        assert4colEnumDefault(data2.getColEnumDefault(), stored2.getColEnumDefault());

        assert4colEnumNullableDefault(data2.getColEnumNullableDefault(), stored2.getColEnumNullableDefault());

        assert4mappedNullableJavaName(data2.getMappedNullableJavaName(), stored2.getMappedNullableJavaName());

        // delete
        var deleteCount = repository.deleteByPk(data2.getPk());
        assertEquals(1, deleteCount);
        // select 3回目
        var stored3 = repository.findByPk(data2.getPk());
        assertTrue(stored3.isEmpty());
    }


    public CoverageTestEntity generateTestData(int seed) {
        var entity = new CoverageTestEntity();
        entity.setPk(generateTestData4pk(seed++));
        entity.setColNullableDefault(generateTestData4colNullableDefault(seed++));
        entity.setColNotnullNodefault(generateTestData4colNotnullNodefault(seed++));
        entity.setColNowWithDefault(generateTestData4colNowWithDefault(seed++));
        entity.setCreatedAt(generateTestData4createdAt(seed++));
        entity.setCreatedBy(generateTestData4createdBy(seed++));
        entity.setUpdatedAt(generateTestData4updatedAt(seed++));
        entity.setColNoUpdateNullable(generateTestData4colNoUpdateNullable(seed++));
        entity.setColEnumDefault(generateTestData4colEnumDefault(seed++));
        entity.setColEnumNullableDefault(generateTestData4colEnumNullableDefault(seed++));
        entity.setMappedNullableJavaName(generateTestData4mappedNullableJavaName(seed));
        return entity;
    }

    protected Long generateTestData4pk(int seed) {
        return (long) seed;
    }

    protected String generateTestData4colNullableDefault(int seed) {
        return String.valueOf(seed);
    }

    protected String generateTestData4colNotnullNodefault(int seed) {
        return String.valueOf(seed);
    }

    protected OffsetDateTime generateTestData4colNowWithDefault(int seed) {
        return OffsetDateTime.of(2001, 1, 1, 0, 0, 0, 0, java.time.ZoneOffset.UTC).plusMinutes(seed);
    }

    protected OffsetDateTime generateTestData4createdAt(int seed) {
        return OffsetDateTime.of(2001, 1, 1, 0, 0, 0, 0, java.time.ZoneOffset.UTC).plusMinutes(seed);
    }

    protected String generateTestData4createdBy(int seed) {
        return String.valueOf(seed);
    }

    protected OffsetDateTime generateTestData4updatedAt(int seed) {
        return OffsetDateTime.of(2001, 1, 1, 0, 0, 0, 0, java.time.ZoneOffset.UTC).plusMinutes(seed);
    }

    protected String generateTestData4colNoUpdateNullable(int seed) {
        return String.valueOf(seed);
    }

    protected StatusEnum generateTestData4colEnumDefault(int seed) {
        return pickBySeed(jp.green_code.spring_jdbc_codegen.test_app.StatusEnum.class, seed);
    }

    protected StatusEnum generateTestData4colEnumNullableDefault(int seed) {
        return pickBySeed(jp.green_code.spring_jdbc_codegen.test_app.StatusEnum.class, seed);
    }

    protected String generateTestData4mappedNullableJavaName(int seed) {
        return String.valueOf(seed);
    }


    protected void assert4pk(Long expected, Long value) {
        assertEquals(expected, value);
    }

    protected void assert4colNullableDefault(String expected, String value) {
        assertEquals(expected, value.trim());
    }

    protected void assert4colNotnullNodefault(String expected, String value) {
        assertEquals(expected, value.trim());
    }

    protected void assert4colNowWithDefault(OffsetDateTime expected, OffsetDateTime value) {
        assertEquals(expected, value);
    }

    protected void assert4createdAt(OffsetDateTime expected, OffsetDateTime value) {
        assertEquals(expected, value);
    }

    protected void assert4createdBy(String expected, String value) {
        assertEquals(expected, value.trim());
    }

    protected void assert4updatedAt(OffsetDateTime expected, OffsetDateTime value) {
        assertEquals(expected, value);
    }

    protected void assert4colNoUpdateNullable(String expected, String value) {
        assertEquals(expected, value.trim());
    }

    protected void assert4colEnumDefault(StatusEnum expected, StatusEnum value) {
        assertEquals(expected, value);
    }

    protected void assert4colEnumNullableDefault(StatusEnum expected, StatusEnum value) {
        assertEquals(expected, value);
    }

    protected void assert4mappedNullableJavaName(String expected, String value) {
        assertEquals(expected, value.trim());
    }
}