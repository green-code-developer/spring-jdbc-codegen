package jp.green_code.spring_jdbc_codegen.test_app.repository.base;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import jp.green_code.spring_jdbc_codegen.test_app.StatusEnum;
import jp.green_code.spring_jdbc_codegen.test_app.entity.AllTypesAsPkEntity;
import jp.green_code.spring_jdbc_codegen.test_app.repository.ColumnDefinition;
import jp.green_code.spring_jdbc_codegen.test_app.repository.RepositoryHelper;
import org.springframework.dao.EmptyResultDataAccessException;
import static java.lang.String.join;
import static java.util.stream.Collectors.joining;

/**
 * Table: all_types_as_pk
 */
public abstract class BaseAllTypesAsPkRepository {

    protected final RepositoryHelper helper;

    public static class Columns {
        public static final ColumnDefinition COL_SMALLINT = new ColumnDefinition("col_smallint", "colSmallint", "java.lang.Short", "int2", 5, 5, 1, false, false, null, null, false, false, false);
        public static final ColumnDefinition COL_SMALLSERIAL = new ColumnDefinition("col_smallserial", "colSmallserial", "java.lang.Short", "smallserial", 5, 5, 2, false, true, null, null, false, false, false);
        public static final ColumnDefinition COL_INTEGER = new ColumnDefinition("col_integer", "colInteger", "java.lang.Integer", "int4", 4, 10, 3, false, false, null, null, false, false, false);
        public static final ColumnDefinition COL_SERIAL = new ColumnDefinition("col_serial", "colSerial", "java.lang.Integer", "serial", 4, 10, 4, false, true, null, null, false, false, false);
        public static final ColumnDefinition COL_BIGINT = new ColumnDefinition("col_bigint", "colBigint", "java.lang.Long", "int8", -5, 19, 5, false, false, null, null, false, false, false);
        public static final ColumnDefinition COL_BIGSERIAL = new ColumnDefinition("col_bigserial", "colBigserial", "java.lang.Long", "bigserial", -5, 19, 6, false, true, null, null, false, false, false);
        public static final ColumnDefinition COL_REAL = new ColumnDefinition("col_real", "colReal", "java.lang.Float", "float4", 7, 8, 7, false, false, null, null, false, false, false);
        public static final ColumnDefinition COL_DOUBLE_PRECISION = new ColumnDefinition("col_double_precision", "colDoublePrecision", "java.lang.Double", "float8", 8, 17, 8, false, false, null, null, false, false, false);
        public static final ColumnDefinition COL_NUMERIC = new ColumnDefinition("col_numeric", "colNumeric", "java.math.BigDecimal", "numeric", 2, 10, 9, false, false, null, null, false, false, false);
        public static final ColumnDefinition COL_BOOLEAN = new ColumnDefinition("col_boolean", "colBoolean", "java.lang.Boolean", "bool", -7, 1, 10, false, false, null, null, false, false, false);
        public static final ColumnDefinition COL_CHAR = new ColumnDefinition("col_char", "colChar", "java.lang.String", "bpchar", 1, 10, 11, false, false, null, null, false, false, false);
        public static final ColumnDefinition COL_VARCHAR = new ColumnDefinition("col_varchar", "colVarchar", "java.lang.String", "varchar", 12, 50, 12, false, false, null, null, false, false, false);
        public static final ColumnDefinition COL_TEXT = new ColumnDefinition("col_text", "colText", "java.lang.String", "text", 12, 2147483647, 13, false, false, null, null, false, false, false);
        public static final ColumnDefinition COL_DATE = new ColumnDefinition("col_date", "colDate", "java.time.LocalDate", "date", 91, 13, 14, false, false, null, null, false, false, false);
        public static final ColumnDefinition COL_TIME = new ColumnDefinition("col_time", "colTime", "java.time.LocalTime", "time", 92, 15, 15, false, false, null, null, false, false, false);
        public static final ColumnDefinition COL_TIME_TZ = new ColumnDefinition("col_time_tz", "colTimeTz", "java.time.OffsetTime", "timetz", 92, 21, 16, false, false, null, null, false, false, false);
        public static final ColumnDefinition COL_TIMESTAMP = new ColumnDefinition("col_timestamp", "colTimestamp", "java.time.LocalDateTime", "timestamp", 93, 29, 17, false, false, null, null, false, false, false);
        public static final ColumnDefinition COL_TIMESTAMP_TZ = new ColumnDefinition("col_timestamp_tz", "colTimestampTz", "java.time.OffsetDateTime", "timestamptz", 93, 35, 18, false, false, null, null, false, false, false);
        public static final ColumnDefinition COL_INTERVAL = new ColumnDefinition("col_interval", "colInterval", "java.lang.Long", "interval", 1111, 49, 19, false, false, "make_interval(secs => :{javaPropertyName})", "extract(epoch FROM {columnName}) AS {columnName}", false, false, false);
        public static final ColumnDefinition COL_BYTEA = new ColumnDefinition("col_bytea", "colBytea", "byte[]", "bytea", -2, 2147483647, 20, false, false, null, null, false, false, false);
        public static final ColumnDefinition COL_UUID = new ColumnDefinition("col_uuid", "colUuid", "java.util.UUID", "uuid", 1111, 2147483647, 21, false, false, null, null, false, false, false);
        public static final ColumnDefinition COL_JSONB = new ColumnDefinition("col_jsonb", "colJsonb", "java.lang.String", "jsonb", 1111, 2147483647, 22, false, false, ":{javaPropertyName}::jsonb", null, false, false, false);
        public static final ColumnDefinition COL_INET = new ColumnDefinition("col_inet", "colInet", "java.lang.String", "inet", 1111, 2147483647, 23, false, false, ":{javaPropertyName}::inet", null, false, false, false);
        public static final ColumnDefinition COL_CIDR = new ColumnDefinition("col_cidr", "colCidr", "java.lang.String", "cidr", 1111, 2147483647, 24, false, false, ":{javaPropertyName}::cidr", null, false, false, false);
        public static final ColumnDefinition COL_MACADDR = new ColumnDefinition("col_macaddr", "colMacaddr", "java.lang.String", "macaddr", 1111, 2147483647, 25, false, false, ":{javaPropertyName}::macaddr", null, false, false, false);
        public static final ColumnDefinition COL_STATUS_ENUM = new ColumnDefinition("col_status_enum", "colStatusEnum", "jp.green_code.spring_jdbc_codegen.test_app.StatusEnum", "status_enum", 12, 2147483647, 26, false, false, ":{javaPropertyName}::status_enum", null, false, false, false);

