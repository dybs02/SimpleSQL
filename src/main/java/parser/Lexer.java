package main.java.parser;

import java.util.ArrayList;
import main.java.exceptions.InvalidCharacterSequenceException;

public class Lexer {
    private final String originalInput;
    private String input;

    public Lexer(String input) {
        this.originalInput = input;
        this.input = input;
    }

    public ArrayList<Token> lex() throws InvalidCharacterSequenceException {
        ArrayList<Token> tokens = new ArrayList<>();

        Token token = getNextToken();
        while (token != null) {
            if (token.type != Token.Type.WHITESPACE) {
                tokens.add(token);
            }
            token = getNextToken();
        }

        if (input.length() > 0) {
            String sequence = getInvalidSequence();
            throw new InvalidCharacterSequenceException(
                    "Invalid character sequence: '" + sequence + "'",
                    sequence,
                    originalInput
            );
        }

        return tokens;
    }

    private Token getNextToken() {
        for (Token.Type t : Token.Type.values()) {
            int endIndex = t.charsMatched(input);

            if (endIndex == -1) {
                continue;
            }

            Token found = new Token(t, input.substring(0, endIndex));
            input = input.substring(endIndex);
            return found;
        }
        return null;
    }

    private String getInvalidSequence() {
        String syntaxErrorPart = input.split(" ", 2)[0];

        for (String s : originalInput.split(" ")) {
            if (s.matches(String.format(".*%s.*", syntaxErrorPart))) {
                return s;
            }
        }

        return syntaxErrorPart;
    }
}
