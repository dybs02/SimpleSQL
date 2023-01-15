package edu.dybs.SimpleSQL.storageEngine;


import edu.dybs.SimpleSQL.exceptions.DatabaseManagementException;
import edu.dybs.SimpleSQL.queryEngine.AST;
import edu.dybs.SimpleSQL.storageEngine.DatabaseComponents.Database;
import edu.dybs.SimpleSQL.storageEngine.DatabaseComponents.Table;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class DatabaseHandler {
    private static Database currentDB;
    private static final String DB_STORAGE_PATH = "databases";

    public static void loadDatabase(String databaseName) throws DatabaseManagementException{
        Path path = Paths.get(DB_STORAGE_PATH, databaseName);
        currentDB = new Database(path);
    }

    public static void createDatabase(String databaseName) throws DatabaseManagementException {
        Path path = Paths.get(DB_STORAGE_PATH);
        currentDB = new Database(path, databaseName);
    }

    public static void createTable(String tableName, ArrayList<AST.columnDefinition> columns) throws DatabaseManagementException {
        Table newTable = new Table(currentDB.getPath(), tableName, columns);
        currentDB.addTable(newTable);
    }
}
