package main.java.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Token {
    public enum Type {
        SELECT("select"),
        FROM("from"),
        INSERT("insert"),
        INTO("into"),
        VALUES("values"),
        UPDATE("update"),
        SET("set"),
        DELETE("delete"),
        CREATE("create"),
        TABLE("table"),
        INT("int"),
        VARCHAR("varchar"),
        LPAREN("[(]"),
        RPAREN("[)]"),
        EQUALS("="),
        ASTERISK("[*]"),
        COLON(","),
        SEMICOLON(";"),
        STRING("'[A-Za-z]+'"),
        NUM("[0-9]+"),
        WHITESPACE("\\s+"),
        VARIABLE("[A-Za-z0-9_]+");

        private final Pattern pattern;

        Type(String r) {
            pattern = Pattern.compile("^" + r);
        }

        public int charsMatched(String s) {
            Matcher m = pattern.matcher(s);

            if (m.lookingAt()) {
                return m.end();
            }

            return -1;
        }
    }

    public final Type type;
    public final String content;

    public Token(Type type, String content) {
        this.type = type;
        this.content = content;
    }

    @Override
    public String toString() {
        return String.format("%s<%s>", type.toString(), content);
    }
}
