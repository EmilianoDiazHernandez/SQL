package parser.ast;

/**
 * Base abstract class for all SQL statements (e.g., SELECT, INSERT, UPDATE).
 * Statements are top-level constructs that execute actions.
 */
public abstract class Statement {
    /**
     * Accepts a visitor to perform an operation on this statement.
     *
     * @param visitor The visitor implementing the operation.
     * @param <R>     The return type of the visitor's operation.
     * @return The result of the visitor's operation.
     */
    public abstract <R> R accept(StatementVisitor<R> visitor);
}
