package jp.green_code.spring_jdbc_codegen.test_app.entity.base;

import java.time.OffsetDateTime;
import org.jspecify.annotations.Nullable;

/**
 * Table: only_pk3_now
 */
public abstract class BaseOnlyPk3NowEntity {

    /** pk1 */
    protected @Nullable OffsetDateTime pk1;

    /** pk2_now */
    protected @Nullable OffsetDateTime pk2Now;

    /** pk3 */
    protected @Nullable OffsetDateTime pk3;

    public @Nullable OffsetDateTime getPk1() {
        return pk1;
    }

    public void setPk1(@Nullable OffsetDateTime pk1) {
        this.pk1 = pk1;
    }

    public @Nullable OffsetDateTime getPk2Now() {
        return pk2Now;
    }

    public void setPk2Now(@Nullable OffsetDateTime pk2Now) {
        this.pk2Now = pk2Now;
    }

    public @Nullable OffsetDateTime getPk3() {
        return pk3;
    }

    public void setPk3(@Nullable OffsetDateTime pk3) {
        this.pk3 = pk3;
    }
}