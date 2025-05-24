package org.example.ForbiddenIsland.gui.updater;

/**
 * UI更新管理器
 * 协调所有UI组件的更新
 */
public class UpdateManager {
    // 各个更新器
    private static BoardUpdater boardUpdater;
    private static FloodUpdater floodUpdater;
    private static PlayerUpdater playerUpdater;
    private static TreasureUpdater treasureUpdater;
    private static WaterMeterUpdater waterMeterUpdater;
    private static ControlsUpdater controlsUpdater;

    /**
     * 初始化所有更新器
     */
    public UpdateManager() {
        boardUpdater = new BoardUpdater();
        floodUpdater = new FloodUpdater();
        playerUpdater = new PlayerUpdater();
        treasureUpdater = new TreasureUpdater();
        waterMeterUpdater = new WaterMeterUpdater();
        controlsUpdater = new ControlsUpdater();
    }

    // 获取各个更新器的方法
    public static BoardUpdater getBoardUpdater() {
        return boardUpdater;
    }

    public static FloodUpdater getFloodUpdater() {
        return floodUpdater;
    }

    public static PlayerUpdater getPlayerUpdater() {
        return playerUpdater;
    }

    public static TreasureUpdater getTreasureUpdater() {
        return treasureUpdater;
    }

    public static WaterMeterUpdater getWaterMeterUpdater() {
        return waterMeterUpdater;
    }

    public static ControlsUpdater getControlsUpdater() {
        return controlsUpdater;
    }

    /**
     * 更新所有UI组件
     */
    public static void updateAllUI() {
        boardUpdater.updateUI();
        floodUpdater.updateUI();
        playerUpdater.updateUI();
        treasureUpdater.updateUI();
        waterMeterUpdater.updateUI();
        controlsUpdater.updateUI();
    }

    /**
     * 游戏结束时禁用所有UI组件
     */
    public static void gameOverAll() {
        boardUpdater.gameOver();
        floodUpdater.gameOver();
        playerUpdater.gameOver();
        treasureUpdater.gameOver();
        waterMeterUpdater.gameOver();
        controlsUpdater.gameOver();
    }
}