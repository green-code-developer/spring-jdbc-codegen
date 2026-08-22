package jp.green_code.spring_jdbc_codegen.test_app.repository.base;

import java.time.OffsetDateTime;
import jp.green_code.spring_jdbc_codegen.test_app.entity.NowPk0Entity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class TestBaseNowPk0Repository {

    protected void test(BaseNowPk0Repository repository) {
        var seed = 1;
        var data = generateTestData(seed);

        // insert
        repository.insert(data);

        // PK がないのでselect, update, delete のテストは行わない
    }


    public NowPk0Entity generateTestData(int seed) {
        var entity = new NowPk0Entity();
        entity.setColNow(generateTestData4colNow(seed));
        return entity;
    }

    protected OffsetDateTime generateTestData4colNow(int seed) {
        return OffsetDateTime.of(2001, 1, 1, 0, 0, 0, 0, java.time.ZoneOffset.UTC).plusMinutes(seed);
    }


    protected void assert4colNow(OffsetDateTime expected, OffsetDateTime value) {
        assertEquals(expected, value);
    }
}