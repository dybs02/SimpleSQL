package edu.dybs.SimpleSQL.queryEngine;


import edu.dybs.SimpleSQL.exceptions.*;

import java.util.ArrayList;

public class Parser {

    private ArrayList<Token> tokens;
    private Token currentToken;

    public Parser(ArrayList<Token> tokens) throws InvalidCharacterSequenceException {
        this.tokens = tokens;
        currentToken = tokens.remove(0);
    }

    private Token assertPop(Token.Type type) throws InvalidSyntaxException {
        Token popped = currentToken;

        if (currentToken.type == type) {
            if (tokens.size() == 0) {
                currentToken = null;
            } else {
                currentToken = tokens.remove(0);
            }
        }else {
            throw new InvalidSyntaxException(
                    String.format("Invalid syntax: \"%s\" - expected %s", currentToken.content, type),
                    currentToken,
                    new Token(type, "")
            );
        }
        return popped;
    }

    public ArrayList<AST.Statement> parse() throws InvalidSyntaxException {
        return query();
    }

    /**
     * query : statement SEMICOLON query*
     */
    private ArrayList<AST.Statement> query() throws InvalidSyntaxException {
        ArrayList<AST.Statement> queries = new ArrayList<>();

        while (currentToken != null) {
            queries.add(statement());
            assertPop(Token.Type.SEMICOLON);
        }

        return queries;
    }

    /**
     * statement : createStatement | dropStatement | insertStatement | selectStatement
     */
    private AST.Statement statement() throws InvalidSyntaxException {
        switch (currentToken.type) {
            case CREATE:
                return createStatement();
            case DROP:
                return dropStatement();
            case INSERT:
                return insertStatement();
            case SELECT:
                return selectStatement();
        }
        return null;
    }

    /**
     * createStatement : CREATE (createDatabaseStatement | createTableStatement)
     */
    public AST.Statement createStatement() throws InvalidSyntaxException {
        assertPop(Token.Type.CREATE);

        if (currentToken.type == Token.Type.DATABASE) {
            return createDatabaseStatement();
        } else {
            return createTableStatement();
        }
    }

    /**
     * createDatabaseStatement : DATABASE VARIABLE
     */
    private AST.Statement createDatabaseStatement() throws InvalidSyntaxException {
        assertPop(Token.Type.DATABASE);
        Token name = assertPop(Token.Type.VARIABLE);

        return new AST.createDatabaseStatement(name.content);
    }

    /**
     * createTableStatement : TABLE VARIABLE LPAREN columnsDefinition RPAREN
     */
    private AST.Statement createTableStatement() throws InvalidSyntaxException {
        assertPop(Token.Type.TABLE);
        Token name = assertPop(Token.Type.VARIABLE);
        assertPop(Token.Type.LPAREN);
        ArrayList<AST.columnDefinition> columns = columnsDefinition();
        assertPop(Token.Type.RPAREN);

        return new AST.createTableStatement(name.content, columns);
    }

    /**
     * columnsDefinition : column (COLON column)*
     */
    private ArrayList<AST.columnDefinition> columnsDefinition() throws InvalidSyntaxException {
        ArrayList<AST.columnDefinition> columns = new ArrayList<>();
        columns.add(column());

        while (currentToken.type == Token.Type.COLON) {
            assertPop(Token.Type.COLON);
            columns.add(column());
        }

        return columns;
    }

    /**
     * column : VARIABLE (INT | VARCHAR)
     */
    private AST.columnDefinition column() throws InvalidSyntaxException {
        Token name = assertPop(Token.Type.VARIABLE);
        Token dataType;

        if (currentToken.type == Token.Type.INT) {
            dataType = assertPop(Token.Type.INT);
        } else {
            dataType = assertPop(Token.Type.VARCHAR);
        }

        return new AST.columnDefinition(name.content, dataType.type);
    }

    /**
     * dropStatement : DROP (dropDatabaseStatement | dropTableStatement)
     */
    private AST.Statement dropStatement() throws InvalidSyntaxException {
        assertPop(Token.Type.DROP);

        if (currentToken.type == Token.Type.DATABASE) {
            return dropDatabaseStatement();
        } else {
            return dropTableStatement();
        }
    }

