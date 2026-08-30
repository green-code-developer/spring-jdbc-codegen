package jp.green_code.spring_jdbc_codegen.test_app.repository.base;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.lang.String.join;

public abstract class BaseRepositoryHelper {
    public final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public BaseRepositoryHelper(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(new JdbcTemplate(dataSource));
    }

    public <T> List<T> list(List<String> sql, Map<String, ?> param, Class<T> clazz) {
        return list(join(" ", sql), param, clazz);
    }

    public <T> List<T> list(String sql, Map<String, ?> param, Class<T> clazz) {
        if (clazz.isPrimitive() || Number.class.isAssignableFrom(clazz) || clazz == String.class) {
            return namedParameterJdbcTemplate.queryForList(sql, param, clazz);
        } else {
            return list(sql, param, new BeanPropertyRowMapper<>(clazz));
        }
    }

    public <T> List<T> list(List<String> sql, Map<String, ?> param, RowMapper<T> mapper) {
        return list(join(" ", sql), param, mapper);
    }

    public <T> List<T> list(String sql, Map<String, ?> param, RowMapper<T> mapper) {
        return namedParameterJdbcTemplate.query(sql, param, mapper);
    }

    public int exec(List<String> sql, Map<String, ?> param) {
        return exec(join(" ", sql), param);
    }

    public int exec(String sql, Map<String, ?> param) {
        return namedParameterJdbcTemplate.update(sql, param);
    }

    public <T> T single(List<String> sql, Map<String, ?> param, Class<T> clazz) {
        return single(join(" ", sql), param, clazz);
    }

    public <T> T single(String sql, Map<String, ?> param, Class<T> clazz) {
        if (clazz.isPrimitive() || Number.class.isAssignableFrom(clazz) || clazz == String.class) {
            return Optional.ofNullable(
                    namedParameterJdbcTemplate.queryForObject(sql, param, clazz)
            ).orElseThrow();
        } else {
            return single(sql, param, new BeanPropertyRowMapper<>(clazz));
        }
    }

    public <T> T single(List<String> sql, Map<String, ?> param, RowMapper<T> mapper) {
        return single(join(" ", sql), param, mapper);
    }

    public <T> T single(String sql, Map<String, ?> param, RowMapper<T> mapper) {
        return Optional.ofNullable(
                namedParameterJdbcTemplate.queryForObject(sql, param, mapper)
        ).orElseThrow();
    }

    public <T> Optional<T> optional(List<String> sql, Map<String, ?> param, Class<T> clazz) {
        return list(sql, param, clazz).stream().findFirst();
    }

    public <T> Optional<T> optional(String sql, Map<String, ?> param, Class<T> clazz) {
        return list(sql, param, clazz).stream().findFirst();
    }

    public <T> Optional<T> optional(List<String> sql, Map<String, ?> param, RowMapper<T> mapper) {
        return list(join(" ", sql), param, mapper).stream().findFirst();
    }

    public <T> Optional<T> optional(String sql, Map<String, ?> param, RowMapper<T> mapper) {
        return list(sql, param, mapper).stream().findFirst();
    }

    public long count(List<String> sql, Map<String, ?> param) {
        return count(join(" ", sql), param);
    }

    public long count(String sql, Map<String, ?> param) {
        return optional(sql, param, Long.class).orElseThrow();
    }

    public static <E extends Enum<E>> E pickBySeed(Class<E> enumClass, int seed) {
        E[] values = enumClass.getEnumConstants();
        int index = Math.floorMod(seed, values.length);
        return values[index];
    }

    public static String escapeLike(String s) {
        return escapeLike(s, '\\');
    }

    public static String escapeLike(String s, char escapeChar) {
        if (escapeChar == '%' || escapeChar == '_') {
            throw new IllegalArgumentException("エスケープ文字にワイルドカードは指定できません: " + escapeChar);
        }
        String e = String.valueOf(escapeChar);
        // エスケープ文字自身を先に置換しないと、後続の置換で二重にエスケープされる
        return s.replace(e, e + e)
                .replace("%", e + "%")
                .replace("_", e + "_");
    }
}
