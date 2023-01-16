package edu.dybs.SimpleSQL.queryEngine;

import java.util.ArrayList;

public class AST {
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

    public static class columnDefinition extends Node {
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

    public static class valueDefinition extends Node {
        public ArrayList<String> valueFields;

        public valueDefinition(ArrayList<String> valueFields) {
            this.valueFields = valueFields;
        }
    }

    static class selectStatement extends Statement {
        public String tableName;
        public ArrayList<String> columnNames;
        public whereCondition condition;

        public selectStatement(String tableName, ArrayList<String> columnNames, whereCondition condition) {
            this.tableName = tableName;
            this.columnNames = columnNames;
            this.condition = condition;
        }
    }

    public static class whereCondition extends Node {
        public String columnName;
        public String operator;
        public String value;

        public whereCondition(String columnName, String operator, String value) {
            this.columnName = columnName;
            this.operator = operator;
            this.value = value;
        }
    }

    public static class deleteStatement extends Statement {
        public String tableName;
        public whereCondition condition;

        public deleteStatement(String tableName, whereCondition condition) {
            this.tableName = tableName;
            this.condition = condition;
        }
    }
}
