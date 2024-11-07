package parser;

import grammar.TSymbol;
import scanner.Token;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Parser {

    List<Token> tokens;

    int tokenSize;

    int tokeIndex;

    Token lookahead;

    ParserState state;


    Parser(List<Token> tokens){
        this.tokens = tokens;
        this.tokenSize = tokens.size();
        this.tokeIndex = 0;
        this.lookahead = tokens.get(tokeIndex);
        this.state = ParserState.BEGIN;
    }

    void match(Token type){
        if(lookahead == type){
            tokeIndex++;
            if(tokeIndex >= tokenSize){
                state = ParserState.ERROR;
            } else {
                lookahead = tokens.get(tokeIndex);
            }
        } else {
            state = ParserState.ERROR;
            System.out.println("== ERROR (Parser): bad token at "+ lookahead.line);
            System.out.println("==== Expected a "+ type +" but got " + lookahead.lexeme);
        }
    }

    void parser(){
        Map<TableEntry, List<TSymbol>> map = new HashMap<>();

    }
}
