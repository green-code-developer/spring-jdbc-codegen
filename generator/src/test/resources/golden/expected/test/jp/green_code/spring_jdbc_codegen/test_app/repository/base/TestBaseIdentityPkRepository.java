package jp.green_code.spring_jdbc_codegen.test_app.repository.base;

import jp.green_code.spring_jdbc_codegen.test_app.entity.IdentityPkEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class TestBaseIdentityPkRepository {

    protected void test(BaseIdentityPkRepository repository) {
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
        assert4colText(data.getColText(), stored.getColText());
        assert4colTextNotNull(data.getColTextNotNull(), stored.getColTextNotNull());

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
        assert4colText(data2.getColText(), stored2.getColText());
        assert4colTextNotNull(data2.getColTextNotNull(), stored2.getColTextNotNull());

        // delete
        var deleteCount = repository.deleteByPk(data2.getPk());
        assertEquals(1, deleteCount);
        // select 3回目
        var stored3 = repository.findByPk(data2.getPk());
        assertTrue(stored3.isEmpty());
    }


    public IdentityPkEntity generateTestData(int seed) {
        var entity = new IdentityPkEntity();
        entity.setPk(generateTestData4pk(seed++));
        entity.setColText(generateTestData4colText(seed++));
        entity.setColTextNotNull(generateTestData4colTextNotNull(seed));
        return entity;
    }

    protected Long generateTestData4pk(int seed) {
        return (long) seed;
    }

    protected String generateTestData4colText(int seed) {
        return String.valueOf(seed);
    }

    protected String generateTestData4colTextNotNull(int seed) {
        return String.valueOf(seed);
    }


    protected void assert4pk(Long expected, Long value) {
        assertEquals(expected, value);
    }

    protected void assert4colText(String expected, String value) {
        assertEquals(expected, value.trim());
    }

    protected void assert4colTextNotNull(String expected, String value) {
        assertEquals(expected, value.trim());
    }
}