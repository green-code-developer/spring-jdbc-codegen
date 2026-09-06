package jp.green_code.spring_jdbc_codegen.test_app.entity.base;

import java.time.OffsetDateTime;
import org.jspecify.annotations.Nullable;

/**
 * Table: trigger_test
 */
public abstract class BaseTriggerTestEntity {

    /** pk */
    protected @Nullable Long pk;

    /** col_text */
    protected @Nullable String colText;

    /** updated_at */
    protected @Nullable OffsetDateTime updatedAt;

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

    public @Nullable OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(@Nullable OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}