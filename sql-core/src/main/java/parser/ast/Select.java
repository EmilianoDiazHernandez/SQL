package parser.ast;

import java.util.List;
import scanner.Token;

/**
 * Represents a SELECT statement in the AST.
 * Structure: SELECT columns FROM table [WHERE condition]
 */
public class Select extends Statement {
    public final List<Expression> columns;
    public final Token from;
    public final Expression where;

    public Select(List<Expression> columns, Token from, Expression where) {
        this.columns = columns;
        this.from = from;
        this.where = where;
    }

    @Override
    public <R> R accept(StatementVisitor<R> visitor) {
        return visitor.visitSelect(this);
    }
}
