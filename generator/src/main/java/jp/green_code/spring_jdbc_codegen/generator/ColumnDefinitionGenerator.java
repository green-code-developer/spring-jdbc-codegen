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
        var nullable = "";

        sb.add("package %s;".formatted(param.repositoryPackage));
        sb.add("");

        sb.add("import %s.%s;".formatted(param.baseRepositoryPackage(), param.toBaseColumnDefinitionClassName()));
        if (param.enableNullUnmarkedForEntityPackages) {
            sb.add("import %s;".formatted(Fqcn.NULLABLE));
            nullable = Fqcn.toAnnotation(Fqcn.NULLABLE) + " ";
        }
        sb.add("");

        sb.add("public class %s extends %s {".formatted(param.columnDefinitionClassName, param.toBaseColumnDefinitionClassName()));
        sb.add("    public %s(String columnName, String javaPropertyName, String javaFqcn, String dbTypeName, Integer jdbcType, Integer columnSize, %sInteger primaryKeySeq, boolean nullable, boolean hasDefault, %sString dbParamTemplate, %sString dbSelectTemplate, boolean isSetNow, boolean hasNameMapping) {".formatted(param.columnDefinitionClassName, nullable, nullable, nullable));
        sb.add("        super(columnName, javaPropertyName, javaFqcn, dbTypeName, jdbcType, columnSize, primaryKeySeq, nullable, hasDefault, dbParamTemplate, dbSelectTemplate, isSetNow, hasNameMapping);");
        sb.add("    }");
        sb.add("}");
        return join("\n", sb);
    }
}
