package jp.green_code.spring_jdbc_codegen.test_app.entity.base;


/**
 * Table: no_update_pk1
 */
public abstract class BaseNoUpdatePk1Entity {

    /** pk */
    protected Long pk;

    /** col_no_update_text_not_null_default_x */
    protected String colNoUpdateTextNotNullDefaultX;

    public Long getPk() {
        return pk;
    }

    public void setPk(Long pk) {
        this.pk = pk;
    }

    public String getColNoUpdateTextNotNullDefaultX() {
        return colNoUpdateTextNotNullDefaultX;
    }

    public void setColNoUpdateTextNotNullDefaultX(String colNoUpdateTextNotNullDefaultX) {
        this.colNoUpdateTextNotNullDefaultX = colNoUpdateTextNotNullDefaultX;
    }
}