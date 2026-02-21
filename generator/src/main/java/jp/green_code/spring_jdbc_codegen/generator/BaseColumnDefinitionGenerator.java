package jp.green_code.spring_jdbc_codegen.generator;

import jp.green_code.spring_jdbc_codegen.Parameter;

public class BaseColumnDefinitionGenerator {
    final Parameter param;

    public BaseColumnDefinitionGenerator(Parameter param) {
        this.param = param;
    }

    public String generateHelper() {
        var sb = """
                package %s;
                
                import static org.apache.commons.lang3.StringUtils.isBlank;
                
                public class %s {
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
                
                    public %s(String columnName, String javaFieldName, String javaFqcn, String dbTypeName, Integer jdbcType, Integer columnSize, Integer primaryKeySeq, boolean nullable, boolean hasDefault, String dbParamTemplate, String dbSelectTemplate, boolean isSetNow, boolean shouldSkipInUpdate) {
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
                
                    public String toString() {
                        return getColumnName();
                    }
                }
                """;
        return sb.formatted(param.baseRepositoryPackage(), param.toBaseColumnDefinitionClassName(), param.toBaseColumnDefinitionClassName());
    }
}
