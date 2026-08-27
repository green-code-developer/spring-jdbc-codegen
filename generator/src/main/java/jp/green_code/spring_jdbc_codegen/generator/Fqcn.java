package jp.green_code.spring_jdbc_codegen.generator;

/**
 * 生成するコードが import する外部ライブラリのFQCN。
 * <p>
 * 生成コード中に文字列で散らばると、依存ライブラリの変更時に追いきれないため
 * ここへ集約する。JDK のクラスは対象外とする。
 */
public final class Fqcn {

    private Fqcn() {
    }

    // JSpecify
    public static final String NULLABLE = "org.jspecify.annotations.Nullable";
    public static final String NULL_MARKED = "org.jspecify.annotations.NullMarked";
    public static final String NULL_UNMARKED = "org.jspecify.annotations.NullUnmarked";

    // Spring JDBC
    public static final String BEAN_PROPERTY_ROW_MAPPER = "org.springframework.jdbc.core.BeanPropertyRowMapper";
    public static final String ROW_MAPPER = "org.springframework.jdbc.core.RowMapper";
    public static final String JDBC_TEMPLATE = "org.springframework.jdbc.core.JdbcTemplate";
    public static final String NAMED_PARAMETER_JDBC_TEMPLATE = "org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate";
    public static final String EMPTY_RESULT_DATA_ACCESS_EXCEPTION = "org.springframework.dao.EmptyResultDataAccessException";

    // Spring Framework
    public static final String COMPONENT = "org.springframework.stereotype.Component";
    public static final String REPOSITORY = "org.springframework.stereotype.Repository";
    public static final String AUTOWIRED = "org.springframework.beans.factory.annotation.Autowired";
    public static final String SPRING_BOOT_TEST = "org.springframework.boot.test.context.SpringBootTest";

    // JUnit
    public static final String TEST = "org.junit.jupiter.api.Test";
    public static final String ASSERT_TRUE = "org.junit.jupiter.api.Assertions.assertTrue";
    public static final String ASSERT_EQUALS = "org.junit.jupiter.api.Assertions.assertEquals";

    // その他
    public static final String DATA_SOURCE = "javax.sql.DataSource";

    /** FQCN から単純名を取り出して {@code @単純名} の形にする */
    public static String toAnnotation(String fqcn) {
        return "@" + fqcn.substring(fqcn.lastIndexOf('.') + 1);
    }
}
