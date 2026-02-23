package semantic;

import dao.Table;
import parser.ast.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ExecutionVisitor implements StatementVisitor<QueryResult> {

    private final Path dbDirectory;

    public ExecutionVisitor() {
        this(Paths.get("db"));
    }

    public ExecutionVisitor(Path dbDirectory) {
        this.dbDirectory = dbDirectory;
    }

    @Override
    public QueryResult visitSelect(Select select) {
        String tableName = select.from.getLexeme();
        Table table;
        try {
            table = new Table(dbDirectory, tableName);
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            return new QueryResult("Error: Table not found.");
        }

        List<Map<String, Object>> rows = table.getRows();
        List<Map<String, Object>> filteredRows = new ArrayList<>();

        // 1. Filter (WHERE)
        for (Map<String, Object> row : rows) {
            boolean include = true;
            if (select.where != null) {
                try {
                    ExpressionEvaluator evaluator = new ExpressionEvaluator(row);
                    Object result = evaluator.evaluate(select.where);
                    if (result instanceof Boolean) {
                        include = (Boolean) result;
                    } else {
                        System.err.println("Runtime Error: WHERE clause did not evaluate to a boolean.");
                        include = false;
                    }
                } catch (RuntimeException e) {
                    System.err.println("Runtime Error: " + e.getMessage());
                    return new QueryResult("Error: " + e.getMessage());
                }
            }
            if (include) {
                filteredRows.add(row);
            }
        }

        // 2. Project (SELECT)
        List<String> headers = new ArrayList<>();
        List<Map<String, Object>> resultRows = new ArrayList<>();

        if (filteredRows.isEmpty()) {
            return new QueryResult(Collections.emptyList(), Collections.emptyList());
        }

        if (select.columns == null || select.columns.isEmpty()) {
            // SELECT *
            headers.addAll(table.getColumns());
            resultRows.addAll(filteredRows);
        } else {
            // SELECT expr1, expr2...
            for (int i = 0; i < select.columns.size(); i++) {
                // Try to use alias or expression string representation for header
                Expression expr = select.columns.get(i);
                if (expr instanceof Identifier) {
                    headers.add(((Identifier) expr).token.getLexeme());
                } else {
                    headers.add("Col_" + (i + 1));
                }
            }

            for (Map<String, Object> row : filteredRows) {
                try {
                    ExpressionEvaluator evaluator = new ExpressionEvaluator(row);
                    Map<String, Object> projectedRow = new java.util.LinkedHashMap<>();
                    
                    for (int i = 0; i < select.columns.size(); i++) {
                        Expression colExpr = select.columns.get(i);
                        Object val = evaluator.evaluate(colExpr);
                        projectedRow.put(headers.get(i), val);
                    }
                    resultRows.add(projectedRow);
                } catch (RuntimeException e) {
                    System.err.println("Runtime Error in SELECT list: " + e.getMessage());
                    return new QueryResult("Error in projection: " + e.getMessage());
                }
            }
        }

        return new QueryResult(headers, resultRows);
    }
}
