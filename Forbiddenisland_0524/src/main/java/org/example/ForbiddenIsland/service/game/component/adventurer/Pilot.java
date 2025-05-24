package org.example.ForbiddenIsland.service.game.component.adventurer;

/**
 * 飞行员角色类
 * 特殊能力：每回合可以飞到任何未沉没的瓦片上
 */
public class Pilot extends Adventurer {
    /**
     * 初始化飞行员
     * @param order 玩家顺序
     */
    public Pilot(int order) {
        super(order, "Pilot");
        this.id = 5;  // 飞行员的ID为5
    }

    /**
     * 飞行员可以飞到任何未沉没的瓦片上
     * 这是在GameData.selectTile()方法中特殊处理的
     */
}