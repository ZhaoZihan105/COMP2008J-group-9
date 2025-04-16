package com.example.forbiddenisland_20250415.model;
/**
 * 宝藏类型枚举
 */
public enum Treasure {
    THE_EARTH_STONE("The Earth Stone"),
    THE_STATUE_OF_THE_WIND("The Statue of the Wind"),
    THE_CRYSTAL_OF_FIRE("The Crystal of Fire"),
    THE_OCEANS_CHALICE("The Ocean's Chalice");

    private String name;
    private boolean captured;

    Treasure(String name) {
        this.name = name;
        this.captured = false;
    }

    public String getName() {
        return name;
    }

    public CardType toCardType() {
        switch (this) {
            case THE_EARTH_STONE: return CardType.EARTH;
            case THE_STATUE_OF_THE_WIND: return CardType.WIND;
            case THE_CRYSTAL_OF_FIRE: return CardType.FIRE;
            case THE_OCEANS_CHALICE: return CardType.WATER;
            default: return null;
        }
    }

    public void setCaptured(boolean captured) {
        this.captured = captured;
    }

    public boolean isCaptured() {
        return captured;
    }
}