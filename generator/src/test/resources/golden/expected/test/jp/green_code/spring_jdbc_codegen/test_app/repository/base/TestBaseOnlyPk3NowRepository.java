package jp.green_code.spring_jdbc_codegen.test_app.repository.base;

import java.time.OffsetDateTime;
import jp.green_code.spring_jdbc_codegen.test_app.entity.OnlyPk3NowEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class TestBaseOnlyPk3NowRepository {

    protected void test(BaseOnlyPk3NowRepository repository) {
        var seed = 1;
        var data = generateTestData(seed);

        // insert
        repository.insertAllColumns(data);

        // select 1回目
        var res = repository.findByPk(data.getPk1(), data.getPk2Now(), data.getPk3());
        assertTrue(res.isPresent());

        // insert 後の確認
        var stored = res.orElseThrow();
        assert4pk1(data.getPk1(), stored.getPk1());
        assert4pk2Now(data.getPk2Now(), stored.getPk2Now());
        assert4pk3(data.getPk3(), stored.getPk3());

        // PK 以外のカラムがないのでupdate のテストは行わない

        // delete
        var deleteCount = repository.deleteByPk(data.getPk1(), data.getPk2Now(), data.getPk3());
        assertEquals(1, deleteCount);
        // select 3回目
        var stored3 = repository.findByPk(data.getPk1(), data.getPk2Now(), data.getPk3());
        assertTrue(stored3.isEmpty());
    }


    public OnlyPk3NowEntity generateTestData(int seed) {
        var entity = new OnlyPk3NowEntity();
        entity.setPk1(generateTestData4pk1(seed++));
        entity.setPk2Now(generateTestData4pk2Now(seed++));
        entity.setPk3(generateTestData4pk3(seed));
        return entity;
    }

    protected OffsetDateTime generateTestData4pk1(int seed) {
        return OffsetDateTime.of(2001, 1, 1, 0, 0, 0, 0, java.time.ZoneOffset.UTC).plusMinutes(seed);
    }

    protected OffsetDateTime generateTestData4pk2Now(int seed) {
        return OffsetDateTime.of(2001, 1, 1, 0, 0, 0, 0, java.time.ZoneOffset.UTC).plusMinutes(seed);
    }

    protected OffsetDateTime generateTestData4pk3(int seed) {
        return OffsetDateTime.of(2001, 1, 1, 0, 0, 0, 0, java.time.ZoneOffset.UTC).plusMinutes(seed);
    }


    protected void assert4pk1(OffsetDateTime expected, OffsetDateTime value) {
        assertEquals(expected, value);
    }

    protected void assert4pk2Now(OffsetDateTime expected, OffsetDateTime value) {
        assertEquals(expected, value);
    }

    protected void assert4pk3(OffsetDateTime expected, OffsetDateTime value) {
        assertEquals(expected, value);
    }
}