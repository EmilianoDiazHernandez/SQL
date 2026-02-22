package parser;

import scanner.Scanner;
import scanner.Token;
import parser.ast.StatementPrinter;
import parser.ast.Statement;

import java.util.List;

public class TestStatementParser {
    public static void main(String[] args) {
        String input = "SELECT id, name, age + 1 FROM users WHERE id > 10;";
        System.out.println("Input SQL: " + input);

        Scanner scanner = new Scanner(input);
        List<Token> tokens = scanner.scanTokens();

        SqlParser parser = new SqlParser(tokens);
        List<Statement> statements = parser.parse();

        StatementPrinter printer = new StatementPrinter();
        for (Statement stmt : statements) {
            System.out.println("AST: " + printer.print(stmt));
        }
    }
}
