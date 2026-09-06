package jp.green_code.spring_jdbc_codegen.test_app.repository.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import jp.green_code.spring_jdbc_codegen.test_app.entity.PrimitiveDefaultEntity;
import jp.green_code.spring_jdbc_codegen.test_app.repository.ColumnDefinition;
import jp.green_code.spring_jdbc_codegen.test_app.repository.RepositoryHelper;
import static java.lang.String.join;
import static java.util.stream.Collectors.joining;

/**
 * Table: primitive_default
 */
public abstract class BasePrimitiveDefaultRepository {

    protected final RepositoryHelper helper;

    public static class Columns {
        public static final ColumnDefinition PK = new ColumnDefinition("pk", "pk", "java.lang.Long", "bigserial", -5, 19, 1, false, true, null, null, false, false);
        public static final ColumnDefinition COL_LONG = new ColumnDefinition("col_long", "colLong", "java.lang.Long", "int8", -5, 19, null, false, true, null, null, false, false);
        public static final ColumnDefinition COL_INT = new ColumnDefinition("col_int", "colInt", "java.lang.Integer", "int4", 4, 10, null, false, true, null, null, false, false);
        public static final ColumnDefinition COL_SHORT = new ColumnDefinition("col_short", "colShort", "java.lang.Short", "int2", 5, 5, null, false, true, null, null, false, false);
        public static final ColumnDefinition COL_BOOL = new ColumnDefinition("col_bool", "colBool", "java.lang.Boolean", "bool", -7, 1, null, false, true, null, null, false, false);
        public static final ColumnDefinition COL_DOUBLE = new ColumnDefinition("col_double", "colDouble", "java.lang.Double", "float8", 8, 17, null, false, true, null, null, false, false);
        public static final ColumnDefinition COL_NUMERIC = new ColumnDefinition("col_numeric", "colNumeric", "java.math.BigDecimal", "numeric", 2, 0, null, false, true, null, null, false, false);
        public static final ColumnDefinition COL_TEXT = new ColumnDefinition("col_text", "colText", "java.lang.String", "text", 12, 2147483647, null, false, true, null, null, false, false);
        public static final ColumnDefinition COL_TIMESTAMPTZ = new ColumnDefinition("col_timestamptz", "colTimestamptz", "java.time.OffsetDateTime", "timestamptz", 93, 35, null, false, true, null, null, false, false);
        public static final ColumnDefinition COL_DATE = new ColumnDefinition("col_date", "colDate", "java.time.LocalDate", "date", 91, 13, null, false, true, null, null, false, false);
        public static final ColumnDefinition COL_UUID = new ColumnDefinition("col_uuid", "colUuid", "java.util.UUID", "uuid", 1111, 2147483647, null, false, true, null, null, false, false);
        public static final ColumnDefinition COL_UUID_FUNC = new ColumnDefinition("col_uuid_func", "colUuidFunc", "java.util.UUID", "uuid", 1111, 2147483647, null, false, true, null, null, false, false);
        public static final ColumnDefinition COL_ENUM = new ColumnDefinition("col_enum", "colEnum", "jp.green_code.spring_jdbc_codegen.test_app.StatusEnum", "status_enum", 12, 2147483647, null, false, true, ":{javaPropertyName}::status_enum", null, false, false);
        public static final ColumnDefinition COL_NO_DEFAULT = new ColumnDefinition("col_no_default", "colNoDefault", "java.lang.String", "text", 12, 2147483647, null, false, false, null, null, false, false);

        public static final Map<String, ColumnDefinition> MAP = new LinkedHashMap<>();

        static {
            MAP.put("pk", PK);
            MAP.put("col_long", COL_LONG);
            MAP.put("col_int", COL_INT);
            MAP.put("col_short", COL_SHORT);
            MAP.put("col_bool", COL_BOOL);
            MAP.put("col_double", COL_DOUBLE);
            MAP.put("col_numeric", COL_NUMERIC);
            MAP.put("col_text", COL_TEXT);
            MAP.put("col_timestamptz", COL_TIMESTAMPTZ);
            MAP.put("col_date", COL_DATE);
            MAP.put("col_uuid", COL_UUID);
            MAP.put("col_uuid_func", COL_UUID_FUNC);
            MAP.put("col_enum", COL_ENUM);
            MAP.put("col_no_default", COL_NO_DEFAULT);
        }

