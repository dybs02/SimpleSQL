package edu.dybs.SimpleSQL.exceptions;

public class InvalidCharacterSequenceException extends SimpleSQLException {
    public String syntaxError;
    public String input;

    public InvalidCharacterSequenceException(String msg) {
        super(msg);
    }

    public InvalidCharacterSequenceException(String msg, String syntaxError, String input) {
        super(msg);
        this.syntaxError = syntaxError;
        this.input = input;
    }
}
