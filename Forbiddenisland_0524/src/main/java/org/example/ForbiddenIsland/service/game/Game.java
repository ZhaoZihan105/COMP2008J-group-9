package org.example.ForbiddenIsland.service.game;

import javafx.application.Platform;
import org.example.ForbiddenIsland.gui.updater.LogManager;
import org.example.ForbiddenIsland.gui.updater.UpdateManager;
import org.example.ForbiddenIsland.service.game.component.adventurer.Adventurer;
import org.example.ForbiddenIsland.service.game.component.adventurer.Engineer;
import org.example.ForbiddenIsland.service.game.component.cards.TreasureFigurines;
import org.example.ForbiddenIsland.service.game.data.Tile;
import org.example.ForbiddenIsland.utils.GameMap;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 游戏控制器
 * 负责管理游戏流程、玩家回合和胜负条件
 */
public class Game {
    // 游戏状态变量
    private static int roundNum = 0;  // 当前回合玩家
    private static int fakeRoundNum = -1;  // 临时回合玩家
    private static int actionCount = 0;  // 当前行动计数
    private static int fakeActionCount = 0;  // 临时行动计数
    private static int numOfPlayer;  // 玩家数量
    private static boolean stage23Done = false;  // 是否完成阶段2和3
    private static boolean needToSave = false;  // 是否需要救援
    private static boolean inFakeRound = false;  // 是否在临时回合中
    private static ArrayList<Integer> playersInWater;  // 落水玩家列表

    // 游戏数据引用
    private GameData gameData;

    /**
     * 创建一个新游戏
     * @param numOfPlayers 玩家数量 (2-4)
     * @param waterLevel 初始水位 (1-5)
     */
    public Game(int numOfPlayers, int waterLevel) {
        numOfPlayer = numOfPlayers;
        playersInWater = new ArrayList<>();

        // 初始化游戏数据
        gameData = new GameData(numOfPlayers, waterLevel);
        LogManager.logMessage("初始化玩家...");

        // 创建UI更新管理器
        new UpdateManager();

        // 初始洪水阶段
        LogManager.logMessage("岛屿开始下沉...");

        UpdateManager.getFloodUpdater().updateUI();
        GameData.getBoard().sinkTiles(GameData.getFloodDeck().getCards());
        GameData.getFloodDeck().discard();
        GameData.getFloodDeck().setToNormal();

        // 开始游戏
        LogManager.logMessage("【游戏开始！】");
        LogManager.logMessage("【玩家 " + (roundNum + 1) + " 】\n(" +
                GameData.getAdventurers()[roundNum].getName() + " 的回合)");
        LogManager.logMessage("请执行最多3个行动");

    }

    /**
     * 执行玩家回合的阶段2和3：
     * - 抽取宝藏卡
     * - 处理水位上升事件
     * - 抽取洪水卡并淹没对应瓦片
     */
    public static void stage23() {
        // 阶段2：抽取宝藏卡
        GameData.getDisplayedTreasureCards().addAll(GameData.getTreasureDeck().getCards());
        actionCount = 3;
        UpdateManager.getTreasureUpdater().updateUI();

        // 处理水位上升卡
        for (int i = GameData.getDisplayedTreasureCards().size() - 1; i >= 0; i--) {
            Integer treasureID = GameData.getDisplayedTreasureCards().get(i);
            if (treasureID == 25 || treasureID == 26 || treasureID == 27) {  // 水位上升卡ID
                GameData.getWaterMeter().waterRise();
                GameData.getFloodDeck().putBackToTop();
                GameData.getTreasureDeck().discard(treasureID);
                GameData.getDisplayedTreasureCards().remove(i);
            }
        }

        // 更新UI
        UpdateManager.getTreasureUpdater().updateUI();
        UpdateManager.getPlayerUpdater().updateUI();
        UpdateManager.getWaterMeterUpdater().updateUI();
        UpdateManager.getBoardUpdater().updateUI();

        // 阶段3：抽取洪水卡并淹没瓦片
        UpdateManager.getFloodUpdater().updateUI();
        GameData.getBoard().sinkTiles(GameData.getFloodDeck().getCards());
        GameData.getFloodDeck().discard();


        // 标记阶段完成
        stage23Done = true;
    }

