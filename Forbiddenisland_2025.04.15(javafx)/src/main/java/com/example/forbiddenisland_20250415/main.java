package com.example.forbiddenisland_20250415;

import com.example.forbiddenisland_20250415.view.StartMenu;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * 游戏主类
 */
public class main extends Application {

    @Override
    public void start(Stage primaryStage) {
        StartMenu startMenu = new StartMenu(primaryStage);
        startMenu.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
