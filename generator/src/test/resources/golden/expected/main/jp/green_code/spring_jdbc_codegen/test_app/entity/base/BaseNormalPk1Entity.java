package jp.green_code.spring_jdbc_codegen.test_app.entity.base;

import org.jspecify.annotations.Nullable;

/**
 * Table: normal_pk1
 */
public abstract class BaseNormalPk1Entity {

    /** pk */
    protected @Nullable Long pk;

    /** col_text */
    protected @Nullable String colText;

    /** col_text_not_null */
    protected @Nullable String colTextNotNull;

    /** col_text_not_null_default_x */
    protected String colTextNotNullDefaultX = "X";

    /** col_text_default_y */
    protected String colTextDefaultY = "y";

    public @Nullable Long getPk() {
        return pk;
    }

    public void setPk(@Nullable Long pk) {
        this.pk = pk;
    }

    public @Nullable String getColText() {
        return colText;
    }

    public void setColText(@Nullable String colText) {
        this.colText = colText;
    }

    public @Nullable String getColTextNotNull() {
        return colTextNotNull;
    }

    public void setColTextNotNull(@Nullable String colTextNotNull) {
        this.colTextNotNull = colTextNotNull;
    }

    public String getColTextNotNullDefaultX() {
        return colTextNotNullDefaultX;
    }

    public void setColTextNotNullDefaultX(String colTextNotNullDefaultX) {
        this.colTextNotNullDefaultX = colTextNotNullDefaultX;
    }

    public String getColTextDefaultY() {
        return colTextDefaultY;
    }

    public void setColTextDefaultY(String colTextDefaultY) {
        this.colTextDefaultY = colTextDefaultY;
    }
}