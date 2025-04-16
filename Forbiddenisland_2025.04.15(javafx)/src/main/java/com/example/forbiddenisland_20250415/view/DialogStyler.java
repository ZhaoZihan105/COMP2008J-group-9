package com.example.forbiddenisland_20250415.view;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;

/**
 * 对话框样式工具类的简化版本，不使用CSS文件，直接使用Java代码设置样式
 */
public class DialogStyler {

    /**
     * 应用自定义样式到对话框
     */
    public static void style(Alert alert) {
        DialogPane dialogPane = alert.getDialogPane();

        // 设置样式
        String dialogStyle =
                "-fx-background-color: #34495e;" +
                        "-fx-padding: 10px;" +
                        "-fx-border-color: #1abc9c;" +
                        "-fx-border-width: 2px;" +
                        "-fx-border-radius: 5px;";

        dialogPane.setStyle(dialogStyle);

        // 为按钮设置样式
        dialogPane.getButtonTypes().forEach(buttonType -> {
            javafx.scene.Node button = dialogPane.lookupButton(buttonType);
            if (button != null) {
                String buttonStyle;

                // 根据按钮类型应用不同的样式
                if (buttonType == ButtonType.OK || buttonType == ButtonType.YES) {
                    buttonStyle = "-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold;";
                } else if (buttonType == ButtonType.CANCEL || buttonType == ButtonType.NO) {
                    buttonStyle = "-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold;";
                } else {
                    buttonStyle = "-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold;";
                }

                button.setStyle(buttonStyle);
            }
        });

        // 为对话框添加阴影效果
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.BLACK);
        shadow.setRadius(10);
        shadow.setOffsetY(3);
        dialogPane.setEffect(shadow);
    }

    /**
     * 创建一个自定义样式的信息对话框
     */
    public static Alert createInfoAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        style(alert);
        return alert;
    }

    /**
     * 创建一个自定义样式的错误对话框
     */
    public static Alert createErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        style(alert);
        return alert;
    }

    /**
     * 创建一个自定义样式的确认对话框
     */
    public static Alert createConfirmAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        style(alert);
        return alert;
    }
}