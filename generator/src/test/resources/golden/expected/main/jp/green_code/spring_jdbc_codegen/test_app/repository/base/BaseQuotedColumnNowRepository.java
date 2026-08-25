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
import jp.green_code.spring_jdbc_codegen.test_app.entity.QuotedColumnNowEntity;
import jp.green_code.spring_jdbc_codegen.test_app.repository.ColumnDefinition;
import jp.green_code.spring_jdbc_codegen.test_app.repository.RepositoryHelper;
import static java.lang.String.join;
import static java.util.stream.Collectors.joining;

/**
 * Table: quoted_column_now
 */
public abstract class BaseQuotedColumnNowRepository {

    protected final RepositoryHelper helper;

    public static class Columns {
        public static final ColumnDefinition PK = new ColumnDefinition("pk", "pk", "java.lang.Long", "bigserial", -5, 19, 1, false, true, null, null, false, false, false);
        public static final ColumnDefinition UPDATED = new ColumnDefinition("Updated", "updated", "java.time.OffsetDateTime", "timestamptz", 93, 35, null, false, true, null, null, true, false, false);
        public static final ColumnDefinition COL_TEXT = new ColumnDefinition("col_text", "colText", "java.lang.String", "text", 12, 2147483647, null, true, false, null, null, false, false, false);

        public static final Map<String, ColumnDefinition> MAP = new LinkedHashMap<>();

        static {
            MAP.put("pk", PK);
            MAP.put("Updated", UPDATED);
            MAP.put("col_text", COL_TEXT);
        }

        public static String selectAster() {
            return MAP.values().stream().map(ColumnDefinition::toSelectColumn).collect(joining(", "));
        }
    }

    public BaseQuotedColumnNowRepository(RepositoryHelper helper) {
        this.helper = helper;
    }

    protected List<String> toInsertColumns(QuotedColumnNowEntity entity) {
        var res = new ArrayList<String>();
        if (entity.getPk() != null) {
            res.add("\"pk\"");
        }
        res.add("\"Updated\"");
        res.add("\"col_text\"");
        return res;
    }

    protected Set<String> toInsertReturning(QuotedColumnNowEntity entity, List<String> insertColumns) {
        var res = new HashSet<String>();
        if (insertColumns.isEmpty()) {
            res.add("pk");
            res.add("Updated");
            res.add("col_text");
        } else {
            if (entity.getPk() == null) {
                res.add("pk");
            }
            res.add("Updated");
        }
        return res;
    }

    protected List<String> toInsertValues(QuotedColumnNowEntity entity) {
        var res = new ArrayList<String>();
        if (entity.getPk() != null) {
            res.add("pk");
        }
        res.add("now()");
        res.add("col_text");
        return res;
    }

    protected void copyReturningValuesInInsert(QuotedColumnNowEntity entity, QuotedColumnNowEntity returning) {
        if (entity.getPk() == null) {
            entity.setPk(returning.getPk());
        }
        entity.setUpdated(returning.getUpdated());
    }

    public QuotedColumnNowEntity insert(QuotedColumnNowEntity entity) {
        var sql = new ArrayList<String>();
        sql.add("insert into \"quoted_column_now\"");
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
            var ret = this.helper.single(sql, param, QuotedColumnNowEntity.class);
            copyReturningValuesInInsert(entity, ret);
        }
        return entity;
    }

    public static Map<String, Object> entityToParam(QuotedColumnNowEntity entity) {
        var param = new HashMap<String, Object>();
        param.put("pk", entity.getPk());
        param.put("updated", entity.getUpdated());
        param.put("colText", entity.getColText());
        return param;
    }

    public QuotedColumnNowEntity update(QuotedColumnNowEntity entity) {
        return updateByPk(entity, entity.getPk());
    }

    protected void copyReturningValuesInUpdate(QuotedColumnNowEntity entity, QuotedColumnNowEntity returning) {
        entity.setUpdated(returning.getUpdated());
    }

    public QuotedColumnNowEntity updateByPk(QuotedColumnNowEntity entity, Long pk) {
        var __sql = new ArrayList<String>();
        var setClause = Columns.MAP.values().stream().filter(c-> !c.isShouldSkipInUpdate()).map(BaseColumnDefinition::toUpdateSetClause).collect(joining(", "));
        __sql.add("update \"quoted_column_now\"");
        __sql.add("set %s".formatted(setClause));
        var __param = entityToParam(entity);
        __param.put("__pk1", pk);
        __sql.add("where \"pk\" = :__pk1");
        var __returning = List.of("Updated").stream().map(c -> Objects.requireNonNull(Columns.MAP.get(c), "Unknown column " + c).toSelectColumn()).collect(joining(", "));
        __sql.add("returning %s".formatted(__returning));
        var ret = this.helper.single(__sql, __param, QuotedColumnNowEntity.class);
        copyReturningValuesInUpdate(entity, ret);
        return entity;
    }

    public Optional<QuotedColumnNowEntity> findByPk(Long pk) {
        var __sql = new ArrayList<String>();
        __sql.add("select %s".formatted(Columns.selectAster()));
        __sql.add("from \"quoted_column_now\"");
        __sql.add("where \"pk\" = :pk");

        var __param = new HashMap<String, Object>();
        __param.put("pk", pk);

        return this.helper.optional(__sql, __param, QuotedColumnNowEntity.class);
    }

    public int deleteByPk(Long pk) {
        var __sql = new ArrayList<String>();
        __sql.add("delete from \"quoted_column_now\"");
        __sql.add("where \"pk\" = :pk");

        var __param = new HashMap<String, Object>();
        __param.put("pk", pk);

        return this.helper.exec(__sql, __param);
    }
}