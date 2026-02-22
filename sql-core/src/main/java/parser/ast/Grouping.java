package parser.ast;

/**
 * Represents a parenthesized expression in the AST.
 * (e.g., "(5 + 3)")
 *
 * <p>It wraps another expression to enforce grouping and precedence.</p>
 */
public final class Grouping extends Expression {
    public final Expression expression;

    public Grouping(Expression expression) {
        this.expression = expression;
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
        return visitor.visitGrouping(this);
    }
}
