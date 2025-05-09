package com.example.forbiddenisland_20250415.controller;

import com.example.forbiddenisland_20250415.model.*;
import com.example.forbiddenisland_20250415.view.*;
import javafx.application.Platform;
import java.util.*;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import java.io.File;
import javafx.scene.control.Alert;

/**
 * 游戏控制器类，处理UI与游戏模型之间的交互
 */
public class GameController {
    private Game game;
    private GameGUI gui;

    public GameController(Game game, GameGUI gui) {
        this.game = game;
        this.gui = gui;
    }

    /**
     * 开始新游戏
     */
    public void startGame(int numPlayers, int difficultyLevel) {
        game.initialize(numPlayers, difficultyLevel);
        
        // 添加存档和读档按钮
        Button saveButton = new Button("保存游戏");
        saveButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 8 20;");
        saveButton.setOnAction(e -> {
            SaveManager.saveGame(game);
            showMessage("游戏已保存");
        });
        
        Button loadButton = new Button("读取存档");
        loadButton.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 8 20;");
        loadButton.setOnAction(e -> {
            File saveFile = SaveDialog.show();
            if (saveFile != null) {
                SaveData saveData = SaveManager.loadGame(saveFile);
                if (saveData != null) {
                    loadGame(saveData);
                    showMessage("游戏已加载");
                }
            }
        });
        
        // 将按钮添加到界面
        HBox saveLoadBox = new HBox(10);
        saveLoadBox.setAlignment(Pos.CENTER);
        saveLoadBox.getChildren().addAll(saveButton, loadButton);
        gui.getControlPanel().addSaveLoadButtons(saveLoadBox);
        
        updateGUI();
    }

    /**
     * 更新GUI
     */
    public void updateGUI() {
        Platform.runLater(() -> {
            gui.updateGUI(
                    game.getGameData(),
                    game.getPlayers(),
                    game.getCurrentPlayer(),
                    game.getWaterMeter(),
                    game.getCurrentPhase()
            );
        });
    }

    /**
     * 处理瓦片选择
     */
    public void onTileSelected(Tile tile) {
        game.setSelectedTile(tile);
        gui.getLogPanel().addLog("选择了瓦片: " + tile.getName());
    }

    /**
     * 处理玩家选择
     */
    public void onPlayerSelected(Player player) {
        game.setSelectedPlayer(player);
        gui.getLogPanel().addLog("选择了玩家: " + player.getRole().getTitle());
    }

    /**
     * 处理卡片选择
     */
    public void onCardSelected(TreasureCard card) {
        game.setSelectedCard(card);
        gui.getLogPanel().addLog("选择了卡片: " + card.getName());
    }

    /**
     * 处理移动行动
     */
    public void onMoveAction() {
        Tile selectedTile = game.getSelectedTile();

        if (selectedTile == null) {
            gui.showError("请先选择一个目标瓦片");
            return;
        }

        Player currentPlayer = game.getCurrentPlayer();
        boolean success = game.movePlayer(currentPlayer, selectedTile);

        if (success) {
            gui.getLogPanel().addLog(currentPlayer.getRole().getTitle() + " 移动到 " + selectedTile.getName());
            updateGUI();
        } else {
            gui.showError("无法移动到选择的瓦片");
        }
    }

    /**
     * 处理加固行动
     */
    public void onShoreUpAction() {
        Tile selectedTile = game.getSelectedTile();

        if (selectedTile == null) {
            gui.showError("请先选择一个要加固的瓦片");
            return;
        }

        boolean success = game.shoreUp(selectedTile);

        if (success) {
            gui.getLogPanel().addLog(game.getCurrentPlayer().getRole().getTitle() + " 加固了 " + selectedTile.getName());
            updateGUI();
        } else {
            gui.showError("无法加固选择的瓦片");
        }
    }

    /**
     * 处理给卡牌行动
     */
    public void onGiveCardAction() {
        TreasureCard selectedCard = game.getSelectedCard();
        Player selectedPlayer = game.getSelectedPlayer();
        Player currentPlayer = game.getCurrentPlayer();

        if (selectedCard == null) {
            gui.showError("请先选择一张要给出的卡牌");
            return;
        }

        if (selectedPlayer == null) {
            gui.showError("请选择一个接收卡牌的玩家");
            return;
        }

        if (selectedPlayer == currentPlayer) {
            gui.showError("不能给自己卡牌");
            return;
        }

        boolean success = game.giveCard(currentPlayer, selectedPlayer, selectedCard);

        if (success) {
            gui.getLogPanel().addLog(currentPlayer.getRole().getTitle() + " 给了 " +
                    selectedPlayer.getRole().getTitle() + " 一张 " + selectedCard.getName() + " 卡");
            updateGUI();
        } else {
            gui.showError("无法给出选择的卡牌");
        }
    }

