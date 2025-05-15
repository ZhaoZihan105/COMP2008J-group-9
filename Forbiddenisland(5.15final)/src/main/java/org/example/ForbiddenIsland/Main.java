package org.example.ForbiddenIsland;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane; // 添加这个导入语句
import javafx.stage.Stage;
import org.example.ForbiddenIsland.gui.GamePane;
import org.example.ForbiddenIsland.utils.Constants;

/**
 * 主入口类，启动JavaFX应用程序
 */
public class Main extends Application {
    public void start(Stage primaryStage) {
        // 创建主游戏界面
        GamePane gamePane = new GamePane();

        // 添加滚动面板
        ScrollPane scrollPane = new ScrollPane(gamePane);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true); // 让内容适应高度
        scrollPane.setPannable(true);


        Scene scene = new Scene(scrollPane, Constants.FRAME_WIDTH, Constants.FRAME_HEIGHT);

        // 设置窗口
        primaryStage.setTitle("FORBIDDEN ISLAND");
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}