package parser.ast.nodes;

public class NodeWhere {

    public final NodeExp condition;

    public NodeWhere(NodeExp condition) {
        this.condition = condition;
    }

}
