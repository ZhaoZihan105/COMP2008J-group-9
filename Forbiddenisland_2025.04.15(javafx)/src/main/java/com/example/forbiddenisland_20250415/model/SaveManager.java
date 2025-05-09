package com.example.forbiddenisland_20250415.model;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SaveManager {
    private static final String SAVE_DIR = "saves";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

    public static void saveGame(Game game) {
        try {
            // 创建存档目录
            File saveDir = new File(SAVE_DIR);
            if (!saveDir.exists()) {
                saveDir.mkdir();
            }

            // 创建存档文件名
            String fileName = String.format("save_%s.sav", LocalDateTime.now().format(DATE_FORMAT));
            File saveFile = new File(saveDir, fileName);

            // 创建存档数据
            SaveData saveData = new SaveData();
            
            // 保存基本游戏信息
            saveData.setNumPlayers(game.getPlayers().size());
            saveData.setDifficultyLevel(game.getWaterMeter().getLevel());
            saveData.setCurrentPlayerIndex(game.getCurrentPlayerIndex());
            saveData.setActionsRemaining(game.getActionsRemaining());
            saveData.setCurrentPhase(game.getCurrentPhase());
            saveData.setGameOver(game.isGameOver());
            saveData.setGameWon(game.isGameWon());
            saveData.setWaterLevel(game.getWaterMeter().getLevel());

            // 保存玩家数据
            List<SaveData.PlayerData> playersData = new ArrayList<>();
            for (Player player : game.getPlayers()) {
                SaveData.PlayerData playerData = new SaveData.PlayerData();
                playerData.role = player.getRole();
                playerData.currentTile = player.getCurrentTile().getType();
                playerData.hasUsedSpecialAction = player.hasUsedSpecialAction();
                
                // 保存玩家手牌
                List<SaveData.TreasureCardData> handData = new ArrayList<>();
                for (TreasureCard card : player.getHand()) {
                    SaveData.TreasureCardData cardData = new SaveData.TreasureCardData();
                    cardData.type = card.getType();
                    cardData.name = card.getName();
                    handData.add(cardData);
                }
                playerData.hand = handData;
                
                playersData.add(playerData);
            }
            saveData.setPlayers(playersData);

            // 保存岛屿数据
            List<SaveData.TileData> islandData = new ArrayList<>();
            for (Tile tile : game.getGameData().getIsland()) {
                SaveData.TileData tileData = new SaveData.TileData();
                tileData.type = tile.getType();
                tileData.state = tile.getState();
                tileData.row = tile.getRow();
                tileData.col = tile.getCol();
                islandData.add(tileData);
            }
            saveData.setIsland(islandData);

            // 保存宝藏卡牌数据
            List<SaveData.TreasureCardData> treasureDeckData = new ArrayList<>();
            for (TreasureCard card : game.getGameData().getTreasureDeck().getCards()) {
                SaveData.TreasureCardData cardData = new SaveData.TreasureCardData();
                cardData.type = card.getType();
                cardData.name = card.getName();
                treasureDeckData.add(cardData);
            }
            saveData.setTreasureDeck(treasureDeckData);

            // 保存洪水卡牌数据
            List<SaveData.FloodCardData> floodDeckData = new ArrayList<>();
            for (FloodCard card : game.getGameData().getFloodDeck().getCards()) {
                SaveData.FloodCardData cardData = new SaveData.FloodCardData();
                cardData.tileType = card.getTileType();
                floodDeckData.add(cardData);
            }
            saveData.setFloodDeck(floodDeckData);

            // 保存已捕获的宝藏
            Map<Treasure, Boolean> capturedTreasures = new HashMap<>();
            for (Treasure treasure : Treasure.values()) {
                capturedTreasures.put(treasure, game.getGameData().isTreasureCaptured(treasure));
            }
            saveData.setCapturedTreasures(capturedTreasures);

            // 序列化并保存数据
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(saveFile))) {
                oos.writeObject(saveData);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static SaveData loadGame(File saveFile) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(saveFile))) {
            return (SaveData) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<File> getSaveFiles() {
        File saveDir = new File(SAVE_DIR);
        if (!saveDir.exists()) {
            return new ArrayList<>();
        }

        List<File> saveFiles = new ArrayList<>();
        File[] files = saveDir.listFiles((dir, name) -> name.endsWith(".sav"));
        if (files != null) {
            for (File file : files) {
                saveFiles.add(file);
            }
        }
        return saveFiles;
    }

    public static void deleteSave(File saveFile) {
        if (saveFile.exists()) {
            saveFile.delete();
        }
    }
} 