        public static final Map<String, ColumnDefinition> MAP = new LinkedHashMap<>();

        static {
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
            MAP.put("col_jsonb", COL_JSONB);
            MAP.put("col_inet", COL_INET);
            MAP.put("col_cidr", COL_CIDR);
            MAP.put("col_macaddr", COL_MACADDR);
            MAP.put("col_status_enum", COL_STATUS_ENUM);
        }

        public static String selectAster() {
            return MAP.values().stream().map(ColumnDefinition::toSelectColumn).collect(joining(", "));
        }
    }

    public BaseAllTypesAsPkRepository(RepositoryHelper helper) {
        this.helper = helper;
    }

    protected List<String> toInsertColumns(AllTypesAsPkEntity entity) {
        var res = new ArrayList<String>();
        res.add("\"col_smallint\"");
        if (entity.getColSmallserial() != null) {
            res.add("\"col_smallserial\"");
        }
        res.add("\"col_integer\"");
        if (entity.getColSerial() != null) {
            res.add("\"col_serial\"");
        }
        res.add("\"col_bigint\"");
        if (entity.getColBigserial() != null) {
            res.add("\"col_bigserial\"");
        }
        res.add("\"col_real\"");
        res.add("\"col_double_precision\"");
        res.add("\"col_numeric\"");
        res.add("\"col_boolean\"");
        res.add("\"col_char\"");
        res.add("\"col_varchar\"");
        res.add("\"col_text\"");
        res.add("\"col_date\"");
        res.add("\"col_time\"");
        res.add("\"col_time_tz\"");
        res.add("\"col_timestamp\"");
        res.add("\"col_timestamp_tz\"");
        res.add("\"col_interval\"");
        res.add("\"col_bytea\"");
        res.add("\"col_uuid\"");
        res.add("\"col_jsonb\"");
        res.add("\"col_inet\"");
        res.add("\"col_cidr\"");
        res.add("\"col_macaddr\"");
        res.add("\"col_status_enum\"");
        return res;
    }

