package scanner;

import grammar.TSymbol;
import main.Main;
import scanner.token.ControlStructure;
import scanner.token.Operand;
import scanner.token.Operator;
import scanner.token.Token;

import java.util.*;

public class Scanner {

    private final String source;

    private final List<Token> tokens = new ArrayList<>();

    private static final Map<String, TSymbol> reservedWords;
    private static final Map<String, TSymbol> reservedWordsOperators;
    private static final Map<String, TSymbol> reservedWordsControlStructure;

    static {
        reservedWords = new HashMap<>();
        reservedWords.put("FALSE", TSymbol.FALSE);
        reservedWords.put("TRUE", TSymbol.TRUE);
        reservedWords.put("NULL", TSymbol.NULL);

        reservedWordsOperators = new HashMap<>();
        reservedWordsOperators.put("DISTINCT", TSymbol.DISTINCT);
        reservedWordsOperators.put("NOT", TSymbol.NOT);
        reservedWordsOperators.put("AND", TSymbol.AND);
        reservedWordsOperators.put("OR", TSymbol.OR);
        reservedWordsOperators.put("IS", TSymbol.IS);

        reservedWordsControlStructure = new HashMap<>();
        reservedWordsControlStructure.put("SELECT", TSymbol.SELECT);
        reservedWordsControlStructure.put("WHERE", TSymbol.WHERE);
        reservedWordsControlStructure.put("FROM", TSymbol.FROM);
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
                        case '+': tokens.add(new Operator(TSymbol.PLUS, "+", i, 2)); break;
                        case '*': tokens.add(new Operator(TSymbol.STAR, "*", i, 2)); break;
                        case ',': tokens.add(new Token(TSymbol.COMA, ",", i)); break;
                        case ';': tokens.add(new Token(TSymbol.SEMICOLON, ";", i)); break;
                        case '.': tokens.add(new Token(TSymbol.DOT, ".", i)); break;
                        case '(': tokens.add(new Token(TSymbol.LEFT_PAREN, "(", i)); break;
                        case ')': tokens.add(new Token(TSymbol.RIGHT_PAREN, ")", i)); break;
                        case '=': tokens.add(new Operator(TSymbol.EQ, "=", i, 2)); break;
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
                    if (character == '=') tokens.add(new Operator(TSymbol.LE, "<=", i, 2));
                    else if(character == '>') tokens.add(new Operator(TSymbol.NE, "<>", i, 2));
                    else {
                        i--;
                        tokens.add(new Operator(TSymbol.LT, "<", i, 2));
                    }
                    state = 0;
                    break;
                case 2:
                    if (character == '=') tokens.add(new Operator(TSymbol.GE, ">=", i, 2));
                    else {
                        i--;
                        tokens.add(new Operator(TSymbol.GT, ">", i, 2));
                    }
                    state = 0;
                    break;
                case 3:
                    if(Character.isLetter(character) || Character.isDigit(character) || character == '_') lexeme.append(character);
                    else {
                        String lexemeU = lexeme.toString().toUpperCase();
                        if (reservedWords.containsKey(lexemeU))
                            tokens.add(new Operand(reservedWords.get(lexemeU), lexeme.toString(), i));
                        else if (reservedWordsOperators.containsKey(lexemeU))
                                tokens.add(new Operator(reservedWordsOperators.get(lexemeU), lexeme.toString(), i,
                                        lexemeU.equals("DISTINCT") || lexemeU.equals("NOT") ? 1:2));
                        else if (reservedWordsControlStructure.containsKey(lexemeU))
                            tokens.add(new ControlStructure(reservedWordsControlStructure.get(lexemeU), lexeme.toString(), i));
                        else
                            tokens.add(new Operand(TSymbol.ID, lexeme.toString(), i));
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
                        tokens.add(new Operand(TSymbol.NUMBER, lexeme.toString(), i));
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
                        tokens.add(new Operand(TSymbol.NUMBER, lexeme.toString(), i));
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
                        tokens.add(new Operand(TSymbol.NUMBER, lexeme.toString(), i));
                        lexeme = new StringBuilder();
                        state = 0;
                        i--;
                    }
                    break;
                case 7:
                    if(Character.isDigit(character)) lexeme.append(character);
                    else {
                        tokens.add(new Operand(TSymbol.NUMBER, lexeme.toString(), i));
                        lexeme = new StringBuilder();
                        state = 0;
                        i--;
                    }
                    break;
                case 8:
                    if(character == '-') state = 9;
                    else {
                        tokens.add(new Operator(TSymbol.MINUS, "-", i, 2));
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
                        tokens.add(new Operator(TSymbol.SLASH, "/", i, 2));
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
                        tokens.add(new Operand(TSymbol.STRING, lexeme.toString(), i));
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
