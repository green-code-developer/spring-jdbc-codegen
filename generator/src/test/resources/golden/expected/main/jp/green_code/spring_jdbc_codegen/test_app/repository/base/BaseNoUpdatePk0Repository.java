package jp.green_code.spring_jdbc_codegen.test_app.repository.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import jp.green_code.spring_jdbc_codegen.test_app.entity.NoUpdatePk0Entity;
import jp.green_code.spring_jdbc_codegen.test_app.repository.ColumnDefinition;
import jp.green_code.spring_jdbc_codegen.test_app.repository.RepositoryHelper;
import org.springframework.dao.EmptyResultDataAccessException;
import static java.lang.String.join;
import static java.util.stream.Collectors.joining;

/**
 * Table: no_update_pk0
 */
public abstract class BaseNoUpdatePk0Repository {

    protected final RepositoryHelper helper;

    public static class Columns {
        public static final ColumnDefinition COL_NO_UPDATE_TEXT_NOT_NULL_DEFAULT_X = new ColumnDefinition("col_no_update_text_not_null_default_x", "colNoUpdateTextNotNullDefaultX", "java.lang.String", "text", 12, 2147483647, null, false, true, null, null, false, true, false);

        public static final Map<String, ColumnDefinition> MAP = new LinkedHashMap<>();

        static {
            MAP.put("col_no_update_text_not_null_default_x", COL_NO_UPDATE_TEXT_NOT_NULL_DEFAULT_X);
        }

        public static String selectAster() {
            return MAP.values().stream().map(ColumnDefinition::toSelectColumn).collect(joining(", "));
        }
    }

    public BaseNoUpdatePk0Repository(RepositoryHelper helper) {
        this.helper = helper;
    }

    protected List<String> toInsertColumns(NoUpdatePk0Entity entity) {
        var res = new ArrayList<String>();
        if (entity.getColNoUpdateTextNotNullDefaultX() != null) {
            res.add("\"col_no_update_text_not_null_default_x\"");
        }
        return res;
    }

    protected Set<String> toInsertReturning(NoUpdatePk0Entity entity, List<String> insertColumns) {
        var res = new HashSet<String>();
        if (insertColumns.isEmpty()) {
            res.add("col_no_update_text_not_null_default_x");
        } else {
            if (entity.getColNoUpdateTextNotNullDefaultX() == null) {
                res.add("col_no_update_text_not_null_default_x");
            }
        }
        return res;
    }

    protected List<String> toInsertValues(NoUpdatePk0Entity entity) {
        var res = new ArrayList<String>();
        if (entity.getColNoUpdateTextNotNullDefaultX() != null) {
            res.add("col_no_update_text_not_null_default_x");
        }
        return res;
    }

    protected void copyReturningValuesInInsert(NoUpdatePk0Entity entity, NoUpdatePk0Entity returning) {
        if (entity.getColNoUpdateTextNotNullDefaultX() == null) {
            entity.setColNoUpdateTextNotNullDefaultX(returning.getColNoUpdateTextNotNullDefaultX());
        }
    }

    public NoUpdatePk0Entity insert(NoUpdatePk0Entity entity) {
        var sql = new ArrayList<String>();
        sql.add("insert into \"no_update_pk0\"");
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
            var ret = this.helper.single(sql, param, NoUpdatePk0Entity.class);
            copyReturningValuesInInsert(entity, ret);
        }
        return entity;
    }

    public static Map<String, Object> entityToParam(NoUpdatePk0Entity entity) {
        var param = new HashMap<String, Object>();
        param.put("colNoUpdateTextNotNullDefaultX", entity.getColNoUpdateTextNotNullDefaultX());
        return param;
    }
}