    protected Set<String> toInsertReturning(AllTypesAsPkEntity entity, List<String> insertColumns) {
        var res = new HashSet<String>();
        if (insertColumns.isEmpty()) {
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
            res.add("col_jsonb");
            res.add("col_inet");
            res.add("col_cidr");
            res.add("col_macaddr");
            res.add("col_status_enum");
        } else {
            if (entity.getColSmallserial() == null) {
                res.add("col_smallserial");
            }
            if (entity.getColSerial() == null) {
                res.add("col_serial");
            }
            if (entity.getColBigserial() == null) {
                res.add("col_bigserial");
            }
        }
        return res;
    }

    protected List<String> toInsertValues(AllTypesAsPkEntity entity) {
        var res = new ArrayList<String>();
        res.add("col_smallint");
        if (entity.getColSmallserial() != null) {
            res.add("col_smallserial");
        }
        res.add("col_integer");
        if (entity.getColSerial() != null) {
            res.add("col_serial");
        }
        res.add("col_bigint");
        if (entity.getColBigserial() != null) {
            res.add("col_bigserial");
        }
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
        res.add("col_jsonb");
        res.add("col_inet");
        res.add("col_cidr");
        res.add("col_macaddr");
        res.add("col_status_enum");
        return res;
    }

    protected void copyReturningValuesInInsert(AllTypesAsPkEntity entity, AllTypesAsPkEntity returning) {
        if (entity.getColSmallserial() == null) {
            entity.setColSmallserial(returning.getColSmallserial());
        }
        if (entity.getColSerial() == null) {
            entity.setColSerial(returning.getColSerial());
        }
        if (entity.getColBigserial() == null) {
            entity.setColBigserial(returning.getColBigserial());
        }
    }

    public AllTypesAsPkEntity insert(AllTypesAsPkEntity entity) {
        var sql = new ArrayList<String>();
        sql.add("insert into \"all_types_as_pk\"");
        var insertColumns = toInsertColumns(entity);
        if (insertColumns.isEmpty()) {
            sql.add("DEFAULT VALUES");
        } else {
            sql.add("(%s)".formatted(join(", ", insertColumns)));
            var insertValues = toInsertValues(entity);
            var insertValuesClause = insertValues.stream().map(c -> Columns.MAP.get(c) == null ? c : Columns.MAP.get(c).toParamColumn()).collect(joining(", "));
            sql.add("values (%s)".formatted(insertValuesClause));
        }
        var param = entityToParam(entity);
        var returningColumns = toInsertReturning(entity, insertColumns);
        if (returningColumns.isEmpty()) {
            this.helper.exec(sql, param);
        } else {
            var returningClause = returningColumns.stream().map(c -> Objects.requireNonNull(Columns.MAP.get(c), "Unknown column " + c).toSelectColumn()).collect(joining(", "));
            sql.add("returning %s".formatted(returningClause));
            var ret = this.helper.single(sql, param, AllTypesAsPkEntity.class);
            copyReturningValuesInInsert(entity, ret);
        }
        return entity;
    }

