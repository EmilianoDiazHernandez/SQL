package semantic;

import dao.Table;
import parser.ast.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExecutionVisitor implements StatementVisitor<Void> {

    @Override
    public Void visitSelect(Select select) {
        String tableName = select.from.getLexeme();
        Table table;
        try {
            table = new Table(tableName);
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            return null;
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
                    // Skip row on error? Or abort?
                    // For now, print error and abort.
                    System.err.println("Runtime Error: " + e.getMessage());
                    return null;
                }
            }
            if (include) {
                filteredRows.add(row);
            }
        }

        // 2. Project (SELECT) and Print
        if (filteredRows.isEmpty()) {
            System.out.println("(No rows returned)");
            return null;
        }

        if (select.columns == null || select.columns.isEmpty()) {
            // SELECT *
            printHeader(table.getColumns());
            for (Map<String, Object> row : filteredRows) {
                printRow(row, table.getColumns());
            }
        } else {
            // SELECT expr1, expr2...
            // Print a dummy header or try to infer names?
            // Let's print "Column 1 | Column 2..."
            List<String> headers = new ArrayList<>();
            for (int i = 0; i < select.columns.size(); i++) {
                headers.add("Col " + (i + 1));
            }
            printHeader(headers);

            for (Map<String, Object> row : filteredRows) {
                try {
                    ExpressionEvaluator evaluator = new ExpressionEvaluator(row);
                    List<String> values = new ArrayList<>();
                    for (Expression colExpr : select.columns) {
                        Object val = evaluator.evaluate(colExpr);
                        values.add(val == null ? "NULL" : val.toString());
                    }
                    System.out.println(String.join(" | ", values));
                } catch (RuntimeException e) {
                    System.err.println("Runtime Error in SELECT list: " + e.getMessage());
                    return null;
                }
            }
        }

        return null;
    }

    private void printHeader(List<String> columns) {
        System.out.println(String.join(" | ", columns));
        System.out.println("-".repeat(Math.max(columns.size() * 10, 20))); 
    }

    private void printRow(Map<String, Object> row, List<String> columns) {
        List<String> values = new ArrayList<>();
        for (String col : columns) {
            Object val = row.get(col);
            values.add(val == null ? "NULL" : val.toString());
        }
        System.out.println(String.join(" | ", values));
    }
}