    /**
     * 结束当前玩家回合，开始下一个玩家的回合
     */
    public static void endRound() {
        // 检查手牌是否超过上限
        if (GameData.getAdventurers()[roundNum].getHandCards().size() +
                GameData.getDisplayedTreasureCards().size() > 5) {
            LogManager.logMessage("你有超过5张卡片，请先丢弃！");
            GameData.resetCardsInRound();
            return;
        } else {
            // 将展示的宝藏卡移到玩家手牌
            GameData.getAdventurers()[roundNum].getHandCards().addAll(GameData.getDisplayedTreasureCards());
            GameData.getDisplayedTreasureCards().clear();
            GameData.selectPawn(GameData.NO_SELECTION);  // 清空选择
            GameData.resetCardsInRound();
            UpdateManager.getTreasureUpdater().updateUI();
            UpdateManager.getPlayerUpdater().updateUI();
        }

        // 检查胜负条件
        if (GameData.getBoard().areTreasuresSunk()) {
            LogManager.logMessage("【警告！】宝藏被海水淹没！");
            gameOver(false);
            return;
        }

        // 准备下一回合
        GameData.selectPawn(GameData.NO_SELECTION);

        // 重置特殊能力
        if (GameData.getAdventurers()[roundNum] instanceof Engineer) {
            ((Engineer) GameData.getAdventurers()[roundNum]).resetShoreUpCount();
        }

        // 进入下一个玩家的回合
        actionCount = 0;
        roundNum = (roundNum + 1) % numOfPlayer;
        stage23Done = false;

        // 开始下一玩家的回合
        LogManager.logMessage("【玩家 " + (roundNum + 1) + " 】\n(" +
                GameData.getAdventurers()[roundNum].getName() + " 的回合)");
        UpdateManager.getPlayerUpdater().updateUI();
    }

    /**
     * 当玩家落入水中，必须游到安全地带
     * 管理从下沉瓦片救援玩家的过程
     */
    public static void rescuePlayersRound() {
        // 检查是否所有玩家都已获救
        if (playersInWater.size() == 0) {
            roundNum = fakeRoundNum;
            fakeRoundNum = -1;
            actionCount = 3;
            needToSave = false;
            inFakeRound = false;
            UpdateManager.getControlsUpdater().updateUI();
            return;
        }

        // 依次救援每个玩家
        for (Adventurer adventurer : GameData.getAdventurers()) {
            if (playersInWater.contains(adventurer.getId())) {
                roundNum = adventurer.getOrder();
                actionCount = 2;
                UpdateManager.getControlsUpdater().updateUI();
                playersInWater.remove((Integer) adventurer.getId());

                int x = GameData.getAdventurers()[roundNum].getX();
                int y = GameData.getAdventurers()[roundNum].getY();

                // 检查玩家是否有相邻瓦片可以游到
                boolean canSwim = checkSwimPossibilities(x, y);
                if (!canSwim) {
                    gameOver(false);
                    LogManager.logMessage("【警告！】没有相邻的瓦片可以游到");
                    return;
                }

                // 提示玩家选择瓦片
                LogManager.logMessage("为【玩家 " + (roundNum + 1) + "】(" +
                        GameData.getAdventurers()[roundNum].getName() + ") 选择一个最近的瓦片游到，然后点击[移动]");
                UpdateManager.getPlayerUpdater().updateUI();
                UpdateManager.getBoardUpdater().updateUI();
                break;
            }
        }
    }