    public static Map<String, Object> entityToParam(AllTypesAsPkEntity entity) {
        var param = new HashMap<String, Object>();
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
        param.put("colJsonb", String.valueOf(entity.getColJsonb()));
        param.put("colInet", entity.getColInet());
        param.put("colCidr", entity.getColCidr());
        param.put("colMacaddr", entity.getColMacaddr());
        param.put("colStatusEnum", String.valueOf(entity.getColStatusEnum()));
        return param;
    }

    public AllTypesAsPkEntity update(AllTypesAsPkEntity entity) {
        return updateByPk(entity, entity.getColSmallint(), entity.getColSmallserial(), entity.getColInteger(), entity.getColSerial(), entity.getColBigint(), entity.getColBigserial(), entity.getColReal(), entity.getColDoublePrecision(), entity.getColNumeric(), entity.getColBoolean(), entity.getColChar(), entity.getColVarchar(), entity.getColText(), entity.getColDate(), entity.getColTime(), entity.getColTimeTz(), entity.getColTimestamp(), entity.getColTimestampTz(), entity.getColInterval(), entity.getColBytea(), entity.getColUuid(), entity.getColJsonb(), entity.getColInet(), entity.getColCidr(), entity.getColMacaddr(), entity.getColStatusEnum());
    }


    public AllTypesAsPkEntity updateByPk(AllTypesAsPkEntity entity, Short colSmallint, Short colSmallserial, Integer colInteger, Integer colSerial, Long colBigint, Long colBigserial, Float colReal, Double colDoublePrecision, BigDecimal colNumeric, Boolean colBoolean, String colChar, String colVarchar, String colText, LocalDate colDate, LocalTime colTime, OffsetTime colTimeTz, LocalDateTime colTimestamp, OffsetDateTime colTimestampTz, Long colInterval, byte[] colBytea, UUID colUuid, String colJsonb, String colInet, String colCidr, String colMacaddr, StatusEnum colStatusEnum) {
        var __sql = new ArrayList<String>();
        var setClause = Columns.MAP.values().stream().filter(c-> !c.isShouldSkipInUpdate()).map(BaseColumnDefinition::toUpdateSetClause).collect(joining(", "));
        __sql.add("update \"all_types_as_pk\"");
        __sql.add("set %s".formatted(setClause));
        var __param = entityToParam(entity);
        __param.put("__pk1", colSmallint);
        __param.put("__pk2", colSmallserial);
        __param.put("__pk3", colInteger);
        __param.put("__pk4", colSerial);
        __param.put("__pk5", colBigint);
        __param.put("__pk6", colBigserial);
        __param.put("__pk7", colReal);
        __param.put("__pk8", colDoublePrecision);
        __param.put("__pk9", colNumeric);
        __param.put("__pk10", colBoolean);
        __param.put("__pk11", colChar);
        __param.put("__pk12", colVarchar);
        __param.put("__pk13", colText);
        __param.put("__pk14", colDate);
        __param.put("__pk15", colTime);
        __param.put("__pk16", colTimeTz);
        __param.put("__pk17", colTimestamp);
        __param.put("__pk18", colTimestampTz);
        __param.put("__pk19", colInterval);
        __param.put("__pk20", colBytea);
        __param.put("__pk21", colUuid);
        __param.put("__pk22", String.valueOf(colJsonb));
        __param.put("__pk23", colInet);
        __param.put("__pk24", colCidr);
        __param.put("__pk25", colMacaddr);
        __param.put("__pk26", String.valueOf(colStatusEnum));
        __sql.add("where \"col_smallint\" = :__pk1 AND \"col_smallserial\" = :__pk2 AND \"col_integer\" = :__pk3 AND \"col_serial\" = :__pk4 AND \"col_bigint\" = :__pk5 AND \"col_bigserial\" = :__pk6 AND \"col_real\" = :__pk7 AND \"col_double_precision\" = :__pk8 AND \"col_numeric\" = :__pk9 AND \"col_boolean\" = :__pk10 AND \"col_char\" = :__pk11 AND \"col_varchar\" = :__pk12 AND \"col_text\" = :__pk13 AND \"col_date\" = :__pk14 AND \"col_time\" = :__pk15 AND \"col_time_tz\" = :__pk16 AND \"col_timestamp\" = :__pk17 AND \"col_timestamp_tz\" = :__pk18 AND \"col_interval\" = make_interval(secs => :__pk19) AND \"col_bytea\" = :__pk20 AND \"col_uuid\" = :__pk21 AND \"col_jsonb\" = :__pk22::jsonb AND \"col_inet\" = :__pk23::inet AND \"col_cidr\" = :__pk24::cidr AND \"col_macaddr\" = :__pk25::macaddr AND \"col_status_enum\" = :__pk26::status_enum");
        var res = this.helper.exec(__sql, __param);
        if (res != 1) {
            throw new EmptyResultDataAccessException(1);
        }
        return entity;
    }

