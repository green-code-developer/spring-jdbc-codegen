package jp.green_code.spring_jdbc_codegen.test_app.repository.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
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
        public static final ColumnDefinition PK = new ColumnDefinition("pk", "pk", "java.lang.Long", "bigserial", -5, 19, 1, false, true, null, null, false, false, false);
        public static final ColumnDefinition COL_NULLABLE_DEFAULT = new ColumnDefinition("col_nullable_default", "colNullableDefault", "java.lang.String", "text", 12, 2147483647, null, true, true, null, null, false, false, false);
        public static final ColumnDefinition COL_NOTNULL_NODEFAULT = new ColumnDefinition("col_notnull_nodefault", "colNotnullNodefault", "java.lang.String", "text", 12, 2147483647, null, false, false, null, null, false, false, false);
        public static final ColumnDefinition COL_NOW_WITH_DEFAULT = new ColumnDefinition("col_now_with_default", "colNowWithDefault", "java.time.OffsetDateTime", "timestamptz", 93, 35, null, false, true, null, null, true, false, false);
        public static final ColumnDefinition CREATED_AT = new ColumnDefinition("created_at", "createdAt", "java.time.OffsetDateTime", "timestamptz", 93, 35, null, false, true, null, null, true, true, false);
        public static final ColumnDefinition CREATED_BY = new ColumnDefinition("created_by", "createdBy", "java.lang.String", "text", 12, 2147483647, null, false, false, null, null, false, true, false);
        public static final ColumnDefinition UPDATED_AT = new ColumnDefinition("updated_at", "updatedAt", "java.time.OffsetDateTime", "timestamptz", 93, 35, null, true, false, null, null, true, false, false);
        public static final ColumnDefinition COL_NO_UPDATE_NULLABLE = new ColumnDefinition("col_no_update_nullable", "colNoUpdateNullable", "java.lang.String", "text", 12, 2147483647, null, true, false, null, null, false, true, false);
        public static final ColumnDefinition COL_ENUM_DEFAULT = new ColumnDefinition("col_enum_default", "colEnumDefault", "jp.green_code.spring_jdbc_codegen.test_app.StatusEnum", "status_enum", 12, 2147483647, null, false, true, ":{javaPropertyName}::status_enum", null, false, false, false);
        public static final ColumnDefinition COL_ENUM_NULLABLE_DEFAULT = new ColumnDefinition("col_enum_nullable_default", "colEnumNullableDefault", "jp.green_code.spring_jdbc_codegen.test_app.StatusEnum", "status_enum", 12, 2147483647, null, true, true, ":{javaPropertyName}::status_enum", null, false, false, false);
        public static final ColumnDefinition MAPPED_NULLABLE = new ColumnDefinition("mapped_nullable", "mappedNullableJavaName", "java.lang.String", "text", 12, 2147483647, null, true, false, null, null, false, false, true);

        public static final Map<String, ColumnDefinition> MAP = new LinkedHashMap<>();

        static {
            MAP.put("pk", PK);
            MAP.put("col_nullable_default", COL_NULLABLE_DEFAULT);
            MAP.put("col_notnull_nodefault", COL_NOTNULL_NODEFAULT);
            MAP.put("col_now_with_default", COL_NOW_WITH_DEFAULT);
            MAP.put("created_at", CREATED_AT);
            MAP.put("created_by", CREATED_BY);
            MAP.put("updated_at", UPDATED_AT);
            MAP.put("col_no_update_nullable", COL_NO_UPDATE_NULLABLE);
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

    protected List<String> toInsertColumns(CoverageTestEntity entity) {
        var res = new ArrayList<String>();
        if (entity.getPk() != null) {
            res.add("\"pk\"");
        }
        res.add("\"col_nullable_default\"");
        res.add("\"col_notnull_nodefault\"");
        res.add("\"col_now_with_default\"");
        res.add("\"created_at\"");
        res.add("\"created_by\"");
        res.add("\"updated_at\"");
        res.add("\"col_no_update_nullable\"");
        if (entity.getColEnumDefault() != null) {
            res.add("\"col_enum_default\"");
        }
        res.add("\"col_enum_nullable_default\"");
        res.add("\"mapped_nullable\"");
        return res;
    }

    protected Set<String> toInsertReturning(CoverageTestEntity entity, List<String> insertColumns) {
        var res = new HashSet<String>();
        if (insertColumns.isEmpty()) {
            res.add("pk");
            res.add("col_nullable_default");
            res.add("col_notnull_nodefault");
            res.add("col_now_with_default");
            res.add("created_at");
            res.add("created_by");
            res.add("updated_at");
            res.add("col_no_update_nullable");
            res.add("col_enum_default");
            res.add("col_enum_nullable_default");
            res.add("mapped_nullable");
        } else {
            if (entity.getPk() == null) {
                res.add("pk");
            }
            res.add("col_now_with_default");
            res.add("created_at");
            res.add("updated_at");
            if (entity.getColEnumDefault() == null) {
                res.add("col_enum_default");
            }
        }
        return res;
    }

    protected List<String> toInsertValues(CoverageTestEntity entity) {
        var res = new ArrayList<String>();
        if (entity.getPk() != null) {
            res.add("pk");
        }
        res.add("col_nullable_default");
        res.add("col_notnull_nodefault");
        res.add("now()");
        res.add("now()");
        res.add("created_by");
        res.add("now()");
        res.add("col_no_update_nullable");
        if (entity.getColEnumDefault() != null) {
            res.add("col_enum_default");
        }
        res.add("col_enum_nullable_default");
        res.add("mapped_nullable");
        return res;
    }

    protected void copyReturningValuesInInsert(CoverageTestEntity entity, CoverageTestEntity returning) {
        if (entity.getPk() == null) {
            entity.setPk(returning.getPk());
        }
        entity.setColNowWithDefault(returning.getColNowWithDefault());
        entity.setCreatedAt(returning.getCreatedAt());
        entity.setUpdatedAt(returning.getUpdatedAt());
        if (entity.getColEnumDefault() == null) {
            entity.setColEnumDefault(returning.getColEnumDefault());
        }
    }

    public CoverageTestEntity insert(CoverageTestEntity entity) {
        var sql = new ArrayList<String>();
        sql.add("insert into \"coverage_test\"");
        var insertColumns = toInsertColumns(entity);
        if (insertColumns.isEmpty()) {
            sql.add("DEFAULT VALUES");
        } else {
            sql.add("(%s)".formatted(join(", ", insertColumns)));
            var insertValues = toInsertValues(entity);
            var insertValuesClause = insertValues.stream().map(c -> Columns.MAP.get(c) == null ? c : Columns.MAP.get(c).toParamColumn()).collect(joining(", "));
            sql.add("values (%s)".formatted(insertValuesClause));
        }
        var param = entityToParam(entity);
        var returningColumns = toInsertReturning(entity, insertColumns);
        if (returningColumns.isEmpty()) {
            this.helper.exec(sql, param);
        } else {
            var returningClause = returningColumns.stream().map(c -> Objects.requireNonNull(Columns.MAP.get(c), "Unknown column " + c).toSelectColumn()).collect(joining(", "));
            sql.add("returning %s".formatted(returningClause));
            var ret = this.helper.single(sql, param, ROW_MAPPER);
            copyReturningValuesInInsert(entity, ret);
        }
        return entity;
    }

    public static Map<String, Object> entityToParam(CoverageTestEntity entity) {
        var param = new HashMap<String, Object>();
        param.put("pk", entity.getPk());
        param.put("colNullableDefault", entity.getColNullableDefault());
        param.put("colNotnullNodefault", entity.getColNotnullNodefault());
        param.put("colNowWithDefault", entity.getColNowWithDefault());
        param.put("createdAt", entity.getCreatedAt());
        param.put("createdBy", entity.getCreatedBy());
        param.put("updatedAt", entity.getUpdatedAt());
        param.put("colNoUpdateNullable", entity.getColNoUpdateNullable());
        param.put("colEnumDefault", String.valueOf(entity.getColEnumDefault()));
        param.put("colEnumNullableDefault", String.valueOf(entity.getColEnumNullableDefault()));
        param.put("mappedNullableJavaName", entity.getMappedNullableJavaName());
        return param;
    }

    public CoverageTestEntity update(CoverageTestEntity entity) {
        return updateByPk(entity, entity.getPk());
    }

    protected void copyReturningValuesInUpdate(CoverageTestEntity entity, CoverageTestEntity returning) {
        entity.setColNowWithDefault(returning.getColNowWithDefault());
        entity.setUpdatedAt(returning.getUpdatedAt());
    }

    public CoverageTestEntity updateByPk(CoverageTestEntity entity, Long pk) {
        var __sql = new ArrayList<String>();
        var setClause = Columns.MAP.values().stream().filter(c-> !c.isShouldSkipInUpdate()).map(BaseColumnDefinition::toUpdateSetClause).collect(joining(", "));
        __sql.add("update \"coverage_test\"");
        __sql.add("set %s".formatted(setClause));
        var __param = entityToParam(entity);
        __param.put("__pk1", pk);
        __sql.add("where \"pk\" = :__pk1");
        var __returning = List.of("col_now_with_default", "updated_at").stream().map(c -> Objects.requireNonNull(Columns.MAP.get(c), "Unknown column " + c).toSelectColumn()).collect(joining(", "));
        __sql.add("returning %s".formatted(__returning));
        var ret = this.helper.single(__sql, __param, ROW_MAPPER);
        copyReturningValuesInUpdate(entity, ret);
        return entity;
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