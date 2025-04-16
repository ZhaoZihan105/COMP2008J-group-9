package com.example.forbiddenisland_20250415.model;
/**
 * 瓦片状态枚举
 */
public enum TileState {
    NORMAL,     // 正常状态
    FLOODED,    // 被淹没状态
    SUNK        // 已沉没（移除游戏）
}
