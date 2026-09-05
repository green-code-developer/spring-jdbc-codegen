package jp.green_code.spring_jdbc_codegen.test_app.repository.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
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

    /** 他テーブルのカラム、重複指定、PK 指定を弾く */
    protected List<ColumnDefinition> validateColumns(ColumnDefinition first, ColumnDefinition[] rest, boolean rejectPk) {
        var columns = new ArrayList<ColumnDefinition>();
        columns.add(first);
        columns.addAll(Arrays.asList(rest));
        var names = new HashSet<String>();
        for (var c : columns) {
            if (rejectPk && c.getPrimaryKeySeq() != null) {
                throw new IllegalArgumentException("PK は指定できません: " + c.getColumnName());
            }
            if (Columns.MAP.get(c.getColumnName()) != c) {
                throw new IllegalArgumentException("normal_pk1 のカラムではありません: " + c.getColumnName());
            }
            if (!names.add(c.getColumnName())) {
                throw new IllegalArgumentException("カラムが重複しています: " + c.getColumnName());
            }
        }
        return columns;
    }

    protected List<String> toInsertColumns(Set<String> excludeColumns) {
        return Columns.MAP.values().stream()
                .filter(c -> !excludeColumns.contains(c.getColumnName()))
                .map(c -> "\"%s\"".formatted(c.getColumnName()))
                .toList();
    }

    protected List<String> toInsertValues(Set<String> excludeColumns) {
        return Columns.MAP.values().stream()
                .filter(c -> !excludeColumns.contains(c.getColumnName()))
                .map(ColumnDefinition::toParamColumn)
                .toList();
    }

    protected Set<String> toInsertReturning(Set<String> excludeColumns) {
        var res = new LinkedHashSet<String>();
        for (var c : Columns.MAP.values()) {
            if (c.isReturning() || excludeColumns.contains(c.getColumnName())) {
                res.add(c.getColumnName());
            }
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

    /** 全カラムをINSERT 対象とする */
    public int insertAllColumns(NormalPk1Entity entity) {
        return doInsert(entity, Set.of());
    }

    /** 指定したカラムをINSERT 対象から外し、DB の既定値を使う */
    public int insertExcept(NormalPk1Entity entity, ColumnDefinition first, ColumnDefinition... rest) {
        var exclude = new LinkedHashSet<String>();
        validateColumns(first, rest, false).forEach(c -> exclude.add(c.getColumnName()));
        return doInsert(entity, exclude);
    }

    /** PK をINSERT 対象から外し、DB に値を決めさせる */
    public int insertExceptPk(NormalPk1Entity entity) {
        return insertExcept(entity, Columns.PK);
    }

    protected int doInsert(NormalPk1Entity entity, Set<String> excludeColumns) {
        var sql = new ArrayList<String>();
        sql.add("insert into \"normal_pk1\"");
        var insertColumns = toInsertColumns(excludeColumns);
        if (insertColumns.isEmpty()) {
            sql.add("DEFAULT VALUES");
        } else {
            sql.add("(%s)".formatted(join(", ", insertColumns)));
            sql.add("values (%s)".formatted(join(", ", toInsertValues(excludeColumns))));
        }
        var param = entityToParam(entity);
        return execWithReturning(sql, param, entity, toInsertReturning(excludeColumns));
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

    /** PK を除く全カラムを更新する */
    public int updateAllColumns(NormalPk1Entity entity) {
        return doUpdate(entity, Columns.MAP.values().stream().filter(c -> c.getPrimaryKeySeq() == null).toList());
    }

    /** 指定したカラムだけを更新する */
    public int updateInclude(NormalPk1Entity entity, ColumnDefinition first, ColumnDefinition... rest) {
        return doUpdate(entity, validateColumns(first, rest, true));
    }

    protected int doUpdate(NormalPk1Entity entity, List<ColumnDefinition> setColumns) {
        var sql = new ArrayList<String>();
        var param = entityToParam(entity);
        var setClause = setColumns.stream().map(BaseColumnDefinition::toUpdateSetClause).collect(joining(", "));
        sql.add("update \"normal_pk1\"");
        sql.add("set %s".formatted(setClause));
        param.put("__pk1", entity.getPk());
        sql.add("where \"pk\" = :__pk1");
        return execWithReturning(sql, param, entity, Set.of());
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