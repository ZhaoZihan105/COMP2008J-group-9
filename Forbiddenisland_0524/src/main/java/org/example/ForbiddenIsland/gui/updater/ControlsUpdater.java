package org.example.ForbiddenIsland.gui.updater;


import javafx.application.Platform;
import javafx.scene.control.Button;
import org.example.ForbiddenIsland.gui.console.ConsolePane;
import org.example.ForbiddenIsland.service.game.Game;

import java.util.ArrayList;

/**
 * 控制按钮更新器
 * 负责更新控制台面板中的按钮状态
 */
public class ControlsUpdater implements IUpdater {
    // 控制台面板引用
    private static ConsolePane consolePane;

    /**
     * 设置控制台面板引用
     * @param pane 控制台面板
     */
    public static void setConsolePane(ConsolePane pane) {
        consolePane = pane;
    }

    @Override
    public void updateUI() {
        if (consolePane == null) return;

        Platform.runLater(() -> {
            ArrayList<Button> consoleButtons = consolePane.getControlButtons();

            // 在假回合中的按钮状态
            if (Game.isInFakeRound()) {
                for (Button button : consoleButtons) {
                    button.setDisable(Game.isNeedToSave());
                }

                // 在救援模式下，只允许移动
                if (Game.isNeedToSave()) {
                    consoleButtons.get(1).setDisable(false); // "移动到"按钮
                }
            }
            // 正常回合中的按钮状态
            else {
                for (Button button : consoleButtons) {
                    button.setDisable(Game.isNeedToSave());
                }
            }
        });
    }

    @Override
    public void gameOver() {
        if (consolePane == null) return;

        Platform.runLater(() -> {
            // 游戏结束时禁用所有控制按钮
            for (Button button : consolePane.getControlButtons()) {
                button.setDisable(true);
            }
        });
    }
}
