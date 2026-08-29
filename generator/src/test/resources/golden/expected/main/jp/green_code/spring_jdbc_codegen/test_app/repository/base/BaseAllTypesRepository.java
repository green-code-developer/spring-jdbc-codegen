package jp.green_code.spring_jdbc_codegen.test_app.repository.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import jp.green_code.spring_jdbc_codegen.test_app.entity.AllTypesEntity;
import jp.green_code.spring_jdbc_codegen.test_app.repository.ColumnDefinition;
import jp.green_code.spring_jdbc_codegen.test_app.repository.RepositoryHelper;
import static java.lang.String.join;
import static java.util.stream.Collectors.joining;

/**
 * Table: all_types
 */
public abstract class BaseAllTypesRepository {

    protected final RepositoryHelper helper;

    public static class Columns {
        public static final ColumnDefinition PK = new ColumnDefinition("pk", "pk", "java.lang.Long", "bigserial", -5, 19, 1, false, true, null, null, false, false);
        public static final ColumnDefinition COL_SMALLINT = new ColumnDefinition("col_smallint", "colSmallint", "java.lang.Short", "int2", 5, 5, null, true, false, null, null, false, false);
        public static final ColumnDefinition COL_SMALLSERIAL = new ColumnDefinition("col_smallserial", "colSmallserial", "java.lang.Short", "smallserial", 5, 5, null, false, true, null, null, false, false);
        public static final ColumnDefinition COL_INTEGER = new ColumnDefinition("col_integer", "colInteger", "java.lang.Integer", "int4", 4, 10, null, true, false, null, null, false, false);
        public static final ColumnDefinition COL_SERIAL = new ColumnDefinition("col_serial", "colSerial", "java.lang.Integer", "serial", 4, 10, null, false, true, null, null, false, false);
        public static final ColumnDefinition COL_BIGINT = new ColumnDefinition("col_bigint", "colBigint", "java.lang.Long", "int8", -5, 19, null, true, false, null, null, false, false);
        public static final ColumnDefinition COL_BIGSERIAL = new ColumnDefinition("col_bigserial", "colBigserial", "java.lang.Long", "bigserial", -5, 19, null, false, true, null, null, false, false);
        public static final ColumnDefinition COL_REAL = new ColumnDefinition("col_real", "colReal", "java.lang.Float", "float4", 7, 8, null, true, false, null, null, false, false);
        public static final ColumnDefinition COL_DOUBLE_PRECISION = new ColumnDefinition("col_double_precision", "colDoublePrecision", "java.lang.Double", "float8", 8, 17, null, true, false, null, null, false, false);
        public static final ColumnDefinition COL_NUMERIC = new ColumnDefinition("col_numeric", "colNumeric", "java.math.BigDecimal", "numeric", 2, 10, null, true, false, null, null, false, false);
        public static final ColumnDefinition COL_BOOLEAN = new ColumnDefinition("col_boolean", "colBoolean", "java.lang.Boolean", "bool", -7, 1, null, true, false, null, null, false, false);
        public static final ColumnDefinition COL_CHAR = new ColumnDefinition("col_char", "colChar", "java.lang.String", "bpchar", 1, 10, null, true, false, null, null, false, false);
        public static final ColumnDefinition COL_VARCHAR = new ColumnDefinition("col_varchar", "colVarchar", "java.lang.String", "varchar", 12, 50, null, true, false, null, null, false, false);
        public static final ColumnDefinition COL_TEXT = new ColumnDefinition("col_text", "colText", "java.lang.String", "text", 12, 2147483647, null, true, false, null, null, false, false);
        public static final ColumnDefinition COL_DATE = new ColumnDefinition("col_date", "colDate", "java.time.LocalDate", "date", 91, 13, null, true, false, null, null, false, false);
        public static final ColumnDefinition COL_TIME = new ColumnDefinition("col_time", "colTime", "java.time.LocalTime", "time", 92, 15, null, true, false, null, null, false, false);
        public static final ColumnDefinition COL_TIME_TZ = new ColumnDefinition("col_time_tz", "colTimeTz", "java.time.OffsetTime", "timetz", 92, 21, null, true, false, null, null, false, false);
        public static final ColumnDefinition COL_TIMESTAMP = new ColumnDefinition("col_timestamp", "colTimestamp", "java.time.LocalDateTime", "timestamp", 93, 29, null, true, false, null, null, false, false);
        public static final ColumnDefinition COL_TIMESTAMP_TZ = new ColumnDefinition("col_timestamp_tz", "colTimestampTz", "java.time.OffsetDateTime", "timestamptz", 93, 35, null, true, false, null, null, false, false);
        public static final ColumnDefinition COL_INTERVAL = new ColumnDefinition("col_interval", "colInterval", "java.lang.Long", "interval", 1111, 49, null, true, false, "make_interval(secs => :{javaPropertyName})", "extract(epoch FROM {columnName}) AS {columnName}", false, false);
        public static final ColumnDefinition COL_BYTEA = new ColumnDefinition("col_bytea", "colBytea", "byte[]", "bytea", -2, 2147483647, null, true, false, null, null, false, false);
        public static final ColumnDefinition COL_UUID = new ColumnDefinition("col_uuid", "colUuid", "java.util.UUID", "uuid", 1111, 2147483647, null, true, false, null, null, false, false);
        public static final ColumnDefinition COL_JSON = new ColumnDefinition("col_json", "colJson", "java.lang.String", "json", 1111, 2147483647, null, true, false, ":{javaPropertyName}::jsonb", null, false, false);
        public static final ColumnDefinition COL_JSONB = new ColumnDefinition("col_jsonb", "colJsonb", "java.lang.String", "jsonb", 1111, 2147483647, null, true, false, ":{javaPropertyName}::jsonb", null, false, false);
        public static final ColumnDefinition COL_XML = new ColumnDefinition("col_xml", "colXml", "java.lang.String", "xml", 2009, 2147483647, null, true, false, ":{javaPropertyName}::xml", null, false, false);
        public static final ColumnDefinition COL_INET = new ColumnDefinition("col_inet", "colInet", "java.lang.String", "inet", 1111, 2147483647, null, true, false, ":{javaPropertyName}::inet", null, false, false);
        public static final ColumnDefinition COL_CIDR = new ColumnDefinition("col_cidr", "colCidr", "java.lang.String", "cidr", 1111, 2147483647, null, true, false, ":{javaPropertyName}::cidr", null, false, false);
        public static final ColumnDefinition COL_MACADDR = new ColumnDefinition("col_macaddr", "colMacaddr", "java.lang.String", "macaddr", 1111, 2147483647, null, true, false, ":{javaPropertyName}::macaddr", null, false, false);
        public static final ColumnDefinition COL_BOX = new ColumnDefinition("col_box", "colBox", "java.lang.String", "box", 1111, 2147483647, null, true, false, ":{javaPropertyName}::box", "{columnName}::text", false, false);
        public static final ColumnDefinition COL_POINT = new ColumnDefinition("col_point", "colPoint", "java.lang.String", "point", 1111, 2147483647, null, true, false, ":{javaPropertyName}::point", "{columnName}::text", false, false);
        public static final ColumnDefinition COL_LINE = new ColumnDefinition("col_line", "colLine", "java.lang.String", "line", 1111, 2147483647, null, true, false, ":{javaPropertyName}::line", "{columnName}::text", false, false);
        public static final ColumnDefinition COL_LSEG = new ColumnDefinition("col_lseg", "colLseg", "java.lang.String", "lseg", 1111, 2147483647, null, true, false, ":{javaPropertyName}::lseg", "{columnName}::text", false, false);
        public static final ColumnDefinition COL_PATH = new ColumnDefinition("col_path", "colPath", "java.lang.String", "path", 1111, 2147483647, null, true, false, ":{javaPropertyName}::path", "{columnName}::text", false, false);
        public static final ColumnDefinition COL_POLYGON = new ColumnDefinition("col_polygon", "colPolygon", "java.lang.String", "polygon", 1111, 2147483647, null, true, false, ":{javaPropertyName}::polygon", "{columnName}::text", false, false);
        public static final ColumnDefinition COL_CIRCLE = new ColumnDefinition("col_circle", "colCircle", "java.lang.String", "circle", 1111, 2147483647, null, true, false, ":{javaPropertyName}::circle", "{columnName}::text", false, false);
        public static final ColumnDefinition COL_STATUS_ENUM = new ColumnDefinition("col_status_enum", "colStatusEnum", "jp.green_code.spring_jdbc_codegen.test_app.StatusEnum", "status_enum", 12, 2147483647, null, true, false, ":{javaPropertyName}::status_enum", null, false, false);

