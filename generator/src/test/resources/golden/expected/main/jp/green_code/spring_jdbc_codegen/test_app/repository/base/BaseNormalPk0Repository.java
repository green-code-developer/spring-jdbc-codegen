package jp.green_code.spring_jdbc_codegen.test_app.repository.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import jp.green_code.spring_jdbc_codegen.test_app.entity.NormalPk0Entity;
import jp.green_code.spring_jdbc_codegen.test_app.repository.ColumnDefinition;
import jp.green_code.spring_jdbc_codegen.test_app.repository.RepositoryHelper;
import org.springframework.dao.EmptyResultDataAccessException;
import static java.lang.String.join;
import static java.util.stream.Collectors.joining;

/**
 * Table: normal_pk0
 */
public abstract class BaseNormalPk0Repository {

    protected final RepositoryHelper helper;

    public static class Columns {
        public static final ColumnDefinition COL_TEXT = new ColumnDefinition("col_text", "colText", "java.lang.String", "text", 12, 2147483647, null, true, false, null, null, false, false, false);
        public static final ColumnDefinition COL_TEXT_NOT_NULL = new ColumnDefinition("col_text_not_null", "colTextNotNull", "java.lang.String", "text", 12, 2147483647, null, false, false, null, null, false, false, false);
        public static final ColumnDefinition COL_TEXT_NOT_NULL_DEFAULT_X = new ColumnDefinition("col_text_not_null_default_x", "colTextNotNullDefaultX", "java.lang.String", "text", 12, 2147483647, null, false, true, null, null, false, false, false);
        public static final ColumnDefinition COL_TEXT_DEFAULT_Y = new ColumnDefinition("col_text_default_y", "colTextDefaultY", "java.lang.String", "text", 12, 2147483647, null, false, true, null, null, false, false, false);

        public static final Map<String, ColumnDefinition> MAP = new LinkedHashMap<>();

        static {
            MAP.put("col_text", COL_TEXT);
            MAP.put("col_text_not_null", COL_TEXT_NOT_NULL);
            MAP.put("col_text_not_null_default_x", COL_TEXT_NOT_NULL_DEFAULT_X);
            MAP.put("col_text_default_y", COL_TEXT_DEFAULT_Y);
        }

        public static String selectAster() {
            return MAP.values().stream().map(ColumnDefinition::toSelectColumn).collect(joining(", "));
        }
    }

    public BaseNormalPk0Repository(RepositoryHelper helper) {
        this.helper = helper;
    }

    protected List<String> toInsertColumns(NormalPk0Entity entity) {
        var res = new ArrayList<String>();
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

    protected Set<String> toInsertReturning(NormalPk0Entity entity, List<String> insertColumns) {
        var res = new HashSet<String>();
        if (insertColumns.isEmpty()) {
            res.add("col_text");
            res.add("col_text_not_null");
            res.add("col_text_not_null_default_x");
            res.add("col_text_default_y");
        } else {
            if (entity.getColTextNotNullDefaultX() == null) {
                res.add("col_text_not_null_default_x");
            }
            if (entity.getColTextDefaultY() == null) {
                res.add("col_text_default_y");
            }
        }
        return res;
    }

    protected List<String> toInsertValues(NormalPk0Entity entity) {
        var res = new ArrayList<String>();
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

    protected void copyReturningValuesInInsert(NormalPk0Entity entity, NormalPk0Entity returning) {
        if (entity.getColTextNotNullDefaultX() == null) {
            entity.setColTextNotNullDefaultX(returning.getColTextNotNullDefaultX());
        }
        if (entity.getColTextDefaultY() == null) {
            entity.setColTextDefaultY(returning.getColTextDefaultY());
        }
    }

    public NormalPk0Entity insert(NormalPk0Entity entity) {
        var sql = new ArrayList<String>();
        sql.add("insert into \"normal_pk0\"");
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
            var ret = this.helper.single(sql, param, NormalPk0Entity.class);
            copyReturningValuesInInsert(entity, ret);
        }
        return entity;
    }

    public static Map<String, Object> entityToParam(NormalPk0Entity entity) {
        var param = new HashMap<String, Object>();
        param.put("colText", entity.getColText());
        param.put("colTextNotNull", entity.getColTextNotNull());
        param.put("colTextNotNullDefaultX", entity.getColTextNotNullDefaultX());
        param.put("colTextDefaultY", entity.getColTextDefaultY());
        return param;
    }
}