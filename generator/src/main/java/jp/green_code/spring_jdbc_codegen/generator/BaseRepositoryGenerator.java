package jp.green_code.spring_jdbc_codegen.generator;

import jp.green_code.spring_jdbc_codegen.Param;
import jp.green_code.spring_jdbc_codegen.db.DbColumnDefinition;
import jp.green_code.spring_jdbc_codegen.db.DbTableDefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Stream;

import static java.lang.String.join;
import static java.util.Locale.ROOT;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.joining;
import static org.apache.commons.lang3.StringUtils.isBlank;

public class BaseRepositoryGenerator {
    final Param param;
    final DbTableDefinition table;

    public BaseRepositoryGenerator(Param param, DbTableDefinition table) {
        this.param = param;
        this.table = table;
    }

    public String generateBaseRepositoryCode() {
        var sb = new ArrayList<String>();
        // package
        sb.add("package %s;".formatted(param.baseRepositoryPackage()));
        sb.add("");
        // import
        sb.addAll(imports());
        sb.add("");
        // class
        sb.add("/**");
        sb.add(" * Table: %s".formatted(table.tableName));
        sb.add(" */");
        sb.add("public abstract class %s {".formatted(table.toBaseRepositoryClassName()));
        sb.add("");
        sb.add("    protected final %s helper;".formatted(param.repositoryHelperClassName));
        if (table.needCustomMapper()) {
            sb.add("");
            sb.add("    public static final %s ROW_MAPPER = new %s();".formatted(table.toMapperClassName(), table.toMapperClassName()));
        }
        sb.add("");
        sb.addAll(columns());
        sb.add("");
        sb.add("    public %s(%s helper) {".formatted(table.toBaseRepositoryClassName(), param.repositoryHelperClassName));
        sb.add("        this.helper = helper;");
        sb.add("    }");
        sb.addAll(insert());
        sb.add("");
        sb.addAll(entityToParam());
        if (!table.pkColumns().isEmpty()) {
            // pk がない場合は、update とfindByPk とdeleteByPk は作れない
            if (!table.nonPkColumns().isEmpty()) {
                // PK しかないテーブルはset 句が空になるため update は作れない
                sb.add("");
                sb.addAll(update());
            }
            sb.add("");
            sb.addAll(findByPk());
            sb.add("");
            sb.addAll(deleteByPk());
        }
        if (table.needCustomMapper()) {
            // Mapper はPK の有無に関係なく必要。ROW_MAPPER の宣言と条件を揃える
            sb.add("");
            sb.addAll(customMapper());
        }
        sb.add("}");
        return join("\n", sb);
    }

    List<String> imports() {
        var imports = new TreeSet<String>();
        imports.add(param.entityPackage + "." + table.toEntityClassName());
        imports.add("java.util.List");
        imports.add("java.util.Set");
        imports.add("java.util.ArrayList");
        imports.add("java.util.Arrays");
        imports.add("java.util.HashMap");
        imports.add("java.util.HashSet");
        imports.add("java.util.LinkedHashMap");
        imports.add("java.util.LinkedHashSet");
        imports.add("java.util.Map");
        imports.add("java.util.Objects");
        if (!table.pkColumns().isEmpty()) {
            imports.add("java.util.Optional");
        }
        table.pkColumns().stream().map(DbColumnDefinition::importName).filter(c -> !isBlank(c)).forEach(imports::add);
        imports.add("%s.%s".formatted(param.repositoryPackage, param.repositoryHelperClassName));
        imports.add("%s.%s".formatted(param.repositoryPackage, param.columnDefinitionClassName));
        if (table.needCustomMapper()) {
            imports.add(Fqcn.NULL_MARKED);
            imports.add(Fqcn.NULLABLE);
            imports.add(Fqcn.BEAN_PROPERTY_ROW_MAPPER);
        }

        var statics = new TreeSet<String>();
        statics.add("java.lang.String.join");
        statics.add("java.util.stream.Collectors.joining");

        return Stream.concat(
                imports.stream().map("import %s;"::formatted),
                statics.stream().map("import static %s;"::formatted)
        ).toList();
    }

