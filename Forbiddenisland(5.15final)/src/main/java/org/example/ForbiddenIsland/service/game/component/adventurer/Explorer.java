package org.example.ForbiddenIsland.service.game.component.adventurer;

/**
 * 探险家角色类
 * 特殊能力：可以对角线移动和加固瓦片
 */
public class Explorer extends Adventurer {
    /**
     * 初始化探险家
     * @param order 玩家顺序
     */
    public Explorer(int order) {
        super(order, "Explorer");
        this.id = 2;  // 探险家的ID为2
    }

    /**
     * 探险家可以对角线移动和加固瓦片
     * 这是在GameData.selectTile()方法中特殊处理的
     */
}