    /**
     * 处理捕获宝藏行动
     */
    public void onCaptureTreasureAction() {
        Player currentPlayer = game.getCurrentPlayer();
        Tile currentTile = currentPlayer.getCurrentTile();
        Treasure tileTreasure = currentTile.getType().getTreasure();

        if (tileTreasure == null) {
            gui.showError("当前瓦片没有可捕获的宝藏");
            return;
        }

        if (game.getGameData().isTreasureCaptured(tileTreasure)) {
            gui.showError("这个宝藏已经被捕获了");
            return;
        }

        boolean success = game.captureTreasure(currentPlayer, tileTreasure);

        if (success) {
            gui.getLogPanel().addLog(currentPlayer.getRole().getTitle() + " 捕获了 " + tileTreasure.getName());
            updateGUI();

            if (game.getGameData().areAllTreasuresCaptured()) {
                gui.showMessage("所有宝藏已捕获", "恭喜！所有宝藏都已被捕获。现在所有玩家必须前往愚者着陆点并使用直升机卡逃离岛屿！");
            }
        } else {
            gui.showError("无法捕获宝藏。你可能没有足够的卡牌或者不在正确的瓦片上。");
        }
    }

    /**
     * 处理下一阶段或下一回合
     */
    public void onNextAction() {
        switch (game.getCurrentPhase()) {
            case PLAYER_ACTIONS:
                if (game.getActionsRemaining() > 0) {
                    boolean confirm = gui.confirmDialog("结束行动阶段",
                            "你还有 " + game.getActionsRemaining() + " 个行动点数未使用。确定要进入下一阶段吗？");
                    if (!confirm) {
                        return;
                    }
                }
                gui.getLogPanel().addLog(game.getCurrentPlayer().getRole().getTitle() + " 结束行动阶段");
                game.nextPhase();
                break;

            case DRAW_TREASURE_CARDS:
                gui.getLogPanel().addLog(game.getCurrentPlayer().getRole().getTitle() + " 抽取宝藏卡");
                game.nextPhase();
                break;

            case DRAW_FLOOD_CARDS:
                gui.getLogPanel().addLog(game.getCurrentPlayer().getRole().getTitle() + " 抽取洪水卡");
                game.nextPhase();
                break;

            case SPECIAL_ACTION:
                game.nextPhase();
                break;

            case GAME_OVER:
                if (game.isGameWon()) {
                    gui.showMessage("游戏胜利", "恭喜！你们成功捕获了所有宝藏并逃离了禁忌岛！");
                } else {
                    gui.showMessage("游戏失败", "很遗憾，你们未能成功逃离禁忌岛。");
                }
                gui.getConfigPanel().reset();
                break;
        }

        updateGUI();

        // 检查游戏是否结束
        if (game.isGameOver()) {
            if (game.isGameWon()) {
                gui.showMessage("游戏胜利", "恭喜！你们成功捕获了所有宝藏并逃离了禁忌岛！");
            } else {
                gui.showMessage("游戏失败", "很遗憾，你们未能成功逃离禁忌岛。");
            }
            gui.getConfigPanel().reset();
        }
    }

    /**
     * 处理特殊行动
     */
    public void onSpecialAction() {
        TreasureCard selectedCard = game.getSelectedCard();

        if (selectedCard == null) {
            gui.showError("请先选择一张特殊行动卡");
            return;
        }

        Player currentPlayer = game.getCurrentPlayer();

        if (selectedCard.getType() == CardType.HELICOPTER) {
            Tile selectedTile = game.getSelectedTile();
            List<Player> selectedPlayers = game.getSelectedPlayers();

            if (selectedTile == null) {
                gui.showError("请选择一个目标瓦片");
                return;
            }

            if (selectedPlayers.isEmpty()) {
                gui.showError("请选择至少一个要移动的玩家");
                return;
            }

            boolean success = game.useHelicopterLift(currentPlayer, selectedCard, selectedTile, selectedPlayers);

            if (success) {
                StringBuilder playersStr = new StringBuilder();
                for (Player p : selectedPlayers) {
                    playersStr.append(p.getRole().getTitle()).append(", ");
                }
                gui.getLogPanel().addLog(currentPlayer.getRole().getTitle() + " 使用直升机卡移动 " +
                        playersStr.toString() + " 到 " + selectedTile.getName());
                updateGUI();
            } else {
                gui.showError("无法使用直升机卡");
            }
        } else if (selectedCard.getType() == CardType.SANDBAG) {
            Tile selectedTile = game.getSelectedTile();

            if (selectedTile == null) {
                gui.showError("请选择一个要加固的瓦片");
                return;
            }

            boolean success = game.useSandbag(currentPlayer, selectedCard, selectedTile);

            if (success) {
                gui.getLogPanel().addLog(currentPlayer.getRole().getTitle() + " 使用沙袋卡加固 " + selectedTile.getName());
                updateGUI();
            } else {
                gui.showError("无法使用沙袋卡");
            }
        } else {
            gui.showError("请选择一张特殊行动卡（直升机或沙袋）");
        }
    }