    List<String> columns() {
        // カラム情報オブジェクトを作成
        var sb = new ArrayList<String>();
        sb.add("public static class Columns {");
        for (var col : table.columns) {
            sb.add("    public static final %s %s = new %s(\"%s\", \"%s\", \"%s\", \"%s\", %s, %s, %s, %s, %s, %s, %s, %s, %s);".formatted(param.columnDefinitionClassName, col.columnName.toUpperCase(ROOT), param.columnDefinitionClassName, col.columnName, col.toJavaPropertyName(), col.toJavaType().fqcn(), col.dbTypeName, col.jdbcType, col.columnSize, col.primaryKeySeq, col.nullable, col.hasDefault(), ofNullable(col.toJavaType().dbParamTemplate()).map("\"%s\""::formatted).orElse("null"), ofNullable(col.toJavaType().dbSelectTemplate()).map("\"%s\""::formatted).orElse("null"), col.isReturningColumn(), col.hasNameMapping()));
        }
        sb.add("");
        sb.add("    public static final Map<String, %s> MAP = new LinkedHashMap<>();".formatted(param.columnDefinitionClassName));
        sb.add("");
        sb.add("    static {");
        for (var col : table.columns) {
            sb.add("        MAP.put(\"%s\", %s);".formatted(col.columnName, col.columnName.toUpperCase(ROOT)));
        }
        sb.add("    }");
        sb.add("");
        // 全カラム名に型変換を付けたものをカンマでつなげた定数。select * の* の代わりに使う
        sb.add("    public static String selectAster() {");
        sb.add("        return MAP.values().stream().map(%s::toSelectColumn).collect(joining(\", \"));".formatted(param.columnDefinitionClassName));
        sb.add("    }");
        sb.add("}");
        return sb.stream().map(s -> isBlank(s) ? s : "    " + s).toList();
    }

    List<String> insert() {
        var sb = new ArrayList<String>();
        sb.add("");
        sb.addAll(validateColumns());
        sb.add("");
        sb.addAll(toInsertColumns());
        sb.add("");
        sb.addAll(toInsertValues());
        sb.add("");
        sb.addAll(toInsertReturning());
        sb.add("");
        // execWithReturning から呼ぶため、returning が不要なテーブルでも生成する
        sb.addAll(copyReturningValues());
        sb.add("");
        sb.addAll(execWithReturning());
        sb.add("");
        sb.add("/** 全カラムをINSERT 対象とする */");
        sb.add("public int insertAllColumns(%s entity) {".formatted(table.toEntityClassName()));
        sb.add("    return doInsert(entity, Set.of());");
        sb.add("}");
        sb.add("");
        sb.add("/** 指定したカラムをINSERT 対象から外し、DB の既定値を使う */");
        sb.add("public int insertExcept(%s entity, %s first, %s... rest) {".formatted(table.toEntityClassName(), param.columnDefinitionClassName, param.columnDefinitionClassName));
        sb.add("    var exclude = new LinkedHashSet<String>();");
        sb.add("    validateColumns(first, rest, false).forEach(c -> exclude.add(c.getColumnName()));");
        sb.add("    return doInsert(entity, exclude);");
        sb.add("}");
        if (table.canInsertExceptPk()) {
            var pkConsts = table.pkColumns().stream().map(c -> "Columns." + c.columnName.toUpperCase(ROOT)).collect(joining(", "));
            sb.add("");
            sb.add("/** PK をINSERT 対象から外し、DB に値を決めさせる */");
            sb.add("public int insertExceptPk(%s entity) {".formatted(table.toEntityClassName()));
            sb.add("    return insertExcept(entity, %s);".formatted(pkConsts));
            sb.add("}");
        }
        sb.add("");
        sb.add("protected int doInsert(%s entity, Set<String> excludeColumns) {".formatted(table.toEntityClassName()));
        sb.add("    var sql = new ArrayList<String>();");
        sb.add("    sql.add(\"insert into \\\"%s\\\"\");".formatted(table.tableName));
        sb.add("    var insertColumns = toInsertColumns(excludeColumns);");
        sb.add("    if (insertColumns.isEmpty()) {");
        sb.add("        sql.add(\"DEFAULT VALUES\");");
        sb.add("    } else {");
        sb.add("        sql.add(\"(%s)\".formatted(join(\", \", insertColumns)));");
        sb.add("        sql.add(\"values (%s)\".formatted(join(\", \", toInsertValues(excludeColumns))));");
        sb.add("    }");
        sb.add("    var param = entityToParam(entity);");
        sb.add("    return execWithReturning(sql, param, entity, toInsertReturning(excludeColumns));");
        sb.add("}");
        return sb.stream().map(s -> isBlank(s) ? s : "    " + s).toList();
    }

