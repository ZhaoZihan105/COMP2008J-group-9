package com.example.forbiddenisland_20250415;

import com.example.forbiddenisland_20250415.model.*;
import com.example.forbiddenisland_20250415.controller.*;
import com.example.forbiddenisland_20250415.view.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * 游戏主类
 */
public class main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // 创建游戏模型
        Game game = new Game();

        // 创建游戏视图
        GameGUI gui = new GameGUI(primaryStage);

        // 创建游戏控制器
        GameController controller = new GameController(game, gui);

        // 设置控制器引用
        gui.setGameController(controller);

        // 显示GUI
        gui.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
