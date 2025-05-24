package org.example.ForbiddenIsland.service.game.data;

//package treasurehunter.service.game.data;

import org.example.ForbiddenIsland.service.game.GameData;

import java.util.ArrayList;
import java.util.Collections;

/**
 * 洪水卡牌组
 * 管理洪水卡牌的抽取和弃牌
 */
public class FloodDeck extends Deck {
    // 存储数据
    private final ArrayList<Integer> displayedCards;  // 展示的洪水卡
    private final ArrayList<Integer> removedFloodCards;  // 已移除的洪水卡（对应已沉没的瓦片）
    private boolean isInitPhase;  // 是否在初始阶段

    /**
     * 初始化洪水卡牌组
     */
    public FloodDeck() {
        super(6);  // 初始阶段抽6张卡
        displayedCards = new ArrayList<>();
        removedFloodCards = new ArrayList<>();
        isInitPhase = true;

        // 添加24张洪水卡（对应24个瓦片）
        for (int i = 1; i <= 24; i++) {
            deck.add(i);
        }
        Collections.shuffle(deck);
    }

    /**
     * 抽取指定数量的洪水卡
     * @return 抽取的洪水卡列表
     */
    @Override
    public ArrayList<Integer> getCards() {
        // 正常阶段根据水位确定抽牌数量
        if (!isInitPhase) {
            numCards = GameData.getFloodCardCount();
        }

        checkAvailability(numCards);
        displayedCards.clear();

        // 检查卡牌组剩余数量，如果不够就抽取所有剩余的
        if (deck.size() + discardPile.size() < numCards) {
            displayedCards.addAll(deck);
        } else {
            displayedCards.addAll(deck.subList(0, numCards));
        }

        return displayedCards;
    }

    /**
     * 弃牌
     */
    public void discard() {
        int count = 0;
        ArrayList<Integer> toDiscard = new ArrayList<>();

        // 收集要弃掉的卡牌
        for (int i = 0; i < deck.size() && count < numCards; i++) {
            toDiscard.add(deck.get(i));
            count++;
        }

        // 从牌堆移除这些卡牌
        deck.removeAll(toDiscard);
        // 将这些卡牌加入弃牌堆
        discardPile.addAll(toDiscard);
    }

    /**
     * 将弃牌堆洗牌后放到牌堆顶部（水位上升事件）
     */
    public void putBackToTop() {
        if (!discardPile.isEmpty()) {
            Collections.shuffle(discardPile);
            ArrayList<Integer> tempDeck = new ArrayList<>(deck);
            deck.clear();
            deck.addAll(discardPile);
            deck.addAll(tempDeck);
            discardPile.clear();
        }
    }

    /**
     * 移除已沉没瓦片对应的洪水卡
     * @param removedTile 已沉没的瓦片ID
     */
    public void removeFloodCard(int removedTile) {
        removedFloodCards.add(removedTile);
        deck.remove((Integer) removedTile);
        discardPile.remove((Integer) removedTile);
    }

    /**
     * 结束初始阶段
     */
    public void setToNormal() {
        this.isInitPhase = false;
    }

    /**
     * 获取已移除的洪水卡
     * @return 已移除的洪水卡列表
     */
    public ArrayList<Integer> getRemovedFloodCards() {
        return removedFloodCards;
    }
}
