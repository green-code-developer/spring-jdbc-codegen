package jp.green_code.spring_jdbc_codegen.test_app.repository.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import jp.green_code.spring_jdbc_codegen.test_app.entity.NowPk0Entity;
import jp.green_code.spring_jdbc_codegen.test_app.repository.ColumnDefinition;
import jp.green_code.spring_jdbc_codegen.test_app.repository.RepositoryHelper;
import org.springframework.dao.EmptyResultDataAccessException;
import static java.lang.String.join;
import static java.util.stream.Collectors.joining;

/**
 * Table: now_pk0
 */
public abstract class BaseNowPk0Repository {

    protected final RepositoryHelper helper;

    public static class Columns {
        public static final ColumnDefinition COL_NOW = new ColumnDefinition("col_now", "colNow", "java.time.OffsetDateTime", "timestamptz", 93, 35, null, false, true, null, null, false, false);

        public static final Map<String, ColumnDefinition> MAP = new LinkedHashMap<>();

        static {
            MAP.put("col_now", COL_NOW);
        }

        public static String selectAster() {
            return MAP.values().stream().map(ColumnDefinition::toSelectColumn).collect(joining(", "));
        }
    }

    public BaseNowPk0Repository(RepositoryHelper helper) {
        this.helper = helper;
    }

    protected List<String> toInsertColumns(NowPk0Entity entity) {
        var res = new ArrayList<String>();
        if (entity.getColNow() != null) {
            res.add("\"col_now\"");
        }
        return res;
    }

    protected Set<String> toInsertReturning(NowPk0Entity entity, List<String> insertColumns) {
        var res = new HashSet<String>();
        if (insertColumns.isEmpty()) {
            res.add("col_now");
        } else {
            if (entity.getColNow() == null) {
                res.add("col_now");
            }
        }
        return res;
    }

    protected List<String> toInsertValues(NowPk0Entity entity) {
        var res = new ArrayList<String>();
        if (entity.getColNow() != null) {
            res.add("col_now");
        }
        return res;
    }

    protected void copyReturningValuesInInsert(NowPk0Entity entity, NowPk0Entity returning) {
        if (entity.getColNow() == null) {
            entity.setColNow(returning.getColNow());
        }
    }

    public NowPk0Entity insert(NowPk0Entity entity) {
        var sql = new ArrayList<String>();
        sql.add("insert into \"now_pk0\"");
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
            var ret = this.helper.single(sql, param, NowPk0Entity.class);
            copyReturningValuesInInsert(entity, ret);
        }
        return entity;
    }

    public static Map<String, Object> entityToParam(NowPk0Entity entity) {
        var param = new HashMap<String, Object>();
        param.put("colNow", entity.getColNow());
        return param;
    }
}