package jp.green_code.spring_jdbc_codegen.test_app.entity.base;

import java.lang.Long;
import java.lang.String;
import java.time.LocalDateTime;

/**
 * Table: 日本語Table
 */
public abstract class Base日本語tableEntity {

    /** order */
    protected Long order;

    /** param */
    protected Long param;

    /** sql */
    protected Long sql;

    /** helper */
    protected Long helper;

    /** joining */
    protected Long joining;

    /** List */
    protected Long list;

    /** rename */
    protected String renamedJavaName;

    /** where */
    protected LocalDateTime where;

    /** select */
    protected String select;

    /** Abc */
    protected String abc;

    public Long getOrder() {
        return order;
    }

    public void setOrder(Long order) {
        this.order = order;
    }

    public Long getParam() {
        return param;
    }

    public void setParam(Long param) {
        this.param = param;
    }

    public Long getSql() {
        return sql;
    }

    public void setSql(Long sql) {
        this.sql = sql;
    }

    public Long getHelper() {
        return helper;
    }

    public void setHelper(Long helper) {
        this.helper = helper;
    }

    public Long getJoining() {
        return joining;
    }

    public void setJoining(Long joining) {
        this.joining = joining;
    }

    public Long getList() {
        return list;
    }

    public void setList(Long list) {
        this.list = list;
    }

    public String getRenamedJavaName() {
        return renamedJavaName;
    }

    public void setRenamedJavaName(String renamedJavaName) {
        this.renamedJavaName = renamedJavaName;
    }

    public LocalDateTime getWhere() {
        return where;
    }

    public void setWhere(LocalDateTime where) {
        this.where = where;
    }

    public String getSelect() {
        return select;
    }

    public void setSelect(String select) {
        this.select = select;
    }

    public String getAbc() {
        return abc;
    }

    public void setAbc(String abc) {
        this.abc = abc;
    }
}