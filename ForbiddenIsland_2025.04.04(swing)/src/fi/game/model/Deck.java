package fi.game.model;

import java.util.*;

/**
 * 卡组类，用于管理宝藏卡和洪水卡
 */
public class Deck<T> {
    private List<T> cards;
    private List<T> discardPile;

    public Deck() {
        cards = new ArrayList<>();
        discardPile = new ArrayList<>();
    }

    public void addCard(T card) {
        cards.add(card);
    }

    public T draw() {
        if (cards.isEmpty()) {
            reshuffleDiscardPile();
            if (cards.isEmpty()) {
                return null;
            }
        }
        return cards.remove(0);
    }

    public void discard(T card) {
        discardPile.add(card);
    }

    public void reshuffleDiscardPile() {
        cards.addAll(discardPile);
        discardPile.clear();
        shuffle();
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public int size() {
        return cards.size();
    }

    public int discardSize() {
        return discardPile.size();
    }

    public List<T> getCards() {
        return new ArrayList<>(cards);
    }

    public List<T> getDiscardPile() {
        return new ArrayList<>(discardPile);
    }
}