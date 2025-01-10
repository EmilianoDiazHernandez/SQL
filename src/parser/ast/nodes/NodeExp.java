package parser.ast.nodes;

import static grammar.TSymbol.*;

import scanner.token.Operand;
import scanner.token.Token;

import java.util.HashMap;

public class NodeExp {
    public Token value;
    public NodeExp left;
    public NodeExp right;
    public String tableName;

    public NodeExp(Token value, String tableName) {
        this.value = value;
        this.left = null;
        this.right = null;
        this.tableName = tableName;
    }

    public Token solve(HashMap<String, String> dataIds) {
        if (left == null && right == null) {
            String lexeme = value.lexeme;
            if (value.type == ID)
                lexeme = dataIds.get(value.lexeme);

            return new Token(value.type, lexeme);
        }

        Token solveLeft = null;
        if (left != null)
            solveLeft = left.solve(dataIds);

        Token solveRight = null;
        if (right != null)
            solveRight = right.solve(dataIds);

        return evaluateOperation(solveLeft, solveRight, value);
    }

    private Operand evaluateOperation(Token left, Token right, Token operator) {
        return switch (operator.type) {
            case PLUS -> new Operand(NUMBER,
                    String.valueOf(Double.parseDouble(left.lexeme) + Double.parseDouble(right.lexeme)));
            case MINUS -> new Operand(NUMBER,
                    String.valueOf(Double.parseDouble(left.lexeme) - Double.parseDouble(right.lexeme)));
            case STAR -> new Operand(NUMBER,
                    String.valueOf(Double.parseDouble(left.lexeme) * Double.parseDouble(right.lexeme)));
            case SLASH -> {
                if (Double.parseDouble(right.lexeme) == 0)
                    throw new ArithmeticException("division by zero.");
                yield new Operand(NUMBER,
                        String.valueOf(Double.parseDouble(left.lexeme) / Double.parseDouble(right.lexeme)));
            }

            case GT -> new Operand(BOOLEAN,
                    String.valueOf(Double.parseDouble(left.lexeme) > Double.parseDouble(right.lexeme)));
            case LT -> new Operand(BOOLEAN,
                    String.valueOf(Double.parseDouble(left.lexeme) < Double.parseDouble(right.lexeme)));
            case EQ -> new Operand(BOOLEAN,
                    String.valueOf(left.lexeme.equals(right.lexeme)));
            case NE -> new Operand(BOOLEAN,
                    String.valueOf(!left.lexeme.equals(right.lexeme)));
            case GE -> new Operand(BOOLEAN,
                    String.valueOf(Double.parseDouble(left.lexeme) >= Double.parseDouble(right.lexeme)));
            case LE -> new Operand(BOOLEAN,
                    String.valueOf(Double.parseDouble(left.lexeme) <= Double.parseDouble(right.lexeme)));

            case AND -> new Operand(BOOLEAN,
                    String.valueOf(Boolean.parseBoolean(left.lexeme) && Boolean.parseBoolean(right.lexeme)));
            case OR -> new Operand(BOOLEAN,
                    String.valueOf(Boolean.parseBoolean(left.lexeme) || Boolean.parseBoolean(right.lexeme)));

            case NOT -> new Operand(BOOLEAN,
                    String.valueOf(!Boolean.parseBoolean(right .lexeme)));
            case DISTINCT -> new Operand(BOOLEAN,
                    String.valueOf(!left.lexeme.equals(right.lexeme)));

            default -> throw new UnsupportedOperationException("Unsupported operator: " + operator.type);
        };
    }
}
