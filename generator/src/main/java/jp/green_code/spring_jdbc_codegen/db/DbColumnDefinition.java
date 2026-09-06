package jp.green_code.spring_jdbc_codegen.db;

import org.jspecify.annotations.NullUnmarked;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static jp.green_code.spring_jdbc_codegen.Param.param;
import static jp.green_code.spring_jdbc_codegen.Util.toCamelCase;
import static org.apache.commons.lang3.StringUtils.capitalize;
import static org.apache.commons.lang3.StringUtils.isBlank;

@NullUnmarked
public class DbColumnDefinition {
    public String tableName;
    public String columnName;
    public String dbTypeName;
    public Integer jdbcType;
    public Integer columnSize;
    public boolean nullable;
    public String primaryKeyName;
    public Short primaryKeySeq;
    public String defaultExpression;
    public boolean autoIncrement;

    public String toLogString() {
        return columnName + " [" + dbTypeName + "] " + (nullable ? "nullable" : "nonnull") + " " + (isPrimaryKey() ? "pk(" + primaryKeySeq + " " + primaryKeyName + ")" : "") + " default[" + defaultExpression + "]";
    }

    public String toJavaPropertyName() {
        var map = param.columnName2javaPropertyMap;
        if (map.containsKey("*") && map.get("*").containsKey(columnName)) {
            // テーブル名に「*」で登録されているカラム
            return map.get("*").get(columnName);
        } else if (map.containsKey(tableName) && map.get(tableName).containsKey(columnName)) {
            // テーブル名とカラム名で登録されている
            return map.get(tableName).get(columnName);
        }
        // カラム名はスネークケースで、Java のフィールド名はキャメルケース
        return toCamelCase(columnName, false);
    }

    public boolean hasDefault() {
        return !isBlank(defaultExpression);
    }

    public boolean isPrimaryKey() {
        return !isBlank(primaryKeyName);
    }

    /** DB 側で値を決められるカラムか（自動採番または既定値を持つ） */
    public boolean isDbDeterminable() {
        return autoIncrement || hasDefault();
    }

    public String toGetter() {
        String methodName = capitalize(toJavaPropertyName());
        return "get%s".formatted(methodName);
    }

    public String toSetter() {
        String methodName = capitalize(toJavaPropertyName());
        return "set%s".formatted(methodName);
    }

    public JavaType toJavaType() {
        return DbTypeMapper.map(dbTypeName);
    }

    public String javaSimpleTypeName() {
        var javaFqcn = toJavaType().fqcn();
        if (toJavaType().isPrimitive()) {
            return javaFqcn;
        }
        int idx = javaFqcn.lastIndexOf('.');
        return javaFqcn.substring(idx + 1);
    }

    /** import が必要な場合はfqcn を、不要な場合はnull を返す */
    public String importName() {
        var javaFqcn = toJavaType().fqcn();
        if (!javaFqcn.contains(".")) {
            // プリミティブ型と配列型
            return null;
        }
        if (javaFqcn.matches("^java\\.lang\\.[^.]+$")) {
            // java.lang はJava が暗黙にimport するため不要
            return null;
        }
        return javaFqcn;
    }

    // DB 側で値が決まるカラムか（PARAM-006）
    public boolean isDbDetermined() {
        return mapContainsColumn(param.dbDeterminedColumnsByTable, tableName, columnName);
    }

    /**
     * サポートする形の既定値か判定し、Java の値として使う部分を返す。
     * サポートしない形はnull（ENTITY-012）
     * <p>
     * 受け入れるのは次の 2 つだけ。ほかは変換しない。想定しない書き方を
     * 誤って解釈するより、@Nullable に落として利用者が値をセットする方が安全なため。
     * <ul>
     *   <li>クォート済みリテラル。型キャストは付いていてもよい　'X'::text</li>
     *   <li>数値と真偽のリテラル　7 / -1.5 / true</li>
     * </ul>
     * nextval('seq'::regclass) のような関数呼び出しは、内側にキャストを含むため
     * 末尾のキャストを機械的に外す方法では判別できない。前者の形に一致しないことで除外する。
     */
    public String toSupportedDefaultLiteral() {
        if (!hasDefault()) {
            return null;
        }
        var expr = defaultExpression.trim();
        var quoted = QUOTED_LITERAL.matcher(expr);
        if (quoted.matches()) {
            // '' は値の中のシングルクォート
            return quoted.group(1).replace("''", "'");
        }
        if (BARE_LITERAL.matcher(expr).matches()) {
            return expr;
        }
        return null;
    }

    /** 'X' または 'X'::型名。型名は空白を含む "timestamp with time zone" もある */
    private static final Pattern QUOTED_LITERAL =
            Pattern.compile("^'((?:[^']|'')*)'(?:::[A-Za-z_][A-Za-z0-9_ ]*)?$");
    /** 7 / -1.5 / true / false */
    private static final Pattern BARE_LITERAL =
            Pattern.compile("^(?:[+-]?\\d+(?:\\.\\d+)?|true|false)$");

