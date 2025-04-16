module com.example.forbiddenisland_20250415 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    exports com.example.forbiddenisland_20250415;
    exports com.example.forbiddenisland_20250415.view;
    exports com.example.forbiddenisland_20250415.controller;
    exports com.example.forbiddenisland_20250415.model;

    opens com.example.forbiddenisland_20250415 to javafx.fxml;
    opens com.example.forbiddenisland_20250415.view to javafx.fxml;
    opens com.example.forbiddenisland_20250415.controller to javafx.fxml;
}