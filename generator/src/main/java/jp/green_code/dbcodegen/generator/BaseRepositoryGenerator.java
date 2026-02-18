package jp.green_code.dbcodegen.generator;

import jp.green_code.dbcodegen.DbCodeGenParameter;
import jp.green_code.dbcodegen.db.ColumnDefinition;
import jp.green_code.dbcodegen.db.TableDefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Stream;

import static java.lang.String.join;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.joining;
import static org.apache.commons.lang3.StringUtils.isBlank;

public class BaseRepositoryGenerator {
    final DbCodeGenParameter param;
    final TableDefinition table;

    public BaseRepositoryGenerator(DbCodeGenParameter param, TableDefinition table) {
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
            // pk がない場合は、updateByOk とfindByPk とdeleteByPk は作れない
            if (table.hasUpdateColumns()) {
                // 全部Update 対象外で
                sb.addAll(update());
                sb.add("");
                sb.addAll(updateByPk());
                sb.add("");
            }
            sb.addAll(findByPk());
            sb.add("");
            sb.addAll(deleteByPk());
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
        imports.add("java.util.HashMap");
        imports.add("java.util.HashSet");
        imports.add("java.util.LinkedHashMap");
        imports.add("java.util.Map");
        if (!table.pkColumns().isEmpty()) {
            imports.add("java.util.Optional");
        }
        table.pkColumns().stream().filter(c -> c.toJavaType().fqcn().contains(".")).forEach(c -> imports.add("%s".formatted(c.toJavaType().fqcn())));
        imports.add("%s.%s.ColumnDefinition".formatted(param.baseRepositoryPackage(), param.repositoryHelperClassName));

        var statics = new TreeSet<String>();
        statics.add("java.lang.String.join");
        statics.add("java.util.stream.Collectors.joining");

        return Stream.concat(
                imports.stream().map("import %s;"::formatted),
                statics.stream().map("import static %s;"::formatted)
        ).toList();
    }

    List<String> columns() {
        // カラム名の定数を提供
        var sb = new ArrayList<String>();
        sb.add("public static class Columns {");
        for (var col : table.columns) {
            sb.add("    public static final ColumnDefinition %s = new ColumnDefinition(\"%s\", \"%s\", \"%s\", \"%s\", %s, %s, %s, %s, %s, %s, %s, %s, %s);".formatted(col.columnName.toUpperCase(), col.columnName, col.toJavaFieldName(), col.toJavaType().fqcn(), col.dbTypeName, col.jdbcType, col.columnSize, col.primaryKeySeq, col.nullable, col.hasDefault(), ofNullable(col.toJavaType().dbParamTemplate()).map("\"%s\""::formatted).orElse("null"), ofNullable(col.toJavaType().dbSelectTemplate()).map("\"%s\""::formatted).orElse("null"), col.isSetNowColumn(), col.shouldSkipInUpdate()));
        }
        sb.add("");
        sb.add("    public static final Map<String, ColumnDefinition> MAP = new LinkedHashMap<>();");
        sb.add("");
        sb.add("    static {");
        for (var col : table.columns) {
            sb.add("        MAP.put(\"%s\", %s);".formatted(col.columnName, col.columnName.toUpperCase()));
        }
        sb.add("    }");
        sb.add("");
        // 全カラム名に型変換を付けたものをカンマでつなげた定数。select * の* の代わりに使う
        sb.add("    public static String selectAster() {");
        sb.add("        return MAP.values().stream().map(ColumnDefinition::toSelectColumn).collect(joining(\", \"));");
        sb.add("    }");
        sb.add("}");
        return sb.stream().map(s -> isBlank(s) ? s : "    " + s).toList();
    }

