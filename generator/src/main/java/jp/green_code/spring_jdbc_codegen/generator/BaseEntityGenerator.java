package jp.green_code.spring_jdbc_codegen.generator;

import jp.green_code.spring_jdbc_codegen.Parameter;
import jp.green_code.spring_jdbc_codegen.db.DbColumnDefinition;
import jp.green_code.spring_jdbc_codegen.db.DbTableDefinition;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class BaseEntityGenerator {
    final Parameter param;

    public BaseEntityGenerator(Parameter param) {
        this.param = param;
    }

    public String generateBaseEntityCode(DbTableDefinition table) {
        var sb = new ArrayList<String>();

        // package
        sb.add("package %s;".formatted(param.baseEntityPackage()));
        sb.add("");

        // import
        sb.addAll(imports(table.columns));
        sb.add("");

        // class
        sb.add("/**");
        sb.add(" * Table: %s".formatted(table.tableName));
        sb.add(" */");
        sb.add("public abstract class %s {".formatted(table.toBaseEntityClassName()));

        // fields
        for (var col : table.columns) {
            sb.add("");
            sb.add("    /** %s */".formatted(col.columnName));
            sb.add("    protected %s %s;".formatted(col.javaSimpleTypeName(), col.toJavaPropertyName()));
        }

        // getter & setter
        for (var col : table.columns) {
            sb.add("");
            var getter = generateGetterLines(col);
            sb.addAll(getter);
            sb.add("");
            var setter = generateSetterLines(col);
            sb.addAll(setter);
        }

        sb.add("}");
        return String.join("\n", sb);
    }

    public static List<String> generateGetterLines(DbColumnDefinition col) {
        var sb = new ArrayList<String>();
        sb.add("public %s %s() {".formatted(col.javaSimpleTypeName(), col.toGetter()));
        sb.add("    return %s;".formatted(col.toJavaPropertyName()));
        sb.add("}");
        return sb.stream().map(s -> isBlank(s) ? s : "    " + s).toList();
    }

    public static List<String> generateSetterLines(DbColumnDefinition col) {
        var sb = new ArrayList<String>();
        sb.add("public void %s(%s %s) {".formatted(col.toSetter(), col.javaSimpleTypeName(), col.toJavaPropertyName()));
        sb.add("    this.%s = %s;".formatted(col.toJavaPropertyName(), col.toJavaPropertyName()));
        sb.add("}");
        return sb.stream().map(s -> isBlank(s) ? s : "    " + s).toList();
    }

    List<String> imports(List<DbColumnDefinition> columnDefs) {
        return columnDefs.stream().map(DbColumnDefinition::importName).filter(c -> !StringUtils.isBlank(c)).distinct().sorted().map("import %s;"::formatted).toList();
    }
}