    public Optional<AllTypesAsPkEntity> findByPk(Short colSmallint, Short colSmallserial, Integer colInteger, Integer colSerial, Long colBigint, Long colBigserial, Float colReal, Double colDoublePrecision, BigDecimal colNumeric, Boolean colBoolean, String colChar, String colVarchar, String colText, LocalDate colDate, LocalTime colTime, OffsetTime colTimeTz, LocalDateTime colTimestamp, OffsetDateTime colTimestampTz, Long colInterval, byte[] colBytea, UUID colUuid, String colJsonb, String colInet, String colCidr, String colMacaddr, StatusEnum colStatusEnum) {
        var __sql = new ArrayList<String>();
        __sql.add("select %s".formatted(Columns.selectAster()));
        __sql.add("from \"all_types_as_pk\"");
        __sql.add("where \"col_smallint\" = :colSmallint AND \"col_smallserial\" = :colSmallserial AND \"col_integer\" = :colInteger AND \"col_serial\" = :colSerial AND \"col_bigint\" = :colBigint AND \"col_bigserial\" = :colBigserial AND \"col_real\" = :colReal AND \"col_double_precision\" = :colDoublePrecision AND \"col_numeric\" = :colNumeric AND \"col_boolean\" = :colBoolean AND \"col_char\" = :colChar AND \"col_varchar\" = :colVarchar AND \"col_text\" = :colText AND \"col_date\" = :colDate AND \"col_time\" = :colTime AND \"col_time_tz\" = :colTimeTz AND \"col_timestamp\" = :colTimestamp AND \"col_timestamp_tz\" = :colTimestampTz AND \"col_interval\" = make_interval(secs => :colInterval) AND \"col_bytea\" = :colBytea AND \"col_uuid\" = :colUuid AND \"col_jsonb\" = :colJsonb::jsonb AND \"col_inet\" = :colInet::inet AND \"col_cidr\" = :colCidr::cidr AND \"col_macaddr\" = :colMacaddr::macaddr AND \"col_status_enum\" = :colStatusEnum::status_enum");

        var __param = new HashMap<String, Object>();
        __param.put("colSmallint", colSmallint);
        __param.put("colSmallserial", colSmallserial);
        __param.put("colInteger", colInteger);
        __param.put("colSerial", colSerial);
        __param.put("colBigint", colBigint);
        __param.put("colBigserial", colBigserial);
        __param.put("colReal", colReal);
        __param.put("colDoublePrecision", colDoublePrecision);
        __param.put("colNumeric", colNumeric);
        __param.put("colBoolean", colBoolean);
        __param.put("colChar", colChar);
        __param.put("colVarchar", colVarchar);
        __param.put("colText", colText);
        __param.put("colDate", colDate);
        __param.put("colTime", colTime);
        __param.put("colTimeTz", colTimeTz);
        __param.put("colTimestamp", colTimestamp);
        __param.put("colTimestampTz", colTimestampTz);
        __param.put("colInterval", colInterval);
        __param.put("colBytea", colBytea);
        __param.put("colUuid", colUuid);
        __param.put("colJsonb", String.valueOf(colJsonb));
        __param.put("colInet", colInet);
        __param.put("colCidr", colCidr);
        __param.put("colMacaddr", colMacaddr);
        __param.put("colStatusEnum", String.valueOf(colStatusEnum));

        return this.helper.optional(__sql, __param, AllTypesAsPkEntity.class);
    }

