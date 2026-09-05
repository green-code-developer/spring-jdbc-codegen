package jp.green_code.spring_jdbc_codegen.db;

import jp.green_code.spring_jdbc_codegen.Param;
import org.apache.commons.lang3.Strings;

import java.util.ArrayList;
import java.util.List;

import static java.util.Comparator.comparing;
import static jp.green_code.spring_jdbc_codegen.Util.toCamelCase;

public class DbTableDefinition {
    final Param param;
    public final String tableName;

    public DbTableDefinition(Param param, String tableName) {
        this.param = param;
        this.tableName = tableName;
    }

    public List<DbColumnDefinition> columns = new ArrayList<>();

    public String toJavaTableName() {
        return toCamelCase(tableName, true);
    }

    public String toBaseEntityClassName() {
        return toCamelCase(param.basePackageName, true) + param.entityClassNamePrefix + toJavaTableName() + param.entityClassNameSuffix;
    }

    public String toEntityClassName() {
        return param.entityClassNamePrefix + toJavaTableName() + param.entityClassNameSuffix;
    }

    public String toMapperClassName() {
        return param.mapperClassNamePrefix + toJavaTableName() + param.mapperClassNameSuffix;
    }

    public String toBaseRepositoryClassName() {
        return toCamelCase(param.basePackageName, true) + param.repositoryClassNamePrefix + toJavaTableName() + param.repositoryClassNameSuffix;
    }

    public String toRepositoryClassName() {
        return param.repositoryClassNamePrefix + toJavaTableName() + param.repositoryClassNameSuffix;
    }

    public String toTestBaseRepositoryClassName() {
        return param.testRepositoryClassNamePrefix + toBaseRepositoryClassName() + param.testRepositoryClassNameSuffix;
    }

    public String toTestRepositoryClassName() {
        return param.testRepositoryClassNamePrefix + toRepositoryClassName() + param.testRepositoryClassNameSuffix;
    }

    // PK のカラム全てを返す
    public List<DbColumnDefinition> pkColumns() {
        return columns.stream().filter(DbColumnDefinition::isPrimaryKey).sorted(comparing(c -> c.primaryKeySeq)).toList();
    }

    // テスト対象外テーブル判定
    public boolean isTestTarget() {
        return param.testTargetTable.contains(tableName);
    }

    // テストデータ作成にpickBySeed を使っているカラムがあるか判定（enum がこれに該当する）
    public boolean hasPickBySeed() {
        return columns.stream().anyMatch(c ->
                Strings.CS.contains(c.toJavaType().generateDateSnippet(), "pickBySeed"));
    }

    // PK 以外のカラム。update 系のset 句の対象
    public List<DbColumnDefinition> nonPkColumns() {
        return columns.stream().filter(c -> !c.isPrimaryKey()).toList();
    }

    // insertExceptPk を生成できるか。PK を除外しても DB が値を埋められる場合のみ
    public boolean canInsertExceptPk() {
        return !pkColumns().isEmpty() && pkColumns().stream().allMatch(DbColumnDefinition::isDbDeterminable);
    }

    // Update 時にreturning が必要かどうか
    public boolean needReturningInUpdate() {
        // UPDATE 対象外のカラムは set 句に含まれず値が変わらないため returning しない
        return columns.stream().anyMatch(DbColumnDefinition::isReturningColumn);
    }

    public boolean needCustomMapper() {
        return columns.stream().anyMatch(DbColumnDefinition::hasNameMapping);
    }

    public String toMapperOrEntityClass() {
        return needCustomMapper() ? "ROW_MAPPER" : toEntityClassName() + ".class";
    }
}
