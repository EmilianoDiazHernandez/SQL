package semantic;

import dao.Table;
import parser.ast.nodes.NodeQuery;
import parser.ast.nodes.NodeTable;

import java.util.HashMap;
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

        List<String> ids = t.content.get(0);
        HashMap<String, String> dataIds = new HashMap<>();

        for (List<String> row : t.content.subList(1, t.content.size())) {
            for (String cell : row)
                dataIds.put(ids.get(row.indexOf(cell)), cell);

            if (query.where != null)
                if (query.where.condition.solve(dataIds).lexeme.equals("true"))
                    System.out.println(row);

        }

    }

}
