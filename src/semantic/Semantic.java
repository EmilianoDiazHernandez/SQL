package semantic;

import dao.Table;
import parser.ast.nodes.NodeExp;
import parser.ast.nodes.NodeQuery;
import parser.ast.nodes.NodeTable;

import java.util.List;
import java.util.Objects;

public class Semantic {

    public Semantic(NodeQuery query) {
        analyze(query);
    }

    public void analyze(NodeQuery query) {
        Table t = null;
        for (NodeTable table : query.from.tables) {
            String tableName = table.alias != null ? table.alias.toString() : table.table.toString();
            String filePath = "db/" + table.table + ".csv";

            try {
                t = new Table(tableName, filePath);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                return;
            }
        }
        Objects.requireNonNull(t).printTable();

        for (List<String> row : t.content) {
            //CODIGO A COMPLETAR
        }

        /*for (NodeExp exp : query.select.columns) {
            exp.solve();
        }*/

    }

}
