package jp.green_code.spring_jdbc_codegen.generator;

import jp.green_code.spring_jdbc_codegen.Parameter;

import java.util.ArrayList;

import static java.lang.String.join;

public class ColumnDefinitionGenerator {
    final Parameter param;

    public ColumnDefinitionGenerator(Parameter param) {
        this.param = param;
    }

    public String generateColumnDefinition() {
        var sb = new ArrayList<String>();

        sb.add("package %s;".formatted(param.repositoryPackage));
        sb.add("");

        sb.add("import %s.%s;".formatted(param.baseRepositoryPackage(), param.toBaseColumnDefinitionClassName()));
        sb.add("");

        sb.add("public class %s extends %s {".formatted(param.columnDefinitionClassName, param.toBaseColumnDefinitionClassName()));
        sb.add("    public %s(String columnName, String javaPropertyName, String javaFqcn, String dbTypeName, Integer jdbcType, Integer columnSize, Integer primaryKeySeq, boolean nullable, boolean hasDefault, String dbParamTemplate, String dbSelectTemplate, boolean isSetNow, boolean shouldSkipInUpdate, boolean hasNameMapping) {".formatted(param.columnDefinitionClassName));
        sb.add("        super(columnName, javaPropertyName, javaFqcn, dbTypeName, jdbcType, columnSize, primaryKeySeq, nullable, hasDefault, dbParamTemplate, dbSelectTemplate, isSetNow, shouldSkipInUpdate, hasNameMapping);");
        sb.add("    }");
        sb.add("}");
        return join("\n", sb);
    }
}
