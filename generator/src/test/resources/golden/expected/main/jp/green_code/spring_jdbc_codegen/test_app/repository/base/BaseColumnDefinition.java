package jp.green_code.spring_jdbc_codegen.test_app.repository.base;

import org.jspecify.annotations.Nullable;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class BaseColumnDefinition {
    /** DB カラム名 */
    private final String columnName;
    /** Java フィールド名 */
    private final String javaPropertyName;
    /** Java 型 */
    private final String javaFqcn;
    /** DB カラム型 */
    private final String dbTypeName;
    /** DB jdbc type */
    private final Integer jdbcType;
    /** DB カラムサイズ */
    private final Integer columnSize;
    /** DB プライマリーキー順番（プライマリーキーでなければnull）*/
    @Nullable
    private final Integer primaryKeySeq;
    /** DB null許可 */
    private final boolean nullable;
    /** DB デフォルト値あり */
    private final boolean hasDefault;
    /** Javaフィールド名と型キャスト用のテンプレート（内部用） */
    @Nullable
    private final String dbParamTemplate;
    /** カラム名と型キャスト用のテンプレート（内部用） */
    @Nullable
    private final String dbSelectTemplate;
    /** now() で上書きを行う */
    private final boolean isSetNow;
    /** Update 対象外 */
    private final boolean shouldSkipInUpdate;
    /** カラム名とJava プロパティ名の明示的マッピング */
    private final boolean hasNameMapping;

    public BaseColumnDefinition(String columnName, String javaPropertyName, String javaFqcn, String dbTypeName, Integer jdbcType, Integer columnSize, @Nullable Integer primaryKeySeq, boolean nullable, boolean hasDefault, @Nullable String dbParamTemplate, @Nullable String dbSelectTemplate, boolean isSetNow, boolean shouldSkipInUpdate, boolean hasNameMapping) {
        this.columnName = columnName;
        this.javaPropertyName = javaPropertyName;
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
        this.hasNameMapping = hasNameMapping;
    }

    public String getColumnName() {
        return columnName;
    }

    public String getJavaPropertyName() {
        return javaPropertyName;
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

    @Nullable
    public Integer getPrimaryKeySeq() {
        return primaryKeySeq;
    }

    public boolean isNullable() {
        return nullable;
    }

    public boolean isHasDefault() {
        return hasDefault;
    }

    @Nullable
    public String getDbParamTemplate() {
        return dbParamTemplate;
    }

    @Nullable
    public String getDbSelectTemplate() {
        return dbSelectTemplate;
    }

    public boolean getHasNameMapping() {
        return hasNameMapping;
    }

    /** Javaフィールド名と型キャスト */
    public String toParamColumn() {
        if (isBlank(dbParamTemplate)) {
            return ":" + javaPropertyName;
        } else {
            return dbParamTemplate.replace("{javaPropertyName}", javaPropertyName);
        }
    }

    /** カラム名と型キャスト */
    public String toSelectColumn() {
        var template = isBlank(dbSelectTemplate) ? "{columnName}" : dbSelectTemplate;
        return template.replace("{columnName}", "\"" + columnName + "\"");
    }

    public boolean isSetNow() {
        return isSetNow;
    }

    public boolean isShouldSkipInUpdate() {
        return shouldSkipInUpdate;
    }

    @Override
    public String toString() {
        return getColumnName();
    }

    public String toUpdateSetClause() {
        var value = isSetNow() ? "now()" : toParamColumn();
        return "\"%s\" = %s".formatted(getColumnName(), value);
    }
}