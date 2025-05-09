package com.example.forbiddenisland_20250415.view;

import com.example.forbiddenisland_20250415.model.Game;
import com.example.forbiddenisland_20250415.controller.GameController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class StartMenu {
    private Stage primaryStage;
    private GameGUI gameGUI;

    public StartMenu(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void show() {
        // 创建主容器
        StackPane mainContainer = new StackPane();
        mainContainer.setStyle("-fx-background-color: linear-gradient(to bottom, #1a2a3a, #2c3e50);");

        // 创建背景装饰
        Rectangle backgroundDecoration = new Rectangle(800, 600);
        backgroundDecoration.setFill(Color.TRANSPARENT);
        backgroundDecoration.setStroke(Color.WHITE);
        backgroundDecoration.setStrokeWidth(2);
        backgroundDecoration.setOpacity(0.1);

        // 创建内容容器
        VBox contentBox = new VBox(30);
        contentBox.setPadding(new Insets(50));
        contentBox.setAlignment(Pos.CENTER);

        // 创建标题
        Text title = new Text("禁忌岛");
        title.setFont(Font.font("System", FontWeight.BOLD, 48));
        title.setFill(Color.WHITE);
        title.setEffect(new DropShadow(10, Color.BLACK));

        // 创建副标题
        Text subtitle = new Text("Forbidden Island");
        subtitle.setFont(Font.font("System", FontWeight.BOLD, 24));
        subtitle.setFill(Color.WHITE);
        subtitle.setEffect(new DropShadow(5, Color.BLACK));

        // 创建装饰线
        Rectangle line = new Rectangle(300, 2);
        line.setFill(Color.WHITE);
        line.setOpacity(0.3);

        // 创建按钮容器
        VBox buttonBox = new VBox(20);
        buttonBox.setAlignment(Pos.CENTER);

        // 创建开始游戏按钮
        Button startButton = createButton("开始游戏");
        startButton.setOnAction(e -> {
            Game game = new Game();
            gameGUI = new GameGUI(primaryStage);
            GameController controller = new GameController(game, gameGUI);
            gameGUI.setGameController(controller);
            gameGUI.show();
        });

        // 创建游戏规则按钮
        Button rulesButton = createButton("游戏规则");
        rulesButton.setOnAction(e -> RulesDialog.show());

        // 创建退出游戏按钮
        Button exitButton = createButton("退出游戏");
        exitButton.setOnAction(e -> System.exit(0));

        // 添加按钮到按钮容器
        buttonBox.getChildren().addAll(startButton, rulesButton, exitButton);

        // 添加所有组件到内容容器
        contentBox.getChildren().addAll(title, subtitle, line, buttonBox);

        // 添加所有组件到主容器
        mainContainer.getChildren().addAll(backgroundDecoration, contentBox);

        // 创建场景
        Scene scene = new Scene(mainContainer, 800, 600);
        primaryStage.setTitle("禁忌岛");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Button createButton(String text) {
        Button button = new Button(text);
        button.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #3498db, #2980b9);" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 18px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 15 40;" +
            "-fx-background-radius: 5;" +
            "-fx-border-color: #2c3e50;" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 5;"
        );

        // 添加悬停效果
        button.setOnMouseEntered(e -> {
            button.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #2980b9, #1a5276);" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 18px;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 15 40;" +
                "-fx-background-radius: 5;" +
                "-fx-border-color: #2c3e50;" +
                "-fx-border-width: 2;" +
                "-fx-border-radius: 5;"
            );
        });

        button.setOnMouseExited(e -> {
            button.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #3498db, #2980b9);" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 18px;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 15 40;" +
                "-fx-background-radius: 5;" +
                "-fx-border-color: #2c3e50;" +
                "-fx-border-width: 2;" +
                "-fx-border-radius: 5;"
            );
        });

        // 添加阴影效果
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.BLACK);
        shadow.setRadius(5);
        shadow.setOffsetY(3);
        button.setEffect(shadow);

        return button;
    }
} 