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
import static java.lang.String.join;
import static java.util.stream.Collectors.joining;

/**
 * Table: normal_pk1
 */
public abstract class BaseNormalPk1Repository {

    protected final RepositoryHelper helper;

    public static class Columns {
        public static final ColumnDefinition PK = new ColumnDefinition("pk", "pk", "java.lang.Long", "bigserial", -5, 19, 1, false, true, null, null, false, false);
        public static final ColumnDefinition COL_TEXT = new ColumnDefinition("col_text", "colText", "java.lang.String", "text", 12, 2147483647, null, true, false, null, null, false, false);
        public static final ColumnDefinition COL_TEXT_NOT_NULL = new ColumnDefinition("col_text_not_null", "colTextNotNull", "java.lang.String", "text", 12, 2147483647, null, false, false, null, null, false, false);
        public static final ColumnDefinition COL_TEXT_NOT_NULL_DEFAULT_X = new ColumnDefinition("col_text_not_null_default_x", "colTextNotNullDefaultX", "java.lang.String", "text", 12, 2147483647, null, false, true, null, null, false, false);
        public static final ColumnDefinition COL_TEXT_DEFAULT_Y = new ColumnDefinition("col_text_default_y", "colTextDefaultY", "java.lang.String", "text", 12, 2147483647, null, false, true, null, null, false, false);

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

    protected List<String> toInsertColumns(NormalPk1Entity entity, boolean excludeNull) {
        var res = new ArrayList<String>();
        if (entity.getPk() != null) {
            res.add("\"pk\"");
        }
        if (!excludeNull || entity.getColText() != null) {
            res.add("\"col_text\"");
        }
        if (!excludeNull || entity.getColTextNotNull() != null) {
            res.add("\"col_text_not_null\"");
        }
        if (entity.getColTextNotNullDefaultX() != null) {
            res.add("\"col_text_not_null_default_x\"");
        }
        if (entity.getColTextDefaultY() != null) {
            res.add("\"col_text_default_y\"");
        }
        return res;
    }

    protected Set<String> toInsertReturning(List<String> insertColumns) {
        var res = new HashSet<String>();
        if (insertColumns.isEmpty()) {
            res.add("pk");
            res.add("col_text");
            res.add("col_text_not_null");
            res.add("col_text_not_null_default_x");
            res.add("col_text_default_y");
        } else {
            if (!insertColumns.contains("\"pk\"")) {
                res.add("pk");
            }
            if (!insertColumns.contains("\"col_text\"")) {
                res.add("col_text");
            }
            if (!insertColumns.contains("\"col_text_not_null\"")) {
                res.add("col_text_not_null");
            }
            if (!insertColumns.contains("\"col_text_not_null_default_x\"")) {
                res.add("col_text_not_null_default_x");
            }
            if (!insertColumns.contains("\"col_text_default_y\"")) {
                res.add("col_text_default_y");
            }
        }
        return res;
    }

    protected List<String> toInsertValues(NormalPk1Entity entity, boolean excludeNull) {
        var res = new ArrayList<String>();
        if (entity.getPk() != null) {
            res.add("pk");
        }
        if (!excludeNull || entity.getColText() != null) {
            res.add("col_text");
        }
        if (!excludeNull || entity.getColTextNotNull() != null) {
            res.add("col_text_not_null");
        }
        if (entity.getColTextNotNullDefaultX() != null) {
            res.add("col_text_not_null_default_x");
        }
        if (entity.getColTextDefaultY() != null) {
            res.add("col_text_default_y");
        }
        return res;
    }

    protected void copyReturningValues(NormalPk1Entity entity, NormalPk1Entity returning, Set<String> returningColumns) {
        if (returningColumns.contains("pk")) {
            entity.setPk(returning.getPk());
        }
        if (returningColumns.contains("col_text")) {
            entity.setColText(returning.getColText());
        }
        if (returningColumns.contains("col_text_not_null")) {
            entity.setColTextNotNull(returning.getColTextNotNull());
        }
        if (returningColumns.contains("col_text_not_null_default_x")) {
            entity.setColTextNotNullDefaultX(returning.getColTextNotNullDefaultX());
        }
        if (returningColumns.contains("col_text_default_y")) {
            entity.setColTextDefaultY(returning.getColTextDefaultY());
        }
    }

    protected int execWithReturning(List<String> sql, Map<String, Object> param, NormalPk1Entity entity, Set<String> returningColumns) {
        if (returningColumns.isEmpty()) {
            return this.helper.exec(sql, param);
        }
        var returningClause = returningColumns.stream().map(c -> Objects.requireNonNull(Columns.MAP.get(c), "Unknown column " + c).toSelectColumn()).collect(joining(", "));
        sql.add("returning %s".formatted(returningClause));
        var ret = this.helper.optional(sql, param, NormalPk1Entity.class);
        ret.ifPresent(r -> copyReturningValues(entity, r, returningColumns));
        return ret.isPresent() ? 1 : 0;
    }

    public int insert(NormalPk1Entity entity) {
        return doInsert(entity, false);
    }

    /** 値がnull のカラムをINSERT 対象から外し、DB の既定値を使う */
    public int insertNotNull(NormalPk1Entity entity) {
        return doInsert(entity, true);
    }

    protected int doInsert(NormalPk1Entity entity, boolean excludeNull) {
        var __sql = new ArrayList<String>();
        __sql.add("insert into \"normal_pk1\"");
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

    public static Map<String, Object> entityToParam(NormalPk1Entity entity) {
        var param = new HashMap<String, Object>();
        param.put("pk", entity.getPk());
        param.put("colText", entity.getColText());
        param.put("colTextNotNull", entity.getColTextNotNull());
        param.put("colTextNotNullDefaultX", entity.getColTextNotNullDefaultX());
        param.put("colTextDefaultY", entity.getColTextDefaultY());
        return param;
    }

    public int update(NormalPk1Entity entity) {
        return doUpdateByPk(entity, false, entity.getPk());
    }

    /** 値がnull のカラムをset 句から外して部分更新する */
    public int updateNotNull(NormalPk1Entity entity) {
        return doUpdateByPk(entity, true, entity.getPk());
    }


    public int updateByPk(NormalPk1Entity entity, Long pk) {
        return doUpdateByPk(entity, false, pk);
    }

    protected int doUpdateByPk(NormalPk1Entity entity, boolean excludeNull, Long pk) {
        var __sql = new ArrayList<String>();
        var __param = entityToParam(entity);
        var setClause = Columns.MAP.values().stream()
                .filter(c -> !excludeNull || __param.get(c.getJavaPropertyName()) != null)
                .map(BaseColumnDefinition::toUpdateSetClause).collect(joining(", "));
        if (setClause.isEmpty()) {
            throw new IllegalArgumentException("更新対象のカラムがありません");
        }
        __sql.add("update \"normal_pk1\"");
        __sql.add("set %s".formatted(setClause));
        __param.put("__pk1", pk);
        __sql.add("where \"pk\" = :__pk1");
        return execWithReturning(__sql, __param, entity, Set.of());
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