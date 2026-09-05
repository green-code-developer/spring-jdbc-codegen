package jp.green_code.spring_jdbc_codegen.test_app.repository.base;

import java.time.LocalDateTime;
import jp.green_code.spring_jdbc_codegen.test_app.entity.日本語tableEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class TestBase日本語tableRepository {

    protected void test(Base日本語tableRepository repository) {
        var seed = 1;
        var data = generateTestData(seed);

        // insert
        repository.insertAllColumns(data);

        // select 1回目
        var res = repository.findByPk(data.getOrder(), data.getParam(), data.getSql(), data.getHelper(), data.getJoining(), data.getList(), data.getRenamedJavaName());
        assertTrue(res.isPresent());

        // insert 後の確認
        var stored = res.orElseThrow();
        assert4order(data.getOrder(), stored.getOrder());
        assert4param(data.getParam(), stored.getParam());
        assert4sql(data.getSql(), stored.getSql());
        assert4helper(data.getHelper(), stored.getHelper());
        assert4joining(data.getJoining(), stored.getJoining());
        assert4list(data.getList(), stored.getList());
        assert4renamedJavaName(data.getRenamedJavaName(), stored.getRenamedJavaName());
        assert4where(data.getWhere(), stored.getWhere());
        assert4select(data.getSelect(), stored.getSelect());
        assert4abc(data.getAbc(), stored.getAbc());

        // update
        seed++;
        var data2 = generateTestData(seed);
        data2.setOrder(data.getOrder());
        data2.setParam(data.getParam());
        data2.setSql(data.getSql());
        data2.setHelper(data.getHelper());
        data2.setJoining(data.getJoining());
        data2.setList(data.getList());
        data2.setRenamedJavaName(data.getRenamedJavaName());
        repository.updateAllColumns(data2);

        // select 2回目
        var res2 = repository.findByPk(data2.getOrder(), data2.getParam(), data2.getSql(), data2.getHelper(), data2.getJoining(), data2.getList(), data2.getRenamedJavaName());
        assertTrue(res2.isPresent());

        // update 後の確認
        var stored2 = res2.orElseThrow();

        assert4order(data2.getOrder(), stored2.getOrder());

        assert4param(data2.getParam(), stored2.getParam());

        assert4sql(data2.getSql(), stored2.getSql());

        assert4helper(data2.getHelper(), stored2.getHelper());

        assert4joining(data2.getJoining(), stored2.getJoining());

        assert4list(data2.getList(), stored2.getList());

        assert4renamedJavaName(data2.getRenamedJavaName(), stored2.getRenamedJavaName());

        assert4where(data2.getWhere(), stored2.getWhere());

        assert4select(data2.getSelect(), stored2.getSelect());

        assert4abc(data2.getAbc(), stored2.getAbc());

        // delete
        var deleteCount = repository.deleteByPk(data2.getOrder(), data2.getParam(), data2.getSql(), data2.getHelper(), data2.getJoining(), data2.getList(), data2.getRenamedJavaName());
        assertEquals(1, deleteCount);
        // select 3回目
        var stored3 = repository.findByPk(data2.getOrder(), data2.getParam(), data2.getSql(), data2.getHelper(), data2.getJoining(), data2.getList(), data2.getRenamedJavaName());
        assertTrue(stored3.isEmpty());
    }


    public 日本語tableEntity generateTestData(int seed) {
        var entity = new 日本語tableEntity();
        entity.setOrder(generateTestData4order(seed++));
        entity.setParam(generateTestData4param(seed++));
        entity.setSql(generateTestData4sql(seed++));
        entity.setHelper(generateTestData4helper(seed++));
        entity.setJoining(generateTestData4joining(seed++));
        entity.setList(generateTestData4list(seed++));
        entity.setRenamedJavaName(generateTestData4renamedJavaName(seed++));
        entity.setWhere(generateTestData4where(seed++));
        entity.setSelect(generateTestData4select(seed++));
        entity.setAbc(generateTestData4abc(seed));
        return entity;
    }

    protected Long generateTestData4order(int seed) {
        return (long) seed;
    }

    protected Long generateTestData4param(int seed) {
        return (long) seed;
    }

    protected Long generateTestData4sql(int seed) {
        return (long) seed;
    }

    protected Long generateTestData4helper(int seed) {
        return (long) seed;
    }

    protected Long generateTestData4joining(int seed) {
        return (long) seed;
    }

    protected Long generateTestData4list(int seed) {
        return (long) seed;
    }

    protected String generateTestData4renamedJavaName(int seed) {
        return String.valueOf(seed);
    }

    protected LocalDateTime generateTestData4where(int seed) {
        return LocalDateTime.of(2001, 1, 1, 0, 0, 0).plusMinutes(seed);
    }

    protected String generateTestData4select(int seed) {
        return String.valueOf(seed);
    }

    protected String generateTestData4abc(int seed) {
        return String.valueOf(seed);
    }


    protected void assert4order(Long expected, Long value) {
        assertEquals(expected, value);
    }

    protected void assert4param(Long expected, Long value) {
        assertEquals(expected, value);
    }

    protected void assert4sql(Long expected, Long value) {
        assertEquals(expected, value);
    }

    protected void assert4helper(Long expected, Long value) {
        assertEquals(expected, value);
    }

    protected void assert4joining(Long expected, Long value) {
        assertEquals(expected, value);
    }

    protected void assert4list(Long expected, Long value) {
        assertEquals(expected, value);
    }

    protected void assert4renamedJavaName(String expected, String value) {
        assertEquals(expected, value.trim());
    }

    protected void assert4where(LocalDateTime expected, LocalDateTime value) {
        assertEquals(expected, value);
    }

    protected void assert4select(String expected, String value) {
        assertEquals(expected, value.trim());
    }

    protected void assert4abc(String expected, String value) {
        assertEquals(expected, value.trim());
    }
}