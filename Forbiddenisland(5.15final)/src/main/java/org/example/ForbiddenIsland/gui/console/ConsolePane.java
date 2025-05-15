package org.example.ForbiddenIsland.gui.console;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.example.ForbiddenIsland.gui.updater.LogManager;
import org.example.ForbiddenIsland.gui.updater.UpdateManager;
import org.example.ForbiddenIsland.service.game.Game;
import org.example.ForbiddenIsland.service.game.GameData;
import org.example.ForbiddenIsland.service.game.component.adventurer.*;
import org.example.ForbiddenIsland.service.game.component.cards.*;
import org.example.ForbiddenIsland.service.game.data.*;
import org.example.ForbiddenIsland.utils.Constants;
import org.example.ForbiddenIsland.utils.GameMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 * 控制台面板
 * 包含游戏操作按钮、配置选项和日志显示
 */
public class ConsolePane extends VBox {
    // 控制按钮列表
    private final ArrayList<Button> controlButtons = new ArrayList<>();

    // 玩家数量下拉框
    private final ComboBox<String> numOfPlayerComboBox;

    // 难度级别下拉框
    private final ComboBox<String> difficultyComboBox;

    // 日志文本区域
    private final TextArea logTextArea;

    /**
     * 初始化控制台面板
     */
    public ConsolePane() {
        // 设置样式和大小
        setSpacing(5);
        setPadding(new Insets(5));
        setPrefWidth(Constants.CONSOLE_WIDTH);
        setAlignment(Pos.TOP_CENTER);

        // 添加标题区域
        addTitleSection();

        // 添加配置区域
        numOfPlayerComboBox = new ComboBox<>();
        difficultyComboBox = new ComboBox<>();
        addConfigSection();

        // 添加日志区域
        logTextArea = new TextArea();
        logTextArea.setEditable(false);
        logTextArea.setWrapText(true);
        addLogSection();

        // 添加操作按钮区域
        addActionSection();

        // 设置日志管理器引用
        LogManager.setLogTextArea(logTextArea);
    }

