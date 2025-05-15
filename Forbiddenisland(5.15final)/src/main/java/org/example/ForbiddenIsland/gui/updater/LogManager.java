package org.example.ForbiddenIsland.gui.updater;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

/**
 * 日志管理器
 * 用于显示游戏消息和指导玩家
 */
public class LogManager {
    // 日志文本区域
    private static TextArea logTextArea;

    /**
     * 设置日志文本区域
     * @param textArea JavaFX文本区域控件
     */
    public static void setLogTextArea(TextArea textArea) {
        logTextArea = textArea;
    }

    /**
     * 添加日志消息
     * @param message 要显示的消息
     */
    public static void logMessage(String message) {
        if (logTextArea != null) {
            // 使用JavaFX应用线程更新UI
            Platform.runLater(() -> {
                logTextArea.appendText(message + "\n");
                // 自动滚动到底部
                logTextArea.setScrollTop(Double.MAX_VALUE);
            });
        } else {
            // 如果UI尚未初始化，则打印到控制台
            System.out.println("[LOG] " + message);
        }
    }

    /**
     * 清空日志
     */
    public static void clearLog() {
        if (logTextArea != null) {
            Platform.runLater(() -> logTextArea.clear());
        }
    }
}