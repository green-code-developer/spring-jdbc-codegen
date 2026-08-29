package jp.green_code.spring_jdbc_codegen;

import jp.green_code.spring_jdbc_codegen.db.DbDefinitionReader;
import jp.green_code.spring_jdbc_codegen.db.DbTableDefinition;
import jp.green_code.spring_jdbc_codegen.db.DbTypeMapper;
import jp.green_code.spring_jdbc_codegen.db.JavaType;
import jp.green_code.spring_jdbc_codegen.generator.BaseColumnDefinitionGenerator;
import jp.green_code.spring_jdbc_codegen.generator.BaseEntityGenerator;
import jp.green_code.spring_jdbc_codegen.generator.BaseHelperGenerator;
import jp.green_code.spring_jdbc_codegen.generator.BaseRepositoryGenerator;
import jp.green_code.spring_jdbc_codegen.generator.ColumnDefinitionGenerator;
import jp.green_code.spring_jdbc_codegen.generator.EntityGenerator;
import jp.green_code.spring_jdbc_codegen.generator.Fqcn;
import jp.green_code.spring_jdbc_codegen.generator.HelperGenerator;
import jp.green_code.spring_jdbc_codegen.generator.RepositoryGenerator;
import jp.green_code.spring_jdbc_codegen.generator.TestBaseRepositoryGenerator;
import jp.green_code.spring_jdbc_codegen.generator.TestRepositoryGenerator;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static java.util.stream.Collectors.toSet;
import static jp.green_code.spring_jdbc_codegen.Parameter.param;
import static org.apache.commons.io.FileUtils.deleteDirectory;
import static org.apache.commons.lang3.StringUtils.isBlank;

public class Runner {

    /** param.yml の有効でない設定。実行後に参照する */
    public final List<String> warnings = new ArrayList<>();

    public void run(String paramPath) throws Exception {
        param = readParameter(paramPath);
        var dbDefinitionReader = new DbDefinitionReader();
        appendEnum();
        var tables = dbDefinitionReader.readDefinition();
        warnings.addAll(validateParameter(tables, dbDefinitionReader.excludedTables));
        deleteBaseSources();
        for (var t : tables) {
            writeEntity(t);
        }
        writeHelper();
        writeColumnDefinition();
        for (var t : tables) {
            writeRepository(t);
        }
        for (var t : tables) {
            if (t.isTestTarget()) {
                writeTestRepository(t);
            } else {
                System.out.printf("%s はテスト対象外 param.yml記載なし%n", t.tableName);
            }
        }
        printWarnings();
    }

    /**
     * param.yml の設定が実在するテーブル・カラムを指しているか検証する。
     * 打ち間違いやテーブル定義の変更で設定が無効化されても、通常はエラーにならず
     * 気付けないため、生成の最後にまとめて警告する。
     */
    List<String> validateParameter(List<DbTableDefinition> tables, List<String> excludedTables) {
        var warnings = new ArrayList<String>();
        var tableNames = tables.stream().map(t -> t.tableName).collect(toSet());

        for (var t : param.excludedTableNames) {
            if (!excludedTables.contains(t)) {
                warnings.add("excludedTableNames のテーブル \"%s\" は存在しません".formatted(t));
            }
        }
        for (var t : param.testTargetTable) {
            if (!tableNames.contains(t)) {
                warnings.add("testTargetTable のテーブル \"%s\" は存在しません".formatted(t));
            }
        }
        warnings.addAll(validateColumnSetting("returningColumnsByTable", param.returningColumnsByTable, tables));
        var mappingKeys = new LinkedHashMap<String, Collection<String>>();
        param.columnName2javaPropertyMap.forEach((k, v) -> mappingKeys.put(k, v.keySet()));
        warnings.addAll(validateColumnSetting("columnName2javaPropertyMap", mappingKeys, tables));

        var dbTypeNames = tables.stream().flatMap(t -> t.columns.stream()).map(c -> c.dbTypeName).collect(toSet());
        for (var dbType : param.enumJavaTypeMappings.keySet()) {
            if (!dbTypeNames.contains(dbType)) {
                warnings.add("enumJavaTypeMappings の型 \"%s\" を使っているカラムはありません".formatted(dbType));
            }
        }
        return warnings;
    }

