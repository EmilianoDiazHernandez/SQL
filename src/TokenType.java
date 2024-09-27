public enum TokenType {
    ID,

    // Palabras reservadas
    AND, DISTINCT, FALSE, FROM, IS, NOT, NULL, OR, SELECT, TRUE, WHERE,

    COMA, SEMICOLON, DOT, LEFT_PAREN, RIGHT_PAREN,
    LT, LE, GT, GE, EQ, NE,
    PLUS, MINUS, STAR, SLASH, QUOTES,

    NUMBER,

    // Final de cadena
    EOF
}
