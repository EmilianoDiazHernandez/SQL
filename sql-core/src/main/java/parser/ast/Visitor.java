package parser.ast;

/**
 * The Visitor interface for the AST.
 */
public interface Visitor<R> {
    R visitBinaryExpression(BinaryExpression expr);
    R visitUnaryExpression(UnaryExpression expr);
    R visitLiteral(Literal expr);
    R visitGrouping(Grouping expr);
    R visitIdentifier(Identifier expr);
}