    List<String> insert() {
        var sb = new ArrayList<String>();
        sb.add("");
        sb.addAll(toInsertColumns());
        sb.add("");
        sb.addAll(toInsertReturning());
        sb.add("");
        sb.addAll(toInsertValues());
        sb.add("");
        if (table.needReturningInInsert()) {
            // intelliJ が警告を出すので不要な場合は作成しない
            sb.addAll(copyReturningValuesInInsert());
            sb.add("");
        }
        sb.add("public %s insert(%s entity) {".formatted(table.toEntityClassName(), table.toEntityClassName()));
        sb.add("    var sql = new ArrayList<String>();");
        sb.add("    sql.add(\"insert into \\\"%s\\\"\");".formatted(table.tableName));
        sb.add("    var insertColumns = toInsertColumns(entity);");
        sb.add("    if (insertColumns.isEmpty()) {");
        sb.add("        sql.add(\"DEFAULT VALUES\");");
        sb.add("    } else {");
        sb.add("        sql.add(\"(%s)\".formatted(join(\", \", insertColumns)));");
        sb.add("        var insertValues = toInsertValues(entity);");
        sb.add("        var insertValuesClause = insertValues.stream().map(c -> Columns.MAP.get(c) == null ? c : Columns.MAP.get(c).toParamColumn()).collect(joining(\", \"));");
        sb.add("        sql.add(\"values (%s)\".formatted(insertValuesClause));");
        sb.add("    }");
        sb.add("    var param = entityToParam(entity);");
        sb.add("    var returningColumns = toInsertReturning(entity, insertColumns);");
        sb.add("    if (returningColumns.isEmpty()) {");
        sb.add("        helper.exec(sql, param);");
        sb.add("    } else {");
        sb.add("        var returningClause = returningColumns.stream().map(c -> Columns.MAP.get(c).toSelectColumn()).collect(joining(\", \"));");
        sb.add("        sql.add(\"returning %s\".formatted(returningClause));");
        var varRet = "";
        if (table.needReturningInInsert()) {
            // intelliJ が警告を出すので不要な場合は作成しない
            varRet = "var ret = ";
        }
        sb.add("        %shelper.single(sql, param, %s.class);".formatted(varRet, table.toEntityClassName()));
        if (table.needReturningInInsert()) {
            sb.add("        copyReturningValuesInInsert(entity, ret);");
        }
        sb.add("    }");
        sb.add("    return entity;");
        sb.add("}");
        return sb.stream().map(s -> isBlank(s) ? s : "    " + s).toList();
    }

    List<String> toInsertColumns() {
        var sb = new ArrayList<String>();
        sb.add("protected List<String> toInsertColumns(%s entity) {".formatted(table.toEntityClassName()));
        sb.add("    var res = new ArrayList<String>();");
        for (var col : table.columns) {
            var justColumn = "    res.add(\"\\\"%s\\\"\");".formatted(col.columnName);
            if (col.isSetNowColumn()) {
                sb.add(justColumn);
            } else if (col.isInsertOmittable()) {
                sb.add("    if (entity.%s() != null) {".formatted(col.toGetter()));
                sb.add("        res.add(\"\\\"%s\\\"\");".formatted(col.columnName));
                sb.add("    }");
            } else {
                sb.add(justColumn);
            }
        }
        sb.add("    return res;");
        sb.add("}");
        return sb;
    }

    List<String> toInsertReturning() {
        var sb = new ArrayList<String>();
        sb.add("protected Set<String> toInsertReturning(%s entity, List<String> insertColumns) {".formatted(table.toEntityClassName()));
        sb.add("    var res = new HashSet<String>();");
        sb.add("    if (insertColumns.isEmpty()) {");
        // insert 対象のカラムがない場合はすべてのカラムをreturning 対象とする
        for (var col : table.columns) {
            sb.add("        res.add(\"%s\");".formatted(col.columnName));
        }
        if (table.columns.stream().anyMatch(col -> col.isSetNowColumn() || col.isInsertOmittable())) {
            sb.add("    } else {");
            for (var col : table.columns) {
                if (col.isSetNowColumn()) {
                    sb.add("        res.add(\"%s\");".formatted(col.columnName));
                } else if (col.isInsertOmittable()) {
                    sb.add("        if (entity.%s() == null) {".formatted(col.toGetter()));
                    sb.add("            res.add(\"%s\");".formatted(col.columnName));
                    sb.add("        }");
                }
            }
        }
        sb.add("    }");
        sb.add("    return res;");
        sb.add("}");
        return sb;
    }

