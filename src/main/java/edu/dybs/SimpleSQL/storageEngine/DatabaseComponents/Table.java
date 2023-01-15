package edu.dybs.SimpleSQL.storageEngine.DatabaseComponents;

import edu.dybs.SimpleSQL.exceptions.DatabaseManagementException;
import edu.dybs.SimpleSQL.queryEngine.AST;
import edu.dybs.SimpleSQL.queryEngine.Token;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Table {
    String name;
    File file;
    ArrayList<AST.columnDefinition> columnDefinitions;

    /**
     * Create new table
     */
    public Table(Path path, String name, ArrayList<AST.columnDefinition> columnDefinitions) throws DatabaseManagementException {
        this.name = name;
        this.file = new File(path.toString() + ".txt");
        this.columnDefinitions = columnDefinitions;

        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new DatabaseManagementException("Table already exists", this.name);
        }

        StringBuilder columnNames = new StringBuilder();
        StringBuilder columnTypes = new StringBuilder();

        for (AST.columnDefinition column : columnDefinitions) {
            columnNames.append(column.name).append(";");
            columnTypes.append(column.dataType.name()).append(";");
        }

        try {
            appendLine(columnNames.toString());
            appendLine(columnTypes.toString());
        } catch (IOException e) {
            throw new DatabaseManagementException("Unable to access file: " + this.file.getName(), this.name);
        }
    }

    /**
     * Load existing table
     */
    public Table(Path path) throws DatabaseManagementException {
        this.name = String.valueOf(path.getFileName());
        this.file = new File(path.toString());

        ArrayList<String> columnNames;
        ArrayList<String> columnTypes;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(this.file));
            columnNames = new ArrayList<>(List.of(reader.readLine().strip().split(";")));
            columnTypes = new ArrayList<>(List.of(reader.readLine().strip().split(";")));
        } catch (IOException e) {
            throw new DatabaseManagementException("Unable to access file: " + this.file.getName(), this.name);
        }

        columnDefinitions = new ArrayList<>();
        for (int i = 0; i < columnNames.size(); i++) {
            columnDefinitions.add(new AST.columnDefinition(
                    columnNames.get(i),
                    columnTypes.get(i).equals("INT") ? Token.Type.INT : Token.Type.VARCHAR
            ));
        }
    }

    private void appendLines(ArrayList<String> lines) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));

        for (String line : lines) {
            writer.append(line + "\n");
        }

        writer.close();
    }

    private void appendLine(String line) throws IOException {
        ArrayList<String> l = new ArrayList<>();
        l.add(line);
        appendLines(l);
    }

    private void readLine() {

    }
}