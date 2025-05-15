package org.example.ForbiddenIsland.gui.game;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import org.example.ForbiddenIsland.gui.updater.LogManager;
import org.example.ForbiddenIsland.service.game.GameData;
import org.example.ForbiddenIsland.service.game.Game;
import org.example.ForbiddenIsland.utils.Constants;

import java.util.ArrayList;

/**
 * 玩家面板
 * 显示玩家棋子和手牌
 */
public class PlayerPane extends HBox {
    // 玩家面板类型
    public static final int TOP_PANEL = 0;
    public static final int BOTTOM_PANEL = 1;

    // 面板类型计数器（用于区分上下面板）
    private static int panelCounter = 0;

    // 当前面板类型
    private final int panelType;

    // 玩家棋子按钮列表
    private final ArrayList<Button> playerPawns = new ArrayList<>();

    // 玩家手牌按钮列表（每个玩家最多5张手牌）
    private final ArrayList<ArrayList<Button>> playerHandCards = new ArrayList<>();

    /**
     * 初始化玩家面板
     */
    public PlayerPane() {
        // 确定面板类型（上方或下方）
        panelType = panelCounter++;
        if (panelCounter > 1) panelCounter = 0;

        // 设置样式和间距
        setSpacing(10);
        setPadding(new Insets(5));

        // 修改: 根据面板类型确定应该显示哪些玩家
        int playersToDisplay = 2; // 每个面板显示2个玩家
        int startPlayerIndex = (panelType == TOP_PANEL) ? 2 : 0; // 上面板从玩家2开始，下面板从玩家0开始

        // 为每个要显示的玩家创建一个子面板
        for (int i = 0; i < playersToDisplay; i++) {
            // 确定实际玩家索引
            int playerIndex = startPlayerIndex + i;

            // 创建玩家子面板
            HBox playerBox = createPlayerPanel(playerIndex);
            getChildren().add(playerBox);

            // 添加弹性空间，使玩家面板均匀分布
            if (i < playersToDisplay - 1) {
                HBox spacer = new HBox();
                HBox.setHgrow(spacer, Priority.ALWAYS);
                getChildren().add(spacer);
            }
        }
    }

    /**
     * 创建单个玩家的面板
     * @param playerIndex 玩家索引
     * @return 玩家面板
     */
    private HBox createPlayerPanel(int playerIndex) {
        HBox playerBox = new HBox(5);
        playerBox.setPadding(new Insets(5));

        // 创建玩家棋子按钮
        Button pawnButton = new Button();
        pawnButton.setPrefSize(Constants.ADVENTURER_WIDTH, Constants.ADVENTURER_HEIGHT);

        // 安全的点击事件处理
        pawnButton.setOnAction(e -> {
            // 确保玩家存在
            if (playerIndex < Game.getNumOfPlayer() && GameData.getAdventurers() != null) {
                GameData.selectPawn(playerIndex);
            } else {
                LogManager.logMessage("该位置没有玩家");
            }
        });

        playerPawns.add(pawnButton);
        playerBox.getChildren().add(pawnButton);

        // 创建玩家手牌区域
        HBox handCardsBox = new HBox(2);
        ArrayList<Button> handCards = new ArrayList<>();

        // 创建5个手牌按钮
        for (int i = 0; i < 5; i++) {
            Button cardButton = new Button();
            cardButton.setPrefSize(Constants.ADVENTURER_WIDTH, Constants.ADVENTURER_HEIGHT);

            final int cardIndex = i;
            cardButton.setOnAction(e -> {
                // 安全检查：确保玩家存在、是当前回合玩家、且卡牌索引有效
                if (playerIndex < Game.getNumOfPlayer() &&
                        playerIndex == Game.getRoundNum() &&
                        cardIndex < GameData.getAdventurers()[playerIndex].getHandCards().size()) {
                    GameData.selectTreasureCard(true, cardIndex);
                } else {
                    LogManager.logMessage("无法选择此卡片");
                }
            });

            handCards.add(cardButton);
            handCardsBox.getChildren().add(cardButton);
        }

        playerHandCards.add(handCards);
        playerBox.getChildren().add(handCardsBox);

        return playerBox;
    }

    /**
     * 获取玩家棋子按钮列表
     * @return 棋子按钮列表
     */
    public ArrayList<Button> getPlayerPawns() {
        return playerPawns;
    }

    /**
     * 获取玩家手牌按钮列表
     * @return 手牌按钮列表
     */
    public ArrayList<ArrayList<Button>> getPlayerHandCards() {
        return playerHandCards;
    }

    /**
     * 获取面板类型
     * @return 面板类型（TOP_PANEL或BOTTOM_PANEL）
     */
    public int getPanelType() {
        return panelType;
    }
}