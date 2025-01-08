package parser;

import grammar.TSymbol;
import scanner.token.ControlStructure;
import scanner.token.Operand;
import scanner.token.Operator;
import scanner.token.Token;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


public class Postfix {

    public static List<Token> convert(List<Token> infix) {
        List<Token> postfix = new ArrayList<>();
        Stack<Token> stack = new Stack<>();

        for (Token token : infix) {
            if (token instanceof Operand) {
                postfix.add(token);
            } else if (token instanceof Operator) {
                while (!stack.isEmpty() && stack.peek().type.precedence >= token.type.precedence)
                    postfix.add(stack.pop());
                stack.push(token);
            } else if (token.type == TSymbol.LEFT_PAREN) {
                stack.push(token);
            } else if (token.type == TSymbol.RIGHT_PAREN) {
                while (stack.peek().type != TSymbol.LEFT_PAREN)
                    postfix.add(stack.pop());
                stack.pop();
            } else if (token instanceof ControlStructure) {
                while (!stack.isEmpty())
                    postfix.add(stack.pop());
                postfix.add(token);
            } else if (token.type == TSymbol.COMA) {
                while (!stack.isEmpty())
                    postfix.add(stack.pop());
                postfix.add(token);
            }
        }

        while (!stack.isEmpty())
            postfix.add(stack.pop());

        postfix.add(new ControlStructure(TSymbol.EOF, "EOF", infix.size()));
        return postfix;
    }
}