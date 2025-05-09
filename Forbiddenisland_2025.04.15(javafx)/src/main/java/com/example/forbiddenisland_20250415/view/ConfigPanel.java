package com.example.forbiddenisland_20250415.view;
// ConfigPanel.java (JavaFX version)

import com.example.forbiddenisland_20250415.controller.*;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

/**
 * 游戏配置面板，用于设置游戏参数并启动游戏
 */
public class ConfigPanel extends TitledPane {
    private ComboBox<Integer> playerCountComboBox;
    private ComboBox<String> difficultyComboBox;
    private Button startButton;
    private Button rulesButton;
    private ImageView logoImageView;

    private GameController controller;

    public ConfigPanel() {
        setText("游戏设置");
        setPrefHeight(90);

        // 创建主布局容器
        HBox contentPane = new HBox(20);
        contentPane.setAlignment(Pos.CENTER);
        contentPane.setPadding(new Insets(10));

        // 尝试加载游戏标志
        try {
            var inputStream = getClass().getResourceAsStream("/logo.png");
            if (inputStream != null) {
                Image logoImage = new Image(inputStream);
                logoImageView = new ImageView(logoImage);
                logoImageView.setFitHeight(50);
                logoImageView.setPreserveRatio(true);
                inputStream.close();
            }
        } catch (Exception e) {
            System.err.println("无法加载游戏标志: " + e.getMessage());
        }

        // 创建游戏标题（如果没有标志或者作为备用）
        Label gameTitle = new Label("禁忌岛");
        gameTitle.setFont(Font.font("System", FontWeight.BOLD, 24));
        gameTitle.setTextFill(Color.WHITE);
        gameTitle.setTextAlignment(TextAlignment.CENTER);

        // 添加阴影效果
        DropShadow titleShadow = new DropShadow();
        titleShadow.setColor(Color.BLACK);
        titleShadow.setRadius(5);
        titleShadow.setOffsetY(2);
        gameTitle.setEffect(titleShadow);

        // 标志或标题容器
        VBox logoBox = new VBox(5);
        logoBox.setAlignment(Pos.CENTER);
        if (logoImageView != null) {
            logoBox.getChildren().add(logoImageView);
        } else {
            logoBox.getChildren().add(gameTitle);
        }

        // 创建玩家数设置
        VBox playerCountBox = new VBox(5);
        playerCountBox.setAlignment(Pos.CENTER);

        Label playerCountLabel = new Label("玩家数量");
        playerCountLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
        playerCountLabel.setTextFill(Color.WHITE);

        playerCountComboBox = new ComboBox<>(FXCollections.observableArrayList(2, 3, 4));
        playerCountComboBox.getSelectionModel().selectFirst();
        playerCountComboBox.setPrefWidth(80);

        playerCountBox.getChildren().addAll(playerCountLabel, playerCountComboBox);

        // 创建难度级别设置
        VBox difficultyBox = new VBox(5);
        difficultyBox.setAlignment(Pos.CENTER);

        Label difficultyLabel = new Label("难度级别");
        difficultyLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
        difficultyLabel.setTextFill(Color.WHITE);

        difficultyComboBox = new ComboBox<>(FXCollections.observableArrayList("新手", "普通", "精英", "传奇"));
        difficultyComboBox.getSelectionModel().selectFirst();
        difficultyComboBox.setPrefWidth(100);

        difficultyBox.getChildren().addAll(difficultyLabel, difficultyComboBox);

        // 创建按钮容器
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        // 创建规则按钮
        rulesButton = new Button("游戏规则");
        rulesButton.setPrefWidth(120);
        rulesButton.setPrefHeight(40);
        rulesButton.setFont(Font.font("System", FontWeight.BOLD, 14));

        // 创建开始按钮
        startButton = new Button("开始冒险");
        startButton.setPrefWidth(120);
        startButton.setPrefHeight(40);
        startButton.setFont(Font.font("System", FontWeight.BOLD, 14));

        // 添加按钮的阴影效果
        DropShadow buttonShadow = new DropShadow();
        buttonShadow.setColor(Color.BLACK);
        buttonShadow.setRadius(5);
        buttonShadow.setOffsetY(2);
        rulesButton.setEffect(buttonShadow);
        startButton.setEffect(buttonShadow);

        buttonBox.getChildren().addAll(rulesButton, startButton);

        // 创建一个填充空间的区域，使按钮靠右对齐
        HBox spacer = new HBox();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // 添加所有组件到内容面板
        contentPane.getChildren().addAll(
                logoBox,
                playerCountBox,
                difficultyBox,
                spacer,
                buttonBox
        );

        setContent(contentPane);
        setCollapsible(false);

        // 设置action listeners
        startButton.setOnAction(e -> {
            if (controller != null) {
                int playerCount = playerCountComboBox.getValue();
                int difficultyLevel = difficultyComboBox.getSelectionModel().getSelectedIndex() + 1;
                controller.startGame(playerCount, difficultyLevel);
                startButton.setDisable(true);
            }
        });

        rulesButton.setOnAction(e -> {
            RulesDialog.show();
        });
    }

    /**
     * 设置游戏控制器
     */
    public void setGameController(GameController controller) {
        this.controller = controller;
    }

    /**
     * 重置配置面板
     * 这是一个公共方法，在游戏结束时调用
     */
    public void reset() {
        startButton.setDisable(false);
    }
}