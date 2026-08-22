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
import org.springframework.dao.EmptyResultDataAccessException;
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
        public static final ColumnDefinition ORDER = new ColumnDefinition("order", "order", "java.lang.Long", "bigserial", -5, 19, 1, false, true, null, null, false, false, false);
        public static final ColumnDefinition PARAM = new ColumnDefinition("param", "param", "java.lang.Long", "bigserial", -5, 19, 2, false, true, null, null, false, false, false);
        public static final ColumnDefinition SQL = new ColumnDefinition("sql", "sql", "java.lang.Long", "bigserial", -5, 19, 3, false, true, null, null, false, false, false);
        public static final ColumnDefinition HELPER = new ColumnDefinition("helper", "helper", "java.lang.Long", "bigserial", -5, 19, 4, false, true, null, null, false, false, false);
        public static final ColumnDefinition JOINING = new ColumnDefinition("joining", "joining", "java.lang.Long", "bigserial", -5, 19, 5, false, true, null, null, false, false, false);
        public static final ColumnDefinition LIST = new ColumnDefinition("List", "list", "java.lang.Long", "bigserial", -5, 19, 6, false, true, null, null, false, false, false);
        public static final ColumnDefinition RENAME = new ColumnDefinition("rename", "renamedJavaName", "java.lang.String", "text", 12, 2147483647, 7, false, false, null, null, false, false, true);
        public static final ColumnDefinition WHERE = new ColumnDefinition("where", "where", "java.time.LocalDateTime", "timestamp", 93, 29, null, true, false, null, null, false, false, false);
        public static final ColumnDefinition SELECT = new ColumnDefinition("select", "select", "java.lang.String", "text", 12, 2147483647, null, true, false, null, null, false, false, false);
        public static final ColumnDefinition ABC = new ColumnDefinition("Abc", "abc", "java.lang.String", "text", 12, 2147483647, null, true, false, null, null, false, false, false);

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

    protected List<String> toInsertColumns(日本語tableEntity entity) {
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
        res.add("\"rename\"");
        res.add("\"where\"");
        res.add("\"select\"");
        res.add("\"Abc\"");
        return res;
    }

    protected Set<String> toInsertReturning(日本語tableEntity entity, List<String> insertColumns) {
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
            if (entity.getOrder() == null) {
                res.add("order");
            }
            if (entity.getParam() == null) {
                res.add("param");
            }
            if (entity.getSql() == null) {
                res.add("sql");
            }
            if (entity.getHelper() == null) {
                res.add("helper");
            }
            if (entity.getJoining() == null) {
                res.add("joining");
            }
            if (entity.getList() == null) {
                res.add("List");
            }
        }
        return res;
    }

    protected List<String> toInsertValues(日本語tableEntity entity) {
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
        res.add("rename");
        res.add("where");
        res.add("select");
        res.add("Abc");
        return res;
    }

    protected void copyReturningValuesInInsert(日本語tableEntity entity, 日本語tableEntity returning) {
        if (entity.getOrder() == null) {
            entity.setOrder(returning.getOrder());
        }
        if (entity.getParam() == null) {
            entity.setParam(returning.getParam());
        }
        if (entity.getSql() == null) {
            entity.setSql(returning.getSql());
        }
        if (entity.getHelper() == null) {
            entity.setHelper(returning.getHelper());
        }
        if (entity.getJoining() == null) {
            entity.setJoining(returning.getJoining());
        }
        if (entity.getList() == null) {
            entity.setList(returning.getList());
        }
    }

    public 日本語tableEntity insert(日本語tableEntity entity) {
        var sql = new ArrayList<String>();
        sql.add("insert into \"日本語Table\"");
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

    public 日本語tableEntity update(日本語tableEntity entity) {
        return updateByPk(entity, entity.getOrder(), entity.getParam(), entity.getSql(), entity.getHelper(), entity.getJoining(), entity.getList(), entity.getRenamedJavaName());
    }


    public 日本語tableEntity updateByPk(日本語tableEntity entity, Long order, Long param, Long sql, Long helper, Long joining, Long list, String renamedJavaName) {
        var __sql = new ArrayList<String>();
        var setClause = Columns.MAP.values().stream().filter(c-> !c.isShouldSkipInUpdate()).map(BaseColumnDefinition::toUpdateSetClause).collect(joining(", "));
        __sql.add("update \"日本語Table\"");
        __sql.add("set %s".formatted(setClause));
        var __param = entityToParam(entity);
        __param.put("__pk1", order);
        __param.put("__pk2", param);
        __param.put("__pk3", sql);
        __param.put("__pk4", helper);
        __param.put("__pk5", joining);
        __param.put("__pk6", list);
        __param.put("__pk7", renamedJavaName);
        __sql.add("where \"order\" = :__pk1 AND \"param\" = :__pk2 AND \"sql\" = :__pk3 AND \"helper\" = :__pk4 AND \"joining\" = :__pk5 AND \"List\" = :__pk6 AND \"rename\" = :__pk7");
        var res = this.helper.exec(__sql, __param);
        if (res != 1) {
            throw new EmptyResultDataAccessException(1);
        }
        return entity;
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