package ui;

import dao.Table;
import parser.SqlParser;
import parser.ast.Statement;
import semantic.ExecutionVisitor;
import semantic.QueryResult;
import scanner.Scanner;
import scanner.Token;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class SqlApp extends Application {

    private TextArea sqlEditor;
    private TableView<Map<String, Object>> resultTable;
    private ListView<String> tableList;
    private Label statusLabel;
    private ExecutionVisitor executor;

    @Override
    public void start(Stage primaryStage) {
        executor = new ExecutionVisitor();

        BorderPane root = new BorderPane();
        
        // --- 1. Toolbar (Top) ---
        HBox toolbar = new HBox(10);
        toolbar.setPadding(new Insets(10));
        toolbar.setAlignment(Pos.CENTER_LEFT);
        toolbar.setStyle("-fx-background-color: #f4f4f4; -fx-border-color: #ddd; -fx-border-width: 0 0 1 0;");

        Button executeButton = new Button("Execute (Ctrl+Enter)");
        executeButton.setStyle("-fx-font-weight: bold; -fx-base: #4caf50;"); // Greenish button
        executeButton.setOnAction(e -> executeQuery());

        toolbar.getChildren().add(executeButton);
        root.setTop(toolbar);

        // --- 2. Sidebar (Left) - Database Schema ---
        VBox sidebar = new VBox(5);
        sidebar.setPadding(new Insets(5));
        sidebar.setPrefWidth(200);
        
        Label sidebarTitle = new Label("Database Tables");
        sidebarTitle.setStyle("-fx-font-weight: bold;");
        
        tableList = new ListView<>();
        refreshTableList();
        
        // Double click on table name inserts "SELECT * FROM table;"
        tableList.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                String selectedTable = tableList.getSelectionModel().getSelectedItem();
                if (selectedTable != null) {
                    sqlEditor.setText("SELECT * FROM " + selectedTable + ";");
                    executeQuery();
                }
            }
        });

        Button refreshButton = new Button("Refresh");
        refreshButton.setMaxWidth(Double.MAX_VALUE);
        refreshButton.setOnAction(e -> refreshTableList());

        sidebar.getChildren().addAll(sidebarTitle, tableList, refreshButton);
        VBox.setVgrow(tableList, Priority.ALWAYS);

        // --- 3. Main Content (Center) - SplitPane ---
        SplitPane mainSplit = new SplitPane();
        mainSplit.setOrientation(javafx.geometry.Orientation.VERTICAL);

        // Editor Area
        VBox editorBox = new VBox(5);
        editorBox.setPadding(new Insets(5));
        Label editorLabel = new Label("SQL Query:");
        sqlEditor = new TextArea();
        sqlEditor.setPromptText("SELECT * FROM table WHERE ...");
        sqlEditor.setFont(javafx.scene.text.Font.font("Monospaced", 14));
        // Add execute shortcut
        sqlEditor.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER && event.isControlDown()) {
                executeQuery();
                event.consume();
            }
        });

        editorBox.getChildren().addAll(editorLabel, sqlEditor);
        VBox.setVgrow(sqlEditor, Priority.ALWAYS);

        // Results Area
        VBox resultsBox = new VBox(5);
        resultsBox.setPadding(new Insets(5));
        Label resultsLabel = new Label("Results:");
        resultTable = new TableView<>();
        resultTable.setPlaceholder(new Label("No results to display"));
        
        resultsBox.getChildren().addAll(resultsLabel, resultTable);
        VBox.setVgrow(resultTable, Priority.ALWAYS);

        mainSplit.getItems().addAll(editorBox, resultsBox);
        mainSplit.setDividerPositions(0.4); // 40% for editor, 60% for results

        // Combine Sidebar and Main Content
        SplitPane horizontalSplit = new SplitPane();
        horizontalSplit.getItems().addAll(sidebar, mainSplit);
        horizontalSplit.setDividerPositions(0.2); // 20% for sidebar

        root.setCenter(horizontalSplit);

        // --- 4. Status Bar (Bottom) ---
        HBox statusBar = new HBox();
        statusBar.setPadding(new Insets(5));
        statusBar.setStyle("-fx-border-color: #ddd; -fx-border-width: 1 0 0 0;");
        statusLabel = new Label("Ready");
        statusBar.getChildren().add(statusLabel);
        root.setBottom(statusBar);

        // Scene Setup
        Scene scene = new Scene(root, 900, 600);
        primaryStage.setTitle("SQL Client");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void refreshTableList() {
        tableList.getItems().clear();
        File folder = new File("db"); // Ensure working directory is project root
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".csv"));
            if (files != null) {
                for (File f : files) {
                    tableList.getItems().add(f.getName().replace(".csv", ""));
                }
            }
        } else {
            tableList.setPlaceholder(new Label("No 'db' folder found!"));
        }
    }

    private void executeQuery() {
        String source = sqlEditor.getText();
        if (source == null || source.trim().isEmpty()) return;

        statusLabel.setText("Executing...");
        resultTable.getColumns().clear();
        resultTable.getItems().clear();

        long startTime = System.nanoTime();

        try {
            // 1. Scan
            Scanner scanner = new Scanner(source);
            List<Token> tokens = scanner.scanTokens();

            // 2. Parse
            SqlParser parser = new SqlParser(tokens);
            List<Statement> statements = parser.parse();

            if (statements.isEmpty()) {
                statusLabel.setText("No valid statements found.");
                return;
            }

            // Execute (only the last one for now, or loop and show last result)
            QueryResult lastResult = null;
            for (Statement stmt : statements) {
                lastResult = stmt.accept(executor);
            }

            long endTime = System.nanoTime();
            double durationMs = (endTime - startTime) / 1_000_000.0;

            if (lastResult != null) {
                displayResult(lastResult);
                statusLabel.setText(String.format("Query executed in %.2f ms. %s", durationMs, lastResult.getMessage()));
            } else {
                statusLabel.setText(String.format("Executed in %.2f ms (No result).", durationMs));
            }

        } catch (Exception e) {
            statusLabel.setText("Error: " + e.getMessage());
            statusLabel.setStyle("-fx-text-fill: red;");
            // Also show error in a dialog if critical? For now status bar is fine.
            e.printStackTrace(); 
        }
    }

    private void displayResult(QueryResult result) {
        statusLabel.setStyle("-fx-text-fill: black;"); // Reset color

        List<String> columns = result.getColumns();
        List<Map<String, Object>> rows = result.getRows();

        if (rows.isEmpty()) {
            resultTable.setPlaceholder(new Label(result.getMessage()));
            return;
        }

        // Create TableColumns dynamically
        for (String colName : columns) {
            TableColumn<Map<String, Object>, String> col = new TableColumn<>(colName);
            col.setCellValueFactory(data -> {
                Object val = data.getValue().get(colName);
                return new SimpleStringProperty(val == null ? "NULL" : val.toString());
            });
            resultTable.getColumns().add(col);
        }

        // Add Data
        resultTable.getItems().addAll(rows);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
