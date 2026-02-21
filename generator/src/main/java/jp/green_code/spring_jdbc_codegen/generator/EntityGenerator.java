package jp.green_code.spring_jdbc_codegen.generator;

import jp.green_code.spring_jdbc_codegen.Parameter;
import jp.green_code.spring_jdbc_codegen.db.TableDefinition;

import java.util.ArrayList;

public class EntityGenerator {
    final Parameter param;

    public EntityGenerator(Parameter param) {
        this.param = param;
    }

    public String generateEntityCode(TableDefinition table) {
        var sb = new ArrayList<String>();

        // package
        sb.add("package %s;".formatted(param.entityPackage));
        sb.add("");

        // import
        sb.add("import %s.%s;".formatted(param.baseEntityPackage(), table.toBaseEntityClassName()));
        sb.add("");

        // class
        sb.add("/**");
        sb.add(" * Table: %s".formatted(table.tableName));
        sb.add(" */");
        sb.add("public class %s extends %s {".formatted(table.toEntityClassName(), table.toBaseEntityClassName()));
        sb.add("}");
        return String.join("\n", sb);
    }
}
