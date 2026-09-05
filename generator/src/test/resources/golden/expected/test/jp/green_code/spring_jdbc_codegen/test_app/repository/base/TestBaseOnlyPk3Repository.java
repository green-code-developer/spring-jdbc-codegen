package jp.green_code.spring_jdbc_codegen.test_app.repository.base;

import java.time.OffsetDateTime;
import java.util.UUID;
import jp.green_code.spring_jdbc_codegen.test_app.entity.OnlyPk3Entity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class TestBaseOnlyPk3Repository {

    protected void test(BaseOnlyPk3Repository repository) {
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

        // PK 以外のカラムがないのでupdate のテストは行わない

        // delete
        var deleteCount = repository.deleteByPk(data.getPk1(), data.getPk2(), data.getPk3());
        assertEquals(1, deleteCount);
        // select 3回目
        var stored3 = repository.findByPk(data.getPk1(), data.getPk2(), data.getPk3());
        assertTrue(stored3.isEmpty());
    }


    public OnlyPk3Entity generateTestData(int seed) {
        var entity = new OnlyPk3Entity();
        entity.setPk1(generateTestData4pk1(seed++));
        entity.setPk2(generateTestData4pk2(seed++));
        entity.setPk3(generateTestData4pk3(seed));
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


    protected void assert4pk1(Long expected, Long value) {
        assertEquals(expected, value);
    }

    protected void assert4pk2(OffsetDateTime expected, OffsetDateTime value) {
        assertEquals(expected, value);
    }

    protected void assert4pk3(UUID expected, UUID value) {
        assertEquals(expected, value);
    }
}