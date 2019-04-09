package com.exple.qe;

public class Data {
    private String databaseName;
    private String table;
    private String keyColumn;
    private String keyValue;
    private String column;
    private String value;
    private String message;

    public Data(String databaseName, String table, String keyColumn, String keyValue, String column, String value, String message) {
        this.databaseName = databaseName;
        this.table = table;
        this.keyColumn = keyColumn;
        this.keyValue = keyValue;
        this.column = column;
        this.value = value;
        this.message = message;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getKeyColumn() {
        return keyColumn;
    }

    public void setKeyColumn(String keyColumn) {
        this.keyColumn = keyColumn;
    }

    public String getKeyValue() {
        return keyValue;
    }

    public void setKeyValue(String keyValue) {
        this.keyValue = keyValue;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return databaseName +
                "\t" + table  +
                "\t" + keyColumn  +
                "\t" + keyValue  +
                "\t" + column +
                "\t" + value +
                "\t" + message +"\n";
    }
}
