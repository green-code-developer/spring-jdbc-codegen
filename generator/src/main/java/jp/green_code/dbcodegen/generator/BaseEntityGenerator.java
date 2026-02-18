package jp.green_code.dbcodegen.generator;

import jp.green_code.dbcodegen.DbCodeGenParameter;
import jp.green_code.dbcodegen.db.ColumnDefinition;
import jp.green_code.dbcodegen.db.TableDefinition;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static jp.green_code.dbcodegen.DbCodeGenUtil.toCamelCase;

public class BaseEntityGenerator {
    final DbCodeGenParameter param;

    public BaseEntityGenerator(DbCodeGenParameter param) {
        this.param = param;
    }

    public String generateBaseEntityCode(TableDefinition table) {
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
        for (ColumnDefinition col : table.columns) {
            sb.add("");
            sb.add("    /** %s */".formatted(col.columnName));
            sb.add("    protected %s %s;".formatted(col.javaSimpleTypeName(), col.toJavaFieldName()));
        }

        // getter & setter
        for (ColumnDefinition col : table.columns) {
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

    public static List<String> generateGetterLines(ColumnDefinition col) {
        var fieldName = toCamelCase(col.columnName, false);
        var sb = new ArrayList<String>();
        sb.add("    public %s %s() {".formatted(col.javaSimpleTypeName(), col.toGetter()));
        sb.add("        return %s;".formatted(fieldName));
        sb.add("    }");
        return sb;
    }

    public static List<String> generateSetterLines(ColumnDefinition col) {
        var fieldName = toCamelCase(col.columnName, false);
        var sb = new ArrayList<String>();
        sb.add("    public void %s(%s %s) {".formatted(col.toSetter(), col.javaSimpleTypeName(), fieldName));
        sb.add("        this.%s = %s;".formatted(fieldName, fieldName));
        sb.add("    }");
        return sb;
    }

    List<String> imports(List<ColumnDefinition> columnDefs) {
        return columnDefs.stream().map(ColumnDefinition::importName).filter(c -> !StringUtils.isBlank(c)).distinct().sorted().map("import %s;"::formatted).toList();
    }
}
