package jp.green_code.dbcodegen;

import org.junit.jupiter.api.Test;

public class TestMain {
    @Test
    void test() throws Exception {
        new DbCodeGenRunner().run(null);
    }
}
