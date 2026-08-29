package jp.green_code.spring_jdbc_codegen.test_app.repository.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import jp.green_code.spring_jdbc_codegen.test_app.entity.MapperNoPkEntity;
import jp.green_code.spring_jdbc_codegen.test_app.repository.ColumnDefinition;
import jp.green_code.spring_jdbc_codegen.test_app.repository.RepositoryHelper;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import static java.lang.String.join;
import static java.util.stream.Collectors.joining;

/**
 * Table: mapper_no_pk
 */
public abstract class BaseMapperNoPkRepository {

    protected final RepositoryHelper helper;

    public static final MapperNoPkMapper ROW_MAPPER = new MapperNoPkMapper();

    public static class Columns {
        public static final ColumnDefinition RENAME_TARGET = new ColumnDefinition("rename_target", "renamedNoPkName", "java.lang.String", "text", 12, 2147483647, null, true, false, null, null, false, true);
        public static final ColumnDefinition OTHER_COL = new ColumnDefinition("other_col", "otherCol", "java.lang.String", "text", 12, 2147483647, null, true, false, null, null, false, false);

        public static final Map<String, ColumnDefinition> MAP = new LinkedHashMap<>();

        static {
            MAP.put("rename_target", RENAME_TARGET);
            MAP.put("other_col", OTHER_COL);
        }

        public static String selectAster() {
            return MAP.values().stream().map(ColumnDefinition::toSelectColumn).collect(joining(", "));
        }
    }

    public BaseMapperNoPkRepository(RepositoryHelper helper) {
        this.helper = helper;
    }

    protected List<String> toInsertColumns(MapperNoPkEntity entity, boolean excludeNull) {
        var res = new ArrayList<String>();
        if (!excludeNull || entity.getRenamedNoPkName() != null) {
            res.add("\"rename_target\"");
        }
        if (!excludeNull || entity.getOtherCol() != null) {
            res.add("\"other_col\"");
        }
        return res;
    }

    protected Set<String> toInsertReturning(List<String> insertColumns) {
        var res = new HashSet<String>();
        if (insertColumns.isEmpty()) {
            res.add("rename_target");
            res.add("other_col");
        } else {
            if (!insertColumns.contains("\"rename_target\"")) {
                res.add("rename_target");
            }
            if (!insertColumns.contains("\"other_col\"")) {
                res.add("other_col");
            }
        }
        return res;
    }

    protected List<String> toInsertValues(MapperNoPkEntity entity, boolean excludeNull) {
        var res = new ArrayList<String>();
        if (!excludeNull || entity.getRenamedNoPkName() != null) {
            res.add("rename_target");
        }
        if (!excludeNull || entity.getOtherCol() != null) {
            res.add("other_col");
        }
        return res;
    }

    protected void copyReturningValues(MapperNoPkEntity entity, MapperNoPkEntity returning, Set<String> returningColumns) {
        if (returningColumns.contains("rename_target")) {
            entity.setRenamedNoPkName(returning.getRenamedNoPkName());
        }
        if (returningColumns.contains("other_col")) {
            entity.setOtherCol(returning.getOtherCol());
        }
    }

    protected int execWithReturning(List<String> sql, Map<String, Object> param, MapperNoPkEntity entity, Set<String> returningColumns) {
        if (returningColumns.isEmpty()) {
            return this.helper.exec(sql, param);
        }
        var returningClause = returningColumns.stream().map(c -> Objects.requireNonNull(Columns.MAP.get(c), "Unknown column " + c).toSelectColumn()).collect(joining(", "));
        sql.add("returning %s".formatted(returningClause));
        var ret = this.helper.optional(sql, param, ROW_MAPPER);
        ret.ifPresent(r -> copyReturningValues(entity, r, returningColumns));
        return ret.isPresent() ? 1 : 0;
    }

    public int insert(MapperNoPkEntity entity) {
        return doInsert(entity, false);
    }

    /** 値がnull のカラムをINSERT 対象から外し、DB の既定値を使う */
    public int insertNotNull(MapperNoPkEntity entity) {
        return doInsert(entity, true);
    }

    protected int doInsert(MapperNoPkEntity entity, boolean excludeNull) {
        var __sql = new ArrayList<String>();
        __sql.add("insert into \"mapper_no_pk\"");
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

    public static Map<String, Object> entityToParam(MapperNoPkEntity entity) {
        var param = new HashMap<String, Object>();
        param.put("renamedNoPkName", entity.getRenamedNoPkName());
        param.put("otherCol", entity.getOtherCol());
        return param;
    }

    @NullMarked
    public static class MapperNoPkMapper extends BeanPropertyRowMapper<MapperNoPkEntity> {
        public MapperNoPkMapper() {
            super(MapperNoPkEntity.class);
        }

        @Override
        protected String underscoreName(@Nullable String name) {
            if ("renamedNoPkName".equals(name)) {
                return "rename_target";
            }
            return super.underscoreName(name);
        }
    }
}