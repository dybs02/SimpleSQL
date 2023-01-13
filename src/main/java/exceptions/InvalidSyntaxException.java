package main.java.exceptions;

import main.java.parser.Token;

public class InvalidSyntaxException extends Exception {
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
