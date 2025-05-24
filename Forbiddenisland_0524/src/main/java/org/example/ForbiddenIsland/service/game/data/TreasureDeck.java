package org.example.ForbiddenIsland.service.game.data;


//package treasurehunter.service.game.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/**
 * 宝藏卡牌组
 * 管理宝藏卡牌的抽取和弃牌
 */
public class TreasureDeck extends Deck {
    // 存储抽取的卡牌
    private final ArrayList<Integer> drawnCards;

    /**
     * 初始化宝藏卡牌组
     */
    public TreasureDeck() {
        super(2); // 每次抽取2张宝藏卡
        drawnCards = new ArrayList<>();

        // 添加28张宝藏卡 (0-27)
        // 0-19: 四种宝藏卡，每种5张
        // 20-22: 直升机卡
        // 23-24: 沙袋卡
        // 25-27: 水位上升卡
        for (int i = 0; i < 28; i++) {
            deck.add(i);
        }
        Collections.shuffle(deck);
    }

    /**
     * 抽取指定数量的卡牌
     * @return 抽取的卡牌列表
     */
    @Override
    public ArrayList<Integer> getCards() {
        checkAvailability(numCards);
        drawnCards.clear();

        // 从牌堆顶部抽取指定数量的卡
        drawnCards.addAll(deck.subList(0, numCards));
        deck.subList(0, numCards).clear();

        return drawnCards;
    }

    /**
     * 初始发牌时抽取卡牌，避免抽到水位上升卡
     * @return 抽取的卡牌列表
     */
    public ArrayList<Integer> getInitialCards() {
        checkAvailability(numCards);
        drawnCards.clear();

        int count = 0;
        Iterator<Integer> iterator = deck.iterator();

        while (iterator.hasNext() && count < numCards) {
            int treasureCard = iterator.next();

            // 如果是水位上升卡，放入弃牌堆
            if (treasureCard >= 25 && treasureCard <= 27) {
                discard(treasureCard);
                iterator.remove();
            } else {
                // 否则添加到抽取的卡牌中
                drawnCards.add(treasureCard);
                iterator.remove();
                count++;
            }
        }

        // 将弃牌堆洗牌后放回牌堆底部
        if (!discardPile.isEmpty()) {
            deck.addAll(discardPile);
            Collections.shuffle(deck);
            discardPile.clear();
        }

        return drawnCards;
    }

    /**
     * 弃牌
     * @param treasureID 要弃掉的卡牌ID
     */
    public void discard(int treasureID) {
        discardPile.add(treasureID);
    }
}