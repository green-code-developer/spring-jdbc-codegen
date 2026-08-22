package jp.green_code.spring_jdbc_codegen.test_app.entity.base;

import java.lang.Long;
import java.lang.String;

/**
 * Table: omittable_pk1
 */
public abstract class BaseOmittablePk1Entity {

    /** pk */
    protected Long pk;

    /** col_text_not_null_default_x */
    protected String colTextNotNullDefaultX;

    public Long getPk() {
        return pk;
    }

    public void setPk(Long pk) {
        this.pk = pk;
    }

    public String getColTextNotNullDefaultX() {
        return colTextNotNullDefaultX;
    }

    public void setColTextNotNullDefaultX(String colTextNotNullDefaultX) {
        this.colTextNotNullDefaultX = colTextNotNullDefaultX;
    }
}