package jp.green_code.spring_jdbc_codegen.generator;

import jp.green_code.spring_jdbc_codegen.Parameter;

import java.util.ArrayList;

public class NullMarkedGenerator {
    final Parameter param;

    public NullMarkedGenerator(Parameter param) {
        this.param = param;
    }

    public String generateNullMarkedPackageInfoCode(String packageName) {
        var sb = new ArrayList<String>();

        sb.add(param.toNullUnmarked());
        sb.add("package %s;".formatted(packageName));
        sb.add("");
        sb.add("import %s;".formatted(param.nullUnmarkedFqcn));

        return String.join("\n", sb);
    }
}
