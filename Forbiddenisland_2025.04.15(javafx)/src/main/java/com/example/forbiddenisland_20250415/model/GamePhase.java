package com.example.forbiddenisland_20250415.model;

/**
 * 游戏阶段枚举
 */
public enum GamePhase {
    SETUP,              // 初始化设置
    PLAYER_ACTIONS,     // 玩家行动阶段
    DRAW_TREASURE_CARDS, // 抽取宝藏卡牌阶段
    DRAW_FLOOD_CARDS,   // 抽取洪水卡牌阶段
    SPECIAL_ACTION,     // 特殊行动阶段
    GAME_OVER           // 游戏结束
}