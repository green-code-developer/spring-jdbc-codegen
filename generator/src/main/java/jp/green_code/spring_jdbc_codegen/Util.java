package jp.green_code.spring_jdbc_codegen;

import jp.green_code.spring_jdbc_codegen.db.TableDefinition;

import java.util.List;

public class Util {

    public static String toCamelCase(String snake, boolean upperFirst) {
        StringBuilder sb = new StringBuilder();
        boolean upper = upperFirst;

        for (char c : snake.toCharArray()) {
            if (c == '_' || c == '-') {
                upper = true;
            } else {
                sb.append(upper ? Character.toUpperCase(c) : Character.toLowerCase(c));
                upper = false;
            }
        }
        return sb.toString();
    }

    public static void dumpTableDefinitions(List<TableDefinition> tables) {
        tables.forEach(t -> {
            System.out.println(t.tableName + " " + t.toEntityClassName());
            t.columns.forEach(c ->
                    System.out.println("  " + c.toLogString())
            );
        });
    }
}