        public static String selectAster() {
            return MAP.values().stream().map(ColumnDefinition::toSelectColumn).collect(joining(", "));
        }
    }

    public BasePrimitiveDefaultRepository(RepositoryHelper helper) {
        this.helper = helper;
    }

    /** 他テーブルのカラム、重複指定、PK 指定を弾く */
    protected List<ColumnDefinition> validateColumns(ColumnDefinition first, ColumnDefinition[] rest, boolean rejectPk) {
        var columns = new ArrayList<ColumnDefinition>();
        columns.add(first);
        columns.addAll(Arrays.asList(rest));
        var names = new HashSet<String>();
        for (var c : columns) {
            if (rejectPk && c.getPrimaryKeySeq() != null) {
                throw new IllegalArgumentException("PK は指定できません: " + c.getColumnName());
            }
            if (Columns.MAP.get(c.getColumnName()) != c) {
                throw new IllegalArgumentException("primitive_default のカラムではありません: " + c.getColumnName());
            }
            if (!names.add(c.getColumnName())) {
                throw new IllegalArgumentException("カラムが重複しています: " + c.getColumnName());
            }
        }
        return columns;
    }

    protected List<String> toInsertColumns(Set<String> excludeColumns) {
        return Columns.MAP.values().stream()
                .filter(c -> !excludeColumns.contains(c.getColumnName()))
                .map(c -> "\"%s\"".formatted(c.getColumnName()))
                .toList();
    }

    protected List<String> toInsertValues(Set<String> excludeColumns) {
        return Columns.MAP.values().stream()
                .filter(c -> !excludeColumns.contains(c.getColumnName()))
                .map(ColumnDefinition::toParamColumn)
                .toList();
    }

    protected Set<String> toInsertReturning(Set<String> excludeColumns) {
        var res = new LinkedHashSet<String>();
        for (var c : Columns.MAP.values()) {
            if (c.isReturning() || excludeColumns.contains(c.getColumnName())) {
                res.add(c.getColumnName());
            }
        }
        return res;
    }

    protected void copyReturningValues(PrimitiveDefaultEntity entity, PrimitiveDefaultEntity returning, Set<String> returningColumns) {
        if (returningColumns.contains("pk")) {
            entity.setPk(returning.getPk());
        }
        if (returningColumns.contains("col_long")) {
            entity.setColLong(returning.getColLong());
        }
        if (returningColumns.contains("col_int")) {
            entity.setColInt(returning.getColInt());
        }
        if (returningColumns.contains("col_short")) {
            entity.setColShort(returning.getColShort());
        }
        if (returningColumns.contains("col_bool")) {
            entity.setColBool(returning.getColBool());
        }
        if (returningColumns.contains("col_double")) {
            entity.setColDouble(returning.getColDouble());
        }
        if (returningColumns.contains("col_numeric")) {
            entity.setColNumeric(returning.getColNumeric());
        }
        if (returningColumns.contains("col_text")) {
            entity.setColText(returning.getColText());
        }
        if (returningColumns.contains("col_timestamptz")) {
            entity.setColTimestamptz(returning.getColTimestamptz());
        }
        if (returningColumns.contains("col_date")) {
            entity.setColDate(returning.getColDate());
        }
        if (returningColumns.contains("col_uuid")) {
            entity.setColUuid(returning.getColUuid());
        }
        if (returningColumns.contains("col_uuid_func")) {
            entity.setColUuidFunc(returning.getColUuidFunc());
        }
        if (returningColumns.contains("col_enum")) {
            entity.setColEnum(returning.getColEnum());
        }
        if (returningColumns.contains("col_no_default")) {
            entity.setColNoDefault(returning.getColNoDefault());
        }
    }

    protected int execWithReturning(List<String> sql, Map<String, Object> param, PrimitiveDefaultEntity entity, Set<String> returningColumns) {
        if (returningColumns.isEmpty()) {
            return this.helper.exec(sql, param);
        }
        var returningClause = returningColumns.stream().map(c -> Objects.requireNonNull(Columns.MAP.get(c), "Unknown column " + c).toSelectColumn()).collect(joining(", "));
        sql.add("returning %s".formatted(returningClause));
        var ret = this.helper.optional(sql, param, PrimitiveDefaultEntity.class);
        ret.ifPresent(r -> copyReturningValues(entity, r, returningColumns));
        return ret.isPresent() ? 1 : 0;
    }

