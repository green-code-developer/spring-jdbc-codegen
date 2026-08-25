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
import org.springframework.dao.EmptyResultDataAccessException;
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
        public static final ColumnDefinition RENAME_TARGET = new ColumnDefinition("rename_target", "renamedNoPkName", "java.lang.String", "text", 12, 2147483647, null, true, false, null, null, false, false, true);
        public static final ColumnDefinition OTHER_COL = new ColumnDefinition("other_col", "otherCol", "java.lang.String", "text", 12, 2147483647, null, true, false, null, null, false, false, false);

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

    protected List<String> toInsertColumns(MapperNoPkEntity entity) {
        var res = new ArrayList<String>();
        res.add("\"rename_target\"");
        res.add("\"other_col\"");
        return res;
    }

    protected Set<String> toInsertReturning(MapperNoPkEntity entity, List<String> insertColumns) {
        var res = new HashSet<String>();
        if (insertColumns.isEmpty()) {
            res.add("rename_target");
            res.add("other_col");
        }
        return res;
    }

    protected List<String> toInsertValues(MapperNoPkEntity entity) {
        var res = new ArrayList<String>();
        res.add("rename_target");
        res.add("other_col");
        return res;
    }

    public MapperNoPkEntity insert(MapperNoPkEntity entity) {
        var sql = new ArrayList<String>();
        sql.add("insert into \"mapper_no_pk\"");
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
            this.helper.single(sql, param, ROW_MAPPER);
        }
        return entity;
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