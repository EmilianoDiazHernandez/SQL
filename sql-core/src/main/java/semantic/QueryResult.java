package semantic;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Represents the result of an executed SQL SELECT statement.
 */
public class QueryResult {
    private final List<String> columns;
    private final List<Map<String, Object>> rows;
    private final String message;

    // Constructor for successful SELECT results
    public QueryResult(List<String> columns, List<Map<String, Object>> rows) {
        this.columns = columns;
        this.rows = rows;
        this.message = rows.size() + " row(s) returned.";
    }

    // Constructor for errors
    public QueryResult(String message) {
        this.columns = Collections.emptyList();
        this.rows = Collections.emptyList();
        this.message = message;
    }

    public List<String> getColumns() {
        return columns;
    }

    public List<Map<String, Object>> getRows() {
        return rows;
    }

    public String getMessage() {
        return message;
    }
}
