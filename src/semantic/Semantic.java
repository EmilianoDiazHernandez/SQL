package semantic;

import parser.ast.nodes.NodeExp;
import parser.ast.nodes.NodeQuery;

public class Semantic {

    public Semantic(NodeQuery query) {
        analyze(query);
    }

    public void analyze(NodeQuery query) {

        for(NodeExp exp: query.select.columns){
            exp.solve();
        }

    }
}
