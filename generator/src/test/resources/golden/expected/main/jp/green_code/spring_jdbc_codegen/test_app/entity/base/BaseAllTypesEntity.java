package jp.green_code.spring_jdbc_codegen.test_app.entity.base;

import java.lang.Boolean;
import java.lang.Double;
import java.lang.Float;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Short;
import java.lang.String;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.util.UUID;
import jp.green_code.spring_jdbc_codegen.test_app.StatusEnum;

/**
 * Table: all_types
 */
public abstract class BaseAllTypesEntity {

    /** pk */
    protected Long pk;

    /** col_smallint */
    protected Short colSmallint;

    /** col_smallserial */
    protected Short colSmallserial;

    /** col_integer */
    protected Integer colInteger;

    /** col_serial */
    protected Integer colSerial;

    /** col_bigint */
    protected Long colBigint;

    /** col_bigserial */
    protected Long colBigserial;

    /** col_real */
    protected Float colReal;

    /** col_double_precision */
    protected Double colDoublePrecision;

    /** col_numeric */
    protected BigDecimal colNumeric;

    /** col_boolean */
    protected Boolean colBoolean;

    /** col_char */
    protected String colChar;

    /** col_varchar */
    protected String colVarchar;

    /** col_text */
    protected String colText;

    /** col_date */
    protected LocalDate colDate;

    /** col_time */
    protected LocalTime colTime;

    /** col_time_tz */
    protected OffsetTime colTimeTz;

    /** col_timestamp */
    protected LocalDateTime colTimestamp;

    /** col_timestamp_tz */
    protected OffsetDateTime colTimestampTz;

    /** col_interval */
    protected Long colInterval;

    /** col_bytea */
    protected byte[] colBytea;

    /** col_uuid */
    protected UUID colUuid;

    /** col_json */
    protected String colJson;

    /** col_jsonb */
    protected String colJsonb;

    /** col_xml */
    protected String colXml;

    /** col_inet */
    protected String colInet;

    /** col_cidr */
    protected String colCidr;

    /** col_macaddr */
    protected String colMacaddr;

    /** col_box */
    protected String colBox;

    /** col_point */
    protected String colPoint;

    /** col_line */
    protected String colLine;

    /** col_lseg */
    protected String colLseg;

    /** col_path */
    protected String colPath;

    /** col_polygon */
    protected String colPolygon;

    /** col_circle */
    protected String colCircle;

    /** col_status_enum */
    protected StatusEnum colStatusEnum;

    public Long getPk() {
        return pk;
    }

    public void setPk(Long pk) {
        this.pk = pk;
    }

    public Short getColSmallint() {
        return colSmallint;
    }

    public void setColSmallint(Short colSmallint) {
        this.colSmallint = colSmallint;
    }

    public Short getColSmallserial() {
        return colSmallserial;
    }

    public void setColSmallserial(Short colSmallserial) {
        this.colSmallserial = colSmallserial;
    }

    public Integer getColInteger() {
        return colInteger;
    }

    public void setColInteger(Integer colInteger) {
        this.colInteger = colInteger;
    }

    public Integer getColSerial() {
        return colSerial;
    }

    public void setColSerial(Integer colSerial) {
        this.colSerial = colSerial;
    }

    public Long getColBigint() {
        return colBigint;
    }

    public void setColBigint(Long colBigint) {
        this.colBigint = colBigint;
    }

    public Long getColBigserial() {
        return colBigserial;
    }

    public void setColBigserial(Long colBigserial) {
        this.colBigserial = colBigserial;
    }

    public Float getColReal() {
        return colReal;
    }

    public void setColReal(Float colReal) {
        this.colReal = colReal;
    }

    public Double getColDoublePrecision() {
        return colDoublePrecision;
    }

    public void setColDoublePrecision(Double colDoublePrecision) {
        this.colDoublePrecision = colDoublePrecision;
    }

    public BigDecimal getColNumeric() {
        return colNumeric;
    }

    public void setColNumeric(BigDecimal colNumeric) {
        this.colNumeric = colNumeric;
    }

