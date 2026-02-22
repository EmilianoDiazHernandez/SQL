package parser.ast;

/**
 * Represents a literal value in the AST.
 * (e.g., 123, 3.14, "hello", true, null)
 */
public final class Literal extends Expression {
    public final Object value;

    public Literal(Object value) {
        this.value = value;
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
        return visitor.visitLiteral(this);
    }
}
