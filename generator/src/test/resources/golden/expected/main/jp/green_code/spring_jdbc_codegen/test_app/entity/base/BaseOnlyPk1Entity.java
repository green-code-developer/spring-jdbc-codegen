package jp.green_code.spring_jdbc_codegen.test_app.entity.base;

import java.lang.Long;

/**
 * Table: only_pk1
 */
public abstract class BaseOnlyPk1Entity {

    /** pk */
    protected Long pk;

    public Long getPk() {
        return pk;
    }

    public void setPk(Long pk) {
        this.pk = pk;
    }
}