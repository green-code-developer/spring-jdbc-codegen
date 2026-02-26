package jp.green_code.spring_jdbc_codegen.generator;

import jp.green_code.spring_jdbc_codegen.Parameter;
import jp.green_code.spring_jdbc_codegen.db.DbTableDefinition;

import java.util.ArrayList;

public class RepositoryGenerator {
    final Parameter param;
    final DbTableDefinition table;

    public RepositoryGenerator(Parameter param, DbTableDefinition table) {
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
