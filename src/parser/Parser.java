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

        table.put(new TableEntry(NTSymbol.D, TSymbol.DISTINCT).hashCode(), List.of(TSymbol.DISTINCT, NTSymbol.P));
        table.put(new TableEntry(NTSymbol.D, TSymbol.STAR).hashCode(), List.of(NTSymbol.P));
        table.put(new TableEntry(NTSymbol.D, TSymbol.ID).hashCode(), List.of(NTSymbol.P));
        table.put(new TableEntry(NTSymbol.D, TSymbol.MINUS).hashCode(), List.of(NTSymbol.P));
        table.put(new TableEntry(NTSymbol.D, TSymbol.TRUE).hashCode(), List.of(NTSymbol.P));
        table.put(new TableEntry(NTSymbol.D, TSymbol.FALSE).hashCode(), List.of(NTSymbol.P));
        table.put(new TableEntry(NTSymbol.D, TSymbol.NULL).hashCode(), List.of(NTSymbol.P));
        table.put(new TableEntry(NTSymbol.D, TSymbol.NUMBER).hashCode(), List.of(NTSymbol.P));
        table.put(new TableEntry(NTSymbol.D, TSymbol.STRING).hashCode(), List.of(NTSymbol.P));
        table.put(new TableEntry(NTSymbol.D, TSymbol.NOT).hashCode(), List.of(NTSymbol.P));
        table.put(new TableEntry(NTSymbol.D, TSymbol.LEFT_PAREN).hashCode(), List.of(NTSymbol.P));

        table.put(new TableEntry(NTSymbol.P, TSymbol.STAR).hashCode(), List.of(TSymbol.STAR));
        table.put(new TableEntry(NTSymbol.P, TSymbol.ID).hashCode(), List.of(NTSymbol.F));
        table.put(new TableEntry(NTSymbol.P, TSymbol.MINUS).hashCode(), List.of(NTSymbol.F));
        table.put(new TableEntry(NTSymbol.P, TSymbol.TRUE).hashCode(), List.of(NTSymbol.F));
        table.put(new TableEntry(NTSymbol.P, TSymbol.FALSE).hashCode(), List.of(NTSymbol.F));
        table.put(new TableEntry(NTSymbol.P, TSymbol.NULL).hashCode(), List.of(NTSymbol.F));
        table.put(new TableEntry(NTSymbol.P, TSymbol.NUMBER).hashCode(), List.of(NTSymbol.F));
        table.put(new TableEntry(NTSymbol.P, TSymbol.STRING).hashCode(), List.of(NTSymbol.F));
        table.put(new TableEntry(NTSymbol.P, TSymbol.NOT).hashCode(), List.of(NTSymbol.F));
        table.put(new TableEntry(NTSymbol.P, TSymbol.LEFT_PAREN).hashCode(), List.of(NTSymbol.F));

        table.put(new TableEntry(NTSymbol.F, TSymbol.ID).hashCode(), List.of(NTSymbol.EXPR, NTSymbol.F1));
        table.put(new TableEntry(NTSymbol.F, TSymbol.MINUS).hashCode(), List.of(NTSymbol.EXPR, NTSymbol.F1));
        table.put(new TableEntry(NTSymbol.F, TSymbol.TRUE).hashCode(), List.of(NTSymbol.EXPR, NTSymbol.F1));
        table.put(new TableEntry(NTSymbol.F, TSymbol.FALSE).hashCode(), List.of(NTSymbol.EXPR, NTSymbol.F1));
        table.put(new TableEntry(NTSymbol.F, TSymbol.NULL).hashCode(), List.of(NTSymbol.EXPR, NTSymbol.F1));
        table.put(new TableEntry(NTSymbol.F, TSymbol.NUMBER).hashCode(), List.of(NTSymbol.EXPR, NTSymbol.F1));
        table.put(new TableEntry(NTSymbol.F, TSymbol.STRING).hashCode(), List.of(NTSymbol.EXPR, NTSymbol.F1));
        table.put(new TableEntry(NTSymbol.F, TSymbol.NOT).hashCode(), List.of(NTSymbol.EXPR, NTSymbol.F1));
        table.put(new TableEntry(NTSymbol.F, TSymbol.LEFT_PAREN).hashCode(), List.of(NTSymbol.EXPR, NTSymbol.F1));

        table.put(new TableEntry(NTSymbol.F1, TSymbol.FROM).hashCode(),List.of(TSymbol.EPSILON));
        table.put(new TableEntry(NTSymbol.F1, TSymbol.COMA).hashCode(),List.of(TSymbol.COMA, NTSymbol.EXPR, NTSymbol.F1));

        table.put(new TableEntry(NTSymbol.T, TSymbol.ID).hashCode(), List.of(NTSymbol.T1, NTSymbol.T3));

        table.put(new TableEntry(NTSymbol.T1, TSymbol.ID).hashCode(), List.of(TSymbol.ID, NTSymbol.T2));

        table.put(new TableEntry(NTSymbol.T2, TSymbol.COMA).hashCode(), List.of(TSymbol.EPSILON));
        table.put(new TableEntry(NTSymbol.T2, TSymbol.ID).hashCode(), List.of(TSymbol.ID));
        table.put(new TableEntry(NTSymbol.T2, TSymbol.WHERE).hashCode(), List.of(TSymbol.EPSILON));
        table.put(new TableEntry(NTSymbol.T2, TSymbol.EOF).hashCode(), List.of(TSymbol.EPSILON));

        table.put(new TableEntry(NTSymbol.T3, TSymbol.COMA).hashCode(), List.of(TSymbol.COMA, NTSymbol.T));
        table.put(new TableEntry(NTSymbol.T3, TSymbol.WHERE).hashCode(), List.of(TSymbol.EPSILON));
        table.put(new TableEntry(NTSymbol.T3, TSymbol.EOF).hashCode(), List.of(TSymbol.EPSILON));

        table.put(new TableEntry(NTSymbol.W, TSymbol.WHERE).hashCode(), List.of(TSymbol.WHERE, NTSymbol.EXPR));
        table.put(new TableEntry(NTSymbol.W, TSymbol.EOF).hashCode(), List.of(TSymbol.EPSILON));

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


        /*Mi parte:
        * -Para Expr:*/
        table.put(new TableEntry(NTSymbol.EXPR, TSymbol.NOT).hashCode(),List.of(NTSymbol.LOGIC_OR));
        table.put(new TableEntry(NTSymbol.EXPR, TSymbol.MINUS).hashCode(),List.of(NTSymbol.LOGIC_OR));
        table.put(new TableEntry(NTSymbol.EXPR, TSymbol.TRUE).hashCode(),List.of(NTSymbol.LOGIC_OR));
        table.put(new TableEntry(NTSymbol.EXPR, TSymbol.FALSE).hashCode(),List.of(NTSymbol.LOGIC_OR));
        table.put(new TableEntry(NTSymbol.EXPR, TSymbol.NULL).hashCode(),List.of(NTSymbol.LOGIC_OR));
        table.put(new TableEntry(NTSymbol.EXPR, TSymbol.NUMBER).hashCode(),List.of(NTSymbol.LOGIC_OR));
        table.put(new TableEntry(NTSymbol.EXPR, TSymbol.STRING).hashCode(),List.of(NTSymbol.LOGIC_OR));
        table.put(new TableEntry(NTSymbol.EXPR, TSymbol.ID).hashCode(),List.of(NTSymbol.LOGIC_OR));
        table.put(new TableEntry(NTSymbol.EXPR, TSymbol.LEFT_PAREN).hashCode(),List.of(NTSymbol.LOGIC_OR));

        //ParaLogicOR
        table.put(new TableEntry(NTSymbol.LOGIC_OR, TSymbol.NOT).hashCode(),List.of(NTSymbol.LOGIC_AND));
        table.put(new TableEntry(NTSymbol.LOGIC_OR, TSymbol.MINUS).hashCode(),List.of(NTSymbol.LOGIC_AND));
        table.put(new TableEntry(NTSymbol.LOGIC_OR, TSymbol.TRUE).hashCode(),List.of(NTSymbol.LOGIC_AND));
        table.put(new TableEntry(NTSymbol.LOGIC_OR, TSymbol.FALSE).hashCode(),List.of(NTSymbol.LOGIC_AND));
        table.put(new TableEntry(NTSymbol.LOGIC_OR, TSymbol.NULL).hashCode(),List.of(NTSymbol.LOGIC_AND));
        table.put(new TableEntry(NTSymbol.LOGIC_OR, TSymbol.NUMBER).hashCode(),List.of(NTSymbol.LOGIC_AND));
        table.put(new TableEntry(NTSymbol.LOGIC_OR, TSymbol.STRING).hashCode(),List.of(NTSymbol.LOGIC_AND));
        table.put(new TableEntry(NTSymbol.LOGIC_OR, TSymbol.ID).hashCode(),List.of(NTSymbol.LOGIC_AND));
        table.put(new TableEntry(NTSymbol.LOGIC_OR, TSymbol.LEFT_PAREN).hashCode(),List.of(NTSymbol.LOGIC_AND));

        //Para LogicOr1,duda en or y right_paren
        table.put(new TableEntry(NTSymbol.LOGIC_OR1, TSymbol.OR).hashCode(),List.of( TSymbol.OR,NTSymbol.LOGIC_OR));
        table.put(new TableEntry(NTSymbol.LOGIC_OR1, TSymbol.EOF).hashCode(),List.of( TSymbol.EPSILON));
        table.put(new TableEntry(NTSymbol.LOGIC_OR1, TSymbol.RIGHT_PAREN).hashCode(),List.of( TSymbol.EPSILON));
        table.put(new TableEntry(NTSymbol.LOGIC_OR1, TSymbol.COMA).hashCode(),List.of( TSymbol.EPSILON));
        table.put(new TableEntry(NTSymbol.LOGIC_OR1, TSymbol.FROM).hashCode(),List.of( TSymbol.EPSILON));

        //Para LogicAnd
        table.put(new TableEntry(NTSymbol.LOGIC_AND, TSymbol.NOT).hashCode(),List.of(NTSymbol.EQUALITY,NTSymbol.LOGIC_AND1));
        table.put(new TableEntry(NTSymbol.LOGIC_AND, TSymbol.MINUS).hashCode(),List.of(NTSymbol.EQUALITY, NTSymbol.LOGIC_AND1));
        table.put(new TableEntry(NTSymbol.LOGIC_AND, TSymbol.TRUE).hashCode(),List.of(NTSymbol.EQUALITY, NTSymbol.LOGIC_AND1));
        table.put(new TableEntry(NTSymbol.LOGIC_AND, TSymbol.FALSE).hashCode(),List.of(NTSymbol.EQUALITY, NTSymbol.LOGIC_AND1));
        table.put(new TableEntry(NTSymbol.LOGIC_AND, TSymbol.NULL).hashCode(),List.of(NTSymbol.EQUALITY, NTSymbol.LOGIC_AND1));
        table.put(new TableEntry(NTSymbol.LOGIC_AND, TSymbol.NUMBER).hashCode(),List.of(NTSymbol.EQUALITY, NTSymbol.LOGIC_AND1));
        table.put(new TableEntry(NTSymbol.LOGIC_AND, TSymbol.STRING).hashCode(),List.of(NTSymbol.EQUALITY, NTSymbol.LOGIC_AND1));
        table.put(new TableEntry(NTSymbol.LOGIC_AND, TSymbol.ID).hashCode(),List.of(NTSymbol.EQUALITY, NTSymbol.LOGIC_AND1));
        table.put(new TableEntry(NTSymbol.LOGIC_AND, TSymbol.LEFT_PAREN).hashCode(),List.of(NTSymbol.EQUALITY, NTSymbol.LOGIC_AND1));

        //Para LogicAnd1
        table.put(new TableEntry(NTSymbol.LOGIC_AND1, TSymbol.AND).hashCode(),List.of( TSymbol.AND,NTSymbol.LOGIC_AND));
        table.put(new TableEntry(NTSymbol.LOGIC_AND1, TSymbol.EOF).hashCode(),List.of( TSymbol.EPSILON));
        table.put(new TableEntry(NTSymbol.LOGIC_AND1, TSymbol.RIGHT_PAREN).hashCode(),List.of( TSymbol.EPSILON));
        table.put(new TableEntry(NTSymbol.LOGIC_AND1, TSymbol.COMA).hashCode(),List.of( TSymbol.EPSILON));
        table.put(new TableEntry(NTSymbol.LOGIC_AND1, TSymbol.FROM).hashCode(),List.of( TSymbol.EPSILON));
        table.put(new TableEntry(NTSymbol.LOGIC_AND1, TSymbol.OR).hashCode(),List.of( TSymbol.EPSILON));

        //Para Equality
        table.put(new TableEntry(NTSymbol.EQUALITY, TSymbol.NOT).hashCode(),List.of(NTSymbol.COMPARISON,NTSymbol.EQUALITY1));
        table.put(new TableEntry(NTSymbol.EQUALITY, TSymbol.MINUS).hashCode(),List.of(NTSymbol.COMPARISON,NTSymbol.EQUALITY1));
        table.put(new TableEntry(NTSymbol.EQUALITY, TSymbol.TRUE).hashCode(),List.of(NTSymbol.COMPARISON,NTSymbol.EQUALITY1));
        table.put(new TableEntry(NTSymbol.EQUALITY, TSymbol.FALSE).hashCode(),List.of(NTSymbol.COMPARISON,NTSymbol.EQUALITY1));
        table.put(new TableEntry(NTSymbol.EQUALITY, TSymbol.NULL).hashCode(),List.of(NTSymbol.COMPARISON,NTSymbol.EQUALITY1));
        table.put(new TableEntry(NTSymbol.EQUALITY, TSymbol.NUMBER).hashCode(),List.of(NTSymbol.COMPARISON,NTSymbol.EQUALITY1));
        table.put(new TableEntry(NTSymbol.EQUALITY, TSymbol.STRING).hashCode(),List.of(NTSymbol.COMPARISON,NTSymbol.EQUALITY1));
        table.put(new TableEntry(NTSymbol.EQUALITY, TSymbol.ID).hashCode(),List.of(NTSymbol.COMPARISON,NTSymbol.EQUALITY1));
        table.put(new TableEntry(NTSymbol.EQUALITY, TSymbol.LEFT_PAREN).hashCode(),List.of(NTSymbol.COMPARISON,NTSymbol.EQUALITY1));
        table.put(new TableEntry(NTSymbol.EQUALITY, TSymbol.OR).hashCode(),List.of(NTSymbol.COMPARISON,NTSymbol.EQUALITY1));

        //Para Equality1
        table.put(new TableEntry(NTSymbol.EQUALITY1, TSymbol.NE).hashCode(),List.of( TSymbol.NE,NTSymbol.EQUALITY));
        table.put(new TableEntry(NTSymbol.EQUALITY1, TSymbol.EQ).hashCode(),List.of( TSymbol.EQ,NTSymbol.EQUALITY));
        table.put(new TableEntry(NTSymbol.EQUALITY1, TSymbol.EOF).hashCode(),List.of( TSymbol.EPSILON));
        table.put(new TableEntry(NTSymbol.EQUALITY1, TSymbol.RIGHT_PAREN).hashCode(),List.of( TSymbol.EPSILON));
        table.put(new TableEntry(NTSymbol.EQUALITY1, TSymbol.COMA).hashCode(),List.of( TSymbol.EPSILON));
        table.put(new TableEntry(NTSymbol.EQUALITY1, TSymbol.FROM).hashCode(),List.of( TSymbol.EPSILON));
        table.put(new TableEntry(NTSymbol.EQUALITY1, TSymbol.OR).hashCode(),List.of( TSymbol.EPSILON));
        table.put(new TableEntry(NTSymbol.EQUALITY1, TSymbol.AND).hashCode(),List.of( TSymbol.EPSILON));

        //Para Comparison
        table.put(new TableEntry(NTSymbol.COMPARISON, TSymbol.NOT).hashCode(),List.of(NTSymbol.TERM, NTSymbol.COMPARISON1));
        table.put(new TableEntry(NTSymbol.COMPARISON, TSymbol.MINUS).hashCode(),List.of(NTSymbol.TERM, NTSymbol.COMPARISON1));
        table.put(new TableEntry(NTSymbol.COMPARISON, TSymbol.TRUE).hashCode(),List.of(NTSymbol.TERM, NTSymbol.COMPARISON1));
        table.put(new TableEntry(NTSymbol.COMPARISON, TSymbol.FALSE).hashCode(),List.of(NTSymbol.TERM, NTSymbol.COMPARISON1));
        table.put(new TableEntry(NTSymbol.COMPARISON, TSymbol.NULL).hashCode(),List.of(NTSymbol.TERM, NTSymbol.COMPARISON1));
        table.put(new TableEntry(NTSymbol.COMPARISON, TSymbol.NUMBER).hashCode(),List.of(NTSymbol.TERM, NTSymbol.COMPARISON1));
        table.put(new TableEntry(NTSymbol.COMPARISON, TSymbol.STRING).hashCode(),List.of(NTSymbol.TERM, NTSymbol.COMPARISON1));
        table.put(new TableEntry(NTSymbol.COMPARISON, TSymbol.ID).hashCode(),List.of(NTSymbol.TERM, NTSymbol.COMPARISON1));
        table.put(new TableEntry(NTSymbol.COMPARISON, TSymbol.LEFT_PAREN).hashCode(),List.of(NTSymbol.TERM, NTSymbol.COMPARISON1));

        //Para Comparison1 DUDA en los simbolos
        table.put(new TableEntry(NTSymbol.COMPARISON1, TSymbol.GT).hashCode(),List.of( TSymbol.GT,NTSymbol.COMPARISON));
        table.put(new TableEntry(NTSymbol.COMPARISON1, TSymbol.GE).hashCode(),List.of( TSymbol.GE,NTSymbol.COMPARISON));
        table.put(new TableEntry(NTSymbol.COMPARISON1, TSymbol.LT).hashCode(),List.of( TSymbol.LT,NTSymbol.COMPARISON));
        table.put(new TableEntry(NTSymbol.COMPARISON1, TSymbol.LE).hashCode(),List.of( TSymbol.LE,NTSymbol.COMPARISON));
        table.put(new TableEntry(NTSymbol.COMPARISON1, TSymbol.RIGHT_PAREN).hashCode(),List.of(TSymbol.EPSILON));
        table.put(new TableEntry(NTSymbol.COMPARISON1, TSymbol.EOF).hashCode(),List.of( TSymbol.EPSILON));
        table.put(new TableEntry(NTSymbol.COMPARISON1, TSymbol.OR).hashCode(),List.of( TSymbol.EPSILON));
        table.put(new TableEntry(NTSymbol.COMPARISON1, TSymbol.AND).hashCode(),List.of( TSymbol.EPSILON));
        table.put(new TableEntry(NTSymbol.COMPARISON1, TSymbol.EQ).hashCode(),List.of( TSymbol.EPSILON));
        table.put(new TableEntry(NTSymbol.COMPARISON1, TSymbol.NE).hashCode(),List.of( TSymbol.EPSILON));
        table.put(new TableEntry(NTSymbol.COMPARISON1, TSymbol.FROM).hashCode(),List.of( TSymbol.EPSILON));
        table.put(new TableEntry(NTSymbol.COMPARISON1, TSymbol.COMA).hashCode(),List.of( TSymbol.EPSILON));

        //Para Term
        table.put(new TableEntry(NTSymbol.TERM, TSymbol.NOT).hashCode(),List.of(NTSymbol.FACTOR,NTSymbol.TERM1));
        table.put(new TableEntry(NTSymbol.TERM, TSymbol.MINUS).hashCode(),List.of(NTSymbol.FACTOR,NTSymbol.TERM1));
        table.put(new TableEntry(NTSymbol.TERM, TSymbol.TRUE).hashCode(),List.of(NTSymbol.FACTOR,NTSymbol.TERM1));
        table.put(new TableEntry(NTSymbol.TERM, TSymbol.FALSE).hashCode(),List.of(NTSymbol.FACTOR,NTSymbol.TERM1));
        table.put(new TableEntry(NTSymbol.TERM, TSymbol.NULL).hashCode(),List.of(NTSymbol.FACTOR,NTSymbol.TERM1));
        table.put(new TableEntry(NTSymbol.TERM, TSymbol.NUMBER).hashCode(),List.of(NTSymbol.FACTOR,NTSymbol.TERM1));
        table.put(new TableEntry(NTSymbol.TERM, TSymbol.STRING).hashCode(),List.of(NTSymbol.FACTOR,NTSymbol.TERM1));
        table.put(new TableEntry(NTSymbol.TERM, TSymbol.ID).hashCode(),List.of(NTSymbol.FACTOR,NTSymbol.TERM1));
        table.put(new TableEntry(NTSymbol.TERM, TSymbol.LEFT_PAREN).hashCode(),List.of(NTSymbol.FACTOR,NTSymbol.TERM1));








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
                    for (int i=production.size()-1; i>=0; i--) {
                        if (!production.get(i).equals(TSymbol.EPSILON))
                            stack.push(production.get(i));
                    }
                }
            }
            X = stack.peek();
        }
    }
}
