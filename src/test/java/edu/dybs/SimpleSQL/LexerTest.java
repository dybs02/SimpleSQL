package edu.dybs.SimpleSQL;

import edu.dybs.SimpleSQL.exceptions.*;
import edu.dybs.SimpleSQL.parser.*;

import java.util.ArrayList;

public class LexerTest {
    public static void main(String[] args) {


        String input1 = "CREATE TABLE students (int id, varchar name, int age);";
        System.out.println("------------------------------------------------------------");
        System.out.println("Testing lexer with VALID input");
        System.out.println("Input: " + input1);
        testLex(input1);

        String input2 = "CREATE TABLE students (int id, varchar name&surname, int age);";
        System.out.println("------------------------------------------------------------");
        System.out.println("Testing lexer with INVALID input");
        System.out.println("Input: " + input2);
        testLex(input2);
    }

    public static void testLex(String input) {
        ArrayList<Token> tokens = new ArrayList<>();
        Lexer l = new Lexer(input);

        try {
            tokens = l.lex();
        } catch (InvalidCharacterSequenceException e) {
            e.printStackTrace();
        }

        for (Token t : tokens) {
            System.out.println(t);
        }
    }
}
