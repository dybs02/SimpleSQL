package edu.dybs.SimpleSQL.exceptions;

public class DatabaseManagementException extends Exception {
    public String tableName;

    public DatabaseManagementException(String msg) {
        super(msg);
        this.tableName = "";
    }

    public DatabaseManagementException(String msg, String tableName) {
        super(msg);
        this.tableName = tableName;
    }
}
