package org.example.ForbiddenIsland.gui.updater;

/**
 * UI更新器接口
 * 所有UI更新组件的基础接口
 */
public interface IUpdater {
    /**
     * 更新图形界面
     */
    void updateUI();


    /**
     * 游戏结束时禁用图形界面
     */
    void gameOver();
}