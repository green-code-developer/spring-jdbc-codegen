package jp.green_code.spring_jdbc_codegen.test_app.entity.base;

import java.time.OffsetDateTime;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

/**
 * Table: omittable_pk3
 */
public abstract class BaseOmittablePk3Entity {

    /** pk1 */
    protected @Nullable Long pk1;

    /** pk2 */
    protected @Nullable OffsetDateTime pk2;

    /** pk3 */
    protected @Nullable UUID pk3;

    /** col_text_not_null_default_x */
    protected String colTextNotNullDefaultX = "X";

    public @Nullable Long getPk1() {
        return pk1;
    }

    public void setPk1(@Nullable Long pk1) {
        this.pk1 = pk1;
    }

    public @Nullable OffsetDateTime getPk2() {
        return pk2;
    }

    public void setPk2(@Nullable OffsetDateTime pk2) {
        this.pk2 = pk2;
    }

    public @Nullable UUID getPk3() {
        return pk3;
    }

    public void setPk3(@Nullable UUID pk3) {
        this.pk3 = pk3;
    }

    public String getColTextNotNullDefaultX() {
        return colTextNotNullDefaultX;
    }

    public void setColTextNotNullDefaultX(String colTextNotNullDefaultX) {
        this.colTextNotNullDefaultX = colTextNotNullDefaultX;
    }
}