    /**
     * 根据玩家位置和特殊能力检查是否有瓦片可以游到
     */
    private static boolean checkSwimPossibilities(int x, int y) {
        // 潜水员和飞行员有增强的移动能力
        if (GameData.getAdventurers()[roundNum].getName().equals("Diver") ||
                GameData.getAdventurers()[roundNum].getName().equals("Pilot")) {
            return true;
        }

        boolean isExplorer = GameData.getAdventurers()[roundNum].getName().equals("Explorer");

        // 基于位置检查相邻瓦片
        if (x == 0) {
            // 顶部边缘
            if (!GameData.getBoard().getTile(x + 1, y).exists() &&
                    !GameData.getBoard().getTile(x, Math.max(0, y - 1)).exists() &&
                    !GameData.getBoard().getTile(x, Math.min(5, y + 1)).exists()) {
                return false;
            } else if (isExplorer &&
                    !GameData.getBoard().getTile(x + 1, y).exists() &&
                    !GameData.getBoard().getTile(x, Math.max(0, y - 1)).exists() &&
                    !GameData.getBoard().getTile(x, Math.min(5, y + 1)).exists() &&
                    !GameData.getBoard().getTile(x + 1, Math.max(0, y - 1)).exists() &&
                    !GameData.getBoard().getTile(x + 1, Math.min(5, y + 1)).exists()) {
                return false;
            }
        } else if (x == 5) {
            // 底部边缘
            if (!GameData.getBoard().getTile(x - 1, y).exists() &&
                    !GameData.getBoard().getTile(x, Math.max(0, y - 1)).exists() &&
                    !GameData.getBoard().getTile(x, Math.min(5, y + 1)).exists()) {
                return false;
            } else if (isExplorer &&
                    !GameData.getBoard().getTile(x - 1, y).exists() &&
                    !GameData.getBoard().getTile(x, Math.max(0, y - 1)).exists() &&
                    !GameData.getBoard().getTile(x, Math.min(5, y + 1)).exists() &&
                    !GameData.getBoard().getTile(x - 1, Math.max(0, y - 1)).exists() &&
                    !GameData.getBoard().getTile(x - 1, Math.min(5, y + 1)).exists()) {
                return false;
            }
        } else if (y == 0) {
            // 左侧边缘
            if (!GameData.getBoard().getTile(x - 1, y).exists() &&
                    !GameData.getBoard().getTile(x + 1, y).exists() &&
                    !GameData.getBoard().getTile(x, y + 1).exists()) {
                return false;
            } else if (isExplorer &&
                    !GameData.getBoard().getTile(x - 1, y).exists() &&
                    !GameData.getBoard().getTile(x + 1, y).exists() &&
                    !GameData.getBoard().getTile(x, y + 1).exists() &&
                    !GameData.getBoard().getTile(x - 1, y + 1).exists() &&
                    !GameData.getBoard().getTile(x + 1, y + 1).exists()) {
                return false;
            }
        } else if (y == 5) {
            // 右侧边缘
            if (!GameData.getBoard().getTile(x - 1, y).exists() &&
                    !GameData.getBoard().getTile(x + 1, y).exists() &&
                    !GameData.getBoard().getTile(x, y - 1).exists()) {
                return false;
            } else if (isExplorer &&
                    !GameData.getBoard().getTile(x - 1, y).exists() &&
                    !GameData.getBoard().getTile(x + 1, y).exists() &&
                    !GameData.getBoard().getTile(x, y - 1).exists() &&
                    !GameData.getBoard().getTile(x - 1, y - 1).exists() &&
                    !GameData.getBoard().getTile(x + 1, y - 1).exists()) {
                return false;
            }
        } else {
            // 棋盘中央
            if (!GameData.getBoard().getTile(x - 1, y).exists() &&
                    !GameData.getBoard().getTile(x + 1, y).exists() &&
                    !GameData.getBoard().getTile(x, y - 1).exists() &&
                    !GameData.getBoard().getTile(x, y + 1).exists()) {
                return false;
            } else if (isExplorer &&
                    !GameData.getBoard().getTile(x - 1, y).exists() &&
                    !GameData.getBoard().getTile(x + 1, y).exists() &&
                    !GameData.getBoard().getTile(x, y - 1).exists() &&
                    !GameData.getBoard().getTile(x, y + 1).exists() &&
                    !GameData.getBoard().getTile(x - 1, y - 1).exists() &&
                    !GameData.getBoard().getTile(x + 1, y - 1).exists() &&
                    !GameData.getBoard().getTile(x - 1, y + 1).exists() &&
                    !GameData.getBoard().getTile(x + 1, y + 1).exists()) {
                return false;
            }
        }

        return true;
    }

