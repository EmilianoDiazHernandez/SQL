package parser;

import grammar.TSymbol;
import scanner.Token;
import parser.ast.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SqlParser {
    private final List<Token> tokens;
    private int current = 0;

    public SqlParser(List<Token> tokens) {
        this.tokens = tokens;
    }

    /**
     * Parses a list of SQL statements.
     */
    public List<Statement> parse() {
        List<Statement> statements = new ArrayList<>();
        try {
            while (!isAtEnd()) {
                statements.add(statement());
            }
        } catch (RuntimeException e) {
            return Collections.emptyList(); // Return empty list on error
        }
        return statements;
    }

    private Statement statement() {
        if (match(TSymbol.SELECT)) return selectStatement();

        // For now, if it's not a SELECT, we assume it's an expression statement (for REPL)
        // In a real SQL parser, only complete statements are allowed.
        // But for testing, let's allow expressions.
        // Actually, let's keep it strict for now.
        throw error(peek(), "Expect statement (SELECT).");
    }

    private Statement selectStatement() {
        List<Expression> columns = new ArrayList<>();
        if (match(TSymbol.STAR)) {
            // SELECT * ... empty columns list implies *
        } else {
            do {
                if (check(TSymbol.FROM)) break; // Safety check
                columns.add(expression());
            } while (match(TSymbol.COMA));
        }

        consume(TSymbol.FROM, "Expect 'FROM' after select list.");

        Token table = consume(TSymbol.ID, "Expect table name.");

        Expression where = null;
        if (match(TSymbol.WHERE)) {
            where = expression();
        }

        consume(TSymbol.SEMICOLON, "Expect ';' after statement.");

        return new Select(columns, table, where);
    }

    public Expression parseExpression() {
        try {
            return expression();
        } catch (RuntimeException e) {
            return null;
        }
    }

    private Expression expression() {
        return logicOr();
    }

    private Expression logicOr() {
        Expression expr = logicAnd();

        while (match(TSymbol.OR)) {
            Token operator = previous();
            Expression right = logicAnd();
            expr = new BinaryExpression(expr, operator, right);
        }

        return expr;
    }

    private Expression logicAnd() {
        Expression expr = equality();

        while (match(TSymbol.AND)) {
            Token operator = previous();
            Expression right = equality();
            expr = new BinaryExpression(expr, operator, right);
        }

        return expr;
    }

    private Expression equality() {
        Expression expr = comparison();

        while (match(TSymbol.DISTINCT, TSymbol.EQ)) {
            Token operator = previous();
            Expression right = comparison();
            expr = new BinaryExpression(expr, operator, right);
        }

        return expr;
    }

    private Expression comparison() {
        Expression expr = term();

        while (match(TSymbol.GT, TSymbol.GE, TSymbol.LT, TSymbol.LE)) {
            Token operator = previous();
            Expression right = term();
            expr = new BinaryExpression(expr, operator, right);
        }

        return expr;
    }

    private Expression term() {
        Expression expr = factor();

        while (match(TSymbol.MINUS, TSymbol.PLUS)) {
            Token operator = previous();
            Expression right = factor();
            expr = new BinaryExpression(expr, operator, right);
        }

        return expr;
    }

    private Expression factor() {
        Expression expr = unary();

        while (match(TSymbol.SLASH, TSymbol.STAR)) {
            Token operator = previous();
            Expression right = unary();
            expr = new BinaryExpression(expr, operator, right);
        }

        return expr;
    }

    private Expression unary() {
        if (match(TSymbol.NOT, TSymbol.MINUS)) {
            Token operator = previous();
            Expression right = unary();
            return new UnaryExpression(operator, right);
        }

        return primary();
    }

    private Expression primary() {
        if (match(TSymbol.FALSE)) return new Literal(false);
        if (match(TSymbol.TRUE)) return new Literal(true);
        if (match(TSymbol.NULL)) return new Literal(null);

        if (match(TSymbol.NUMBER)) {
             return new Literal(Double.parseDouble(previous().getLexeme()));
        }
        if (match(TSymbol.STRING)) {
             return new Literal(previous().getLexeme());
        }
        if (match(TSymbol.ID)) {
             // For now, ID is a literal column name? Or a Identifier expression?
             // It should be an Identifier expression.
             // We need Identifier class in AST.
             // For now, let's treat it as a Literal string or make Identifier class.
             // Correct way: Identifier class.
             return new Identifier(previous());
        }

        if (match(TSymbol.LEFT_PAREN)) {
            Expression expr = expression();
            consume(TSymbol.RIGHT_PAREN, "Expected ')' after expression.");
            return new Grouping(expr);
        }

        throw error(peek(), "Expect expression.");
    }

    // --- Helper Methods ---

    private boolean match(TSymbol... types) {
        for (TSymbol type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }
        return false;
    }

    private Token consume(TSymbol type, String message) {
        if (check(type)) return advance();

        throw error(peek(), message);
    }

    private boolean check(TSymbol type) {
        if (isAtEnd()) return false;
        return peek().getType() == type;
    }

    private Token advance() {
        if (!isAtEnd()) current++;
        return previous();
    }

    private boolean isAtEnd() {
        return peek().getType() == TSymbol.EOF;
    }

    private Token peek() {
        return tokens.get(current);
    }

    private Token previous() {
        return tokens.get(current - 1);
    }

    private RuntimeException error(Token token, String message) {
        System.err.println("[line " + token.getLine() + "] Error at '" + token.getLexeme() + "': " + message);
        return new RuntimeException(message);
    }
}
