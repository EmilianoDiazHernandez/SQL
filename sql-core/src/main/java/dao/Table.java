package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

/**
 * Represents a database table loaded from a CSV file.
 */
public class Table {
    private final String name;
    private final List<String> columns;
    private final List<Map<String, Object>> rows;

    /**
     * Loads a table from a CSV file in the specified directory.
     *
     * @param baseDir   The directory containing the CSV file.
     * @param tableName The name of the table (without .csv extension).
     * @throws IOException If the file cannot be read.
     */
    public Table(Path baseDir, String tableName) throws IOException {
        this.name = tableName;
        this.columns = new ArrayList<>();
        this.rows = new ArrayList<>();
        loadFromCsv(baseDir, tableName);
    }

    private void loadFromCsv(Path baseDir, String tableName) throws IOException {
        File file = baseDir.resolve(tableName + ".csv").toFile();
        if (!file.exists()) {
            throw new IOException("Table '" + tableName + "' not found (file: " + file.getAbsolutePath() + ")");
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String headerLine = br.readLine();
            if (headerLine == null) return; // Empty file

            // Parse header
            String[] headers = headerLine.split(",");
            for (String header : headers) {
                columns.add(header.trim());
            }

            // Parse rows
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                Map<String, Object> row = new HashMap<>();

                for (int i = 0; i < headers.length; i++) {
                    if (i < values.length) {
                        String val = values[i].trim();
                        Object parsedVal = parseValue(val);
                        row.put(headers[i].trim(), parsedVal);
                    } else {
                        row.put(headers[i].trim(), null);
                    }
                }
                rows.add(row);
            }
        }
    }

    private Object parseValue(String val) {
        if ("true".equalsIgnoreCase(val)) return true;
        if ("false".equalsIgnoreCase(val)) return false;
        if ("null".equalsIgnoreCase(val)) return null;

        try {
            return Double.parseDouble(val);
        } catch (NumberFormatException e) {
            if (val.startsWith("\"") && val.endsWith("\"")) {
                return val.substring(1, val.length() - 1);
            }
            if (val.startsWith("'") && val.endsWith("'")) {
                return val.substring(1, val.length() - 1);
            }
            return val;
        }
    }

    public List<String> getColumns() {
        return Collections.unmodifiableList(columns);
    }

    public List<Map<String, Object>> getRows() {
        return Collections.unmodifiableList(rows);
    }

    public String getName() {
        return name;
    }
    
    @Override
    public String toString() {
        return "Table " + name + " (" + rows.size() + " rows)";
    }
}
