package jp.green_code.spring_jdbc_codegen.test_app.entity.base;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.util.UUID;
import jp.green_code.spring_jdbc_codegen.test_app.StatusEnum;
import org.jspecify.annotations.Nullable;

/**
 * Table: all_types_as_pk
 */
public abstract class BaseAllTypesAsPkEntity {

    /** col_smallint */
    protected @Nullable Short colSmallint;

    /** col_smallserial */
    protected @Nullable Short colSmallserial;

    /** col_integer */
    protected @Nullable Integer colInteger;

    /** col_serial */
    protected @Nullable Integer colSerial;

    /** col_bigint */
    protected @Nullable Long colBigint;

    /** col_bigserial */
    protected @Nullable Long colBigserial;

    /** col_real */
    protected @Nullable Float colReal;

    /** col_double_precision */
    protected @Nullable Double colDoublePrecision;

    /** col_numeric */
    protected @Nullable BigDecimal colNumeric;

    /** col_boolean */
    protected @Nullable Boolean colBoolean;

    /** col_char */
    protected @Nullable String colChar;

    /** col_varchar */
    protected @Nullable String colVarchar;

    /** col_text */
    protected @Nullable String colText;

    /** col_date */
    protected @Nullable LocalDate colDate;

    /** col_time */
    protected @Nullable LocalTime colTime;

    /** col_time_tz */
    protected @Nullable OffsetTime colTimeTz;

    /** col_timestamp */
    protected @Nullable LocalDateTime colTimestamp;

    /** col_timestamp_tz */
    protected @Nullable OffsetDateTime colTimestampTz;

    /** col_interval */
    protected @Nullable Long colInterval;

    /** col_bytea */
    protected byte @Nullable [] colBytea;

    /** col_uuid */
    protected @Nullable UUID colUuid;

    /** col_jsonb */
    protected @Nullable String colJsonb;

    /** col_inet */
    protected @Nullable String colInet;

    /** col_cidr */
    protected @Nullable String colCidr;

    /** col_macaddr */
    protected @Nullable String colMacaddr;

    /** col_status_enum */
    protected @Nullable StatusEnum colStatusEnum;

    public @Nullable Short getColSmallint() {
        return colSmallint;
    }

    public void setColSmallint(@Nullable Short colSmallint) {
        this.colSmallint = colSmallint;
    }

    public @Nullable Short getColSmallserial() {
        return colSmallserial;
    }

    public void setColSmallserial(@Nullable Short colSmallserial) {
        this.colSmallserial = colSmallserial;
    }

    public @Nullable Integer getColInteger() {
        return colInteger;
    }

    public void setColInteger(@Nullable Integer colInteger) {
        this.colInteger = colInteger;
    }

    public @Nullable Integer getColSerial() {
        return colSerial;
    }

    public void setColSerial(@Nullable Integer colSerial) {
        this.colSerial = colSerial;
    }

    public @Nullable Long getColBigint() {
        return colBigint;
    }

    public void setColBigint(@Nullable Long colBigint) {
        this.colBigint = colBigint;
    }

    public @Nullable Long getColBigserial() {
        return colBigserial;
    }

    public void setColBigserial(@Nullable Long colBigserial) {
        this.colBigserial = colBigserial;
    }

    public @Nullable Float getColReal() {
        return colReal;
    }

    public void setColReal(@Nullable Float colReal) {
        this.colReal = colReal;
    }

    public @Nullable Double getColDoublePrecision() {
        return colDoublePrecision;
    }

    public void setColDoublePrecision(@Nullable Double colDoublePrecision) {
        this.colDoublePrecision = colDoublePrecision;
    }

    public @Nullable BigDecimal getColNumeric() {
        return colNumeric;
    }

    public void setColNumeric(@Nullable BigDecimal colNumeric) {
        this.colNumeric = colNumeric;
    }

    public @Nullable Boolean getColBoolean() {
        return colBoolean;
    }

    public void setColBoolean(@Nullable Boolean colBoolean) {
        this.colBoolean = colBoolean;
    }

    public @Nullable String getColChar() {
        return colChar;
    }

    public void setColChar(@Nullable String colChar) {
        this.colChar = colChar;
    }

    public @Nullable String getColVarchar() {
        return colVarchar;
    }

    public void setColVarchar(@Nullable String colVarchar) {
        this.colVarchar = colVarchar;
    }

    public @Nullable String getColText() {
        return colText;
    }

    public void setColText(@Nullable String colText) {
        this.colText = colText;
    }

    public @Nullable LocalDate getColDate() {
        return colDate;
    }

    public void setColDate(@Nullable LocalDate colDate) {
        this.colDate = colDate;
    }

    public @Nullable LocalTime getColTime() {
        return colTime;
    }

    public void setColTime(@Nullable LocalTime colTime) {
        this.colTime = colTime;
    }

    public @Nullable OffsetTime getColTimeTz() {
        return colTimeTz;
    }

    public void setColTimeTz(@Nullable OffsetTime colTimeTz) {
        this.colTimeTz = colTimeTz;
    }

    public @Nullable LocalDateTime getColTimestamp() {
        return colTimestamp;
    }

    public void setColTimestamp(@Nullable LocalDateTime colTimestamp) {
        this.colTimestamp = colTimestamp;
    }

    public @Nullable OffsetDateTime getColTimestampTz() {
        return colTimestampTz;
    }

    public void setColTimestampTz(@Nullable OffsetDateTime colTimestampTz) {
        this.colTimestampTz = colTimestampTz;
    }

    public @Nullable Long getColInterval() {
        return colInterval;
    }

    public void setColInterval(@Nullable Long colInterval) {
        this.colInterval = colInterval;
    }

    public byte @Nullable [] getColBytea() {
        return colBytea;
    }

    public void setColBytea(byte @Nullable [] colBytea) {
        this.colBytea = colBytea;
    }

    public @Nullable UUID getColUuid() {
        return colUuid;
    }

    public void setColUuid(@Nullable UUID colUuid) {
        this.colUuid = colUuid;
    }

    public @Nullable String getColJsonb() {
        return colJsonb;
    }

    public void setColJsonb(@Nullable String colJsonb) {
        this.colJsonb = colJsonb;
    }

    public @Nullable String getColInet() {
        return colInet;
    }

    public void setColInet(@Nullable String colInet) {
        this.colInet = colInet;
    }

    public @Nullable String getColCidr() {
        return colCidr;
    }

    public void setColCidr(@Nullable String colCidr) {
        this.colCidr = colCidr;
    }

    public @Nullable String getColMacaddr() {
        return colMacaddr;
    }

    public void setColMacaddr(@Nullable String colMacaddr) {
        this.colMacaddr = colMacaddr;
    }

    public @Nullable StatusEnum getColStatusEnum() {
        return colStatusEnum;
    }

    public void setColStatusEnum(@Nullable StatusEnum colStatusEnum) {
        this.colStatusEnum = colStatusEnum;
    }
}