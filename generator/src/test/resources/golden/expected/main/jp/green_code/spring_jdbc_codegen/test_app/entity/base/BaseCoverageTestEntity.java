package jp.green_code.spring_jdbc_codegen.test_app.entity.base;

import java.time.OffsetDateTime;
import jp.green_code.spring_jdbc_codegen.test_app.StatusEnum;

/**
 * Table: coverage_test
 */
public abstract class BaseCoverageTestEntity {

    /** pk */
    protected Long pk;

    /** col_nullable_default */
    protected String colNullableDefault;

    /** col_notnull_nodefault */
    protected String colNotnullNodefault;

    /** col_now_with_default */
    protected OffsetDateTime colNowWithDefault;

    /** created_at */
    protected OffsetDateTime createdAt;

    /** created_by */
    protected String createdBy;

    /** updated_at */
    protected OffsetDateTime updatedAt;

    /** col_no_update_nullable */
    protected String colNoUpdateNullable;

    /** col_enum_default */
    protected StatusEnum colEnumDefault;

    /** col_enum_nullable_default */
    protected StatusEnum colEnumNullableDefault;

    /** mapped_nullable */
    protected String mappedNullableJavaName;

    public Long getPk() {
        return pk;
    }

    public void setPk(Long pk) {
        this.pk = pk;
    }

    public String getColNullableDefault() {
        return colNullableDefault;
    }

    public void setColNullableDefault(String colNullableDefault) {
        this.colNullableDefault = colNullableDefault;
    }

    public String getColNotnullNodefault() {
        return colNotnullNodefault;
    }

    public void setColNotnullNodefault(String colNotnullNodefault) {
        this.colNotnullNodefault = colNotnullNodefault;
    }

    public OffsetDateTime getColNowWithDefault() {
        return colNowWithDefault;
    }

    public void setColNowWithDefault(OffsetDateTime colNowWithDefault) {
        this.colNowWithDefault = colNowWithDefault;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getColNoUpdateNullable() {
        return colNoUpdateNullable;
    }

    public void setColNoUpdateNullable(String colNoUpdateNullable) {
        this.colNoUpdateNullable = colNoUpdateNullable;
    }

    public StatusEnum getColEnumDefault() {
        return colEnumDefault;
    }

    public void setColEnumDefault(StatusEnum colEnumDefault) {
        this.colEnumDefault = colEnumDefault;
    }

    public StatusEnum getColEnumNullableDefault() {
        return colEnumNullableDefault;
    }

    public void setColEnumNullableDefault(StatusEnum colEnumNullableDefault) {
        this.colEnumNullableDefault = colEnumNullableDefault;
    }

    public String getMappedNullableJavaName() {
        return mappedNullableJavaName;
    }

    public void setMappedNullableJavaName(String mappedNullableJavaName) {
        this.mappedNullableJavaName = mappedNullableJavaName;
    }
}