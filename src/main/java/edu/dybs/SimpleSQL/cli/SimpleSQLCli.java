package edu.dybs.SimpleSQL.cli;

import edu.dybs.SimpleSQL.exceptions.DatabaseManagementException;
import edu.dybs.SimpleSQL.exceptions.SimpleSQLException;
import edu.dybs.SimpleSQL.queryEngine.Interpreter;
import edu.dybs.SimpleSQL.queryEngine.Lexer;
import edu.dybs.SimpleSQL.queryEngine.Parser;
import edu.dybs.SimpleSQL.storageEngine.DatabaseHandler;

import java.util.Scanner;

public class SimpleSQLCli {
    private static Scanner in = new Scanner(System.in);
    private static boolean running = true;

    public static void run() {
        System.out.println("----------SimpleSQL----------");

        while(running) {
            promptName();
            String input = in.nextLine().strip();
            if (input.isEmpty()) {
                continue;
            }

            if (input.charAt(0) == '.') {
                executeCommand(input);
                continue;
            }

            if (DatabaseHandler.getDBName().equals("")) {
                System.out.println("ERROR: Database not specified");
                continue;
            }

            input += ";";
            try {
                Lexer l = new Lexer(input);
                Parser p = new Parser(l.lex());
                Interpreter i = new Interpreter(p.parse());
                i.interpret();
            } catch (SimpleSQLException e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        }
    }

    private static void promptName() {
        System.out.print(DatabaseHandler.getDBName());
        System.out.print("> ");
    }

    private static void executeCommand(String input) {
        String[] args = input.split(" ");
        switch (args[0]) {
            case ".switch":
                try {
                    DatabaseHandler.loadDatabase(args[1]);
                } catch (DatabaseManagementException e) {
                    System.out.println("ERROR: " + e.getMessage());
                }
                break;
            case ".exit":
                running = false;
                break;
            default:
                System.out.println("Invalid command");
        }
    }
}

