package jp.green_code.spring_jdbc_codegen.db;

public record JavaType(
        /* Java の型 Full Qualified Class Name */
        String fqcn,
        /* entity のフィールドをJDBC のパラメータとして渡す際の型変換コード。省略時は変換なし */
        String javaCastSnippetInEntityToParam,
        /* Insert Update 時に行うキャストするコード。省略時は :%s (%sはJavaフィールド名)  */
        String dbParamTemplate,
        /* Select 時に行うキャストするコード。省略時は %s (%sはカラム名)  */
        String dbSelectTemplate,
        /* テストデータを自動作成するコード。テストに必要な場合は省略不可 */
        String generateDateSnippet,
        /* テストのassertion コード。省略時は assertEquals() となる */
        String assertSnippet) {

    // param.yml enumJavaTypeMappings から読み込む場合
    public JavaType(String fqcn, String dbParamTemplate) {
        this(fqcn, "String.valueOf({value})", dbParamTemplate, null, "return pickBySeed(%s.class, seed);".formatted(fqcn), null);
    }

    // 文字列
    public static final JavaType STRING = new JavaType("java.lang.String", null, null, null, "return String.valueOf(seed);", "assertEquals(expected, value.trim());");
    public static final JavaType UUID = new JavaType("java.util.UUID", null, null, null, "return UUID.fromString(\"9529478b-20d7-4232-ba79-\"+String.format(\"%012d\", seed));", null);
    public static final JavaType XML = new JavaType("java.lang.String", null, ":{javaPropertyName}::xml", null, "return \"<xml>%s</xml>\".formatted(seed);", null);
    public static final JavaType JSON = new JavaType("java.lang.String", "String.valueOf({value})", ":{javaPropertyName}::jsonb", null, "return \"{\\\"id\\\": %d}\".formatted(seed);", null);

    // 数字
    public static final JavaType BOOLEAN = new JavaType("java.lang.Boolean", null, null, null, "return seed %2 == 0;", null);
    public static final JavaType SHORT = new JavaType("java.lang.Short", null, null, null, "return (short) seed;", null);
    public static final JavaType INTEGER = new JavaType("java.lang.Integer", null, null, null, "return seed;", null);
    public static final JavaType LONG = new JavaType("java.lang.Long", null, null, null, "return (long) seed;", null);
    public static final JavaType FLOAT = new JavaType("java.lang.Float", null, null, null, "return (float) seed;", null);
    public static final JavaType DOUBLE = new JavaType("java.lang.Double", null, null, null, "return (double) seed;", null);
    public static final JavaType BIG_DECIMAL = new JavaType("java.math.BigDecimal", null, null, null, "return BigDecimal.valueOf(seed);", "assertEquals(0, expected.compareTo(value));");

    // 時間
    public static final JavaType LOCAL_TIME = new JavaType("java.time.LocalTime", null, null, null, "return LocalTime.of(0, 0, 0).plusMinutes(seed);", null);
    public static final JavaType LOCAL_DATE = new JavaType("java.time.LocalDate", null, null, null, "return LocalDate.of(2001, 1, 1).plusDays(seed);", null);
    public static final JavaType LOCAL_DATE_TIME = new JavaType("java.time.LocalDateTime", null, null, null, "return LocalDateTime.of(2001, 1, 1, 0, 0, 0).plusMinutes(seed);", null);
    public static final JavaType OFFSET_TIME = new JavaType("java.time.OffsetTime", null, null, null, "return OffsetTime.of(0, 0, 0, 0, java.time.ZoneOffset.UTC).plusMinutes(seed);", null);
    public static final JavaType OFFSET_DATE_TIME = new JavaType("java.time.OffsetDateTime", null, null, null, "return OffsetDateTime.of(2001, 1, 1, 0, 0, 0, 0, java.time.ZoneOffset.UTC).plusMinutes(seed);", null);
    public static final JavaType DURATION = new JavaType("java.lang.Long", null, "make_interval(secs => :{javaPropertyName})", "extract(epoch FROM {columnName}) AS {columnName}", "return (long) seed;", null);

    // ネットワーク
    public static final JavaType INET = new JavaType("java.lang.String", null, ":{javaPropertyName}::inet", null, "return String.format(\"%d.%d.%d.%d\", (seed >> 24) & 0xFF, (seed >> 16) & 0xFF, (seed >> 8) & 0xFF, seed & 0xFF);", null);
    public static final JavaType CIDR = new JavaType("java.lang.String", null, ":{javaPropertyName}::cidr", null, "return String.format(\"%d.%d.%d.%d/32\", (seed >> 24) & 0xFF, (seed >> 16) & 0xFF, (seed >> 8) & 0xFF, seed & 0xFF);", null);
    public static final JavaType MACADDR = new JavaType("java.lang.String", null, ":{javaPropertyName}::macaddr", null, "return String.format(\"00:00:%02x:%02x:%02x:%02x\", (seed >> 24) & 0xFF, (seed >> 16) & 0xFF, (seed >> 8) & 0xFF, seed & 0xFF);", null);

    // 幾何
    public static final JavaType POINT = new JavaType("java.lang.String", null, ":{javaPropertyName}::point", "{columnName}::text", "return \"(0,%d)\".formatted(seed);", null);
    public static final JavaType LINE = new JavaType("java.lang.String", null, ":{javaPropertyName}::line", "{columnName}::text", "return \"{1,-1,%d}\".formatted(seed);", null);
    public static final JavaType BOX = new JavaType("java.lang.String", null, ":{javaPropertyName}::box", "{columnName}::text", "return \"(1,%d),(0,0)\".formatted(seed);", null);
    public static final JavaType LSEG = new JavaType("java.lang.String", null, ":{javaPropertyName}::lseg", "{columnName}::text", "return \"[(1,%d),(0,0)]\".formatted(seed);", null);
    public static final JavaType PATH = new JavaType("java.lang.String", null, ":{javaPropertyName}::path", "{columnName}::text", "return \"((2,%d),(1,1),(0,0))\".formatted(seed);", null);
    public static final JavaType POLYGON = new JavaType("java.lang.String", null, ":{javaPropertyName}::polygon", "{columnName}::text", "return \"((2,%d),(1,1),(0,0))\".formatted(seed);", null);
    public static final JavaType CIRCLE = new JavaType("java.lang.String", null, ":{javaPropertyName}::circle", "{columnName}::text", "return \"<(0,0),%d>\".formatted(seed);", null);

    // バイト配列
    public static final JavaType BYTE_ARRAY = new JavaType("byte[]", null, null, null, "return new byte[]{(byte)(seed), (byte)(seed >> 8), (byte)(seed >> 16), (byte)(seed >> 24)};", "org.junit.jupiter.api.Assertions.assertArrayEquals(expected, value);");
}