    /** カラム指定の検証。insert とupdate で共通 */
    List<String> validateColumns() {
        var sb = new ArrayList<String>();
        sb.add("/** 他テーブルのカラム、重複指定、PK 指定を弾く */");
        sb.add("protected List<%s> validateColumns(%s first, %s[] rest, boolean rejectPk) {".formatted(
                param.columnDefinitionClassName, param.columnDefinitionClassName, param.columnDefinitionClassName));
        sb.add("    var columns = new ArrayList<%s>();".formatted(param.columnDefinitionClassName));
        sb.add("    columns.add(first);");
        sb.add("    columns.addAll(Arrays.asList(rest));");
        sb.add("    var names = new HashSet<String>();");
        sb.add("    for (var c : columns) {");
        // 判定の安い順に並べる。PK はフィールド参照だけで済む
        sb.add("        if (rejectPk && c.getPrimaryKeySeq() != null) {");
        sb.add("            throw new IllegalArgumentException(\"PK は指定できません: \" + c.getColumnName());");
        sb.add("        }");
        sb.add("        if (Columns.MAP.get(c.getColumnName()) != c) {");
        sb.add("            throw new IllegalArgumentException(\"%s のカラムではありません: \" + c.getColumnName());".formatted(table.tableName));
        sb.add("        }");
        sb.add("        if (!names.add(c.getColumnName())) {");
        sb.add("            throw new IllegalArgumentException(\"カラムが重複しています: \" + c.getColumnName());");
        sb.add("        }");
        sb.add("    }");
        sb.add("    return columns;");
        sb.add("}");
        return sb;
    }

    List<String> toInsertColumns() {
        var sb = new ArrayList<String>();
        sb.add("protected List<String> toInsertColumns(Set<String> excludeColumns) {");
        sb.add("    return Columns.MAP.values().stream()");
        sb.add("            .filter(c -> !excludeColumns.contains(c.getColumnName()))");
        sb.add("            .map(c -> \"\\\"%s\\\"\".formatted(c.getColumnName()))");
        sb.add("            .toList();");
        sb.add("}");
        return sb;
    }

    List<String> toInsertValues() {
        var sb = new ArrayList<String>();
        sb.add("protected List<String> toInsertValues(Set<String> excludeColumns) {");
        sb.add("    return Columns.MAP.values().stream()");
        sb.add("            .filter(c -> !excludeColumns.contains(c.getColumnName()))");
        sb.add("            .map(%s::toParamColumn)".formatted(param.columnDefinitionClassName));
        sb.add("            .toList();");
        sb.add("}");
        return sb;
    }

    List<String> toInsertReturning() {
        var sb = new ArrayList<String>();
        sb.add("protected Set<String> toInsertReturning(Set<String> excludeColumns) {");
        sb.add("    var res = new LinkedHashSet<String>();");
        // 全カラムを除外した場合は excludeColumns が全カラム名を含むため、
        // 「INSERT 対象が1つもなければ全カラム」の規則は下の条件で自然に満たされる
        sb.add("    for (var c : Columns.MAP.values()) {");
        sb.add("        if (c.isReturning() || excludeColumns.contains(c.getColumnName())) {");
        sb.add("            res.add(c.getColumnName());");
        sb.add("        }");
        sb.add("    }");
        sb.add("    return res;");
        sb.add("}");
        return sb;
    }

