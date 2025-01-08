package scanner;

import grammar.TSymbol;
import main.Main;
import scanner.token.ControlStructure;
import scanner.token.Operand;
import scanner.token.Operator;
import scanner.token.Token;

import java.util.*;

import static grammar.TSymbol.*;

public class Scanner {

    private final String source;

    private final List<Token> tokens = new ArrayList<>();

    private static final Map<String, TSymbol> reservedWords;
    private static final Map<String, TSymbol> reservedWordsOperators;
    private static final Map<String, TSymbol> reservedWordsControlStructure;

    static {
        reservedWords = new HashMap<>();
        reservedWords.put("FALSE", FALSE);
        reservedWords.put("TRUE", TRUE);
        reservedWords.put("NULL", NULL);

        reservedWordsOperators = new HashMap<>();
        reservedWordsOperators.put("DISTINCT", DISTINCT);
        reservedWordsOperators.put("NOT", NOT);
        reservedWordsOperators.put("AND", AND);
        reservedWordsOperators.put("OR", OR);
        reservedWordsOperators.put("IS", IS);

        reservedWordsControlStructure = new HashMap<>();
        reservedWordsControlStructure.put("SELECT", SELECT);
        reservedWordsControlStructure.put("WHERE", WHERE);
        reservedWordsControlStructure.put("FROM", FROM);
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
                        case '+': tokens.add(new Operator(PLUS, "+", i, 2)); break;
                        case '*': tokens.add(new Operator(STAR, "*", i, 2)); break;
                        case ',': tokens.add(new Token(COMA, ",", i)); break;
                        case ';': tokens.add(new Token(SEMICOLON, ";", i)); break;
                        case '.': tokens.add(new Token(DOT, ".", i)); break;
                        case '(': tokens.add(new Token(LEFT_PAREN, "(", i)); break;
                        case ')': tokens.add(new Token(RIGHT_PAREN, ")", i)); break;
                        case '=': tokens.add(new Operator(EQ, "=", i, 2)); break;
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
                    if (character == '=') tokens.add(new Operator(LE, "<=", i, 2));
                    else if(character == '>') tokens.add(new Operator(NE, "<>", i, 2));
                    else {
                        i--;
                        tokens.add(new Operator(LT, "<", i, 2));
                    }
                    state = 0;
                    break;
                case 2:
                    if (character == '=') tokens.add(new Operator(GE, ">=", i, 2));
                    else {
                        i--;
                        tokens.add(new Operator(GT, ">", i, 2));
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
                            tokens.add(new Operand(ID, lexeme.toString(), i));
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
                        tokens.add(new Operand(NUMBER, lexeme.toString(), i));
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
                        tokens.add(new Operand(NUMBER, lexeme.toString(), i));
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
                        tokens.add(new Operand(NUMBER, lexeme.toString(), i));
                        lexeme = new StringBuilder();
                        state = 0;
                        i--;
                    }
                    break;
                case 7:
                    if(Character.isDigit(character)) lexeme.append(character);
                    else {
                        tokens.add(new Operand(NUMBER, lexeme.toString(), i));
                        lexeme = new StringBuilder();
                        state = 0;
                        i--;
                    }
                    break;
                case 8:
                    if(character == '-') state = 9;
                    else {
                        tokens.add(new Operator(MINUS, "-", i, 2));
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
                        tokens.add(new Operator(SLASH, "/", i, 2));
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
                        tokens.add(new Operand(STRING, lexeme.toString(), i));
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
        tokens.add(new ControlStructure(EOF, "", source.length()));

        return tokens;
    }
}
