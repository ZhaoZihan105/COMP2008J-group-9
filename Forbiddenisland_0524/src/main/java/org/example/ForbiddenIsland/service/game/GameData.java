package org.example.ForbiddenIsland.service.game;

import org.example.ForbiddenIsland.gui.updater.LogManager;
import org.example.ForbiddenIsland.service.game.component.adventurer.*;
import org.example.ForbiddenIsland.service.game.data.*;
import org.example.ForbiddenIsland.utils.GameMap;

import java.util.ArrayList;
import java.util.Collections;

/**
 * 游戏数据中心
 * 存储和管理所有游戏状态数据，包括棋盘、卡牌、玩家等
 */
public class GameData {
    // 定义表示"未选择"的常量
    public static final int NO_SELECTION = Integer.MIN_VALUE;

    // 特殊操作的瓦片坐标
    private static final int[] specialActionTile = {-1, -1};

    // 游戏核心组件
    private static BoardData board;
    private static TreasureDeck treasureDeck;
    private static FloodDeck floodDeck;
    private static WaterMeter waterMeter;
    private static Adventurer[] adventurers;

    // 游戏数据
    private static ArrayList<Integer> tiles;  // 瓦片ID列表
    private static ArrayList<Integer> displayedTreasureCards;  // 展示的宝藏卡片
    private static ArrayList<Integer> cardsInRound;  // 当前回合中选择的卡片
    private static int selectedPawn = NO_SELECTION;  // 当前选择的玩家
    private static ArrayList<Integer> selectedPawns;  // 所有选择的玩家

    /**
     * 初始化一个新游戏
     * @param numOfPlayers 玩家数量
     * @param waterLevel 初始水位
     */
    public GameData(int numOfPlayers, int waterLevel) {
        // 初始化坐标映射
        GameMap.setupMatchers();

        // 初始化游戏组件
        floodDeck = new FloodDeck();
        treasureDeck = new TreasureDeck();
        waterMeter = new WaterMeter(waterLevel);
        adventurers = new Adventurer[numOfPlayers];
        displayedTreasureCards = new ArrayList<>();
        cardsInRound = new ArrayList<>();
        selectedPawns = new ArrayList<>();

        // 随机分配玩家角色
        ArrayList<Integer> rolesList = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            rolesList.add(i);
        }
        Collections.shuffle(rolesList);
        ArrayList<Integer> playerRoles = new ArrayList<>(rolesList.subList(0, numOfPlayers));

        // 初始化玩家角色
        for (int i = 0; i < playerRoles.size(); i++) {
            switch (playerRoles.get(i)) {
                case 0:
                    adventurers[i] = new Diver(i);
                    break;
                case 1:
                    adventurers[i] = new Engineer(i);
                    break;
                case 2:
                    adventurers[i] = new Explorer(i);
                    break;
                case 3:
                    adventurers[i] = new Messenger(i);
                    break;
                case 4:
                    adventurers[i] = new Navigator(i);
                    break;
                case 5:
                    adventurers[i] = new Pilot(i);
                    break;
            }
        }

        // 初始化游戏瓦片（随机排列）
        tiles = new ArrayList<>();
        for (int i = 1; i <= 24; i++) {
            tiles.add(i);
        }
        Collections.shuffle(tiles);

        // 创建游戏板
        board = new BoardData(playerRoles, tiles);

