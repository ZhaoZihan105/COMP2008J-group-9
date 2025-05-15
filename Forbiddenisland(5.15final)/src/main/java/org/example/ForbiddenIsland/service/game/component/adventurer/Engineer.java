package org.example.ForbiddenIsland.service.game.component.adventurer;

/**
 * 工程师角色类
 * 特殊能力：可以一次行动加固两个瓦片
 */
public class Engineer extends Adventurer {
    // 记录工程师的特殊能力使用情况
    private int shoreUpCount = 1;  // 可以额外加固的次数

    /**
     * 初始化工程师
     * @param order 玩家顺序
     */
    public Engineer(int order) {
        super(order, "Engineer");
        this.id = 1;  // 工程师的ID为1
    }

    /**
     * 获取当前可以额外加固的次数
     * @return 额外加固次数
     */
    public int getShoreUpCount() {
        return shoreUpCount;
    }

    /**
     * 使用一次额外加固能力
     */
    public void useShoreUp() {
        this.shoreUpCount -= 1;
    }

    /**
     * 重置额外加固能力（每回合开始时）
     */
    public void resetShoreUpCount() {
        this.shoreUpCount = 1;
    }
}