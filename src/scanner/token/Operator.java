package scanner.token;

import grammar.TSymbol;

public class Operator extends Token {

    public final int arity;

    public Operator(TSymbol type, String lexeme, int line, int arity) {
        super(type, lexeme, line);
        this.arity = arity;
    }

    public Operator(TSymbol type, String lexeme, int arity) {
        super(type, lexeme);
        this.arity = arity;
    }

    public Object operation() {
        return null;
    }

}
