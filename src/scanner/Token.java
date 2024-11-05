package scanner;

import grammar.TerminalSymbol;

public class Token {
    final TerminalSymbol type;
    public final String lexeme;
    public final int line;

    public Token(TerminalSymbol type, String lexeme, int line) {
        this.type = type;
        this.lexeme = lexeme;
        this.line = line;
    }

    public Token(TerminalSymbol type, String lexeme) {
        this.type = type;
        this.lexeme = lexeme;
        this.line = 0;
    }

    public String toString() {
        return "tipo: " + type + "\tlexema: " + lexeme + "\t linea: " + line;
    }
}
