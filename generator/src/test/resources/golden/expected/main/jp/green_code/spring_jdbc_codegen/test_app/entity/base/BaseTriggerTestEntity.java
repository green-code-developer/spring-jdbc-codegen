package jp.green_code.spring_jdbc_codegen.test_app.entity.base;

import java.time.OffsetDateTime;

/**
 * Table: trigger_test
 */
public abstract class BaseTriggerTestEntity {

    /** pk */
    protected Long pk;

    /** col_text */
    protected String colText;

    /** updated_at */
    protected OffsetDateTime updatedAt;

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

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}