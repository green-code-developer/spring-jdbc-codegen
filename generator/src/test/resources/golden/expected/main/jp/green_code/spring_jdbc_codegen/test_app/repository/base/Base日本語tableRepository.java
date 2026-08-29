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

    protected List<String> toInsertColumns(日本語tableEntity entity, boolean excludeNull) {
        var res = new ArrayList<String>();
        if (entity.getOrder() != null) {
            res.add("\"order\"");
        }
        if (entity.getParam() != null) {
            res.add("\"param\"");
        }
        if (entity.getSql() != null) {
            res.add("\"sql\"");
        }
        if (entity.getHelper() != null) {
            res.add("\"helper\"");
        }
        if (entity.getJoining() != null) {
            res.add("\"joining\"");
        }
        if (entity.getList() != null) {
            res.add("\"List\"");
        }
        if (!excludeNull || entity.getRenamedJavaName() != null) {
            res.add("\"rename\"");
        }
        if (!excludeNull || entity.getWhere() != null) {
            res.add("\"where\"");
        }
        if (!excludeNull || entity.getSelect() != null) {
            res.add("\"select\"");
        }
        if (!excludeNull || entity.getAbc() != null) {
            res.add("\"Abc\"");
        }
        return res;
    }

    protected Set<String> toInsertReturning(List<String> insertColumns) {
        var res = new HashSet<String>();
        if (insertColumns.isEmpty()) {
            res.add("order");
            res.add("param");
            res.add("sql");
            res.add("helper");
            res.add("joining");
            res.add("List");
            res.add("rename");
            res.add("where");
            res.add("select");
            res.add("Abc");
        } else {
            if (!insertColumns.contains("\"order\"")) {
                res.add("order");
            }
            if (!insertColumns.contains("\"param\"")) {
                res.add("param");
            }
            if (!insertColumns.contains("\"sql\"")) {
                res.add("sql");
            }
            if (!insertColumns.contains("\"helper\"")) {
                res.add("helper");
            }
            if (!insertColumns.contains("\"joining\"")) {
                res.add("joining");
            }
            if (!insertColumns.contains("\"List\"")) {
                res.add("List");
            }
            if (!insertColumns.contains("\"rename\"")) {
                res.add("rename");
            }
            if (!insertColumns.contains("\"where\"")) {
                res.add("where");
            }
            if (!insertColumns.contains("\"select\"")) {
                res.add("select");
            }
            if (!insertColumns.contains("\"Abc\"")) {
                res.add("Abc");
            }
        }
        return res;
    }

    protected List<String> toInsertValues(日本語tableEntity entity, boolean excludeNull) {
        var res = new ArrayList<String>();
        if (entity.getOrder() != null) {
            res.add("order");
        }
        if (entity.getParam() != null) {
            res.add("param");
        }
        if (entity.getSql() != null) {
            res.add("sql");
        }
        if (entity.getHelper() != null) {
            res.add("helper");
        }
        if (entity.getJoining() != null) {
            res.add("joining");
        }
        if (entity.getList() != null) {
            res.add("List");
        }
        if (!excludeNull || entity.getRenamedJavaName() != null) {
            res.add("rename");
        }
        if (!excludeNull || entity.getWhere() != null) {
            res.add("where");
        }
        if (!excludeNull || entity.getSelect() != null) {
            res.add("select");
        }
        if (!excludeNull || entity.getAbc() != null) {
            res.add("Abc");
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

    public int insert(日本語tableEntity entity) {
        return doInsert(entity, false);
    }

    /** 値がnull のカラムをINSERT 対象から外し、DB の既定値を使う */
    public int insertNotNull(日本語tableEntity entity) {
        return doInsert(entity, true);
    }

    protected int doInsert(日本語tableEntity entity, boolean excludeNull) {
        var __sql = new ArrayList<String>();
        __sql.add("insert into \"日本語Table\"");
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

    public int update(日本語tableEntity entity) {
        return doUpdateByPk(entity, false, entity.getOrder(), entity.getParam(), entity.getSql(), entity.getHelper(), entity.getJoining(), entity.getList(), entity.getRenamedJavaName());
    }

    /** 値がnull のカラムをset 句から外して部分更新する */
    public int updateNotNull(日本語tableEntity entity) {
        return doUpdateByPk(entity, true, entity.getOrder(), entity.getParam(), entity.getSql(), entity.getHelper(), entity.getJoining(), entity.getList(), entity.getRenamedJavaName());
    }


    public int updateByPk(日本語tableEntity entity, Long order, Long param, Long sql, Long helper, Long joining, Long list, String renamedJavaName) {
        return doUpdateByPk(entity, false, order, param, sql, helper, joining, list, renamedJavaName);
    }

    protected int doUpdateByPk(日本語tableEntity entity, boolean excludeNull, Long order, Long param, Long sql, Long helper, Long joining, Long list, String renamedJavaName) {
        var __sql = new ArrayList<String>();
        var __param = entityToParam(entity);
        var setClause = Columns.MAP.values().stream()
                .filter(c -> !excludeNull || __param.get(c.getJavaPropertyName()) != null)
                .map(BaseColumnDefinition::toUpdateSetClause).collect(joining(", "));
        if (setClause.isEmpty()) {
            throw new IllegalArgumentException("更新対象のカラムがありません");
        }
        __sql.add("update \"日本語Table\"");
        __sql.add("set %s".formatted(setClause));
        __param.put("__pk1", order);
        __param.put("__pk2", param);
        __param.put("__pk3", sql);
        __param.put("__pk4", helper);
        __param.put("__pk5", joining);
        __param.put("__pk6", list);
        __param.put("__pk7", renamedJavaName);
        __sql.add("where \"order\" = :__pk1 AND \"param\" = :__pk2 AND \"sql\" = :__pk3 AND \"helper\" = :__pk4 AND \"joining\" = :__pk5 AND \"List\" = :__pk6 AND \"rename\" = :__pk7");
        return execWithReturning(__sql, __param, entity, Set.of());
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