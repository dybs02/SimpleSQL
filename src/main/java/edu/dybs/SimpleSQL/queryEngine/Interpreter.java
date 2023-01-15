package edu.dybs.SimpleSQL.queryEngine;

import java.util.ArrayList;

public class Interpreter {

    ArrayList<AST.Statement> queries;

    public Interpreter (ArrayList<AST.Statement> queries) {
        this.queries = queries;
    }

    public void interpret() {
        for (AST.Statement query : queries) {
            if (query instanceof AST.createDatabaseStatement) {
                System.out.println("instanceof AST.createDatabaseStatement");
            } else if (query instanceof AST.createTableStatement) {
                System.out.println("instanceof AST.createTableStatement");
            } else if (query instanceof AST.dropDatabaseStatement) {
                System.out.println("instanceof AST.dropDatabaseStatement");
            } else if (query instanceof AST.dropTableStatement) {
                System.out.println("instanceof AST.dropTableStatement");
            } else if (query instanceof AST.insertStatement) {
                System.out.println("instanceof AST.insertStatement");
            } else if (query instanceof AST.selectStatement) {
                System.out.println("instanceof AST.selectStatement");
            }
        }
    }
}
