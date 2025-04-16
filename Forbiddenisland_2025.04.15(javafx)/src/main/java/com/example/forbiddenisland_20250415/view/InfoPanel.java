package com.example.forbiddenisland_20250415.view;
// InfoPanel.java (JavaFX version)

import com.example.forbiddenisland_20250415.model.*;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TitledPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Reflection;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 游戏信息面板
 */
class InfoPanel extends TitledPane {
    private WaterMeter waterMeter;
    private GameData gameData;

    // UI组件
    private Label waterLevelLabel;
    private ProgressBar waterLevelBar;
    private GridPane treasuresPane;
    private VBox deckInfoPane;

    // 图像资源
    private Map<Treasure, Image> treasureImages;
    public InfoPanel() {
        setText("游戏状态");
        setPrefWidth(250);

        VBox contentPane = new VBox(20);
        contentPane.setPadding(new Insets(15));
        contentPane.setAlignment(Pos.TOP_CENTER);

        // 初始化组件
        // 水位指示器区域
        VBox waterLevelBox = new VBox(5);
        waterLevelBox.setAlignment(Pos.CENTER);
        waterLevelBox.setStyle(
                "-fx-padding: 10;" +
                        "-fx-spacing: 5;" +
                        "-fx-border-color: #1abc9c;" +
                        "-fx-border-radius: 5px;" +
                        "-fx-background-color: rgba(44, 62, 80, 0.7);" +
                        "-fx-background-radius: 5px;"
        );

        waterLevelLabel = new Label("水位: 1");
        waterLevelLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        waterLevelLabel.setTextFill(Color.WHITE);

        waterLevelBar = new ProgressBar(0.1);
        waterLevelBar.setPrefWidth(220);
        waterLevelBar.setPrefHeight(20);
        waterLevelBar.setStyle("-fx-accent: #2ecc71;"); // 初始颜色为绿色

        // 添加反射效果
        Reflection reflection = new Reflection();
        reflection.setFraction(0.2);
        waterLevelBar.setEffect(reflection);

        waterLevelBox.getChildren().addAll(waterLevelLabel, waterLevelBar);

        // 宝藏区域
        VBox treasureBox = new VBox(10);
        treasureBox.setAlignment(Pos.CENTER);
        treasureBox.setStyle(
                "-fx-padding: 10;" +
                        "-fx-spacing: 5;" +
                        "-fx-border-color: #1abc9c;" +
                        "-fx-border-radius: 5px;" +
                        "-fx-background-color: rgba(44, 62, 80, 0.7);" +
                        "-fx-background-radius: 5px;"
        );

        Label treasuresTitle = new Label("探索宝藏");
        treasuresTitle.setFont(Font.font("System", FontWeight.BOLD, 14));
        treasuresTitle.setTextFill(Color.WHITE);

        treasuresPane = new GridPane();
        treasuresPane.setHgap(10);
        treasuresPane.setVgap(10);
        treasuresPane.setPadding(new Insets(10));
        treasuresPane.setBorder(new Border(new BorderStroke(
                Color.rgb(26, 188, 156), BorderStrokeStyle.SOLID, new CornerRadii(5), BorderWidths.DEFAULT
        )));
        treasuresPane.setBackground(new Background(new BackgroundFill(
                Color.rgb(44, 62, 80, 0.7), new CornerRadii(5), Insets.EMPTY
        )));

        // 添加阴影效果
        DropShadow treasureShadow = new DropShadow();
        treasureShadow.setColor(Color.BLACK);
        treasureShadow.setRadius(5);
        treasureShadow.setOffsetY(2);
        treasuresPane.setEffect(treasureShadow);

        treasureBox.getChildren().addAll(treasuresTitle, treasuresPane);

        // 牌组信息区域
        VBox deckBox = new VBox(10);
        deckBox.setAlignment(Pos.CENTER);
        deckBox.setStyle(
                "-fx-padding: 10;" +
                        "-fx-spacing: 5;" +
                        "-fx-border-color: #1abc9c;" +
                        "-fx-border-radius: 5px;" +
                        "-fx-background-color: rgba(44, 62, 80, 0.7);" +
                        "-fx-background-radius: 5px;"
        );

        Label deckInfoTitle = new Label("牌组信息");
        deckInfoTitle.setFont(Font.font("System", FontWeight.BOLD, 14));
        deckInfoTitle.setTextFill(Color.WHITE);

        deckInfoPane = new VBox(8);
        deckInfoPane.setPadding(new Insets(10));
        deckInfoPane.setBorder(new Border(new BorderStroke(
                Color.rgb(26, 188, 156), BorderStrokeStyle.SOLID, new CornerRadii(5), BorderWidths.DEFAULT
        )));
        deckInfoPane.setBackground(new Background(new BackgroundFill(
                Color.rgb(44, 62, 80, 0.7), new CornerRadii(5), Insets.EMPTY
        )));

        // 添加阴影效果
        DropShadow deckShadow = new DropShadow();
        deckShadow.setColor(Color.BLACK);
        deckShadow.setRadius(5);
        deckShadow.setOffsetY(2);
        deckInfoPane.setEffect(deckShadow);

        // 添加牌组信息
        Label treasureDeckLabel = createInfoLabel("宝藏牌组: 0 张");
        Label floodDeckLabel = createInfoLabel("洪水牌组: 0 张");
        deckInfoPane.getChildren().addAll(treasureDeckLabel, floodDeckLabel);

        deckBox.getChildren().addAll(deckInfoTitle, deckInfoPane);

        // 加载宝藏图像
        loadTreasureImages();

        // 添加宝藏占位符
        int row = 0, col = 0;
        for (Treasure treasure : Treasure.values()) {
            BorderPane treasurePane = new BorderPane();
            treasurePane.setPrefSize(100, 100);
            treasurePane.setBorder(new Border(new BorderStroke(
                    Color.rgb(52, 73, 94), BorderStrokeStyle.SOLID, new CornerRadii(5), BorderWidths.DEFAULT
            )));
            treasurePane.getStyleClass().add("treasure-pane");

            Image treasureImage = treasureImages.get(treasure);
            if (treasureImage != null) {
                ImageView imageView = new ImageView(treasureImage);
                imageView.setFitWidth(80);
                imageView.setFitHeight(80);
                imageView.setPreserveRatio(true);
                treasurePane.setCenter(imageView);

                // 添加宝藏名称
                Label treasureLabel = new Label(treasure.getName());
                treasureLabel.setFont(Font.font("System", FontWeight.BOLD, 10));
                treasureLabel.setTextFill(Color.WHITE);
                treasureLabel.setAlignment(Pos.CENTER);
                treasureLabel.setPrefWidth(100);
                treasureLabel.setStyle("-fx-background-color: rgba(0,0,0,0.5); -fx-padding: 2;");
                treasurePane.setBottom(treasureLabel);
            } else {
                Label fallbackLabel = new Label(treasure.getName());
                fallbackLabel.setTextFill(Color.WHITE);
                fallbackLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
                treasurePane.setCenter(fallbackLabel);
            }

            treasuresPane.add(treasurePane, col, row);

            col++;
            if (col > 1) {
                col = 0;
                row++;
            }
        }

        // 添加组件到面板
        contentPane.getChildren().addAll(
                waterLevelBox,
                treasureBox,
                deckBox
        );

        setContent(contentPane);
        setCollapsible(false);
    }

