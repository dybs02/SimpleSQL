package edu.dybs.SimpleSQL;

import edu.dybs.SimpleSQL.queryEngine.*;
import edu.dybs.SimpleSQL.exceptions.*;

import java.io.File;


public class queryTest {
    public static void main(String[] args) {
        deleteFile(new File("databases"));

        testParser("CREATE DATABASE school;");
        testParser("CREATE TABLE students (id int, name varchar, age int);");
        testParser("CREATE TABLE teachers (name varchar, age int, subject varchar);");
//        testParser("DROP DATABASE school;");
//        testParser("DROP TABLE students;");
        testParser("INSERT INTO students VALUES (1, 'Jim', 28);");
        testParser("INSERT INTO students VALUES (2, 'Pam', 26), (3, 'Michael', 42);");
        testParser("INSERT INTO students (id, name) VALUES (4, 'Creed');");
        testParser("INSERT INTO students VALUES (5, 'Ryan', 25);");
        testParser("SELECT * FROM students;");
        testParser("SELECT name, age FROM students;");

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
