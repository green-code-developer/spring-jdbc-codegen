package jp.green_code.spring_jdbc_codegen.test_app.entity.base;

import java.lang.Long;
import java.lang.String;
import java.time.OffsetDateTime;

/**
 * Table: now_all_excluded
 */
public abstract class BaseNowAllExcludedEntity {

    /** pk */
    protected Long pk;

    /** created_at */
    protected OffsetDateTime createdAt;

    /** col_text */
    protected String colText;

    public Long getPk() {
        return pk;
    }

    public void setPk(Long pk) {
        this.pk = pk;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getColText() {
        return colText;
    }

    public void setColText(String colText) {
        this.colText = colText;
    }
}