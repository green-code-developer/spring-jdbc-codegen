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
import jp.green_code.spring_jdbc_codegen.test_app.entity.CoverageTestEntity;
import jp.green_code.spring_jdbc_codegen.test_app.repository.ColumnDefinition;
import jp.green_code.spring_jdbc_codegen.test_app.repository.RepositoryHelper;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import static java.lang.String.join;
import static java.util.stream.Collectors.joining;

/**
 * Table: coverage_test
 */
public abstract class BaseCoverageTestRepository {

    protected final RepositoryHelper helper;

    public static final CoverageTestMapper ROW_MAPPER = new CoverageTestMapper();

    public static class Columns {
        public static final ColumnDefinition PK = new ColumnDefinition("pk", "pk", "java.lang.Long", "bigserial", -5, 19, 1, false, true, null, null, false, false);
        public static final ColumnDefinition COL_NULLABLE_DEFAULT = new ColumnDefinition("col_nullable_default", "colNullableDefault", "java.lang.String", "text", 12, 2147483647, null, true, true, null, null, false, false);
        public static final ColumnDefinition COL_NOTNULL_NODEFAULT = new ColumnDefinition("col_notnull_nodefault", "colNotnullNodefault", "java.lang.String", "text", 12, 2147483647, null, false, false, null, null, false, false);
        public static final ColumnDefinition COL_NOW_WITH_DEFAULT = new ColumnDefinition("col_now_with_default", "colNowWithDefault", "java.time.OffsetDateTime", "timestamptz", 93, 35, null, false, true, null, null, true, false);
        public static final ColumnDefinition UPDATED_AT = new ColumnDefinition("updated_at", "updatedAt", "java.time.OffsetDateTime", "timestamptz", 93, 35, null, true, false, null, null, true, false);
        public static final ColumnDefinition COL_ENUM_DEFAULT = new ColumnDefinition("col_enum_default", "colEnumDefault", "jp.green_code.spring_jdbc_codegen.test_app.StatusEnum", "status_enum", 12, 2147483647, null, false, true, ":{javaPropertyName}::status_enum", null, false, false);
        public static final ColumnDefinition COL_ENUM_NULLABLE_DEFAULT = new ColumnDefinition("col_enum_nullable_default", "colEnumNullableDefault", "jp.green_code.spring_jdbc_codegen.test_app.StatusEnum", "status_enum", 12, 2147483647, null, true, true, ":{javaPropertyName}::status_enum", null, false, false);
        public static final ColumnDefinition MAPPED_NULLABLE = new ColumnDefinition("mapped_nullable", "mappedNullableJavaName", "java.lang.String", "text", 12, 2147483647, null, true, false, null, null, false, true);

        public static final Map<String, ColumnDefinition> MAP = new LinkedHashMap<>();

        static {
            MAP.put("pk", PK);
            MAP.put("col_nullable_default", COL_NULLABLE_DEFAULT);
            MAP.put("col_notnull_nodefault", COL_NOTNULL_NODEFAULT);
            MAP.put("col_now_with_default", COL_NOW_WITH_DEFAULT);
            MAP.put("updated_at", UPDATED_AT);
            MAP.put("col_enum_default", COL_ENUM_DEFAULT);
            MAP.put("col_enum_nullable_default", COL_ENUM_NULLABLE_DEFAULT);
            MAP.put("mapped_nullable", MAPPED_NULLABLE);
        }

        public static String selectAster() {
            return MAP.values().stream().map(ColumnDefinition::toSelectColumn).collect(joining(", "));
        }
    }