        public static final Map<String, ColumnDefinition> MAP = new LinkedHashMap<>();

        static {
            MAP.put("pk", PK);
            MAP.put("col_smallint", COL_SMALLINT);
            MAP.put("col_smallserial", COL_SMALLSERIAL);
            MAP.put("col_integer", COL_INTEGER);
            MAP.put("col_serial", COL_SERIAL);
            MAP.put("col_bigint", COL_BIGINT);
            MAP.put("col_bigserial", COL_BIGSERIAL);
            MAP.put("col_real", COL_REAL);
            MAP.put("col_double_precision", COL_DOUBLE_PRECISION);
            MAP.put("col_numeric", COL_NUMERIC);
            MAP.put("col_boolean", COL_BOOLEAN);
            MAP.put("col_char", COL_CHAR);
            MAP.put("col_varchar", COL_VARCHAR);
            MAP.put("col_text", COL_TEXT);
            MAP.put("col_date", COL_DATE);
            MAP.put("col_time", COL_TIME);
            MAP.put("col_time_tz", COL_TIME_TZ);
            MAP.put("col_timestamp", COL_TIMESTAMP);
            MAP.put("col_timestamp_tz", COL_TIMESTAMP_TZ);
            MAP.put("col_interval", COL_INTERVAL);
            MAP.put("col_bytea", COL_BYTEA);
            MAP.put("col_uuid", COL_UUID);
            MAP.put("col_json", COL_JSON);
            MAP.put("col_jsonb", COL_JSONB);
            MAP.put("col_xml", COL_XML);
            MAP.put("col_inet", COL_INET);
            MAP.put("col_cidr", COL_CIDR);
            MAP.put("col_macaddr", COL_MACADDR);
            MAP.put("col_box", COL_BOX);
            MAP.put("col_point", COL_POINT);
            MAP.put("col_line", COL_LINE);
            MAP.put("col_lseg", COL_LSEG);
            MAP.put("col_path", COL_PATH);
            MAP.put("col_polygon", COL_POLYGON);
            MAP.put("col_circle", COL_CIRCLE);
            MAP.put("col_status_enum", COL_STATUS_ENUM);
        }

