package jp.green_code.spring_jdbc_codegen.db;

import jp.green_code.spring_jdbc_codegen.Parameter;
import org.apache.commons.lang3.Strings;

import java.util.ArrayList;
import java.util.List;

import static java.util.Comparator.comparing;
import static jp.green_code.spring_jdbc_codegen.Util.toCamelCase;

public class TableDefinition {
    final Parameter param;

    public TableDefinition(Parameter param) {
        this.param = param;
    }

    public String tableName;
    public List<ColumnDefinition> columns = new ArrayList<>();

    public String toJavaTableName() {
        return toCamelCase(tableName, true);
    }

    public String toBaseEntityClassName() {
        return toCamelCase(param.basePackageName, true) + param.entityClassNamePrefix + toJavaTableName() + param.entityClassNameSuffix;
    }

    public String toEntityClassName() {
        return param.entityClassNamePrefix + toJavaTableName() + param.entityClassNameSuffix;
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
    public List<ColumnDefinition> pkColumns() {
        return columns.stream().filter(ColumnDefinition::isPrimaryKey).sorted(comparing(c -> c.primaryKeySeq)).toList();
    }

    // テスト対象外テーブル判定
    public boolean isTestTarget() {
        return param.testTargetTable.contains(tableName);
    }

    public boolean hasUpdateColumns() {
        return !columns.stream().allMatch(ColumnDefinition::shouldSkipInUpdate);
    }

    // テストデータ作成にpickBySeed を使っているカラムがあるか判定（enum がこれに該当する）
    public boolean hasPickBySeed() {
        return columns.stream().anyMatch(c ->
                Strings.CS.contains(c.toJavaType().generateDateSnippet(), "pickBySeed"));
    }

    // Insert 時にreturning が必要かどうか
    public boolean needReturningInInsert() {
        return columns.stream().anyMatch(c -> c.isSetNowColumn() || c.isInsertOmittable());
    }

    // Update 時にreturning が必要かどうか
    public boolean needReturningInUpdate() {
        return columns.stream().anyMatch(ColumnDefinition::isSetNowColumn);
    }
}
