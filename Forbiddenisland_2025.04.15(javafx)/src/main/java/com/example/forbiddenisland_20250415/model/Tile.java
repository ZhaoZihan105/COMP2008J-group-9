package com.example.forbiddenisland_20250415.model;

import java.util.*;

/**
 * 瓦片类
 */
public class Tile {
    private TileType type;
    private TileState state;
    private int row;
    private int col;
    private List<Tile> adjacentTiles;
    private List<Player> playersOnTile;

    public Tile(TileType type, int row, int col) {
        this.type = type;
        this.state = TileState.NORMAL;
        this.row = row;
        this.col = col;
        this.adjacentTiles = new ArrayList<>();
        this.playersOnTile = new ArrayList<>();
    }

    public void flood() {
        if (state == TileState.NORMAL) {
            state = TileState.FLOODED;
        }
    }

    public void shoreUp() {
        if (state == TileState.FLOODED) {
            state = TileState.NORMAL;
        }
    }

    public void sink() {
        if (state == TileState.FLOODED) {
            state = TileState.SUNK;
            // 当瓦片沉没时，所有玩家必须立即移动到相邻瓦片
            // 这个逻辑在Game类中处理
        }
    }

    public boolean isAdjacent(Tile other) {
        return adjacentTiles.contains(other);
    }

    public void addAdjacentTile(Tile tile) {
        if (!adjacentTiles.contains(tile)) {
            adjacentTiles.add(tile);
        }
    }

    public void addPlayer(Player player) {
        if (!playersOnTile.contains(player)) {
            playersOnTile.add(player);
        }
    }

    public void removePlayer(Player player) {
        playersOnTile.remove(player);
    }

    public void setState(TileState state) {
        this.state = state;
    }

    // Getters
    public TileType getType() {
        return type;
    }

    public String getName() {
        return type.getName();
    }

    public TileState getState() {
        return state;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public List<Tile> getAdjacentTiles() {
        return new ArrayList<>(adjacentTiles);
    }

    public List<Player> getPlayersOnTile() {
        return new ArrayList<>(playersOnTile);
    }

    @Override
    public String toString() {
        return type.getName() + " (" + state + ")";
    }
}