        public static String selectAster() {
            return MAP.values().stream().map(ColumnDefinition::toSelectColumn).collect(joining(", "));
        }
    }

    public BaseAllTypesRepository(RepositoryHelper helper) {
        this.helper = helper;
    }

    protected List<String> toInsertColumns(AllTypesEntity entity, boolean excludeNull) {
        var res = new ArrayList<String>();
        if (entity.getPk() != null) {
            res.add("\"pk\"");
        }
        if (!excludeNull || entity.getColSmallint() != null) {
            res.add("\"col_smallint\"");
        }
        if (entity.getColSmallserial() != null) {
            res.add("\"col_smallserial\"");
        }
        if (!excludeNull || entity.getColInteger() != null) {
            res.add("\"col_integer\"");
        }
        if (entity.getColSerial() != null) {
            res.add("\"col_serial\"");
        }
        if (!excludeNull || entity.getColBigint() != null) {
            res.add("\"col_bigint\"");
        }
        if (entity.getColBigserial() != null) {
            res.add("\"col_bigserial\"");
        }
        if (!excludeNull || entity.getColReal() != null) {
            res.add("\"col_real\"");
        }
        if (!excludeNull || entity.getColDoublePrecision() != null) {
            res.add("\"col_double_precision\"");
        }
        if (!excludeNull || entity.getColNumeric() != null) {
            res.add("\"col_numeric\"");
        }
        if (!excludeNull || entity.getColBoolean() != null) {
            res.add("\"col_boolean\"");
        }
        if (!excludeNull || entity.getColChar() != null) {
            res.add("\"col_char\"");
        }
        if (!excludeNull || entity.getColVarchar() != null) {
            res.add("\"col_varchar\"");
        }
        if (!excludeNull || entity.getColText() != null) {
            res.add("\"col_text\"");
        }
        if (!excludeNull || entity.getColDate() != null) {
            res.add("\"col_date\"");
        }
        if (!excludeNull || entity.getColTime() != null) {
            res.add("\"col_time\"");
        }
        if (!excludeNull || entity.getColTimeTz() != null) {
            res.add("\"col_time_tz\"");
        }
        if (!excludeNull || entity.getColTimestamp() != null) {
            res.add("\"col_timestamp\"");
        }
        if (!excludeNull || entity.getColTimestampTz() != null) {
            res.add("\"col_timestamp_tz\"");
        }
        if (!excludeNull || entity.getColInterval() != null) {
            res.add("\"col_interval\"");
        }
        if (!excludeNull || entity.getColBytea() != null) {
            res.add("\"col_bytea\"");
        }
        if (!excludeNull || entity.getColUuid() != null) {
            res.add("\"col_uuid\"");
        }
        if (!excludeNull || entity.getColJson() != null) {
            res.add("\"col_json\"");
        }
        if (!excludeNull || entity.getColJsonb() != null) {
            res.add("\"col_jsonb\"");
        }
        if (!excludeNull || entity.getColXml() != null) {
            res.add("\"col_xml\"");
        }
        if (!excludeNull || entity.getColInet() != null) {
            res.add("\"col_inet\"");
        }
        if (!excludeNull || entity.getColCidr() != null) {
            res.add("\"col_cidr\"");
        }
        if (!excludeNull || entity.getColMacaddr() != null) {
            res.add("\"col_macaddr\"");
        }
        if (!excludeNull || entity.getColBox() != null) {
            res.add("\"col_box\"");
        }
        if (!excludeNull || entity.getColPoint() != null) {
            res.add("\"col_point\"");
        }
        if (!excludeNull || entity.getColLine() != null) {
            res.add("\"col_line\"");
        }
        if (!excludeNull || entity.getColLseg() != null) {
            res.add("\"col_lseg\"");
        }
        if (!excludeNull || entity.getColPath() != null) {
            res.add("\"col_path\"");
        }
        if (!excludeNull || entity.getColPolygon() != null) {
            res.add("\"col_polygon\"");
        }
        if (!excludeNull || entity.getColCircle() != null) {
            res.add("\"col_circle\"");
        }
        if (!excludeNull || entity.getColStatusEnum() != null) {
            res.add("\"col_status_enum\"");
        }
        return res;
    }

