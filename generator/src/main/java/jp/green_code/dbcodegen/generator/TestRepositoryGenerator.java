package jp.green_code.dbcodegen.generator;

import jp.green_code.dbcodegen.DbCodeGenParameter;
import jp.green_code.dbcodegen.db.TableDefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class TestRepositoryGenerator {
    final DbCodeGenParameter param;
    final TableDefinition table;

    public TestRepositoryGenerator(DbCodeGenParameter param, TableDefinition table) {
        this.param = param;
        this.table = table;
    }

    public String generateTestRepositoryCode() {
        var sb = new ArrayList<String>();
        sb.add("package %s;".formatted(param.repositoryPackage));
        sb.add("");
        sb.addAll(imports());
        sb.add("");
        sb.add("@SpringBootTest");
        sb.add("public class %s extends %s {".formatted(table.toTestRepositoryClassName(), table.toTestBaseRepositoryClassName()));
        sb.add("");
        sb.add("    @Autowired");
        sb.add("    %s repository;".formatted(table.toRepositoryClassName()));
        sb.add("");
        sb.add("    @Test");
        sb.add("    void test() {");
        sb.add("        super.test(repository);");
        sb.add("    }");
        sb.add("}");
        return String.join("\n", sb);
    }

    List<String> imports() {
        var packages = new TreeSet<String>();
        packages.add(param.baseRepositoryPackage() + "." + table.toTestBaseRepositoryClassName());
        packages.add("org.junit.jupiter.api.Test");
        packages.add("org.springframework.beans.factory.annotation.Autowired");
        packages.add("org.springframework.boot.test.context.SpringBootTest");
        return packages.stream().map("import %s;"::formatted).toList();
    }
}
