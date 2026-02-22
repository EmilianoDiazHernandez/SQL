package parser.ast;

import scanner.Token;
import grammar.TSymbol;

public class TestAst {
    public static void main(String[] args) {
        Expression expression = new BinaryExpression(
            new UnaryExpression(
                new Token(TSymbol.MINUS, "-", 1),
                new Literal(123)
            ),
            new Token(TSymbol.STAR, "*", 1),
            new Grouping(
                new Literal(45.67)
            )
        );

        System.out.println(new AstPrinter().print(expression));
    }
}
