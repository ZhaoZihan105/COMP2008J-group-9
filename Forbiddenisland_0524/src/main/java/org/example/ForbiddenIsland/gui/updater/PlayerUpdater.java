package org.example.ForbiddenIsland.gui.updater;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.example.ForbiddenIsland.gui.game.PlayerPane;
import org.example.ForbiddenIsland.service.game.Game;
import org.example.ForbiddenIsland.service.game.GameData;
import org.example.ForbiddenIsland.service.game.component.adventurer.Adventurer;
import org.example.ForbiddenIsland.service.game.component.cards.TreasureFigurines;
import org.example.ForbiddenIsland.utils.Constants;
import org.example.ForbiddenIsland.utils.ImageLoader;

import java.util.ArrayList;

/**
 * 修改后的PlayerUpdater类 - 处理上下两个面板
 */
public class PlayerUpdater implements IUpdater {
    // 修改为分别存储上下面板引用
    private static PlayerPane playerPaneUp;    // 上方面板引用
    private static PlayerPane playerPaneDown;  // 下方面板引用

    /**
     * 设置玩家面板引用
     * @param pane 玩家面板
     */
    public static void setPlayerPane(PlayerPane pane) {
        // 根据面板类型保存到不同的引用
        if (pane.getPanelType() == PlayerPane.TOP_PANEL) {
            playerPaneUp = pane;
            System.out.println("已设置上方面板引用");
        } else {
            playerPaneDown = pane;
            System.out.println("已设置下方面板引用");
        }
    }

    /**
     * 初始化玩家更新器
     * 注意：需要分别初始化上下两个面板
     */
    public PlayerUpdater() {
        // 初始化上方面板
        if (playerPaneUp != null) {
            System.out.println("正在初始化上方面板...");
            initPanelPawns(playerPaneUp);
            initPanelHandCards(playerPaneUp);
        }

        // 初始化下方面板
        if (playerPaneDown != null) {
            System.out.println("正在初始化下方面板...");
            initPanelPawns(playerPaneDown);
            initPanelHandCards(playerPaneDown);
        }
    }

    /**
     * 初始化面板中的玩家棋子
     * @param panel 要初始化的面板
     */
    private void initPanelPawns(PlayerPane panel) {
        // 获取面板类型
        int panelType = panel.getPanelType();
        // 确定起始玩家索引
        int startPlayerIndex = (panelType == PlayerPane.TOP_PANEL) ? 2 : 0;

        System.out.println("初始化" + (panelType == PlayerPane.TOP_PANEL ? "上方" : "下方") +
                "面板，起始玩家索引: " + startPlayerIndex);

        // 对于面板上的每个玩家位置
        for (int i = 0; i < panel.getPlayerPawns().size(); i++) {
            // 计算实际玩家索引
            int playerIndex = startPlayerIndex + i;
            Button pawnButton = panel.getPlayerPawns().get(i);

            // 检查玩家是否存在
            if (Game.getNumOfPlayer() > 0 && playerIndex < Game.getNumOfPlayer() &&
                    GameData.getAdventurers() != null && GameData.getAdventurers()[playerIndex] != null) {

                Adventurer adventurer = GameData.getAdventurers()[playerIndex];
                try {
                    // 加载玩家棋子图像
                    Image pawnImage = ImageLoader.loadScaled(
                            adventurer.getPawnImage(),
                            Constants.ADVENTURER_WIDTH, Constants.ADVENTURER_HEIGHT);

                    pawnButton.setGraphic(new ImageView(pawnImage));
                    pawnButton.setDisable(false);
                    pawnButton.setVisible(true);

                    System.out.println("  显示玩家 " + playerIndex + " (" + adventurer.getName() + ")");
                } catch (Exception e) {
                    System.err.println("  加载玩家 " + playerIndex + " 图像失败: " + e.getMessage());
                    pawnButton.setGraphic(null);
                    pawnButton.setDisable(true);
                    pawnButton.setVisible(false);
                }
            } else {
                // 玩家不存在，隐藏按钮
                pawnButton.setGraphic(null);
                pawnButton.setDisable(true);
                pawnButton.setVisible(false);
                System.out.println("  隐藏玩家位置 " + playerIndex + " (玩家不存在)");
            }
        }
    }

