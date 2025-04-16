package com.example.forbiddenisland_20250415.model;

/**
 * 洪水卡类
 */
public class FloodCard {
    private TileType tileType;
    private String name;
    private int id;
    private static int nextId = 0;

    public FloodCard(TileType tileType) {
        this.tileType = tileType;
        this.name = tileType.getName();
        this.id = nextId++;
    }

    public TileType getTileType() {
        return tileType;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof FloodCard) {
            return ((FloodCard) obj).id == this.id;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return name;
    }
}