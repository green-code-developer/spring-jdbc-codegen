package jp.green_code.spring_jdbc_codegen.test_app.entity.base;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;
import jp.green_code.spring_jdbc_codegen.test_app.StatusEnum;
import org.jspecify.annotations.Nullable;

/**
 * Table: primitive_default
 */
public abstract class BasePrimitiveDefaultEntity {

    /** pk */
    protected @Nullable Long pk;

    /** col_long */
    protected long colLong = -1L;

    /** col_int */
    protected int colInt = 7;

    /** col_short */
    protected short colShort = (short) 3;

    /** col_bool */
    protected boolean colBool = true;

    /** col_double */
    protected double colDouble = 1.5d;

    /** col_numeric */
    protected BigDecimal colNumeric = new BigDecimal("0.5");

    /** col_text */
    protected String colText = "X";

    /** col_timestamptz */
    protected OffsetDateTime colTimestamptz = OffsetDateTime.parse("2000-01-01T00:00:00+09:00");

    /** col_date */
    protected LocalDate colDate = LocalDate.parse("2000-01-01");

    /** col_uuid */
    protected UUID colUuid = UUID.fromString("9529478b-20d7-4232-ba79-000000000001");

    /** col_uuid_func */
    protected @Nullable UUID colUuidFunc;

    /** col_enum */
    protected StatusEnum colEnum = StatusEnum.DONE;

    /** col_no_default */
    protected @Nullable String colNoDefault;

    public @Nullable Long getPk() {
        return pk;
    }

    public void setPk(@Nullable Long pk) {
        this.pk = pk;
    }

    public long getColLong() {
        return colLong;
    }

    public void setColLong(long colLong) {
        this.colLong = colLong;
    }

    public int getColInt() {
        return colInt;
    }

    public void setColInt(int colInt) {
        this.colInt = colInt;
    }

    public short getColShort() {
        return colShort;
    }

    public void setColShort(short colShort) {
        this.colShort = colShort;
    }

    public boolean getColBool() {
        return colBool;
    }

    public void setColBool(boolean colBool) {
        this.colBool = colBool;
    }

    public double getColDouble() {
        return colDouble;
    }

    public void setColDouble(double colDouble) {
        this.colDouble = colDouble;
    }

    public BigDecimal getColNumeric() {
        return colNumeric;
    }

    public void setColNumeric(BigDecimal colNumeric) {
        this.colNumeric = colNumeric;
    }

    public String getColText() {
        return colText;
    }

    public void setColText(String colText) {
        this.colText = colText;
    }

    public OffsetDateTime getColTimestamptz() {
        return colTimestamptz;
    }

    public void setColTimestamptz(OffsetDateTime colTimestamptz) {
        this.colTimestamptz = colTimestamptz;
    }

    public LocalDate getColDate() {
        return colDate;
    }

    public void setColDate(LocalDate colDate) {
        this.colDate = colDate;
    }

    public UUID getColUuid() {
        return colUuid;
    }

    public void setColUuid(UUID colUuid) {
        this.colUuid = colUuid;
    }

    public @Nullable UUID getColUuidFunc() {
        return colUuidFunc;
    }

    public void setColUuidFunc(@Nullable UUID colUuidFunc) {
        this.colUuidFunc = colUuidFunc;
    }

    public StatusEnum getColEnum() {
        return colEnum;
    }

    public void setColEnum(StatusEnum colEnum) {
        this.colEnum = colEnum;
    }

    public @Nullable String getColNoDefault() {
        return colNoDefault;
    }

    public void setColNoDefault(@Nullable String colNoDefault) {
        this.colNoDefault = colNoDefault;
    }
}