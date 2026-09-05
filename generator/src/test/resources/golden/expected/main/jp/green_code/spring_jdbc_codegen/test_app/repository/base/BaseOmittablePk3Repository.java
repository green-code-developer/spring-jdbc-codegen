package jp.green_code.spring_jdbc_codegen.test_app.repository.base;

import java.time.OffsetDateTime;
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
import java.util.UUID;
import jp.green_code.spring_jdbc_codegen.test_app.entity.OmittablePk3Entity;
import jp.green_code.spring_jdbc_codegen.test_app.repository.ColumnDefinition;
import jp.green_code.spring_jdbc_codegen.test_app.repository.RepositoryHelper;
import static java.lang.String.join;
import static java.util.stream.Collectors.joining;

/**
 * Table: omittable_pk3
 */
public abstract class BaseOmittablePk3Repository {

    protected final RepositoryHelper helper;

    public static class Columns {
        public static final ColumnDefinition PK1 = new ColumnDefinition("pk1", "pk1", "java.lang.Long", "bigserial", -5, 19, 1, false, true, null, null, false, false);
        public static final ColumnDefinition PK2 = new ColumnDefinition("pk2", "pk2", "java.time.OffsetDateTime", "timestamptz", 93, 35, 2, false, true, null, null, false, false);
        public static final ColumnDefinition PK3 = new ColumnDefinition("pk3", "pk3", "java.util.UUID", "uuid", 1111, 2147483647, 3, false, true, null, null, false, false);
        public static final ColumnDefinition COL_TEXT_NOT_NULL_DEFAULT_X = new ColumnDefinition("col_text_not_null_default_x", "colTextNotNullDefaultX", "java.lang.String", "text", 12, 2147483647, null, false, true, null, null, false, false);

        public static final Map<String, ColumnDefinition> MAP = new LinkedHashMap<>();

        static {
            MAP.put("pk1", PK1);
            MAP.put("pk2", PK2);
            MAP.put("pk3", PK3);
            MAP.put("col_text_not_null_default_x", COL_TEXT_NOT_NULL_DEFAULT_X);
        }

        public static String selectAster() {
            return MAP.values().stream().map(ColumnDefinition::toSelectColumn).collect(joining(", "));
        }
    }

    public BaseOmittablePk3Repository(RepositoryHelper helper) {
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
                throw new IllegalArgumentException("omittable_pk3 のカラムではありません: " + c.getColumnName());
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

    protected void copyReturningValues(OmittablePk3Entity entity, OmittablePk3Entity returning, Set<String> returningColumns) {
        if (returningColumns.contains("pk1")) {
            entity.setPk1(returning.getPk1());
        }
        if (returningColumns.contains("pk2")) {
            entity.setPk2(returning.getPk2());
        }
        if (returningColumns.contains("pk3")) {
            entity.setPk3(returning.getPk3());
        }
        if (returningColumns.contains("col_text_not_null_default_x")) {
            entity.setColTextNotNullDefaultX(returning.getColTextNotNullDefaultX());
        }
    }

    protected int execWithReturning(List<String> sql, Map<String, Object> param, OmittablePk3Entity entity, Set<String> returningColumns) {
        if (returningColumns.isEmpty()) {
            return this.helper.exec(sql, param);
        }
        var returningClause = returningColumns.stream().map(c -> Objects.requireNonNull(Columns.MAP.get(c), "Unknown column " + c).toSelectColumn()).collect(joining(", "));
        sql.add("returning %s".formatted(returningClause));
        var ret = this.helper.optional(sql, param, OmittablePk3Entity.class);
        ret.ifPresent(r -> copyReturningValues(entity, r, returningColumns));
        return ret.isPresent() ? 1 : 0;
    }

    /** 全カラムをINSERT 対象とする */
    public int insertAllColumns(OmittablePk3Entity entity) {
        return doInsert(entity, Set.of());
    }

    /** 指定したカラムをINSERT 対象から外し、DB の既定値を使う */
    public int insertExcept(OmittablePk3Entity entity, ColumnDefinition first, ColumnDefinition... rest) {
        var exclude = new LinkedHashSet<String>();
        validateColumns(first, rest, false).forEach(c -> exclude.add(c.getColumnName()));
        return doInsert(entity, exclude);
    }

    /** PK をINSERT 対象から外し、DB に値を決めさせる */
    public int insertExceptPk(OmittablePk3Entity entity) {
        return insertExcept(entity, Columns.PK1, Columns.PK2, Columns.PK3);
    }

    protected int doInsert(OmittablePk3Entity entity, Set<String> excludeColumns) {
        var sql = new ArrayList<String>();
        sql.add("insert into \"omittable_pk3\"");
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

    public static Map<String, Object> entityToParam(OmittablePk3Entity entity) {
        var param = new HashMap<String, Object>();
        param.put("pk1", entity.getPk1());
        param.put("pk2", entity.getPk2());
        param.put("pk3", entity.getPk3());
        param.put("colTextNotNullDefaultX", entity.getColTextNotNullDefaultX());
        return param;
    }

    /** PK を除く全カラムを更新する */
    public int updateAllColumns(OmittablePk3Entity entity) {
        return doUpdate(entity, Columns.MAP.values().stream().filter(c -> c.getPrimaryKeySeq() == null).toList());
    }

    /** 指定したカラムだけを更新する */
    public int updateInclude(OmittablePk3Entity entity, ColumnDefinition first, ColumnDefinition... rest) {
        return doUpdate(entity, validateColumns(first, rest, true));
    }

    protected int doUpdate(OmittablePk3Entity entity, List<ColumnDefinition> setColumns) {
        var sql = new ArrayList<String>();
        var param = entityToParam(entity);
        var setClause = setColumns.stream().map(BaseColumnDefinition::toUpdateSetClause).collect(joining(", "));
        sql.add("update \"omittable_pk3\"");
        sql.add("set %s".formatted(setClause));
        param.put("__pk1", entity.getPk1());
        param.put("__pk2", entity.getPk2());
        param.put("__pk3", entity.getPk3());
        sql.add("where \"pk1\" = :__pk1 AND \"pk2\" = :__pk2 AND \"pk3\" = :__pk3");
        return execWithReturning(sql, param, entity, Set.of());
    }

    public Optional<OmittablePk3Entity> findByPk(Long pk1, OffsetDateTime pk2, UUID pk3) {
        var __sql = new ArrayList<String>();
        __sql.add("select %s".formatted(Columns.selectAster()));
        __sql.add("from \"omittable_pk3\"");
        __sql.add("where \"pk1\" = :pk1 AND \"pk2\" = :pk2 AND \"pk3\" = :pk3");

        var __param = new HashMap<String, Object>();
        __param.put("pk1", pk1);
        __param.put("pk2", pk2);
        __param.put("pk3", pk3);

        return this.helper.optional(__sql, __param, OmittablePk3Entity.class);
    }

    public int deleteByPk(Long pk1, OffsetDateTime pk2, UUID pk3) {
        var __sql = new ArrayList<String>();
        __sql.add("delete from \"omittable_pk3\"");
        __sql.add("where \"pk1\" = :pk1 AND \"pk2\" = :pk2 AND \"pk3\" = :pk3");

        var __param = new HashMap<String, Object>();
        __param.put("pk1", pk1);
        __param.put("pk2", pk2);
        __param.put("pk3", pk3);

        return this.helper.exec(__sql, __param);
    }
}