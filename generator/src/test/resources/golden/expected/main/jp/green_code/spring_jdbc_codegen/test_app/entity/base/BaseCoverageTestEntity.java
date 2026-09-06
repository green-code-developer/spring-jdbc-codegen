package jp.green_code.spring_jdbc_codegen.test_app.entity.base;

import java.time.OffsetDateTime;
import jp.green_code.spring_jdbc_codegen.test_app.StatusEnum;
import org.jspecify.annotations.Nullable;

/**
 * Table: coverage_test
 */
public abstract class BaseCoverageTestEntity {

    /** pk */
    protected @Nullable Long pk;

    /** col_nullable_default */
    protected @Nullable String colNullableDefault;

    /** col_notnull_nodefault */
    protected @Nullable String colNotnullNodefault;

    /** col_now_with_default */
    protected @Nullable OffsetDateTime colNowWithDefault;

    /** updated_at */
    protected @Nullable OffsetDateTime updatedAt;

    /** col_enum_default */
    protected StatusEnum colEnumDefault = StatusEnum.NEW;

    /** col_enum_nullable_default */
    protected @Nullable StatusEnum colEnumNullableDefault;

    /** mapped_nullable */
    protected @Nullable String mappedNullableJavaName;

    public @Nullable Long getPk() {
        return pk;
    }

    public void setPk(@Nullable Long pk) {
        this.pk = pk;
    }

    public @Nullable String getColNullableDefault() {
        return colNullableDefault;
    }

    public void setColNullableDefault(@Nullable String colNullableDefault) {
        this.colNullableDefault = colNullableDefault;
    }

    public @Nullable String getColNotnullNodefault() {
        return colNotnullNodefault;
    }

    public void setColNotnullNodefault(@Nullable String colNotnullNodefault) {
        this.colNotnullNodefault = colNotnullNodefault;
    }

    public @Nullable OffsetDateTime getColNowWithDefault() {
        return colNowWithDefault;
    }

    public void setColNowWithDefault(@Nullable OffsetDateTime colNowWithDefault) {
        this.colNowWithDefault = colNowWithDefault;
    }

    public @Nullable OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(@Nullable OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public StatusEnum getColEnumDefault() {
        return colEnumDefault;
    }

    public void setColEnumDefault(StatusEnum colEnumDefault) {
        this.colEnumDefault = colEnumDefault;
    }

    public @Nullable StatusEnum getColEnumNullableDefault() {
        return colEnumNullableDefault;
    }

    public void setColEnumNullableDefault(@Nullable StatusEnum colEnumNullableDefault) {
        this.colEnumNullableDefault = colEnumNullableDefault;
    }

    public @Nullable String getMappedNullableJavaName() {
        return mappedNullableJavaName;
    }

    public void setMappedNullableJavaName(@Nullable String mappedNullableJavaName) {
        this.mappedNullableJavaName = mappedNullableJavaName;
    }
}