package parser.ast.nodes;

import java.util.List;

public class NodeFrom {

    public List<NodeTable> tables;

    public NodeFrom(List<NodeTable> tables) {
        this.tables = tables;
    }

}