    protected Set<String> toInsertReturning(List<String> insertColumns) {
        var res = new HashSet<String>();
        if (insertColumns.isEmpty()) {
            res.add("pk");
            res.add("col_smallint");
            res.add("col_smallserial");
            res.add("col_integer");
            res.add("col_serial");
            res.add("col_bigint");
            res.add("col_bigserial");
            res.add("col_real");
            res.add("col_double_precision");
            res.add("col_numeric");
            res.add("col_boolean");
            res.add("col_char");
            res.add("col_varchar");
            res.add("col_text");
            res.add("col_date");
            res.add("col_time");
            res.add("col_time_tz");
            res.add("col_timestamp");
            res.add("col_timestamp_tz");
            res.add("col_interval");
            res.add("col_bytea");
            res.add("col_uuid");
            res.add("col_json");
            res.add("col_jsonb");
            res.add("col_xml");
            res.add("col_inet");
            res.add("col_cidr");
            res.add("col_macaddr");
            res.add("col_box");
            res.add("col_point");
            res.add("col_line");
            res.add("col_lseg");
            res.add("col_path");
            res.add("col_polygon");
            res.add("col_circle");
            res.add("col_status_enum");
        } else {
            if (!insertColumns.contains("\"pk\"")) {
                res.add("pk");
            }
            if (!insertColumns.contains("\"col_smallint\"")) {
                res.add("col_smallint");
            }
            if (!insertColumns.contains("\"col_smallserial\"")) {
                res.add("col_smallserial");
            }
            if (!insertColumns.contains("\"col_integer\"")) {
                res.add("col_integer");
            }
            if (!insertColumns.contains("\"col_serial\"")) {
                res.add("col_serial");
            }
            if (!insertColumns.contains("\"col_bigint\"")) {
                res.add("col_bigint");
            }
            if (!insertColumns.contains("\"col_bigserial\"")) {
                res.add("col_bigserial");
            }
            if (!insertColumns.contains("\"col_real\"")) {
                res.add("col_real");
            }
            if (!insertColumns.contains("\"col_double_precision\"")) {
                res.add("col_double_precision");
            }
            if (!insertColumns.contains("\"col_numeric\"")) {
                res.add("col_numeric");
            }
            if (!insertColumns.contains("\"col_boolean\"")) {
                res.add("col_boolean");
            }
            if (!insertColumns.contains("\"col_char\"")) {
                res.add("col_char");
            }
            if (!insertColumns.contains("\"col_varchar\"")) {
                res.add("col_varchar");
            }
            if (!insertColumns.contains("\"col_text\"")) {
                res.add("col_text");
            }
            if (!insertColumns.contains("\"col_date\"")) {
                res.add("col_date");
            }
            if (!insertColumns.contains("\"col_time\"")) {
                res.add("col_time");
            }
            if (!insertColumns.contains("\"col_time_tz\"")) {
                res.add("col_time_tz");
            }
            if (!insertColumns.contains("\"col_timestamp\"")) {
                res.add("col_timestamp");
            }
            if (!insertColumns.contains("\"col_timestamp_tz\"")) {
                res.add("col_timestamp_tz");
            }
            if (!insertColumns.contains("\"col_interval\"")) {
                res.add("col_interval");
            }
            if (!insertColumns.contains("\"col_bytea\"")) {
                res.add("col_bytea");
            }
            if (!insertColumns.contains("\"col_uuid\"")) {
                res.add("col_uuid");
            }
            if (!insertColumns.contains("\"col_json\"")) {
                res.add("col_json");
            }
            if (!insertColumns.contains("\"col_jsonb\"")) {
                res.add("col_jsonb");
            }
            if (!insertColumns.contains("\"col_xml\"")) {
                res.add("col_xml");
            }
            if (!insertColumns.contains("\"col_inet\"")) {
                res.add("col_inet");
            }
            if (!insertColumns.contains("\"col_cidr\"")) {
                res.add("col_cidr");
            }
            if (!insertColumns.contains("\"col_macaddr\"")) {
                res.add("col_macaddr");
            }
            if (!insertColumns.contains("\"col_box\"")) {
                res.add("col_box");
            }
            if (!insertColumns.contains("\"col_point\"")) {
                res.add("col_point");
            }
            if (!insertColumns.contains("\"col_line\"")) {
                res.add("col_line");
            }
            if (!insertColumns.contains("\"col_lseg\"")) {
                res.add("col_lseg");
            }
            if (!insertColumns.contains("\"col_path\"")) {
                res.add("col_path");
            }
            if (!insertColumns.contains("\"col_polygon\"")) {
                res.add("col_polygon");
            }
            if (!insertColumns.contains("\"col_circle\"")) {
                res.add("col_circle");
            }
            if (!insertColumns.contains("\"col_status_enum\"")) {
                res.add("col_status_enum");
            }
        }
        return res;
    }

