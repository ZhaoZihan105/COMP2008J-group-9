package com.example.forbiddenisland_20250415.view;

import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

/**
 * 日志面板，用于显示游戏过程中的消息
 */
public class LogPanel extends TitledPane {
    private TextArea logArea;
    private ScrollPane scrollPane;

    public LogPanel() {
        setText("任务日志");
        setPrefHeight(150);

        // 创建日志文本区域
        logArea = new TextArea();
        logArea.setEditable(false);
        logArea.setWrapText(true);
        logArea.setFont(Font.font("Consolas", FontWeight.NORMAL, 12));

        // 设置增强的样式
        logArea.setStyle(
                "-fx-text-fill: #ecf0f1;" +
                        "-fx-control-inner-background: #2c3e50;" +
                        "-fx-background-insets: 0;" +
                        "-fx-background-radius: 5px;" +
                        "-fx-border-radius: 5px;" +
                        "-fx-focus-color: transparent;" +
                        "-fx-faint-focus-color: transparent;"
        );

        // 添加内阴影效果
        InnerShadow innerShadow = new InnerShadow();
        innerShadow.setOffsetX(1);
        innerShadow.setOffsetY(1);
        innerShadow.setColor(Color.BLACK);
        logArea.setEffect(innerShadow);

        // 创建初始欢迎消息
        logArea.setText("欢迎来到禁忌岛！\n准备好你的冒险吧...\n");

        // 创建滚动面板
        scrollPane = new ScrollPane(logArea);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        // 设置增强的滚动面板样式
        scrollPane.setStyle(
                "-fx-background-color: #2c3e50;" +
                        "-fx-background: #2c3e50;" +
                        "-fx-border-color: transparent;" +
                        "-fx-padding: 0;" +
                        "-fx-background-insets: 0;"
        );

        scrollPane.setPadding(new Insets(5));

        setContent(scrollPane);
        setCollapsible(false);
    }
    /**
     * 添加日志
     */
    public void addLog(String message) {
        // 格式化日志消息，添加时间戳
        java.time.LocalTime now = java.time.LocalTime.now();
        String formattedMessage = String.format("[%02d:%02d] %s",
                now.getHour(), now.getMinute(), message);

        // 添加消息
        logArea.appendText(formattedMessage + "\n");

        // 自动滚动到底部
        logArea.positionCaret(logArea.getText().length());

        // 创建淡入动画效果 (不能直接应用在TextArea的文本上，这里是示意)
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(500), logArea);
        fadeTransition.setFromValue(0.9);
        fadeTransition.setToValue(1.0);
        fadeTransition.play();
    }

    /**
     * 添加带有颜色标记的日志
     */
    public void addColoredLog(String message, LogType type) {
        String logPrefix;

        switch (type) {
            case INFO:
                logPrefix = "[信息] ";
                break;
            case WARNING:
                logPrefix = "[警告] ";
                break;
            case ERROR:
                logPrefix = "[错误] ";
                break;
            case SUCCESS:
                logPrefix = "[成功] ";
                break;
            default:
                logPrefix = "";
        }

        addLog(logPrefix + message);
    }

    /**
     * 清除日志
     */
    public void clearLog() {
        logArea.clear();
    }

    /**
     * 日志类型枚举
     */
    public enum LogType {
        INFO,
        WARNING,
        ERROR,
        SUCCESS
    }
}