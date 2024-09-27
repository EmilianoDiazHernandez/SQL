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
        StringBuilder lexeme = new StringBuilder();
        char character;
        int state = 0;
        //int beginLexeme = 0;

        for (int i = 0; i < source.length(); i++) {
            character = source.charAt(i);
            switch (state) {
                case 0:
                    switch (character) {
                        case ' ': case '\t': case '\n': break;
                        case '+': tokens.add(new Token(TokenType.PLUS, "+", i)); break;
                        case '*': tokens.add(new Token(TokenType.STAR, "*", i)); break;
                        case '/': tokens.add(new Token(TokenType.SLASH, "/", i)); break;
                        case ',': tokens.add(new Token(TokenType.COMA, ",", i)); break;
                        case ';': tokens.add(new Token(TokenType.SEMICOLON, ";", i)); break;
                        case '.': tokens.add(new Token(TokenType.DOT, ".", i)); break;
                        case '(': tokens.add(new Token(TokenType.LEFT_PAREN, "(", i)); break;
                        case ')': tokens.add(new Token(TokenType.RIGHT_PAREN, ")", i)); break;
                        case '"': tokens.add(new Token(TokenType.QUOTES, "\"", i)); break;
                        case '=': tokens.add(new Token(TokenType.EQ, "=", i)); break;
                        case '<': state = 1; break;
                        case '>': state = 2; break;
                        case '-': state = 8; break;
                        default:
                            if(Character.isLetter(character)){
                                lexeme.append(character);
                                state = 3;
                            }else if(Character.isDigit(character)){
                                lexeme.append(character);
                                state = 4;
                            }else{
                                state = -1;
                                i--;
                            }
                    }
                    break;
                case 1:
                    if (character == '=') tokens.add(new Token(TokenType.LE, "<=", i));
                    else if(character == '>') tokens.add(new Token(TokenType.DISTINCT, "<>", i));
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
                    if(Character.isLetter(character) || Character.isDigit(character) || character == '_') lexeme.append(character);
                    else{
                        tokens.add(new Token(reservedWords.getOrDefault(lexeme.toString().toUpperCase(), TokenType.ID), lexeme.toString(), i));
                        lexeme = new StringBuilder();
                        state = 0;
                        i--;
                    }
                    break;
                case 4:
                    if(Character.isDigit(character)) lexeme.append(character);
                    else if (character=='.' && Character.isDigit(source.charAt(i+1))) {
                        lexeme.append(character);
                        state = 5;
                    }else if (character=='E' && (Character.isDigit(source.charAt(i+1)) || source.charAt(i+1) == '-')) {
                        lexeme.append(character);
                        state = 6;
                    }else {
                        tokens.add(new Token(TokenType.NUMBER, lexeme.toString(), i));
                        lexeme = new StringBuilder();
                        state = 0;
                        i--;
                    }
                    break;
                case 5:
                    if(Character.isDigit(character)) lexeme.append(character);
                    else if (character=='E' && (Character.isDigit(source.charAt(i+1)) || source.charAt(i+1) == '-')) {
                        lexeme.append(character);
                        state = 6;
                    }else {
                        tokens.add(new Token(TokenType.NUMBER, lexeme.toString(), i));
                        lexeme = new StringBuilder();
                        state = 0;
                        i--;
                    }
                    break;
                case 6:
                    if(Character.isDigit(character) || (character == '-' && Character.isDigit(source.charAt(i+1)))){
                        lexeme.append(character);
                        state = 7;
                    }else {
                        tokens.add(new Token(TokenType.NUMBER, lexeme.toString(), i));
                        lexeme = new StringBuilder();
                        state = 0;
                        i--;
                    }
                    break;
                case 7:
                    if(Character.isDigit(character)) lexeme.append(character);
                    else {
                        tokens.add(new Token(TokenType.NUMBER, lexeme.toString(), i));
                        lexeme = new StringBuilder();
                        state = 0;
                        i--;
                    }
                    break;
                case 8:
                    if(character == '-') state = 9;
                    else {
                        tokens.add(new Token(TokenType.MINUS, "-", i));
                        state = 0;
                        i--;
                    }
                    break;
                case 9:
                    if(character == '\n') state = 0;
                    break;
                default:
                    Main.error(i, "Caracater '"+character+"' invalido");
                    state = 0;
                    break;
            }
        }
        tokens.add(new Token(TokenType.EOF, "", source.length()));

        return tokens;
    }
}
