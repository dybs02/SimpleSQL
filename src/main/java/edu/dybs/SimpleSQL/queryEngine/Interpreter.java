package edu.dybs.SimpleSQL.queryEngine;

import edu.dybs.SimpleSQL.exceptions.DatabaseManagementException;
import edu.dybs.SimpleSQL.storageEngine.DatabaseHandler;

import java.util.ArrayList;

public class Interpreter {

    ArrayList<AST.Statement> queries;

    public Interpreter (ArrayList<AST.Statement> queries) {
        this.queries = queries;
    }

    public void interpret() throws DatabaseManagementException {
        for (AST.Statement query : queries) {
            if (query instanceof AST.createDatabaseStatement) {
                DatabaseHandler.createDatabase(
                        ((AST.createDatabaseStatement) query).databaseName
                );
            } else if (query instanceof AST.createTableStatement) {
                DatabaseHandler.createTable(
                        ((AST.createTableStatement) query).tableName,
                        ((AST.createTableStatement) query).columns
                );
            } else if (query instanceof AST.dropDatabaseStatement) {
                DatabaseHandler.dropDatabase(
                        ((AST.dropDatabaseStatement) query).databaseName
                );
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