    List<String> toInsertValues() {
        var sb = new ArrayList<String>();
        sb.add("protected List<String> toInsertValues(%s entity) {".formatted(table.toEntityClassName()));
        sb.add("    var res = new ArrayList<String>();");
        for (var col : table.columns) {
            if (col.isSetNowColumn()) {
                sb.add("    res.add(\"now()\");");
            } else if (col.isInsertOmittable()) {
                sb.add("    if (entity.%s() != null) {".formatted(col.toGetter()));
                sb.add("        res.add(\"%s\");".formatted(col.columnName));
                sb.add("    }");
            } else {
                sb.add("    res.add(\"%s\");".formatted(col.columnName));
            }
        }
        sb.add("    return res;");
        sb.add("}");
        return sb;
    }

    List<String> copyReturningValuesInInsert() {
        var sb = new ArrayList<String>();
        sb.add("protected void copyReturningValuesInInsert(%s entity, %s returning) {".formatted(table.toEntityClassName(), table.toEntityClassName()));
        for (var col : table.columns) {
            if (col.isSetNowColumn()) {
                sb.add("    entity.%s(returning.%s());".formatted(col.toSetter(), col.toGetter()));
            } else if (col.isInsertOmittable()) {
                sb.add("    if (entity.%s() == null) {".formatted(col.toGetter()));
                sb.add("        entity.%s(returning.%s());".formatted(col.toSetter(), col.toGetter()));
                sb.add("    }");
            }
        }
        sb.add("}");
        return sb;
    }

    List<String> update() {
        var sb = new ArrayList<String>();
        sb.add("public %s update(%s entity) {".formatted(table.toEntityClassName(), table.toEntityClassName()));
        var pkArgs = table.pkColumns().stream().map(c -> "entity.%s()".formatted(c.toGetter())).collect(joining(", "));
        sb.add("    return updateByPk(entity, %s);".formatted(pkArgs));
        sb.add("}");
        return sb.stream().map(s -> isBlank(s) ? s : "    " + s).toList();
    }

    List<String> updateByPk() {
        var sb = new ArrayList<String>();
        if (table.needReturningInUpdate()) {
            // intelliJ が警告を出すので不要な場合は作成しない
            sb.add("");
            sb.addAll(copyReturningValuesInUpdate());
        }
        sb.add("");
        var pkArgs = toPkArgs();
        sb.add("public %s updateByPk(%s entity, %s) {".formatted(table.toEntityClassName(), table.toEntityClassName(), pkArgs));
        sb.add("    var sql = new ArrayList<String>();");
        sb.add("    sql.add(\"update \\\"%s\\\"\");".formatted(table.tableName));
        var updateValues = toUpdateValues();
        sb.add("    sql.add(\"set %s\");".formatted(join(", ", updateValues)));
        var pkConditions = table.pkColumns().stream().map(c -> "\\\"%s\\\" = %s".formatted(c.columnName, c.toParamColumn())).collect(joining(" AND "));
        sb.add("    sql.add(\"where %s\");".formatted(pkConditions));
        sb.add("    var param = entityToParam(entity);");
        if (table.needReturningInUpdate()) {
            var returningColumns = table.columns.stream().filter(c -> !c.shouldSkipInUpdate() && c.isSetNowColumn()).toList();
            sb.add("    sql.add(\"returning %s\");".formatted(returningColumns.stream().map(ColumnDefinition::toSelectColumn).collect(joining(", "))));
            sb.add("    var ret = helper.single(sql, param, %s.class);".formatted(table.toEntityClassName()));
            sb.add("    copyReturningValuesInUpdate(entity, ret);");
        } else {
            sb.add("    helper.exec(sql, param);");
        }
        sb.add("    return entity;");
        sb.add("}");
        return sb.stream().map(s -> isBlank(s) ? s : "    " + s).toList();
    }

