package com.example.forbiddenisland_20250415.view;
// ActionPanel.java (JavaFX version)

import com.example.forbiddenisland_20250415.model.*;
import com.example.forbiddenisland_20250415.controller.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.TilePane;
import javafx.scene.control.TitledPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * 行动按钮面板，用于游戏操作
 */
public class ActionPanel extends TitledPane {
    private Button moveButton;
    private Button shoreUpButton;
    private Button giveCardButton;
    private Button captureTreasureButton;
    private Button nextButton;
    private Button specialActionButton;
    private Button clearButton;
    private Button liftOffButton;

    private GameController controller;

    public ActionPanel() {
        setText("行动控制台");
        setPrefHeight(100);

        // 使用HBox代替FlowPane以获得更好的按钮布局
        HBox contentPane = new HBox();
        contentPane.setSpacing(15);
        contentPane.setPadding(new Insets(10));
        contentPane.setAlignment(Pos.CENTER);

        // 创建按钮
        moveButton = createButton("移动到", "移动你的冒险家到相邻的瓦片");
        shoreUpButton = createButton("加固", "加固相邻的或当前位置的洪水瓦片");
        giveCardButton = createButton("给予卡牌", "将宝藏卡牌给予同一瓦片上的另一位玩家");
        captureTreasureButton = createButton("捕获宝藏", "在拥有4张相同宝藏卡片时在对应瓦片上捕获宝藏");
        nextButton = createButton("下一步", "完成当前行动并进入下一阶段");
        specialActionButton = createButton("特殊行动", "使用你的角色特殊能力");
        clearButton = createButton("清除选择", "清除当前选择");
        liftOffButton = createButton("起飞", "当所有宝藏都被捕获且所有玩家都在愚者着陆点时起飞逃生");

        // 设置特殊按钮样式 - 确保所有按钮都已初始化
        nextButton.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #2ecc71, #27ae60);" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;"
        );

        specialActionButton.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #e67e22, #d35400);" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;"
        );

        liftOffButton.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #9b59b6, #8e44ad);" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;"
        );

        // 添加按钮到面板
        contentPane.getChildren().addAll(moveButton, shoreUpButton, giveCardButton,
                captureTreasureButton, nextButton, specialActionButton, clearButton, liftOffButton);

        setContent(contentPane);
        setCollapsible(false);

        // 设置action listeners
        moveButton.setOnAction(e -> {
            if (controller != null) controller.onMoveAction();
        });

        shoreUpButton.setOnAction(e -> {
            if (controller != null) controller.onShoreUpAction();
        });

        giveCardButton.setOnAction(e -> {
            if (controller != null) controller.onGiveCardAction();
        });

        captureTreasureButton.setOnAction(e -> {
            if (controller != null) controller.onCaptureTreasureAction();
        });

        nextButton.setOnAction(e -> {
            if (controller != null) controller.onNextAction();
        });

        specialActionButton.setOnAction(e -> {
            if (controller != null) controller.onSpecialAction();
        });

        clearButton.setOnAction(e -> {
            if (controller != null) controller.onClearSelections();
        });

        liftOffButton.setOnAction(e -> {
            if (controller != null) controller.onLiftOffAction();
        });

        // 初始禁用所有按钮
        updateButtons(GamePhase.SETUP, null);
    }

    /**
     * 创建一个带有工具提示的美化按钮
     */
    private Button createButton(String text, String tooltipText) {
        Button button = new Button(text);

        // 设置按钮大小
        button.setPrefWidth(120);
        button.setPrefHeight(40);

        // 设置通用按钮样式
        button.setStyle(
                "-fx-background-color: #34495e;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 8px 12px;" +
                        "-fx-background-radius: 5px;" +
                        "-fx-border-radius: 5px;"
        );

        // 设置工具提示
        Tooltip tooltip = new Tooltip(tooltipText);
        tooltip.setFont(Font.font("System", FontWeight.NORMAL, 12));
        button.setTooltip(tooltip);

        // 设置按钮字体
        button.setFont(Font.font("System", FontWeight.BOLD, 12));

        // 添加悬停效果
        button.setOnMouseEntered(e ->
                button.setStyle(button.getStyle().replace("-fx-background-color: #34495e;", "-fx-background-color: #2c3e50;"))
        );

        button.setOnMouseExited(e ->
                button.setStyle(button.getStyle().replace("-fx-background-color: #2c3e50;", "-fx-background-color: #34495e;"))
        );

        // 添加阴影效果
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.BLACK);
        shadow.setRadius(5);
        shadow.setOffsetY(2);
        button.setEffect(shadow);

        return button;
    }

    /**
     * 更新按钮状态
     */
    public void updateButtons(GamePhase phase, Player currentPlayer) {
        boolean inActionPhase = (phase == GamePhase.PLAYER_ACTIONS);
        boolean inDrawPhase = (phase == GamePhase.DRAW_TREASURE_CARDS);
        boolean inFloodPhase = (phase == GamePhase.DRAW_FLOOD_CARDS);

        moveButton.setDisable(!inActionPhase);
        shoreUpButton.setDisable(!inActionPhase);
        giveCardButton.setDisable(!inActionPhase);
        captureTreasureButton.setDisable(!inActionPhase);

        specialActionButton.setDisable(!(inActionPhase || inDrawPhase || inFloodPhase));
        clearButton.setDisable(!(inActionPhase || inDrawPhase || inFloodPhase));

        nextButton.setDisable(false);  // 始终启用下一步按钮

        // 只有在满足所有条件时才启用起飞按钮
        // 需要基于是否所有宝藏都已捕获且所有玩家都在愚者着陆点
        liftOffButton.setDisable(true);  // 默认禁用

        // 为禁用的按钮添加半透明效果
        applyDisabledEffect(moveButton);
        applyDisabledEffect(shoreUpButton);
        applyDisabledEffect(giveCardButton);
        applyDisabledEffect(captureTreasureButton);
        applyDisabledEffect(specialActionButton);
        applyDisabledEffect(clearButton);
        applyDisabledEffect(liftOffButton);
    }

    /**
     * 为禁用的按钮应用半透明效果
     */
    private void applyDisabledEffect(Button button) {
        if (button.isDisabled()) {
            button.setOpacity(0.5);
        } else {
            button.setOpacity(1.0);
        }
    }

    /**
     * 设置游戏控制器
     */
    public void setGameController(GameController controller) {
        this.controller = controller;
    }
}