    public BaseCoverageTestRepository(RepositoryHelper helper) {
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
                throw new IllegalArgumentException("coverage_test のカラムではありません: " + c.getColumnName());
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

    protected void copyReturningValues(CoverageTestEntity entity, CoverageTestEntity returning, Set<String> returningColumns) {
        if (returningColumns.contains("pk")) {
            entity.setPk(returning.getPk());
        }
        if (returningColumns.contains("col_nullable_default")) {
            entity.setColNullableDefault(returning.getColNullableDefault());
        }
        if (returningColumns.contains("col_notnull_nodefault")) {
            entity.setColNotnullNodefault(returning.getColNotnullNodefault());
        }
        if (returningColumns.contains("col_now_with_default")) {
            entity.setColNowWithDefault(returning.getColNowWithDefault());
        }
        if (returningColumns.contains("updated_at")) {
            entity.setUpdatedAt(returning.getUpdatedAt());
        }
        if (returningColumns.contains("col_enum_default")) {
            entity.setColEnumDefault(returning.getColEnumDefault());
        }
        if (returningColumns.contains("col_enum_nullable_default")) {
            entity.setColEnumNullableDefault(returning.getColEnumNullableDefault());
        }
        if (returningColumns.contains("mapped_nullable")) {
            entity.setMappedNullableJavaName(returning.getMappedNullableJavaName());
        }
    }

    protected int execWithReturning(List<String> sql, Map<String, Object> param, CoverageTestEntity entity, Set<String> returningColumns) {
        if (returningColumns.isEmpty()) {
            return this.helper.exec(sql, param);
        }
        var returningClause = returningColumns.stream().map(c -> Objects.requireNonNull(Columns.MAP.get(c), "Unknown column " + c).toSelectColumn()).collect(joining(", "));
        sql.add("returning %s".formatted(returningClause));
        var ret = this.helper.optional(sql, param, ROW_MAPPER);
        ret.ifPresent(r -> copyReturningValues(entity, r, returningColumns));
        return ret.isPresent() ? 1 : 0;
    }

    /** 全カラムをINSERT 対象とする */
    public int insertAllColumns(CoverageTestEntity entity) {
        return doInsert(entity, Set.of());
    }

    /** 指定したカラムをINSERT 対象から外し、DB の既定値を使う */
    public int insertExcept(CoverageTestEntity entity, ColumnDefinition first, ColumnDefinition... rest) {
        var exclude = new LinkedHashSet<String>();
        validateColumns(first, rest, false).forEach(c -> exclude.add(c.getColumnName()));
        return doInsert(entity, exclude);
    }

    /** PK をINSERT 対象から外し、DB に値を決めさせる */
    public int insertExceptPk(CoverageTestEntity entity) {
        return insertExcept(entity, Columns.PK);
    }

    protected int doInsert(CoverageTestEntity entity, Set<String> excludeColumns) {
        var sql = new ArrayList<String>();
        sql.add("insert into \"coverage_test\"");
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

    public static Map<String, Object> entityToParam(CoverageTestEntity entity) {
        var param = new HashMap<String, Object>();
        param.put("pk", entity.getPk());
        param.put("colNullableDefault", entity.getColNullableDefault());
        param.put("colNotnullNodefault", entity.getColNotnullNodefault());
        param.put("colNowWithDefault", entity.getColNowWithDefault());
        param.put("updatedAt", entity.getUpdatedAt());
        param.put("colEnumDefault", entity.getColEnumDefault() == null ? null : entity.getColEnumDefault().name());
        param.put("colEnumNullableDefault", entity.getColEnumNullableDefault() == null ? null : entity.getColEnumNullableDefault().name());
        param.put("mappedNullableJavaName", entity.getMappedNullableJavaName());
        return param;
    }

    /** PK を除く全カラムを更新する */
    public int updateAllColumns(CoverageTestEntity entity) {
        return doUpdate(entity, Columns.MAP.values().stream().filter(c -> c.getPrimaryKeySeq() == null).toList());
    }

    /** 指定したカラムだけを更新する */
    public int updateInclude(CoverageTestEntity entity, ColumnDefinition first, ColumnDefinition... rest) {
        return doUpdate(entity, validateColumns(first, rest, true));
    }

    protected int doUpdate(CoverageTestEntity entity, List<ColumnDefinition> setColumns) {
        var sql = new ArrayList<String>();
        var param = entityToParam(entity);
        var setClause = setColumns.stream().map(BaseColumnDefinition::toUpdateSetClause).collect(joining(", "));
        sql.add("update \"coverage_test\"");
        sql.add("set %s".formatted(setClause));
        param.put("__pk1", entity.getPk());
        sql.add("where \"pk\" = :__pk1");
        return execWithReturning(sql, param, entity, Set.of("col_now_with_default", "updated_at"));
    }

    public Optional<CoverageTestEntity> findByPk(Long pk) {
        var __sql = new ArrayList<String>();
        __sql.add("select %s".formatted(Columns.selectAster()));
        __sql.add("from \"coverage_test\"");
        __sql.add("where \"pk\" = :pk");

        var __param = new HashMap<String, Object>();
        __param.put("pk", pk);

        return this.helper.optional(__sql, __param, ROW_MAPPER);
    }

    public int deleteByPk(Long pk) {
        var __sql = new ArrayList<String>();
        __sql.add("delete from \"coverage_test\"");
        __sql.add("where \"pk\" = :pk");

        var __param = new HashMap<String, Object>();
        __param.put("pk", pk);

        return this.helper.exec(__sql, __param);
    }

    @NullMarked
    public static class CoverageTestMapper extends BeanPropertyRowMapper<CoverageTestEntity> {
        public CoverageTestMapper() {
            super(CoverageTestEntity.class);
        }

        @Override
        protected String underscoreName(@Nullable String name) {
            if ("mappedNullableJavaName".equals(name)) {
                return "mapped_nullable";
            }
            return super.underscoreName(name);
        }
    }
}