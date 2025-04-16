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
        // 根据游戏说明创建岛屿网格：
        // 首先在中央创建一个4x4的方格
        // 然后在每边的中间2个瓦片旁边再各放置2个瓦片

        // 创建所有24个瓦片，随机排序
        List<TileType> tileTypes = new ArrayList<>(Arrays.asList(TileType.values()));
        Collections.shuffle(tileTypes);

        // 创建岛屿网格 - 这是一个简化的方法
        // 实际的网格需要精确匹配游戏的布局

        // 中央4x4网格
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                createTile(tileTypes.remove(0), row+1, col+1);
            }
        }

        // 额外的瓦片:
        // 上排
        createTile(tileTypes.remove(0), 0, 2);
        createTile(tileTypes.remove(0), 0, 3);

        // 下排
        createTile(tileTypes.remove(0), 5, 2);
        createTile(tileTypes.remove(0), 5, 3);

        // 左列
        createTile(tileTypes.remove(0), 2, 0);
        createTile(tileTypes.remove(0), 3, 0);

        // 右列
        createTile(tileTypes.remove(0), 2, 5);
        createTile(tileTypes.remove(0), 3, 5);

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