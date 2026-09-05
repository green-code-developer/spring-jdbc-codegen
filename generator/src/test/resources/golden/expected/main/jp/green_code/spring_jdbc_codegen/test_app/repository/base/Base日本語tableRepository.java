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
import jp.green_code.spring_jdbc_codegen.test_app.entity.日本語tableEntity;
import jp.green_code.spring_jdbc_codegen.test_app.repository.ColumnDefinition;
import jp.green_code.spring_jdbc_codegen.test_app.repository.RepositoryHelper;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import static java.lang.String.join;
import static java.util.stream.Collectors.joining;

/**
 * Table: 日本語Table
 */
public abstract class Base日本語tableRepository {

    protected final RepositoryHelper helper;

    public static final 日本語tableMapper ROW_MAPPER = new 日本語tableMapper();

    public static class Columns {
        public static final ColumnDefinition ORDER = new ColumnDefinition("order", "order", "java.lang.Long", "bigserial", -5, 19, 1, false, true, null, null, false, false);
        public static final ColumnDefinition PARAM = new ColumnDefinition("param", "param", "java.lang.Long", "bigserial", -5, 19, 2, false, true, null, null, false, false);
        public static final ColumnDefinition SQL = new ColumnDefinition("sql", "sql", "java.lang.Long", "bigserial", -5, 19, 3, false, true, null, null, false, false);
        public static final ColumnDefinition HELPER = new ColumnDefinition("helper", "helper", "java.lang.Long", "bigserial", -5, 19, 4, false, true, null, null, false, false);
        public static final ColumnDefinition JOINING = new ColumnDefinition("joining", "joining", "java.lang.Long", "bigserial", -5, 19, 5, false, true, null, null, false, false);
        public static final ColumnDefinition LIST = new ColumnDefinition("List", "list", "java.lang.Long", "bigserial", -5, 19, 6, false, true, null, null, false, false);
        public static final ColumnDefinition RENAME = new ColumnDefinition("rename", "renamedJavaName", "java.lang.String", "text", 12, 2147483647, 7, false, false, null, null, false, true);
        public static final ColumnDefinition WHERE = new ColumnDefinition("where", "where", "java.time.LocalDateTime", "timestamp", 93, 29, null, true, false, null, null, false, false);
        public static final ColumnDefinition SELECT = new ColumnDefinition("select", "select", "java.lang.String", "text", 12, 2147483647, null, true, false, null, null, false, false);
        public static final ColumnDefinition ABC = new ColumnDefinition("Abc", "abc", "java.lang.String", "text", 12, 2147483647, null, true, false, null, null, false, false);

        public static final Map<String, ColumnDefinition> MAP = new LinkedHashMap<>();

        static {
            MAP.put("order", ORDER);
            MAP.put("param", PARAM);
            MAP.put("sql", SQL);
            MAP.put("helper", HELPER);
            MAP.put("joining", JOINING);
            MAP.put("List", LIST);
            MAP.put("rename", RENAME);
            MAP.put("where", WHERE);
            MAP.put("select", SELECT);
            MAP.put("Abc", ABC);
        }

        public static String selectAster() {
            return MAP.values().stream().map(ColumnDefinition::toSelectColumn).collect(joining(", "));
        }
    }

