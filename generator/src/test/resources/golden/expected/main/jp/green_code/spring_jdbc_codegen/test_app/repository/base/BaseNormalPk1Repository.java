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
import jp.green_code.spring_jdbc_codegen.test_app.entity.NormalPk1Entity;
import jp.green_code.spring_jdbc_codegen.test_app.repository.ColumnDefinition;
import jp.green_code.spring_jdbc_codegen.test_app.repository.RepositoryHelper;
import org.springframework.dao.EmptyResultDataAccessException;
import static java.lang.String.join;
import static java.util.stream.Collectors.joining;

/**
 * Table: normal_pk1
 */
public abstract class BaseNormalPk1Repository {

    protected final RepositoryHelper helper;

    public static class Columns {
        public static final ColumnDefinition PK = new ColumnDefinition("pk", "pk", "java.lang.Long", "bigserial", -5, 19, 1, false, true, null, null, false, false, false);
        public static final ColumnDefinition COL_TEXT = new ColumnDefinition("col_text", "colText", "java.lang.String", "text", 12, 2147483647, null, true, false, null, null, false, false, false);
        public static final ColumnDefinition COL_TEXT_NOT_NULL = new ColumnDefinition("col_text_not_null", "colTextNotNull", "java.lang.String", "text", 12, 2147483647, null, false, false, null, null, false, false, false);
        public static final ColumnDefinition COL_TEXT_NOT_NULL_DEFAULT_X = new ColumnDefinition("col_text_not_null_default_x", "colTextNotNullDefaultX", "java.lang.String", "text", 12, 2147483647, null, false, true, null, null, false, false, false);
        public static final ColumnDefinition COL_TEXT_DEFAULT_Y = new ColumnDefinition("col_text_default_y", "colTextDefaultY", "java.lang.String", "text", 12, 2147483647, null, false, true, null, null, false, false, false);

        public static final Map<String, ColumnDefinition> MAP = new LinkedHashMap<>();

        static {
            MAP.put("pk", PK);
            MAP.put("col_text", COL_TEXT);
            MAP.put("col_text_not_null", COL_TEXT_NOT_NULL);
            MAP.put("col_text_not_null_default_x", COL_TEXT_NOT_NULL_DEFAULT_X);
            MAP.put("col_text_default_y", COL_TEXT_DEFAULT_Y);
        }

        public static String selectAster() {
            return MAP.values().stream().map(ColumnDefinition::toSelectColumn).collect(joining(", "));
        }
    }

    public BaseNormalPk1Repository(RepositoryHelper helper) {
        this.helper = helper;
    }

    protected List<String> toInsertColumns(NormalPk1Entity entity) {
        var res = new ArrayList<String>();
        if (entity.getPk() != null) {
            res.add("\"pk\"");
        }
        res.add("\"col_text\"");
        res.add("\"col_text_not_null\"");
        if (entity.getColTextNotNullDefaultX() != null) {
            res.add("\"col_text_not_null_default_x\"");
        }
        if (entity.getColTextDefaultY() != null) {
            res.add("\"col_text_default_y\"");
        }
        return res;
    }

    protected Set<String> toInsertReturning(NormalPk1Entity entity, List<String> insertColumns) {
        var res = new HashSet<String>();
        if (insertColumns.isEmpty()) {
            res.add("pk");
            res.add("col_text");
            res.add("col_text_not_null");
            res.add("col_text_not_null_default_x");
            res.add("col_text_default_y");
        } else {
            if (entity.getPk() == null) {
                res.add("pk");
            }
            if (entity.getColTextNotNullDefaultX() == null) {
                res.add("col_text_not_null_default_x");
            }
            if (entity.getColTextDefaultY() == null) {
                res.add("col_text_default_y");
            }
        }
        return res;
    }

    protected List<String> toInsertValues(NormalPk1Entity entity) {
        var res = new ArrayList<String>();
        if (entity.getPk() != null) {
            res.add("pk");
        }
        res.add("col_text");
        res.add("col_text_not_null");
        if (entity.getColTextNotNullDefaultX() != null) {
            res.add("col_text_not_null_default_x");
        }
        if (entity.getColTextDefaultY() != null) {
            res.add("col_text_default_y");
        }
        return res;
    }

    protected void copyReturningValuesInInsert(NormalPk1Entity entity, NormalPk1Entity returning) {
        if (entity.getPk() == null) {
            entity.setPk(returning.getPk());
        }
        if (entity.getColTextNotNullDefaultX() == null) {
            entity.setColTextNotNullDefaultX(returning.getColTextNotNullDefaultX());
        }
        if (entity.getColTextDefaultY() == null) {
            entity.setColTextDefaultY(returning.getColTextDefaultY());
        }
    }

    public NormalPk1Entity insert(NormalPk1Entity entity) {
        var sql = new ArrayList<String>();
        sql.add("insert into \"normal_pk1\"");
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
            var ret = this.helper.single(sql, param, NormalPk1Entity.class);
            copyReturningValuesInInsert(entity, ret);
        }
        return entity;
    }

    public static Map<String, Object> entityToParam(NormalPk1Entity entity) {
        var param = new HashMap<String, Object>();
        param.put("pk", entity.getPk());
        param.put("colText", entity.getColText());
        param.put("colTextNotNull", entity.getColTextNotNull());
        param.put("colTextNotNullDefaultX", entity.getColTextNotNullDefaultX());
        param.put("colTextDefaultY", entity.getColTextDefaultY());
        return param;
    }

    public NormalPk1Entity update(NormalPk1Entity entity) {
        return updateByPk(entity, entity.getPk());
    }


    public NormalPk1Entity updateByPk(NormalPk1Entity entity, Long pk) {
        var __sql = new ArrayList<String>();
        var setClause = Columns.MAP.values().stream().filter(c-> !c.isShouldSkipInUpdate()).map(BaseColumnDefinition::toUpdateSetClause).collect(joining(", "));
        __sql.add("update \"normal_pk1\"");
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

    public Optional<NormalPk1Entity> findByPk(Long pk) {
        var __sql = new ArrayList<String>();
        __sql.add("select %s".formatted(Columns.selectAster()));
        __sql.add("from \"normal_pk1\"");
        __sql.add("where \"pk\" = :pk");

        var __param = new HashMap<String, Object>();
        __param.put("pk", pk);

        return this.helper.optional(__sql, __param, NormalPk1Entity.class);
    }

    public int deleteByPk(Long pk) {
        var __sql = new ArrayList<String>();
        __sql.add("delete from \"normal_pk1\"");
        __sql.add("where \"pk\" = :pk");

        var __param = new HashMap<String, Object>();
        __param.put("pk", pk);

        return this.helper.exec(__sql, __param);
    }
}