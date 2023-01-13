package main.java.parser;

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
        public ArrayList<Node> columns;
        public createTableStatement(String tableName, ArrayList<Node> columns) {
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

}
