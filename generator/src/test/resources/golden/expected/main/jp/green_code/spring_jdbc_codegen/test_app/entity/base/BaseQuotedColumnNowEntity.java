package jp.green_code.spring_jdbc_codegen.test_app.entity.base;

import java.time.OffsetDateTime;
import org.jspecify.annotations.Nullable;

/**
 * Table: quoted_column_now
 */
public abstract class BaseQuotedColumnNowEntity {

    /** pk */
    protected @Nullable Long pk;

    /** Updated */
    protected @Nullable OffsetDateTime updated;

    /** col_text */
    protected @Nullable String colText;

    public @Nullable Long getPk() {
        return pk;
    }

    public void setPk(@Nullable Long pk) {
        this.pk = pk;
    }

    public @Nullable OffsetDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(@Nullable OffsetDateTime updated) {
        this.updated = updated;
    }

    public @Nullable String getColText() {
        return colText;
    }

    public void setColText(@Nullable String colText) {
        this.colText = colText;
    }
}