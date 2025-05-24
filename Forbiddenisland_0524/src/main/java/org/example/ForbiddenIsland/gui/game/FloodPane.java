package org.example.ForbiddenIsland.gui.game;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.example.ForbiddenIsland.utils.Constants;

import java.io.File;
import java.util.ArrayList;

/**
 * 洪水卡面板
 * 显示洪水卡牌堆和抽出的洪水卡
 */
public class FloodPane extends VBox {
    // 洪水卡按钮列表
    private final ArrayList<Button> floodCards = new ArrayList<>();

    /**
     * 初始化洪水卡面板
     */
    public FloodPane() {
        // 设置样式和大小
        setSpacing(3);
        setPadding(new Insets(5));
        setPrefWidth(Constants.FLOOD_WIDTH + 20);

        // 添加弃牌堆标签
        Label discardPileLabel = new Label("洪水弃牌堆");
        getChildren().add(discardPileLabel);

        // 添加弃牌堆图像
        ImageView discardPileImage = new ImageView(new Image(
                new File("src/main/java/org/example/ForbiddenIsland/image/flood/discard.png").toURI().toString()));

        discardPileImage.setFitWidth(Constants.FLOOD_WIDTH);
        discardPileImage.setFitHeight(Constants.FLOOD_HEIGHT);
        getChildren().add(discardPileImage);

        // 添加牌堆标签
        Label deckLabel = new Label("洪水牌堆");
        getChildren().add(deckLabel);

        // 添加牌堆图像
        ImageView deckImage = new ImageView(new Image(
                new File("src/main/java/org/example/ForbiddenIsland/image/flood/deck.png").toURI().toString()));
        deckImage.setFitWidth(Constants.FLOOD_WIDTH);
        deckImage.setFitHeight(Constants.FLOOD_HEIGHT);
        getChildren().add(deckImage);

        // 创建6个洪水卡按钮（最多同时显示6张洪水卡）
        for (int i = 0; i < 6; i++) {
            Button cardButton = new Button();
            cardButton.setPrefSize(Constants.FLOOD_WIDTH, Constants.FLOOD_HEIGHT);
            cardButton.setVisible(false);

            floodCards.add(cardButton);
            getChildren().add(cardButton);
        }
    }

    /**
     * 获取洪水卡按钮列表
     * @return 洪水卡按钮列表
     */
    public ArrayList<Button> getFloodCards() {
        return floodCards;
    }
}