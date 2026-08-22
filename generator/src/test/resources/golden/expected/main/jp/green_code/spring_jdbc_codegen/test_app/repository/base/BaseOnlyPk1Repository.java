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
import jp.green_code.spring_jdbc_codegen.test_app.entity.OnlyPk1Entity;
import jp.green_code.spring_jdbc_codegen.test_app.repository.ColumnDefinition;
import jp.green_code.spring_jdbc_codegen.test_app.repository.RepositoryHelper;
import org.springframework.dao.EmptyResultDataAccessException;
import static java.lang.String.join;
import static java.util.stream.Collectors.joining;

/**
 * Table: only_pk1
 */
public abstract class BaseOnlyPk1Repository {

    protected final RepositoryHelper helper;

    public static class Columns {
        public static final ColumnDefinition PK = new ColumnDefinition("pk", "pk", "java.lang.Long", "bigserial", -5, 19, 1, false, true, null, null, false, false, false);

        public static final Map<String, ColumnDefinition> MAP = new LinkedHashMap<>();

        static {
            MAP.put("pk", PK);
        }

        public static String selectAster() {
            return MAP.values().stream().map(ColumnDefinition::toSelectColumn).collect(joining(", "));
        }
    }

    public BaseOnlyPk1Repository(RepositoryHelper helper) {
        this.helper = helper;
    }

    protected List<String> toInsertColumns(OnlyPk1Entity entity) {
        var res = new ArrayList<String>();
        if (entity.getPk() != null) {
            res.add("\"pk\"");
        }
        return res;
    }

    protected Set<String> toInsertReturning(OnlyPk1Entity entity, List<String> insertColumns) {
        var res = new HashSet<String>();
        if (insertColumns.isEmpty()) {
            res.add("pk");
        } else {
            if (entity.getPk() == null) {
                res.add("pk");
            }
        }
        return res;
    }

    protected List<String> toInsertValues(OnlyPk1Entity entity) {
        var res = new ArrayList<String>();
        if (entity.getPk() != null) {
            res.add("pk");
        }
        return res;
    }

    protected void copyReturningValuesInInsert(OnlyPk1Entity entity, OnlyPk1Entity returning) {
        if (entity.getPk() == null) {
            entity.setPk(returning.getPk());
        }
    }

    public OnlyPk1Entity insert(OnlyPk1Entity entity) {
        var sql = new ArrayList<String>();
        sql.add("insert into \"only_pk1\"");
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
            var ret = this.helper.single(sql, param, OnlyPk1Entity.class);
            copyReturningValuesInInsert(entity, ret);
        }
        return entity;
    }

    public static Map<String, Object> entityToParam(OnlyPk1Entity entity) {
        var param = new HashMap<String, Object>();
        param.put("pk", entity.getPk());
        return param;
    }

    public OnlyPk1Entity update(OnlyPk1Entity entity) {
        return updateByPk(entity, entity.getPk());
    }


    public OnlyPk1Entity updateByPk(OnlyPk1Entity entity, Long pk) {
        var __sql = new ArrayList<String>();
        var setClause = Columns.MAP.values().stream().filter(c-> !c.isShouldSkipInUpdate()).map(BaseColumnDefinition::toUpdateSetClause).collect(joining(", "));
        __sql.add("update \"only_pk1\"");
        __sql.add("set %s".formatted(setClause));
        var __param = entityToParam(entity);
        __param.put("__pk1", pk);
        __sql.add("where \"pk\" = :__pk1");
        var res = this.helper.exec(__sql, __param);
        if (res != 1) {
            throw new EmptyResultDataAccessException(1);
        }
        return entity;
    }

    public Optional<OnlyPk1Entity> findByPk(Long pk) {
        var __sql = new ArrayList<String>();
        __sql.add("select %s".formatted(Columns.selectAster()));
        __sql.add("from \"only_pk1\"");
        __sql.add("where \"pk\" = :pk");

        var __param = new HashMap<String, Object>();
        __param.put("pk", pk);

        return this.helper.optional(__sql, __param, OnlyPk1Entity.class);
    }

    public int deleteByPk(Long pk) {
        var __sql = new ArrayList<String>();
        __sql.add("delete from \"only_pk1\"");
        __sql.add("where \"pk\" = :pk");

        var __param = new HashMap<String, Object>();
        __param.put("pk", pk);

        return this.helper.exec(__sql, __param);
    }
}