    /** テーブル名とカラム名を指定する形式の設定を検証する */
    List<String> validateColumnSetting(String settingName, Map<String, ? extends Collection<String>> setting, List<DbTableDefinition> tables) {
        var warnings = new ArrayList<String>();
        setting.forEach((tableName, columnNames) -> {
            List<DbTableDefinition> targets;
            if ("*".equals(tableName)) {
                targets = tables;
            } else {
                targets = tables.stream().filter(t -> t.tableName.equals(tableName)).toList();
                if (targets.isEmpty()) {
                    warnings.add("%s のテーブル \"%s\" は存在しません".formatted(settingName, tableName));
                    return;
                }
            }
            var columnsInTargets = targets.stream().flatMap(t -> t.columns.stream()).map(c -> c.columnName).collect(toSet());
            for (var columnName : columnNames) {
                if (!columnsInTargets.contains(columnName)) {
                    var where = "*".equals(tableName) ? "どのテーブルにも" : "テーブル \"%s\" に".formatted(tableName);
                    warnings.add("%s のカラム \"%s\" は%s存在しません".formatted(settingName, columnName, where));
                }
            }
        });
        return warnings;
    }

    void printWarnings() {
        if (warnings.isEmpty()) {
            return;
        }
        System.out.println();
        System.out.println("========================================");
        System.out.printf("param.yml に有効でない設定が %d 件あります%n", warnings.size());
        warnings.forEach(w -> System.out.println("  警告: " + w));
        System.out.println("========================================");
    }

    static Parameter readParameter(String paramPath) throws IOException {
        Yaml yaml = new Yaml();
        Parameter param;
        if (isBlank(paramPath)) {
            // テストコード用
            try (InputStream is = Main.class.getClassLoader().getResourceAsStream("param.yml")) {
                param = yaml.loadAs(is, Parameter.class);
                param.paramYmlDir = Path.of(System.getProperty("user.dir"), "src/main/resources/param.yml").getParent();
            }
        } else {
            Path path = Path.of(paramPath);
            try (InputStream is = new FileInputStream(path.toFile())) {
                param = yaml.loadAs(is, Parameter.class);
                param.paramYmlDir = path.toAbsolutePath().getParent();
            }
        }
        return param;
    }

    void appendEnum() {
        param.enumJavaTypeMappings.forEach((key, value) -> {
            var javaType = new JavaType(value, ":{javaPropertyName}::" + key);
            DbTypeMapper.put(key, javaType);
        });
    }

    void deleteBaseSources() throws IOException {
        var entityBaseDir = Path.of(toMainJavaDir(), param.baseEntityPackage().replace(".", "/"));
        deleteDirectory(entityBaseDir.toFile());
        var repositoryBaseDir = Path.of(toMainJavaDir(), param.baseRepositoryPackage().replace(".", "/"));
        deleteDirectory(repositoryBaseDir.toFile());
        if (!isBlank(param.testJavaDir)) {
            var testRepositoryBaseDir = Path.of(toTestJavaDir(), param.baseRepositoryPackage().replace(".", "/"));
            deleteDirectory(testRepositoryBaseDir.toFile());
        }
    }

    String toMainJavaDir() {
        return Path.of(param.paramYmlDir.toUri().getPath(), param.mainJavaDir).toUri().getPath();
    }

    String toTestJavaDir() {
        return Path.of(param.paramYmlDir.toUri().getPath(), param.testJavaDir).toUri().getPath();
    }

