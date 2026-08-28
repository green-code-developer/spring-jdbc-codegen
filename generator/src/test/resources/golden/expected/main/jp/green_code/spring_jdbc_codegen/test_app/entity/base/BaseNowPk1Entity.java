package jp.green_code.spring_jdbc_codegen.test_app.entity.base;

import java.time.OffsetDateTime;

/**
 * Table: now_pk1
 */
public abstract class BaseNowPk1Entity {

    /** pk */
    protected Long pk;

    /** col_now */
    protected OffsetDateTime colNow;

    public Long getPk() {
        return pk;
    }

    public void setPk(Long pk) {
        this.pk = pk;
    }

    public OffsetDateTime getColNow() {
        return colNow;
    }

    public void setColNow(OffsetDateTime colNow) {
        this.colNow = colNow;
    }
}