package parser.ast.nodes;

import grammar.TSymbol;
import scanner.token.Operand;
import scanner.token.Token;

public class NodeExp {
    public Token value;
    public NodeExp left;
    public NodeExp right;

    public NodeExp(Token value) {
        this.value = value;
        this.left = null;
        this.right = null;
    }

    public void solve() {
        // Si el nodo es hoja, no hacemos nada.
        if (left == null && right == null) {
            return;
        }

        // Resolvemos recursivamente los nodos izquierdo y derecho si existen.
        if (left != null) {
            left.solve();
        }
        if (right != null) {
            right.solve();
        }

        // Actualizamos el valor del nodo con el resultado de evaluar la operación.
        this.value = evaluateOperation(
                left != null ? left.value : null,
                right != null ? right.value : null,
                this.value
        );
    }

    private Token evaluateOperation(Token left, Token right, Token operator) {
        switch (operator.type) {
            // Operadores aritméticos
            case PLUS:
                return new Operand(TSymbol.NUMBER,
                        String.valueOf(Double.parseDouble(left.lexeme) + Double.parseDouble(right.lexeme)));
            case MINUS:
                return new Operand(TSymbol.NUMBER,
                        String.valueOf(Double.parseDouble(left.lexeme) - Double.parseDouble(right.lexeme)));
            case STAR:
                return new Operand(TSymbol.NUMBER,
                        String.valueOf(Double.parseDouble(left.lexeme) * Double.parseDouble(right.lexeme)));
            case SLASH:
                if (Double.parseDouble(right.lexeme) == 0) {
                    throw new ArithmeticException("División por cero.");
                }
                return new Operand(TSymbol.NUMBER,
                        String.valueOf(Double.parseDouble(left.lexeme) / Double.parseDouble(right.lexeme)));

            // Operadores de comparación
            case GT: // >
                return new Operand(TSymbol.BOOLEAN,
                        String.valueOf(Double.parseDouble(left.lexeme) > Double.parseDouble(right.lexeme)));
            case LT: // <
                return new Operand(TSymbol.BOOLEAN,
                        String.valueOf(Double.parseDouble(left.lexeme) < Double.parseDouble(right.lexeme)));
            case EQ: // =
                return new Operand(TSymbol.BOOLEAN,
                        String.valueOf(left.lexeme.equals(right.lexeme)));
            case GE: // >=
                return new Operand(TSymbol.BOOLEAN,
                        String.valueOf(Double.parseDouble(left.lexeme) >= Double.parseDouble(right.lexeme)));
            case LE: // <=
                return new Operand(TSymbol.BOOLEAN,
                        String.valueOf(Double.parseDouble(left.lexeme) <= Double.parseDouble(right.lexeme)));

            // Operadores lógicos
            case AND:
                return new Operand(TSymbol.BOOLEAN,
                        String.valueOf(Boolean.parseBoolean(left.lexeme) && Boolean.parseBoolean(right.lexeme)));
            case NOT:
                return new Operand(TSymbol.BOOLEAN,
                        String.valueOf(!Boolean.parseBoolean(left.lexeme)));

            // Operador de control
            case DISTINCT:
                return new Operand(TSymbol.BOOLEAN,
                        String.valueOf(!left.lexeme.equals(right.lexeme)));

            default:
                throw new UnsupportedOperationException("Operador no soportado: " + operator.type);
        }
    }
}
