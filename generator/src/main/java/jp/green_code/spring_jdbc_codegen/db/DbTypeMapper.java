package jp.green_code.spring_jdbc_codegen.db;

import java.util.HashMap;
import java.util.Map;

import static jp.green_code.spring_jdbc_codegen.db.JavaType.BIG_DECIMAL;
import static jp.green_code.spring_jdbc_codegen.db.JavaType.BOOLEAN;
import static jp.green_code.spring_jdbc_codegen.db.JavaType.BOX;
import static jp.green_code.spring_jdbc_codegen.db.JavaType.BYTE_ARRAY;
import static jp.green_code.spring_jdbc_codegen.db.JavaType.CIDR;
import static jp.green_code.spring_jdbc_codegen.db.JavaType.CIRCLE;
import static jp.green_code.spring_jdbc_codegen.db.JavaType.DOUBLE;
import static jp.green_code.spring_jdbc_codegen.db.JavaType.DURATION;
import static jp.green_code.spring_jdbc_codegen.db.JavaType.FLOAT;
import static jp.green_code.spring_jdbc_codegen.db.JavaType.INET;
import static jp.green_code.spring_jdbc_codegen.db.JavaType.INTEGER;
import static jp.green_code.spring_jdbc_codegen.db.JavaType.JSON;
import static jp.green_code.spring_jdbc_codegen.db.JavaType.LINE;
import static jp.green_code.spring_jdbc_codegen.db.JavaType.LOCAL_DATE;
import static jp.green_code.spring_jdbc_codegen.db.JavaType.LOCAL_DATE_TIME;
import static jp.green_code.spring_jdbc_codegen.db.JavaType.LOCAL_TIME;
import static jp.green_code.spring_jdbc_codegen.db.JavaType.LONG;
import static jp.green_code.spring_jdbc_codegen.db.JavaType.LSEG;
import static jp.green_code.spring_jdbc_codegen.db.JavaType.MACADDR;
import static jp.green_code.spring_jdbc_codegen.db.JavaType.OFFSET_DATE_TIME;
import static jp.green_code.spring_jdbc_codegen.db.JavaType.OFFSET_TIME;
import static jp.green_code.spring_jdbc_codegen.db.JavaType.PATH;
import static jp.green_code.spring_jdbc_codegen.db.JavaType.POINT;
import static jp.green_code.spring_jdbc_codegen.db.JavaType.POLYGON;
import static jp.green_code.spring_jdbc_codegen.db.JavaType.SHORT;
import static jp.green_code.spring_jdbc_codegen.db.JavaType.STRING;
import static jp.green_code.spring_jdbc_codegen.db.JavaType.UUID;
import static jp.green_code.spring_jdbc_codegen.db.JavaType.XML;

public final class DbTypeMapper {

    private static final Map<String, JavaType> MAP = new HashMap<>();

    static {
        MAP.put("bpchar", STRING);
        MAP.put("varchar", STRING);
        MAP.put("text", STRING);
        MAP.put("uuid", UUID);
        MAP.put("xml", XML);
        MAP.put("json", JSON);
        MAP.put("jsonb", JSON);

        MAP.put("bool", BOOLEAN);
        MAP.put("int2", SHORT);
        MAP.put("smallserial", SHORT);
        MAP.put("serial", INTEGER);
        MAP.put("int4", INTEGER);
        MAP.put("bigserial", LONG);
        MAP.put("int8", LONG);
        MAP.put("float4", FLOAT);
        MAP.put("float8", DOUBLE);
        MAP.put("numeric", BIG_DECIMAL);

        MAP.put("time", LOCAL_TIME);
        MAP.put("date", LOCAL_DATE);
        MAP.put("timestamp", LOCAL_DATE_TIME);
        MAP.put("timetz", OFFSET_TIME);
        MAP.put("timestamptz", OFFSET_DATE_TIME);
        MAP.put("interval", DURATION);

        MAP.put("inet", INET);
        MAP.put("cidr", CIDR);
        MAP.put("macaddr", MACADDR);

        MAP.put("point", POINT);
        MAP.put("line", LINE);
        MAP.put("box", BOX);
        MAP.put("lseg", LSEG);
        MAP.put("path", PATH);
        MAP.put("polygon", POLYGON);
        MAP.put("circle", CIRCLE);

        MAP.put("bytea", BYTE_ARRAY);
    }

    public static JavaType map(String dbTypeName) {
        if (dbTypeName == null) throw new RuntimeException("dbTypeName is null ");
        var result = MAP.get(dbTypeName.toLowerCase());
        if (result == null) throw new RuntimeException("対応できない型です " + dbTypeName);
        return result;
    }

    public static void put(String dbType, JavaType javaType) {
        MAP.put(dbType, javaType);
    }
}
