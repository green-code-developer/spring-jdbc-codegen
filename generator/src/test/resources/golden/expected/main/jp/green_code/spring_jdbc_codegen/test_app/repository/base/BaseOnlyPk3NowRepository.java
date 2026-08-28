package jp.green_code.spring_jdbc_codegen.test_app.repository.base;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import jp.green_code.spring_jdbc_codegen.test_app.entity.OnlyPk3NowEntity;
import jp.green_code.spring_jdbc_codegen.test_app.repository.ColumnDefinition;
import jp.green_code.spring_jdbc_codegen.test_app.repository.RepositoryHelper;
import static java.lang.String.join;
import static java.util.stream.Collectors.joining;

/**
 * Table: only_pk3_now
 */
public abstract class BaseOnlyPk3NowRepository {

    protected final RepositoryHelper helper;

    public static class Columns {
        public static final ColumnDefinition PK1 = new ColumnDefinition("pk1", "pk1", "java.time.OffsetDateTime", "timestamptz", 93, 35, 1, false, true, null, null, false, false);
        public static final ColumnDefinition PK2_NOW = new ColumnDefinition("pk2_now", "pk2Now", "java.time.OffsetDateTime", "timestamptz", 93, 35, 2, false, false, null, null, true, false);
        public static final ColumnDefinition PK3 = new ColumnDefinition("pk3", "pk3", "java.time.OffsetDateTime", "timestamptz", 93, 35, 3, false, true, null, null, false, false);

        public static final Map<String, ColumnDefinition> MAP = new LinkedHashMap<>();

        static {
            MAP.put("pk1", PK1);
            MAP.put("pk2_now", PK2_NOW);
            MAP.put("pk3", PK3);
        }

        public static String selectAster() {
            return MAP.values().stream().map(ColumnDefinition::toSelectColumn).collect(joining(", "));
        }
    }

    public BaseOnlyPk3NowRepository(RepositoryHelper helper) {
        this.helper = helper;
    }

    protected List<String> toInsertColumns(OnlyPk3NowEntity entity) {
        var res = new ArrayList<String>();
        if (entity.getPk1() != null) {
            res.add("\"pk1\"");
        }
        res.add("\"pk2_now\"");
        if (entity.getPk3() != null) {
            res.add("\"pk3\"");
        }
        return res;
    }

    protected Set<String> toInsertReturning(OnlyPk3NowEntity entity, List<String> insertColumns) {
        var res = new HashSet<String>();
        if (insertColumns.isEmpty()) {
            res.add("pk1");
            res.add("pk2_now");
            res.add("pk3");
        } else {
            if (entity.getPk1() == null) {
                res.add("pk1");
            }
            res.add("pk2_now");
            if (entity.getPk3() == null) {
                res.add("pk3");
            }
        }
        return res;
    }

    protected List<String> toInsertValues(OnlyPk3NowEntity entity) {
        var res = new ArrayList<String>();
        if (entity.getPk1() != null) {
            res.add("pk1");
        }
        res.add("now()");
        if (entity.getPk3() != null) {
            res.add("pk3");
        }
        return res;
    }

    protected void copyReturningValuesInInsert(OnlyPk3NowEntity entity, OnlyPk3NowEntity returning) {
        if (entity.getPk1() == null) {
            entity.setPk1(returning.getPk1());
        }
        entity.setPk2Now(returning.getPk2Now());
        if (entity.getPk3() == null) {
            entity.setPk3(returning.getPk3());
        }
    }

    public OnlyPk3NowEntity insert(OnlyPk3NowEntity entity) {
        var sql = new ArrayList<String>();
        sql.add("insert into \"only_pk3_now\"");
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
            var ret = this.helper.single(sql, param, OnlyPk3NowEntity.class);
            copyReturningValuesInInsert(entity, ret);
        }
        return entity;
    }

    public static Map<String, Object> entityToParam(OnlyPk3NowEntity entity) {
        var param = new HashMap<String, Object>();
        param.put("pk1", entity.getPk1());
        param.put("pk2Now", entity.getPk2Now());
        param.put("pk3", entity.getPk3());
        return param;
    }

    public OnlyPk3NowEntity update(OnlyPk3NowEntity entity) {
        return updateByPk(entity, entity.getPk1(), entity.getPk2Now(), entity.getPk3());
    }

    protected void copyReturningValuesInUpdate(OnlyPk3NowEntity entity, OnlyPk3NowEntity returning) {
        entity.setPk2Now(returning.getPk2Now());
    }

    public OnlyPk3NowEntity updateByPk(OnlyPk3NowEntity entity, OffsetDateTime pk1, OffsetDateTime pk2Now, OffsetDateTime pk3) {
        var __sql = new ArrayList<String>();
        var setClause = Columns.MAP.values().stream().map(BaseColumnDefinition::toUpdateSetClause).collect(joining(", "));
        __sql.add("update \"only_pk3_now\"");
        __sql.add("set %s".formatted(setClause));
        var __param = entityToParam(entity);
        __param.put("__pk1", pk1);
        __param.put("__pk2", pk2Now);
        __param.put("__pk3", pk3);
        __sql.add("where \"pk1\" = :__pk1 AND \"pk2_now\" = :__pk2 AND \"pk3\" = :__pk3");
        var __returning = List.of("pk2_now").stream().map(c -> Objects.requireNonNull(Columns.MAP.get(c), "Unknown column " + c).toSelectColumn()).collect(joining(", "));
        __sql.add("returning %s".formatted(__returning));
        var ret = this.helper.single(__sql, __param, OnlyPk3NowEntity.class);
        copyReturningValuesInUpdate(entity, ret);
        return entity;
    }

    public Optional<OnlyPk3NowEntity> findByPk(OffsetDateTime pk1, OffsetDateTime pk2Now, OffsetDateTime pk3) {
        var __sql = new ArrayList<String>();
        __sql.add("select %s".formatted(Columns.selectAster()));
        __sql.add("from \"only_pk3_now\"");
        __sql.add("where \"pk1\" = :pk1 AND \"pk2_now\" = :pk2Now AND \"pk3\" = :pk3");

        var __param = new HashMap<String, Object>();
        __param.put("pk1", pk1);
        __param.put("pk2Now", pk2Now);
        __param.put("pk3", pk3);

        return this.helper.optional(__sql, __param, OnlyPk3NowEntity.class);
    }

    public int deleteByPk(OffsetDateTime pk1, OffsetDateTime pk2Now, OffsetDateTime pk3) {
        var __sql = new ArrayList<String>();
        __sql.add("delete from \"only_pk3_now\"");
        __sql.add("where \"pk1\" = :pk1 AND \"pk2_now\" = :pk2Now AND \"pk3\" = :pk3");

        var __param = new HashMap<String, Object>();
        __param.put("pk1", pk1);
        __param.put("pk2Now", pk2Now);
        __param.put("pk3", pk3);

        return this.helper.exec(__sql, __param);
    }
}