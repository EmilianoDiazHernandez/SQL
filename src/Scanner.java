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
        char current = 0;
        StringBuilder lexeme = new StringBuilder();
        int inicioLexema = 0;

        for (int i = 0; i < source.length(); i++) {
            current = source.charAt(i);
            switch (current) {
                case ' ': case '\r': case '\t': case '\n': break; // Ignore whitespace
                case '+': tokens.add(new Token(TokenType.PLUS, "+")); break;
                case '-': tokens.add(new Token(TokenType.MINUS, "-")); break;
                case '*': tokens.add(new Token(TokenType.STAR, "*")); break;
                case '/': tokens.add(new Token(TokenType.SLASH, "/")); break;
                case ',': tokens.add(new Token(TokenType.COMA, ",")); break;
                case ';': tokens.add(new Token(TokenType.SEMICOLON, ";")); break;
                case '.': tokens.add(new Token(TokenType.DOT, ".")); break;
                case '(': tokens.add(new Token(TokenType.LEFT_PAREN, "(")); break;
                case ')': tokens.add(new Token(TokenType.RIGHT_PAREN, ")")); break;
                case '<':
                    if (source.charAt(i + 1) == '=') {
                        tokens.add(new Token(TokenType.LE, "<="));
                        i++;
                    } else tokens.add(new Token(TokenType.LT, "<"));
                    break;
                case '>':
                    if (source.charAt(i + 1) == '=') {
                        tokens.add(new Token(TokenType.GE, ">="));
                        i++;
                    } else tokens.add(new Token(TokenType.GT, ">"));
                    break;
                case '=':
                    if (source.charAt(i+1) == '='){
                        tokens.add(new Token(TokenType.EQUAL, "=="));
                        i++;
                    }break;
                case '!':
                    if (source.charAt(i+1) == '='){
                        tokens.add(new Token(TokenType.NE, "!="));
                        i++;
                    }break;
                default:
                    if(Character.isLetter(current)){
                        while (Character.isLetter(source.charAt(i+1)) || isDigit(source.charAt(i+1))){
                            lexeme.append(current);
                            i++;
                            current = source.charAt(i);
                        }
                        lexeme.append(current);
                        tokens.add(new Token(reservedWords.getOrDefault(lexeme.toString(), TokenType.ID), lexeme.toString()));
                        lexeme = new StringBuilder();
                    } else if (isDigit(current)) {
                        lexeme.append(current);
                        while (isDigit(source.charAt(i+1))) {
                            i++;
                            current = source.charAt(i);
                            lexeme.append(current);
                        }
                        if(source.charAt(i+1) == '.' && isDigit(source.charAt(i+2))){
                            lexeme.append('.');
                            i++;
                        }
                        while (isDigit(source.charAt(i+1))) {
                            i++;
                            current = source.charAt(i);
                            lexeme.append(current);
                        }
                        if(source.charAt(i+1) == 'E' && isDigit(source.charAt(i+2))){
                            lexeme.append('E');
                            i++;
                        }
                        while (isDigit(source.charAt(i+1))) {
                            i++;
                            current = source.charAt(i);
                            lexeme.append(current);
                        }
                        tokens.add(new Token(TokenType.NUMBER, lexeme.toString()));
                        lexeme = new StringBuilder();
                    }
                    break;
            }
        }
        tokens.add(new Token(TokenType.EOF, "", source.length()));

        return tokens;
    }
}
