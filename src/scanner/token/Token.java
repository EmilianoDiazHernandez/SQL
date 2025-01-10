package scanner.token;

import grammar.TSymbol;

public class Token {
    public TSymbol type;
    public String lexeme;
    public int line;

    public Token(TSymbol type, String lexeme, int line) {
        this.type = type;
        this.lexeme = lexeme;
        this.line = line;
    }

    public Token(TSymbol type, String lexeme) {
        this.type = type;
        this.lexeme = lexeme;
        this.line = 0;
    }

    public String toString() {
        //return "tipo: " + type + "\tlexema: \t linea: " + line;
        return lexeme;
    }
}