    List<String> toUpdateValues() {
        var res = new ArrayList<String>();
        for (var col : table.columns) {
            if (col.isSetNowColumn()) {
                res.add("\\\"%s\\\" = now()".formatted(col.columnName));
            } else if (!col.shouldSkipInUpdate()) {
                res.add("\\\"%s\\\" = %s".formatted(col.columnName, col.toParamColumn()));
            }
        }
        return res;
    }

    List<String> copyReturningValuesInUpdate() {
        var sb = new ArrayList<String>();
        sb.add("protected void copyReturningValuesInUpdate(%s entity, %s returning) {".formatted(table.toEntityClassName(), table.toEntityClassName()));
        for (var col : table.columns) {
            if (!col.shouldSkipInUpdate() && col.isSetNowColumn()) {
                sb.add("    entity.%s(returning.%s());".formatted(col.toSetter(), col.toGetter()));
            }
        }
        sb.add("}");
        return sb;
    }

    List<String> entityToParam() {
        var sb = new ArrayList<String>();
        sb.add("public static Map<String, Object> entityToParam(%s entity) {".formatted(table.toEntityClassName()));
        sb.add("    var param = new HashMap<String, Object>();");
        for (var col : table.columns) {
            sb.add("    param.put(\"%s\", %s);".formatted(col.toJavaFieldName(), col.toJavaValueExpression("entity.%s()".formatted(col.toGetter()))));
        }
        sb.add("    return param;");
        sb.add("}");
        return sb.stream().map(s -> isBlank(s) ? s : "    " + s).toList();
    }

    List<String> findByPk() {
        var sb = new ArrayList<String>();
        var pkArgs = toPkArgs();
        sb.add("public Optional<%s> findByPk(%s) {".formatted(table.toEntityClassName(), pkArgs));
        sb.add("    var sql = new ArrayList<String>();");
        sb.add("    sql.add(\"select %s\".formatted(Columns.selectAster()));");
        sb.add("    sql.add(\"from \\\"%s\\\"\");".formatted(table.tableName));
        var pkConditions = table.pkColumns().stream().map(c -> "\\\"%s\\\" = %s".formatted(c.columnName, c.toParamColumn())).collect(joining(" AND "));
        sb.add("    sql.add(\"where %s\");".formatted(pkConditions));
        sb.add("");
        sb.add("    var param = new HashMap<String, Object>();");
        for (var col : table.pkColumns()) {
            sb.add("    param.put(\"%s\", %s);".formatted(col.toJavaFieldName(), col.toJavaValueExpression(col.toJavaFieldName())));
        }
        sb.add("");
        sb.add("    return helper.optional(sql, param, %s.class);".formatted(table.toEntityClassName()));
        sb.add("}");
        return sb.stream().map(s -> isBlank(s) ? s : "    " + s).toList();
    }

    List<String> deleteByPk() {
        var sb = new ArrayList<String>();
        var pkArgs = toPkArgs();
        sb.add("public int deleteByPk(%s) {".formatted(pkArgs));
        sb.add("    var sql = new ArrayList<String>();");
        sb.add("    sql.add(\"delete from \\\"%s\\\"\");".formatted(table.tableName));
        var pkConditions = table.pkColumns().stream().map(c -> "\\\"%s\\\" = %s".formatted(c.columnName, c.toParamColumn())).collect(joining(" AND "));
        sb.add("    sql.add(\"where %s\");".formatted(pkConditions));
        sb.add("");
        sb.add("    var param = new HashMap<String, Object>();");
        for (var col : table.pkColumns()) {
            sb.add("    param.put(\"%s\", %s);".formatted(col.toJavaFieldName(), col.toJavaValueExpression(col.toJavaFieldName())));
        }
        sb.add("");
        sb.add("    return helper.exec(sql, param);");
        sb.add("}");
        return sb.stream().map(s -> isBlank(s) ? s : "    " + s).toList();
    }

    String toPkArgs() {
        return table.pkColumns().stream().map(c -> c.javaSimpleTypeName() + " " + c.toJavaFieldName()).collect(joining(", "));
    }
}
