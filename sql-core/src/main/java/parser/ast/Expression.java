package parser.ast;

/**
 * Base abstract class for all SQL expressions.
 * An expression is anything that can be evaluated to a value.
 *
 * <p>This uses the Visitor pattern for operations like validation, printing, and evaluation.</p>
 */
public abstract class Expression {

    /**
     * Accepts a visitor to perform an operation on this expression.
     *
     * @param visitor The visitor implementing the operation.
     * @param <R>     The return type of the visitor's operation.
     * @return The result of the visitor's operation.
     */
    public abstract <R> R accept(Visitor<R> visitor);
}
