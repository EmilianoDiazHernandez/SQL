package ui;

import parser.SqlParser;
import parser.ast.Statement;
import semantic.ExecutionVisitor;
import semantic.QueryResult;
import scanner.Scanner;
import scanner.Token;

import javafx.application.Application;
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
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class SqlApp extends Application {

    private TextArea sqlEditor;
    private TableView<Map<String, Object>> resultTable;
    private ListView<String> tableList;
    private Label statusLabel;
    
    private ExecutionVisitor executor;
    private Path dbDirectory = Paths.get("db"); // Default

    @Override
    public void start(Stage primaryStage) {
        // Initialize executor with default path
        executor = new ExecutionVisitor(dbDirectory);

        BorderPane root = new BorderPane();
        
        // --- 1. Toolbar (Top) ---
        HBox toolbar = new HBox(10);
        toolbar.setPadding(new Insets(10));
        toolbar.setAlignment(Pos.CENTER_LEFT);
        toolbar.setStyle("-fx-background-color: #f4f4f4; -fx-border-color: #ddd; -fx-border-width: 0 0 1 0;");

        Button openButton = new Button("Open Folder...");
        openButton.setOnAction(e -> openDatabaseFolder(primaryStage));

        Button executeButton = new Button("Execute (Ctrl+Enter)");
        executeButton.setStyle("-fx-font-weight: bold; -fx-base: #4caf50;");
        executeButton.setOnAction(e -> executeQuery());

        toolbar.getChildren().addAll(openButton, new Separator(javafx.geometry.Orientation.VERTICAL), executeButton);
        root.setTop(toolbar);

        // --- 2. Sidebar (Left) - Database Schema ---
        VBox sidebar = new VBox(5);
        sidebar.setPadding(new Insets(5));
        sidebar.setPrefWidth(200);
        
        Label sidebarTitle = new Label("Tables");
        sidebarTitle.setStyle("-fx-font-weight: bold;");
        
        tableList = new ListView<>();
        refreshTableList();
        
        tableList.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                String selectedTable = tableList.getSelectionModel().getSelectedItem();
                if (selectedTable != null) {
                    sqlEditor.setText("SELECT * FROM " + selectedTable + ";");
                    executeQuery();
                }
            }
        });

        Button refreshButton = new Button("Refresh List");
        refreshButton.setMaxWidth(Double.MAX_VALUE);
        refreshButton.setOnAction(e -> refreshTableList());

        sidebar.getChildren().addAll(sidebarTitle, tableList, refreshButton);
        VBox.setVgrow(tableList, Priority.ALWAYS);

        // --- 3. Main Content (Center) ---
        SplitPane mainSplit = new SplitPane();
        mainSplit.setOrientation(javafx.geometry.Orientation.VERTICAL);

        // Editor Area
        VBox editorBox = new VBox(5);
        editorBox.setPadding(new Insets(5));
        Label editorLabel = new Label("SQL Query:");
        sqlEditor = new TextArea();
        sqlEditor.setPromptText("SELECT * FROM table WHERE ...");
        sqlEditor.setFont(javafx.scene.text.Font.font("Monospaced", 14));
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
        mainSplit.setDividerPositions(0.4);

        SplitPane horizontalSplit = new SplitPane();
        horizontalSplit.getItems().addAll(sidebar, mainSplit);
        horizontalSplit.setDividerPositions(0.2);

        root.setCenter(horizontalSplit);

        // --- 4. Status Bar (Bottom) ---
        HBox statusBar = new HBox();
        statusBar.setPadding(new Insets(5));
        statusBar.setStyle("-fx-border-color: #ddd; -fx-border-width: 1 0 0 0;");
        statusLabel = new Label("Ready. Database: " + dbDirectory.toAbsolutePath());
        statusBar.getChildren().add(statusLabel);
        root.setBottom(statusBar);

        Scene scene = new Scene(root, 900, 600);
        primaryStage.setTitle("SQL Client");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void openDatabaseFolder(Stage stage) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Database Folder (containing CSV files)");
        
        File initialDir = dbDirectory.toFile();
        if (initialDir.exists()) {
            directoryChooser.setInitialDirectory(initialDir);
        }

        File selectedDirectory = directoryChooser.showDialog(stage);
        if (selectedDirectory != null) {
            dbDirectory = selectedDirectory.toPath();
            executor = new ExecutionVisitor(dbDirectory);
            refreshTableList();
            statusLabel.setText("Database changed to: " + dbDirectory.toAbsolutePath());
        }
    }

    private void refreshTableList() {
        tableList.getItems().clear();
        File folder = dbDirectory.toFile();
        
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".csv"));
            if (files != null) {
                for (File f : files) {
                    tableList.getItems().add(f.getName().replace(".csv", ""));
                }
            } else {
                 tableList.setPlaceholder(new Label("No CSV files found."));
            }
        } else {
            tableList.setPlaceholder(new Label("Folder not found!"));
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
            Scanner scanner = new Scanner(source);
            List<Token> tokens = scanner.scanTokens();

            SqlParser parser = new SqlParser(tokens);
            List<Statement> statements = parser.parse();

            if (statements.isEmpty()) {
                statusLabel.setText("No valid statements found.");
                return;
            }

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
            e.printStackTrace(); 
        }
    }

    private void displayResult(QueryResult result) {
        statusLabel.setStyle("-fx-text-fill: black;");

        List<String> columns = result.getColumns();
        List<Map<String, Object>> rows = result.getRows();

        if (rows.isEmpty()) {
            resultTable.setPlaceholder(new Label(result.getMessage()));
            return;
        }

        for (String colName : columns) {
            TableColumn<Map<String, Object>, String> col = new TableColumn<>(colName);
            col.setCellValueFactory(data -> {
                Object val = data.getValue().get(colName);
                return new SimpleStringProperty(val == null ? "NULL" : val.toString());
            });
            resultTable.getColumns().add(col);
        }

        resultTable.getItems().addAll(rows);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
