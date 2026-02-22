package main;

import parser.SqlParser;
import parser.ast.Statement;
import semantic.ExecutionVisitor;
import semantic.QueryResult;
import scanner.Scanner;
import scanner.Token;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
                QueryResult result = stmt.accept(executor);
                printResult(result);
            } catch (Exception e) {
                System.err.println("Execution Error: " + e.getMessage());
            }
        }
    }

    private static void printResult(QueryResult result) {
        if (result == null) return;

        List<String> columns = result.getColumns();
        List<Map<String, Object>> rows = result.getRows();

        if (rows.isEmpty()) {
            System.out.println(result.getMessage());
            return;
        }

        // Print Header
        System.out.println(String.join(" | ", columns));
        System.out.println("-".repeat(Math.max(columns.size() * 10, 20)));

        // Print Rows
        for (Map<String, Object> row : rows) {
            List<String> values = new ArrayList<>();
            for (String col : columns) {
                Object val = row.get(col);
                values.add(val == null ? "NULL" : val.toString());
            }
            System.out.println(String.join(" | ", values));
        }
        // System.out.println(result.getMessage()); // Optional: print row count
    }
}
