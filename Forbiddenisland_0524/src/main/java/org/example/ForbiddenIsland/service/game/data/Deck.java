package org.example.ForbiddenIsland.service.game.data;


//package org.example.ForbiddenIsland.service.game.data;

import java.util.ArrayList;
import java.util.Collections;

/**
 * 卡牌组基类
 * 管理卡牌的抽取和弃牌堆
 */
public abstract class Deck {
    // 卡牌数据
    protected ArrayList<Integer> deck;         // 抽牌堆
    protected ArrayList<Integer> discardPile;  // 弃牌堆
    protected int numCards;                     // 每次抽取的卡牌数量

    /**
     * 初始化卡牌组
     * @param numCards 每次抽取的卡牌数量
     */
    public Deck(int numCards) {
        deck = new ArrayList<>();
        discardPile = new ArrayList<>();
        this.numCards = numCards;
    }

    /**
     * 抽取一定数量的卡牌
     * @return 抽取的卡牌列表
     */
    protected abstract ArrayList<Integer> getCards();

    /**
     * 检查卡牌组是否有足够的卡牌，如果没有，将弃牌堆洗牌后放入抽牌堆
     * @param n 需要的卡牌数量
     */
    protected void checkAvailability(int n) {
        if (deck.size() < n) {
            Collections.shuffle(discardPile);
            deck.addAll(discardPile);
            discardPile.clear();
        }
    }
}