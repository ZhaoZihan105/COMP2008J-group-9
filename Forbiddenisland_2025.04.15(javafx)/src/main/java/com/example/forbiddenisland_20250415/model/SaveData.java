package com.example.forbiddenisland_20250415.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class SaveData implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int numPlayers;
    private int difficultyLevel;
    private int currentPlayerIndex;
    private int actionsRemaining;
    private GamePhase currentPhase;
    private boolean gameOver;
    private boolean gameWon;
    
    // 玩家数据
    private List<PlayerData> players;
    
    // 岛屿数据
    private List<TileData> island;
    
    // 卡牌数据
    private List<TreasureCardData> treasureDeck;
    private List<TreasureCardData> treasureDiscard;
    private List<FloodCardData> floodDeck;
    private List<FloodCardData> floodDiscard;
    
    // 水位数据
    private int waterLevel;
    
    // 已捕获的宝藏
    private Map<Treasure, Boolean> capturedTreasures;
    
    // 内部数据类
    public static class PlayerData implements Serializable {
        private static final long serialVersionUID = 1L;
        public Role role;
        public TileType currentTile;
        public boolean hasUsedSpecialAction;
        public List<TreasureCardData> hand;
    }
    
    public static class TileData implements Serializable {
        private static final long serialVersionUID = 1L;
        public TileType type;
        public TileState state;
        public int row;
        public int col;
    }
    
    public static class TreasureCardData implements Serializable {
        private static final long serialVersionUID = 1L;
        public CardType type;
        public String name;
    }
    
    public static class FloodCardData implements Serializable {
        private static final long serialVersionUID = 1L;
        public TileType tileType;
    }
    
    // Getters and Setters
    public int getNumPlayers() { return numPlayers; }
    public void setNumPlayers(int numPlayers) { this.numPlayers = numPlayers; }
    
    public int getDifficultyLevel() { return difficultyLevel; }
    public void setDifficultyLevel(int difficultyLevel) { this.difficultyLevel = difficultyLevel; }
    
    public int getCurrentPlayerIndex() { return currentPlayerIndex; }
    public void setCurrentPlayerIndex(int currentPlayerIndex) { this.currentPlayerIndex = currentPlayerIndex; }
    
    public int getActionsRemaining() { return actionsRemaining; }
    public void setActionsRemaining(int actionsRemaining) { this.actionsRemaining = actionsRemaining; }
    
    public GamePhase getCurrentPhase() { return currentPhase; }
    public void setCurrentPhase(GamePhase currentPhase) { this.currentPhase = currentPhase; }
    
    public boolean isGameOver() { return gameOver; }
    public void setGameOver(boolean gameOver) { this.gameOver = gameOver; }
    
    public boolean isGameWon() { return gameWon; }
    public void setGameWon(boolean gameWon) { this.gameWon = gameWon; }
    
    public List<PlayerData> getPlayers() { return players; }
    public void setPlayers(List<PlayerData> players) { this.players = players; }
    
    public List<TileData> getIsland() { return island; }
    public void setIsland(List<TileData> island) { this.island = island; }
    
    public List<TreasureCardData> getTreasureDeck() { return treasureDeck; }
    public void setTreasureDeck(List<TreasureCardData> treasureDeck) { this.treasureDeck = treasureDeck; }
    
    public List<TreasureCardData> getTreasureDiscard() { return treasureDiscard; }
    public void setTreasureDiscard(List<TreasureCardData> treasureDiscard) { this.treasureDiscard = treasureDiscard; }
    
    public List<FloodCardData> getFloodDeck() { return floodDeck; }
    public void setFloodDeck(List<FloodCardData> floodDeck) { this.floodDeck = floodDeck; }
    
    public List<FloodCardData> getFloodDiscard() { return floodDiscard; }
    public void setFloodDiscard(List<FloodCardData> floodDiscard) { this.floodDiscard = floodDiscard; }
    
    public int getWaterLevel() { return waterLevel; }
    public void setWaterLevel(int waterLevel) { this.waterLevel = waterLevel; }
    
    public Map<Treasure, Boolean> getCapturedTreasures() { return capturedTreasures; }
    public void setCapturedTreasures(Map<Treasure, Boolean> capturedTreasures) { this.capturedTreasures = capturedTreasures; }
} 