    /**
     * 处理起飞行动
     */
    public void onLiftOffAction() {
        boolean success = game.liftOff();

        if (success) {
            gui.getLogPanel().addLog("所有玩家成功逃离禁忌岛！");
            gui.showMessage("游戏胜利", "恭喜！你们成功捕获了所有宝藏并逃离了禁忌岛！");
            gui.getConfigPanel().reset();
        } else {
            gui.showError("无法起飞。确保所有宝藏都已捕获，所有玩家都在愚者着陆点，并且有人持有直升机卡。");
        }

        updateGUI();
    }

    /**
     * 清除所有选择
     */
    public void onClearSelections() {
        game.clearSelections();
        gui.getIslandPanel().clearSelection();
        gui.getPlayerPanel().clearSelection();
        gui.getLogPanel().addLog("清除了所有选择");
    }

    private void initializeGame() {
        // 添加存档按钮
        Button saveButton = new Button("保存游戏");
        saveButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 8 20;");
        saveButton.setOnAction(e -> {
            SaveManager.saveGame(game);
            showMessage("游戏已保存");
        });
        
        // 添加读档按钮
        Button loadButton = new Button("读取存档");
        loadButton.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 8 20;");
        loadButton.setOnAction(e -> {
            File saveFile = SaveDialog.show();
            if (saveFile != null) {
                SaveData saveData = SaveManager.loadGame(saveFile);
                if (saveData != null) {
                    loadGame(saveData);
                    showMessage("游戏已加载");
                }
            }
        });
        
        // 将按钮添加到界面
        HBox saveLoadBox = new HBox(10);
        saveLoadBox.setAlignment(Pos.CENTER);
        saveLoadBox.getChildren().addAll(saveButton, loadButton);
    }
    
    private void loadGame(SaveData saveData) {
        // 重新初始化游戏
        game.initialize(saveData.getNumPlayers(), saveData.getDifficultyLevel());
        
        // 恢复游戏状态
        game.setCurrentPlayerIndex(saveData.getCurrentPlayerIndex());
        game.setActionsRemaining(saveData.getActionsRemaining());
        game.setCurrentPhase(saveData.getCurrentPhase());
        game.setGameOver(saveData.isGameOver());
        game.setGameWon(saveData.isGameWon());
        game.getWaterMeter().setLevel(saveData.getWaterLevel());
        
        // 恢复玩家数据
        for (int i = 0; i < saveData.getPlayers().size(); i++) {
            SaveData.PlayerData playerData = saveData.getPlayers().get(i);
            Player player = game.getPlayers().get(i);
            
            // 恢复玩家位置
            for (Tile tile : game.getGameData().getIsland()) {
                if (tile.getType() == playerData.currentTile) {
                    player.setCurrentTile(tile);
                    break;
                }
            }
            
            // 恢复玩家手牌
            player.getHand().clear();
            for (SaveData.TreasureCardData cardData : playerData.hand) {
                TreasureCard card = new TreasureCard(cardData.type, cardData.name);
                player.getHand().add(card);
            }
            
            // 恢复特殊能力使用状态
            player.setHasUsedSpecialAction(playerData.hasUsedSpecialAction);
        }
        
        // 恢复岛屿数据
        for (int i = 0; i < saveData.getIsland().size(); i++) {
            SaveData.TileData tileData = saveData.getIsland().get(i);
            Tile tile = game.getGameData().getIsland().get(i);
            tile.setState(tileData.state);
        }
        
        // 恢复宝藏卡牌数据
        game.getGameData().getTreasureDeck().getCards().clear();
        for (SaveData.TreasureCardData cardData : saveData.getTreasureDeck()) {
            TreasureCard card = new TreasureCard(cardData.type, cardData.name);
            game.getGameData().getTreasureDeck().getCards().add(card);
        }
        
        // 恢复洪水卡牌数据
        game.getGameData().getFloodDeck().getCards().clear();
        for (SaveData.FloodCardData cardData : saveData.getFloodDeck()) {
            FloodCard card = new FloodCard(cardData.tileType);
            game.getGameData().getFloodDeck().getCards().add(card);
        }
        
        // 恢复已捕获的宝藏
        for (Map.Entry<Treasure, Boolean> entry : saveData.getCapturedTreasures().entrySet()) {
            if (entry.getValue()) {
                game.getGameData().captureTreasure(entry.getKey());
            }
        }
        
        // 更新界面
        updateGUI();
        gui.getLogPanel().addLog("游戏已加载");
        gui.getLogPanel().addLog(game.getCurrentPlayer().getRole().getTitle() + "的回合开始");
    }

    private void showMessage(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("提示");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
}