package jp.green_code.spring_jdbc_codegen.test_app.entity.base;

import java.lang.Long;
import java.lang.String;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Table: normal_pk3
 */
public abstract class BaseNormalPk3Entity {

    /** pk1 */
    protected Long pk1;

    /** pk2 */
    protected OffsetDateTime pk2;

    /** pk3 */
    protected UUID pk3;

    /** col_text */
    protected String colText;

    /** col_text_not_null */
    protected String colTextNotNull;

    /** col_text_not_null_default_x */
    protected String colTextNotNullDefaultX;

    /** col_text_default_y */
    protected String colTextDefaultY;

    public Long getPk1() {
        return pk1;
    }

    public void setPk1(Long pk1) {
        this.pk1 = pk1;
    }

    public OffsetDateTime getPk2() {
        return pk2;
    }

    public void setPk2(OffsetDateTime pk2) {
        this.pk2 = pk2;
    }

    public UUID getPk3() {
        return pk3;
    }

    public void setPk3(UUID pk3) {
        this.pk3 = pk3;
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