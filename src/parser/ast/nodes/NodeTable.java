package parser.ast.nodes;

import scanner.token.Token;

public class NodeTable {

    public Token table;
    public Token alias;

    public NodeTable(Token table, Token alias) {
        this.table = table;
        this.alias = alias;
    }

}
