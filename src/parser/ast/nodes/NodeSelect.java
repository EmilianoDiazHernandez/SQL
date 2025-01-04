package parser.ast.nodes;

import java.util.List;

public class NodeSelect {

    public List<NodeExp> columns;

    public NodeSelect(List<NodeExp> columns) {
        this.columns = columns;
    }
}
