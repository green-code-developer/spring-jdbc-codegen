package jp.green_code.spring_jdbc_codegen.test_app.entity.base;

import java.lang.Long;
import java.lang.String;
import java.time.OffsetDateTime;

/**
 * Table: quoted_column_now
 */
public abstract class BaseQuotedColumnNowEntity {

    /** pk */
    protected Long pk;

    /** Updated */
    protected OffsetDateTime updated;

    /** col_text */
    protected String colText;

    public Long getPk() {
        return pk;
    }

    public void setPk(Long pk) {
        this.pk = pk;
    }

    public OffsetDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(OffsetDateTime updated) {
        this.updated = updated;
    }

    public String getColText() {
        return colText;
    }

    public void setColText(String colText) {
        this.colText = colText;
    }
}