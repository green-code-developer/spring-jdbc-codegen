package jp.green_code.spring_jdbc_codegen.test_app.entity.base;


/**
 * Table: identity_pk
 */
public abstract class BaseIdentityPkEntity {

    /** pk */
    protected Long pk;

    /** col_text */
    protected String colText;

    /** col_text_not_null */
    protected String colTextNotNull;

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
}