    /**
     * 游戏结束
     * @param isWin true表示玩家胜利，false表示玩家失败
     */
    public static void gameOver(boolean isWin) {
        if (isWin) {
            LogManager.logMessage("【恭喜！】你们成功逃离了岛屿并带走了宝藏！\n（＾∀＾●）ﾉｼ");

        } else {
            LogManager.logMessage("【游戏结束】岛屿已将你们吞噬！\n＞﹏＜");

        }

        // 禁用所有游戏组件
        Platform.runLater(() -> {
            try {
                Thread.sleep(1000);
                UpdateManager.getBoardUpdater().gameOver();
                UpdateManager.getTreasureUpdater().gameOver();
                UpdateManager.getWaterMeterUpdater().gameOver();
                UpdateManager.getFloodUpdater().gameOver();
                UpdateManager.getControlsUpdater().gameOver();
                UpdateManager.getPlayerUpdater().gameOver();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    public static boolean checkLiftOff() {
        // 检查是否所有玩家都在起飞瓦片上
        int idx14 = GameData.getTiles().indexOf(14); // 起飞点ID是14

        if (idx14 == -1) {
            LogManager.logMessage("【错误】起飞点ID 14 不存在于当前游戏瓦片中，无法起飞！");
            return false;
        }

        int[] coords = GameMap.getCoordinates(idx14);
        if (coords[0] < 0 || coords[1] < 0 || coords[0] >= 6 || coords[1] >= 6) {
            LogManager.logMessage("【错误】起飞点坐标非法：" + Arrays.toString(coords));
            return false;
        }

        Tile liftoffTile = GameData.getBoard().getTile(coords[0], coords[1]);

        if (!liftoffTile.exists()) {
            LogManager.logMessage("起飞点已沉没！");
            return false;
        }

        // 检查玩家数量和起飞瓦片上的玩家
        if (liftoffTile.getPlayersOnTile().size() != Game.getNumOfPlayer()) {
            LogManager.logMessage("不是所有玩家都在起飞点上！");
            return false;
        }

        // 检查是否有直升机卡
        boolean hasHelicopterCard = false;
        ArrayList<Integer> handCards = new ArrayList<>();

        for (Adventurer adventurer : GameData.getAdventurers()) {
            handCards.addAll(adventurer.getHandCards());
        }

        if (handCards.contains(20) || handCards.contains(21) || handCards.contains(22)) {
            hasHelicopterCard = true;
        }

        // 检查是否收集了所有宝藏
        ArrayList<TreasureFigurines> collectedFigurines = new ArrayList<>();
        for (Adventurer adventurer : GameData.getAdventurers()) {
            collectedFigurines.addAll(adventurer.getCapturedFigurines());
        }

        // 必须有4种宝藏和直升机卡才能起飞
        if (hasHelicopterCard && collectedFigurines.size() == 4) {
            return true;
        } else {
            if (!hasHelicopterCard) {
                LogManager.logMessage("需要一张直升机卡来起飞！");
            }
            if (collectedFigurines.size() < 4) {
                LogManager.logMessage("还没有收集齐所有宝藏！");
            }
            return false;
        }
    }


    /**
     * 执行一个行动并增加行动计数
     */
    public static void doAction() {
        actionCount += 1;
    }

    /**
     * 减少行动计数（用于特殊能力）
     */
    public static void moreAction() {
        actionCount -= 1;
    }

    /**
     * 播放当前玩家回合的音频
     */
    /** private static void playTurnAudio() {
        if (Constants.AUDIO_ENABLED) {
            switch (roundNum) {
                case 0:
                    AudioManager.playAudio(AudioManager.AudioType.PLAYER1);
                    break;
                case 1:
                    AudioManager.playAudio(AudioManager.AudioType.PLAYER2);
                    break;
                case 2:
                    AudioManager.playAudio(AudioManager.AudioType.PLAYER3);
                    break;
                case 3:
                    AudioManager.playAudio(AudioManager.AudioType.PLAYER4);
                    break;
            }
        }
    }
     */
    // Getters and setters
    public static void setPlayersInWater(ArrayList<Integer> playerIDs) {
        Game.playersInWater.addAll(playerIDs);
    }

    public static int getNumOfPlayer() {
        return numOfPlayer;
    }

    public static int getActionCount() {
        return actionCount;
    }

    public static void setActionCount(int num) {
        actionCount = num;
    }

    public static int getFakeActionCount() {
        return fakeActionCount;
    }

    public static void setFakeActionCount(int fakeActionCount) {
        Game.fakeActionCount = fakeActionCount;
    }

    public static int getRoundNum() {
        return roundNum;
    }

    public static void setRoundNum(int roundNum) {
        Game.roundNum = roundNum;
    }

    public static int getFakeRoundNum() {
        return fakeRoundNum;
    }

    public static void setFakeRoundNum(int fakeRoundNum) {
        Game.fakeRoundNum = fakeRoundNum;
    }

    public static boolean isStage23Done() {
        return stage23Done;
    }

    public static boolean isNeedToSave() {
        return needToSave;
    }

    public static void setNeedToSave(boolean needToSave) {
        Game.needToSave = needToSave;
    }

    public static boolean isInFakeRound() {
        return inFakeRound;
    }

    public static void setInFakeRound(boolean inFakeRound) {
        Game.inFakeRound = inFakeRound;
    }
}