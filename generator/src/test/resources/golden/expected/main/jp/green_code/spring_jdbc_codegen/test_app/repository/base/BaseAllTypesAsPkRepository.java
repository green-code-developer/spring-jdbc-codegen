package jp.green_code.spring_jdbc_codegen.test_app.repository.base;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
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
import static java.lang.String.join;
import static java.util.stream.Collectors.joining;

/**
 * Table: all_types_as_pk
 */
public abstract class BaseAllTypesAsPkRepository {

    protected final RepositoryHelper helper;

    public static class Columns {
        public static final ColumnDefinition COL_SMALLINT = new ColumnDefinition("col_smallint", "colSmallint", "java.lang.Short", "int2", 5, 5, 1, false, false, null, null, false, false);
        public static final ColumnDefinition COL_SMALLSERIAL = new ColumnDefinition("col_smallserial", "colSmallserial", "java.lang.Short", "smallserial", 5, 5, 2, false, true, null, null, false, false);
        public static final ColumnDefinition COL_INTEGER = new ColumnDefinition("col_integer", "colInteger", "java.lang.Integer", "int4", 4, 10, 3, false, false, null, null, false, false);
        public static final ColumnDefinition COL_SERIAL = new ColumnDefinition("col_serial", "colSerial", "java.lang.Integer", "serial", 4, 10, 4, false, true, null, null, false, false);
        public static final ColumnDefinition COL_BIGINT = new ColumnDefinition("col_bigint", "colBigint", "java.lang.Long", "int8", -5, 19, 5, false, false, null, null, false, false);
        public static final ColumnDefinition COL_BIGSERIAL = new ColumnDefinition("col_bigserial", "colBigserial", "java.lang.Long", "bigserial", -5, 19, 6, false, true, null, null, false, false);
        public static final ColumnDefinition COL_REAL = new ColumnDefinition("col_real", "colReal", "java.lang.Float", "float4", 7, 8, 7, false, false, null, null, false, false);
        public static final ColumnDefinition COL_DOUBLE_PRECISION = new ColumnDefinition("col_double_precision", "colDoublePrecision", "java.lang.Double", "float8", 8, 17, 8, false, false, null, null, false, false);
        public static final ColumnDefinition COL_NUMERIC = new ColumnDefinition("col_numeric", "colNumeric", "java.math.BigDecimal", "numeric", 2, 10, 9, false, false, null, null, false, false);
        public static final ColumnDefinition COL_BOOLEAN = new ColumnDefinition("col_boolean", "colBoolean", "java.lang.Boolean", "bool", -7, 1, 10, false, false, null, null, false, false);
        public static final ColumnDefinition COL_CHAR = new ColumnDefinition("col_char", "colChar", "java.lang.String", "bpchar", 1, 10, 11, false, false, null, null, false, false);
        public static final ColumnDefinition COL_VARCHAR = new ColumnDefinition("col_varchar", "colVarchar", "java.lang.String", "varchar", 12, 50, 12, false, false, null, null, false, false);
        public static final ColumnDefinition COL_TEXT = new ColumnDefinition("col_text", "colText", "java.lang.String", "text", 12, 2147483647, 13, false, false, null, null, false, false);
        public static final ColumnDefinition COL_DATE = new ColumnDefinition("col_date", "colDate", "java.time.LocalDate", "date", 91, 13, 14, false, false, null, null, false, false);
        public static final ColumnDefinition COL_TIME = new ColumnDefinition("col_time", "colTime", "java.time.LocalTime", "time", 92, 15, 15, false, false, null, null, false, false);
        public static final ColumnDefinition COL_TIME_TZ = new ColumnDefinition("col_time_tz", "colTimeTz", "java.time.OffsetTime", "timetz", 92, 21, 16, false, false, null, null, false, false);
        public static final ColumnDefinition COL_TIMESTAMP = new ColumnDefinition("col_timestamp", "colTimestamp", "java.time.LocalDateTime", "timestamp", 93, 29, 17, false, false, null, null, false, false);
        public static final ColumnDefinition COL_TIMESTAMP_TZ = new ColumnDefinition("col_timestamp_tz", "colTimestampTz", "java.time.OffsetDateTime", "timestamptz", 93, 35, 18, false, false, null, null, false, false);
        public static final ColumnDefinition COL_INTERVAL = new ColumnDefinition("col_interval", "colInterval", "java.lang.Long", "interval", 1111, 49, 19, false, false, "make_interval(secs => :{javaPropertyName})", "extract(epoch FROM {columnName}) AS {columnName}", false, false);
        public static final ColumnDefinition COL_BYTEA = new ColumnDefinition("col_bytea", "colBytea", "byte[]", "bytea", -2, 2147483647, 20, false, false, null, null, false, false);
        public static final ColumnDefinition COL_UUID = new ColumnDefinition("col_uuid", "colUuid", "java.util.UUID", "uuid", 1111, 2147483647, 21, false, false, null, null, false, false);
        public static final ColumnDefinition COL_JSONB = new ColumnDefinition("col_jsonb", "colJsonb", "java.lang.String", "jsonb", 1111, 2147483647, 22, false, false, ":{javaPropertyName}::jsonb", null, false, false);
        public static final ColumnDefinition COL_INET = new ColumnDefinition("col_inet", "colInet", "java.lang.String", "inet", 1111, 2147483647, 23, false, false, ":{javaPropertyName}::inet", null, false, false);
        public static final ColumnDefinition COL_CIDR = new ColumnDefinition("col_cidr", "colCidr", "java.lang.String", "cidr", 1111, 2147483647, 24, false, false, ":{javaPropertyName}::cidr", null, false, false);
        public static final ColumnDefinition COL_MACADDR = new ColumnDefinition("col_macaddr", "colMacaddr", "java.lang.String", "macaddr", 1111, 2147483647, 25, false, false, ":{javaPropertyName}::macaddr", null, false, false);
        public static final ColumnDefinition COL_STATUS_ENUM = new ColumnDefinition("col_status_enum", "colStatusEnum", "jp.green_code.spring_jdbc_codegen.test_app.StatusEnum", "status_enum", 12, 2147483647, 26, false, false, ":{javaPropertyName}::status_enum", null, false, false);

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

