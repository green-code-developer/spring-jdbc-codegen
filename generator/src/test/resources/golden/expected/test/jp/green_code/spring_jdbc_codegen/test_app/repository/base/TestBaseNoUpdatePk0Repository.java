package jp.green_code.spring_jdbc_codegen.test_app.repository.base;

import jp.green_code.spring_jdbc_codegen.test_app.entity.NoUpdatePk0Entity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class TestBaseNoUpdatePk0Repository {

    protected void test(BaseNoUpdatePk0Repository repository) {
        var seed = 1;
        var data = generateTestData(seed);

        // insert
        repository.insert(data);

        // PK がないのでselect, update, delete のテストは行わない
    }


    public NoUpdatePk0Entity generateTestData(int seed) {
        var entity = new NoUpdatePk0Entity();
        entity.setColNoUpdateTextNotNullDefaultX(generateTestData4colNoUpdateTextNotNullDefaultX(seed));
        return entity;
    }

    protected String generateTestData4colNoUpdateTextNotNullDefaultX(int seed) {
        return String.valueOf(seed);
    }


    protected void assert4colNoUpdateTextNotNullDefaultX(String expected, String value) {
        assertEquals(expected, value.trim());
    }
}