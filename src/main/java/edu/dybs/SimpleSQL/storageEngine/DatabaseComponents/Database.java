package edu.dybs.SimpleSQL.storageEngine.DatabaseComponents;

import edu.dybs.SimpleSQL.exceptions.DatabaseManagementException;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;

public class Database {
    private String name;
    private HashMap<String, Table> tables;
    private Path path;

    /**
     * Create new Database
     */
    public Database(Path path, String name) throws DatabaseManagementException {
        this.name = name;
        this.tables = new HashMap<>();
        this.path = path.resolve(name);

        File dir = this.path.toFile();
        if (!dir.mkdirs()) {
            throw new DatabaseManagementException("Database already exists");
        }
    }

    /**
     * Load existing Database
     */
    public Database(Path path) throws DatabaseManagementException {
        this.path = path;
        this.name = String.valueOf(path.getFileName());

        if (!this.path.toFile().exists()) {
            throw new DatabaseManagementException("Database does not exist");
        }

        this.tables = new HashMap<>();
        File[] files = path.toFile().listFiles();
        if (files != null) {
            for (File file : files) {
                tables.put(
                        file.getName(),
                        new Table(file.toPath())
                );
            }
        }
    }

    public Path getPath() {
        return this.path;
    }

    public void addTable(Table table) {
        tables.put(table.name, table);
    }

    public Table getTable(String name) {
        return tables.get(name);
    }

    public String getName() {
        return name;
    }
}