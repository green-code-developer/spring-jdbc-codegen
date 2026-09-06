package jp.green_code.spring_jdbc_codegen.test_app.entity.base;

import java.time.LocalDateTime;
import org.jspecify.annotations.Nullable;

/**
 * Table: 日本語Table
 */
public abstract class Base日本語tableEntity {

    /** order */
    protected @Nullable Long order;

    /** param */
    protected @Nullable Long param;

    /** sql */
    protected @Nullable Long sql;

    /** helper */
    protected @Nullable Long helper;

    /** joining */
    protected @Nullable Long joining;

    /** List */
    protected @Nullable Long list;

    /** rename */
    protected @Nullable String renamedJavaName;

    /** where */
    protected @Nullable LocalDateTime where;

    /** select */
    protected @Nullable String select;

    /** Abc */
    protected @Nullable String abc;

    public @Nullable Long getOrder() {
        return order;
    }

    public void setOrder(@Nullable Long order) {
        this.order = order;
    }

    public @Nullable Long getParam() {
        return param;
    }

    public void setParam(@Nullable Long param) {
        this.param = param;
    }

    public @Nullable Long getSql() {
        return sql;
    }

    public void setSql(@Nullable Long sql) {
        this.sql = sql;
    }

    public @Nullable Long getHelper() {
        return helper;
    }

    public void setHelper(@Nullable Long helper) {
        this.helper = helper;
    }

    public @Nullable Long getJoining() {
        return joining;
    }

    public void setJoining(@Nullable Long joining) {
        this.joining = joining;
    }

    public @Nullable Long getList() {
        return list;
    }

    public void setList(@Nullable Long list) {
        this.list = list;
    }

    public @Nullable String getRenamedJavaName() {
        return renamedJavaName;
    }

    public void setRenamedJavaName(@Nullable String renamedJavaName) {
        this.renamedJavaName = renamedJavaName;
    }

    public @Nullable LocalDateTime getWhere() {
        return where;
    }

    public void setWhere(@Nullable LocalDateTime where) {
        this.where = where;
    }

    public @Nullable String getSelect() {
        return select;
    }

    public void setSelect(@Nullable String select) {
        this.select = select;
    }

    public @Nullable String getAbc() {
        return abc;
    }

    public void setAbc(@Nullable String abc) {
        this.abc = abc;
    }
}