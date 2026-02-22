package main;

import parser.SqlParser;
import parser.ast.Statement;
import semantic.ExecutionVisitor;
import scanner.Scanner;
import scanner.Token;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        System.out.println("SQL Engine v2.0 (Recursive Descent)");
        System.out.println("Type 'exit' or Ctrl+D to quit.");
        runPrompt();
    }

    private static void runPrompt() throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        for (;;) {
            System.out.print("sql> ");
            String line = reader.readLine();
            if (line == null) break;
            if ("exit".equalsIgnoreCase(line.trim())) break;
            
            if (line.trim().isEmpty()) continue;

            run(line);
        }
    }

    private static void run(String source) {
        // 1. Scan
        Scanner scanner = new Scanner(source);
        List<Token> tokens = scanner.scanTokens();

        // 2. Parse
        SqlParser parser = new SqlParser(tokens);
        // We only support one statement per line in this REPL mostly, but parser returns List.
        List<Statement> statements = parser.parse();

        if (statements.isEmpty()) {
            // Parser error handled inside parser (printed to stderr)
            return;
        }

        // 3. Execute
        ExecutionVisitor executor = new ExecutionVisitor();
        for (Statement stmt : statements) {
            try {
                stmt.accept(executor);
            } catch (Exception e) {
                System.err.println("Execution Error: " + e.getMessage());
            }
        }
    }
}
