package com.example.forbiddenisland_20250415.model;

import java.util.*;

/**
 * 游戏数据类，存储所有游戏状态
 */
public class GameData {
    private List<Tile> island;
    private Map<TileType, Tile> tileMap;
    private Deck<TreasureCard> treasureDeck;
    private Deck<FloodCard> floodDeck;
    private Map<Treasure, Boolean> capturedTreasures;

    public GameData() {
        island = new ArrayList<>();
        tileMap = new HashMap<>();
        treasureDeck = new Deck<>();
        floodDeck = new Deck<>();
        capturedTreasures = new HashMap<>();

        // 初始化宝藏为未捕获状态
        for (Treasure treasure : Treasure.values()) {
            capturedTreasures.put(treasure, false);
        }
    }

    public void createIsland() {
        // 创建所有24个瓦片，使用固定的布局
        // 中央4x4网格
        createTile(TileType.FOOLS_LANDING, 2, 2);
        createTile(TileType.BRONZE_GATE, 1, 1);
        createTile(TileType.CORAL_PALACE, 1, 2);
        createTile(TileType.CAVE_OF_EMBERS, 1, 3);
        createTile(TileType.CAVE_OF_SHADOWS, 1, 4);
        createTile(TileType.COPPER_GATE, 2, 1);
        createTile(TileType.CRIMSON_FOREST, 2, 3);
        createTile(TileType.DUNES_OF_DECEPTION, 2, 4);
        createTile(TileType.GOLD_GATE, 3, 1);
        createTile(TileType.HOWLING_GARDEN, 3, 2);
        createTile(TileType.IRON_GATE, 3, 3);
        createTile(TileType.LOST_LAGOON, 3, 4);
        createTile(TileType.MISTY_MARSH, 4, 1);
        createTile(TileType.OBSERVATORY, 4, 2);
        createTile(TileType.PHANTOM_ROCK, 4, 3);
        createTile(TileType.SILVER_GATE, 4, 4);

        // 额外的瓦片
        createTile(TileType.TEMPLE_OF_THE_MOON, 0, 2);
        createTile(TileType.TEMPLE_OF_THE_SUN, 0, 3);
        createTile(TileType.WATCHTOWER, 5, 2);
        createTile(TileType.WHISPERING_GARDEN, 5, 3);
        createTile(TileType.BREAKERS_BRIDGE, 2, 0);
        createTile(TileType.CLIFFS_OF_ABANDON, 3, 0);
        createTile(TileType.TIDAL_PALACE, 2, 5);
        createTile(TileType.TWILIGHT_HOLLOW, 3, 5);

        // 设置相邻关系
        setupAdjacency();
    }

    private void createTile(TileType type, int row, int col) {
        Tile tile = new Tile(type, row, col);
        island.add(tile);
        tileMap.put(type, tile);
    }

    private void setupAdjacency() {
        // 连接相邻瓦片（不包括对角线）
        for (Tile tile1 : island) {
            for (Tile tile2 : island) {
                if (tile1 != tile2) {
                    // 检查瓦片是否相邻（曼哈顿距离 = 1）
                    if (Math.abs(tile1.getRow() - tile2.getRow()) +
                            Math.abs(tile1.getCol() - tile2.getCol()) == 1) {
                        tile1.addAdjacentTile(tile2);
                    }
                }
            }
        }
    }

    public void createTreasureDeck() {
        // 创建宝藏卡
        for (Treasure treasure : Treasure.values()) {
            CardType cardType = treasure.toCardType();
            for (int i = 0; i < 5; i++) {
                treasureDeck.addCard(new TreasureCard(cardType, treasure.getName()));
            }
        }

        // 添加特殊卡
        for (int i = 0; i < 3; i++) {
            treasureDeck.addCard(new TreasureCard(CardType.WATERS_RISE, "Waters Rise!"));
        }

        for (int i = 0; i < 3; i++) {
            treasureDeck.addCard(new TreasureCard(CardType.HELICOPTER, "Helicopter Lift"));
        }

        for (int i = 0; i < 2; i++) {
            treasureDeck.addCard(new TreasureCard(CardType.SANDBAG, "Sandbag"));
        }

        // 洗牌
        treasureDeck.shuffle();
    }

    public void createFloodDeck() {
        // 为每个瓦片创建洪水卡
        for (Tile tile : island) {
            floodDeck.addCard(new FloodCard(tile.getType()));
        }

        // 洗牌
        floodDeck.shuffle();
    }

    public Tile getTile(TileType type) {
        return tileMap.get(type);
    }

    public Tile getStartingTile(Role role) {
        TileType tileType = TileType.getStartingTile(role);
        return tileMap.get(tileType);
    }

    public void floodTile(TileType tileType) {
        Tile tile = tileMap.get(tileType);
        if (tile != null && tile.getState() == TileState.NORMAL) {
            tile.flood();
        }
    }

    public void sinkTile(TileType tileType) {
        Tile tile = tileMap.get(tileType);
        if (tile != null && tile.getState() == TileState.FLOODED) {
            tile.sink();
            // 从岛上移除瓦片 - 但在tileMap中保留引用
            island.remove(tile);
        }
    }

    public void captureTreasure(Treasure treasure) {
        capturedTreasures.put(treasure, true);
        treasure.setCaptured(true);
    }

    public boolean isTreasureCaptured(Treasure treasure) {
        return capturedTreasures.get(treasure);
    }

    public boolean areAllTreasuresCaptured() {
        for (Boolean captured : capturedTreasures.values()) {
            if (!captured) {
                return false;
            }
        }
        return true;
    }

    public TreasureCard drawTreasureCard() {
        return treasureDeck.draw();
    }

    public void discardTreasureCard(TreasureCard card) {
        treasureDeck.discard(card);
    }

    public FloodCard drawFloodCard() {
        return floodDeck.draw();
    }

    public void discardFloodCard(FloodCard card) {
        floodDeck.discard(card);
    }

    public void reshuffleFloodDiscard() {
        floodDeck.reshuffleDiscardPile();
    }

    // Getters
    public List<Tile> getIsland() {
        return new ArrayList<>(island);
    }

    public Deck<TreasureCard> getTreasureDeck() {
        return treasureDeck;
    }

    public Deck<FloodCard> getFloodDeck() {
        return floodDeck;
    }

    public int getTreasureDeckSize() {
        return treasureDeck.size();
    }

    public int getFloodDeckSize() {
        return floodDeck.size();
    }

    public boolean isFoolsLandingSunk() {
        Tile foolsLanding = getTile(TileType.FOOLS_LANDING);
        return foolsLanding == null || foolsLanding.getState() == TileState.SUNK;
    }

    public boolean areTreasureTilesSunk(Treasure treasure) {
        // 检查一个宝藏的两个瓦片是否都已经沉没
        boolean allSunk = true;

        for (TileType tileType : TileType.values()) {
            if (tileType.getTreasure() == treasure) {
                Tile tile = getTile(tileType);
                if (tile != null && tile.getState() != TileState.SUNK) {
                    allSunk = false;
                    break;
                }
            }
        }

        return allSunk;
    }
}