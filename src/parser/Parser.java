package parser;

import grammar.NTSymbol;
import grammar.ProductionTable;
import grammar.Symbol;
import grammar.TSymbol;
import parser.ast.Tree;
import parser.ast.nodes.NodeQuery;
import scanner.token.Token;

import java.util.*;

public class Parser {

    List<Token> tokens;

    int tokenSize;

    int tokeIndex;

    Token lookahead;

    ParserState state;

    Map<Integer, List<Symbol>> table;


    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.tokenSize = tokens.size();
        this.tokeIndex = 0;
        this.lookahead = tokens.get(tokeIndex);
        this.state = ParserState.BEGIN;
        this.table = ProductionTable.TABLE;
    }

    void match(Symbol type) {
        if (lookahead.type == type) {
            tokeIndex++;
            if (tokeIndex < tokenSize)
                lookahead = tokens.get(tokeIndex);
        } else {
            System.out.println("== ERROR (Parser): bad token at " + lookahead.line);
            System.out.println("==== Expected a " + type + " but got " + lookahead.lexeme);
        }
    }

    public NodeQuery parse() {
        Stack<Symbol> stack = new Stack<>();
        stack.push(TSymbol.EOF);
        stack.push(NTSymbol.Q);

        Symbol X = stack.peek();

        while (!X.equals(TSymbol.EOF)) {
            if (X.equals(lookahead.type)) {
                stack.pop();
                match(X);
            } else if (X instanceof TSymbol) {
                System.out.println("== ERROR (Parser): bad token at " + lookahead.line);
                System.out.println("== Expected a " + X + " but got " + lookahead.type);
                break;
            } else {
                List<Symbol> production = table.get(new TableEntry((NTSymbol) X, lookahead.type).hashCode());
                if (production == null) {
                    System.out.println("== ERROR (Parser): no production for [" + X + "," + lookahead.type + "]");
                    break;
                } else {
                    stack.pop();
                    for (int i = production.size() - 1; i >= 0; i--) {
                        if (!production.get(i).equals(TSymbol.EPSILON))
                            stack.push(production.get(i));
                    }
                }
            }
            X = stack.peek();
        }

        if (X.equals(TSymbol.EOF) && lookahead.type == TSymbol.EOF) {
            state = ParserState.FINISH;

            Tree tree = new Tree(Postfix.convert(tokens));
            tree.printAST(tree.root, 0);
            return tree.root;
        }
        state = ParserState.ERROR;
        System.out.println(state);
        return null;
    }
}
