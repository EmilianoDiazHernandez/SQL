import java.util.*;

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
                        case '+': tokens.add(new Token(TokenType.PLUS, "+", i)); break;
                        case '-': tokens.add(new Token(TokenType.MINUS, "-", i)); break;
                        case '*': tokens.add(new Token(TokenType.STAR, "*", i)); break;
                        case '/': tokens.add(new Token(TokenType.SLASH, "/", i)); break;
                        case ',': tokens.add(new Token(TokenType.COMA, ",", i)); break;
                        case ';': tokens.add(new Token(TokenType.SEMICOLON, ";", i)); break;
                        case '.': tokens.add(new Token(TokenType.DOT, ".", i)); break;
                        case '(': tokens.add(new Token(TokenType.LEFT_PAREN, "(", i)); break;
                        case ')': tokens.add(new Token(TokenType.RIGHT_PAREN, ")", i)); break;
                        case '<': state = 1; break;
                        case '>': state = 2; break;
                        case '=': state = 3; break;
                        case '!': state = 4; break;
                        default:
                            if(Character.isLetter(character)){
                                lexeme += character;
                                state = 5;
                            }else if(Character.isDigit(character)){
                                lexeme += character;
                                state = 6;
                            }
                    }
                    break;
                case 1:
                    if (character == '=') tokens.add(new Token(TokenType.LE, "<=", i));
                    else {
                        i--;
                        tokens.add(new Token(TokenType.LT, "<", i));
                    }
                    state = 0;
                    break;
                case 2:
                    if (character == '=') tokens.add(new Token(TokenType.GE, ">=", i));
                    else {
                        i--;
                        tokens.add(new Token(TokenType.GT, ">", i));
                    }
                    state = 0;
                    break;
                case 3:
                    if(character == '=') tokens.add(new Token(TokenType.EQ, "==", i));
                    else i--;
                    state = 0;
                    break;
                case 4:
                    if(character == '=') tokens.add(new Token(TokenType.NE, "!=", i));
                    else i--;
                    state = 0;
                    break;
                case 5:
                    if(Character.isLetter(character) || Character.isDigit(character) || character == '_') lexeme += character;
                    else{
                        tokens.add(new Token(reservedWords.getOrDefault(lexeme, TokenType.ID), lexeme, i));
                        lexeme = "";
                        state = 0;
                        i--;
                    }
                    break;
                case 6:
                    if(Character.isDigit(character)) lexeme += character;
                    else if (character=='.' && Character.isDigit(source.charAt(i+1))) {
                        lexeme += character;
                        state = 7;
                    }else if (character=='E' && (Character.isDigit(source.charAt(i+1)) || source.charAt(i+1) == '-')) {
                        lexeme += character;
                        state = 8;
                    }else {
                        tokens.add(new Token(TokenType.NUMBER, lexeme, i));
                        lexeme = "";
                        state = 0;
                        i--;
                    }
                    break;
                case 7:
                    if(Character.isDigit(character)) lexeme += character;
                    else if (character=='E' && (Character.isDigit(source.charAt(i+1)) || source.charAt(i+1) == '-')) {
                        lexeme += character;
                        state = 8;
                    }else {
                        tokens.add(new Token(TokenType.NUMBER, lexeme, i));
                        lexeme = "";
                        state = 0;
                        i--;
                    }
                    break;
                case 8:
                    if(Character.isDigit(character) || (character == '-' && Character.isDigit(source.charAt(i+1)))){
                        lexeme += character;
                        state = 9;
                    }else {
                        tokens.add(new Token(TokenType.NUMBER, lexeme, i));
                        lexeme = "";
                        state = 0;
                        i--;
                    }
                    break;
                case 9:
                    if(Character.isDigit(character)) lexeme += character;
                    else {
                        tokens.add(new Token(TokenType.NUMBER, lexeme, i));
                        lexeme = "";
                        state = 0;
                        i--;
                    }
                    break;
                default:

                    break;
            }
        }
        tokens.add(new Token(TokenType.EOF, "", source.length()));

        return tokens;
    }
}