    /**
     * Entity のフィールドの初期値となるJava の式。変換できない場合はnull（ENTITY-012）
     */
    public String toDefaultValueExpression() {
        var snippet = toJavaType().defaultValueSnippet();
        var value = toSupportedDefaultLiteral();
        if (snippet == null || value == null) {
            return null;
        }
        var normalized = normalizeForJavaTime(value);
        if (!isConvertible(normalized)) {
            return null;
        }
        // enum は定数名をそのまま埋める。ほかは文字列リテラルとして埋める
        return snippet.replace("{value}", isEnumType() ? normalized : escapeJavaString(normalized));
    }

    /**
     * 生成時に実際の変換を試す。ここで弾いておかないと、生成コードのクラス初期化で
     * 例外になり原因が分かりにくい
     */
    boolean isConvertible(String v) {
        if (isEnumType()) {
            // enum は定数名を直接参照するため、Java の識別子として妥当である必要がある
            // 妥当でなければ生成コードがコンパイルできない
            return v.matches("[A-Za-z_$][A-Za-z0-9_$]*");
        }
        try {
            switch (toJavaType().fqcn()) {
                case "java.lang.String" -> { return true; }
                case "java.lang.Boolean" -> { return "true".equals(v) || "false".equals(v); }
                case "java.lang.Short" -> Short.parseShort(v);
                case "java.lang.Integer" -> Integer.parseInt(v);
                case "java.lang.Long" -> Long.parseLong(v);
                case "java.lang.Float" -> Float.parseFloat(v);
                case "java.lang.Double" -> Double.parseDouble(v);
                case "java.math.BigDecimal" -> new java.math.BigDecimal(v);
                case "java.util.UUID" -> java.util.UUID.fromString(v);
                case "java.time.LocalTime" -> java.time.LocalTime.parse(v);
                case "java.time.LocalDate" -> java.time.LocalDate.parse(v);
                case "java.time.LocalDateTime" -> java.time.LocalDateTime.parse(v);
                case "java.time.OffsetTime" -> java.time.OffsetTime.parse(v);
                case "java.time.OffsetDateTime" -> java.time.OffsetDateTime.parse(v);
                default -> {
                    return false;
                }
            }
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

    boolean isEnumType() {
        return param.enumJavaTypeMappings.containsKey(dbTypeName);
    }

    /** PostgreSQL の日時表記をISO へ寄せる。日時型以外はそのまま返す（ENTITY-012） */
    String normalizeForJavaTime(String value) {
        var fqcn = toJavaType().fqcn();
        if (!fqcn.startsWith("java.time.")) {
            return value;
        }
        var v = value.replace(' ', 'T');
        // オフセットが +09 のように2桁の場合は :00 を補う
        //   時刻を含む値だけを対象にする。date の "2000-01-01" は日部分の -01 が
        //   オフセットに見えてしまうため、コロンの有無で判別する
        if (v.indexOf(':') >= 0) {
            v = v.replaceAll("([+-]\\d{2})$", "$1:00");
        }
        return v;
    }

    /** Java の文字列リテラルに埋め込めるようエスケープする */
    static String escapeJavaString(String value) {
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    /**
     * 非null のフィールドにできるか（ENTITY-010）
     * <p>
     * 3 条件のうち「リテラルの既定値を持つ」は、実際に Java の値へ変換できたことまでを
     * 指す。形が合っていても変換に失敗した既定値は初期値を書けないため @Nullable にする。
     */
    public boolean isNonNullField() {
        return !nullable
                && !isDbDetermined()
                && toDefaultValueExpression() != null;
    }

    /** Entity のフィールドの型名。非null かつプリミティブ化できる場合はプリミティブ */
    public String toFieldTypeName() {
        if (isNonNullField()) {
            var primitive = toJavaType().toPrimitiveName();
            if (primitive != null) {
                return primitive;
            }
        }
        return javaSimpleTypeName();
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
        return toParamColumn(toJavaPropertyName());
    }

    public String toParamColumn(String javaPropertyName) {
        if (isBlank(toJavaType().dbParamTemplate())) {
            return ":" + javaPropertyName;
        } else {
            return toJavaType().dbParamTemplate().replace("{javaPropertyName}", javaPropertyName);
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

    public boolean hasNameMapping() {
        var map = param.columnName2javaPropertyMap;
        if (map.containsKey("*") && map.get("*").containsKey(columnName)) {
            // テーブル名に「*」で登録されているカラム
            return true;
        } else if (map.containsKey(tableName) && map.get(tableName).containsKey(columnName)) {
            // テーブル名とカラム名で登録されている
            return true;
        }
        return false;
    }
}
