package jp.green_code.dbcodegen.generator;

import jp.green_code.dbcodegen.DbCodeGenParameter;
import jp.green_code.dbcodegen.db.TableDefinition;

import java.util.ArrayList;

public class RepositoryGenerator {
    final DbCodeGenParameter param;
    final TableDefinition table;

    public RepositoryGenerator(DbCodeGenParameter param, TableDefinition table) {
        this.param = param;
        this.table = table;
    }

    public String generateRepositoryCode() {
        var sb = new ArrayList<String>();

        // package
        sb.add("package %s;".formatted(param.repositoryPackage));
        sb.add("");

        // import
        sb.add("import org.springframework.stereotype.Repository;");
        sb.add("import %s.%s;".formatted(param.baseRepositoryPackage(), param.repositoryHelperClassName));
        sb.add("import %s.%s;".formatted(param.baseRepositoryPackage(), table.toBaseRepositoryClassName()));
        sb.add("");

        // class
        sb.add("/**");
        sb.add(" * Table: %s".formatted(table.tableName));
        sb.add(" */");
        sb.add("@Repository");
        sb.add("public class %s extends %s {".formatted(table.toRepositoryClassName(), table.toBaseRepositoryClassName()));
        sb.add("    public %s(%s helper) {".formatted(table.toRepositoryClassName(), param.repositoryHelperClassName));
        sb.add("        super(helper);");
        sb.add("    }");
        sb.add("}");
        return String.join("\n", sb);
    }
}
