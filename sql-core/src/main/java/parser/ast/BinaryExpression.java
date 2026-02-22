package parser.ast;

import scanner.Token;

/**
 * Represents a binary operation in the AST.
 * (e.g., "5 + 3", "salary > 1000", "column AND column2")
 */
public final class BinaryExpression extends Expression {

    public final Expression left;
    public final Token operator;
    public final Expression right;

    public BinaryExpression(Expression left, Token operator, Expression right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
        return visitor.visitBinaryExpression(this);
    }
}
