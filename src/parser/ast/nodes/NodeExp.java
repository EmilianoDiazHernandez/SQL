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

    public Token solve(NodeExp node) {
        if (node.left == null && node.right == null) return node.value;

        Token left = solve(node.left);
        Token right = solve(node.right);

        return evaluateOperation(left, right, node.value);
    }

    public Token evaluateOperation(Token left, Token right, Token operator) {
        return switch (operator.type) {
            case PLUS ->
                    new Operand(TSymbol.NUMBER, String.valueOf(Double.parseDouble(left.lexeme) + Double.parseDouble(right.lexeme)));
            case MINUS ->
                    new Operand(TSymbol.NUMBER, String.valueOf(Double.parseDouble(left.lexeme) - Double.parseDouble(right.lexeme)));
            case STAR ->
                    new Operand(TSymbol.NUMBER, String.valueOf(Double.parseDouble(left.lexeme) * Double.parseDouble(right.lexeme)));
            case SLASH -> {
                if (Double.parseDouble(right.lexeme) == 0) {
                    throw new ArithmeticException("División por cero.");
                }
                yield new Operand(TSymbol.NUMBER, String.valueOf(Double.parseDouble(left.lexeme) / Double.parseDouble(right.lexeme)));
            }
            default -> throw new UnsupportedOperationException("Operador no soportado: " + operator.type);
        };
    }
}
