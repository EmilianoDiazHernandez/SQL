import java.util.*;

import static java.lang.Character.isDigit;

public class Scanner {

    private final String source;

    private final List<Token> tokens = new ArrayList<>();

    private static final Map<String, TokenType> reservedWords;

    static {
        reservedWords = new HashMap<>();

        reservedWords.put("AND", TokenType.AND);
        reservedWords.put("DISTINCT", TokenType.DISTINCT);
        reservedWords.put("FALSE", TokenType.FALSE);
        reservedWords.put("FROM", TokenType.FROM);
        reservedWords.put("IS", TokenType.IS);
        reservedWords.put("NOT", TokenType.NOT);
        reservedWords.put("NULL", TokenType.NULL);
        reservedWords.put("OR", TokenType.OR);
        reservedWords.put("SELECT", TokenType.SELECT);
        reservedWords.put("TRUE", TokenType.TRUE);
        reservedWords.put("WHERE", TokenType.WHERE);
    }

    Scanner(String source) {
        this.source = source + " ";
    }

    List<Token> scanTokens() {
        int state = 0;
        char character = 0;
        String lexeme = "";
        int beginLexeme = 0;

        for (int i = 0; i < source.length(); i++) {
            character = source.charAt(i);
            switch (state) {
                case 0:
                    switch (character) {
                        case ' ': case '\t': case '\n': break;
                        case '+': tokens.add(new Token(TokenType.PLUS, "+")); break;
                        case '-': tokens.add(new Token(TokenType.MINUS, "-")); break;
                        case '*': tokens.add(new Token(TokenType.STAR, "*")); break;
                        case '/': tokens.add(new Token(TokenType.SLASH, "/")); break;
                        case ',': tokens.add(new Token(TokenType.COMA, ",")); break;
                        case ';': tokens.add(new Token(TokenType.SEMICOLON, ";")); break;
                        case '.': tokens.add(new Token(TokenType.DOT, ".")); break;
                        case '(': tokens.add(new Token(TokenType.LEFT_PAREN, "(")); break;
                        case ')': tokens.add(new Token(TokenType.RIGHT_PAREN, ")")); break;
                        case '<': state = 1; break;
                        case '>': state = 2; break;
                        case '=': state = 3; break;
                        case '!': state = 4; break;
                    }
                    break;
                case 1:
                    if (character == '=') tokens.add(new Token(TokenType.LE, "<="));
                    else {
                        i--;
                        tokens.add(new Token(TokenType.LT, "<"));
                    }
                    state = 0;
                    break;
                case 2:
                    if (character == '=') tokens.add(new Token(TokenType.GE, ">="));
                    else {
                        i--;
                        tokens.add(new Token(TokenType.GT, ">"));
                    }
                    state = 0;
                    break;
                case 3:
                    if(character == '=') tokens.add(new Token(TokenType.EQ, "=="));
                    else i--;
                    state = 0;
                    break;
                case 4:
                    if(character == '=') tokens.add(new Token(TokenType.NE, "!="));
                    else i--;
                    state = 0;
                    break;
                default:

                    break;
            }
        }
        tokens.add(new Token(TokenType.EOF, "", source.length()));

        return tokens;
    }
}
