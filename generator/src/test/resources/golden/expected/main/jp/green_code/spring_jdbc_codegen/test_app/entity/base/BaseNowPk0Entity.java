package jp.green_code.spring_jdbc_codegen.test_app.entity.base;

import java.time.OffsetDateTime;

/**
 * Table: now_pk0
 */
public abstract class BaseNowPk0Entity {

    /** col_now */
    protected OffsetDateTime colNow;

    public OffsetDateTime getColNow() {
        return colNow;
    }

    public void setColNow(OffsetDateTime colNow) {
        this.colNow = colNow;
    }
}