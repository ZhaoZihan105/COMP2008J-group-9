package org.example.ForbiddenIsland.gui;

import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import org.example.ForbiddenIsland.gui.console.ConsolePane;
import org.example.ForbiddenIsland.gui.game.BoardPane;
import org.example.ForbiddenIsland.gui.game.FloodPane;
import org.example.ForbiddenIsland.gui.game.PlayerPane;
import org.example.ForbiddenIsland.gui.game.TreasurePane;
import org.example.ForbiddenIsland.gui.updater.*;
import org.example.ForbiddenIsland.utils.Constants;

import java.io.File;
public class GamePane extends BorderPane {
    private PlayerPane playerPaneUp;
    private PlayerPane playerPaneDown;
    private BoardPane boardPane;
    private TreasurePane treasurePane;
    private FloodPane floodPane;
    private ConsolePane consolePane;

    public GamePane() {
        setPadding(new Insets(5));
        setStyle("-fx-background-color: #c58e8e;");

        initPanels();

        // 顶部/底部布局（上下玩家）
        VBox topBox = new VBox(playerPaneUp);
        VBox bottomBox = new VBox(playerPaneDown);
        topBox.setPrefHeight(120);  // 确保有足够的高度显示所有内容
        bottomBox.setPrefHeight(120);
        VBox.setVgrow(playerPaneUp, Priority.NEVER);
        VBox.setVgrow(playerPaneDown, Priority.NEVER);
        setTop(topBox);
        setBottom(bottomBox);

        // 左侧宝物面板
        VBox leftBox = new VBox(treasurePane);
        VBox.setVgrow(treasurePane, Priority.NEVER);  // 禁止自动扩展
        treasurePane.setMaxWidth(Double.MAX_VALUE);
        setLeft(leftBox);

        // 右侧水位和控制台
        VBox rightBox = new VBox();
        rightBox.getChildren().addAll(floodPane, consolePane);
        VBox.setVgrow(floodPane, Priority.NEVER);
        VBox.setVgrow(consolePane, Priority.ALWAYS);
        floodPane.setMaxHeight(120);
        consolePane.setMaxHeight(Double.MAX_VALUE);
        rightBox.setSpacing(5);  // 减小间距
        setRight(rightBox);

        // 中央地图区域 - 大幅减小高度
        StackPane centerStack = new StackPane();

        // 设置中央区域的固定尺寸，高度大幅减小
        int mapHeight = 350;  // 自定义高度，比Constants中的还要小
        centerStack.setPrefSize(Constants.BOARD_WIDTH, mapHeight);
        centerStack.setMaxSize(Constants.BOARD_WIDTH, mapHeight);

        // 设置游戏板固定尺寸
        boardPane.setPrefSize(Constants.BOARD_WIDTH, mapHeight);
        boardPane.setMaxSize(Constants.BOARD_WIDTH, mapHeight);

        // 背景图使用固定尺寸
        ImageView bgImage = new ImageView(new Image(
                new File("src/main/java/org/example/ForbiddenIsland/image/board/board_bg.jpg").toURI().toString()));
        bgImage.setFitWidth(Constants.BOARD_WIDTH);
        bgImage.setFitHeight(mapHeight);
        bgImage.setPreserveRatio(false);  // 确保完全匹配指定尺寸

        centerStack.getChildren().addAll(bgImage, boardPane);
        StackPane.setMargin(boardPane, new Insets(2));  // 减小边距

        // 禁用滚轮缩放，防止改变尺寸
        // centerStack.setOnScroll(this::handleZoom);

        setCenter(centerStack);

        setupUpdaters();
    }

    private void initPanels() {
        playerPaneUp = new PlayerPane();
        playerPaneDown = new PlayerPane();
        boardPane = new BoardPane();
        treasurePane = new TreasurePane();
        floodPane = new FloodPane();
        consolePane = new ConsolePane();
    }

    private void setupUpdaters() {
        BoardUpdater.setBoardPane(boardPane);
        PlayerUpdater.setPlayerPane(playerPaneUp);
        PlayerUpdater.setPlayerPane(playerPaneDown);
        TreasureUpdater.setTreasurePane(treasurePane);
        FloodUpdater.setFloodPane(floodPane);
        WaterMeterUpdater.setWaterMeterImageView(treasurePane.getWaterMeterImageView());
        ControlsUpdater.setConsolePane(consolePane);
    }

    // 滚轮缩放（保留但可能不再使用）
    private void handleZoom(ScrollEvent event) {
        double zoomFactor = event.getDeltaY() > 0 ? 1.1 : 0.9;
        boardPane.setScaleX(boardPane.getScaleX() * zoomFactor);
        boardPane.setScaleY(boardPane.getScaleY() * zoomFactor);
        event.consume();
    }
}