    /**
     * 初始化面板中的玩家手牌
     * @param panel 要初始化的面板
     */
    private void initPanelHandCards(PlayerPane panel) {
        // 获取面板类型
        int panelType = panel.getPanelType();
        // 确定起始玩家索引
        int startPlayerIndex = (panelType == PlayerPane.TOP_PANEL) ? 2 : 0;

        // 对于面板上的每个玩家位置
        for (int i = 0; i < panel.getPlayerHandCards().size(); i++) {
            int playerIndex = startPlayerIndex + i;
            ArrayList<Button> handCards = panel.getPlayerHandCards().get(i);

            // 检查玩家是否存在
            if (Game.getNumOfPlayer() > 0 && playerIndex < Game.getNumOfPlayer() &&
                    GameData.getAdventurers() != null) {

                Adventurer adventurer = GameData.getAdventurers()[playerIndex];

                // 显示该玩家的手牌
                int count = Math.min(handCards.size(), adventurer.getHandCards().size());
                for (int j = 0; j < count; j++) {
                    Button cardButton = handCards.get(j);
                    int cardId = adventurer.getHandCards().get(j);

                    try {
                        Image cardImage = ImageLoader.loadScaled(
                                "treasure_cards/" + cardId + ".png",
                                Constants.ADVENTURER_WIDTH, Constants.ADVENTURER_HEIGHT);

                        cardButton.setGraphic(new ImageView(cardImage));
                        cardButton.setDisable(playerIndex != Game.getRoundNum()); // 只启用当前回合玩家的卡牌
                        cardButton.setVisible(true);
                    } catch (Exception e) {
                        System.err.println("加载卡片图像失败: " + e.getMessage());
                        cardButton.setGraphic(null);
                        cardButton.setDisable(true);
                        cardButton.setVisible(false);
                    }
                }

                // 清除多余的手牌按钮
                for (int j = count; j < handCards.size(); j++) {
                    handCards.get(j).setGraphic(null);
                    handCards.get(j).setDisable(true);
                    handCards.get(j).setVisible(false);
                }
            } else {
                // 玩家不存在，隐藏所有手牌按钮
                for (Button card : handCards) {
                    card.setGraphic(null);
                    card.setDisable(true);
                    card.setVisible(false);
                }
            }
        }
    }

    /**
     * 更新UI - 同时更新上下两个面板
     */
    @Override
    public void updateUI() {
        // 更新上方面板
        if (playerPaneUp != null) {
            updatePanel(playerPaneUp);
        }

        // 更新下方面板
        if (playerPaneDown != null) {
            updatePanel(playerPaneDown);
        }
    }

