package jp.green_code.spring_jdbc_codegen.generator;

import jp.green_code.spring_jdbc_codegen.Param;
import jp.green_code.spring_jdbc_codegen.db.DbTableDefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class TestRepositoryGenerator {
    final Param param;
    final DbTableDefinition table;

    public TestRepositoryGenerator(Param param, DbTableDefinition table) {
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
        packages.add(Fqcn.TEST);
        packages.add(Fqcn.AUTOWIRED);
        packages.add(Fqcn.SPRING_BOOT_TEST);
        return packages.stream().map("import %s;"::formatted).toList();
    }
}