    protected List<String> toInsertValues(AllTypesEntity entity, boolean excludeNull) {
        var res = new ArrayList<String>();
        if (entity.getPk() != null) {
            res.add("pk");
        }
        if (!excludeNull || entity.getColSmallint() != null) {
            res.add("col_smallint");
        }
        if (entity.getColSmallserial() != null) {
            res.add("col_smallserial");
        }
        if (!excludeNull || entity.getColInteger() != null) {
            res.add("col_integer");
        }
        if (entity.getColSerial() != null) {
            res.add("col_serial");
        }
        if (!excludeNull || entity.getColBigint() != null) {
            res.add("col_bigint");
        }
        if (entity.getColBigserial() != null) {
            res.add("col_bigserial");
        }
        if (!excludeNull || entity.getColReal() != null) {
            res.add("col_real");
        }
        if (!excludeNull || entity.getColDoublePrecision() != null) {
            res.add("col_double_precision");
        }
        if (!excludeNull || entity.getColNumeric() != null) {
            res.add("col_numeric");
        }
        if (!excludeNull || entity.getColBoolean() != null) {
            res.add("col_boolean");
        }
        if (!excludeNull || entity.getColChar() != null) {
            res.add("col_char");
        }
        if (!excludeNull || entity.getColVarchar() != null) {
            res.add("col_varchar");
        }
        if (!excludeNull || entity.getColText() != null) {
            res.add("col_text");
        }
        if (!excludeNull || entity.getColDate() != null) {
            res.add("col_date");
        }
        if (!excludeNull || entity.getColTime() != null) {
            res.add("col_time");
        }
        if (!excludeNull || entity.getColTimeTz() != null) {
            res.add("col_time_tz");
        }
        if (!excludeNull || entity.getColTimestamp() != null) {
            res.add("col_timestamp");
        }
        if (!excludeNull || entity.getColTimestampTz() != null) {
            res.add("col_timestamp_tz");
        }
        if (!excludeNull || entity.getColInterval() != null) {
            res.add("col_interval");
        }
        if (!excludeNull || entity.getColBytea() != null) {
            res.add("col_bytea");
        }
        if (!excludeNull || entity.getColUuid() != null) {
            res.add("col_uuid");
        }
        if (!excludeNull || entity.getColJson() != null) {
            res.add("col_json");
        }
        if (!excludeNull || entity.getColJsonb() != null) {
            res.add("col_jsonb");
        }
        if (!excludeNull || entity.getColXml() != null) {
            res.add("col_xml");
        }
        if (!excludeNull || entity.getColInet() != null) {
            res.add("col_inet");
        }
        if (!excludeNull || entity.getColCidr() != null) {
            res.add("col_cidr");
        }
        if (!excludeNull || entity.getColMacaddr() != null) {
            res.add("col_macaddr");
        }
        if (!excludeNull || entity.getColBox() != null) {
            res.add("col_box");
        }
        if (!excludeNull || entity.getColPoint() != null) {
            res.add("col_point");
        }
        if (!excludeNull || entity.getColLine() != null) {
            res.add("col_line");
        }
        if (!excludeNull || entity.getColLseg() != null) {
            res.add("col_lseg");
        }
        if (!excludeNull || entity.getColPath() != null) {
            res.add("col_path");
        }
        if (!excludeNull || entity.getColPolygon() != null) {
            res.add("col_polygon");
        }
        if (!excludeNull || entity.getColCircle() != null) {
            res.add("col_circle");
        }
        if (!excludeNull || entity.getColStatusEnum() != null) {
            res.add("col_status_enum");
        }
        return res;
    }

