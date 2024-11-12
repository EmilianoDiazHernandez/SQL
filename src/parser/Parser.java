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


        /*Mi parte:
        * -Para Expr:*/
        tabla.put(new TableEntry(NTSymbol.EXPR, TSymbol.NOT).hashCode(),List.of(NTSymbol.LOGIC_OR));
        tabla.put(new TableEntry(NTSymbol.EXPR, TSymbol.MINUS).hashCode(),List.of(NTSymbol.LOGIC_OR));
        tabla.put(new TableEntry(NTSymbol.EXPR, TSymbol.TRUE).hashCode(),List.of(NTSymbol.LOGIC_OR));
        tabla.put(new TableEntry(NTSymbol.EXPR, TSymbol.FALSE).hashCode(),List.of(NTSymbol.LOGIC_OR));
        tabla.put(new TableEntry(NTSymbol.EXPR, TSymbol.NULL).hashCode(),List.of(NTSymbol.LOGIC_OR));
        tabla.put(new TableEntry(NTSymbol.EXPR, TSymbol.NUMBER).hashCode(),List.of(NTSymbol.LOGIC_OR));
        tabla.put(new TableEntry(NTSymbol.EXPR, TSymbol.STRING).hashCode(),List.of(NTSymbol.LOGIC_OR));
        tabla.put(new TableEntry(NTSymbol.EXPR, TSymbol.ID).hashCode(),List.of(NTSymbol.LOGIC_OR));
        tabla.put(new TableEntry(NTSymbol.EXPR, TSymbol.LEFT_PAREN).hashCode(),List.of(NTSymbol.LOGIC_OR));

        //ParaLogicOR
        tabla.put(new TableEntry(NTSymbol.LOGIC_OR, TSymbol.NOT).hashCode(),List.of(NTSymbol.LOGIC_AND));
        tabla.put(new TableEntry(NTSymbol.LOGIC_OR, TSymbol.MINUS).hashCode(),List.of(NTSymbol.LOGIC_AND));
        tabla.put(new TableEntry(NTSymbol.LOGIC_OR, TSymbol.TRUE).hashCode(),List.of(NTSymbol.LOGIC_AND));
        tabla.put(new TableEntry(NTSymbol.LOGIC_OR, TSymbol.FALSE).hashCode(),List.of(NTSymbol.LOGIC_AND));
        tabla.put(new TableEntry(NTSymbol.LOGIC_OR, TSymbol.NULL).hashCode(),List.of(NTSymbol.LOGIC_AND));
        tabla.put(new TableEntry(NTSymbol.LOGIC_OR, TSymbol.NUMBER).hashCode(),List.of(NTSymbol.LOGIC_AND));
        tabla.put(new TableEntry(NTSymbol.LOGIC_OR, TSymbol.STRING).hashCode(),List.of(NTSymbol.LOGIC_AND));
        tabla.put(new TableEntry(NTSymbol.LOGIC_OR, TSymbol.ID).hashCode(),List.of(NTSymbol.LOGIC_AND));
        tabla.put(new TableEntry(NTSymbol.LOGIC_OR, TSymbol.LEFT_PAREN).hashCode(),List.of(NTSymbol.LOGIC_AND));

        //Para LogicOr1,duda en or y right_paren
        tabla.put(new TableEntry(NTSymbol.LOGIC_OR1, TSymbol.OR).hashCode(),List.of( TSymbol.OR,NTSymbol.LOGIC_OR));
        tabla.put(new TableEntry(NTSymbol.LOGIC_OR1, TSymbol.EOF).hashCode(),List.of( TSymbol.EPSILON));
        tabla.put(new TableEntry(NTSymbol.LOGIC_OR1, TSymbol.RIGHT_PAREN).hashCode(),List.of( TSymbol.EPSILON));
        tabla.put(new TableEntry(NTSymbol.LOGIC_OR1, TSymbol.COMA).hashCode(),List.of( TSymbol.EPSILON));
        tabla.put(new TableEntry(NTSymbol.LOGIC_OR1, TSymbol.FROM).hashCode(),List.of( TSymbol.EPSILON));

        //Para LogicAnd
        tabla.put(new TableEntry(NTSymbol.LOGIC_AND, TSymbol.NOT).hashCode(),List.of(NTSymbol.EQUALITY));
        tabla.put(new TableEntry(NTSymbol.LOGIC_AND, TSymbol.MINUS).hashCode(),List.of(NTSymbol.EQUALITY));
        tabla.put(new TableEntry(NTSymbol.LOGIC_AND, TSymbol.TRUE).hashCode(),List.of(NTSymbol.EQUALITY));
        tabla.put(new TableEntry(NTSymbol.LOGIC_AND, TSymbol.FALSE).hashCode(),List.of(NTSymbol.EQUALITY));
        tabla.put(new TableEntry(NTSymbol.LOGIC_AND, TSymbol.NULL).hashCode(),List.of(NTSymbol.EQUALITY));
        tabla.put(new TableEntry(NTSymbol.LOGIC_AND, TSymbol.NUMBER).hashCode(),List.of(NTSymbol.EQUALITY));
        tabla.put(new TableEntry(NTSymbol.LOGIC_AND, TSymbol.STRING).hashCode(),List.of(NTSymbol.EQUALITY));
        tabla.put(new TableEntry(NTSymbol.LOGIC_AND, TSymbol.ID).hashCode(),List.of(NTSymbol.EQUALITY));
        tabla.put(new TableEntry(NTSymbol.LOGIC_AND, TSymbol.LEFT_PAREN).hashCode(),List.of(NTSymbol.EQUALITY));

        //Para LogicAnd1
        tabla.put(new TableEntry(NTSymbol.LOGIC_AND1, TSymbol.AND).hashCode(),List.of( TSymbol.AND,NTSymbol.LOGIC_AND));
        tabla.put(new TableEntry(NTSymbol.LOGIC_AND1, TSymbol.EOF).hashCode(),List.of( TSymbol.EPSILON));
        tabla.put(new TableEntry(NTSymbol.LOGIC_AND1, TSymbol.RIGHT_PAREN).hashCode(),List.of( TSymbol.EPSILON));
        tabla.put(new TableEntry(NTSymbol.LOGIC_AND1, TSymbol.COMA).hashCode(),List.of( TSymbol.EPSILON));
        tabla.put(new TableEntry(NTSymbol.LOGIC_AND1, TSymbol.FROM).hashCode(),List.of( TSymbol.EPSILON));
        tabla.put(new TableEntry(NTSymbol.LOGIC_AND1, TSymbol.OR).hashCode(),List.of( TSymbol.EPSILON));

        //Para Equality
        tabla.put(new TableEntry(NTSymbol.EQUALITY, TSymbol.NOT).hashCode(),List.of(NTSymbol.COMPARISON));
        tabla.put(new TableEntry(NTSymbol.EQUALITY, TSymbol.MINUS).hashCode(),List.of(NTSymbol.COMPARISON));
        tabla.put(new TableEntry(NTSymbol.EQUALITY, TSymbol.TRUE).hashCode(),List.of(NTSymbol.COMPARISON));
        tabla.put(new TableEntry(NTSymbol.EQUALITY, TSymbol.FALSE).hashCode(),List.of(NTSymbol.COMPARISON));
        tabla.put(new TableEntry(NTSymbol.EQUALITY, TSymbol.NULL).hashCode(),List.of(NTSymbol.COMPARISON));
        tabla.put(new TableEntry(NTSymbol.EQUALITY, TSymbol.NUMBER).hashCode(),List.of(NTSymbol.COMPARISON));
        tabla.put(new TableEntry(NTSymbol.EQUALITY, TSymbol.STRING).hashCode(),List.of(NTSymbol.COMPARISON));
        tabla.put(new TableEntry(NTSymbol.EQUALITY, TSymbol.ID).hashCode(),List.of(NTSymbol.COMPARISON));
        tabla.put(new TableEntry(NTSymbol.EQUALITY, TSymbol.LEFT_PAREN).hashCode(),List.of(NTSymbol.COMPARISON));
        tabla.put(new TableEntry(NTSymbol.EQUALITY, TSymbol.OR).hashCode(),List.of(NTSymbol.COMPARISON));

        //Para Equality1
        tabla.put(new TableEntry(NTSymbol.EQUALITY1, TSymbol.NE).hashCode(),List.of( TSymbol.NE,NTSymbol.EQUALITY));
        tabla.put(new TableEntry(NTSymbol.EQUALITY1, TSymbol.EQ).hashCode(),List.of( TSymbol.EQ,NTSymbol.EQUALITY));
        tabla.put(new TableEntry(NTSymbol.EQUALITY1, TSymbol.EOF).hashCode(),List.of( TSymbol.EPSILON));
        tabla.put(new TableEntry(NTSymbol.EQUALITY1, TSymbol.RIGHT_PAREN).hashCode(),List.of( TSymbol.EPSILON));
        tabla.put(new TableEntry(NTSymbol.EQUALITY1, TSymbol.COMA).hashCode(),List.of( TSymbol.EPSILON));
        tabla.put(new TableEntry(NTSymbol.EQUALITY1, TSymbol.FROM).hashCode(),List.of( TSymbol.EPSILON));
        tabla.put(new TableEntry(NTSymbol.EQUALITY1, TSymbol.OR).hashCode(),List.of( TSymbol.EPSILON));
        tabla.put(new TableEntry(NTSymbol.EQUALITY1, TSymbol.AND).hashCode(),List.of( TSymbol.EPSILON));

        //Para Comparison
        tabla.put(new TableEntry(NTSymbol.COMPARISON, TSymbol.NOT).hashCode(),List.of(NTSymbol.TERM));
        tabla.put(new TableEntry(NTSymbol.COMPARISON, TSymbol.MINUS).hashCode(),List.of(NTSymbol.TERM));
        tabla.put(new TableEntry(NTSymbol.COMPARISON, TSymbol.TRUE).hashCode(),List.of(NTSymbol.TERM));
        tabla.put(new TableEntry(NTSymbol.COMPARISON, TSymbol.FALSE).hashCode(),List.of(NTSymbol.TERM));
        tabla.put(new TableEntry(NTSymbol.COMPARISON, TSymbol.NULL).hashCode(),List.of(NTSymbol.TERM));
        tabla.put(new TableEntry(NTSymbol.COMPARISON, TSymbol.NUMBER).hashCode(),List.of(NTSymbol.TERM));
        tabla.put(new TableEntry(NTSymbol.COMPARISON, TSymbol.STRING).hashCode(),List.of(NTSymbol.TERM));
        tabla.put(new TableEntry(NTSymbol.COMPARISON, TSymbol.ID).hashCode(),List.of(NTSymbol.TERM));
        tabla.put(new TableEntry(NTSymbol.COMPARISON, TSymbol.LEFT_PAREN).hashCode(),List.of(NTSymbol.TERM));
        tabla.put(new TableEntry(NTSymbol.COMPARISON, TSymbol.OR).hashCode(),List.of(NTSymbol.COMPARISON));

        //Para Comparison1 DUDA en los simbolos
        tabla.put(new TableEntry(NTSymbol.COMPARISON1, TSymbol.GT).hashCode(),List.of( TSymbol.GT,NTSymbol.COMPARISON));
        tabla.put(new TableEntry(NTSymbol.COMPARISON1, TSymbol.GE).hashCode(),List.of( TSymbol.GE,NTSymbol.COMPARISON));
        tabla.put(new TableEntry(NTSymbol.COMPARISON1, TSymbol.LT).hashCode(),List.of( TSymbol.LT,NTSymbol.COMPARISON));
        tabla.put(new TableEntry(NTSymbol.COMPARISON1, TSymbol.LE).hashCode(),List.of( TSymbol.LE,NTSymbol.COMPARISON));
        tabla.put(new TableEntry(NTSymbol.COMPARISON1, TSymbol.RIGHT_PAREN).hashCode(),List.of(TSymbol.EPSILON));
        tabla.put(new TableEntry(NTSymbol.COMPARISON1, TSymbol.EOF).hashCode(),List.of( TSymbol.EPSILON));
        tabla.put(new TableEntry(NTSymbol.COMPARISON1, TSymbol.OR).hashCode(),List.of( TSymbol.EPSILON));
        tabla.put(new TableEntry(NTSymbol.COMPARISON1, TSymbol.AND).hashCode(),List.of( TSymbol.EPSILON));
        tabla.put(new TableEntry(NTSymbol.COMPARISON1, TSymbol.EQ).hashCode(),List.of( TSymbol.EPSILON));
        tabla.put(new TableEntry(NTSymbol.COMPARISON1, TSymbol.NE).hashCode(),List.of( TSymbol.EPSILON));
        tabla.put(new TableEntry(NTSymbol.COMPARISON1, TSymbol.FROM).hashCode(),List.of( TSymbol.EPSILON));
        tabla.put(new TableEntry(NTSymbol.COMPARISON1, TSymbol.COMA).hashCode(),List.of( TSymbol.EPSILON));

        //Para Term
        tabla.put(new TableEntry(NTSymbol.TERM, TSymbol.NOT).hashCode(),List.of(NTSymbol.FACTOR));
        tabla.put(new TableEntry(NTSymbol.TERM, TSymbol.MINUS).hashCode(),List.of(NTSymbol.FACTOR));
        tabla.put(new TableEntry(NTSymbol.TERM, TSymbol.TRUE).hashCode(),List.of(NTSymbol.FACTOR));
        tabla.put(new TableEntry(NTSymbol.TERM, TSymbol.FALSE).hashCode(),List.of(NTSymbol.FACTOR));
        tabla.put(new TableEntry(NTSymbol.TERM, TSymbol.NULL).hashCode(),List.of(NTSymbol.FACTOR));
        tabla.put(new TableEntry(NTSymbol.TERM, TSymbol.NUMBER).hashCode(),List.of(NTSymbol.FACTOR));
        tabla.put(new TableEntry(NTSymbol.TERM, TSymbol.STRING).hashCode(),List.of(NTSymbol.FACTOR));
        tabla.put(new TableEntry(NTSymbol.TERM, TSymbol.ID).hashCode(),List.of(NTSymbol.FACTOR));
        tabla.put(new TableEntry(NTSymbol.TERM, TSymbol.LEFT_PAREN).hashCode(),List.of(NTSymbol.FACTOR));








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
