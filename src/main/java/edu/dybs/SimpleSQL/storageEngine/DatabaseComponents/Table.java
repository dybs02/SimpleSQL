package edu.dybs.SimpleSQL.storageEngine.DatabaseComponents;

import edu.dybs.SimpleSQL.exceptions.DatabaseManagementException;
import edu.dybs.SimpleSQL.queryEngine.AST;
import edu.dybs.SimpleSQL.queryEngine.Token;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Table {
    String name;
    File file;
    ArrayList<AST.columnDefinition> columnDefinitions;
    private final int LINE_WIDTH = 20;

    /**
     * Create new table
     */
    public Table(Path path, String name, ArrayList<AST.columnDefinition> columnDefinitions) throws DatabaseManagementException {
        this.name = name;
        path = path.resolve(name);
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

        appendLine(columnNames.toString());
        appendLine(columnTypes.toString());
    }

    /**
     * Load existing table
     */
    public Table(Path path) throws DatabaseManagementException {
        this.name = String.valueOf(path.getFileName()).split("[.]")[0];
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

    public void insert(ArrayList<String> columnNames, ArrayList<AST.valueDefinition> valueDefinitions) throws DatabaseManagementException {
        ArrayList<String> lines = new ArrayList<>();

        if (columnNames.isEmpty()) {
            for (AST.columnDefinition cd : columnDefinitions) {
                columnNames.add(cd.name);
            }
        }
        String template = getColumnStringTemplate((ArrayList<String>) columnNames.clone());

        for (AST.valueDefinition valueDef : valueDefinitions) {
            if (valueDef.valueFields.size() != columnNames.size()) {
                throw new DatabaseManagementException("Invalid number of values to insert", this.name);
            }

            lines.add(populateTemplate(template, valueDef));
        }

        appendLines(lines);
    }

    private String getColumnStringTemplate(ArrayList<String> columnNames) throws DatabaseManagementException {
        String[] template = new String[columnDefinitions.size()];
        Arrays.fill(template, ";");
        int columnNamesSize = columnNames.size();

        for (int i = 0; i < columnDefinitions.size(); i++) {
            String cdName = columnDefinitions.get(i).name;
            if (columnNames.contains(cdName)) {
                template[i] = "{" + (columnNamesSize - columnNames.size()) + "};";
                columnNames.remove(cdName);
            }
        }

        if (!columnNames.isEmpty()) {
            throw new DatabaseManagementException("Invalid column name: " + columnNames, this.name);
        }

        return String.join("", template);
    }

    private String populateTemplate(String template, AST.valueDefinition valueDef) {
        int i = 0;

        for (String value : valueDef.valueFields) {
            template = template.replace("{" + i + "}", value); // TODO check type

            i++;
        }

        return template;
    }

    public void select(ArrayList<String> columnNames, AST.whereCondition condition) throws DatabaseManagementException {
        ArrayList<Integer> columnIndexes = getColumnIndexes(columnNames);

        try {
            BufferedReader reader = new BufferedReader(new FileReader(this.file));
            String line = reader.readLine();

            while (line != null) {
                String row = buildRowLine(line, columnIndexes, condition);
                if (row != null) {
                    System.out.println(row);
                }
                line = reader.readLine();
            }
        } catch (IOException e) {
            throw new DatabaseManagementException("Unable to access file: " + this.file.getName(), this.name);
        }
    }

    private ArrayList<Integer> getColumnIndexes(ArrayList<String> columnNames) throws DatabaseManagementException {
        ArrayList<Integer> columnIndexes = new ArrayList<>();

        for (int i = 0; i < columnDefinitions.size(); i++) {
            String cdName = columnDefinitions.get(i).name;
            if (columnNames.contains(cdName)) {
                columnIndexes.add(i);
                columnNames.remove(cdName);
            }
        }

        if (!columnNames.isEmpty()) {
            throw new DatabaseManagementException("Invalid column name: " + columnNames, this.name);
        }
        return columnIndexes;
    }

    private String buildRowLine(String line, ArrayList<Integer> columnIndexes, AST.whereCondition condition) throws DatabaseManagementException {
        int conditionIndex = -1;
        if (condition != null) {
            conditionIndex = getColumnIndexes(new ArrayList<>(Arrays.asList(condition.columnName))).get(0);
        }

        String[] values = line.split(";", -1);
        StringBuilder row = new StringBuilder();

        for (int i = 0; i < columnDefinitions.size(); i++) {
            if (columnIndexes.contains(i) || columnIndexes.isEmpty()) {
                if (conditionIndex == i && !checkCondition(values[i], condition)) {
                    return null;
                }
                row.append(values[i]);
                row.append(" ".repeat(Math.max(LINE_WIDTH - values[i].length(), 0)));
            }
        }

        return row.toString();
    }

    private boolean checkCondition(String columnValue, AST.whereCondition condition) {
        switch (condition.operator) {
            case "=":
                return columnValue.equals(condition.value);
            case "<":
                return columnValue.compareTo(condition.value) > 0;
            case ">":
                return columnValue.compareTo(condition.value) < 0;
        }
        return false;
    }

    private void appendLines(ArrayList<String> lines) throws DatabaseManagementException {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));

            for (String line : lines) {
                writer.append(line + "\n");
            }

            writer.close();
        } catch (IOException e) {
            throw new DatabaseManagementException("Unable to access file: " + this.file.getName(), this.name);
        }
    }

    private void appendLine(String line) throws DatabaseManagementException {
        ArrayList<String> l = new ArrayList<>();
        l.add(line);
        appendLines(l);
    }

    private void readLine() {

    }
}