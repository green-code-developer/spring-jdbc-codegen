package jp.green_code.spring_jdbc_codegen.generator;

import jp.green_code.spring_jdbc_codegen.Parameter;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.join;

public class BaseColumnDefinitionGenerator {
    final Parameter param;

    public BaseColumnDefinitionGenerator(Parameter param) {
        this.param = param;
    }

    public String generateHelper() {
        var sb = new ArrayList<String>();
        var nullable = "";

        sb.add("package %s;".formatted(param.baseRepositoryPackage()));
        sb.add("");
        if (param.enableNullUnmarkedForEntityPackages) {
            sb.add("import %s;".formatted(Fqcn.NULLABLE));
            sb.add("");
            nullable = Fqcn.toAnnotation(Fqcn.NULLABLE) + " ";
        }
        sb.add("import static org.apache.commons.lang3.StringUtils.isBlank;");
        sb.add("");
        sb.add("public class %s {".formatted(param.toBaseColumnDefinitionClassName()));
        sb.add("    /** DB カラム名 */");
        sb.add("    private final String columnName;");
        sb.add("    /** Java フィールド名 */");
        sb.add("    private final String javaPropertyName;");
        sb.add("    /** Java 型 */");
        sb.add("    private final String javaFqcn;");
        sb.add("    /** DB カラム型 */");
        sb.add("    private final String dbTypeName;");
        sb.add("    /** DB jdbc type */");
        sb.add("    private final Integer jdbcType;");
        sb.add("    /** DB カラムサイズ */");
        sb.add("    private final Integer columnSize;");
        sb.add("    /** DB プライマリーキー順番（プライマリーキーでなければnull）*/");
        addNullableIfNeed(sb);
        sb.add("    private final Integer primaryKeySeq;");
        sb.add("    /** DB null許可 */");
        sb.add("    private final boolean nullable;");
        sb.add("    /** DB デフォルト値あり */");
        sb.add("    private final boolean hasDefault;");
        sb.add("    /** Javaフィールド名と型キャスト用のテンプレート（内部用） */");
        addNullableIfNeed(sb);
        sb.add("    private final String dbParamTemplate;");
        sb.add("    /** カラム名と型キャスト用のテンプレート（内部用） */");
        addNullableIfNeed(sb);
        sb.add("    private final String dbSelectTemplate;");
        sb.add("    /** now() で上書きを行う */");
        sb.add("    private final boolean isSetNow;");
        sb.add("    /** Update 対象外 */");
        sb.add("    private final boolean shouldSkipInUpdate;");
        sb.add("    /** カラム名とJava プロパティ名の明示的マッピング */");
        sb.add("    private final boolean hasNameMapping;");
        sb.add("");
        sb.add("    public %s(String columnName, String javaPropertyName, String javaFqcn, String dbTypeName, Integer jdbcType, Integer columnSize, %sInteger primaryKeySeq, boolean nullable, boolean hasDefault, %sString dbParamTemplate, %sString dbSelectTemplate, boolean isSetNow, boolean shouldSkipInUpdate, boolean hasNameMapping) {".formatted(param.toBaseColumnDefinitionClassName(), nullable, nullable, nullable));
        sb.add("        this.columnName = columnName;");
        sb.add("        this.javaPropertyName = javaPropertyName;");
        sb.add("        this.javaFqcn = javaFqcn;");
        sb.add("        this.dbTypeName = dbTypeName;");
        sb.add("        this.jdbcType = jdbcType;");
        sb.add("        this.columnSize = columnSize;");
        sb.add("        this.primaryKeySeq = primaryKeySeq;");
        sb.add("        this.nullable = nullable;");
        sb.add("        this.hasDefault = hasDefault;");
        sb.add("        this.dbParamTemplate = dbParamTemplate;");
        sb.add("        this.dbSelectTemplate = dbSelectTemplate;");
        sb.add("        this.isSetNow = isSetNow;");
        sb.add("        this.shouldSkipInUpdate = shouldSkipInUpdate;");
        sb.add("        this.hasNameMapping = hasNameMapping;");
        sb.add("    }");
        sb.add("");
        sb.add("    public String getColumnName() {");
        sb.add("        return columnName;");
        sb.add("    }");
        sb.add("");
        sb.add("    public String getJavaPropertyName() {");
        sb.add("        return javaPropertyName;");
        sb.add("    }");
        sb.add("");
        sb.add("    public String getJavaFqcn() {");
        sb.add("        return javaFqcn;");
        sb.add("    }");
        sb.add("");
        sb.add("    public String getDbTypeName() {");
        sb.add("        return dbTypeName;");
        sb.add("    }");
        sb.add("");
        sb.add("    public Integer getJdbcType() {");
        sb.add("        return jdbcType;");
        sb.add("    }");
        sb.add("");
        sb.add("    public Integer getColumnSize() {");
        sb.add("        return columnSize;");
        sb.add("    }");
        sb.add("");
        addNullableIfNeed(sb);
        sb.add("    public Integer getPrimaryKeySeq() {");
        sb.add("        return primaryKeySeq;");
        sb.add("    }");
        sb.add("");
        sb.add("    public boolean isNullable() {");
        sb.add("        return nullable;");
        sb.add("    }");
        sb.add("");
        sb.add("    public boolean isHasDefault() {");
        sb.add("        return hasDefault;");
        sb.add("    }");
        sb.add("");
        addNullableIfNeed(sb);
        sb.add("    public String getDbParamTemplate() {");
        sb.add("        return dbParamTemplate;");
        sb.add("    }");
        sb.add("");
        addNullableIfNeed(sb);
        sb.add("    public String getDbSelectTemplate() {");
        sb.add("        return dbSelectTemplate;");
        sb.add("    }");
        sb.add("");
        sb.add("    public boolean getHasNameMapping() {");
        sb.add("        return hasNameMapping;");
        sb.add("    }");
        sb.add("");
        sb.add("    /** Javaフィールド名と型キャスト */");
        sb.add("    public String toParamColumn() {");
        sb.add("        if (isBlank(dbParamTemplate)) {");
        sb.add("            return \":\" + javaPropertyName;");
        sb.add("        } else {");
        sb.add("            return dbParamTemplate.replace(\"{javaPropertyName}\", javaPropertyName);");
        sb.add("        }");
        sb.add("    }");
        sb.add("");
        sb.add("    /** カラム名と型キャスト */");
        sb.add("    public String toSelectColumn() {");
        sb.add("        var template = isBlank(dbSelectTemplate) ? \"{columnName}\" : dbSelectTemplate;");
        sb.add("        return template.replace(\"{columnName}\", \"\\\"\" + columnName + \"\\\"\");");
        sb.add("    }");
        sb.add("");
        sb.add("    public boolean isSetNow() {");
        sb.add("        return isSetNow;");
        sb.add("    }");
        sb.add("");
        sb.add("    public boolean isShouldSkipInUpdate() {");
        sb.add("        return shouldSkipInUpdate;");
        sb.add("    }");
        sb.add("");
        sb.add("    @Override");
        sb.add("    public String toString() {");
        sb.add("        return getColumnName();");
        sb.add("    }");
        sb.add("");
        sb.add("    public String toUpdateSetClause() {");
        sb.add("        var value = isSetNow() ? \"now()\" : toParamColumn();");
        sb.add("        return \"\\\"%s\\\" = %s\".formatted(getColumnName(), value);");
        sb.add("    }");
        sb.add("}");
        return join("\n", sb);
    }

    void addNullableIfNeed(List<String> sb) {
        if (param.enableNullUnmarkedForEntityPackages) {
            sb.add("    " + Fqcn.toAnnotation(Fqcn.NULLABLE));
        }
    }
}
