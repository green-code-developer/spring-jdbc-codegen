package jp.green_code.spring_jdbc_codegen.generator;

import jp.green_code.spring_jdbc_codegen.Parameter;

import java.util.ArrayList;

import static java.lang.String.join;

public class BaseHelperGenerator {
    final Parameter param;

    public BaseHelperGenerator(Parameter param) {
        this.param = param;
    }

    public String generateBaseHelper() {
        var sb = new ArrayList<String>();
        sb.add("package %s;".formatted(param.baseRepositoryPackage()));
        sb.add("");
        sb.add("import org.springframework.jdbc.core.BeanPropertyRowMapper;");
        sb.add("import org.springframework.jdbc.core.RowMapper;");
        sb.add("import org.springframework.jdbc.core.JdbcTemplate;");
        sb.add("import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;");
        sb.add("");
        sb.add("import javax.sql.DataSource;");
        sb.add("import java.util.List;");
        sb.add("import java.util.Map;");
        sb.add("import java.util.Optional;");
        sb.add("");
        sb.add("import static java.lang.String.join;");
        sb.add("");
        sb.add("public abstract class %s {".formatted(param.toBaseHelperRepositoryClassName()));
        sb.add("    public final NamedParameterJdbcTemplate namedParameterJdbcTemplate;");
        sb.add("");
        sb.add("    public %s(DataSource dataSource) {".formatted(param.toBaseHelperRepositoryClassName()));
        sb.add("        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(new JdbcTemplate(dataSource));");
        sb.add("    }");
        sb.add("");
        sb.add("    public <T> List<T> list(List<String> sql, Map<String, ?> param, Class<T> clazz) {");
        sb.add("        return list(join(\" \", sql), param, clazz);");
        sb.add("    }");
        sb.add("");
        sb.add("    public <T> List<T> list(String sql, Map<String, ?> param, Class<T> clazz) {");
        sb.add("        if (clazz.isPrimitive() || Number.class.isAssignableFrom(clazz) || clazz == String.class) {");
        sb.add("            return namedParameterJdbcTemplate.queryForList(sql, param, clazz);");
        sb.add("        } else {");
        sb.add("            return list(sql, param, new BeanPropertyRowMapper<>(clazz));");
        sb.add("        }");
        sb.add("    }");
        sb.add("");
        sb.add("    public <T> List<T> list(List<String> sql, Map<String, ?> param, RowMapper<T> mapper) {");
        sb.add("        return list(join(\" \", sql), param, mapper);");
        sb.add("    }");
        sb.add("");
        sb.add("    public <T> List<T> list(String sql, Map<String, ?> param, RowMapper<T> mapper) {");
        sb.add("        return namedParameterJdbcTemplate.query(sql, param, mapper);");
        sb.add("    }");
        sb.add("");
        sb.add("    public int exec(List<String> sql, Map<String, ?> param) {");
        sb.add("        return exec(join(\" \", sql), param);");
        sb.add("    }");
        sb.add("");
        sb.add("    public int exec(String sql, Map<String, ?> param) {");
        sb.add("        return namedParameterJdbcTemplate.update(sql, param);");
        sb.add("    }");
        sb.add("");
        sb.add("    public <T> T single(List<String> sql, Map<String, ?> param, Class<T> clazz) {");
        sb.add("        return single(join(\" \", sql), param, clazz);");
        sb.add("    }");
        sb.add("");
        sb.add("    public <T> T single(String sql, Map<String, ?> param, Class<T> clazz) {");
        sb.add("        if (clazz.isPrimitive() || Number.class.isAssignableFrom(clazz) || clazz == String.class) {");
        sb.add("            return namedParameterJdbcTemplate.queryForObject(sql, param, clazz);");
        sb.add("        } else {");
        sb.add("            return single(sql, param, new BeanPropertyRowMapper<>(clazz));");
        sb.add("        }");
        sb.add("    }");
        sb.add("");
        sb.add("    public <T> T single(List<String> sql, Map<String, ?> param, RowMapper<T> mapper) {");
        sb.add("        return single(join(\" \", sql), param, mapper);");
        sb.add("    }");
        sb.add("");
        sb.add("    public <T> T single(String sql, Map<String, ?> param, RowMapper<T> mapper) {");
        sb.add("        return namedParameterJdbcTemplate.queryForObject(sql, param, mapper);");
        sb.add("    }");
        sb.add("");
        sb.add("    public <T> Optional<T> optional(List<String> sql, Map<String, ?> param, Class<T> clazz) {");
        sb.add("        return list(sql, param, clazz).stream().findFirst();");
        sb.add("    }");
        sb.add("");
        sb.add("    public <T> Optional<T> optional(String sql, Map<String, ?> param, Class<T> clazz) {");
        sb.add("        return list(sql, param, clazz).stream().findFirst();");
        sb.add("    }");
        sb.add("");
        sb.add("    public <T> Optional<T> optional(List<String> sql, Map<String, ?> param, RowMapper<T> mapper) {");
        sb.add("        return list(join(\" \", sql), param, mapper).stream().findFirst();");
        sb.add("    }");
        sb.add("");
        sb.add("    public <T> Optional<T> optional(String sql, Map<String, ?> param, RowMapper<T> mapper) {");
        sb.add("        return list(sql, param, mapper).stream().findFirst();");
        sb.add("    }");
        sb.add("");
        sb.add("    public long count(List<String> sql, Map<String, ?> param) {");
        sb.add("        return count(join(\" \", sql), param);");
        sb.add("    }");
        sb.add("");
        sb.add("    public long count(String sql, Map<String, ?> param) {");
        sb.add("        return optional(sql, param, Long.class).orElseThrow();");
        sb.add("    }");
        sb.add("");
        sb.add("    public static <E extends Enum<E>> E pickBySeed(Class<E> enumClass, int seed) {");
        sb.add("        E[] values = enumClass.getEnumConstants();");
        sb.add("        int index = Math.floorMod(seed, values.length);");
        sb.add("        return values[index];");
        sb.add("    }");
        sb.add("}");
        sb.add("");
        return join("\n", sb);
    }
}
