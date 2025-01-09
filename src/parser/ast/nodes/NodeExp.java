package parser.ast.nodes;

import grammar.TSymbol;
import scanner.token.Operand;
import scanner.token.Token;

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

    public void solve() {
        if (left == null && right == null) {
            return;
        }

        if (left != null) {
            left.solve();
        }
        if (right != null) {
            right.solve();
        }

        this.value = evaluateOperation(
                left != null ? left.value : null,
                right != null ? right.value : null,
                this.value
        );
    }

    private Token evaluateOperation(Token left, Token right, Token operator) {
        return switch (operator.type) {
            case PLUS -> new Operand(TSymbol.NUMBER,
                    String.valueOf(Double.parseDouble(left.lexeme) + Double.parseDouble(right.lexeme)));
            case MINUS -> new Operand(TSymbol.NUMBER,
                    String.valueOf(Double.parseDouble(left.lexeme) - Double.parseDouble(right.lexeme)));
            case STAR -> new Operand(TSymbol.NUMBER,
                    String.valueOf(Double.parseDouble(left.lexeme) * Double.parseDouble(right.lexeme)));
            case SLASH -> {
                if (Double.parseDouble(right.lexeme) == 0)
                    throw new ArithmeticException("division by zero.");
                yield new Operand(TSymbol.NUMBER,
                        String.valueOf(Double.parseDouble(left.lexeme) / Double.parseDouble(right.lexeme)));
            }

            case GT -> new Operand(TSymbol.BOOLEAN,
                    String.valueOf(Double.parseDouble(left.lexeme) > Double.parseDouble(right.lexeme)));
            case LT -> new Operand(TSymbol.BOOLEAN,
                    String.valueOf(Double.parseDouble(left.lexeme) < Double.parseDouble(right.lexeme)));
            case EQ -> new Operand(TSymbol.BOOLEAN,
                    String.valueOf(left.lexeme.equals(right.lexeme)));
            case GE -> new Operand(TSymbol.BOOLEAN,
                    String.valueOf(Double.parseDouble(left.lexeme) >= Double.parseDouble(right.lexeme)));
            case LE -> new Operand(TSymbol.BOOLEAN,
                    String.valueOf(Double.parseDouble(left.lexeme) <= Double.parseDouble(right.lexeme)));

            case AND -> new Operand(TSymbol.BOOLEAN,
                    String.valueOf(Boolean.parseBoolean(left.lexeme) && Boolean.parseBoolean(right.lexeme)));
            case NOT -> new Operand(TSymbol.BOOLEAN,
                    String.valueOf(!Boolean.parseBoolean(left.lexeme)));

            case DISTINCT -> new Operand(TSymbol.BOOLEAN,
                    String.valueOf(!left.lexeme.equals(right.lexeme)));
            default -> throw new UnsupportedOperationException("Unsupported operator: " + operator.type);
        };
    }
}
