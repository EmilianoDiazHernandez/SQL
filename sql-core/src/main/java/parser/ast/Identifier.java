package parser.ast;

import scanner.Token;

public final class Identifier extends Expression {
    public final Token token;

    public Identifier(Token token) {
        this.token = token;
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
        // We need to add visitIdentifier to Visitor interface
        return visitor.visitIdentifier(this);
    }
}
