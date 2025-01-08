package grammar;

public enum TSymbol implements Symbol {
    ID,

    AND(2), DISTINCT(8), FALSE, FROM, IS, NOT(3), NULL, OR(1), SELECT, TRUE, WHERE, QUERY,

    COMA, SEMICOLON, DOT, LEFT_PAREN, RIGHT_PAREN, BOOLEAN,
    LT(4), LE(4), GT(4), GE(4), EQ(4), NE(4),
    PLUS(5), MINUS(5), STAR(6), SLASH(6), STRING,

    NUMBER, EPSILON,

    EOF;

    public int precedence;

    TSymbol(int p) {
        this.precedence = p;
    }

    TSymbol() {
    }
}
