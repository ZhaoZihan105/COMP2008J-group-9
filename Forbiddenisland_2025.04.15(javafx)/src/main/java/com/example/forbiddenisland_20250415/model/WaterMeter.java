package com.example.forbiddenisland_20250415.model;

/**
 * 水位表类
 */
public class WaterMeter {
    private int level;

    public WaterMeter() {
        level = 1; // 从"新手"级别开始
    }

    public void rise() {
        if (level < 10) {
            level++;
        }
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        if (level >= 1 && level <= 10) {
            this.level = level;
        }
    }

    public int getNumFloodCards() {
        // 根据当前水位返回需要抽取的洪水卡数量
        if (level <= 2) return 2;
        if (level <= 5) return 3;
        if (level <= 7) return 4;
        if (level <= 9) return 5;
        return 6; // Level 10
    }

    public boolean isGameOver() {
        return level >= 10;
    }
}