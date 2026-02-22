package main;

import dao.Table;
import parser.SqlParser;
import parser.ast.Statement;
import semantic.ExecutionVisitor;
import semantic.QueryResult;
import scanner.Scanner;
import scanner.Token;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws IOException {
        // If arguments are provided, try to run a script file
        if (args.length > 0) {
            runScript(args[0]);
        } else {
            runRepl();
        }
    }

    private static void runScript(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            System.err.println("Error: File not found: " + filePath);
            return;
        }
        String content = Files.readString(path);
        run(content);
    }

    private static void runRepl() throws IOException {
        System.out.println("==================================================");
        System.out.println("   SQL Engine ");
        System.out.println("   Type '.help' for instructions.");
        System.out.println("   Type '.exit' to quit.");
        System.out.println("==================================================");

        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);
        StringBuilder buffer = new StringBuilder();

        for (;;) {
            String prompt = buffer.length() == 0 ? "sql> " : "  -> ";
            System.out.print(prompt);
            String line = reader.readLine();
            
            if (line == null) break; // EOF
            
            String trimmedLine = line.trim();

            // Handle empty lines
            if (trimmedLine.isEmpty()) continue;

            // Handle meta-commands (only if buffer is empty)
            if (buffer.length() == 0 && trimmedLine.startsWith(".")) {
                handleMetaCommand(trimmedLine);
                continue;
            }

            buffer.append(line).append(" ");

            // Check if statement ends with semicolon
            if (trimmedLine.endsWith(";")) {
                long startTime = System.nanoTime();
                run(buffer.toString());
                long endTime = System.nanoTime();
                
                double durationMs = (endTime - startTime) / 1_000_000.0;
                System.out.printf("(%.2f ms)%n", durationMs);
                
                buffer.setLength(0); // Clear buffer
            }
        }
    }

    private static void handleMetaCommand(String command) {
        String[] parts = command.split("\\s+");
        String cmd = parts[0].toLowerCase();

        switch (cmd) {
            case ".exit":
            case ".quit":
                System.out.println("Goodbye!");
                System.exit(0);
                break;
            case ".help":
                printHelp();
                break;
            case ".tables":
                listTables();
                break;
            case ".describe":
            case ".desc":
                if (parts.length > 1) {
                    describeTable(parts[1]);
                } else {
                    System.out.println("Usage: .describe <table_name>");
                }
                break;
            case ".clear":
                System.out.println("Buffer cleared.");
                break;
            default:
                System.out.println("Unknown command: " + cmd + ". Type '.help' for available commands.");
        }
    }

    private static void printHelp() {
        System.out.println("\n--- Meta Commands ---");
        System.out.println("  .help                Show this help message");
        System.out.println("  .tables              List available tables (CSV files)");
        System.out.println("  .describe <table_name> Show columns of a table");
        System.out.println("  .exit                Exit the application");
        System.out.println("  .clear               Clear the current input buffer");
        System.out.println("\n--- SQL Syntax ---");
        System.out.println("  SELECT * FROM table;");
        System.out.println("  SELECT col1, col2 FROM table WHERE col1 > 10;");
        System.out.println();
    }

    private static void listTables() {
        File folder = new File("db");
        if (!folder.exists() || !folder.isDirectory()) {
            System.out.println("Database directory 'db' not found.");
            return;
        }

        File[] listOfFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".csv"));
        
        System.out.println("\n--- Tables ---");
        if (listOfFiles != null && listOfFiles.length > 0) {
            for (File file : listOfFiles) {
                String tableName = file.getName().replace(".csv", "");
                System.out.println("  " + tableName);
            }
        } else {
            System.out.println("  (No tables found)");
        }
        System.out.println();
    }

    private static void describeTable(String tableName) {
        try {
            Table table = new Table(tableName);
            List<String> columns = table.getColumns();
            System.out.println("\nTable: " + tableName);
            System.out.println("Columns:");
            for (String col : columns) {
                System.out.println("  - " + col);
            }
            System.out.println();
        } catch (IOException e) {
            System.out.println("Error describing table '" + tableName + "': " + e.getMessage());
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

        // Calculate dynamic column widths
        int[] widths = new int[columns.size()];
        for (int i = 0; i < columns.size(); i++) {
            widths[i] = columns.get(i).length();
        }
        for (Map<String, Object> row : rows) {
            for (int i = 0; i < columns.size(); i++) {
                Object val = row.get(columns.get(i));
                String strVal = val == null ? "NULL" : val.toString();
                if (strVal.length() > widths[i]) {
                    widths[i] = strVal.length();
                }
            }
        }

        // Print Header
        printRow(columns, widths);
        printSeparator(widths);

        // Print Rows
        for (Map<String, Object> row : rows) {
            List<String> values = new ArrayList<>();
            for (String col : columns) {
                Object val = row.get(col);
                values.add(val == null ? "NULL" : val.toString());
            }
            printRow(values, widths);
        }
    }

    private static void printRow(List<String> values, int[] widths) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < values.size(); i++) {
            String val = values.get(i);
            sb.append(String.format("%-" + (widths[i] + 2) + "s", val)); // +2 for padding
            if (i < values.size() - 1) sb.append("| ");
        }
        System.out.println(sb.toString());
    }

    private static void printSeparator(int[] widths) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < widths.length; i++) {
            sb.append("-".repeat(widths[i] + 2));
            if (i < widths.length - 1) sb.append("+-");
        }
        System.out.println(sb.toString());
    }
}
