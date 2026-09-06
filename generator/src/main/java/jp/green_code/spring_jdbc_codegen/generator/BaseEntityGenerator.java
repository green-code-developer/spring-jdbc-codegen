package jp.green_code.spring_jdbc_codegen.generator;

import jp.green_code.spring_jdbc_codegen.Param;
import jp.green_code.spring_jdbc_codegen.db.DbColumnDefinition;
import jp.green_code.spring_jdbc_codegen.db.DbTableDefinition;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class BaseEntityGenerator {
    final Param param;

    public BaseEntityGenerator(Param param) {
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
            // 非null のフィールドはDB の既定値で初期化する（ENTITY-012）
            var initializer = col.isNonNullField() ? " = " + col.toDefaultValueExpression() : "";
            sb.add("    protected %s %s%s;".formatted(toDeclaredType(col), col.toJavaPropertyName(), initializer));
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
        sb.add("public %s %s() {".formatted(toDeclaredType(col), col.toGetter()));
        sb.add("    return %s;".formatted(col.toJavaPropertyName()));
        sb.add("}");
        return sb.stream().map(s -> isBlank(s) ? s : "    " + s).toList();
    }

    public static List<String> generateSetterLines(DbColumnDefinition col) {
        var sb = new ArrayList<String>();
        sb.add("public void %s(%s %s) {".formatted(col.toSetter(), toDeclaredType(col), col.toJavaPropertyName()));
        sb.add("    this.%s = %s;".formatted(col.toJavaPropertyName(), col.toJavaPropertyName()));
        sb.add("}");
        return sb.stream().map(s -> isBlank(s) ? s : "    " + s).toList();
    }

    /**
     * 宣言に使う型。@Nullable は型注釈なので、配列では要素型と [] の間に置く。
     * "@Nullable byte[]" と書くと要素のbyte に掛かり、配列自体はnull 不可のままになる
     */
    static String toDeclaredType(DbColumnDefinition col) {
        var type = col.toFieldTypeName();
        if (col.isNonNullField()) {
            return type;
        }
        var nullable = Fqcn.toAnnotation(Fqcn.NULLABLE);
        if (type.endsWith("[]")) {
            return "%s %s []".formatted(type.substring(0, type.length() - 2), nullable);
        }
        return "%s %s".formatted(nullable, type);
    }

    List<String> imports(List<DbColumnDefinition> columnDefs) {
        var imports = new TreeSet<String>();
        columnDefs.stream().map(DbColumnDefinition::importName).filter(c -> !StringUtils.isBlank(c)).forEach(imports::add);
        if (columnDefs.stream().anyMatch(c -> !c.isNonNullField())) {
            imports.add(Fqcn.NULLABLE);
        }
        return imports.stream().map("import %s;"::formatted).toList();
    }
}