    protected void copyReturningValues(AllTypesEntity entity, AllTypesEntity returning, Set<String> returningColumns) {
        if (returningColumns.contains("pk")) {
            entity.setPk(returning.getPk());
        }
        if (returningColumns.contains("col_smallint")) {
            entity.setColSmallint(returning.getColSmallint());
        }
        if (returningColumns.contains("col_smallserial")) {
            entity.setColSmallserial(returning.getColSmallserial());
        }
        if (returningColumns.contains("col_integer")) {
            entity.setColInteger(returning.getColInteger());
        }
        if (returningColumns.contains("col_serial")) {
            entity.setColSerial(returning.getColSerial());
        }
        if (returningColumns.contains("col_bigint")) {
            entity.setColBigint(returning.getColBigint());
        }
        if (returningColumns.contains("col_bigserial")) {
            entity.setColBigserial(returning.getColBigserial());
        }
        if (returningColumns.contains("col_real")) {
            entity.setColReal(returning.getColReal());
        }
        if (returningColumns.contains("col_double_precision")) {
            entity.setColDoublePrecision(returning.getColDoublePrecision());
        }
        if (returningColumns.contains("col_numeric")) {
            entity.setColNumeric(returning.getColNumeric());
        }
        if (returningColumns.contains("col_boolean")) {
            entity.setColBoolean(returning.getColBoolean());
        }
        if (returningColumns.contains("col_char")) {
            entity.setColChar(returning.getColChar());
        }
        if (returningColumns.contains("col_varchar")) {
            entity.setColVarchar(returning.getColVarchar());
        }
        if (returningColumns.contains("col_text")) {
            entity.setColText(returning.getColText());
        }
        if (returningColumns.contains("col_date")) {
            entity.setColDate(returning.getColDate());
        }
        if (returningColumns.contains("col_time")) {
            entity.setColTime(returning.getColTime());
        }
        if (returningColumns.contains("col_time_tz")) {
            entity.setColTimeTz(returning.getColTimeTz());
        }
        if (returningColumns.contains("col_timestamp")) {
            entity.setColTimestamp(returning.getColTimestamp());
        }
        if (returningColumns.contains("col_timestamp_tz")) {
            entity.setColTimestampTz(returning.getColTimestampTz());
        }
        if (returningColumns.contains("col_interval")) {
            entity.setColInterval(returning.getColInterval());
        }
        if (returningColumns.contains("col_bytea")) {
            entity.setColBytea(returning.getColBytea());
        }
        if (returningColumns.contains("col_uuid")) {
            entity.setColUuid(returning.getColUuid());
        }
        if (returningColumns.contains("col_json")) {
            entity.setColJson(returning.getColJson());
        }
        if (returningColumns.contains("col_jsonb")) {
            entity.setColJsonb(returning.getColJsonb());
        }
        if (returningColumns.contains("col_xml")) {
            entity.setColXml(returning.getColXml());
        }
        if (returningColumns.contains("col_inet")) {
            entity.setColInet(returning.getColInet());
        }
        if (returningColumns.contains("col_cidr")) {
            entity.setColCidr(returning.getColCidr());
        }
        if (returningColumns.contains("col_macaddr")) {
            entity.setColMacaddr(returning.getColMacaddr());
        }
        if (returningColumns.contains("col_box")) {
            entity.setColBox(returning.getColBox());
        }
        if (returningColumns.contains("col_point")) {
            entity.setColPoint(returning.getColPoint());
        }
        if (returningColumns.contains("col_line")) {
            entity.setColLine(returning.getColLine());
        }
        if (returningColumns.contains("col_lseg")) {
            entity.setColLseg(returning.getColLseg());
        }
        if (returningColumns.contains("col_path")) {
            entity.setColPath(returning.getColPath());
        }
        if (returningColumns.contains("col_polygon")) {
            entity.setColPolygon(returning.getColPolygon());
        }
        if (returningColumns.contains("col_circle")) {
            entity.setColCircle(returning.getColCircle());
        }
        if (returningColumns.contains("col_status_enum")) {
            entity.setColStatusEnum(returning.getColStatusEnum());
        }
    }

