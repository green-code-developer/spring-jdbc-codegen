package jp.green_code.spring_jdbc_codegen.test_app.repository.base;

import java.lang.Long;
import java.lang.String;
import jp.green_code.spring_jdbc_codegen.test_app.entity.NoUpdatePk1Entity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class TestBaseNoUpdatePk1Repository {

    protected void test(BaseNoUpdatePk1Repository repository) {
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
        assert4colNoUpdateTextNotNullDefaultX(data.getColNoUpdateTextNotNullDefaultX(), stored.getColNoUpdateTextNotNullDefaultX());

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

        // col_no_update_text_not_null_default_x はupdate 対象外のため変更前と変わらないことを確認
        assert4colNoUpdateTextNotNullDefaultX(stored.getColNoUpdateTextNotNullDefaultX(), stored2.getColNoUpdateTextNotNullDefaultX());

        // delete
        var deleteCount = repository.deleteByPk(data2.getPk());
        assertEquals(1, deleteCount);
        // select 3回目
        var stored3 = repository.findByPk(data2.getPk());
        assertTrue(stored3.isEmpty());
    }


    public NoUpdatePk1Entity generateTestData(int seed) {
        var entity = new NoUpdatePk1Entity();
        entity.setPk(generateTestData4pk(seed++));
        entity.setColNoUpdateTextNotNullDefaultX(generateTestData4colNoUpdateTextNotNullDefaultX(seed));
        return entity;
    }

    protected Long generateTestData4pk(int seed) {
        return (long) seed;
    }

    protected String generateTestData4colNoUpdateTextNotNullDefaultX(int seed) {
        return String.valueOf(seed);
    }


    protected void assert4pk(Long expected, Long value) {
        assertEquals(expected, value);
    }

    protected void assert4colNoUpdateTextNotNullDefaultX(String expected, String value) {
        assertEquals(expected, value.trim());
    }
}