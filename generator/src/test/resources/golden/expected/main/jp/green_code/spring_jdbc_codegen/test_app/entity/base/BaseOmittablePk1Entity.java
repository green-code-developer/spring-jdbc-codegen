package jp.green_code.spring_jdbc_codegen.test_app.entity.base;

import org.jspecify.annotations.Nullable;

/**
 * Table: omittable_pk1
 */
public abstract class BaseOmittablePk1Entity {

    /** pk */
    protected @Nullable Long pk;

    /** col_text_not_null_default_x */
    protected String colTextNotNullDefaultX = "X";

    public @Nullable Long getPk() {
        return pk;
    }

    public void setPk(@Nullable Long pk) {
        this.pk = pk;
    }

    public String getColTextNotNullDefaultX() {
        return colTextNotNullDefaultX;
    }

    public void setColTextNotNullDefaultX(String colTextNotNullDefaultX) {
        this.colTextNotNullDefaultX = colTextNotNullDefaultX;
    }
}