package edu.dybs.SimpleSQL.exceptions;


import edu.dybs.SimpleSQL.queryEngine.Token;

public class InvalidSyntaxException extends SimpleSQLException {
    public Token given;
    public Token expected;

    public InvalidSyntaxException(String msg) {
        super(msg);
    }

    public InvalidSyntaxException(String msg, Token given, Token expected) {
        super(msg);
        this.given = given;
        this.expected = expected;
    }
}
