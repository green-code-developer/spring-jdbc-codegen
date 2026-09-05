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
                throw new IllegalArgumentException("mapper_no_pk のカラムではありません: " + c.getColumnName());
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

    /** 全カラムをINSERT 対象とする */
    public int insertAllColumns(MapperNoPkEntity entity) {
        return doInsert(entity, Set.of());
    }

    /** 指定したカラムをINSERT 対象から外し、DB の既定値を使う */
    public int insertExcept(MapperNoPkEntity entity, ColumnDefinition first, ColumnDefinition... rest) {
        var exclude = new LinkedHashSet<String>();
        validateColumns(first, rest, false).forEach(c -> exclude.add(c.getColumnName()));
        return doInsert(entity, exclude);
    }

    protected int doInsert(MapperNoPkEntity entity, Set<String> excludeColumns) {
        var sql = new ArrayList<String>();
        sql.add("insert into \"mapper_no_pk\"");
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