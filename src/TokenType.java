public enum TokenType {
    ID,

    // Palabras reservadas
    AND, DISTINCT, FALSE, FROM, IS, NOT, NULL, OR, SELECT, TRUE, WHERE,

    COMA, SEMICOLON, DOT, LEFT_PAREN, RIGHT_PAREN,
    LT, LE, GT, GE, EQUAL, NE,
    PLUS, MINUS, STAR, SLASH,

    NUMBER,

    // Final de cadena
    EOF
}