    /** 他テーブルのカラム、重複指定、PK 指定を弾く */
    protected List<ColumnDefinition> validateColumns(ColumnDefinition first, ColumnDefinition[] rest, boolean rejectPk) {
        var columns = new ArrayList<ColumnDefinition>();
        columns.add(first);
        columns.addAll(Arrays.asList(rest));
        var names = new HashSet<String>();
        for (var c : columns) {
            if (rejectPk && c.getPrimaryKeySeq() != null) {
                throw new IllegalArgumentException("PK は指定できません: " + c.getColumnName());
            }
            if (Columns.MAP.get(c.getColumnName()) != c) {
                throw new IllegalArgumentException("all_types_as_pk のカラムではありません: " + c.getColumnName());
            }
            if (!names.add(c.getColumnName())) {
                throw new IllegalArgumentException("カラムが重複しています: " + c.getColumnName());
            }
        }
        return columns;
    }

    protected List<String> toInsertColumns(Set<String> excludeColumns) {
        return Columns.MAP.values().stream()
                .filter(c -> !excludeColumns.contains(c.getColumnName()))
                .map(c -> "\"%s\"".formatted(c.getColumnName()))
                .toList();
    }

    protected List<String> toInsertValues(Set<String> excludeColumns) {
        return Columns.MAP.values().stream()
                .filter(c -> !excludeColumns.contains(c.getColumnName()))
                .map(ColumnDefinition::toParamColumn)
                .toList();
    }

    protected Set<String> toInsertReturning(Set<String> excludeColumns) {
        var res = new LinkedHashSet<String>();
        for (var c : Columns.MAP.values()) {
            if (c.isReturning() || excludeColumns.contains(c.getColumnName())) {
                res.add(c.getColumnName());
            }
        }
        return res;
    }

    protected void copyReturningValues(AllTypesAsPkEntity entity, AllTypesAsPkEntity returning, Set<String> returningColumns) {
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
        if (returningColumns.contains("col_jsonb")) {
            entity.setColJsonb(returning.getColJsonb());
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
        if (returningColumns.contains("col_status_enum")) {
            entity.setColStatusEnum(returning.getColStatusEnum());
        }
    }

    protected int execWithReturning(List<String> sql, Map<String, Object> param, AllTypesAsPkEntity entity, Set<String> returningColumns) {
        if (returningColumns.isEmpty()) {
            return this.helper.exec(sql, param);
        }
        var returningClause = returningColumns.stream().map(c -> Objects.requireNonNull(Columns.MAP.get(c), "Unknown column " + c).toSelectColumn()).collect(joining(", "));
        sql.add("returning %s".formatted(returningClause));
        var ret = this.helper.optional(sql, param, AllTypesAsPkEntity.class);
        ret.ifPresent(r -> copyReturningValues(entity, r, returningColumns));
        return ret.isPresent() ? 1 : 0;
    }

    /** 全カラムをINSERT 対象とする */
    public int insertAllColumns(AllTypesAsPkEntity entity) {
        return doInsert(entity, Set.of());
    }

    /** 指定したカラムをINSERT 対象から外し、DB の既定値を使う */
    public int insertExcept(AllTypesAsPkEntity entity, ColumnDefinition first, ColumnDefinition... rest) {
        var exclude = new LinkedHashSet<String>();
        validateColumns(first, rest, false).forEach(c -> exclude.add(c.getColumnName()));
        return doInsert(entity, exclude);
    }

    protected int doInsert(AllTypesAsPkEntity entity, Set<String> excludeColumns) {
        var sql = new ArrayList<String>();
        sql.add("insert into \"all_types_as_pk\"");
        var insertColumns = toInsertColumns(excludeColumns);
        if (insertColumns.isEmpty()) {
            sql.add("DEFAULT VALUES");
        } else {
            sql.add("(%s)".formatted(join(", ", insertColumns)));
            sql.add("values (%s)".formatted(join(", ", toInsertValues(excludeColumns))));
        }
        var param = entityToParam(entity);
        return execWithReturning(sql, param, entity, toInsertReturning(excludeColumns));
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
        param.put("colStatusEnum", entity.getColStatusEnum() == null ? null : entity.getColStatusEnum().name());
        return param;
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
        __param.put("colStatusEnum", colStatusEnum == null ? null : colStatusEnum.name());

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
        __param.put("colStatusEnum", colStatusEnum == null ? null : colStatusEnum.name());

        return this.helper.exec(__sql, __param);
    }
}