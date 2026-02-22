package parser.ast;

/**
 * A utility class to print the AST structure in a readable format.
 */
public class AstPrinter implements Visitor<String> {

    public String print(Expression expr) {
        return expr.accept(this);
    }

    @Override
    public String visitBinaryExpression(BinaryExpression expr) {
        return parenthesize(expr.operator.getLexeme(), expr.left, expr.right);
    }

    @Override
    public String visitGrouping(Grouping expr) {
        return parenthesize("group", expr.expression);
    }

    @Override
    public String visitLiteral(Literal expr) {
        if (expr.value == null) return "nil";
        return expr.value.toString();
    }

    @Override
    public String visitUnaryExpression(UnaryExpression expr) {
        return parenthesize(expr.operator.getLexeme(), expr.right);
    }

    @Override
    public String visitIdentifier(Identifier expr) {
        return expr.token.getLexeme();
    }

    private String parenthesize(String name, Expression... exprs) {
        StringBuilder builder = new StringBuilder();

        builder.append("(").append(name);
        for (Expression expr : exprs) {
            builder.append(" ");
            builder.append(expr.accept(this));
        }
        builder.append(")");

        return builder.toString();
    }
}
