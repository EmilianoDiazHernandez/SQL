module sql.ui {
    requires javafx.controls;
    requires javafx.fxml;

    requires sql.core;

    opens ui to javafx.graphics, javafx.fxml;
    exports ui;
}