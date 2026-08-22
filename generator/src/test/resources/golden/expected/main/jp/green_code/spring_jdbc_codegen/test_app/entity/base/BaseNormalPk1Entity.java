package jp.green_code.spring_jdbc_codegen.test_app.entity.base;

import java.lang.Long;
import java.lang.String;

/**
 * Table: normal_pk1
 */
public abstract class BaseNormalPk1Entity {

    /** pk */
    protected Long pk;

    /** col_text */
    protected String colText;

    /** col_text_not_null */
    protected String colTextNotNull;

    /** col_text_not_null_default_x */
    protected String colTextNotNullDefaultX;

    /** col_text_default_y */
    protected String colTextDefaultY;

    public Long getPk() {
        return pk;
    }

    public void setPk(Long pk) {
        this.pk = pk;
    }

    public String getColText() {
        return colText;
    }

    public void setColText(String colText) {
        this.colText = colText;
    }

    public String getColTextNotNull() {
        return colTextNotNull;
    }

    public void setColTextNotNull(String colTextNotNull) {
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