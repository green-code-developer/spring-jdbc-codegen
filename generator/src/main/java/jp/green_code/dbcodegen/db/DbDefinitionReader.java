package jp.green_code.dbcodegen.db;

import org.apache.commons.lang3.Strings;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import static java.sql.DatabaseMetaData.columnNullable;
import static jp.green_code.dbcodegen.DbCodeGenParameter.param;

public class DbDefinitionReader {

    public List<TableDefinition> readDefinition() throws Exception {
        var result = new ArrayList<TableDefinition>();
        try (Connection conn = DriverManager.getConnection(param.jdbcUrl, param.jdbcUser, param.jdbcPass)) {
            var meta = conn.getMetaData();
            var rs = meta.getTables(null, param.jdbcSchema, "%", new String[]{"TABLE"});
            while (rs.next()) {
                var table = readTableDefinition(meta, rs);
                if (table != null) {
                    result.add(table);
                }
            }
        }
        return result;
    }

    TableDefinition readTableDefinition(DatabaseMetaData meta, ResultSet tableRs) throws Exception {
        var table = new TableDefinition(param);
        table.tableName = tableRs.getString("TABLE_NAME");
        if (param.excludedTableNames.contains(table.tableName)) {
            return null;
        }
        int index = 0;
        try (ResultSet columnRs = meta.getColumns(null, param.jdbcSchema, table.tableName, null)) {
            while (columnRs.next()) {
                index++;
                var columnDef = readColumnDefinition(table.tableName, columnRs);
                table.columns.add(columnDef);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to read column definition: table=%s columnIndex=%d".formatted(table.tableName, index), e);
        }
        index = 0;
        try (ResultSet pkRs = meta.getPrimaryKeys(null, param.jdbcSchema, table.tableName)) {
            while (pkRs.next()) {
                index++;
                readPKDefinition(table, pkRs);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to read pk definition: table=%s columnIndex=%d".formatted(table.tableName, index), e);
        }
        return table;
    }

    ColumnDefinition readColumnDefinition(String tableName, ResultSet columnRs) throws Exception {
        var result = new ColumnDefinition();
        result.tableName = tableName;
        result.columnName = columnRs.getString("COLUMN_NAME");
        result.dbTypeName = columnRs.getString("TYPE_NAME");
        result.jdbcType = columnRs.getInt("DATA_TYPE");
        result.columnSize = columnRs.getInt("COLUMN_SIZE");
        result.nullable = columnRs.getInt("NULLABLE") == columnNullable;
        result.defaultExpression = columnRs.getString("COLUMN_DEF");
        try {
            // Java に変換できない場合は異常終了させる
            result.toJavaType();
        } catch (Exception e) {
            throw new RuntimeException("Unknown Java type (%s) column:%s".formatted(result.dbTypeName, result.columnName), e);
        }
        return result;
    }

    void readPKDefinition(TableDefinition tableDef, ResultSet pkRs) throws Exception {
        var columnName = pkRs.getString("COLUMN_NAME");
        // PK のカラムがとれないことはないはず
        var columnDef = tableDef.columns.stream().filter(c -> Strings.CI.equals(c.columnName, columnName)).findFirst().orElseThrow();
        columnDef.primaryKeySeq = pkRs.getShort("KEY_SEQ");
        columnDef.primaryKeyName = pkRs.getString("PK_NAME");
    }
}
