package jp.green_code.spring_jdbc_codegen.test_app.entity.base;

import org.jspecify.annotations.Nullable;

/**
 * Table: only_pk1
 */
public abstract class BaseOnlyPk1Entity {

    /** pk */
    protected @Nullable Long pk;

    public @Nullable Long getPk() {
        return pk;
    }

    public void setPk(@Nullable Long pk) {
        this.pk = pk;
    }
}