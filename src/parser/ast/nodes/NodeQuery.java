package parser.ast.nodes;

public class NodeQuery{
    public NodeSelect select;
    public NodeFrom from;
    public NodeWhere where;

    public NodeQuery(NodeSelect select, NodeFrom from, NodeWhere where) {
        this.select = select;
        this.from = from;
        this.where = where;
    }
}
