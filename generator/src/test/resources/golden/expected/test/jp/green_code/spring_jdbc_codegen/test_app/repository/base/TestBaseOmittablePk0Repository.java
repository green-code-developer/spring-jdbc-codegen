package jp.green_code.spring_jdbc_codegen.test_app.repository.base;

import java.lang.String;
import jp.green_code.spring_jdbc_codegen.test_app.entity.OmittablePk0Entity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class TestBaseOmittablePk0Repository {

    protected void test(BaseOmittablePk0Repository repository) {
        var seed = 1;
        var data = generateTestData(seed);

        // insert
        repository.insert(data);

        // PK がないのでselect, update, delete のテストは行わない
    }


    public OmittablePk0Entity generateTestData(int seed) {
        var entity = new OmittablePk0Entity();
        entity.setColTextNotNullDefaultX(generateTestData4colTextNotNullDefaultX(seed));
        return entity;
    }

    protected String generateTestData4colTextNotNullDefaultX(int seed) {
        return String.valueOf(seed);
    }


    protected void assert4colTextNotNullDefaultX(String expected, String value) {
        assertEquals(expected, value.trim());
    }
}