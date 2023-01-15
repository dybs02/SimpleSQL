package edu.dybs.SimpleSQL.parser;

import java.util.ArrayList;

class AST {
    static abstract class Node {}
    static abstract class Statement extends Node {}


    static class createDatabaseStatement extends Statement {
        public String databaseName;
        public createDatabaseStatement(String databaseName) {
            this.databaseName = databaseName;
        }
    }

    static class createTableStatement extends Statement {
        public String tableName;
        public ArrayList<columnDefinition> columns;
        public createTableStatement(String tableName, ArrayList<columnDefinition> columns) {
            this.tableName = tableName;
            this.columns = columns;
        }
    }

    static class columnDefinition extends Node {
        public String name;
        public Token.Type dataType;
        public columnDefinition(String name, Token.Type dataType) {
            this.name = name;
            this.dataType = dataType;
        }
    }

    static class dropDatabaseStatement extends Statement {
        public String databaseName;

        public dropDatabaseStatement(String databaseName) {
            this.databaseName = databaseName;
        }
    }

    static class dropTableStatement extends Statement {
        public String tableName;

        public dropTableStatement(String tableName) {
            this.tableName = tableName;
        }
    }

    static class insertStatement extends Statement {
        public String tableName;
        public ArrayList<String> columnNames;
        public ArrayList<valueDefinition> values;

        public insertStatement(String tableName, ArrayList<String> columnNames, ArrayList<valueDefinition> values) {
            this.tableName = tableName;
            this.columnNames = columnNames;
            this.values = values;
        }
    }

    static class valueDefinition extends Node {
        public ArrayList<String> valueFields;

        public valueDefinition(ArrayList<String> valueFields) {
            this.valueFields = valueFields;
        }
    }

    static class selectStatement extends Statement {
        public String tableName;
        public ArrayList<String> columnNames;

        public selectStatement(String tableName, ArrayList<String> columnNames) {
            this.tableName = tableName;
            this.columnNames = columnNames;
        }
    }
}
