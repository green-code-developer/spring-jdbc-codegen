package jp.green_code.spring_jdbc_codegen.test_app.entity.base;

import org.jspecify.annotations.Nullable;

/**
 * Table: identity_pk
 */
public abstract class BaseIdentityPkEntity {

    /** pk */
    protected @Nullable Long pk;

    /** col_text */
    protected @Nullable String colText;

    /** col_text_not_null */
    protected @Nullable String colTextNotNull;

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

    public @Nullable String getColTextNotNull() {
        return colTextNotNull;
    }

    public void setColTextNotNull(@Nullable String colTextNotNull) {
        this.colTextNotNull = colTextNotNull;
    }
}