    /** returning 句の組み立てと実行。insert とupdate で共通 */
    List<String> execWithReturning() {
        var sb = new ArrayList<String>();
        sb.add("protected int execWithReturning(List<String> sql, Map<String, Object> param, %s entity, Set<String> returningColumns) {".formatted(table.toEntityClassName()));
        sb.add("    if (returningColumns.isEmpty()) {");
        sb.add("        return this.helper.exec(sql, param);");
        sb.add("    }");
        // カラム名のクォートと型変換は ColumnDefinition に任せる（実行時に評価する）
        sb.add("    var returningClause = returningColumns.stream().map(c -> Objects.requireNonNull(Columns.MAP.get(c), \"Unknown column \" + c).toSelectColumn()).collect(joining(\", \"));");
        sb.add("    sql.add(\"returning %s\".formatted(returningClause));");
        // 該当レコードがない場合は書き戻しを行わず 0 を返す
        sb.add("    var ret = this.helper.optional(sql, param, %s);".formatted(table.toMapperOrEntityClass()));
        sb.add("    ret.ifPresent(r -> copyReturningValues(entity, r, returningColumns));");
        sb.add("    return ret.isPresent() ? 1 : 0;");
        sb.add("}");
        return sb;
    }

    List<String> copyReturningValues() {
        var sb = new ArrayList<String>();
        sb.add("protected void copyReturningValues(%s entity, %s returning, Set<String> returningColumns) {".formatted(table.toEntityClassName(), table.toEntityClassName()));
        for (var col : table.columns) {
            sb.add("    if (returningColumns.contains(\"%s\")) {".formatted(col.columnName));
            sb.add("        entity.%s(returning.%s());".formatted(col.toSetter(), col.toGetter()));
            sb.add("    }");
        }
        sb.add("}");
        return sb;
    }

    List<String> update() {
        var sb = new ArrayList<String>();
        sb.add("/** PK を除く全カラムを更新する */");
        sb.add("public int updateAllColumns(%s entity) {".formatted(table.toEntityClassName()));
        sb.add("    return doUpdate(entity, Columns.MAP.values().stream().filter(c -> c.getPrimaryKeySeq() == null).toList());");
        sb.add("}");
        sb.add("");
        sb.add("/** 指定したカラムだけを更新する */");
        sb.add("public int updateInclude(%s entity, %s first, %s... rest) {".formatted(table.toEntityClassName(), param.columnDefinitionClassName, param.columnDefinitionClassName));
        sb.add("    return doUpdate(entity, validateColumns(first, rest, true));");
        sb.add("}");
        sb.add("");
        sb.add("protected int doUpdate(%s entity, List<%s> setColumns) {".formatted(table.toEntityClassName(), param.columnDefinitionClassName));
        sb.add("    var sql = new ArrayList<String>();");
        sb.add("    var param = entityToParam(entity);");
        sb.add("    var setClause = setColumns.stream().map(%s::toUpdateSetClause).collect(joining(\", \"));".formatted(param.toBaseColumnDefinitionClassName()));
        sb.add("    sql.add(\"update \\\"%s\\\"\");".formatted(table.tableName));
        sb.add("    sql.add(\"set %s\".formatted(setClause));");
        var pkConditions = new ArrayList<String>();
        var i = 0;
        for (var col : table.pkColumns()) {
            i++;
            pkConditions.add("\\\"%s\\\" = %s".formatted(col.columnName, col.toParamColumn("__pk" + i)));
            sb.add("    param.put(\"__pk%d\", %s);".formatted(i, col.toJavaValueExpression("entity.%s()".formatted(col.toGetter()))));
        }
        sb.add("    sql.add(\"where %s\");".formatted(join(" AND ", pkConditions)));

        if (table.needReturningInUpdate()) {
            var returningNames = table.columns.stream().filter(DbColumnDefinition::isReturningColumn)
                    .map(c -> "\"%s\"".formatted(c.columnName)).collect(joining(", "));
            sb.add("    return execWithReturning(sql, param, entity, Set.of(%s));".formatted(returningNames));
        } else {
            sb.add("    return execWithReturning(sql, param, entity, Set.of());");
        }
        sb.add("}");
        return sb.stream().map(s -> isBlank(s) ? s : "    " + s).toList();
    }

