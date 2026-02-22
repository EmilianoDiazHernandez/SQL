package grammar;

public enum TSymbol {
    ID,

    // Palabras reservadas
    AND, DISTINCT, FALSE, FROM, IS, NOT, NULL, OR, SELECT, TRUE, WHERE,

    COMA, SEMICOLON, DOT, LEFT_PAREN, RIGHT_PAREN,
    LT, LE, GT, GE, EQ, NE,
    PLUS, MINUS, STAR, SLASH, STRING,

    NUMBER,

    // Final de cadena
    EOF
}