    public Boolean getColBoolean() {
        return colBoolean;
    }

    public void setColBoolean(Boolean colBoolean) {
        this.colBoolean = colBoolean;
    }

    public String getColChar() {
        return colChar;
    }

    public void setColChar(String colChar) {
        this.colChar = colChar;
    }

    public String getColVarchar() {
        return colVarchar;
    }

    public void setColVarchar(String colVarchar) {
        this.colVarchar = colVarchar;
    }

    public String getColText() {
        return colText;
    }

    public void setColText(String colText) {
        this.colText = colText;
    }

    public LocalDate getColDate() {
        return colDate;
    }

    public void setColDate(LocalDate colDate) {
        this.colDate = colDate;
    }

    public LocalTime getColTime() {
        return colTime;
    }

    public void setColTime(LocalTime colTime) {
        this.colTime = colTime;
    }

    public OffsetTime getColTimeTz() {
        return colTimeTz;
    }

    public void setColTimeTz(OffsetTime colTimeTz) {
        this.colTimeTz = colTimeTz;
    }

    public LocalDateTime getColTimestamp() {
        return colTimestamp;
    }

    public void setColTimestamp(LocalDateTime colTimestamp) {
        this.colTimestamp = colTimestamp;
    }

    public OffsetDateTime getColTimestampTz() {
        return colTimestampTz;
    }

    public void setColTimestampTz(OffsetDateTime colTimestampTz) {
        this.colTimestampTz = colTimestampTz;
    }

    public Long getColInterval() {
        return colInterval;
    }

    public void setColInterval(Long colInterval) {
        this.colInterval = colInterval;
    }

    public byte[] getColBytea() {
        return colBytea;
    }

    public void setColBytea(byte[] colBytea) {
        this.colBytea = colBytea;
    }

    public UUID getColUuid() {
        return colUuid;
    }

    public void setColUuid(UUID colUuid) {
        this.colUuid = colUuid;
    }

    public String getColJson() {
        return colJson;
    }

    public void setColJson(String colJson) {
        this.colJson = colJson;
    }

    public String getColJsonb() {
        return colJsonb;
    }

    public void setColJsonb(String colJsonb) {
        this.colJsonb = colJsonb;
    }

    public String getColXml() {
        return colXml;
    }

    public void setColXml(String colXml) {
        this.colXml = colXml;
    }

    public String getColInet() {
        return colInet;
    }

    public void setColInet(String colInet) {
        this.colInet = colInet;
    }

    public String getColCidr() {
        return colCidr;
    }

    public void setColCidr(String colCidr) {
        this.colCidr = colCidr;
    }

    public String getColMacaddr() {
        return colMacaddr;
    }

    public void setColMacaddr(String colMacaddr) {
        this.colMacaddr = colMacaddr;
    }

    public String getColBox() {
        return colBox;
    }

    public void setColBox(String colBox) {
        this.colBox = colBox;
    }

    public String getColPoint() {
        return colPoint;
    }

    public void setColPoint(String colPoint) {
        this.colPoint = colPoint;
    }

    public String getColLine() {
        return colLine;
    }

    public void setColLine(String colLine) {
        this.colLine = colLine;
    }

    public String getColLseg() {
        return colLseg;
    }

    public void setColLseg(String colLseg) {
        this.colLseg = colLseg;
    }

    public String getColPath() {
        return colPath;
    }

    public void setColPath(String colPath) {
        this.colPath = colPath;
    }

    public String getColPolygon() {
        return colPolygon;
    }

    public void setColPolygon(String colPolygon) {
        this.colPolygon = colPolygon;
    }

    public String getColCircle() {
        return colCircle;
    }

    public void setColCircle(String colCircle) {
        this.colCircle = colCircle;
    }

    public StatusEnum getColStatusEnum() {
        return colStatusEnum;
    }

    public void setColStatusEnum(StatusEnum colStatusEnum) {
        this.colStatusEnum = colStatusEnum;
    }
}