    List<String> entityToParam() {
        var sb = new ArrayList<String>();
        sb.add("public static Map<String, Object> entityToParam(%s entity) {".formatted(table.toEntityClassName()));
        sb.add("    var param = new HashMap<String, Object>();");
        for (var col : table.columns) {
            sb.add("    param.put(\"%s\", %s);".formatted(col.toJavaPropertyName(), col.toJavaValueExpression("entity.%s()".formatted(col.toGetter()))));
        }
        sb.add("    return param;");
        sb.add("}");
        return sb.stream().map(s -> isBlank(s) ? s : "    " + s).toList();
    }

    List<String> findByPk() {
        var sb = new ArrayList<String>();
        // 引数がカラム名由来のため、ローカル変数には __ を付けて衝突を避ける
        //   カラム名がsql / param のときに引数と同名になる
        var pkArgs = toPkArgs();
        sb.add("public Optional<%s> findByPk(%s) {".formatted(table.toEntityClassName(), pkArgs));
        sb.add("    var __sql = new ArrayList<String>();");
        sb.add("    __sql.add(\"select %s\".formatted(Columns.selectAster()));");
        sb.add("    __sql.add(\"from \\\"%s\\\"\");".formatted(table.tableName));
        var pkConditions = table.pkColumns().stream().map(c -> "\\\"%s\\\" = %s".formatted(c.columnName, c.toParamColumn())).collect(joining(" AND "));
        sb.add("    __sql.add(\"where %s\");".formatted(pkConditions));
        sb.add("");
        sb.add("    var __param = new HashMap<String, Object>();");
        for (var col : table.pkColumns()) {
            sb.add("    __param.put(\"%s\", %s);".formatted(col.toJavaPropertyName(), col.toJavaValueExpression(col.toJavaPropertyName())));
        }
        sb.add("");
        sb.add("    return this.helper.optional(__sql, __param, %s);".formatted(table.toMapperOrEntityClass()));
        sb.add("}");
        return sb.stream().map(s -> isBlank(s) ? s : "    " + s).toList();
    }

    List<String> deleteByPk() {
        var sb = new ArrayList<String>();
        var pkArgs = toPkArgs();
        sb.add("public int deleteByPk(%s) {".formatted(pkArgs));
        sb.add("    var __sql = new ArrayList<String>();");
        sb.add("    __sql.add(\"delete from \\\"%s\\\"\");".formatted(table.tableName));
        var pkConditions = table.pkColumns().stream().map(c -> "\\\"%s\\\" = %s".formatted(c.columnName, c.toParamColumn())).collect(joining(" AND "));
        sb.add("    __sql.add(\"where %s\");".formatted(pkConditions));
        sb.add("");
        sb.add("    var __param = new HashMap<String, Object>();");
        for (var col : table.pkColumns()) {
            sb.add("    __param.put(\"%s\", %s);".formatted(col.toJavaPropertyName(), col.toJavaValueExpression(col.toJavaPropertyName())));
        }
        sb.add("");
        sb.add("    return this.helper.exec(__sql, __param);");
        sb.add("}");
        return sb.stream().map(s -> isBlank(s) ? s : "    " + s).toList();
    }

    String toPkArgs() {
        return table.pkColumns().stream().map(c -> c.javaSimpleTypeName() + " " + c.toJavaPropertyName()).collect(joining(", "));
    }

    List<String> customMapper() {
        var sb = new ArrayList<String>();
        sb.add(Fqcn.toAnnotation(Fqcn.NULL_MARKED));
        sb.add("public static class %s extends BeanPropertyRowMapper<%s> {".formatted(table.toMapperClassName(), table.toEntityClassName()));
        sb.add("    public %s() {".formatted(table.toMapperClassName()));
        sb.add("        super(%s.class);".formatted(table.toEntityClassName()));
        sb.add("    }");
        sb.add("");
        sb.add("    @Override");
        sb.add("    protected String underscoreName(@Nullable String name) {");
        for (var col : table.columns.stream().filter(DbColumnDefinition::hasNameMapping).toList()) {
            sb.add("        if (\"%s\".equals(name)) {".formatted(col.toJavaPropertyName()));
            sb.add("            return \"%s\";".formatted(col.columnName));
            sb.add("        }");
        }
        sb.add("        return super.underscoreName(name);");
        sb.add("    }");
        sb.add("}");
        return sb.stream().map(s -> isBlank(s) ? s : "    " + s).toList();
    }
}
