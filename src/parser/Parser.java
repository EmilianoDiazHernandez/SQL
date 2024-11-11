package parser;

import grammar.NTSymbol;
import grammar.Symbol;
import grammar.TSymbol;
import scanner.Token;

import java.util.*;

public class Parser {

    List<Token> tokens;

    int tokenSize;

    int tokeIndex;

    Token lookahead;

    ParserState state;


    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.tokenSize = tokens.size();
        this.tokeIndex = 0;
        this.lookahead = tokens.get(tokeIndex);
        this.state = ParserState.BEGIN;
    }

    void match(Symbol type) {
        if (lookahead.type == type) {
            tokeIndex++;
            if (tokeIndex >= tokenSize)
                state = ParserState.ERROR;
            else
                lookahead = tokens.get(tokeIndex);

        } else {
            state = ParserState.ERROR;
            System.out.println("== ERROR (Parser): bad token at " + lookahead.line);
            System.out.println("==== Expected a " + type + " but got " + lookahead.lexeme);
        }
    }

    public void parse() {
        Map<Integer, List<Symbol>> table = new HashMap<>();

        table.put(new TableEntry(NTSymbol.Q, TSymbol.SELECT).hashCode(), List.of(TSymbol.SELECT, NTSymbol.D, TSymbol.FROM, NTSymbol.T, NTSymbol.W));

        table.put(new TableEntry(NTSymbol.TERM1, TSymbol.FROM).hashCode(), List.of(TSymbol.EPSILON));
        table.put(new TableEntry(NTSymbol.TERM1, TSymbol.COMA).hashCode(), List.of(TSymbol.EPSILON));
        table.put(new TableEntry(NTSymbol.TERM1, TSymbol.OR).hashCode(), List.of(TSymbol.EPSILON));
        table.put(new TableEntry(NTSymbol.TERM1, TSymbol.AND).hashCode(), List.of(TSymbol.EPSILON));
        table.put(new TableEntry(NTSymbol.TERM1, TSymbol.LE).hashCode(), List.of(TSymbol.EPSILON));
        table.put(new TableEntry(NTSymbol.TERM1, TSymbol.GT).hashCode(), List.of(TSymbol.EPSILON));
        table.put(new TableEntry(NTSymbol.TERM1, TSymbol.GE).hashCode(), List.of(TSymbol.EPSILON));
        table.put(new TableEntry(NTSymbol.TERM1, TSymbol.LT).hashCode(), List.of(TSymbol.EPSILON));
        table.put(new TableEntry(NTSymbol.TERM1, TSymbol.EQ).hashCode(), List.of(TSymbol.EPSILON));
        table.put(new TableEntry(NTSymbol.TERM1, TSymbol.NE).hashCode(), List.of(TSymbol.EPSILON));
        table.put(new TableEntry(NTSymbol.TERM1, TSymbol.EOF).hashCode(), List.of(TSymbol.EPSILON));
        table.put(new TableEntry(NTSymbol.TERM1, TSymbol.RIGHT_PAREN).hashCode(), List.of(TSymbol.EPSILON));
        table.put(new TableEntry(NTSymbol.TERM1, TSymbol.MINUS).hashCode(), List.of(TSymbol.MINUS, NTSymbol.TERM));
        table.put(new TableEntry(NTSymbol.TERM1, TSymbol.PLUS).hashCode(), List.of(TSymbol.PLUS, NTSymbol.TERM));

        table.put(new TableEntry(NTSymbol.FACTOR, TSymbol.MINUS).hashCode(), List.of(NTSymbol.UNARY, NTSymbol.FACTOR1));
        table.put(new TableEntry(NTSymbol.FACTOR, TSymbol.NOT).hashCode(), List.of(NTSymbol.UNARY, NTSymbol.FACTOR1));
        table.put(new TableEntry(NTSymbol.FACTOR, TSymbol.ID).hashCode(), List.of(NTSymbol.UNARY, NTSymbol.FACTOR1));
        table.put(new TableEntry(NTSymbol.FACTOR, TSymbol.TRUE).hashCode(), List.of(NTSymbol.UNARY, NTSymbol.FACTOR1));
        table.put(new TableEntry(NTSymbol.FACTOR, TSymbol.FALSE).hashCode(), List.of(NTSymbol.UNARY, NTSymbol.FACTOR1));
        table.put(new TableEntry(NTSymbol.FACTOR, TSymbol.NULL).hashCode(), List.of(NTSymbol.UNARY, NTSymbol.FACTOR1));
        table.put(new TableEntry(NTSymbol.FACTOR, TSymbol.NUMBER).hashCode(), List.of(NTSymbol.UNARY, NTSymbol.FACTOR1));
        table.put(new TableEntry(NTSymbol.FACTOR, TSymbol.STRING).hashCode(), List.of(NTSymbol.UNARY, NTSymbol.FACTOR1));
        table.put(new TableEntry(NTSymbol.FACTOR, TSymbol.LEFT_PAREN).hashCode(), List.of(NTSymbol.UNARY, NTSymbol.FACTOR1));

        table.put(new TableEntry(NTSymbol.FACTOR1, TSymbol.SLASH).hashCode(), List.of(TSymbol.SLASH, NTSymbol.FACTOR));
        table.put(new TableEntry(NTSymbol.FACTOR1, TSymbol.STAR).hashCode(), List.of(TSymbol.STAR, NTSymbol.FACTOR));
        table.put(new TableEntry(NTSymbol.FACTOR1, TSymbol.MINUS).hashCode(), List.of(TSymbol.EPSILON));
        table.put(new TableEntry(NTSymbol.FACTOR1, TSymbol.PLUS).hashCode(), List.of(TSymbol.EPSILON));
        table.put(new TableEntry(NTSymbol.FACTOR1, TSymbol.GT).hashCode(), List.of(TSymbol.EPSILON));
        table.put(new TableEntry(NTSymbol.FACTOR1, TSymbol.GE).hashCode(), List.of(TSymbol.EPSILON));
        table.put(new TableEntry(NTSymbol.FACTOR1, TSymbol.LT).hashCode(), List.of(TSymbol.EPSILON));
        table.put(new TableEntry(NTSymbol.FACTOR1, TSymbol.LE).hashCode(), List.of(TSymbol.EPSILON));
        table.put(new TableEntry(NTSymbol.FACTOR1, TSymbol.EQ).hashCode(), List.of(TSymbol.EPSILON));
        table.put(new TableEntry(NTSymbol.FACTOR1, TSymbol.NOT).hashCode(), List.of(TSymbol.EPSILON));
        table.put(new TableEntry(NTSymbol.FACTOR1, TSymbol.AND).hashCode(), List.of(TSymbol.EPSILON));
        table.put(new TableEntry(NTSymbol.FACTOR1, TSymbol.OR).hashCode(), List.of(TSymbol.EPSILON));
        table.put(new TableEntry(NTSymbol.FACTOR1, TSymbol.EOF).hashCode(), List.of(TSymbol.EPSILON));
        table.put(new TableEntry(NTSymbol.FACTOR1, TSymbol.COMA).hashCode(), List.of(TSymbol.EPSILON));
        table.put(new TableEntry(NTSymbol.FACTOR1, TSymbol.FROM).hashCode(), List.of(TSymbol.EPSILON));
        table.put(new TableEntry(NTSymbol.FACTOR1, TSymbol.RIGHT_PAREN).hashCode(), List.of(TSymbol.EPSILON));

        table.put(new TableEntry(NTSymbol.UNARY, TSymbol.NOT).hashCode(), List.of(TSymbol.NOT, NTSymbol.UNARY));
        table.put(new TableEntry(NTSymbol.UNARY, TSymbol.MINUS).hashCode(), List.of(TSymbol.MINUS, NTSymbol.UNARY));
        table.put(new TableEntry(NTSymbol.UNARY, TSymbol.TRUE).hashCode(), List.of(NTSymbol.CALL));
        table.put(new TableEntry(NTSymbol.UNARY, TSymbol.FALSE).hashCode(), List.of(NTSymbol.CALL));
        table.put(new TableEntry(NTSymbol.UNARY, TSymbol.NULL).hashCode(), List.of(NTSymbol.CALL));
        table.put(new TableEntry(NTSymbol.UNARY, TSymbol.NUMBER).hashCode(), List.of(NTSymbol.CALL));
        table.put(new TableEntry(NTSymbol.UNARY, TSymbol.STRING).hashCode(), List.of(NTSymbol.CALL));
        table.put(new TableEntry(NTSymbol.UNARY, TSymbol.ID).hashCode(), List.of(NTSymbol.CALL));
        table.put(new TableEntry(NTSymbol.UNARY, TSymbol.LEFT_PAREN).hashCode(), List.of(NTSymbol.CALL));

        table.put(new TableEntry(NTSymbol.CALL, TSymbol.TRUE).hashCode(), List.of(NTSymbol.PRIMARY, NTSymbol.CALL1));
        table.put(new TableEntry(NTSymbol.CALL, TSymbol.FALSE).hashCode(), List.of(NTSymbol.PRIMARY, NTSymbol.CALL1));
        table.put(new TableEntry(NTSymbol.CALL, TSymbol.NULL).hashCode(), List.of(NTSymbol.PRIMARY, NTSymbol.CALL1));
        table.put(new TableEntry(NTSymbol.CALL, TSymbol.NUMBER).hashCode(), List.of(NTSymbol.PRIMARY, NTSymbol.CALL1));
        table.put(new TableEntry(NTSymbol.CALL, TSymbol.STRING).hashCode(), List.of(NTSymbol.PRIMARY, NTSymbol.CALL1));
        table.put(new TableEntry(NTSymbol.CALL, TSymbol.ID).hashCode(), List.of(NTSymbol.PRIMARY, NTSymbol.CALL1));
        table.put(new TableEntry(NTSymbol.CALL, TSymbol.LEFT_PAREN).hashCode(), List.of(NTSymbol.PRIMARY, NTSymbol.CALL1));

        table.put(new TableEntry(NTSymbol.CALL1, TSymbol.LEFT_PAREN).hashCode(), List.of(TSymbol.LEFT_PAREN, NTSymbol.ARGUMENTS, TSymbol.RIGHT_PAREN));
        table.put(new TableEntry(NTSymbol.CALL1, TSymbol.SLASH).hashCode(), List.of(TSymbol.EPSILON));
        table.put(new TableEntry(NTSymbol.CALL1, TSymbol.STAR).hashCode(), List.of(TSymbol.EPSILON));
        table.put(new TableEntry(NTSymbol.CALL1, TSymbol.MINUS).hashCode(), List.of(TSymbol.EPSILON));
        table.put(new TableEntry(NTSymbol.CALL1, TSymbol.PLUS).hashCode(), List.of(TSymbol.EPSILON));
        table.put(new TableEntry(NTSymbol.CALL1, TSymbol.GT).hashCode(), List.of(TSymbol.EPSILON));
        table.put(new TableEntry(NTSymbol.CALL1, TSymbol.GE).hashCode(), List.of(TSymbol.EPSILON));
        table.put(new TableEntry(NTSymbol.CALL1, TSymbol.LT).hashCode(), List.of(TSymbol.EPSILON));
        table.put(new TableEntry(NTSymbol.CALL1, TSymbol.LE).hashCode(), List.of(TSymbol.EPSILON));
        table.put(new TableEntry(NTSymbol.CALL1, TSymbol.EQ).hashCode(), List.of(TSymbol.EPSILON));
        table.put(new TableEntry(NTSymbol.CALL1, TSymbol.NE).hashCode(), List.of(TSymbol.EPSILON));
        table.put(new TableEntry(NTSymbol.CALL1, TSymbol.AND).hashCode(), List.of(TSymbol.EPSILON));
        table.put(new TableEntry(NTSymbol.CALL1, TSymbol.OR).hashCode(), List.of(TSymbol.EPSILON));
        table.put(new TableEntry(NTSymbol.CALL1, TSymbol.COMA).hashCode(), List.of(TSymbol.EPSILON));
        table.put(new TableEntry(NTSymbol.CALL1, TSymbol.FROM).hashCode(), List.of(TSymbol.EPSILON));
        table.put(new TableEntry(NTSymbol.CALL1, TSymbol.EOF).hashCode(), List.of(TSymbol.EPSILON));
        table.put(new TableEntry(NTSymbol.CALL1, TSymbol.RIGHT_PAREN).hashCode(), List.of(TSymbol.EPSILON));

        table.put(new TableEntry(NTSymbol.PRIMARY, TSymbol.TRUE).hashCode(), List.of(TSymbol.TRUE));
        table.put(new TableEntry(NTSymbol.PRIMARY, TSymbol.FALSE).hashCode(), List.of(TSymbol.FALSE));
        table.put(new TableEntry(NTSymbol.PRIMARY, TSymbol.NULL).hashCode(), List.of(TSymbol.NULL));
        table.put(new TableEntry(NTSymbol.PRIMARY, TSymbol.NUMBER).hashCode(), List.of(TSymbol.NUMBER));
        table.put(new TableEntry(NTSymbol.PRIMARY, TSymbol.STRING).hashCode(), List.of(TSymbol.STRING));
        table.put(new TableEntry(NTSymbol.PRIMARY, TSymbol.ID).hashCode(), List.of(TSymbol.ID, NTSymbol.ALIAS_OPC));
        table.put(new TableEntry(NTSymbol.PRIMARY, TSymbol.LEFT_PAREN).hashCode(), List.of(TSymbol.LEFT_PAREN, NTSymbol.EXPR, TSymbol.RIGHT_PAREN));

        table.put(new TableEntry(NTSymbol.ALIAS_OPC, TSymbol.DOT).hashCode(), List.of(TSymbol.DOT, TSymbol.ID));
        table.put(new TableEntry(NTSymbol.ALIAS_OPC, TSymbol.LEFT_PAREN).hashCode(), List.of(TSymbol.EPSILON));
        table.put(new TableEntry(NTSymbol.ALIAS_OPC, TSymbol.SLASH).hashCode(), List.of(TSymbol.EPSILON));
        table.put(new TableEntry(NTSymbol.ALIAS_OPC, TSymbol.STAR).hashCode(), List.of(TSymbol.EPSILON));
        table.put(new TableEntry(NTSymbol.ALIAS_OPC, TSymbol.MINUS).hashCode(), List.of(TSymbol.EPSILON));
        table.put(new TableEntry(NTSymbol.ALIAS_OPC, TSymbol.PLUS).hashCode(), List.of(TSymbol.EPSILON));
        table.put(new TableEntry(NTSymbol.ALIAS_OPC, TSymbol.GT).hashCode(), List.of(TSymbol.EPSILON));
        table.put(new TableEntry(NTSymbol.ALIAS_OPC, TSymbol.GE).hashCode(), List.of(TSymbol.EPSILON));
        table.put(new TableEntry(NTSymbol.ALIAS_OPC, TSymbol.LT).hashCode(), List.of(TSymbol.EPSILON));
        table.put(new TableEntry(NTSymbol.ALIAS_OPC, TSymbol.LE).hashCode(), List.of(TSymbol.EPSILON));
        table.put(new TableEntry(NTSymbol.ALIAS_OPC, TSymbol.EQ).hashCode(), List.of(TSymbol.EPSILON));
        table.put(new TableEntry(NTSymbol.ALIAS_OPC, TSymbol.NE).hashCode(), List.of(TSymbol.EPSILON));
        table.put(new TableEntry(NTSymbol.ALIAS_OPC, TSymbol.AND).hashCode(), List.of(TSymbol.EPSILON));
        table.put(new TableEntry(NTSymbol.ALIAS_OPC, TSymbol.OR).hashCode(), List.of(TSymbol.EPSILON));
        table.put(new TableEntry(NTSymbol.ALIAS_OPC, TSymbol.COMA).hashCode(), List.of(TSymbol.EPSILON));
        table.put(new TableEntry(NTSymbol.ALIAS_OPC, TSymbol.FROM).hashCode(), List.of(TSymbol.EPSILON));
        table.put(new TableEntry(NTSymbol.ALIAS_OPC, TSymbol.EOF).hashCode(), List.of(TSymbol.EPSILON));
        table.put(new TableEntry(NTSymbol.ALIAS_OPC, TSymbol.RIGHT_PAREN).hashCode(), List.of(TSymbol.EPSILON));

        table.put(new TableEntry(NTSymbol.ARGUMENTS, TSymbol.NOT).hashCode(), List.of(NTSymbol.EXPR, NTSymbol.ARGUMENTS1));
        table.put(new TableEntry(NTSymbol.ARGUMENTS, TSymbol.MINUS).hashCode(), List.of(NTSymbol.EXPR, NTSymbol.ARGUMENTS1));
        table.put(new TableEntry(NTSymbol.ARGUMENTS, TSymbol.TRUE).hashCode(), List.of(NTSymbol.EXPR, NTSymbol.ARGUMENTS1));
        table.put(new TableEntry(NTSymbol.ARGUMENTS, TSymbol.FALSE).hashCode(), List.of(NTSymbol.EXPR, NTSymbol.ARGUMENTS1));
        table.put(new TableEntry(NTSymbol.ARGUMENTS, TSymbol.NULL).hashCode(), List.of(NTSymbol.EXPR, NTSymbol.ARGUMENTS1));
        table.put(new TableEntry(NTSymbol.ARGUMENTS, TSymbol.NUMBER).hashCode(), List.of(NTSymbol.EXPR, NTSymbol.ARGUMENTS1));
        table.put(new TableEntry(NTSymbol.ARGUMENTS, TSymbol.STRING).hashCode(), List.of(NTSymbol.EXPR, NTSymbol.ARGUMENTS1));
        table.put(new TableEntry(NTSymbol.ARGUMENTS, TSymbol.ID).hashCode(), List.of(NTSymbol.EXPR, NTSymbol.ARGUMENTS1));
        table.put(new TableEntry(NTSymbol.ARGUMENTS, TSymbol.LEFT_PAREN).hashCode(), List.of(NTSymbol.EXPR, NTSymbol.ARGUMENTS1));
        table.put(new TableEntry(NTSymbol.ARGUMENTS, TSymbol.RIGHT_PAREN).hashCode(), List.of(TSymbol.EPSILON));

        table.put(new TableEntry(NTSymbol.ARGUMENTS1, TSymbol.COMA).hashCode(), List.of(TSymbol.COMA, NTSymbol.EXPR, NTSymbol.ARGUMENTS1));
        table.put(new TableEntry(NTSymbol.ARGUMENTS1, TSymbol.RIGHT_PAREN).hashCode(), List.of(TSymbol.EPSILON));

        Stack<Symbol> stack = new Stack<>();
        stack.push(TSymbol.EOF);
        stack.push(NTSymbol.Q);
        Symbol X = stack.peek();

        while (!X.equals(TSymbol.EOF)) {
            if (X.equals(lookahead.type)) {
                stack.pop();
                match(X);
            } else if (X instanceof TSymbol) {
                System.out.println("== ERROR (Parser): bad token at " + lookahead.line);
                System.out.println("== EXpected a " + X + " but got " + lookahead.type);
                break;
            } else {
                List<Symbol> production = table.get(new TableEntry((NTSymbol) X, lookahead.type).hashCode());
                if (production == null) {
                    System.out.println("== ERROR (Parser): no production for [" + X + "," + lookahead.type + "]");
                    break;
                } else {
                    stack.pop();
                    for (Symbol symbol : production) {
                        if (!symbol.equals(TSymbol.EPSILON))
                            stack.push(symbol);
                    }
                }
            }
            X = stack.peek();
        }
    }
}