    protected AllTypesEntity execWithReturning(List<String> sql, Map<String, Object> param, AllTypesEntity entity, Set<String> returningColumns) {
        if (returningColumns.isEmpty()) {
            this.helper.exec(sql, param);
            return entity;
        }
        var returningClause = returningColumns.stream().map(c -> Objects.requireNonNull(Columns.MAP.get(c), "Unknown column " + c).toSelectColumn()).collect(joining(", "));
        sql.add("returning %s".formatted(returningClause));
        this.helper.optional(sql, param, AllTypesEntity.class)
                .ifPresent(ret -> copyReturningValues(entity, ret, returningColumns));
        return entity;
    }

    public AllTypesEntity insert(AllTypesEntity entity) {
        return doInsert(entity, false);
    }

    /** 値がnull のカラムをINSERT 対象から外し、DB の既定値を使う */
    public AllTypesEntity insertNotNull(AllTypesEntity entity) {
        return doInsert(entity, true);
    }

    protected AllTypesEntity doInsert(AllTypesEntity entity, boolean excludeNull) {
        var __sql = new ArrayList<String>();
        __sql.add("insert into \"all_types\"");
        var __insertColumns = toInsertColumns(entity, excludeNull);
        if (__insertColumns.isEmpty()) {
            __sql.add("DEFAULT VALUES");
        } else {
            __sql.add("(%s)".formatted(join(", ", __insertColumns)));
            var __insertValues = toInsertValues(entity, excludeNull);
            var __valuesClause = __insertValues.stream().map(c -> Columns.MAP.get(c) == null ? c : Columns.MAP.get(c).toParamColumn()).collect(joining(", "));
            __sql.add("values (%s)".formatted(__valuesClause));
        }
        var __param = entityToParam(entity);
        return execWithReturning(__sql, __param, entity, toInsertReturning(__insertColumns));
    }

