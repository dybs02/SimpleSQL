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
                DatabaseHandler.dropTable(
                        ((AST.dropTableStatement) query).tableName
                );
            } else if (query instanceof AST.insertStatement) {
                DatabaseHandler.insertValues(
                        ((AST.insertStatement) query).tableName,
                        ((AST.insertStatement) query).columnNames,
                        ((AST.insertStatement) query).values
                );
            } else if (query instanceof AST.selectStatement) {
                DatabaseHandler.selectColumns(
                        ((AST.selectStatement) query).tableName,
                        ((AST.selectStatement) query).columnNames,
                        ((AST.selectStatement) query).condition
                );
            }
        }
    }
}