    public int deleteByPk(Short colSmallint, Short colSmallserial, Integer colInteger, Integer colSerial, Long colBigint, Long colBigserial, Float colReal, Double colDoublePrecision, BigDecimal colNumeric, Boolean colBoolean, String colChar, String colVarchar, String colText, LocalDate colDate, LocalTime colTime, OffsetTime colTimeTz, LocalDateTime colTimestamp, OffsetDateTime colTimestampTz, Long colInterval, byte[] colBytea, UUID colUuid, String colJsonb, String colInet, String colCidr, String colMacaddr, StatusEnum colStatusEnum) {
        var __sql = new ArrayList<String>();
        __sql.add("delete from \"all_types_as_pk\"");
        __sql.add("where \"col_smallint\" = :colSmallint AND \"col_smallserial\" = :colSmallserial AND \"col_integer\" = :colInteger AND \"col_serial\" = :colSerial AND \"col_bigint\" = :colBigint AND \"col_bigserial\" = :colBigserial AND \"col_real\" = :colReal AND \"col_double_precision\" = :colDoublePrecision AND \"col_numeric\" = :colNumeric AND \"col_boolean\" = :colBoolean AND \"col_char\" = :colChar AND \"col_varchar\" = :colVarchar AND \"col_text\" = :colText AND \"col_date\" = :colDate AND \"col_time\" = :colTime AND \"col_time_tz\" = :colTimeTz AND \"col_timestamp\" = :colTimestamp AND \"col_timestamp_tz\" = :colTimestampTz AND \"col_interval\" = make_interval(secs => :colInterval) AND \"col_bytea\" = :colBytea AND \"col_uuid\" = :colUuid AND \"col_jsonb\" = :colJsonb::jsonb AND \"col_inet\" = :colInet::inet AND \"col_cidr\" = :colCidr::cidr AND \"col_macaddr\" = :colMacaddr::macaddr AND \"col_status_enum\" = :colStatusEnum::status_enum");

        var __param = new HashMap<String, Object>();
        __param.put("colSmallint", colSmallint);
        __param.put("colSmallserial", colSmallserial);
        __param.put("colInteger", colInteger);
        __param.put("colSerial", colSerial);
        __param.put("colBigint", colBigint);
        __param.put("colBigserial", colBigserial);
        __param.put("colReal", colReal);
        __param.put("colDoublePrecision", colDoublePrecision);
        __param.put("colNumeric", colNumeric);
        __param.put("colBoolean", colBoolean);
        __param.put("colChar", colChar);
        __param.put("colVarchar", colVarchar);
        __param.put("colText", colText);
        __param.put("colDate", colDate);
        __param.put("colTime", colTime);
        __param.put("colTimeTz", colTimeTz);
        __param.put("colTimestamp", colTimestamp);
        __param.put("colTimestampTz", colTimestampTz);
        __param.put("colInterval", colInterval);
        __param.put("colBytea", colBytea);
        __param.put("colUuid", colUuid);
        __param.put("colJsonb", String.valueOf(colJsonb));
        __param.put("colInet", colInet);
        __param.put("colCidr", colCidr);
        __param.put("colMacaddr", colMacaddr);
        __param.put("colStatusEnum", String.valueOf(colStatusEnum));

        return this.helper.exec(__sql, __param);
    }
}