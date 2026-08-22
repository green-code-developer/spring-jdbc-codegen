package jp.green_code.spring_jdbc_codegen.test_app.entity.base;

import java.lang.Long;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Table: only_pk3
 */
public abstract class BaseOnlyPk3Entity {

    /** pk1 */
    protected Long pk1;

    /** pk2 */
    protected OffsetDateTime pk2;

    /** pk3 */
    protected UUID pk3;

    public Long getPk1() {
        return pk1;
    }

    public void setPk1(Long pk1) {
        this.pk1 = pk1;
    }

    public OffsetDateTime getPk2() {
        return pk2;
    }

    public void setPk2(OffsetDateTime pk2) {
        this.pk2 = pk2;
    }

    public UUID getPk3() {
        return pk3;
    }

    public void setPk3(UUID pk3) {
        this.pk3 = pk3;
    }
}