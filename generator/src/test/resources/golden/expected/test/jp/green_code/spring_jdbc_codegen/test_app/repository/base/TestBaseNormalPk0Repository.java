package jp.green_code.spring_jdbc_codegen.test_app.repository.base;

import jp.green_code.spring_jdbc_codegen.test_app.entity.NormalPk0Entity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class TestBaseNormalPk0Repository {

    protected void test(BaseNormalPk0Repository repository) {
        var seed = 1;
        var data = generateTestData(seed);

        // insert
        repository.insert(data);

        // PK がないのでselect, update, delete のテストは行わない
    }


    public NormalPk0Entity generateTestData(int seed) {
        var entity = new NormalPk0Entity();
        entity.setColText(generateTestData4colText(seed++));
        entity.setColTextNotNull(generateTestData4colTextNotNull(seed++));
        entity.setColTextNotNullDefaultX(generateTestData4colTextNotNullDefaultX(seed++));
        entity.setColTextDefaultY(generateTestData4colTextDefaultY(seed));
        return entity;
    }

    protected String generateTestData4colText(int seed) {
        return String.valueOf(seed);
    }

    protected String generateTestData4colTextNotNull(int seed) {
        return String.valueOf(seed);
    }

    protected String generateTestData4colTextNotNullDefaultX(int seed) {
        return String.valueOf(seed);
    }

    protected String generateTestData4colTextDefaultY(int seed) {
        return String.valueOf(seed);
    }


    protected void assert4colText(String expected, String value) {
        assertEquals(expected, value.trim());
    }

    protected void assert4colTextNotNull(String expected, String value) {
        assertEquals(expected, value.trim());
    }

    protected void assert4colTextNotNullDefaultX(String expected, String value) {
        assertEquals(expected, value.trim());
    }

    protected void assert4colTextDefaultY(String expected, String value) {
        assertEquals(expected, value.trim());
    }
}