package jp.green_code.spring_jdbc_codegen.test_app.entity.base;

import java.lang.Long;
import java.lang.String;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Table: no_update_pk3
 */
public abstract class BaseNoUpdatePk3Entity {

    /** pk1 */
    protected Long pk1;

    /** pk2 */
    protected OffsetDateTime pk2;

    /** pk3 */
    protected UUID pk3;

    /** col_no_update_text_not_null_default_x */
    protected String colNoUpdateTextNotNullDefaultX;

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

    public String getColNoUpdateTextNotNullDefaultX() {
        return colNoUpdateTextNotNullDefaultX;
    }

    public void setColNoUpdateTextNotNullDefaultX(String colNoUpdateTextNotNullDefaultX) {
        this.colNoUpdateTextNotNullDefaultX = colNoUpdateTextNotNullDefaultX;
    }
}