package jp.green_code.spring_jdbc_codegen.test_app.entity.base;

import java.time.OffsetDateTime;

/**
 * Table: only_pk3_now
 */
public abstract class BaseOnlyPk3NowEntity {

    /** pk1 */
    protected OffsetDateTime pk1;

    /** pk2_now */
    protected OffsetDateTime pk2Now;

    /** pk3 */
    protected OffsetDateTime pk3;

    public OffsetDateTime getPk1() {
        return pk1;
    }

    public void setPk1(OffsetDateTime pk1) {
        this.pk1 = pk1;
    }

    public OffsetDateTime getPk2Now() {
        return pk2Now;
    }

    public void setPk2Now(OffsetDateTime pk2Now) {
        this.pk2Now = pk2Now;
    }

    public OffsetDateTime getPk3() {
        return pk3;
    }

    public void setPk3(OffsetDateTime pk3) {
        this.pk3 = pk3;
    }
}