package org.example.ForbiddenIsland.gui.game;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;       // 加这个：负责加载图片资源
import java.io.File;                   // 加这个：加载本地文件用的
import org.example.ForbiddenIsland.service.game.GameData;
import org.example.ForbiddenIsland.utils.Constants;
import org.example.ForbiddenIsland.utils.ImageLoader;

import java.util.ArrayList;

/**
 * 宝藏卡面板
 * 显示宝藏卡牌堆和水位计
 */
public class TreasurePane extends BorderPane {
    // 宝藏卡按钮列表
    private final ArrayList<Button> treasureCards = new ArrayList<>();

    // 水位计图像视图
    private final ImageView waterMeterImageView;

    /**
     * 初始化宝藏卡面板
     */
    public TreasurePane() {
        // 设置样式和大小
        setPadding(new Insets(5));
        setPrefWidth(Constants.TREASURE_WIDTH + 20);

        // 创建宝藏卡区域
        VBox treasureCardArea = createTreasureCardArea();
        setTop(treasureCardArea);

        // 创建水位计
        waterMeterImageView = new ImageView();
        waterMeterImageView.setFitWidth(Constants.WATER_METER_WIDTH);
        waterMeterImageView.setFitHeight(Constants.WATER_METER_HEIGHT);
        setCenter(waterMeterImageView);
    }

    /**
     * 创建宝藏卡区域
     * @return 宝藏卡区域
     */
    private VBox createTreasureCardArea() {
        VBox treasureCardArea = new VBox(3);
        treasureCardArea.setPadding(new Insets(5));

        // 添加弃牌堆标签
        Label discardPileLabel = new Label("弃牌堆");
        treasureCardArea.getChildren().add(discardPileLabel);

        // 添加弃牌堆图像
        ImageView discardPileImage = new ImageView(new Image(
                new File("src/main/java/org/example/ForbiddenIsland/image/treasure_cards/discard.png").toURI().toString()));

        discardPileImage.setFitWidth(Constants.TREASURE_WIDTH);
        discardPileImage.setFitHeight(Constants.TREASURE_HEIGHT);
        treasureCardArea.getChildren().add(discardPileImage);

        // 添加牌堆标签
        Label deckLabel = new Label("牌堆");
        treasureCardArea.getChildren().add(deckLabel);

        // 添加牌堆图像
        ImageView deckImage = new ImageView(new Image(
                new File("src/main/java/org/example/ForbiddenIsland/image/treasure_cards/deck.png").toURI().toString()));
        deckImage.setFitWidth(Constants.TREASURE_WIDTH);
        deckImage.setFitHeight(Constants.TREASURE_HEIGHT);
        treasureCardArea.getChildren().add(deckImage);

        // 创建2个宝藏卡按钮
        for (int i = 0; i < 2; i++) {
            Button cardButton = new Button();
            cardButton.setPrefSize(Constants.TREASURE_WIDTH, Constants.TREASURE_HEIGHT);

            final int cardIndex = i;
            cardButton.setOnAction(e -> GameData.selectTreasureCard(false, cardIndex));

            treasureCards.add(cardButton);
            treasureCardArea.getChildren().add(cardButton);
        }

        return treasureCardArea;
    }

    /**
     * 获取宝藏卡按钮列表
     * @return 宝藏卡按钮列表
     */
    public ArrayList<Button> getTreasureCards() {
        return treasureCards;
    }

    /**
     * 获取水位计图像视图
     * @return 水位计图像视图
     */
    public ImageView getWaterMeterImageView() {
        return waterMeterImageView;
    }
}