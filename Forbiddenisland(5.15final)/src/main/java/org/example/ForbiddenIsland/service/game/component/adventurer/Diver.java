package org.example.ForbiddenIsland.service.game.component.adventurer;

/**
 * 潜水员角色类
 * 特殊能力：可以穿过已沉没的瓦片，游到最近的未沉没瓦片
 */
public class Diver extends Adventurer {
    /**
     * 初始化潜水员
     * @param order 玩家顺序
     */
    public Diver(int order) {
        super(order, "Diver");
        this.id = 0;  // 潜水员的ID为0
    }

    /**
     * 潜水员可以穿过已沉没的瓦片移动
     * 这是在GameData.selectTile()方法中特殊处理的
     */
}