package org.example.ForbiddenIsland.service.game.component.adventurer;

import org.example.ForbiddenIsland.service.game.component.cards.TreasureFigurines;
import org.example.ForbiddenIsland.utils.GameMap;

import java.util.ArrayList;

/**
 * 冒险家基类
 * 所有特殊角色的基础类
 */
public abstract class Adventurer {
    // 冒险家基本数据
    protected int id;             // 冒险家ID
    protected int order;          // 玩家顺序
    protected int x;              // 当前位置x坐标
    protected int y;              // 当前位置y坐标
    protected int moveX;          // 临时移动目标x坐标
    protected int moveY;          // 临时移动目标y坐标
    protected int shoreUpX;       // 临时加固目标x坐标
    protected int shoreUpY;       // 临时加固目标y坐标
    protected String name;        // 冒险家名称
    protected String pawnImage;   // 棋子图像路径
    protected ArrayList<Integer> handCards;               // 手牌列表
    protected ArrayList<TreasureFigurines> capturedTreasures;  // 已收集的宝藏
    protected String pawnImagePath;
    /**
     * 初始化冒险家
     * @param order 玩家顺序
     * @param name 冒险家名称
     */
    public Adventurer(int order, String name) {
        this.order = order;
        this.name = name;
        this.pawnImagePath = "src/main/java/org/example/ForbiddenIsland/image/pawns/" + this.name.toLowerCase() + ".png";
        this.handCards = new ArrayList<>();
        this.capturedTreasures = new ArrayList<>();
    }

    public String getPawnImagePath() {
        return pawnImagePath;
    }
    public String getPawnImage() {
        if (name == null) {
            System.err.println("❌ name 为空，无法生成棋子图路径！");
            return null;
        }
        return "pawns/" + name.toLowerCase() + ".png";
    }    /**
     * 设置冒险家位置
     * @param x x坐标
     * @param y y坐标
     */
    public void setPos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * 设置临时移动目标
     * @param x 目标x坐标
     * @param y 目标y坐标
     */
    public void setMove(int x, int y) {
        this.moveX = x;
        this.moveY = y;
    }

    /**
     * 确认移动到临时目标
     */
    public void move() {
        this.x = this.moveX;
        this.y = this.moveY;
    }

    /**
     * 设置临时加固目标
     * @param x 目标x坐标
     * @param y 目标y坐标
     */
    public void setShoreUp(int x, int y) {
        this.shoreUpX = x;
        this.shoreUpY = y;
    }

    /**
     * A添加卡牌到手牌
     * @param cards 要添加的卡牌列表
     */
    public void setHandCards(ArrayList<Integer> cards) {
        this.handCards.addAll(cards);
    }

    /**
     * 添加收集的宝藏
     * @param treasure 收集的宝藏
     */
    public void addCapturedTreasure(TreasureFigurines treasure) {
        this.capturedTreasures.add(treasure);
    }

    // Getter方法
    public ArrayList<TreasureFigurines> getCapturedFigurines() {
        return capturedTreasures;
    }

    public ArrayList<Integer> getHandCards() {
        return handCards;
    }



    public int getOrder() {
        return order;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getShoreUpX() {
        return shoreUpX;
    }

    public int getShoreUpY() {
        return shoreUpY;
    }

    /**
     * 获取在棋盘上的位置索引
     * @return 位置索引
     */
    public int getPos() {
        return GameMap.getPositionIndex(new int[]{this.x, this.y});
    }

    /**
     * 获取加固目标在棋盘上的位置索引
     * @return 位置索引
     */
    public int getShoreUpPos() {
        return GameMap.getPositionIndex(new int[]{this.shoreUpX, this.shoreUpY});
    }
}