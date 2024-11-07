package grammar;

public enum NTSymbol implements Symbol {
    Q, D, P, F, F1, T, T1, T2, T3, W,
    EXPR,
    LOGIC_OR, LOGIC_OR1,
    LOGIC_AND, LOGIC_AND1,
    EQUALITY, EQUALITY1,
    COMPARISON, COMPARISON1,
    TERM, TERM1,
    FACTOR, FACTOR1,
    UNARY,
    CALL, CALL1,
    PRIMARY,
    ALIAS_OPC,
    ARGUMENTS, ARGUMENTS1
}
