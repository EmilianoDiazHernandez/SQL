package grammar;

public enum TSymbol implements Symbol {
    ID,

    // Palabras reservadas
    AND, DISTINCT, FALSE, FROM, IS, NOT, NULL, OR, SELECT, TRUE, WHERE,

    COMA, SEMICOLON, DOT, LEFT_PAREN, RIGHT_PAREN,
    LT, LE, GT, GE, EQ, NE,
    PLUS, MINUS, STAR, SLASH,

    NUMBER, STRING,

    EPSILON,

    // Final de cadena
    EOF
}
