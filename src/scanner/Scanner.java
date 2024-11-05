package scanner;

import grammar.TerminalSymbol;
import main.Main;

import java.util.*;

public class Scanner {

    private final String source;

    private final List<Token> tokens = new ArrayList<>();

    private static final Map<String, TerminalSymbol> reservedWords;

    static {
        reservedWords = new HashMap<>();

        reservedWords.put("AND", TerminalSymbol.AND);
        reservedWords.put("DISTINCT", TerminalSymbol.DISTINCT);
        reservedWords.put("FALSE", TerminalSymbol.FALSE);
        reservedWords.put("FROM", TerminalSymbol.FROM);
        reservedWords.put("IS", TerminalSymbol.IS);
        reservedWords.put("NOT", TerminalSymbol.NOT);
        reservedWords.put("NULL", TerminalSymbol.NULL);
        reservedWords.put("OR", TerminalSymbol.OR);
        reservedWords.put("SELECT", TerminalSymbol.SELECT);
        reservedWords.put("TRUE", TerminalSymbol.TRUE);
        reservedWords.put("WHERE", TerminalSymbol.WHERE);
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
                        case '+': tokens.add(new Token(TerminalSymbol.PLUS, "+", i)); break;
                        case '*': tokens.add(new Token(TerminalSymbol.STAR, "*", i)); break;
                        case ',': tokens.add(new Token(TerminalSymbol.COMA, ",", i)); break;
                        case ';': tokens.add(new Token(TerminalSymbol.SEMICOLON, ";", i)); break;
                        case '.': tokens.add(new Token(TerminalSymbol.DOT, ".", i)); break;
                        case '(': tokens.add(new Token(TerminalSymbol.LEFT_PAREN, "(", i)); break;
                        case ')': tokens.add(new Token(TerminalSymbol.RIGHT_PAREN, ")", i)); break;
                        case '"': tokens.add(new Token(TerminalSymbol.QUOTES, "\"", i)); break;
                        case '=': tokens.add(new Token(TerminalSymbol.EQ, "=", i)); break;
                        case '<': state = 1; break;
                        case '>': state = 2; break;
                        case '-': state = 8; break;
                        case '/': state = 10; break;
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
                    if (character == '=') tokens.add(new Token(TerminalSymbol.LE, "<=", i));
                    else if(character == '>') tokens.add(new Token(TerminalSymbol.DISTINCT, "<>", i));
                    else {
                        i--;
                        tokens.add(new Token(TerminalSymbol.LT, "<", i));
                    }
                    state = 0;
                    break;
                case 2:
                    if (character == '=') tokens.add(new Token(TerminalSymbol.GE, ">=", i));
                    else {
                        i--;
                        tokens.add(new Token(TerminalSymbol.GT, ">", i));
                    }
                    state = 0;
                    break;
                case 3:
                    if(Character.isLetter(character) || Character.isDigit(character) || character == '_') lexeme.append(character);
                    else{
                        tokens.add(new Token(reservedWords.getOrDefault(lexeme.toString().toUpperCase(), TerminalSymbol.ID), lexeme.toString(), i));
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
                        tokens.add(new Token(TerminalSymbol.NUMBER, lexeme.toString(), i));
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
                        tokens.add(new Token(TerminalSymbol.NUMBER, lexeme.toString(), i));
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
                        tokens.add(new Token(TerminalSymbol.NUMBER, lexeme.toString(), i));
                        lexeme = new StringBuilder();
                        state = 0;
                        i--;
                    }
                    break;
                case 7:
                    if(Character.isDigit(character)) lexeme.append(character);
                    else {
                        tokens.add(new Token(TerminalSymbol.NUMBER, lexeme.toString(), i));
                        lexeme = new StringBuilder();
                        state = 0;
                        i--;
                    }
                    break;
                case 8:
                    if(character == '-') state = 9;
                    else {
                        tokens.add(new Token(TerminalSymbol.MINUS, "-", i));
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
                        tokens.add(new Token(TerminalSymbol.SLASH, "/", i));
                        state = 0;
                        i--;
                    }
                    break;
                case 11:
                    if (character == '*' && source.charAt(i+1) == '/') {
                        state = 0; i++;
                    }
                    break;
                default:
                    Main.error(i, "Caracater '"+character+"' invalido");
                    state = 0;
                    break;
            }
        }
        tokens.add(new Token(TerminalSymbol.EOF, "", source.length()));

        return tokens;
    }
}
