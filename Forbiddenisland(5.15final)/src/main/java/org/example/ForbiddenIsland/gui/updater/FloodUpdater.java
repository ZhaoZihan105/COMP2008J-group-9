package org.example.ForbiddenIsland.gui.updater;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.example.ForbiddenIsland.gui.game.FloodPane;
import org.example.ForbiddenIsland.service.game.GameData;
import org.example.ForbiddenIsland.utils.Constants;
import org.example.ForbiddenIsland.utils.ImageLoader;

import java.util.ArrayList;


/**
 * 洪水卡更新器
 * 负责更新洪水卡面板的界面
 */
public class FloodUpdater implements IUpdater {
    // 洪水卡面板引用
    private static FloodPane floodPane;

    /**
     * 设置洪水卡面板引用
     * @param pane 洪水卡面板
     */
    public static void setFloodPane(FloodPane pane) {
        floodPane = pane;
    }

    @Override
    public void updateUI() {
        if (floodPane == null) return;

        Platform.runLater(() -> {
            ArrayList<Button> floodCards = floodPane.getFloodCards();
            ArrayList<Integer> drawnCards = GameData.getFloodDeck().getCards();

            // 更新每张洪水卡
            for (int i = 0; i < floodCards.size(); i++) {
                if (i < drawnCards.size()) {
                    Button cardButton = floodCards.get(i);
                    cardButton.setDisable(false);
                    cardButton.setVisible(true);

                    // 设置洪水卡图像（已更换为使用 ImageLoader）
                    Image cardImage = ImageLoader.loadScaled(
                            "flood/" + drawnCards.get(i) + ".png",
                            Constants.FLOOD_WIDTH,
                            Constants.FLOOD_HEIGHT
                    );

                    cardButton.setGraphic(new ImageView(cardImage));
                } else {
                    // 隐藏未使用的卡片按钮
                    floodCards.get(i).setDisable(true);
                    floodCards.get(i).setVisible(false);
                }
            }
        });
    }

    @Override
    public void gameOver() {
        if (floodPane == null) return;

        Platform.runLater(() -> {
            // 禁用所有洪水卡按钮
            for (Button floodCard : floodPane.getFloodCards()) {
                floodCard.setDisable(true);
            }
        });
    }
}