    public Base日本語tableRepository(RepositoryHelper helper) {
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
                throw new IllegalArgumentException("日本語Table のカラムではありません: " + c.getColumnName());
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

    protected void copyReturningValues(日本語tableEntity entity, 日本語tableEntity returning, Set<String> returningColumns) {
        if (returningColumns.contains("order")) {
            entity.setOrder(returning.getOrder());
        }
        if (returningColumns.contains("param")) {
            entity.setParam(returning.getParam());
        }
        if (returningColumns.contains("sql")) {
            entity.setSql(returning.getSql());
        }
        if (returningColumns.contains("helper")) {
            entity.setHelper(returning.getHelper());
        }
        if (returningColumns.contains("joining")) {
            entity.setJoining(returning.getJoining());
        }
        if (returningColumns.contains("List")) {
            entity.setList(returning.getList());
        }
        if (returningColumns.contains("rename")) {
            entity.setRenamedJavaName(returning.getRenamedJavaName());
        }
        if (returningColumns.contains("where")) {
            entity.setWhere(returning.getWhere());
        }
        if (returningColumns.contains("select")) {
            entity.setSelect(returning.getSelect());
        }
        if (returningColumns.contains("Abc")) {
            entity.setAbc(returning.getAbc());
        }
    }

    protected int execWithReturning(List<String> sql, Map<String, Object> param, 日本語tableEntity entity, Set<String> returningColumns) {
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
    public int insertAllColumns(日本語tableEntity entity) {
        return doInsert(entity, Set.of());
    }

    /** 指定したカラムをINSERT 対象から外し、DB の既定値を使う */
    public int insertExcept(日本語tableEntity entity, ColumnDefinition first, ColumnDefinition... rest) {
        var exclude = new LinkedHashSet<String>();
        validateColumns(first, rest, false).forEach(c -> exclude.add(c.getColumnName()));
        return doInsert(entity, exclude);
    }

    protected int doInsert(日本語tableEntity entity, Set<String> excludeColumns) {
        var sql = new ArrayList<String>();
        sql.add("insert into \"日本語Table\"");
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

    public static Map<String, Object> entityToParam(日本語tableEntity entity) {
        var param = new HashMap<String, Object>();
        param.put("order", entity.getOrder());
        param.put("param", entity.getParam());
        param.put("sql", entity.getSql());
        param.put("helper", entity.getHelper());
        param.put("joining", entity.getJoining());
        param.put("list", entity.getList());
        param.put("renamedJavaName", entity.getRenamedJavaName());
        param.put("where", entity.getWhere());
        param.put("select", entity.getSelect());
        param.put("abc", entity.getAbc());
        return param;
    }

    /** PK を除く全カラムを更新する */
    public int updateAllColumns(日本語tableEntity entity) {
        return doUpdate(entity, Columns.MAP.values().stream().filter(c -> c.getPrimaryKeySeq() == null).toList());
    }

    /** 指定したカラムだけを更新する */
    public int updateInclude(日本語tableEntity entity, ColumnDefinition first, ColumnDefinition... rest) {
        return doUpdate(entity, validateColumns(first, rest, true));
    }

    protected int doUpdate(日本語tableEntity entity, List<ColumnDefinition> setColumns) {
        var sql = new ArrayList<String>();
        var param = entityToParam(entity);
        var setClause = setColumns.stream().map(BaseColumnDefinition::toUpdateSetClause).collect(joining(", "));
        sql.add("update \"日本語Table\"");
        sql.add("set %s".formatted(setClause));
        param.put("__pk1", entity.getOrder());
        param.put("__pk2", entity.getParam());
        param.put("__pk3", entity.getSql());
        param.put("__pk4", entity.getHelper());
        param.put("__pk5", entity.getJoining());
        param.put("__pk6", entity.getList());
        param.put("__pk7", entity.getRenamedJavaName());
        sql.add("where \"order\" = :__pk1 AND \"param\" = :__pk2 AND \"sql\" = :__pk3 AND \"helper\" = :__pk4 AND \"joining\" = :__pk5 AND \"List\" = :__pk6 AND \"rename\" = :__pk7");
        return execWithReturning(sql, param, entity, Set.of());
    }

    public Optional<日本語tableEntity> findByPk(Long order, Long param, Long sql, Long helper, Long joining, Long list, String renamedJavaName) {
        var __sql = new ArrayList<String>();
        __sql.add("select %s".formatted(Columns.selectAster()));
        __sql.add("from \"日本語Table\"");
        __sql.add("where \"order\" = :order AND \"param\" = :param AND \"sql\" = :sql AND \"helper\" = :helper AND \"joining\" = :joining AND \"List\" = :list AND \"rename\" = :renamedJavaName");

        var __param = new HashMap<String, Object>();
        __param.put("order", order);
        __param.put("param", param);
        __param.put("sql", sql);
        __param.put("helper", helper);
        __param.put("joining", joining);
        __param.put("list", list);
        __param.put("renamedJavaName", renamedJavaName);

        return this.helper.optional(__sql, __param, ROW_MAPPER);
    }

    public int deleteByPk(Long order, Long param, Long sql, Long helper, Long joining, Long list, String renamedJavaName) {
        var __sql = new ArrayList<String>();
        __sql.add("delete from \"日本語Table\"");
        __sql.add("where \"order\" = :order AND \"param\" = :param AND \"sql\" = :sql AND \"helper\" = :helper AND \"joining\" = :joining AND \"List\" = :list AND \"rename\" = :renamedJavaName");

        var __param = new HashMap<String, Object>();
        __param.put("order", order);
        __param.put("param", param);
        __param.put("sql", sql);
        __param.put("helper", helper);
        __param.put("joining", joining);
        __param.put("list", list);
        __param.put("renamedJavaName", renamedJavaName);

        return this.helper.exec(__sql, __param);
    }

    @NullMarked
    public static class 日本語tableMapper extends BeanPropertyRowMapper<日本語tableEntity> {
        public 日本語tableMapper() {
            super(日本語tableEntity.class);
        }

        @Override
        protected String underscoreName(@Nullable String name) {
            if ("renamedJavaName".equals(name)) {
                return "rename";
            }
            return super.underscoreName(name);
        }
    }
}