    /** 全カラムをINSERT 対象とする */
    public int insertAllColumns(PrimitiveDefaultEntity entity) {
        return doInsert(entity, Set.of());
    }

    /** 指定したカラムをINSERT 対象から外し、DB の既定値を使う */
    public int insertExcept(PrimitiveDefaultEntity entity, ColumnDefinition first, ColumnDefinition... rest) {
        var exclude = new LinkedHashSet<String>();
        validateColumns(first, rest, false).forEach(c -> exclude.add(c.getColumnName()));
        return doInsert(entity, exclude);
    }

    /** PK をINSERT 対象から外し、DB に値を決めさせる */
    public int insertExceptPk(PrimitiveDefaultEntity entity) {
        return insertExcept(entity, Columns.PK);
    }

    protected int doInsert(PrimitiveDefaultEntity entity, Set<String> excludeColumns) {
        var sql = new ArrayList<String>();
        sql.add("insert into \"primitive_default\"");
        var insertColumns = toInsertColumns(excludeColumns);
        if (insertColumns.isEmpty()) {
            sql.add("DEFAULT VALUES");
        } else {
            sql.add("(%s)".formatted(join(", ", insertColumns)));
            sql.add("values (%s)".formatted(join(", ", toInsertValues(excludeColumns))));
        }
        var param = entityToParam(entity);
        return execWithReturning(sql, param, entity, toInsertReturning(excludeColumns));
    }

    public static Map<String, Object> entityToParam(PrimitiveDefaultEntity entity) {
        var param = new HashMap<String, Object>();
        param.put("pk", entity.getPk());
        param.put("colLong", entity.getColLong());
        param.put("colInt", entity.getColInt());
        param.put("colShort", entity.getColShort());
        param.put("colBool", entity.getColBool());
        param.put("colDouble", entity.getColDouble());
        param.put("colNumeric", entity.getColNumeric());
        param.put("colText", entity.getColText());
        param.put("colTimestamptz", entity.getColTimestamptz());
        param.put("colDate", entity.getColDate());
        param.put("colUuid", entity.getColUuid());
        param.put("colUuidFunc", entity.getColUuidFunc());
        param.put("colEnum", entity.getColEnum() == null ? null : entity.getColEnum().name());
        param.put("colNoDefault", entity.getColNoDefault());
        return param;
    }

    /** PK を除く全カラムを更新する */
    public int updateAllColumns(PrimitiveDefaultEntity entity) {
        return doUpdate(entity, Columns.MAP.values().stream().filter(c -> c.getPrimaryKeySeq() == null).toList());
    }

    /** 指定したカラムだけを更新する */
    public int updateInclude(PrimitiveDefaultEntity entity, ColumnDefinition first, ColumnDefinition... rest) {
        return doUpdate(entity, validateColumns(first, rest, true));
    }

    protected int doUpdate(PrimitiveDefaultEntity entity, List<ColumnDefinition> setColumns) {
        var sql = new ArrayList<String>();
        var param = entityToParam(entity);
        var setClause = setColumns.stream().map(BaseColumnDefinition::toUpdateSetClause).collect(joining(", "));
        sql.add("update \"primitive_default\"");
        sql.add("set %s".formatted(setClause));
        param.put("__pk1", entity.getPk());
        sql.add("where \"pk\" = :__pk1");
        return execWithReturning(sql, param, entity, Set.of());
    }

    public Optional<PrimitiveDefaultEntity> findByPk(Long pk) {
        var __sql = new ArrayList<String>();
        __sql.add("select %s".formatted(Columns.selectAster()));
        __sql.add("from \"primitive_default\"");
        __sql.add("where \"pk\" = :pk");

        var __param = new HashMap<String, Object>();
        __param.put("pk", pk);

        return this.helper.optional(__sql, __param, PrimitiveDefaultEntity.class);
    }

    public int deleteByPk(Long pk) {
        var __sql = new ArrayList<String>();
        __sql.add("delete from \"primitive_default\"");
        __sql.add("where \"pk\" = :pk");

        var __param = new HashMap<String, Object>();
        __param.put("pk", pk);

        return this.helper.exec(__sql, __param);
    }
}