package semantic;

import parser.ast.*;
import scanner.Token;
import grammar.TSymbol;

import java.util.Map;

/**
 * Evaluates expressions against a specific row of data.
 */
public class ExpressionEvaluator implements Visitor<Object> {
    private final Map<String, Object> row;

    public ExpressionEvaluator(Map<String, Object> row) {
        this.row = row;
    }

    public Object evaluate(Expression expr) {
        return expr.accept(this);
    }

    @Override
    public Object visitLiteral(Literal expr) {
        return expr.value;
    }

    @Override
    public Object visitIdentifier(Identifier expr) {
        String columnName = expr.token.getLexeme();
        if (row.containsKey(columnName)) {
            return row.get(columnName);
        }
        // If not found, throw exception or return null.
        // For simplicity, null.
        throw new RuntimeException("Column '" + columnName + "' not found.");
    }

    @Override
    public Object visitGrouping(Grouping expr) {
        return evaluate(expr.expression);
    }

    @Override
    public Object visitUnaryExpression(UnaryExpression expr) {
        Object right = evaluate(expr.right);

        switch (expr.operator.getType()) {
            case MINUS:
                checkNumberOperand(expr.operator, right);
                return -(double)right;
            case NOT:
                return !isTruthy(right);
        }

        return null;
    }

    @Override
    public Object visitBinaryExpression(BinaryExpression expr) {
        // Handle logical operators separately for short-circuiting
        if (expr.operator.getType() == TSymbol.OR) {
            Object left = evaluate(expr.left);
            if (isTruthy(left)) return true;
            return isTruthy(evaluate(expr.right));
        }
        if (expr.operator.getType() == TSymbol.AND) {
            Object left = evaluate(expr.left);
            if (!isTruthy(left)) return false;
            return isTruthy(evaluate(expr.right));
        }

        Object left = evaluate(expr.left);
        Object right = evaluate(expr.right);

        switch (expr.operator.getType()) {
            case GT:
                checkNumberOperands(expr.operator, left, right);
                return (double)left > (double)right;
            case GE:
                checkNumberOperands(expr.operator, left, right);
                return (double)left >= (double)right;
            case LT:
                checkNumberOperands(expr.operator, left, right);
                return (double)left < (double)right;
            case LE:
                checkNumberOperands(expr.operator, left, right);
                return (double)left <= (double)right;
            case MINUS:
                checkNumberOperands(expr.operator, left, right);
                return (double)left - (double)right;
            case PLUS:
                if (left instanceof Double && right instanceof Double) {
                    return (double)left + (double)right;
                }
                if (left instanceof String || right instanceof String) {
                    return String.valueOf(left) + String.valueOf(right);
                }
                throw new RuntimeException("Operands must be two numbers or two strings.");
            case SLASH:
                checkNumberOperands(expr.operator, left, right);
                return (double)left / (double)right;
            case STAR:
                checkNumberOperands(expr.operator, left, right);
                return (double)left * (double)right;
            case EQ:
                return isEqual(left, right);
            case DISTINCT:
                return !isEqual(left, right);
        }
        return null;
    }

    private boolean isTruthy(Object object) {
        if (object == null) return false;
        if (object instanceof Boolean) return (boolean)object;
        return true;
    }

    private boolean isEqual(Object a, Object b) {
        if (a == null && b == null) return true;
        if (a == null) return false;
        return a.equals(b);
    }

    private void checkNumberOperand(Token operator, Object operand) {
        if (operand instanceof Double) return;
        throw new RuntimeException(operator.getLexeme() + " operand must be a number.");
    }

    private void checkNumberOperands(Token operator, Object left, Object right) {
        if (left instanceof Double && right instanceof Double) return;
        throw new RuntimeException(operator.getLexeme() + " operands must be numbers.");
    }
}
