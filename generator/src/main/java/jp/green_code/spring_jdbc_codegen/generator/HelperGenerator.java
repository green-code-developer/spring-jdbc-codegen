package jp.green_code.spring_jdbc_codegen.generator;

import jp.green_code.spring_jdbc_codegen.Parameter;

import java.util.ArrayList;

public class HelperGenerator {
    final Parameter param;

    public HelperGenerator(Parameter param) {
        this.param = param;
    }

    public String generateHelper() {
        var sb = new ArrayList<String>();

        sb.add("package %s;".formatted(param.repositoryPackage));
        sb.add("");

        sb.add("import org.springframework.stereotype.Component;");
        sb.add("import javax.sql.DataSource;");
        sb.add("import %s.%s;".formatted(param.baseRepositoryPackage(), param.toBaseHelperRepositoryClassName()));
        sb.add("");

        sb.add("@Component");
        sb.add("public class %s extends %s {".formatted(param.repositoryHelperClassName, param.toBaseHelperRepositoryClassName()));
        sb.add("    public %s(DataSource dataSource) {".formatted(param.repositoryHelperClassName));
        sb.add("        super(dataSource);");
        sb.add("    }");
        sb.add("}");
        return String.join("\n", sb);
    }
}
