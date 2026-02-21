package jp.green_code.spring_jdbc_codegen;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static jp.green_code.spring_jdbc_codegen.Util.toCamelCase;

public class Parameter {
    public static Parameter param;
    /** param.yml のパス */
    public Path paramYmlDir;
    /** JDBC url */
    public String jdbcUrl;
    /** JDBC user */
    public String jdbcUser;
    /** JDBC password */
    public String jdbcPass;
    /** JDBC schema */
    public String jdbcSchema;
    /** Entity のパッケージ名 jp.green_code.demo.entity */
    public String entityPackage;
    /** Repository のパッケージ名 jp.green_code.demo.repository */
    public String repositoryPackage;
    /** 除外テーブル名 spring_session, spring_session_attribute */
    public List<String> excludedTableNames = List.of();
    /** UPDATE 対象外カラム */
    public Map<String, List<String>> excludeUpdateColumnsByTable = Map.of();
    /** set now() カラム */
    public Map<String, List<String>> setNowColumnsByTable = Map.of();
    /** テスト対象テーブル */
    public List<String> testTargetTable = List.of();
    /** Enum 型 */
    public Map<String, String> enumJavaTypeMappings = Map.of();
    /** 本体プロジェクトの src/main/java */
    public String mainJavaDir;
    /** 本体プロジェクトの test/main/java */
    public String testJavaDir;

    // 以下高度なカスタマイズ用設定
    /** 規定クラスのパッケージ名 base */
    public String basePackageName = "base";
    /** Entity クラスの接頭辞 */
    public String entityClassNamePrefix = "";
    /** Entity クラスの接尾辞 */
    public String entityClassNameSuffix = "Entity";
    /** Repository クラスの接頭辞 */
    public String repositoryClassNamePrefix = "";
    /** Repository クラスの接尾辞 */
    public String repositoryClassNameSuffix = "Repository";
    /** TestRepository クラスの接頭辞 */
    public String testRepositoryClassNamePrefix = "Test";
    /** TestRepository クラスの接尾辞 TestRepository */
    public String testRepositoryClassNameSuffix = "";
    /** Repository ヘルパークラス名 */
    public String repositoryHelperClassName = "RepositoryHelper";
    /** カラム定義クラス名 */
    public String columnDefinitionClassName = "ColumnDefinition";
    /** 実装クラスが存在していても再作成する */
    public boolean forceOverwriteImplementation = false;

    public String baseEntityPackage() {
        return entityPackage + "." + basePackageName;
    }

    public String baseRepositoryPackage() {
        return repositoryPackage + "." + basePackageName;
    }

    public String toBaseColumnDefinitionClassName() {
        return toCamelCase(param.basePackageName, true) + param.columnDefinitionClassName;
    }

    public String toBaseHelperRepositoryClassName() {
        return toCamelCase(param.basePackageName, true) + param.repositoryHelperClassName;
    }
}
