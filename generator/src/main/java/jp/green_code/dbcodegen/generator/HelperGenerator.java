package jp.green_code.dbcodegen.generator;

import jp.green_code.dbcodegen.DbCodeGenParameter;

public class HelperGenerator {
    final DbCodeGenParameter param;

    public HelperGenerator(DbCodeGenParameter param) {
        this.param = param;
    }

    public String generateHelper() {
        var sb = """
                package %s;
                
                import org.springframework.jdbc.core.BeanPropertyRowMapper;
                import org.springframework.jdbc.core.JdbcTemplate;
                import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
                import org.springframework.stereotype.Component;

                import javax.sql.DataSource;
                import java.util.List;
                import java.util.Map;
                import java.util.Optional;

                import static java.lang.String.join;
                import static org.apache.commons.lang3.StringUtils.isBlank;
                
                @Component
                public class %s {
                    public final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
                
                    public %s(DataSource dataSource) {
                        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(new JdbcTemplate(dataSource));
                    }
                
                    public <T> List<T> list(List<String> sql, Map<String, ?> param, Class<T> clazz) {
                        return list(join(" ", sql), param, clazz);
                    }
                
                    public <T> List<T> list(String sql, Map<String, ?> param, Class<T> clazz) {
                        if (clazz.isPrimitive() || Number.class.isAssignableFrom(clazz) || clazz == String.class) {
                            return namedParameterJdbcTemplate.queryForList(sql, param, clazz);
                        } else {
                            return namedParameterJdbcTemplate.query(sql, param, new BeanPropertyRowMapper<>(clazz));
                        }
                    }
                
                    public int exec(List<String> sql, Map<String, ?> param) {
                        return exec(join(" ", sql), param);
                    }
                
                    public int exec(String sql, Map<String, ?> param) {
                        return namedParameterJdbcTemplate.update(sql, param);
                    }
                
                    public <T> T single(List<String> sql, Map<String, ?> param, Class<T> clazz) {
                        return single(join(" ", sql), param, clazz);
                    }
                
                    public <T> T single(String sql, Map<String, ?> param, Class<T> clazz) {
                        if (clazz.isPrimitive() || Number.class.isAssignableFrom(clazz) || clazz == String.class) {
                            return namedParameterJdbcTemplate.queryForObject(sql, param, clazz);
                        } else {
                            return namedParameterJdbcTemplate.queryForObject(sql, param, new BeanPropertyRowMapper<>(clazz));
                        }
                    }
                
                    public <T> Optional<T> optional(List<String> sql, Map<String, ?> param, Class<T> clazz) {
                        return list(sql, param, clazz).stream().findFirst();
                    }
                
                    public <T> Optional<T> optional(String sql, Map<String, ?> param, Class<T> clazz) {
                        return list(sql, param, clazz).stream().findFirst();
                    }
                
                    public long count(List<String> sql, Map<String, ?> param) {
                        return count(join(" ", sql), param);
                    }
                
                    public long count(String sql, Map<String, ?> param) {
                        return optional(sql, param, Long.class).orElseThrow();
                    }
                
                    public static <E extends Enum<E>> E pickBySeed(Class<E> enumClass, int seed) {
                        E[] values = enumClass.getEnumConstants();
                        int index = Math.floorMod(seed, values.length);
                        return values[index];
                    }
                
                    public static String toCamelCase(String snake, boolean upperFirst) {
                        StringBuilder sb = new StringBuilder();
                        boolean upper = upperFirst;
            
                        for (char c : snake.toCharArray()) {
                            if (c == '_' || c == '-') {
                                upper = true;
                            } else {
                                sb.append(upper ? Character.toUpperCase(c) : Character.toLowerCase(c));
                                upper = false;
                            }
                        }
                        return sb.toString();
                    }
                
                    public static class ColumnDefinition {
                        /** DB カラム名 */
                        private final String columnName;
                        /** Java フィールド名 */
                        private final String javaFieldName;
                        /** Java 型 */
                        private final String javaFqcn;
                        /** DB カラム型 */
                        private final String dbTypeName;
                        /** DB jdbc type */
                        private final Integer jdbcType;
                        /** DB カラムサイズ */
                        private final Integer columnSize;
                        /** DB プライマリーキー順番（プライマリーキーでなければnull）*/
                        private final Integer primaryKeySeq;
                        /** DB null許可 */
                        private final boolean nullable;
                        /** DB デフォルト値あり */
                        private final boolean hasDefault;
                        /** Javaフィールド名と型キャスト用のテンプレート（内部用） */
                        private final String dbParamTemplate;
                        /** カラム名と型キャスト用のテンプレート（内部用） */
                        private final String dbSelectTemplate;
                        /** now() で上書きを行う */
                        private final boolean isSetNow;
                        /** Update 対象外 */
                        private final boolean shouldSkipInUpdate;
                
                        public ColumnDefinition(String columnName, String javaFieldName, String javaFqcn, String dbTypeName, Integer jdbcType, Integer columnSize, Integer primaryKeySeq, boolean nullable, boolean hasDefault, String dbParamTemplate, String dbSelectTemplate, boolean isSetNow, boolean shouldSkipInUpdate) {
                            this.columnName = columnName;
                            this.javaFieldName = javaFieldName;
                            this.javaFqcn = javaFqcn;
                            this.dbTypeName = dbTypeName;
                            this.jdbcType = jdbcType;
                            this.columnSize = columnSize;
                            this.primaryKeySeq = primaryKeySeq;
                            this.nullable = nullable;
                            this.hasDefault = hasDefault;
                            this.dbParamTemplate = dbParamTemplate;
                            this.dbSelectTemplate = dbSelectTemplate;
                            this.isSetNow = isSetNow;
                            this.shouldSkipInUpdate = shouldSkipInUpdate;
                        }
                
                        public String getColumnName() {
                            return columnName;
                        }
                
                        public String getJavaFieldName() {
                            return javaFieldName;
                        }
                
                        public String getJavaFqcn() {
                            return javaFqcn;
                        }
                
                        public String getDbTypeName() {
                            return dbTypeName;
                        }
                
                        public Integer getJdbcType() {
                            return jdbcType;
                        }
                
                        public Integer getColumnSize() {
                            return columnSize;
                        }
                
                        public Integer getPrimaryKeySeq() {
                            return primaryKeySeq;
                        }
                
                        public boolean isNullable() {
                            return nullable;
                        }
                
                        public boolean isHasDefault() {
                            return hasDefault;
                        }
                
                        public String getDbParamTemplate() {
                            return dbParamTemplate;
                        }
                
                        public String getDbSelectTemplate() {
                            return dbSelectTemplate;
                        }

                        /** Javaフィールド名と型キャスト */
                        public String toParamColumn() {
                            if (isBlank(dbParamTemplate)) {
                                return ":" + javaFieldName;
                            } else {
                                return dbParamTemplate.replace("{javaFieldName}", javaFieldName);
                            }
                        }
                
                        /** カラム名と型キャスト */
                        public String toSelectColumn() {
                            var template = isBlank(dbSelectTemplate) ? "{columnName}" : dbSelectTemplate;
                            return template.replace("{columnName}", "\\"" + columnName + "\\"");
                        }
                
                        public boolean isSetNow() {
                            return isSetNow;
                        }
                
                        public boolean isShouldSkipInUpdate() {
                            return shouldSkipInUpdate;
                        }
                    }
                }
                """;
        return sb.formatted(param.baseRepositoryPackage(), param.repositoryHelperClassName, param.repositoryHelperClassName);
    }
}
