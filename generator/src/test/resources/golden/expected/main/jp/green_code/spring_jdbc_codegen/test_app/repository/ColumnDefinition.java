package jp.green_code.spring_jdbc_codegen.test_app.repository;

import jp.green_code.spring_jdbc_codegen.test_app.repository.base.BaseColumnDefinition;
import org.jspecify.annotations.Nullable;

public class ColumnDefinition extends BaseColumnDefinition {
    public ColumnDefinition(String columnName, String javaPropertyName, String javaFqcn, String dbTypeName, Integer jdbcType, Integer columnSize, @Nullable Integer primaryKeySeq, boolean nullable, boolean hasDefault, @Nullable String dbParamTemplate, @Nullable String dbSelectTemplate, boolean isSetNow, boolean hasNameMapping) {
        super(columnName, javaPropertyName, javaFqcn, dbTypeName, jdbcType, columnSize, primaryKeySeq, nullable, hasDefault, dbParamTemplate, dbSelectTemplate, isSetNow, hasNameMapping);
    }
}