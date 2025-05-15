package org.example.ForbiddenIsland.service.game.component.adventurer;

/**
 * 信使角色类
 * 特殊能力：可以传递卡牌给任何位置的玩家，不需要在同一瓦片上
 */
public class Messenger extends Adventurer {
    /**
     * 初始化信使
     * @param order 玩家顺序
     */
    public Messenger(int order) {
        super(order, "Messenger");
        this.id = 3;  // 信使的ID为3
    }

    /**
     * 信使可以传递卡牌给任何位置的玩家
     * 这是在GameData.passCardTo()方法中特殊处理的
     */
}