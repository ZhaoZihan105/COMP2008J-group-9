package fi.game;

import fi.game.model.*;
import fi.game.view.*;
import fi.game.controller.*;

/**
 * 游戏主类
 */
public class Main {
    public static void main(String[] args) {
        // 创建游戏模型
        Game game = new Game();

        // 创建游戏视图
        GameGUI gui = new GameGUI();

        // 创建游戏控制器
        GameController controller = new GameController(game, gui);

        // 设置控制器引用
        gui.setGameController(controller);

        // 显示GUI
        gui.setVisible(true);
    }
}