    public static Map<String, Object> entityToParam(AllTypesEntity entity) {
        var param = new HashMap<String, Object>();
        param.put("pk", entity.getPk());
        param.put("colSmallint", entity.getColSmallint());
        param.put("colSmallserial", entity.getColSmallserial());
        param.put("colInteger", entity.getColInteger());
        param.put("colSerial", entity.getColSerial());
        param.put("colBigint", entity.getColBigint());
        param.put("colBigserial", entity.getColBigserial());
        param.put("colReal", entity.getColReal());
        param.put("colDoublePrecision", entity.getColDoublePrecision());
        param.put("colNumeric", entity.getColNumeric());
        param.put("colBoolean", entity.getColBoolean());
        param.put("colChar", entity.getColChar());
        param.put("colVarchar", entity.getColVarchar());
        param.put("colText", entity.getColText());
        param.put("colDate", entity.getColDate());
        param.put("colTime", entity.getColTime());
        param.put("colTimeTz", entity.getColTimeTz());
        param.put("colTimestamp", entity.getColTimestamp());
        param.put("colTimestampTz", entity.getColTimestampTz());
        param.put("colInterval", entity.getColInterval());
        param.put("colBytea", entity.getColBytea());
        param.put("colUuid", entity.getColUuid());
        param.put("colJson", String.valueOf(entity.getColJson()));
        param.put("colJsonb", String.valueOf(entity.getColJsonb()));
        param.put("colXml", entity.getColXml());
        param.put("colInet", entity.getColInet());
        param.put("colCidr", entity.getColCidr());
        param.put("colMacaddr", entity.getColMacaddr());
        param.put("colBox", entity.getColBox());
        param.put("colPoint", entity.getColPoint());
        param.put("colLine", entity.getColLine());
        param.put("colLseg", entity.getColLseg());
        param.put("colPath", entity.getColPath());
        param.put("colPolygon", entity.getColPolygon());
        param.put("colCircle", entity.getColCircle());
        param.put("colStatusEnum", entity.getColStatusEnum() == null ? null : entity.getColStatusEnum().name());
        return param;
    }

    public AllTypesEntity update(AllTypesEntity entity) {
        return doUpdateByPk(entity, false, entity.getPk());
    }

    /** 値がnull のカラムをset 句から外して部分更新する */
    public AllTypesEntity updateNotNull(AllTypesEntity entity) {
        return doUpdateByPk(entity, true, entity.getPk());
    }


    public AllTypesEntity updateByPk(AllTypesEntity entity, Long pk) {
        return doUpdateByPk(entity, false, pk);
    }

    protected AllTypesEntity doUpdateByPk(AllTypesEntity entity, boolean excludeNull, Long pk) {
        var __sql = new ArrayList<String>();
        var __param = entityToParam(entity);
        var setClause = Columns.MAP.values().stream()
                .filter(c -> !excludeNull || __param.get(c.getJavaPropertyName()) != null)
                .map(BaseColumnDefinition::toUpdateSetClause).collect(joining(", "));
        if (setClause.isEmpty()) {
            throw new IllegalArgumentException("更新対象のカラムがありません");
        }
        __sql.add("update \"all_types\"");
        __sql.add("set %s".formatted(setClause));
        __param.put("__pk1", pk);
        __sql.add("where \"pk\" = :__pk1");
        return execWithReturning(__sql, __param, entity, Set.of());
    }

    public Optional<AllTypesEntity> findByPk(Long pk) {
        var __sql = new ArrayList<String>();
        __sql.add("select %s".formatted(Columns.selectAster()));
        __sql.add("from \"all_types\"");
        __sql.add("where \"pk\" = :pk");

        var __param = new HashMap<String, Object>();
        __param.put("pk", pk);

        return this.helper.optional(__sql, __param, AllTypesEntity.class);
    }

    public int deleteByPk(Long pk) {
        var __sql = new ArrayList<String>();
        __sql.add("delete from \"all_types\"");
        __sql.add("where \"pk\" = :pk");

        var __param = new HashMap<String, Object>();
        __param.put("pk", pk);

        return this.helper.exec(__sql, __param);
    }
}