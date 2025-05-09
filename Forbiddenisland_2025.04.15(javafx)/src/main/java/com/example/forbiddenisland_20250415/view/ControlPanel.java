package com.example.forbiddenisland_20250415.view;

import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ControlPanel extends TitledPane {
    private VBox content;

    public ControlPanel() {
        setText("游戏控制");
        content = new VBox(10);
        setContent(content);
    }

    public void addSaveLoadButtons(HBox buttons) {
        content.getChildren().add(buttons);
    }
} 