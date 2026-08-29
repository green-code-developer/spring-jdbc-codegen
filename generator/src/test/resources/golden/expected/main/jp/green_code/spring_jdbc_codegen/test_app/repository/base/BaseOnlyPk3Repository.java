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
import java.util.UUID;
import jp.green_code.spring_jdbc_codegen.test_app.entity.OnlyPk3Entity;
import jp.green_code.spring_jdbc_codegen.test_app.repository.ColumnDefinition;
import jp.green_code.spring_jdbc_codegen.test_app.repository.RepositoryHelper;
import static java.lang.String.join;
import static java.util.stream.Collectors.joining;

/**
 * Table: only_pk3
 */
public abstract class BaseOnlyPk3Repository {

    protected final RepositoryHelper helper;

    public static class Columns {
        public static final ColumnDefinition PK1 = new ColumnDefinition("pk1", "pk1", "java.lang.Long", "bigserial", -5, 19, 1, false, true, null, null, false, false);
        public static final ColumnDefinition PK2 = new ColumnDefinition("pk2", "pk2", "java.time.OffsetDateTime", "timestamptz", 93, 35, 2, false, true, null, null, false, false);
        public static final ColumnDefinition PK3 = new ColumnDefinition("pk3", "pk3", "java.util.UUID", "uuid", 1111, 2147483647, 3, false, true, null, null, false, false);

        public static final Map<String, ColumnDefinition> MAP = new LinkedHashMap<>();

        static {
            MAP.put("pk1", PK1);
            MAP.put("pk2", PK2);
            MAP.put("pk3", PK3);
        }

        public static String selectAster() {
            return MAP.values().stream().map(ColumnDefinition::toSelectColumn).collect(joining(", "));
        }
    }

    public BaseOnlyPk3Repository(RepositoryHelper helper) {
        this.helper = helper;
    }

    protected List<String> toInsertColumns(OnlyPk3Entity entity, boolean excludeNull) {
        var res = new ArrayList<String>();
        if (entity.getPk1() != null) {
            res.add("\"pk1\"");
        }
        if (entity.getPk2() != null) {
            res.add("\"pk2\"");
        }
        if (entity.getPk3() != null) {
            res.add("\"pk3\"");
        }
        return res;
    }

    protected Set<String> toInsertReturning(List<String> insertColumns) {
        var res = new HashSet<String>();
        if (insertColumns.isEmpty()) {
            res.add("pk1");
            res.add("pk2");
            res.add("pk3");
        } else {
            if (!insertColumns.contains("\"pk1\"")) {
                res.add("pk1");
            }
            if (!insertColumns.contains("\"pk2\"")) {
                res.add("pk2");
            }
            if (!insertColumns.contains("\"pk3\"")) {
                res.add("pk3");
            }
        }
        return res;
    }

    protected List<String> toInsertValues(OnlyPk3Entity entity, boolean excludeNull) {
        var res = new ArrayList<String>();
        if (entity.getPk1() != null) {
            res.add("pk1");
        }
        if (entity.getPk2() != null) {
            res.add("pk2");
        }
        if (entity.getPk3() != null) {
            res.add("pk3");
        }
        return res;
    }

    protected void copyReturningValues(OnlyPk3Entity entity, OnlyPk3Entity returning, Set<String> returningColumns) {
        if (returningColumns.contains("pk1")) {
            entity.setPk1(returning.getPk1());
        }
        if (returningColumns.contains("pk2")) {
            entity.setPk2(returning.getPk2());
        }
        if (returningColumns.contains("pk3")) {
            entity.setPk3(returning.getPk3());
        }
    }

    protected OnlyPk3Entity execWithReturning(List<String> sql, Map<String, Object> param, OnlyPk3Entity entity, Set<String> returningColumns) {
        if (returningColumns.isEmpty()) {
            this.helper.exec(sql, param);
            return entity;
        }
        var returningClause = returningColumns.stream().map(c -> Objects.requireNonNull(Columns.MAP.get(c), "Unknown column " + c).toSelectColumn()).collect(joining(", "));
        sql.add("returning %s".formatted(returningClause));
        this.helper.optional(sql, param, OnlyPk3Entity.class)
                .ifPresent(ret -> copyReturningValues(entity, ret, returningColumns));
        return entity;
    }

