package edu.dybs.SimpleSQL;

import edu.dybs.SimpleSQL.queryEngine.*;
import edu.dybs.SimpleSQL.exceptions.*;
import edu.dybs.SimpleSQL.storageEngine.DatabaseHandler;

import java.io.File;


public class queryTest {
    public static void main(String[] args) {
        deleteFile(new File("databases"));

        testParser("CREATE DATABASE school;");
        testParser("CREATE TABLE students (id int, name varchar, age int);");

    }

    public static void testParser(String input) {
        System.out.println("\nInput: " + input);

        try {
            Lexer l = new Lexer(input);
            Parser p = new Parser(l.lex());
            Interpreter i = new Interpreter(p.parse());
            i.interpret();
        } catch (InvalidCharacterSequenceException | InvalidSyntaxException | DatabaseManagementException e) {
            e.printStackTrace();
        }
    }

    public static void deleteFile(File element) {
        if (element.isDirectory()) {
            for (File sub : element.listFiles()) {
                deleteFile(sub);
            }
        }
        element.delete();
    }
}
