package scanner.token;

import grammar.TSymbol;

public class Operand extends Token {

    public Operand(TSymbol type, String lexeme, int line) {
        super(type, lexeme, line);
    }

    public Operand(TSymbol type, String lexeme) {
        super(type, lexeme);
    }

    public Object getValue() {
        return null;
    }

}
