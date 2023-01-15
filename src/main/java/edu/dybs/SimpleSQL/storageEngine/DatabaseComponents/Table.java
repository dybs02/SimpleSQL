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

    public void select(ArrayList<String> columnNames) throws DatabaseManagementException {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(this.file));
            String line = reader.readLine();

            while (line != null) {
                String[] values = line.split(";");

                StringBuilder row = new StringBuilder();
                for (String value : values) {
                    row.append(value).append(" ".repeat(LINE_WIDTH - value.length()));
                }
                System.out.println(row);

                line = reader.readLine();
            }
        } catch (IOException e) {
            throw new DatabaseManagementException("Unable to access file: " + this.file.getName(), this.name);
        }
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