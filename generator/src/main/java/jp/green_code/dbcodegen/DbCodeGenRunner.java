package jp.green_code.dbcodegen;

import jp.green_code.dbcodegen.db.DbDefinitionReader;
import jp.green_code.dbcodegen.db.DbTypeMapper;
import jp.green_code.dbcodegen.db.JavaType;
import jp.green_code.dbcodegen.db.TableDefinition;
import jp.green_code.dbcodegen.generator.BaseEntityGenerator;
import jp.green_code.dbcodegen.generator.BaseRepositoryGenerator;
import jp.green_code.dbcodegen.generator.EntityGenerator;
import jp.green_code.dbcodegen.generator.HelperGenerator;
import jp.green_code.dbcodegen.generator.RepositoryGenerator;
import jp.green_code.dbcodegen.generator.TestBaseRepositoryGenerator;
import jp.green_code.dbcodegen.generator.TestRepositoryGenerator;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static jp.green_code.dbcodegen.DbCodeGenParameter.param;
import static jp.green_code.dbcodegen.DbCodeGenUtil.dumpTableDefinitions;
import static org.apache.commons.io.FileUtils.deleteDirectory;
import static org.apache.commons.lang3.StringUtils.isBlank;

public class DbCodeGenRunner {

    public void run(String paramPath) throws Exception {
        param = readParameter(paramPath);
        var dbDefinitionReader = new DbDefinitionReader();
        appendEnum();
        var tables = dbDefinitionReader.readDefinition();
        dumpTableDefinitions(tables);
        deleteBaseSources();
        for (var t : tables) {
            writeEntity(t);
        }
        writeHelper();
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
    }

    static DbCodeGenParameter readParameter(String paramPath) throws IOException {
        Yaml yaml = new Yaml();
        DbCodeGenParameter param;
        if (isBlank(paramPath)) {
            // テストコード用
            try (InputStream is = DbCodeGenMain.class.getClassLoader().getResourceAsStream("param.yml")) {
                param = yaml.loadAs(is, DbCodeGenParameter.class);
                param.paramYmlDir = Path.of(System.getProperty("user.dir"), "src/main/resources/param.yml").getParent();
            }
        } else {
            Path path = Path.of(paramPath);
            try (InputStream is = new FileInputStream(path.toFile())) {
                param = yaml.loadAs(is, DbCodeGenParameter.class);
                param.paramYmlDir = path.toAbsolutePath().getParent();
                System.out.println(param.paramYmlDir.toUri().getPath());
            }
        }
        return param;
    }

    void appendEnum() {
        param.enumJavaTypeMappings.forEach((key, value) -> {
            var javaType = new JavaType(value, ":{javaFieldName}::" + key);
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

    void writeEntity(TableDefinition tableDef) throws IOException {
        var baseGenerator = new BaseEntityGenerator(param);
        var baseCode = baseGenerator.generateBaseEntityCode(tableDef);
        writeJavaCode(toMainJavaDir(), param.baseEntityPackage(), tableDef.toBaseEntityClassName(), baseCode);

        var generator = new EntityGenerator(param);
        var code = generator.generateEntityCode(tableDef);
        writeJavaCodeIfAbsent(toMainJavaDir(), param.entityPackage, tableDef.toEntityClassName(), code);
    }

    void writeJavaCode(String dir, String packageName, String className, String code) throws IOException {
        var packagePath = packageName.replace(".", "/");
        Path file = Path.of(dir, packagePath, "%s.java".formatted(className));
        System.out.printf("java code: %s%n", file.toAbsolutePath());
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
        var generator = new HelperGenerator(param);
        var code = generator.generateHelper();
        writeJavaCode(toMainJavaDir(), param.baseRepositoryPackage(), param.repositoryHelperClassName, code);
    }

    void writeRepository(TableDefinition table) throws IOException {
        var baseGenerator = new BaseRepositoryGenerator(param, table);
        var baseCode = baseGenerator.generateBaseRepositoryCode();
        writeJavaCode(toMainJavaDir(), param.baseRepositoryPackage(), table.toBaseRepositoryClassName(), baseCode);

        var generator = new RepositoryGenerator(param, table);
        var normalCode = generator.generateRepositoryCode();
        writeJavaCodeIfAbsent(toMainJavaDir(), param.repositoryPackage, table.toRepositoryClassName(), normalCode);
    }

    void writeTestRepository(TableDefinition table) throws IOException {
        var testBaseGenerator = new TestBaseRepositoryGenerator(param, table);
        var testBaseCode = testBaseGenerator.generateBaseTestCode();
        writeJavaCode(toTestJavaDir(), param.baseRepositoryPackage(), table.toTestBaseRepositoryClassName(), testBaseCode);

        var testGenerator = new TestRepositoryGenerator(param, table);
        var testCode = testGenerator.generateTestRepositoryCode();
        writeJavaCodeIfAbsent(toTestJavaDir(), param.repositoryPackage, table.toTestRepositoryClassName(), testCode);
    }
}
