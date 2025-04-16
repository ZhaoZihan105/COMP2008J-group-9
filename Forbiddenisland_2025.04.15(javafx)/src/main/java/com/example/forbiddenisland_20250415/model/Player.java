package com.example.forbiddenisland_20250415.model;

import java.util.*;

/**
 * 玩家类
 */
public class Player {
    private Role role;
    private List<TreasureCard> hand;
    private Tile currentTile;
    private int id;
    private static int nextId = 0;
    private boolean hasUsedSpecialAction;

    public Player(Role role) {
        this.role = role;
        this.hand = new ArrayList<>();
        this.id = nextId++;
        this.hasUsedSpecialAction = false;
    }

    public boolean addCard(TreasureCard card) {
        if (hand.size() < 5) {
            hand.add(card);
            return true;
        }
        return false;
    }

    public boolean removeCard(TreasureCard card) {
        return hand.remove(card);
    }

    public boolean hasCard(CardType type) {
        for (TreasureCard card : hand) {
            if (card.getType() == type) {
                return true;
            }
        }
        return false;
    }

    public int countCards(CardType type) {
        int count = 0;
        for (TreasureCard card : hand) {
            if (card.getType() == type) {
                count++;
            }
        }
        return count;
    }

    public List<TreasureCard> getCardsOfType(CardType type) {
        List<TreasureCard> result = new ArrayList<>();
        for (TreasureCard card : hand) {
            if (card.getType() == type) {
                result.add(card);
            }
        }
        return result;
    }

    public boolean canCaptureTreasure(Treasure treasure) {
        CardType cardType = treasure.toCardType();
        return countCards(cardType) >= 4 && isOnTreasureTile(treasure);
    }

    private boolean isOnTreasureTile(Treasure treasure) {
        return currentTile != null &&
                currentTile.getType().getTreasure() == treasure;
    }

    public void resetSpecialAction() {
        hasUsedSpecialAction = false;
    }

    public boolean hasUsedSpecialAction() {
        return hasUsedSpecialAction;
    }

    public void useSpecialAction() {
        hasUsedSpecialAction = true;
    }

    // Getters and setters
    public Role getRole() {
        return role;
    }

    public List<TreasureCard> getHand() {
        return new ArrayList<>(hand); // 返回副本以防止直接修改
    }

    public Tile getCurrentTile() {
        return currentTile;
    }

    public void setCurrentTile(Tile currentTile) {
        this.currentTile = currentTile;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Player) {
            return ((Player) obj).id == this.id;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return id;
    }
}