        // 给玩家发初始宝藏卡（避免抽到水位上升卡）
        for (Adventurer adventurer : adventurers) {
            adventurer.setHandCards(treasureDeck.getInitialCards());
        }
    }

    /**
     * 选择宝藏卡
     * @param isFromHands 是否来自玩家手牌
     * @param index 卡片索引
     */
    public static void selectTreasureCard(boolean isFromHands, int index) {
        int cardInUse;
        if (isFromHands) {
            cardInUse = adventurers[Game.getRoundNum()].getHandCards().get(index);
        } else {
            cardInUse = displayedTreasureCards.get(index);
        }

        if (cardsInRound.size() < 5) {
            cardsInRound.add(cardInUse);
        }
    }

    /**
     * 选择一个角色（用于卡片交换或特殊操作）
     * @param index 角色索引，负值表示清除选择
     */
    public static void selectPawn(int index) {
        if (index < 0) {
            selectedPawn = NO_SELECTION;
            cardsInRound.clear();
            selectedPawns.clear();
        } else {
            selectedPawn = index;
            selectedPawns.add(index);
        }
        // 添加日志输出，帮助调试
        LogManager.logMessage("选择玩家: " + (index < 0 ? "无" : index) +
                ", selectedPawns大小: " + selectedPawns.size());
    }

    /**
     * 处理瓦片选择（移动或加固）
     * @param coords 选择瓦片的坐标
     */
    public static void selectTile(int[] coords) {

        // 添加日志，帮助调试
        System.out.println("选择瓦片坐标: [" + coords[0] + ", " + coords[1] + "]");

        specialActionTile[0] = coords[0];
        specialActionTile[1] = coords[1];

        Adventurer currentPlayer = adventurers[Game.getRoundNum()];

        // 输出当前玩家位置和目标位置
        System.out.println("当前玩家位置: [" + currentPlayer.getX() + ", " + currentPlayer.getY() + "]");
        System.out.println("目标位置: [" + coords[0] + ", " + coords[1] + "]");

        boolean isNearY = ((currentPlayer.getX() == coords[0]) &&
                (Math.abs(currentPlayer.getY() - coords[1]) == 1));
        boolean isNearX = ((currentPlayer.getY() == coords[1]) &&
                (Math.abs(currentPlayer.getX() - coords[0]) == 1));
        boolean isOnTile = ((currentPlayer.getX() == coords[0]) &&
                (currentPlayer.getY() == coords[1]));
        boolean isNearDiagonally = ((Math.abs(currentPlayer.getX() - coords[0]) == 1) &&
                (Math.abs(currentPlayer.getY() - coords[1]) == 1));

        // 检查玩家是否可以移动到选择的瓦片
        if (((isNearX || isNearY) && (board.getTile(coords[0], coords[1]).exists())) ||
                ((isNearDiagonally) && (board.getTile(coords[0], coords[1]).exists()) &&
                        (currentPlayer.getName().equals("Explorer"))) ||
                (currentPlayer.getName().equals("Pilot") && !isOnTile)) {

            currentPlayer.setMove(coords[0], coords[1]);
            board.setCanMove(true);

            // 检查瓦片是否可以加固
            if (board.getTile(coords[0], coords[1]).getStatus() == TileStatus.FLOODED) {
                if (!currentPlayer.getName().equals("Pilot") ||
                        ((isNearX || isNearY) && (board.getTile(coords[0], coords[1]).exists()))) {
                    currentPlayer.setShoreUp(coords[0], coords[1]);
                    board.setCanShoreUp(true);
                }
            } else {
                board.setCanShoreUp(false);
            }
        }
        // 检查玩家是否站在可以加固的已淹没瓦片上
        else if ((isOnTile) && (board.getTile(coords[0], coords[1]).exists() &&
                (board.getTile(coords[0], coords[1]).getStatus() == TileStatus.FLOODED))) {
            board.setCanMove(false);
            currentPlayer.setShoreUp(coords[0], coords[1]);
            board.setCanShoreUp(true);
        }
        // 潜水员从水中救援的特殊情况
        else if (currentPlayer.getName().equals("Diver") &&
                (board.getTile(coords[0], coords[1]).exists()) &&
                Game.isInFakeRound() && Game.isNeedToSave()) {

            ArrayList<int[]> candidates = new ArrayList<>();
            double minDistance = 7.0;

            // 查找潜水员可以游泳到的最近瓦片
            for (int i = 0; i < board.getTileMap().length; i++) {
                for (int j = 0; j < board.getTileMap()[i].length; j++) {
                    if (board.getTile(i, j).exists()) {
                        if (currentPlayer.getX() == i && currentPlayer.getY() == j) {
                            continue;
                        }

                        // 计算距离
                        double distance = Math.sqrt(
                                Math.pow(((double) i - (double) currentPlayer.getX()), 2) +
                                        Math.pow(((double) j - (double) currentPlayer.getY()), 2));

                        if (distance < minDistance) {
                            candidates.clear();
                            minDistance = distance;
                            candidates.add(new int[]{i, j});
                        } else if (distance == minDistance) {
                            candidates.add(new int[]{i, j});
                        }
                    }
                }
            }

            board.setCanMove(false);
            for (int[] candidate : candidates) {
                if ((candidate[0] == coords[0]) && (candidate[1] == coords[1])) {
                    currentPlayer.setMove(coords[0], coords[1]);
                    board.setCanMove(true);
                }
            }
            board.setCanShoreUp(false);
            candidates.clear();
        } else {
            board.setCanMove(false);
            board.setCanShoreUp(false);
        }
    }

    /**
     * 执行移动操作
     */
    public static void moveTo() {
        Adventurer currentPlayer = adventurers[Game.getRoundNum()];
        board.getTile(currentPlayer.getX(), currentPlayer.getY()).moveOff(currentPlayer);
        currentPlayer.move();
        board.getTile(currentPlayer.getX(), currentPlayer.getY()).moveOnto(currentPlayer.getId());
    }

    /**
     * 执行加固操作
     */
    public static void shoreUp() {
        Adventurer currentPlayer = adventurers[Game.getRoundNum()];
        board.getTile(currentPlayer.getShoreUpX(), currentPlayer.getShoreUpY()).shoreUp();
    }

    /**
     * 将卡片传递给其他玩家
     * @return 是否成功传递
     */
    public static boolean passCardTo() {
        if (selectedPawn != NO_SELECTION && selectedPawn >= 0 &&
                selectedPawn < adventurers.length && cardsInRound.size() != 0) {
            ArrayList<Integer> subList = new ArrayList<>(cardsInRound.subList(0, 1));

            // 如果接收方手牌已满，需要先丢弃
            if (adventurers[selectedPawn].getHandCards().size() == 5) {
                LogManager.logMessage("玩家手牌已满，请在接收卡片前丢弃一张卡片");
                cardsInRound.clear();
                Game.setInFakeRound(true);
                Game.setFakeRoundNum(Game.getRoundNum());
                Game.setFakeActionCount(Game.getActionCount());
                Game.setRoundNum(selectedPawn);
                Game.setActionCount(3);
                LogManager.logMessage("请选择需要丢弃的卡片，然后重新尝试[传递卡片]");
                return false;
            } else {
                // 传递卡片给选中的玩家
                adventurers[selectedPawn].setHandCards(subList);
                adventurers[Game.getRoundNum()].getHandCards().remove(Integer.valueOf(cardsInRound.get(0)));
                return true;
            }
        } else {
            LogManager.logMessage("请选择一张卡片和一个要传递给的玩家");
            return false;
        }
    }

    // Getter方法
    public static BoardData getBoard() {
        return board;
    }

    public static ArrayList<Integer> getTiles() {
        return tiles;
    }

    public static FloodDeck getFloodDeck() {
        return floodDeck;
    }

    public static TreasureDeck getTreasureDeck() {
        return treasureDeck;
    }

    public static WaterMeter getWaterMeter() {
        return waterMeter;
    }

    public static String getWaterMeterImage() {
        return waterMeter.getImage();
    }

    public static Adventurer[] getAdventurers() {
        return adventurers;
    }

    public static int getFloodCardCount() {
        return waterMeter.getFloodCardCount();
    }

    public static ArrayList<Integer> getDisplayedTreasureCards() {
        return displayedTreasureCards;
    }

    public static ArrayList<Integer> getCardsInRound() {
        return cardsInRound;
    }

    public static void resetCardsInRound() {
        cardsInRound.clear();
    }

    public static int[] getSpecialActionTile() {
        return specialActionTile;
    }

    public static void resetSpecialActionTile() {
        specialActionTile[0] = -1;
        specialActionTile[1] = -1;
    }

    public static int getSelectedPawn() {
        return selectedPawn;
    }

    public static ArrayList<Integer> getSelectedPawns() {
        return selectedPawns;
    }

    /**
     * 检查是否有选择的玩家
     * @return 是否有选择玩家
     */
    public static boolean hasSelectedPawn() {
        return selectedPawn != NO_SELECTION;
    }
}