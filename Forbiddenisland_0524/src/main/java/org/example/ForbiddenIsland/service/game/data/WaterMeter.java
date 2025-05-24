package org.example.ForbiddenIsland.service.game.data;

import org.example.ForbiddenIsland.gui.updater.LogManager;
import org.example.ForbiddenIsland.service.game.Game;

/**
 * 水位计
 * 跟踪当前水位和决定每回合抽取的洪水卡数量
 */
public class WaterMeter {
    private int waterLevel;       // 当前水位
    private int floodCardCount;   // 每回合抽取的洪水卡数量
    private String imagePath;     // 水位计图像路径（用于 ImageLoader）

    /**
     * 初始化水位计
     * @param waterLevel 初始水位（1-5）
     */
    public WaterMeter(int waterLevel) {
        this.waterLevel = waterLevel;
        this.imagePath = "water_meter/level_" + waterLevel + ".png"; // ✅ 修正路径
        setFloodCardCount();
    }

    public void waterRise() {
        waterLevel += 1;
        imagePath = "water_meter/level_" + waterLevel + ".png"; // ✅ 修正路径
        setFloodCardCount();

        if (waterLevel == 10) {
            LogManager.logMessage("【警告！】水位已达到最高点，游戏结束");
            Game.gameOver(false);
        }
    }

    private void setFloodCardCount() {
        switch (waterLevel) {
            case 1, 2 -> floodCardCount = 2;
            case 3, 4, 5 -> floodCardCount = 3;
            case 6, 7 -> floodCardCount = 4;
            case 8, 9, 10 -> floodCardCount = 5;
        }
    }

    public int getFloodCardCount() {
        return floodCardCount;
    }

    public String getImage() {
        return imagePath;
    }

    public int getWaterLevel() {
        return waterLevel;
    }
}
