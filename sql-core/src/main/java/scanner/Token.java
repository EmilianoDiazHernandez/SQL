package scanner;

import grammar.TSymbol;

public class Token {
    final TSymbol type;
    public final String lexeme;
    public final int line;

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

    public TSymbol getType() {
        return type;
    }

    public String getLexeme() {
        return lexeme;
    }

    public int getLine() {
        return line;
    }

    @Override
    public String toString() {
        return "tipo: " + type + "\tlexema: " + lexeme + "\t linea: " + line;
    }
}
