package main.java.exceptions;

import main.java.parser.Token;

public class InvalidSyntaxError extends Exception {
    public Token given;
    public Token expected;

    public InvalidSyntaxError(String msg) {
        super(msg);
    }

    public InvalidSyntaxError(String msg, Token given, Token expected) {
        super(msg);
        this.given = given;
        this.expected = expected;
    }
}
