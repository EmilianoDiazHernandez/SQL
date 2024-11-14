package grammar;

import parser.TableEntry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductionTable {
    public static final Map<Integer, List<Symbol>> TABLE = new HashMap<>();

    static {
        TABLE.put(new TableEntry(NTSymbol.Q, TSymbol.SELECT).hashCode(), List.of(TSymbol.SELECT, NTSymbol.D, TSymbol.FROM, NTSymbol.T, NTSymbol.W));

        TABLE.put(new TableEntry(NTSymbol.D, TSymbol.DISTINCT).hashCode(), List.of(TSymbol.DISTINCT, NTSymbol.P));
        TABLE.put(new TableEntry(NTSymbol.D, TSymbol.STAR).hashCode(), List.of(NTSymbol.P));
        TABLE.put(new TableEntry(NTSymbol.D, TSymbol.ID).hashCode(), List.of(NTSymbol.P));
        TABLE.put(new TableEntry(NTSymbol.D, TSymbol.MINUS).hashCode(), List.of(NTSymbol.P));
        TABLE.put(new TableEntry(NTSymbol.D, TSymbol.TRUE).hashCode(), List.of(NTSymbol.P));
        TABLE.put(new TableEntry(NTSymbol.D, TSymbol.FALSE).hashCode(), List.of(NTSymbol.P));
        TABLE.put(new TableEntry(NTSymbol.D, TSymbol.NULL).hashCode(), List.of(NTSymbol.P));
        TABLE.put(new TableEntry(NTSymbol.D, TSymbol.NUMBER).hashCode(), List.of(NTSymbol.P));
        TABLE.put(new TableEntry(NTSymbol.D, TSymbol.STRING).hashCode(), List.of(NTSymbol.P));
        TABLE.put(new TableEntry(NTSymbol.D, TSymbol.NOT).hashCode(), List.of(NTSymbol.P));
        TABLE.put(new TableEntry(NTSymbol.D, TSymbol.LEFT_PAREN).hashCode(), List.of(NTSymbol.P));

        TABLE.put(new TableEntry(NTSymbol.P, TSymbol.STAR).hashCode(), List.of(TSymbol.STAR));
        TABLE.put(new TableEntry(NTSymbol.P, TSymbol.ID).hashCode(), List.of(NTSymbol.F));
        TABLE.put(new TableEntry(NTSymbol.P, TSymbol.MINUS).hashCode(), List.of(NTSymbol.F));
        TABLE.put(new TableEntry(NTSymbol.P, TSymbol.TRUE).hashCode(), List.of(NTSymbol.F));
        TABLE.put(new TableEntry(NTSymbol.P, TSymbol.FALSE).hashCode(), List.of(NTSymbol.F));
        TABLE.put(new TableEntry(NTSymbol.P, TSymbol.NULL).hashCode(), List.of(NTSymbol.F));
        TABLE.put(new TableEntry(NTSymbol.P, TSymbol.NUMBER).hashCode(), List.of(NTSymbol.F));
        TABLE.put(new TableEntry(NTSymbol.P, TSymbol.STRING).hashCode(), List.of(NTSymbol.F));
        TABLE.put(new TableEntry(NTSymbol.P, TSymbol.NOT).hashCode(), List.of(NTSymbol.F));
        TABLE.put(new TableEntry(NTSymbol.P, TSymbol.LEFT_PAREN).hashCode(), List.of(NTSymbol.F));

        TABLE.put(new TableEntry(NTSymbol.F, TSymbol.ID).hashCode(), List.of(NTSymbol.EXPR, NTSymbol.F1));
        TABLE.put(new TableEntry(NTSymbol.F, TSymbol.MINUS).hashCode(), List.of(NTSymbol.EXPR, NTSymbol.F1));
        TABLE.put(new TableEntry(NTSymbol.F, TSymbol.TRUE).hashCode(), List.of(NTSymbol.EXPR, NTSymbol.F1));
        TABLE.put(new TableEntry(NTSymbol.F, TSymbol.FALSE).hashCode(), List.of(NTSymbol.EXPR, NTSymbol.F1));
        TABLE.put(new TableEntry(NTSymbol.F, TSymbol.NULL).hashCode(), List.of(NTSymbol.EXPR, NTSymbol.F1));
        TABLE.put(new TableEntry(NTSymbol.F, TSymbol.NUMBER).hashCode(), List.of(NTSymbol.EXPR, NTSymbol.F1));
        TABLE.put(new TableEntry(NTSymbol.F, TSymbol.STRING).hashCode(), List.of(NTSymbol.EXPR, NTSymbol.F1));
        TABLE.put(new TableEntry(NTSymbol.F, TSymbol.NOT).hashCode(), List.of(NTSymbol.EXPR, NTSymbol.F1));
        TABLE.put(new TableEntry(NTSymbol.F, TSymbol.LEFT_PAREN).hashCode(), List.of(NTSymbol.EXPR, NTSymbol.F1));

        TABLE.put(new TableEntry(NTSymbol.F1, TSymbol.FROM).hashCode(), List.of(TSymbol.EPSILON));
        TABLE.put(new TableEntry(NTSymbol.F1, TSymbol.COMA).hashCode(), List.of(TSymbol.COMA, NTSymbol.EXPR, NTSymbol.F1));

        TABLE.put(new TableEntry(NTSymbol.T, TSymbol.ID).hashCode(), List.of(NTSymbol.T1, NTSymbol.T3));

        TABLE.put(new TableEntry(NTSymbol.T1, TSymbol.ID).hashCode(), List.of(TSymbol.ID, NTSymbol.T2));

        TABLE.put(new TableEntry(NTSymbol.T2, TSymbol.COMA).hashCode(), List.of(TSymbol.EPSILON));
        TABLE.put(new TableEntry(NTSymbol.T2, TSymbol.ID).hashCode(), List.of(TSymbol.ID));
        TABLE.put(new TableEntry(NTSymbol.T2, TSymbol.WHERE).hashCode(), List.of(TSymbol.EPSILON));
        TABLE.put(new TableEntry(NTSymbol.T2, TSymbol.EOF).hashCode(), List.of(TSymbol.EPSILON));

        TABLE.put(new TableEntry(NTSymbol.T3, TSymbol.COMA).hashCode(), List.of(TSymbol.COMA, NTSymbol.T));
        TABLE.put(new TableEntry(NTSymbol.T3, TSymbol.WHERE).hashCode(), List.of(TSymbol.EPSILON));
        TABLE.put(new TableEntry(NTSymbol.T3, TSymbol.EOF).hashCode(), List.of(TSymbol.EPSILON));

        TABLE.put(new TableEntry(NTSymbol.W, TSymbol.WHERE).hashCode(), List.of(TSymbol.WHERE, NTSymbol.EXPR));
        TABLE.put(new TableEntry(NTSymbol.W, TSymbol.EOF).hashCode(), List.of(TSymbol.EPSILON));

        TABLE.put(new TableEntry(NTSymbol.EXPR, TSymbol.NOT).hashCode(), List.of(NTSymbol.LOGIC_OR));
        TABLE.put(new TableEntry(NTSymbol.EXPR, TSymbol.MINUS).hashCode(), List.of(NTSymbol.LOGIC_OR));
        TABLE.put(new TableEntry(NTSymbol.EXPR, TSymbol.TRUE).hashCode(), List.of(NTSymbol.LOGIC_OR));
        TABLE.put(new TableEntry(NTSymbol.EXPR, TSymbol.FALSE).hashCode(), List.of(NTSymbol.LOGIC_OR));
        TABLE.put(new TableEntry(NTSymbol.EXPR, TSymbol.NULL).hashCode(), List.of(NTSymbol.LOGIC_OR));
        TABLE.put(new TableEntry(NTSymbol.EXPR, TSymbol.NUMBER).hashCode(), List.of(NTSymbol.LOGIC_OR));
        TABLE.put(new TableEntry(NTSymbol.EXPR, TSymbol.STRING).hashCode(), List.of(NTSymbol.LOGIC_OR));
        TABLE.put(new TableEntry(NTSymbol.EXPR, TSymbol.ID).hashCode(), List.of(NTSymbol.LOGIC_OR));
        TABLE.put(new TableEntry(NTSymbol.EXPR, TSymbol.LEFT_PAREN).hashCode(), List.of(NTSymbol.LOGIC_OR));

        TABLE.put(new TableEntry(NTSymbol.LOGIC_OR, TSymbol.NOT).hashCode(), List.of(NTSymbol.LOGIC_AND, NTSymbol.LOGIC_OR1));
        TABLE.put(new TableEntry(NTSymbol.LOGIC_OR, TSymbol.MINUS).hashCode(), List.of(NTSymbol.LOGIC_AND, NTSymbol.LOGIC_OR1));
        TABLE.put(new TableEntry(NTSymbol.LOGIC_OR, TSymbol.TRUE).hashCode(), List.of(NTSymbol.LOGIC_AND, NTSymbol.LOGIC_OR1));
        TABLE.put(new TableEntry(NTSymbol.LOGIC_OR, TSymbol.FALSE).hashCode(), List.of(NTSymbol.LOGIC_AND, NTSymbol.LOGIC_OR1));
        TABLE.put(new TableEntry(NTSymbol.LOGIC_OR, TSymbol.NULL).hashCode(), List.of(NTSymbol.LOGIC_AND, NTSymbol.LOGIC_OR1));
        TABLE.put(new TableEntry(NTSymbol.LOGIC_OR, TSymbol.NUMBER).hashCode(), List.of(NTSymbol.LOGIC_AND, NTSymbol.LOGIC_OR1));
        TABLE.put(new TableEntry(NTSymbol.LOGIC_OR, TSymbol.STRING).hashCode(), List.of(NTSymbol.LOGIC_AND, NTSymbol.LOGIC_OR1));
        TABLE.put(new TableEntry(NTSymbol.LOGIC_OR, TSymbol.ID).hashCode(), List.of(NTSymbol.LOGIC_AND, NTSymbol.LOGIC_OR1));
        TABLE.put(new TableEntry(NTSymbol.LOGIC_OR, TSymbol.LEFT_PAREN).hashCode(), List.of(NTSymbol.LOGIC_AND, NTSymbol.LOGIC_OR1));

        TABLE.put(new TableEntry(NTSymbol.LOGIC_OR1, TSymbol.OR).hashCode(), List.of(TSymbol.OR, NTSymbol.LOGIC_OR));
        TABLE.put(new TableEntry(NTSymbol.LOGIC_OR1, TSymbol.RIGHT_PAREN).hashCode(), List.of(TSymbol.EPSILON));
        TABLE.put(new TableEntry(NTSymbol.LOGIC_OR1, TSymbol.EOF).hashCode(), List.of(TSymbol.EPSILON));
        TABLE.put(new TableEntry(NTSymbol.LOGIC_OR1, TSymbol.COMA).hashCode(), List.of(TSymbol.EPSILON));
        TABLE.put(new TableEntry(NTSymbol.LOGIC_OR1, TSymbol.FROM).hashCode(), List.of(TSymbol.EPSILON));

        TABLE.put(new TableEntry(NTSymbol.LOGIC_AND, TSymbol.NOT).hashCode(), List.of(NTSymbol.EQUALITY, NTSymbol.LOGIC_AND1));
        TABLE.put(new TableEntry(NTSymbol.LOGIC_AND, TSymbol.MINUS).hashCode(), List.of(NTSymbol.EQUALITY, NTSymbol.LOGIC_AND1));
        TABLE.put(new TableEntry(NTSymbol.LOGIC_AND, TSymbol.TRUE).hashCode(), List.of(NTSymbol.EQUALITY, NTSymbol.LOGIC_AND1));
        TABLE.put(new TableEntry(NTSymbol.LOGIC_AND, TSymbol.FALSE).hashCode(), List.of(NTSymbol.EQUALITY, NTSymbol.LOGIC_AND1));
        TABLE.put(new TableEntry(NTSymbol.LOGIC_AND, TSymbol.NULL).hashCode(), List.of(NTSymbol.EQUALITY, NTSymbol.LOGIC_AND1));
        TABLE.put(new TableEntry(NTSymbol.LOGIC_AND, TSymbol.NUMBER).hashCode(), List.of(NTSymbol.EQUALITY, NTSymbol.LOGIC_AND1));
        TABLE.put(new TableEntry(NTSymbol.LOGIC_AND, TSymbol.STRING).hashCode(), List.of(NTSymbol.EQUALITY, NTSymbol.LOGIC_AND1));
        TABLE.put(new TableEntry(NTSymbol.LOGIC_AND, TSymbol.ID).hashCode(), List.of(NTSymbol.EQUALITY, NTSymbol.LOGIC_AND1));
        TABLE.put(new TableEntry(NTSymbol.LOGIC_AND, TSymbol.LEFT_PAREN).hashCode(), List.of(NTSymbol.EQUALITY, NTSymbol.LOGIC_AND1));

        TABLE.put(new TableEntry(NTSymbol.LOGIC_AND1, TSymbol.AND).hashCode(), List.of(TSymbol.AND, NTSymbol.LOGIC_AND));
        TABLE.put(new TableEntry(NTSymbol.LOGIC_AND1, TSymbol.EOF).hashCode(), List.of(TSymbol.EPSILON));
        TABLE.put(new TableEntry(NTSymbol.LOGIC_AND1, TSymbol.RIGHT_PAREN).hashCode(), List.of(TSymbol.EPSILON));
        TABLE.put(new TableEntry(NTSymbol.LOGIC_AND1, TSymbol.COMA).hashCode(), List.of(TSymbol.EPSILON));
        TABLE.put(new TableEntry(NTSymbol.LOGIC_AND1, TSymbol.FROM).hashCode(), List.of(TSymbol.EPSILON));
        TABLE.put(new TableEntry(NTSymbol.LOGIC_AND1, TSymbol.OR).hashCode(), List.of(TSymbol.EPSILON));

        TABLE.put(new TableEntry(NTSymbol.EQUALITY, TSymbol.NOT).hashCode(), List.of(NTSymbol.COMPARISON, NTSymbol.EQUALITY1));
        TABLE.put(new TableEntry(NTSymbol.EQUALITY, TSymbol.MINUS).hashCode(), List.of(NTSymbol.COMPARISON, NTSymbol.EQUALITY1));
        TABLE.put(new TableEntry(NTSymbol.EQUALITY, TSymbol.TRUE).hashCode(), List.of(NTSymbol.COMPARISON, NTSymbol.EQUALITY1));
        TABLE.put(new TableEntry(NTSymbol.EQUALITY, TSymbol.FALSE).hashCode(), List.of(NTSymbol.COMPARISON, NTSymbol.EQUALITY1));
        TABLE.put(new TableEntry(NTSymbol.EQUALITY, TSymbol.NULL).hashCode(), List.of(NTSymbol.COMPARISON, NTSymbol.EQUALITY1));
        TABLE.put(new TableEntry(NTSymbol.EQUALITY, TSymbol.NUMBER).hashCode(), List.of(NTSymbol.COMPARISON, NTSymbol.EQUALITY1));
        TABLE.put(new TableEntry(NTSymbol.EQUALITY, TSymbol.STRING).hashCode(), List.of(NTSymbol.COMPARISON, NTSymbol.EQUALITY1));
        TABLE.put(new TableEntry(NTSymbol.EQUALITY, TSymbol.ID).hashCode(), List.of(NTSymbol.COMPARISON, NTSymbol.EQUALITY1));
        TABLE.put(new TableEntry(NTSymbol.EQUALITY, TSymbol.LEFT_PAREN).hashCode(), List.of(NTSymbol.COMPARISON, NTSymbol.EQUALITY1));
        TABLE.put(new TableEntry(NTSymbol.EQUALITY, TSymbol.OR).hashCode(), List.of(NTSymbol.COMPARISON, NTSymbol.EQUALITY1));

        TABLE.put(new TableEntry(NTSymbol.EQUALITY1, TSymbol.NE).hashCode(), List.of(TSymbol.NE, NTSymbol.EQUALITY));
        TABLE.put(new TableEntry(NTSymbol.EQUALITY1, TSymbol.EQ).hashCode(), List.of(TSymbol.EQ, NTSymbol.EQUALITY));
        TABLE.put(new TableEntry(NTSymbol.EQUALITY1, TSymbol.EOF).hashCode(), List.of(TSymbol.EPSILON));
        TABLE.put(new TableEntry(NTSymbol.EQUALITY1, TSymbol.RIGHT_PAREN).hashCode(), List.of(TSymbol.EPSILON));
        TABLE.put(new TableEntry(NTSymbol.EQUALITY1, TSymbol.COMA).hashCode(), List.of(TSymbol.EPSILON));
        TABLE.put(new TableEntry(NTSymbol.EQUALITY1, TSymbol.FROM).hashCode(), List.of(TSymbol.EPSILON));
        TABLE.put(new TableEntry(NTSymbol.EQUALITY1, TSymbol.OR).hashCode(), List.of(TSymbol.EPSILON));
        TABLE.put(new TableEntry(NTSymbol.EQUALITY1, TSymbol.AND).hashCode(), List.of(TSymbol.EPSILON));

        TABLE.put(new TableEntry(NTSymbol.COMPARISON, TSymbol.NOT).hashCode(), List.of(NTSymbol.TERM, NTSymbol.COMPARISON1));
        TABLE.put(new TableEntry(NTSymbol.COMPARISON, TSymbol.MINUS).hashCode(), List.of(NTSymbol.TERM, NTSymbol.COMPARISON1));
        TABLE.put(new TableEntry(NTSymbol.COMPARISON, TSymbol.TRUE).hashCode(), List.of(NTSymbol.TERM, NTSymbol.COMPARISON1));
        TABLE.put(new TableEntry(NTSymbol.COMPARISON, TSymbol.FALSE).hashCode(), List.of(NTSymbol.TERM, NTSymbol.COMPARISON1));
        TABLE.put(new TableEntry(NTSymbol.COMPARISON, TSymbol.NULL).hashCode(), List.of(NTSymbol.TERM, NTSymbol.COMPARISON1));
        TABLE.put(new TableEntry(NTSymbol.COMPARISON, TSymbol.NUMBER).hashCode(), List.of(NTSymbol.TERM, NTSymbol.COMPARISON1));
        TABLE.put(new TableEntry(NTSymbol.COMPARISON, TSymbol.STRING).hashCode(), List.of(NTSymbol.TERM, NTSymbol.COMPARISON1));
        TABLE.put(new TableEntry(NTSymbol.COMPARISON, TSymbol.ID).hashCode(), List.of(NTSymbol.TERM, NTSymbol.COMPARISON1));
        TABLE.put(new TableEntry(NTSymbol.COMPARISON, TSymbol.LEFT_PAREN).hashCode(), List.of(NTSymbol.TERM, NTSymbol.COMPARISON1));

        TABLE.put(new TableEntry(NTSymbol.COMPARISON1, TSymbol.GT).hashCode(), List.of(TSymbol.GT, NTSymbol.COMPARISON));
        TABLE.put(new TableEntry(NTSymbol.COMPARISON1, TSymbol.GE).hashCode(), List.of(TSymbol.GE, NTSymbol.COMPARISON));
        TABLE.put(new TableEntry(NTSymbol.COMPARISON1, TSymbol.LT).hashCode(), List.of(TSymbol.LT, NTSymbol.COMPARISON));
        TABLE.put(new TableEntry(NTSymbol.COMPARISON1, TSymbol.LE).hashCode(), List.of(TSymbol.LE, NTSymbol.COMPARISON));
        TABLE.put(new TableEntry(NTSymbol.COMPARISON1, TSymbol.RIGHT_PAREN).hashCode(), List.of(TSymbol.EPSILON));
        TABLE.put(new TableEntry(NTSymbol.COMPARISON1, TSymbol.EOF).hashCode(), List.of(TSymbol.EPSILON));
        TABLE.put(new TableEntry(NTSymbol.COMPARISON1, TSymbol.OR).hashCode(), List.of(TSymbol.EPSILON));
        TABLE.put(new TableEntry(NTSymbol.COMPARISON1, TSymbol.AND).hashCode(), List.of(TSymbol.EPSILON));
        TABLE.put(new TableEntry(NTSymbol.COMPARISON1, TSymbol.EQ).hashCode(), List.of(TSymbol.EPSILON));
        TABLE.put(new TableEntry(NTSymbol.COMPARISON1, TSymbol.NE).hashCode(), List.of(TSymbol.EPSILON));
        TABLE.put(new TableEntry(NTSymbol.COMPARISON1, TSymbol.FROM).hashCode(), List.of(TSymbol.EPSILON));
        TABLE.put(new TableEntry(NTSymbol.COMPARISON1, TSymbol.COMA).hashCode(), List.of(TSymbol.EPSILON));

        TABLE.put(new TableEntry(NTSymbol.TERM, TSymbol.NOT).hashCode(), List.of(NTSymbol.FACTOR, NTSymbol.TERM1));
        TABLE.put(new TableEntry(NTSymbol.TERM, TSymbol.MINUS).hashCode(), List.of(NTSymbol.FACTOR, NTSymbol.TERM1));
        TABLE.put(new TableEntry(NTSymbol.TERM, TSymbol.TRUE).hashCode(), List.of(NTSymbol.FACTOR, NTSymbol.TERM1));
        TABLE.put(new TableEntry(NTSymbol.TERM, TSymbol.FALSE).hashCode(), List.of(NTSymbol.FACTOR, NTSymbol.TERM1));
        TABLE.put(new TableEntry(NTSymbol.TERM, TSymbol.NULL).hashCode(), List.of(NTSymbol.FACTOR, NTSymbol.TERM1));
        TABLE.put(new TableEntry(NTSymbol.TERM, TSymbol.NUMBER).hashCode(), List.of(NTSymbol.FACTOR, NTSymbol.TERM1));
        TABLE.put(new TableEntry(NTSymbol.TERM, TSymbol.STRING).hashCode(), List.of(NTSymbol.FACTOR, NTSymbol.TERM1));
        TABLE.put(new TableEntry(NTSymbol.TERM, TSymbol.ID).hashCode(), List.of(NTSymbol.FACTOR, NTSymbol.TERM1));
        TABLE.put(new TableEntry(NTSymbol.TERM, TSymbol.LEFT_PAREN).hashCode(), List.of(NTSymbol.FACTOR, NTSymbol.TERM1));

        TABLE.put(new TableEntry(NTSymbol.TERM1, TSymbol.FROM).hashCode(), List.of(TSymbol.EPSILON));
        TABLE.put(new TableEntry(NTSymbol.TERM1, TSymbol.COMA).hashCode(), List.of(TSymbol.EPSILON));
        TABLE.put(new TableEntry(NTSymbol.TERM1, TSymbol.OR).hashCode(), List.of(TSymbol.EPSILON));
        TABLE.put(new TableEntry(NTSymbol.TERM1, TSymbol.AND).hashCode(), List.of(TSymbol.EPSILON));
        TABLE.put(new TableEntry(NTSymbol.TERM1, TSymbol.LE).hashCode(), List.of(TSymbol.EPSILON));
        TABLE.put(new TableEntry(NTSymbol.TERM1, TSymbol.GT).hashCode(), List.of(TSymbol.EPSILON));
        TABLE.put(new TableEntry(NTSymbol.TERM1, TSymbol.GE).hashCode(), List.of(TSymbol.EPSILON));
        TABLE.put(new TableEntry(NTSymbol.TERM1, TSymbol.LT).hashCode(), List.of(TSymbol.EPSILON));
        TABLE.put(new TableEntry(NTSymbol.TERM1, TSymbol.EQ).hashCode(), List.of(TSymbol.EPSILON));
        TABLE.put(new TableEntry(NTSymbol.TERM1, TSymbol.NE).hashCode(), List.of(TSymbol.EPSILON));
        TABLE.put(new TableEntry(NTSymbol.TERM1, TSymbol.EOF).hashCode(), List.of(TSymbol.EPSILON));
        TABLE.put(new TableEntry(NTSymbol.TERM1, TSymbol.RIGHT_PAREN).hashCode(), List.of(TSymbol.EPSILON));
        TABLE.put(new TableEntry(NTSymbol.TERM1, TSymbol.MINUS).hashCode(), List.of(TSymbol.MINUS, NTSymbol.TERM));
        TABLE.put(new TableEntry(NTSymbol.TERM1, TSymbol.PLUS).hashCode(), List.of(TSymbol.PLUS, NTSymbol.TERM));

        TABLE.put(new TableEntry(NTSymbol.FACTOR, TSymbol.MINUS).hashCode(), List.of(NTSymbol.UNARY, NTSymbol.FACTOR1));
        TABLE.put(new TableEntry(NTSymbol.FACTOR, TSymbol.NOT).hashCode(), List.of(NTSymbol.UNARY, NTSymbol.FACTOR1));
        TABLE.put(new TableEntry(NTSymbol.FACTOR, TSymbol.ID).hashCode(), List.of(NTSymbol.UNARY, NTSymbol.FACTOR1));
        TABLE.put(new TableEntry(NTSymbol.FACTOR, TSymbol.TRUE).hashCode(), List.of(NTSymbol.UNARY, NTSymbol.FACTOR1));
        TABLE.put(new TableEntry(NTSymbol.FACTOR, TSymbol.FALSE).hashCode(), List.of(NTSymbol.UNARY, NTSymbol.FACTOR1));
        TABLE.put(new TableEntry(NTSymbol.FACTOR, TSymbol.NULL).hashCode(), List.of(NTSymbol.UNARY, NTSymbol.FACTOR1));
        TABLE.put(new TableEntry(NTSymbol.FACTOR, TSymbol.NUMBER).hashCode(), List.of(NTSymbol.UNARY, NTSymbol.FACTOR1));
        TABLE.put(new TableEntry(NTSymbol.FACTOR, TSymbol.STRING).hashCode(), List.of(NTSymbol.UNARY, NTSymbol.FACTOR1));
        TABLE.put(new TableEntry(NTSymbol.FACTOR, TSymbol.LEFT_PAREN).hashCode(), List.of(NTSymbol.UNARY, NTSymbol.FACTOR1));

        TABLE.put(new TableEntry(NTSymbol.FACTOR1, TSymbol.SLASH).hashCode(), List.of(TSymbol.SLASH, NTSymbol.FACTOR));
        TABLE.put(new TableEntry(NTSymbol.FACTOR1, TSymbol.STAR).hashCode(), List.of(TSymbol.STAR, NTSymbol.FACTOR));
        TABLE.put(new TableEntry(NTSymbol.FACTOR1, TSymbol.MINUS).hashCode(), List.of(TSymbol.EPSILON));
        TABLE.put(new TableEntry(NTSymbol.FACTOR1, TSymbol.PLUS).hashCode(), List.of(TSymbol.EPSILON));
        TABLE.put(new TableEntry(NTSymbol.FACTOR1, TSymbol.GT).hashCode(), List.of(TSymbol.EPSILON));
        TABLE.put(new TableEntry(NTSymbol.FACTOR1, TSymbol.GE).hashCode(), List.of(TSymbol.EPSILON));
        TABLE.put(new TableEntry(NTSymbol.FACTOR1, TSymbol.LT).hashCode(), List.of(TSymbol.EPSILON));
        TABLE.put(new TableEntry(NTSymbol.FACTOR1, TSymbol.LE).hashCode(), List.of(TSymbol.EPSILON));
        TABLE.put(new TableEntry(NTSymbol.FACTOR1, TSymbol.EQ).hashCode(), List.of(TSymbol.EPSILON));
        TABLE.put(new TableEntry(NTSymbol.FACTOR1, TSymbol.NE).hashCode(), List.of(TSymbol.EPSILON));
        TABLE.put(new TableEntry(NTSymbol.FACTOR1, TSymbol.AND).hashCode(), List.of(TSymbol.EPSILON));
        TABLE.put(new TableEntry(NTSymbol.FACTOR1, TSymbol.OR).hashCode(), List.of(TSymbol.EPSILON));
        TABLE.put(new TableEntry(NTSymbol.FACTOR1, TSymbol.EOF).hashCode(), List.of(TSymbol.EPSILON));
        TABLE.put(new TableEntry(NTSymbol.FACTOR1, TSymbol.COMA).hashCode(), List.of(TSymbol.EPSILON));
        TABLE.put(new TableEntry(NTSymbol.FACTOR1, TSymbol.FROM).hashCode(), List.of(TSymbol.EPSILON));
        TABLE.put(new TableEntry(NTSymbol.FACTOR1, TSymbol.RIGHT_PAREN).hashCode(), List.of(TSymbol.EPSILON));

        TABLE.put(new TableEntry(NTSymbol.UNARY, TSymbol.NOT).hashCode(), List.of(TSymbol.NOT, NTSymbol.UNARY));
        TABLE.put(new TableEntry(NTSymbol.UNARY, TSymbol.MINUS).hashCode(), List.of(TSymbol.MINUS, NTSymbol.UNARY));
        TABLE.put(new TableEntry(NTSymbol.UNARY, TSymbol.TRUE).hashCode(), List.of(NTSymbol.CALL));
        TABLE.put(new TableEntry(NTSymbol.UNARY, TSymbol.FALSE).hashCode(), List.of(NTSymbol.CALL));
        TABLE.put(new TableEntry(NTSymbol.UNARY, TSymbol.NULL).hashCode(), List.of(NTSymbol.CALL));
        TABLE.put(new TableEntry(NTSymbol.UNARY, TSymbol.NUMBER).hashCode(), List.of(NTSymbol.CALL));
        TABLE.put(new TableEntry(NTSymbol.UNARY, TSymbol.STRING).hashCode(), List.of(NTSymbol.CALL));
        TABLE.put(new TableEntry(NTSymbol.UNARY, TSymbol.ID).hashCode(), List.of(NTSymbol.CALL));
        TABLE.put(new TableEntry(NTSymbol.UNARY, TSymbol.LEFT_PAREN).hashCode(), List.of(NTSymbol.CALL));

        TABLE.put(new TableEntry(NTSymbol.CALL, TSymbol.TRUE).hashCode(), List.of(NTSymbol.PRIMARY, NTSymbol.CALL1));
        TABLE.put(new TableEntry(NTSymbol.CALL, TSymbol.FALSE).hashCode(), List.of(NTSymbol.PRIMARY, NTSymbol.CALL1));
        TABLE.put(new TableEntry(NTSymbol.CALL, TSymbol.NULL).hashCode(), List.of(NTSymbol.PRIMARY, NTSymbol.CALL1));
        TABLE.put(new TableEntry(NTSymbol.CALL, TSymbol.NUMBER).hashCode(), List.of(NTSymbol.PRIMARY, NTSymbol.CALL1));
        TABLE.put(new TableEntry(NTSymbol.CALL, TSymbol.STRING).hashCode(), List.of(NTSymbol.PRIMARY, NTSymbol.CALL1));
        TABLE.put(new TableEntry(NTSymbol.CALL, TSymbol.ID).hashCode(), List.of(NTSymbol.PRIMARY, NTSymbol.CALL1));
        TABLE.put(new TableEntry(NTSymbol.CALL, TSymbol.LEFT_PAREN).hashCode(), List.of(NTSymbol.PRIMARY, NTSymbol.CALL1));

        TABLE.put(new TableEntry(NTSymbol.CALL1, TSymbol.LEFT_PAREN).hashCode(), List.of(TSymbol.LEFT_PAREN, NTSymbol.ARGUMENTS, TSymbol.RIGHT_PAREN));
        TABLE.put(new TableEntry(NTSymbol.CALL1, TSymbol.SLASH).hashCode(), List.of(TSymbol.EPSILON));
        TABLE.put(new TableEntry(NTSymbol.CALL1, TSymbol.STAR).hashCode(), List.of(TSymbol.EPSILON));
        TABLE.put(new TableEntry(NTSymbol.CALL1, TSymbol.MINUS).hashCode(), List.of(TSymbol.EPSILON));
        TABLE.put(new TableEntry(NTSymbol.CALL1, TSymbol.PLUS).hashCode(), List.of(TSymbol.EPSILON));
        TABLE.put(new TableEntry(NTSymbol.CALL1, TSymbol.GT).hashCode(), List.of(TSymbol.EPSILON));
        TABLE.put(new TableEntry(NTSymbol.CALL1, TSymbol.GE).hashCode(), List.of(TSymbol.EPSILON));
        TABLE.put(new TableEntry(NTSymbol.CALL1, TSymbol.LT).hashCode(), List.of(TSymbol.EPSILON));
        TABLE.put(new TableEntry(NTSymbol.CALL1, TSymbol.LE).hashCode(), List.of(TSymbol.EPSILON));
        TABLE.put(new TableEntry(NTSymbol.CALL1, TSymbol.EQ).hashCode(), List.of(TSymbol.EPSILON));
        TABLE.put(new TableEntry(NTSymbol.CALL1, TSymbol.NE).hashCode(), List.of(TSymbol.EPSILON));
        TABLE.put(new TableEntry(NTSymbol.CALL1, TSymbol.AND).hashCode(), List.of(TSymbol.EPSILON));
        TABLE.put(new TableEntry(NTSymbol.CALL1, TSymbol.OR).hashCode(), List.of(TSymbol.EPSILON));
        TABLE.put(new TableEntry(NTSymbol.CALL1, TSymbol.COMA).hashCode(), List.of(TSymbol.EPSILON));
        TABLE.put(new TableEntry(NTSymbol.CALL1, TSymbol.FROM).hashCode(), List.of(TSymbol.EPSILON));
        TABLE.put(new TableEntry(NTSymbol.CALL1, TSymbol.EOF).hashCode(), List.of(TSymbol.EPSILON));
        TABLE.put(new TableEntry(NTSymbol.CALL1, TSymbol.RIGHT_PAREN).hashCode(), List.of(TSymbol.EPSILON));

        TABLE.put(new TableEntry(NTSymbol.PRIMARY, TSymbol.TRUE).hashCode(), List.of(TSymbol.TRUE));
        TABLE.put(new TableEntry(NTSymbol.PRIMARY, TSymbol.FALSE).hashCode(), List.of(TSymbol.FALSE));
        TABLE.put(new TableEntry(NTSymbol.PRIMARY, TSymbol.NULL).hashCode(), List.of(TSymbol.NULL));
        TABLE.put(new TableEntry(NTSymbol.PRIMARY, TSymbol.NUMBER).hashCode(), List.of(TSymbol.NUMBER));
        TABLE.put(new TableEntry(NTSymbol.PRIMARY, TSymbol.STRING).hashCode(), List.of(TSymbol.STRING));
        TABLE.put(new TableEntry(NTSymbol.PRIMARY, TSymbol.ID).hashCode(), List.of(TSymbol.ID, NTSymbol.ALIAS_OPC));
        TABLE.put(new TableEntry(NTSymbol.PRIMARY, TSymbol.LEFT_PAREN).hashCode(), List.of(TSymbol.LEFT_PAREN, NTSymbol.EXPR, TSymbol.RIGHT_PAREN));

        TABLE.put(new TableEntry(NTSymbol.ALIAS_OPC, TSymbol.DOT).hashCode(), List.of(TSymbol.DOT, TSymbol.ID));
        TABLE.put(new TableEntry(NTSymbol.ALIAS_OPC, TSymbol.LEFT_PAREN).hashCode(), List.of(TSymbol.EPSILON));
        TABLE.put(new TableEntry(NTSymbol.ALIAS_OPC, TSymbol.SLASH).hashCode(), List.of(TSymbol.EPSILON));
        TABLE.put(new TableEntry(NTSymbol.ALIAS_OPC, TSymbol.STAR).hashCode(), List.of(TSymbol.EPSILON));
        TABLE.put(new TableEntry(NTSymbol.ALIAS_OPC, TSymbol.MINUS).hashCode(), List.of(TSymbol.EPSILON));
        TABLE.put(new TableEntry(NTSymbol.ALIAS_OPC, TSymbol.PLUS).hashCode(), List.of(TSymbol.EPSILON));
        TABLE.put(new TableEntry(NTSymbol.ALIAS_OPC, TSymbol.GT).hashCode(), List.of(TSymbol.EPSILON));
        TABLE.put(new TableEntry(NTSymbol.ALIAS_OPC, TSymbol.GE).hashCode(), List.of(TSymbol.EPSILON));
        TABLE.put(new TableEntry(NTSymbol.ALIAS_OPC, TSymbol.LT).hashCode(), List.of(TSymbol.EPSILON));
        TABLE.put(new TableEntry(NTSymbol.ALIAS_OPC, TSymbol.LE).hashCode(), List.of(TSymbol.EPSILON));
        TABLE.put(new TableEntry(NTSymbol.ALIAS_OPC, TSymbol.EQ).hashCode(), List.of(TSymbol.EPSILON));
        TABLE.put(new TableEntry(NTSymbol.ALIAS_OPC, TSymbol.NE).hashCode(), List.of(TSymbol.EPSILON));
        TABLE.put(new TableEntry(NTSymbol.ALIAS_OPC, TSymbol.AND).hashCode(), List.of(TSymbol.EPSILON));
        TABLE.put(new TableEntry(NTSymbol.ALIAS_OPC, TSymbol.OR).hashCode(), List.of(TSymbol.EPSILON));
        TABLE.put(new TableEntry(NTSymbol.ALIAS_OPC, TSymbol.COMA).hashCode(), List.of(TSymbol.EPSILON));
        TABLE.put(new TableEntry(NTSymbol.ALIAS_OPC, TSymbol.FROM).hashCode(), List.of(TSymbol.EPSILON));
        TABLE.put(new TableEntry(NTSymbol.ALIAS_OPC, TSymbol.EOF).hashCode(), List.of(TSymbol.EPSILON));
        TABLE.put(new TableEntry(NTSymbol.ALIAS_OPC, TSymbol.RIGHT_PAREN).hashCode(), List.of(TSymbol.EPSILON));

        TABLE.put(new TableEntry(NTSymbol.ARGUMENTS, TSymbol.NOT).hashCode(), List.of(NTSymbol.EXPR, NTSymbol.ARGUMENTS1));
        TABLE.put(new TableEntry(NTSymbol.ARGUMENTS, TSymbol.MINUS).hashCode(), List.of(NTSymbol.EXPR, NTSymbol.ARGUMENTS1));
        TABLE.put(new TableEntry(NTSymbol.ARGUMENTS, TSymbol.TRUE).hashCode(), List.of(NTSymbol.EXPR, NTSymbol.ARGUMENTS1));
        TABLE.put(new TableEntry(NTSymbol.ARGUMENTS, TSymbol.FALSE).hashCode(), List.of(NTSymbol.EXPR, NTSymbol.ARGUMENTS1));
        TABLE.put(new TableEntry(NTSymbol.ARGUMENTS, TSymbol.NULL).hashCode(), List.of(NTSymbol.EXPR, NTSymbol.ARGUMENTS1));
        TABLE.put(new TableEntry(NTSymbol.ARGUMENTS, TSymbol.NUMBER).hashCode(), List.of(NTSymbol.EXPR, NTSymbol.ARGUMENTS1));
        TABLE.put(new TableEntry(NTSymbol.ARGUMENTS, TSymbol.STRING).hashCode(), List.of(NTSymbol.EXPR, NTSymbol.ARGUMENTS1));
        TABLE.put(new TableEntry(NTSymbol.ARGUMENTS, TSymbol.ID).hashCode(), List.of(NTSymbol.EXPR, NTSymbol.ARGUMENTS1));
        TABLE.put(new TableEntry(NTSymbol.ARGUMENTS, TSymbol.LEFT_PAREN).hashCode(), List.of(NTSymbol.EXPR, NTSymbol.ARGUMENTS1));
        TABLE.put(new TableEntry(NTSymbol.ARGUMENTS, TSymbol.RIGHT_PAREN).hashCode(), List.of(TSymbol.EPSILON));

        TABLE.put(new TableEntry(NTSymbol.ARGUMENTS1, TSymbol.COMA).hashCode(), List.of(TSymbol.COMA, NTSymbol.EXPR, NTSymbol.ARGUMENTS1));
        TABLE.put(new TableEntry(NTSymbol.ARGUMENTS1, TSymbol.RIGHT_PAREN).hashCode(), List.of(TSymbol.EPSILON));
    }
}