    /**
     * 添加标题区域
     */
    private void addTitleSection() {
        VBox titleBox = new VBox(5);
        titleBox.setPadding(new Insets(5));
        titleBox.setAlignment(Pos.CENTER);

        // 游戏标题
        Label titleLabel = new Label("宝藏猎人岛");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 18));

        // 开发者信息
        Label developerLabel = new Label("开发者: 宝藏猎人团队");
        developerLabel.setFont(Font.font("System", FontWeight.NORMAL, 12));

        titleBox.getChildren().addAll(titleLabel, developerLabel);
        getChildren().add(titleBox);
    }

    /**
     * 添加配置区域
     */
    private void addConfigSection() {
        VBox configBox = new VBox(5);
        configBox.setPadding(new Insets(5));
        configBox.setAlignment(Pos.CENTER);

        // 玩家数量选择
        numOfPlayerComboBox.setPromptText("选择玩家数量");
        numOfPlayerComboBox.setItems(FXCollections.observableArrayList(
                "2人", "3人", "4人"
        ));

        // 难度级别选择
        difficultyComboBox.setPromptText("选择难度级别");
        difficultyComboBox.setItems(FXCollections.observableArrayList(
                "简单 (1)", "普通 (2)", "困难 (3)", "专家 (4)", "传奇 (5)"
        ));

        // 开始游戏按钮
        Button startButton = new Button("开始游戏");
        startButton.setMaxWidth(Double.MAX_VALUE);
        startButton.setOnAction(e -> {
            // 检查是否已选择玩家数量和难度级别
            if (numOfPlayerComboBox.getSelectionModel().getSelectedIndex() != -1 &&
                    difficultyComboBox.getSelectionModel().getSelectedIndex() != -1) {

                LogManager.logMessage("[游戏初始化]");


                // 禁用开始按钮，避免重复点击
                startButton.setDisable(true);

                // 获取玩家数量和难度级别
                int numOfPlayers = numOfPlayerComboBox.getSelectionModel().getSelectedIndex() + 2;
                int difficultyLevel = difficultyComboBox.getSelectionModel().getSelectedIndex() + 1;

                // 创建游戏
                new Game(numOfPlayers, difficultyLevel);

                // 启用操作按钮
                for (int i = 1; i < controlButtons.size(); i++) {
                    controlButtons.get(i).setDisable(false);
                }
            }
        });

        controlButtons.add(startButton);

        configBox.getChildren().addAll(numOfPlayerComboBox, difficultyComboBox, startButton);
        getChildren().add(configBox);
    }

    /**
     * 添加日志区域
     */
    private void addLogSection() {
        VBox logBox = new VBox(5);
        logBox.setPadding(new Insets(5));

        // 日志标题
        Label logLabel = new Label("游戏日志");
        logLabel.setFont(Font.font("System", FontWeight.BOLD, 14));

        // 设置日志文本区域
        logTextArea.setPrefHeight(200); // 从300增加到400

        // 可选：设置最小高度，确保日志区域始终有足够的显示空间
        logTextArea.setMinHeight(200);

        logBox.getChildren().addAll(logLabel, logTextArea);
        getChildren().add(logBox);
    }

    /**
     * 添加操作按钮区域
     */
    private void addActionSection() {
        VBox actionBox = new VBox(5);
        actionBox.setPadding(new Insets(5));

        // 操作标题
        Label actionLabel = new Label("操作");
        actionLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        actionBox.getChildren().add(actionLabel);

        // 创建操作按钮
        String[] buttonLabels = {
                "移动到", "加固", "传递给", "捕获宝藏", "起飞",
                "特殊行动", "下一阶段", "丢弃", "清除选择"
        };

        for (String label : buttonLabels) {
            Button actionButton = new Button(label);
            actionButton.setMaxWidth(Double.MAX_VALUE);
            actionButton.setDisable(true); // 初始禁用，直到游戏开始

            controlButtons.add(actionButton);
            actionBox.getChildren().add(actionButton);
        }

        // 设置按钮事件处理
        setupButtonHandlers();

        getChildren().add(actionBox);
    }

    /**
     * 设置操作按钮的事件处理
     */
    private void setupButtonHandlers() {
        // 移动到
        controlButtons.get(1).setOnAction(e -> {
            if (Game.getActionCount() < 3) {
                if (GameData.getBoard().isCanMove()) {
                    GameData.moveTo();
                    LogManager.logMessage("移动到 " +
                            Arrays.toString(GameMap.getCoordinates(
                                    GameData.getAdventurers()[Game.getRoundNum()].getPos())));



                    // 更新界面
                    UpdateManager.getBoardUpdater().updateUI();
                    Game.doAction();

                    if (Game.isNeedToSave()) {
                        Game.rescuePlayersRound();
                    }
                } else {
                    LogManager.logMessage("无法移动到此瓦片");
                }
            } else {
                LogManager.logMessage("已超过最大行动次数");
            }
        });

        // 加固
        controlButtons.get(2).setOnAction(e -> {
            if (Game.getActionCount() < 3) {
                if (GameData.getBoard().isCanShoreUp()) {
                    GameData.shoreUp();
                    LogManager.logMessage("加固 " +
                            Arrays.toString(GameMap.getCoordinates(
                                    GameData.getAdventurers()[Game.getRoundNum()].getShoreUpPos())));

                    // 更新界面
                    UpdateManager.getBoardUpdater().updateUI();



                    Game.doAction();

                    // 工程师特殊能力：可以一次行动加固两个瓦片
                    if (GameData.getAdventurers()[Game.getRoundNum()] instanceof Engineer) {
                        Engineer engineer = (Engineer) GameData.getAdventurers()[Game.getRoundNum()];
                        if (engineer.getShoreUpCount() > 0) {
                            engineer.useShoreUp();
                            Game.moreAction();


                        }
                    }
                } else {
                    LogManager.logMessage("无法加固此瓦片");
                }
            } else {
                LogManager.logMessage("已超过最大行动次数");
            }
        });

        // 传递给
        controlButtons.get(3).setOnAction(e -> {
            // 确保选中的玩家索引有效
            if (Game.getActionCount() < 3 &&
                    GameData.getSelectedPawn() != -1 &&
                    GameData.getSelectedPawn() >= 0 &&
                    GameData.getSelectedPawn() < GameData.getAdventurers().length) {

                if (GameData.getBoard().getTile(
                                GameData.getAdventurers()[Game.getRoundNum()].getX(),
                                GameData.getAdventurers()[Game.getRoundNum()].getY())
                        .canPassTo(GameData.getAdventurers()[Game.getRoundNum()],
                                GameData.getAdventurers()[GameData.getSelectedPawn()]) ||
                        GameData.getAdventurers()[Game.getRoundNum()].getName().equals("Messenger")) {

                    if (GameData.passCardTo()) {
                        LogManager.logMessage(
                                GameData.getAdventurers()[Game.getRoundNum()].getName() +
                                        " 传递了一张卡片给 " +
                                        GameData.getAdventurers()[GameData.getSelectedPawn()].getName());

                        // 更新界面
                        UpdateManager.getPlayerUpdater().updateUI();
                        UpdateManager.getTreasureUpdater().updateUI();

                        Game.doAction();
                        GameData.selectPawn(GameData.NO_SELECTION);  // 清空选择
                        GameData.resetCardsInRound();
                    }
                } else {
                    LogManager.logMessage("无法执行[传递]操作");
                }
            } else {
                LogManager.logMessage("已超过最大行动次数或未选择接收者");
            }
        });

        // 捕获宝藏
        controlButtons.get(4).setOnAction(e -> {
            LogManager.logMessage("正在尝试捕获宝藏...");

            if (Game.getActionCount() < 3) {
                ArrayList<Integer> handCards = new ArrayList<>(
                        GameData.getAdventurers()[Game.getRoundNum()].getHandCards());

                // 统计每种宝藏卡的数量
                int[] treasureCount = {0, 0, 0, 0};
                for (int handCard : handCards) {
                    if (handCard >= 0 && handCard <= 4) {
                        treasureCount[0]++;
                    } else if (handCard >= 5 && handCard <= 9) {
                        treasureCount[1]++;
                    } else if (handCard >= 10 && handCard <= 14) {
                        treasureCount[2]++;
                    } else if (handCard >= 15 && handCard <= 19) {
                        treasureCount[3]++;
                    }
                }

                // 检查是否有4张相同的宝藏卡，且在对应宝藏瓦片上
                for (int i = 0; i < treasureCount.length; i++) {
                    if (treasureCount[i] == 4) {
                        Tile currentTile = GameData.getBoard().getTile(
                                GameData.getAdventurers()[Game.getRoundNum()].getX(),
                                GameData.getAdventurers()[Game.getRoundNum()].getY());

                        if (currentTile.getTileId() == 2 * i + 1 || currentTile.getTileId() == 2 * i + 2) {
                            // 标记瓦片宝藏已捕获
                            currentTile.setCaptured();

                            // 添加宝藏到玩家收集列表
                            for (TreasureFigurines figurine : TreasureFigurines.values()) {
                                if (figurine.ordinal() == i) {
                                    GameData.getAdventurers()[Game.getRoundNum()].addCapturedTreasure(figurine);

                                    // 移除相应的宝藏卡
                                    Iterator<Integer> iterator = GameData.getAdventurers()[Game.getRoundNum()].getHandCards().iterator();
                                    while (iterator.hasNext()) {
                                        Integer handCardNo = iterator.next();
                                        if (handCardNo >= i * 5 && handCardNo <= i * 5 + 4) {
                                            iterator.remove();


                                            GameData.getTreasureDeck().discard(handCardNo);
                                        }
                                    }
                                }
                            }

                            // 标记所有相同宝藏瓦片为已捕获
                            int tileId1 = 2 * i + 1;
                            int tileId2 = 2 * i + 2;
                            int idx1 = GameData.getTiles().indexOf(tileId1);
                            int idx2 = GameData.getTiles().indexOf(tileId2);

                            if (idx1 >= 0) {
                                int[] coords1 = GameMap.getCoordinates(idx1);
                                GameData.getBoard().getTile(coords1[0], coords1[1]).setCaptured();
                            }

                            if (idx2 >= 0) {
                                int[] coords2 = GameMap.getCoordinates(idx2);
                                GameData.getBoard().getTile(coords2[0], coords2[1]).setCaptured();
                            }

                            // 更新界面
                            UpdateManager.getPlayerUpdater().updateUI();
                            UpdateManager.getBoardUpdater().updateUI();

                            LogManager.logMessage("成功捕获了宝藏！");
                            Game.doAction();
                            return;
                        }
                    }
                }

                LogManager.logMessage("你没有足够的宝藏卡来捕获宝藏");
            } else {
                LogManager.logMessage("已超过最大行动次数");
            }
        });

        // 起飞
        controlButtons.get(5).setOnAction(e -> {
            if (Game.checkLiftOff()) {

                LogManager.logMessage("起飞成功！");
                Game.gameOver(true);
            }
        });

        // 特殊行动
        controlButtons.get(6).setOnAction(e -> {
            // 使用特殊卡牌
            if (GameData.getCardsInRound() != null &&
                    !GameData.getCardsInRound().isEmpty() &&
                    GameData.getSpecialActionTile()[0] != -1 &&
                    GameData.getSpecialActionTile()[1] != -1) {

                int lastSelected = GameData.getCardsInRound().get(GameData.getCardsInRound().size() - 1);

                // 使用沙袋卡
                if (lastSelected == 23 || lastSelected == 24) {
                    Tile shoredTile = GameData.getBoard().getTile(
                            GameData.getSpecialActionTile()[0],
                            GameData.getSpecialActionTile()[1]);

                    if (shoredTile.exists() && shoredTile.getStatus() == TileStatus.FLOODED) {
                        shoredTile.shoreUp();
                        GameData.getAdventurers()[Game.getRoundNum()].getHandCards().remove((Integer) lastSelected);
                        GameData.getTreasureDeck().discard(lastSelected);
                        LogManager.logMessage("使用[沙袋]加固了一个瓦片");
                        GameData.resetCardsInRound();
                        GameData.resetSpecialActionTile();



                        // 更新界面
                        UpdateManager.getBoardUpdater().updateUI();
                        UpdateManager.getPlayerUpdater().updateUI();
                    }
                }
                // 使用直升机卡
                else if ((lastSelected == 20 || lastSelected == 21 || lastSelected == 22) &&
                        !GameData.getSelectedPawns().isEmpty()) {  // 使用isEmpty()而不是size() != 0

                    // 当前玩家先移动
                    GameData.getBoard().getTile(
                                    GameData.getAdventurers()[Game.getRoundNum()].getX(),
                                    GameData.getAdventurers()[Game.getRoundNum()].getY())
                            .moveOff(GameData.getAdventurers()[Game.getRoundNum()]);

                    GameData.getAdventurers()[Game.getRoundNum()].setPos(
                            GameData.getSpecialActionTile()[0],
                            GameData.getSpecialActionTile()[1]);

                    GameData.getBoard().getTile(
                                    GameData.getSpecialActionTile()[0],
                                    GameData.getSpecialActionTile()[1])
                            .moveOnto(GameData.getAdventurers()[Game.getRoundNum()].getId());

                    // 然后移动所有选中的玩家
                    for (int pawn : GameData.getSelectedPawns()) {
                        // 确保索引有效且不是-1
                        if (pawn >= 0 && pawn < GameData.getAdventurers().length) {
                            GameData.getBoard().getTile(
                                            GameData.getAdventurers()[pawn].getX(),
                                            GameData.getAdventurers()[pawn].getY())
                                    .moveOff(GameData.getAdventurers()[pawn]);

                            GameData.getAdventurers()[pawn].setPos(
                                    GameData.getSpecialActionTile()[0],
                                    GameData.getSpecialActionTile()[1]);

                            GameData.getBoard().getTile(
                                            GameData.getSpecialActionTile()[0],
                                            GameData.getSpecialActionTile()[1])
                                    .moveOnto(GameData.getAdventurers()[pawn].getId());
                        }
                    }

                    // 丢弃使用的卡片
                    GameData.getAdventurers()[Game.getRoundNum()].getHandCards().remove((Integer) lastSelected);
                    GameData.getTreasureDeck().discard(lastSelected);
                    LogManager.logMessage("使用[直升机]运送了玩家");
                    GameData.selectPawn(GameData.NO_SELECTION);
                    GameData.resetCardsInRound();
                    GameData.resetSpecialActionTile();

                    // 更新界面
                    UpdateManager.getBoardUpdater().updateUI();
                    UpdateManager.getPlayerUpdater().updateUI();
                }

                // 如果在假回合中，返回正常回合
                if (Game.isInFakeRound()) {
                    Game.setInFakeRound(false);
                    Game.setRoundNum(Game.getFakeRoundNum());
                    Game.setFakeRoundNum(-1);
                    Game.setActionCount(Game.getFakeActionCount());
                    Game.setFakeActionCount(-1);
                    LogManager.logMessage("返回到玩家 " + (Game.getRoundNum() + 1) +
                            " 的回合 (" + GameData.getAdventurers()[Game.getRoundNum()].getName() + ")");
                    LogManager.logMessage("已执行 " + Game.getActionCount() + " 个行动");

                    // 更新界面
                    UpdateManager.getPlayerUpdater().updateUI();
                }
            }
            // 进入假回合（使用其他玩家的特殊卡）
            else if (GameData.getSelectedPawn() != -1 &&
                    GameData.getSelectedPawn() >= 0 &&
                    GameData.getSelectedPawn() < GameData.getAdventurers().length &&
                    !Game.isInFakeRound()) {

                Game.setInFakeRound(true);
                Game.setFakeRoundNum(Game.getRoundNum());
                Game.setFakeActionCount(Game.getActionCount());
                Game.setActionCount(3);
                Game.setRoundNum(GameData.getSelectedPawn());

                // 更新界面
                UpdateManager.getPlayerUpdater().updateUI();
                LogManager.logMessage("切换到玩家 " + (Game.getRoundNum() + 1) +
                        " 的回合 (" + GameData.getAdventurers()[Game.getRoundNum()].getName() + ")");
            }
            // 不执行任何操作返回
            else if (GameData.getSelectedPawn() == Game.getFakeRoundNum() && Game.isInFakeRound()) {
                Game.setInFakeRound(false);
                Game.setRoundNum(Game.getFakeRoundNum());
                Game.setFakeRoundNum(-1);
                Game.setActionCount(Game.getFakeActionCount());
                Game.setFakeActionCount(-1);

                // 更新界面
                UpdateManager.getBoardUpdater().updateUI();
                UpdateManager.getPlayerUpdater().updateUI();

                LogManager.logMessage("返回到玩家 " + (Game.getRoundNum() + 1) +
                        " 的回合 (" + GameData.getAdventurers()[Game.getRoundNum()].getName() + ")");
                LogManager.logMessage("已执行 " + Game.getActionCount() + " 个行动");
            }

            // 最终更新界面
            UpdateManager.getBoardUpdater().updateUI();
            UpdateManager.getPlayerUpdater().updateUI();
        });

        // 下一阶段
        controlButtons.get(7).setOnAction(e -> {


            if (!Game.isInFakeRound()) {
                if (!Game.isStage23Done()) {
                    LogManager.logMessage("[下一阶段]");
                    Game.stage23();
                } else if (Game.isNeedToSave()) {
                    LogManager.logMessage("请先拯救落水的冒险者");
                } else {
                    LogManager.logMessage("[下一回合]");
                    Game.endRound();
                }
            } else {
                Game.setInFakeRound(false);
                Game.setRoundNum(Game.getFakeRoundNum());
                Game.setFakeRoundNum(-1);
                Game.setActionCount(Game.getFakeActionCount());
                Game.setFakeActionCount(-1);
                GameData.resetSpecialActionTile();
                GameData.resetCardsInRound();
                GameData.selectPawn(GameData.NO_SELECTION);

                // 更新界面
                UpdateManager.getBoardUpdater().updateUI();
                UpdateManager.getPlayerUpdater().updateUI();
            }
        });

        // 丢弃
        controlButtons.get(8).setOnAction(e -> {
            if (GameData.getCardsInRound().size() != 0 && !Game.isInFakeRound()) {
                ArrayList<Integer> allCardsInRound = new ArrayList<>();
                allCardsInRound.addAll(GameData.getAdventurers()[Game.getRoundNum()].getHandCards());
                allCardsInRound.addAll(GameData.getDisplayedTreasureCards());

                GameData.getAdventurers()[Game.getRoundNum()].getHandCards().clear();
                GameData.getDisplayedTreasureCards().clear();

                for (Integer card : allCardsInRound) {
                    if (GameData.getCardsInRound().contains(card)) {
                        GameData.getTreasureDeck().discard(card);
                    } else {
                        GameData.getAdventurers()[Game.getRoundNum()].getHandCards().add(card);
                    }
                }

                // 如果手牌超过5张，将多余的放入展示区
                Iterator<Integer> iterator = GameData.getAdventurers()[Game.getRoundNum()].getHandCards().iterator();
                int count = 0;
                while (iterator.hasNext()) {
                    int handCard = iterator.next();
                    if (count >= 5) {
                        GameData.getDisplayedTreasureCards().add(handCard);
                        iterator.remove();
                    }
                    count++;
                }

                LogManager.logMessage("[丢弃]卡片");
                GameData.resetCardsInRound();



                // 更新界面
                UpdateManager.getPlayerUpdater().updateUI();
                UpdateManager.getTreasureUpdater().updateUI();
            }
            // 在假回合中丢弃卡片
            else if (Game.isInFakeRound() && GameData.getCardsInRound().size() != 0) {
                ArrayList<Integer> allCardsInRound = new ArrayList<>(
                        GameData.getAdventurers()[Game.getRoundNum()].getHandCards());

                GameData.getAdventurers()[Game.getRoundNum()].getHandCards().clear();

                for (Integer card : allCardsInRound) {
                    if (GameData.getCardsInRound().contains(card)) {
                        GameData.getTreasureDeck().discard(card);
                    } else {
                        GameData.getAdventurers()[Game.getRoundNum()].getHandCards().add(card);
                    }
                }

                LogManager.logMessage("[丢弃]卡片");
                Game.setInFakeRound(false);
                Game.setActionCount(Game.getFakeActionCount());
                Game.setFakeActionCount(-1);
                Game.setRoundNum(Game.getFakeRoundNum());
                Game.setFakeRoundNum(-1);
                GameData.selectPawn(GameData.NO_SELECTION);
                GameData.resetCardsInRound();



                // 更新界面
                UpdateManager.getPlayerUpdater().updateUI();
                UpdateManager.getTreasureUpdater().updateUI();
            } else {
                LogManager.logMessage("请选择要[丢弃]的卡片");
            }
        });

        // 清除选择
        controlButtons.get(9).setOnAction(e -> {
            LogManager.logMessage("[清除]所有选择");
            GameData.selectPawn(GameData.NO_SELECTION);
            GameData.resetCardsInRound();
            GameData.resetSpecialActionTile();

            // 更新界面
            UpdateManager.getPlayerUpdater().updateUI();
        });
    }

    /**
     * 获取控制按钮列表
     * @return 控制按钮列表
     */
    public ArrayList<Button> getControlButtons() {
        return controlButtons;
    }
}
