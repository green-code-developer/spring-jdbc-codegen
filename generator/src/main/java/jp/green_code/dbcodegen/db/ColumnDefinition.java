package jp.green_code.dbcodegen.db;

import java.util.List;
import java.util.Map;

import static jp.green_code.dbcodegen.DbCodeGenParameter.param;
import static jp.green_code.dbcodegen.DbCodeGenUtil.toCamelCase;
import static org.apache.commons.lang3.StringUtils.isBlank;

public class ColumnDefinition {
    public String tableName;
    public String columnName;
    public String dbTypeName;
    public Integer jdbcType;
    public Integer columnSize;
    public boolean nullable;
    public String primaryKeyName;
    public Short primaryKeySeq;
    public String defaultExpression;

    public String toLogString() {
        return columnName + " [" + dbTypeName + "] " + (nullable ? "nullable" : "nonnull") + " " + (isPrimaryKey() ? "pk(" + primaryKeySeq + " " + primaryKeyName + ")" : "") + " default[" + defaultExpression + "]";
    }

    public String toJavaFieldName() {
        return toCamelCase(columnName, false);
    }

    public boolean hasDefault() {
        return !isBlank(defaultExpression);
    }

    public boolean isPrimaryKey() {
        return !isBlank(primaryKeyName);
    }

    public boolean isInsertOmittable() {
        return !nullable && hasDefault();
    }

    public String toGetter() {
        String methodName = toCamelCase(columnName, true);
        return "get%s".formatted(methodName);
    }

    public String toSetter() {
        String methodName = toCamelCase(columnName, true);
        return "set%s".formatted(methodName);
    }

    public JavaType toJavaType() {
        return DbTypeMapper.map(dbTypeName);
    }

    public String javaSimpleTypeName() {
        var javaFqcn = toJavaType().fqcn();
        int idx = javaFqcn.lastIndexOf('.');
        return (idx >= 0) ? javaFqcn.substring(idx + 1) : javaFqcn;
    }

    public String importName() {
        // fqcn と同じだがプリミティブ型の場合はnull を返す
        var javaFqcn = toJavaType().fqcn();
        return javaFqcn.contains(".") ? javaFqcn : null;
    }

    // UPDATE 対象外判定
    public boolean shouldSkipInUpdate() {
        return mapContainsColumn(param.excludeUpdateColumnsByTable, tableName, columnName);
    }

    // set now() 対象判定
    public boolean isSetNowColumn() {
        return mapContainsColumn(param.setNowColumnsByTable, tableName, columnName);
    }

    // map にカラムが含まれるか汎用判定
    static boolean mapContainsColumn(Map<String, List<String>> map, String tableName, String columnName) {
        // テーブル名に「*」で登録されているカラム
        if (map.containsKey("*") && map.get("*").contains(columnName)) {
            return true;
        }
        // テーブル名とカラム名で登録されている
        return map.containsKey(tableName) && map.get(tableName).contains(columnName);
    }

    /** Javaフィールド名と型キャスト */
    public String toParamColumn() {
        if (isBlank(toJavaType().dbParamTemplate())) {
            return ":" + toJavaFieldName();
        } else {
            return toJavaType().dbParamTemplate().replace("{javaFieldName}", toJavaFieldName());
        }
    }

    /** カラム名と型キャスト */
    public String toSelectColumn() {
        if (isBlank(toJavaType().dbSelectTemplate())) {
            return columnName;
        } else {
            return toJavaType().dbSelectTemplate().replace("{columnName}", columnName);
        }
    }

    /** entity のフィールドをJDBC のパラメータとして渡す際の型変換コードを適用したもの */
    public String toJavaValueExpression(String javaValue) {
        var template = isBlank(toJavaType().javaCastSnippetInEntityToParam()) ? "{value}" : toJavaType().javaCastSnippetInEntityToParam();
        return template.replace("{value}", javaValue);
    }
}