    /**
     * 更新特定面板的UI
     * @param panel 要更新的面板
     */
    private void updatePanel(PlayerPane panel) {
        Platform.runLater(() -> {
            // 获取面板类型
            int panelType = panel.getPanelType();
            // 确定起始玩家索引
            int startPlayerIndex = (panelType == PlayerPane.TOP_PANEL) ? 2 : 0;

            // 对于面板上的每个玩家位置
            for (int i = 0; i < panel.getPlayerPawns().size(); i++) {
                int playerIndex = startPlayerIndex + i;
                Button pawnButton = panel.getPlayerPawns().get(i);

                // 检查玩家是否存在
                if (Game.getNumOfPlayer() > 0 && playerIndex < Game.getNumOfPlayer() &&
                        GameData.getAdventurers() != null) {

                    Adventurer adventurer = GameData.getAdventurers()[playerIndex];

                    // 更新玩家棋子
                    try {
                        Image pawnImage = ImageLoader.loadScaled(
                                adventurer.getPawnImage(),
                                Constants.ADVENTURER_WIDTH, Constants.ADVENTURER_HEIGHT);

                        // 检查是否持有宝藏
                        if (!adventurer.getCapturedFigurines().isEmpty()) {
                            // 显示宝藏标记
                            ImageView pawnView = new ImageView(pawnImage);
                            ImageView treasureView = new ImageView();

                            TreasureFigurines treasure = adventurer.getCapturedFigurines().get(
                                    adventurer.getCapturedFigurines().size() - 1);

                            Image treasureImage = ImageLoader.loadScaled(
                                    treasure.getImagePath(),
                                    Constants.ADVENTURER_WIDTH / 2, Constants.ADVENTURER_HEIGHT / 2);

                            treasureView.setImage(treasureImage);
                            treasureView.setTranslateX(Constants.ADVENTURER_WIDTH / 4);
                            treasureView.setTranslateY(-Constants.ADVENTURER_HEIGHT / 4);

                            javafx.scene.layout.StackPane stackPane = new javafx.scene.layout.StackPane();
                            stackPane.getChildren().addAll(pawnView, treasureView);
                            pawnButton.setGraphic(stackPane);
                        } else {
                            pawnButton.setGraphic(new ImageView(pawnImage));
                        }
                        pawnButton.setVisible(true);
                        pawnButton.setDisable(false);
                    } catch (Exception e) {
                        System.err.println("更新棋子图像失败: " + e.getMessage());
                        pawnButton.setGraphic(null);
                        pawnButton.setDisable(true);
                        pawnButton.setVisible(false);
                    }

                    // 更新玩家手牌
                    ArrayList<Button> handCards = panel.getPlayerHandCards().get(i);
                    int count = Math.min(handCards.size(), adventurer.getHandCards().size());

                    for (int j = 0; j < count; j++) {
                        Button cardButton = handCards.get(j);
                        int cardId = adventurer.getHandCards().get(j);

                        try {
                            Image cardImage = ImageLoader.loadScaled(
                                    "treasure_cards/" + cardId + ".png",
                                    Constants.ADVENTURER_WIDTH, Constants.ADVENTURER_HEIGHT);

                            cardButton.setGraphic(new ImageView(cardImage));
                            cardButton.setDisable(playerIndex != Game.getRoundNum());
                            cardButton.setVisible(true);
                        } catch (Exception e) {
                            System.err.println("更新卡片图像失败: " + e.getMessage());
                            cardButton.setGraphic(null);
                            cardButton.setDisable(true);
                            cardButton.setVisible(false);
                        }
                    }

                    for (int j = count; j < handCards.size(); j++) {
                        handCards.get(j).setGraphic(null);
                        handCards.get(j).setDisable(true);
                        handCards.get(j).setVisible(false);
                    }
                } else {
                    // 玩家不存在，隐藏所有UI元素
                    pawnButton.setGraphic(null);
                    pawnButton.setDisable(true);
                    pawnButton.setVisible(false);

                    ArrayList<Button> handCards = panel.getPlayerHandCards().get(i);
                    for (Button card : handCards) {
                        card.setGraphic(null);
                        card.setDisable(true);
                        card.setVisible(false);
                    }
                }
            }
        });
    }

    /**
     * 游戏结束处理
     */
    @Override
    public void gameOver() {
        Platform.runLater(() -> {
            // 禁用上方面板
            if (playerPaneUp != null) {
                for (Button pawn : playerPaneUp.getPlayerPawns()) {
                    pawn.setDisable(true);
                }

                for (ArrayList<Button> hands : playerPaneUp.getPlayerHandCards()) {
                    for (Button card : hands) {
                        card.setDisable(true);
                    }
                }
            }

            // 禁用下方面板
            if (playerPaneDown != null) {
                for (Button pawn : playerPaneDown.getPlayerPawns()) {
                    pawn.setDisable(true);
                }

                for (ArrayList<Button> hands : playerPaneDown.getPlayerHandCards()) {
                    for (Button card : hands) {
                        card.setDisable(true);
                    }
                }
            }
        });
    }
}

