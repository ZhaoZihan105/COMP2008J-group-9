package org.example.ForbiddenIsland.service.game.data;

//package org.example.ForbiddenIsland.service.game.data;

/**
 * 瓦片状态枚举
 * 定义瓦片的三种可能状态
 */
public enum TileStatus {
    NORMAL,   // 正常状态
    FLOODED,  // 已淹没状态（可加固恢复）
    SUNK      // 已沉没状态（永久移除）
}