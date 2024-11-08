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
            if (tokeIndex >= tokenSize) {
                state = ParserState.ERROR;
            } else {
                lookahead = tokens.get(tokeIndex);
            }
        } else {
            state = ParserState.ERROR;
            System.out.println("== ERROR (Parser): bad token at " + lookahead.line);
            System.out.println("==== Expected a " + type + " but got " + lookahead.lexeme);
        }
    }

    public void parse() {
        Map<Integer, List<Symbol>> tabla = new HashMap<>();
        tabla.put(new TableEntry(NTSymbol.Q, TSymbol.SELECT).hashCode(), List.of(TSymbol.SELECT, NTSymbol.D, TSymbol.FROM, NTSymbol.T, NTSymbol.W));

        Stack<Symbol> stack = new Stack<>();
        stack.push(TSymbol.EOF);
        stack.push(NTSymbol.Q);
        Symbol x = stack.peek();

        while (x != TSymbol.EOF) {
            if (x == lookahead.type) {
                stack.pop();
                match(x);
            } else if (x instanceof TSymbol) {
                state = ParserState.ERROR;
                System.out.println("== ERROR (Parser): bad token at " + lookahead.line);
                System.out.println("==== Expected a " + x + " but got " + lookahead.lexeme);
                break;
            } else {
                List<Symbol> production = tabla.get(new TableEntry((NTSymbol) x, lookahead.type).hashCode());
                if (production == null) {
                    System.out.println("== ERROR (Parser): No " + lookahead.line);
                    System.out.println("==== Expected a " + x + " but got " + lookahead.lexeme);
                    break;
                } else {
                    stack.pop();
                    for (Symbol symbol : production) {
                        stack.push(symbol);
                    }
                }
            }
            x = stack.peek();
        }
    }
}