    public OnlyPk3Entity insert(OnlyPk3Entity entity) {
        return doInsert(entity, false);
    }

    /** 値がnull のカラムをINSERT 対象から外し、DB の既定値を使う */
    public OnlyPk3Entity insertNotNull(OnlyPk3Entity entity) {
        return doInsert(entity, true);
    }

    protected OnlyPk3Entity doInsert(OnlyPk3Entity entity, boolean excludeNull) {
        var __sql = new ArrayList<String>();
        __sql.add("insert into \"only_pk3\"");
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

    public static Map<String, Object> entityToParam(OnlyPk3Entity entity) {
        var param = new HashMap<String, Object>();
        param.put("pk1", entity.getPk1());
        param.put("pk2", entity.getPk2());
        param.put("pk3", entity.getPk3());
        return param;
    }

    public OnlyPk3Entity update(OnlyPk3Entity entity) {
        return doUpdateByPk(entity, false, entity.getPk1(), entity.getPk2(), entity.getPk3());
    }

    /** 値がnull のカラムをset 句から外して部分更新する */
    public OnlyPk3Entity updateNotNull(OnlyPk3Entity entity) {
        return doUpdateByPk(entity, true, entity.getPk1(), entity.getPk2(), entity.getPk3());
    }


    public OnlyPk3Entity updateByPk(OnlyPk3Entity entity, Long pk1, OffsetDateTime pk2, UUID pk3) {
        return doUpdateByPk(entity, false, pk1, pk2, pk3);
    }

    protected OnlyPk3Entity doUpdateByPk(OnlyPk3Entity entity, boolean excludeNull, Long pk1, OffsetDateTime pk2, UUID pk3) {
        var __sql = new ArrayList<String>();
        var __param = entityToParam(entity);
        var setClause = Columns.MAP.values().stream()
                .filter(c -> !excludeNull || __param.get(c.getJavaPropertyName()) != null)
                .map(BaseColumnDefinition::toUpdateSetClause).collect(joining(", "));
        if (setClause.isEmpty()) {
            throw new IllegalArgumentException("更新対象のカラムがありません");
        }
        __sql.add("update \"only_pk3\"");
        __sql.add("set %s".formatted(setClause));
        __param.put("__pk1", pk1);
        __param.put("__pk2", pk2);
        __param.put("__pk3", pk3);
        __sql.add("where \"pk1\" = :__pk1 AND \"pk2\" = :__pk2 AND \"pk3\" = :__pk3");
        return execWithReturning(__sql, __param, entity, Set.of());
    }

    public Optional<OnlyPk3Entity> findByPk(Long pk1, OffsetDateTime pk2, UUID pk3) {
        var __sql = new ArrayList<String>();
        __sql.add("select %s".formatted(Columns.selectAster()));
        __sql.add("from \"only_pk3\"");
        __sql.add("where \"pk1\" = :pk1 AND \"pk2\" = :pk2 AND \"pk3\" = :pk3");

        var __param = new HashMap<String, Object>();
        __param.put("pk1", pk1);
        __param.put("pk2", pk2);
        __param.put("pk3", pk3);

        return this.helper.optional(__sql, __param, OnlyPk3Entity.class);
    }

    public int deleteByPk(Long pk1, OffsetDateTime pk2, UUID pk3) {
        var __sql = new ArrayList<String>();
        __sql.add("delete from \"only_pk3\"");
        __sql.add("where \"pk1\" = :pk1 AND \"pk2\" = :pk2 AND \"pk3\" = :pk3");

        var __param = new HashMap<String, Object>();
        __param.put("pk1", pk1);
        __param.put("pk2", pk2);
        __param.put("pk3", pk3);

        return this.helper.exec(__sql, __param);
    }
}