    void writeEntity(DbTableDefinition tableDef) throws IOException {
        var baseGenerator = new BaseEntityGenerator(param);
        var baseCode = baseGenerator.generateBaseEntityCode(tableDef);
        writeJavaCode(toMainJavaDir(), param.baseEntityPackage(), tableDef.toBaseEntityClassName(), baseCode);

        var generator = new EntityGenerator(param);
        var code = generator.generateEntityCode(tableDef);
        writeJavaCodeIfAbsent(toMainJavaDir(), param.entityPackage, tableDef.toEntityClassName(), code);

        if (param.enableNullUnmarkedForEntityPackages) {
            writeJavaCode(toMainJavaDir(), param.baseEntityPackage(), "package-info",
                    toPackageInfoCode(param.baseEntityPackage()));
            writeJavaCodeIfAbsent(toMainJavaDir(), param.entityPackage, "package-info",
                    toPackageInfoCode(param.entityPackage));
        }
    }

    /** Entity のパッケージに @NullUnmarked を付ける package-info を組み立てる */
    static String toPackageInfoCode(String packageName) {
        return """
                %s
                package %s;

                import %s;""".formatted(Fqcn.toAnnotation(Fqcn.NULL_UNMARKED), packageName, Fqcn.NULL_UNMARKED);
    }

    void writeJavaCode(String dir, String packageName, String className, String code) throws IOException {
        var packagePath = packageName.replace(".", "/");
        Path file = Path.of(dir, packagePath, "%s.java".formatted(className));
        Files.createDirectories(file.getParent());
        Files.writeString(file, code, CREATE, TRUNCATE_EXISTING);
    }

    void writeJavaCodeIfAbsent(String dir, String packageName, String className, String code) throws IOException {
        var packagePath = packageName.replace(".", "/");
        Path file = Path.of(dir, packagePath, "%s.java".formatted(className));
        if (!param.forceOverwriteImplementation && Files.exists(file)) {
            // ファイルがあれば何もしない
            return;
        }
        Files.createDirectories(file.getParent());
        Files.writeString(file, code, CREATE, TRUNCATE_EXISTING);
    }

    void writeHelper() throws IOException {
        var generator = new BaseHelperGenerator(param);
        var baseCode = generator.generateBaseHelper();
        writeJavaCode(toMainJavaDir(), param.baseRepositoryPackage(), param.toBaseHelperRepositoryClassName(), baseCode);

        var helperGenerator = new HelperGenerator(param);
        var helperCode = helperGenerator.generateHelper();
        writeJavaCode(toMainJavaDir(), param.repositoryPackage, param.repositoryHelperClassName, helperCode);
    }

    void writeColumnDefinition() throws IOException {
        var baseColumnGenerator = new BaseColumnDefinitionGenerator(param);
        var baseCode = baseColumnGenerator.generateHelper();
        writeJavaCode(toMainJavaDir(), param.baseRepositoryPackage(), param.toBaseColumnDefinitionClassName(), baseCode);

        var columnGenerator = new ColumnDefinitionGenerator(param);
        var columnCode = columnGenerator.generateColumnDefinition();
        writeJavaCode(toMainJavaDir(), param.repositoryPackage, param.columnDefinitionClassName, columnCode);
    }

    void writeRepository(DbTableDefinition table) throws IOException {
        var baseGenerator = new BaseRepositoryGenerator(param, table);
        var baseCode = baseGenerator.generateBaseRepositoryCode();
        writeJavaCode(toMainJavaDir(), param.baseRepositoryPackage(), table.toBaseRepositoryClassName(), baseCode);

        var generator = new RepositoryGenerator(param, table);
        var normalCode = generator.generateRepositoryCode();
        writeJavaCodeIfAbsent(toMainJavaDir(), param.repositoryPackage, table.toRepositoryClassName(), normalCode);
    }

    void writeTestRepository(DbTableDefinition table) throws IOException {
        var testBaseGenerator = new TestBaseRepositoryGenerator(param, table);
        var testBaseCode = testBaseGenerator.generateBaseTestCode();
        writeJavaCode(toTestJavaDir(), param.baseRepositoryPackage(), table.toTestBaseRepositoryClassName(), testBaseCode);

        var testGenerator = new TestRepositoryGenerator(param, table);
        var testCode = testGenerator.generateTestRepositoryCode();
        writeJavaCodeIfAbsent(toTestJavaDir(), param.repositoryPackage, table.toTestRepositoryClassName(), testCode);
    }
}
