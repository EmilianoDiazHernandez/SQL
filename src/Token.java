public class Token {
    final TokenType type;
    final String lexeme;
    final int line;

    public Token(TokenType type, String lexeme, int line){
        this.type = type;
        this.lexeme = lexeme;
        this.line = line;
    }

    public Token(TokenType type, String lexeme){
        this.type = type;
        this.lexeme = lexeme;
        this.line = 0;
    }

    public String toString(){
        return "tipo: "+ type + "\tlexema: " + lexeme + "\t line: " + line;
    }
}
