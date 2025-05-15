package org.example.ForbiddenIsland.service.game.component.adventurer;

/**
 * 导航员角色类
 * 特殊能力：可以移动其他玩家1-2个相邻瓦片
 */
public class Navigator extends Adventurer {
    /**
     * 初始化导航员
     * @param order 玩家顺序
     */
    public Navigator(int order) {
        super(order, "Navigator");
        this.id = 4;  // 导航员的ID为4
    }

    /**
     * 导航员可以移动其他玩家
     * 这需要在特殊行动中实现
     */
}