    /**
     * dropDatabaseStatement : DATABASE VARIABLE
     */
    private AST.Statement dropDatabaseStatement() throws InvalidSyntaxException {
        assertPop(Token.Type.DATABASE);
        Token name = assertPop(Token.Type.VARIABLE);

        return new AST.dropDatabaseStatement(name.content);
    }

    /**
     * dropTableStatement : TABLE VARIABLE
     */
    private AST.Statement dropTableStatement() throws InvalidSyntaxException {
        assertPop(Token.Type.TABLE);
        Token name = assertPop(Token.Type.VARIABLE);

        return new AST.dropTableStatement(name.content);
    }

    /**
     * insertStatement : INSERT INTO VARIABLE (LPAREN columnNames RPAREN)? VALUES values+
     */
    private AST.Statement insertStatement() throws InvalidSyntaxException {
        assertPop(Token.Type.INSERT);
        assertPop(Token.Type.INTO);
        Token tableName = assertPop(Token.Type.VARIABLE);

        ArrayList<String> columnNames = new ArrayList<>();
        if (currentToken.type == Token.Type.LPAREN) {
            assertPop(Token.Type.LPAREN);
            columnNames = columnNames();
            assertPop(Token.Type.RPAREN);
        }

        assertPop(Token.Type.VALUES);
        ArrayList<AST.valueDefinition> values = valuesList();

        return new AST.insertStatement(tableName.content, columnNames, values);
    }

    /**
     * columnNames : VARIABLE (COLON VARIABLE)*
     */
    private ArrayList<String> columnNames() throws InvalidSyntaxException {
        ArrayList<String> names = new ArrayList<>();

        names.add(assertPop(Token.Type.VARIABLE).content);
        while (currentToken.type == Token.Type.COLON) {
            assertPop(Token.Type.COLON);
            names.add(assertPop(Token.Type.VARIABLE).content);
        }

        return names;
    }

    /**
     * valuesList : valueDefinition (COLON valueDefinition)*
     */
    private ArrayList<AST.valueDefinition> valuesList() throws InvalidSyntaxException {
        ArrayList<AST.valueDefinition> valueDefinitions = new ArrayList<>();

        valueDefinitions.add(valueDefinition());
        while (currentToken.type == Token.Type.COLON) {
            assertPop(Token.Type.COLON);
            valueDefinitions.add(valueDefinition());
        }

        return valueDefinitions;
    }

    /**
     * valueDefinition : LPAREN valueField (COLON valueField)* RPAREN
     */
    private AST.valueDefinition valueDefinition() throws InvalidSyntaxException {
        ArrayList<String> valueFields = new ArrayList<>();

        assertPop(Token.Type.LPAREN);
        valueFields.add(valueField());
        while (currentToken.type == Token.Type.COLON) {
            assertPop(Token.Type.COLON);
            valueFields.add(valueField());
        }
        assertPop(Token.Type.RPAREN);

        return new AST.valueDefinition(valueFields);
    }
    
    
    /**
     * valueField : (NUM | STRING)
     */
    private String valueField() throws InvalidSyntaxException {

        String val;
        if (currentToken.type == Token.Type.NUM) {
            val = assertPop(Token.Type.NUM).content;
        } else {
            val = assertPop(Token.Type.STRING).content;
        }

        return val;
    }

    /**
     * selectStatement : SELECT (ASTERISK | columnNames) FROM VARIABLE
     */
    private AST.Statement selectStatement() throws InvalidSyntaxException {
        assertPop(Token.Type.SELECT);

        ArrayList<String> columnNames = new ArrayList<>();
        if (currentToken.type == Token.Type.ASTERISK) {
            assertPop(Token.Type.ASTERISK);
        } else {
            columnNames = columnNames();
        }

        assertPop(Token.Type.FROM);
        Token tableName = assertPop(Token.Type.VARIABLE);

        return new AST.selectStatement(tableName.content, columnNames);
    }
}
