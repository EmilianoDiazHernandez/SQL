package parser.ast;

import java.util.List;

/**
 * Utility to visualize Statement ASTs.
 */
public class StatementPrinter implements StatementVisitor<String> {
    private final AstPrinter expressionPrinter = new AstPrinter();

    public String print(Statement stmt) {
        return stmt.accept(this);
    }

    @Override
    public String visitSelect(Select select) {
        StringBuilder builder = new StringBuilder();
        builder.append("(SELECT ");

        if (select.columns == null || select.columns.isEmpty()) {
            builder.append("*");
        } else {
            builder.append("(");
            for (int i = 0; i < select.columns.size(); i++) {
                builder.append(expressionPrinter.print(select.columns.get(i)));
                if (i < select.columns.size() - 1) builder.append(" ");
            }
            builder.append(")");
        }

        builder.append(" FROM ").append(select.from.getLexeme());

        if (select.where != null) {
            builder.append(" WHERE ").append(expressionPrinter.print(select.where));
        }

        builder.append(")");
        return builder.toString();
    }
}
