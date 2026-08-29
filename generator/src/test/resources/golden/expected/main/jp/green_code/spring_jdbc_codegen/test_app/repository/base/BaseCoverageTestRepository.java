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

    protected List<String> toInsertColumns(CoverageTestEntity entity, boolean excludeNull) {
        var res = new ArrayList<String>();
        if (entity.getPk() != null) {
            res.add("\"pk\"");
        }
        if (!excludeNull || entity.getColNullableDefault() != null) {
            res.add("\"col_nullable_default\"");
        }
        if (!excludeNull || entity.getColNotnullNodefault() != null) {
            res.add("\"col_notnull_nodefault\"");
        }
        if (entity.getColNowWithDefault() != null) {
            res.add("\"col_now_with_default\"");
        }
        if (!excludeNull || entity.getUpdatedAt() != null) {
            res.add("\"updated_at\"");
        }
        if (entity.getColEnumDefault() != null) {
            res.add("\"col_enum_default\"");
        }
        if (!excludeNull || entity.getColEnumNullableDefault() != null) {
            res.add("\"col_enum_nullable_default\"");
        }
        if (!excludeNull || entity.getMappedNullableJavaName() != null) {
            res.add("\"mapped_nullable\"");
        }
        return res;
    }

    protected Set<String> toInsertReturning(List<String> insertColumns) {
        var res = new HashSet<String>();
        if (insertColumns.isEmpty()) {
            res.add("pk");
            res.add("col_nullable_default");
            res.add("col_notnull_nodefault");
            res.add("col_now_with_default");
            res.add("updated_at");
            res.add("col_enum_default");
            res.add("col_enum_nullable_default");
            res.add("mapped_nullable");
        } else {
            if (!insertColumns.contains("\"pk\"")) {
                res.add("pk");
            }
            if (!insertColumns.contains("\"col_nullable_default\"")) {
                res.add("col_nullable_default");
            }
            if (!insertColumns.contains("\"col_notnull_nodefault\"")) {
                res.add("col_notnull_nodefault");
            }
            res.add("col_now_with_default");
            res.add("updated_at");
            if (!insertColumns.contains("\"col_enum_default\"")) {
                res.add("col_enum_default");
            }
            if (!insertColumns.contains("\"col_enum_nullable_default\"")) {
                res.add("col_enum_nullable_default");
            }
            if (!insertColumns.contains("\"mapped_nullable\"")) {
                res.add("mapped_nullable");
            }
        }
        return res;
    }

    protected List<String> toInsertValues(CoverageTestEntity entity, boolean excludeNull) {
        var res = new ArrayList<String>();
        if (entity.getPk() != null) {
            res.add("pk");
        }
        if (!excludeNull || entity.getColNullableDefault() != null) {
            res.add("col_nullable_default");
        }
        if (!excludeNull || entity.getColNotnullNodefault() != null) {
            res.add("col_notnull_nodefault");
        }
        if (entity.getColNowWithDefault() != null) {
            res.add("col_now_with_default");
        }
        if (!excludeNull || entity.getUpdatedAt() != null) {
            res.add("updated_at");
        }
        if (entity.getColEnumDefault() != null) {
            res.add("col_enum_default");
        }
        if (!excludeNull || entity.getColEnumNullableDefault() != null) {
            res.add("col_enum_nullable_default");
        }
        if (!excludeNull || entity.getMappedNullableJavaName() != null) {
            res.add("mapped_nullable");
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

    public int insert(CoverageTestEntity entity) {
        return doInsert(entity, false);
    }

    /** 値がnull のカラムをINSERT 対象から外し、DB の既定値を使う */
    public int insertNotNull(CoverageTestEntity entity) {
        return doInsert(entity, true);
    }

    protected int doInsert(CoverageTestEntity entity, boolean excludeNull) {
        var __sql = new ArrayList<String>();
        __sql.add("insert into \"coverage_test\"");
        var __insertColumns = toInsertColumns(entity, excludeNull);
        if (__insertColumns.isEmpty()) {
            __sql.add("DEFAULT VALUES");
        } else {
            __sql.add("(%s)".formatted(join(", ", __insertColumns)));
            var __insertValues = toInsertValues(entity, excludeNull);
            var __valuesClause = __insertValues.stream().map(c -> Columns.MAP.get(c) == null ? c : Columns.MAP.get(c).toParamColumn()).collect(joining(", "));
            __sql.add("values (%s)".formatted(__valuesClause));
        }
        var __param = entityToParam(entity);
        return execWithReturning(__sql, __param, entity, toInsertReturning(__insertColumns));
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

    public int update(CoverageTestEntity entity) {
        return doUpdateByPk(entity, false, entity.getPk());
    }

    /** 値がnull のカラムをset 句から外して部分更新する */
    public int updateNotNull(CoverageTestEntity entity) {
        return doUpdateByPk(entity, true, entity.getPk());
    }


    public int updateByPk(CoverageTestEntity entity, Long pk) {
        return doUpdateByPk(entity, false, pk);
    }

    protected int doUpdateByPk(CoverageTestEntity entity, boolean excludeNull, Long pk) {
        var __sql = new ArrayList<String>();
        var __param = entityToParam(entity);
        var setClause = Columns.MAP.values().stream()
                .filter(c -> !excludeNull || __param.get(c.getJavaPropertyName()) != null)
                .map(BaseColumnDefinition::toUpdateSetClause).collect(joining(", "));
        if (setClause.isEmpty()) {
            throw new IllegalArgumentException("更新対象のカラムがありません");
        }
        __sql.add("update \"coverage_test\"");
        __sql.add("set %s".formatted(setClause));
        __param.put("__pk1", pk);
        __sql.add("where \"pk\" = :__pk1");
        return execWithReturning(__sql, __param, entity, Set.of("col_now_with_default", "updated_at"));
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