    /**
     * 创建一个格式一致的信息标签
     */
    private Label createInfoLabel(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("System", FontWeight.NORMAL, 12));
        label.setTextFill(Color.WHITE);
        return label;
    }

    /**
     * 加载宝藏图像
     */
    private void loadTreasureImages() {
        treasureImages = new HashMap<>();

        for (Treasure treasure : Treasure.values()) {
            try {
                // 构造路径：注意前面一定要有斜杠表示从资源根路径开始
                String imagePath = "/treasures/" + treasure.name().toLowerCase() + ".png";
                // 从 resources 中读取资源
                InputStream inputStream = getClass().getResourceAsStream(imagePath);

                if (inputStream == null) {
                    System.err.println("未找到资源文件: " + imagePath);
                    continue;
                }

                Image image = new Image(inputStream);
                treasureImages.put(treasure, image);
                inputStream.close();
            } catch (Exception e) {
                System.err.println("无法加载宝藏图像: " + treasure.name() + ", " + e.getMessage());
            }
        }
    }

    /**
     * 获取宝藏对应的颜色
     */
    private Color getTreasureColor(Treasure treasure) {
        switch (treasure) {
            case THE_EARTH_STONE:
                return Color.rgb(139, 69, 19); // 棕色
            case THE_STATUE_OF_THE_WIND:
                return Color.rgb(169, 169, 169); // 银色
            case THE_CRYSTAL_OF_FIRE:
                return Color.rgb(220, 20, 60); // 红色
            case THE_OCEANS_CHALICE:
                return Color.rgb(30, 144, 255); // 蓝色
            default:
                return Color.GRAY;
        }
    }

    /**
     * 更新信息显示
     */
    public void updateInfo(GameData gameData, WaterMeter waterMeter) {
        this.gameData = gameData;
        this.waterMeter = waterMeter;

        // 更新水位显示
        int level = waterMeter.getLevel();
        double progress = level / 10.0;

        // 动画更新水位
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(waterLevelBar.progressProperty(), waterLevelBar.getProgress())),
                new KeyFrame(Duration.millis(500), new KeyValue(waterLevelBar.progressProperty(), progress))
        );
        timeline.play();

        waterLevelLabel.setText("水位: " + level);

        // 根据水位设置条形颜色
        if (level >= 7) {
            waterLevelBar.setStyle("-fx-accent: #e74c3c;"); // 红色
        } else if (level >= 5) {
            waterLevelBar.setStyle("-fx-accent: #f39c12;"); // 橙色
        } else if (level >= 3) {
            waterLevelBar.setStyle("-fx-accent: #3498db;"); // 蓝色
        } else {
            waterLevelBar.setStyle("-fx-accent: #2ecc71;"); // 绿色
        }

        // 更新牌组信息
        Label treasureDeckLabel = (Label) deckInfoPane.getChildren().get(0);
        Label floodDeckLabel = (Label) deckInfoPane.getChildren().get(1);

        int treasureDeckSize = gameData.getTreasureDeckSize();
        int floodDeckSize = gameData.getFloodDeckSize();

        treasureDeckLabel.setText("宝藏牌组: " + treasureDeckSize + " 张");
        floodDeckLabel.setText("洪水牌组: " + floodDeckSize + " 张");

        // 如果牌组快用完了，添加警告颜色
        if (treasureDeckSize <= 3) {
            treasureDeckLabel.setTextFill(Color.rgb(231, 76, 60));
            treasureDeckLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
        } else {
            treasureDeckLabel.setTextFill(Color.WHITE);
            treasureDeckLabel.setFont(Font.font("System", FontWeight.NORMAL, 12));
        }

        if (floodDeckSize <= 3) {
            floodDeckLabel.setTextFill(Color.rgb(231, 76, 60));
            floodDeckLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
        } else {
            floodDeckLabel.setTextFill(Color.WHITE);
            floodDeckLabel.setFont(Font.font("System", FontWeight.NORMAL, 12));
        }

        // 更新宝藏状态（显示哪些已被捕获）
        int treasureCount = 0;
        for (Treasure treasure : Treasure.values()) {
            BorderPane treasurePane = (BorderPane) treasuresPane.getChildren().get(treasureCount);

            if (gameData.isTreasureCaptured(treasure)) {
                treasurePane.getStyleClass().add("treasure-captured");

                // 添加发光效果
                DropShadow glow = new DropShadow();
                glow.setColor(Color.rgb(46, 204, 113));
                glow.setRadius(15);
                glow.setSpread(0.7);
                treasurePane.setEffect(glow);
            } else {
                treasurePane.getStyleClass().remove("treasure-captured");
                treasurePane.setEffect(null);
            }

            treasureCount++;
        }
    }
}