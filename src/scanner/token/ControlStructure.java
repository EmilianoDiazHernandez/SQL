package scanner.token;

import grammar.TSymbol;

public class ControlStructure extends Token {

    public ControlStructure(TSymbol type, String lexeme, int line) {
        super(type, lexeme, line);
    }

    public ControlStructure(TSymbol type, String lexeme) {
        super(type, lexeme);
    }

}
