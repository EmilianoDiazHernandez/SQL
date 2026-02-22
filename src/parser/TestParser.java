package parser;

import scanner.Scanner;
import scanner.Token;
import parser.ast.AstPrinter;
import parser.ast.Expression;

import java.util.List;

public class TestParser {
    public static void main(String[] args) {
        String input = "1 + 2 * 3 OR 4 > 5";
        System.out.println("Input: " + input);

        Scanner scanner = new Scanner(input);
        List<Token> tokens = scanner.scanTokens();

        // Debug: print tokens to see if scanner is working
        // for (Token t : tokens) System.out.println(t);

        SqlParser parser = new SqlParser(tokens);
        Expression expression = parser.parseExpression();

        if (expression != null) {
            System.out.println("AST: " + new AstPrinter().print(expression));
        } else {
            System.out.println("Error parsing.");
        }
    }
}
