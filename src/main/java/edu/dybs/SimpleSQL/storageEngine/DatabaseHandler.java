package edu.dybs.SimpleSQL.storageEngine;


import edu.dybs.SimpleSQL.exceptions.DatabaseManagementException;
import edu.dybs.SimpleSQL.queryEngine.AST;
import edu.dybs.SimpleSQL.storageEngine.DatabaseComponents.Database;
import edu.dybs.SimpleSQL.storageEngine.DatabaseComponents.Table;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class DatabaseHandler {
    private static Database currentDB = null;
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

    public static void dropDatabase(String databaseName) throws DatabaseManagementException {
        Path dbPath = Paths.get(DB_STORAGE_PATH, databaseName);
        File db = new File(dbPath.toString());

        if (!Files.exists(dbPath)) {
            throw new DatabaseManagementException("Database does not exist");
        }

        deleteDir(db);
        if (databaseName.equals(currentDB.getName())) {
            currentDB = null;
        }
    }

    private static void deleteDir(File element) {
        if (element.isDirectory()) {
            for (File sub : element.listFiles()) {
                deleteDir(sub);
            }
        }
        element.delete();
    }
}
