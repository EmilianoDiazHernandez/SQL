package scanner;

import grammar.TSymbol;
import main.Main;

import java.util.*;

public class Scanner {

    private final String source;

    private final List<Token> tokens = new ArrayList<>();

    private static final Map<String, TSymbol> reservedWords;

    static {
        reservedWords = new HashMap<>();

        reservedWords.put("AND", TSymbol.AND);
        reservedWords.put("DISTINCT", TSymbol.DISTINCT);
        reservedWords.put("FALSE", TSymbol.FALSE);
        reservedWords.put("FROM", TSymbol.FROM);
        reservedWords.put("IS", TSymbol.IS);
        reservedWords.put("NOT", TSymbol.NOT);
        reservedWords.put("NULL", TSymbol.NULL);
        reservedWords.put("OR", TSymbol.OR);
        reservedWords.put("SELECT", TSymbol.SELECT);
        reservedWords.put("TRUE", TSymbol.TRUE);
        reservedWords.put("WHERE", TSymbol.WHERE);
    }

    public Scanner(String source) {
        this.source = source + " ";
    }

    public List<Token> scanTokens() {
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
                        case '+': tokens.add(new Token(TSymbol.PLUS, "+", i)); break;
                        case '*': tokens.add(new Token(TSymbol.STAR, "*", i)); break;
                        case ',': tokens.add(new Token(TSymbol.COMA, ",", i)); break;
                        case ';': tokens.add(new Token(TSymbol.SEMICOLON, ";", i)); break;
                        case '.': tokens.add(new Token(TSymbol.DOT, ".", i)); break;
                        case '(': tokens.add(new Token(TSymbol.LEFT_PAREN, "(", i)); break;
                        case ')': tokens.add(new Token(TSymbol.RIGHT_PAREN, ")", i)); break;
                        case '=': tokens.add(new Token(TSymbol.EQ, "=", i)); break;
                        case '<': state = 1; break;
                        case '>': state = 2; break;
                        case '-': state = 8; break;
                        case '/': state = 10; break;
                        case '"': case '\'': state = 12; break;
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
                    if (character == '=') tokens.add(new Token(TSymbol.LE, "<=", i));
                    else if(character == '>') tokens.add(new Token(TSymbol.DISTINCT, "<>", i));
                    else {
                        i--;
                        tokens.add(new Token(TSymbol.LT, "<", i));
                    }
                    state = 0;
                    break;
                case 2:
                    if (character == '=') tokens.add(new Token(TSymbol.GE, ">=", i));
                    else {
                        i--;
                        tokens.add(new Token(TSymbol.GT, ">", i));
                    }
                    state = 0;
                    break;
                case 3:
                    if(Character.isLetter(character) || Character.isDigit(character) || character == '_') lexeme.append(character);
                    else{
                        tokens.add(new Token(reservedWords.getOrDefault(lexeme.toString().toUpperCase(), TSymbol.ID), lexeme.toString(), i));
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
                        tokens.add(new Token(TSymbol.NUMBER, lexeme.toString(), i));
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
                        tokens.add(new Token(TSymbol.NUMBER, lexeme.toString(), i));
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
                        tokens.add(new Token(TSymbol.NUMBER, lexeme.toString(), i));
                        lexeme = new StringBuilder();
                        state = 0;
                        i--;
                    }
                    break;
                case 7:
                    if(Character.isDigit(character)) lexeme.append(character);
                    else {
                        tokens.add(new Token(TSymbol.NUMBER, lexeme.toString(), i));
                        lexeme = new StringBuilder();
                        state = 0;
                        i--;
                    }
                    break;
                case 8:
                    if(character == '-') state = 9;
                    else {
                        tokens.add(new Token(TSymbol.MINUS, "-", i));
                        state = 0;
                        i--;
                    }
                    break;
                case 9:
                    if(character == '\n') state = 0;
                    break;
                case 10:
                    if(character == '*') state = 11;
                    else {
                        tokens.add(new Token(TSymbol.SLASH, "/", i));
                        state = 0;
                        i--;
                    }
                    break;
                case 11:
                    if (character == '*' && source.charAt(i+1) == '/') {
                        state = 0; i++;
                    }
                    break;
                case 12:
                    if(character == '"' || character == '\''){
                        tokens.add(new Token(TSymbol.STRING, lexeme.toString(), i));
                        lexeme = new StringBuilder();
                        state = 0;
                    }else lexeme.append(character);
                    break;
                default:
                    Main.error(i, "Caracater '"+character+"' invalido");
                    state = 0;
                    break;
            }
        }
        tokens.add(new Token(TSymbol.EOF, "", source.length()));

        return tokens;
    }
}
