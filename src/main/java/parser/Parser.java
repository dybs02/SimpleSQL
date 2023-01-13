package main.java.parser;

import main.java.exceptions.InvalidCharacterSequenceException;
import main.java.exceptions.InvalidSyntaxError;

import java.util.ArrayList;

public class Parser {

    private ArrayList<Token> tokens;
    private Token currentToken;

    public Parser(ArrayList<Token> tokens) throws InvalidCharacterSequenceException {
        this.tokens = tokens;
        currentToken = tokens.remove(0);
    }

    private Token assertPop(Token.Type type) throws InvalidSyntaxError {
        Token popped = currentToken;

        if (currentToken.type == type) {
            if (tokens.size() == 0) {
                currentToken = null;
            } else {
                currentToken = tokens.remove(0);
            }
        }else {
            throw new InvalidSyntaxError(
                    String.format("Invalid syntax: \"%s\" - expected %s", currentToken.content, type),
                    currentToken,
                    new Token(type, "")
            );
        }
        return popped;
    }

    public ArrayList<AST.Statement> parse() throws InvalidSyntaxError {
        return query();
    }

    /**
     * query : statement SEMICOLON query*
     */
    private ArrayList<AST.Statement> query() throws InvalidSyntaxError {
        ArrayList<AST.Statement> queries = new ArrayList<>();

        while (currentToken != null) {
            queries.add(statement());
            assertPop(Token.Type.SEMICOLON);
        }

        return queries;
    }

    /**
     * statement : createStatement | dropStatement
     */
    private AST.Statement statement() throws InvalidSyntaxError {
        switch (currentToken.type) {
            case CREATE:
                return createStatement();
            case DROP:
                return dropStatement();
        }
        return null;
    }

    /**
     * createStatement : CREATE (createDatabaseStatement | createTableStatement)
     */
    public AST.Statement createStatement() throws InvalidSyntaxError {
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
    private AST.Statement createDatabaseStatement() throws InvalidSyntaxError {
        assertPop(Token.Type.DATABASE);
        Token name = assertPop(Token.Type.VARIABLE);

        return new AST.createDatabaseStatement(name.content);
    }

    /**
     * createTableStatement : TABLE VARIABLE LPAREN columnsDefinition RPAREN
     */
    private AST.Statement createTableStatement() throws InvalidSyntaxError {
        assertPop(Token.Type.TABLE);
        Token name = assertPop(Token.Type.VARIABLE);
        assertPop(Token.Type.LPAREN);
        ArrayList<AST.Node> columns = columnsDefinition();
        assertPop(Token.Type.RPAREN);

        return new AST.createTableStatement(name.content, columns);
    }

    /**
     * columnsDefinition : column (COLON column)*
     */
    private ArrayList<AST.Node> columnsDefinition() throws InvalidSyntaxError {
        ArrayList<AST.Node> columns = new ArrayList<>();
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
    private AST.Node column() throws InvalidSyntaxError {
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
    private AST.Statement dropStatement() throws InvalidSyntaxError {
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
    private AST.Statement dropDatabaseStatement() throws InvalidSyntaxError {
        assertPop(Token.Type.DATABASE);
        Token name = assertPop(Token.Type.VARIABLE);

        return new AST.dropDatabaseStatement(name.content);
    }

    /**
     * dropTableStatement : TABLE VARIABLE
     */
    private AST.Statement dropTableStatement() throws InvalidSyntaxError {
        assertPop(Token.Type.TABLE);
        Token name = assertPop(Token.Type.VARIABLE);

        return new AST.dropTableStatement(name.content);
    }
}
