package jp.green_code.spring_jdbc_codegen.test_app.repository.base;

import jp.green_code.spring_jdbc_codegen.test_app.entity.OnlyPk1Entity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class TestBaseOnlyPk1Repository {

    protected void test(BaseOnlyPk1Repository repository) {
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

        // PK 以外のカラムがないのでupdate のテストは行わない

        // delete
        var deleteCount = repository.deleteByPk(data.getPk());
        assertEquals(1, deleteCount);
        // select 3回目
        var stored3 = repository.findByPk(data.getPk());
        assertTrue(stored3.isEmpty());
    }


    public OnlyPk1Entity generateTestData(int seed) {
        var entity = new OnlyPk1Entity();
        entity.setPk(generateTestData4pk(seed));
        return entity;
    }

    protected Long generateTestData4pk(int seed) {
        return (long) seed;
    }


    protected void assert4pk(Long expected, Long value) {
        assertEquals(expected, value);
    }
}