package jp.green_code.dbcodegen.generator;

import jp.green_code.dbcodegen.DbCodeGenParameter;
import jp.green_code.dbcodegen.db.TableDefinition;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.joining;
import static org.apache.commons.lang3.StringUtils.isBlank;

public class TestBaseRepositoryGenerator {
    final DbCodeGenParameter param;
    final TableDefinition table;

    public TestBaseRepositoryGenerator(DbCodeGenParameter param, TableDefinition table) {
        this.param = param;
        this.table = table;
    }

    public String generateBaseTestCode() {
        var sb = new ArrayList<String>();
        sb.add("package %s;".formatted(param.baseRepositoryPackage()));
        sb.add("");
        sb.addAll(imports());
        sb.add("");
        sb.add("public abstract class %s {".formatted(table.toTestBaseRepositoryClassName()));
        sb.add("");
        sb.addAll(test());
        sb.add("");
        sb.addAll(generateTestData4Xxx());
        sb.add("");
        sb.addAll(assertEntity4Xxx());
        sb.add("}");
        return String.join("\n", sb);
    }

    List<String> imports() {
        var sb = new ArrayList<String>();

        var packages = new ArrayList<String>();
        packages.add(param.entityPackage + "." + table.toEntityClassName());
        table.columns.stream().filter(c -> !isBlank(c.importName())).forEach(c -> packages.add(c.importName()));
        packages.stream().distinct().sorted().map("import %s;"::formatted).forEach(sb::add);

        var statics = new ArrayList<String>();
        statics.add("org.junit.jupiter.api.Assertions.assertTrue");
        statics.add("org.junit.jupiter.api.Assertions.assertEquals");
        if (table.hasPickBySeed()) {
            statics.add("%s.%s.pickBySeed".formatted(param.baseRepositoryPackage(), param.repositoryHelperClassName));
        }
        statics.stream().distinct().sorted().map("import static %s;"::formatted).forEach(sb::add);

        return sb;
    }

    List<String> test() {
        var sb = new ArrayList<String>();
        sb.add("protected void test(%s repository) {".formatted(table.toBaseRepositoryClassName()));
        sb.add("    var seed = (int) (short) System.currentTimeMillis();");
        sb.add("    var data = generateTestData(seed);");
        sb.add("");
        sb.add("    // insert");
        table.pkColumns().forEach(c -> {
            if (c.isInsertOmittable()) {
                sb.add("    data.%s(null);".formatted(c.toSetter()));
            }
        });
        sb.add("    repository.insert(data);");
        sb.add("");
        if (table.pkColumns().isEmpty()) {
            sb.add("    // PK がないのでselect, update, delete のテストは行わない");
        } else if (!table.hasUpdateColumns()) {
            sb.add("    // Update 対象カラムがないのでselect, update, delete のテストは行わない");
        } else {
            sb.add("    // select 1回目");
            var pks = table.pkColumns().stream().map(c -> "data.%s()".formatted(c.toGetter())).collect(joining(", "));
            sb.add("    var res = repository.findByPk(%s);".formatted(pks));
            sb.add("    assertTrue(res.isPresent());");
            sb.add("");
            sb.add("    // insert 後の確認");
            sb.add("    var stored = res.orElseThrow();");
            for (var col : table.columns) {
                sb.add("    assert4%s(data.%s(), stored.%s());".formatted(col.toJavaFieldName(), col.toGetter(), col.toGetter()));
            }
            sb.add("");
            sb.add("    // update");
            sb.add("    seed++;");
            sb.add("    var data2 = generateTestData(seed);");
            for (var c : table.pkColumns()) {
                sb.add("    data2.%s(data.%s());".formatted(c.toSetter(), c.toGetter()));
            }
            sb.add("    repository.update(data2);");
            sb.add("");
            sb.add("    // select 2回目");
            var pks2 = table.pkColumns().stream().map(c -> "data2.%s()".formatted(c.toGetter())).collect(joining(", "));
            sb.add("    var res2 = repository.findByPk(%s);".formatted(pks2));
            sb.add("    assertTrue(res2.isPresent());");
            sb.add("");
            sb.add("    // update 後の確認");
            sb.add("    var stored2 = res2.orElseThrow();");
            for (var col : table.columns) {
                sb.add("");
                if (col.shouldSkipInUpdate()) {
                    sb.add("    // %s はupdate 対象外のため変更前と変わらないことを確認".formatted(col.columnName));
                    sb.add("    assert4%s(stored.%s(), stored2.%s());".formatted(col.toJavaFieldName(), col.toGetter(), col.toGetter()));
                } else {
                    sb.add("    assert4%s(data2.%s(), stored2.%s());".formatted(col.toJavaFieldName(), col.toGetter(), col.toGetter()));
                }
            }
            sb.add("");
            sb.add("    // delete");
            sb.add("    var deleteCount = repository.deleteByPk(%s);".formatted(pks2));
            sb.add("    assertEquals(1, deleteCount);");
            sb.add("    // select 3回目");
            sb.add("    var stored3 = repository.findByPk(%s);".formatted(pks2));
            sb.add("    assertTrue(stored3.isEmpty());");
        }
        sb.add("}");
        sb.add("");
        return sb.stream().map(s -> isBlank(s) ? s : "    " + s).toList();
    }

    List<String> generateTestData4Xxx() {
        var sb = new ArrayList<String>();
        sb.add("public %s generateTestData(int seed) {".formatted(table.toEntityClassName()));
        sb.add("    var entity = new %s();".formatted(table.toEntityClassName()));
        for (var col : table.columns) {
            var plusplus = table.columns.getLast() == col ? "" : "++";
            sb.add("    entity.%s(generateTestData4%s(seed%s));".formatted(col.toSetter(), col.toJavaFieldName(), plusplus));
        }
        sb.add("    return entity;");
        sb.add("}");
        for (var col : table.columns) {
            sb.add("");
            if (isBlank(col.toJavaType().generateDateSnippet())) {
                System.out.printf("致命的 table:%s column:%s 対応できない型です(javaTestSnippet not found)%n", table.tableName, col.columnName);
            } else {
                sb.add("protected %s generateTestData4%s(int seed) {".formatted(col.javaSimpleTypeName(), col.toJavaFieldName()));
                sb.add("    %s".formatted(col.toJavaType().generateDateSnippet()));
                sb.add("}");
            }
        }
        return sb.stream().map(s -> isBlank(s) ? s : "    " + s).toList();
    }

    List<String> assertEntity4Xxx() {
        var sb = new ArrayList<String>();
        for (var col : table.columns) {
            sb.add("");
            sb.add("protected void assert4%s(%s expected, %s value) {".formatted(col.toJavaFieldName(), col.javaSimpleTypeName(), col.javaSimpleTypeName()));
            if (isBlank(col.toJavaType().assertSnippet())) {
                sb.add("    assertEquals(expected, value);");
            } else {
                sb.add("    %s".formatted(col.toJavaType().assertSnippet()));
            }
            sb.add("}");
        }
        return sb.stream().map(s -> isBlank(s) ? s : "    " + s).toList();
    }
}
