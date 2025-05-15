package org.example.ForbiddenIsland.gui.updater;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.example.ForbiddenIsland.gui.game.TreasurePane;
import org.example.ForbiddenIsland.service.game.GameData;
import org.example.ForbiddenIsland.utils.Constants;

import java.util.ArrayList;
import org.example.ForbiddenIsland.utils.ImageLoader;

/**
 * 宝藏卡更新器
 * 负责更新宝藏卡面板的界面
 */
public class TreasureUpdater implements IUpdater {
    // 宝藏卡面板引用
    private static TreasurePane treasurePane;

    /**
     * 设置宝藏卡面板引用
     * @param pane 宝藏卡面板
     */
    public static void setTreasurePane(TreasurePane pane) {
        treasurePane = pane;
    }

    /**
     * 初始化宝藏卡面板
     */
    public TreasureUpdater() {
        if (treasurePane != null) {
            // 初始禁用所有宝藏卡按钮
            for (Button treasureCard : treasurePane.getTreasureCards()) {
                treasureCard.setDisable(true);
                treasureCard.setGraphic(null);
            }
        }
    }

    @Override
    public void updateUI() {
        if (treasurePane == null) return;

        Platform.runLater(() -> {
            ArrayList<Button> treasureCards = treasurePane.getTreasureCards();
            ArrayList<Integer> displayedCards = GameData.getDisplayedTreasureCards();

            // 更新每张展示的宝藏卡
            for (int i = 0; i < displayedCards.size() && i < treasureCards.size(); i++) {
                Button cardButton = treasureCards.get(i);
                cardButton.setDisable(false);

                // 设置宝藏卡图像（旋转90度显示）
                Image cardImage = ImageLoader.loadScaled(
                        "treasure_cards/" + displayedCards.get(i) + ".png",
                        Constants.TREASURE_WIDTH, Constants.TREASURE_HEIGHT);

                ImageView imageView = new ImageView(cardImage);
                imageView.setRotate(270); // JavaFX可以直接旋转ImageView

                cardButton.setGraphic(imageView);
            }

            // 清空剩余未使用的宝藏卡按钮
            for (int i = displayedCards.size(); i < treasureCards.size(); i++) {
                treasureCards.get(i).setGraphic(null);
                treasureCards.get(i).setDisable(true);
            }
        });
    }

    @Override
    public void gameOver() {
        if (treasurePane == null) return;

        Platform.runLater(() -> {
            // 禁用所有宝藏卡按钮
            for (Button treasureCard : treasurePane.getTreasureCards()) {
                treasureCard.setDisable(true);
            }
        });
    }
}