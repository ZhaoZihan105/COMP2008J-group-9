package com.example.forbiddenisland_20250415.view;

import com.example.forbiddenisland_20250415.model.*;
import com.example.forbiddenisland_20250415.controller.*;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.layout.Pane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.InputStream;
import java.util.*;
import java.util.List;

/**
 * 岛屿面板，负责显示游戏地图
 */
public class IslandPanel extends TitledPane {
    private List<TileView> tileViews;
    private GameController controller;
    private Pane contentPane;

    // 选择跟踪
    private TileView selectedTileView;

    // 图像资源
    private Map<TileType, Image> tileImages;
    private Map<TileType, Image> floodedTileImages;

    // 背景图像
    private ImageView backgroundImageView;

    public IslandPanel() {
        setText("禁忌岛地图");
        setPrefSize(800, 700);

        contentPane = new Pane();
        contentPane.setStyle("-fx-background-color: linear-gradient(to bottom, #1a3c5a, #0e1c2a);");
        setContent(contentPane);
        setCollapsible(false);

        tileViews = new ArrayList<>();

        // 尝试加载背景图像
        try {
            InputStream bgStream = getClass().getResourceAsStream("/backgrounds/ocean.png");
            if (bgStream != null) {
                Image bgImage = new Image(bgStream);
                backgroundImageView = new ImageView(bgImage);
                backgroundImageView.setFitWidth(700);
                backgroundImageView.setFitHeight(600);
                backgroundImageView.setPreserveRatio(false);
                contentPane.getChildren().add(backgroundImageView);
                bgStream.close();
            }
        } catch (Exception e) {
            System.err.println("无法加载背景图像: " + e.getMessage());
        }

        // 加载瓦片图像
        loadTileImages();
    }

    /**
     * 加载瓦片图像
     */
    private void loadTileImages() {
        tileImages = new HashMap<>();
        floodedTileImages = new HashMap<>();

        for (TileType type : TileType.values()) {
            try {
                String imagePath = "/tiles/" + type.name().toLowerCase() + ".png";
                String floodedImagePath = "/tiles/" + type.name().toLowerCase() + "_flooded.png";

                // 加载正常瓦片图像
                InputStream imageStream = getClass().getResourceAsStream(imagePath);
                if (imageStream != null) {
                    Image image = new Image(imageStream);
                    tileImages.put(type, image);
                    imageStream.close();
                } else {
                    System.err.println("未找到瓦片图像: " + imagePath);
                }

                // 加载被淹没瓦片图像
                InputStream floodedImageStream = getClass().getResourceAsStream(floodedImagePath);
                if (floodedImageStream != null) {
                    Image floodedImage = new Image(floodedImageStream);
                    floodedTileImages.put(type, floodedImage);
                    floodedImageStream.close();
                } else {
                    System.err.println("未找到被淹没瓦片图像: " + floodedImagePath);
                }

            } catch (Exception e) {
                System.err.println("无法加载瓦片图像: " + type.name() + ", " + e.getMessage());

                // 如果无法加载图像，使用备用的纯色块
                if (!tileImages.containsKey(type)) {
                    tileImages.put(type, createColorImage(Color.FORESTGREEN));
                }
                if (!floodedTileImages.containsKey(type)) {
                    floodedTileImages.put(type, createColorImage(Color.CORNFLOWERBLUE));
                }
            }
        }
    }

    /**
     * 创建一个纯色图像作为备用
     */
    private Image createColorImage(Color color) {
        return new Image("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8z8BQDwAEhQGAhKmMIQAAAABJRU5ErkJggg==", 1, 1, true, true);
    }

    /**
     * 更新岛屿显示
     */
    public void updateIsland(List<Tile> tiles) {
        // 保留背景图像
        contentPane.getChildren().clear();
        if (backgroundImageView != null) {
            contentPane.getChildren().add(backgroundImageView);
        }

        tileViews.clear();

        // 为每个瓦片创建视图
        for (Tile tile : tiles) {
            if (tile.getState() != TileState.SUNK) {
                TileView tileView = new TileView(tile);
                tileViews.add(tileView);
                contentPane.getChildren().add(tileView);

                // 添加渐入动画
                FadeTransition fadeIn = new FadeTransition(Duration.millis(300), tileView);
                fadeIn.setFromValue(0.0);
                fadeIn.setToValue(1.0);
                fadeIn.play();

                // 添加瓦片点击监听器
                tileView.setOnMouseClicked(e -> selectTile(tileView));

                // 添加悬停效果
                tileView.setOnMouseEntered(e -> {
                    if (tileView != selectedTileView) {
                        tileView.setHoverEffect(true);

                        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(150), tileView);
                        scaleUp.setToX(1.05);
                        scaleUp.setToY(1.05);
                        scaleUp.play();
                    }
                });

                tileView.setOnMouseExited(e -> {
                    if (tileView != selectedTileView) {
                        tileView.setHoverEffect(false);
                        ScaleTransition scaleDown = new ScaleTransition(Duration.millis(150), tileView);
                        scaleDown.setToX(1.0);
                        scaleDown.setToY(1.0);
                        scaleDown.play();
                    }
                });
            }
        }
    }

    /**
     * 选择瓦片
     */
    private void selectTile(TileView tileView) {
        // 取消选择先前的瓦片
        if (selectedTileView != null) {
            selectedTileView.setSelected(false);

            // 恢复原始大小
            ScaleTransition scaleDown = new ScaleTransition(Duration.millis(150), selectedTileView);
            scaleDown.setToX(1.0);
            scaleDown.setToY(1.0);
            scaleDown.play();
        }

        // 选择新瓦片
        selectedTileView = tileView;
        selectedTileView.setSelected(true);

        // 选中瓦片放大
        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(150), selectedTileView);
        scaleUp.setToX(1.1);
        scaleUp.setToY(1.1);
        scaleUp.play();

        // 通知控制器
        if (controller != null) {
            controller.onTileSelected(tileView.getTile());
        }
    }

    /**
     * 清除选择
     */
    public void clearSelection() {
        if (selectedTileView != null) {
            selectedTileView.setSelected(false);

            // 恢复原始大小
            ScaleTransition scaleDown = new ScaleTransition(Duration.millis(150), selectedTileView);
            scaleDown.setToX(1.0);
            scaleDown.setToY(1.0);
            scaleDown.play();

            selectedTileView = null;
        }
    }

    /**
     * 设置游戏控制器
     */
    public void setGameController(GameController controller) {
        this.controller = controller;
    }

    /**
     * 获取角色对应的颜色
     */
    private Color getRoleColor(Role role) {
        switch (role) {
            case ENGINEER: return Color.rgb(231, 76, 60); // 红色
            case EXPLORER: return Color.rgb(46, 204, 113); // 绿色
            case DIVER: return Color.rgb(52, 73, 94); // 深蓝/黑色
            case PILOT: return Color.rgb(52, 152, 219); // 蓝色
            case MESSENGER: return Color.rgb(189, 195, 199); // 浅灰色
            case NAVIGATOR: return Color.rgb(241, 196, 15); // 黄色
            default: return Color.GRAY;
        }
    }

    /**
     * 瓦片视图内部类
     */
    class TileView extends Pane {
        private Tile tile;
        private boolean selected;
        private boolean hover;
        private ImageView imageView;
        private Rectangle selectionBorder;
        private Text nameLabel;
        private List<Circle> playerMarkers = new ArrayList<>();

        // 特效
        private DropShadow shadow;
        private Glow glow;

        public TileView(Tile tile) {
            this.tile = tile;
            this.selected = false;
            this.hover = false;

            // 基于瓦片坐标设置大小和位置
            int tileSize = 90;
            int spacing = 15;

            // 布局计算
            int row = tile.getRow();
            int col = tile.getCol();

            int x = col * (tileSize + spacing) + 20;
            int y = row * (tileSize + spacing) + 20;

            setLayoutX(x);
            setLayoutY(y);
            setPrefSize(tileSize, tileSize);

            // 创建阴影效果
            shadow = new DropShadow();
            shadow.setColor(Color.BLACK);
            shadow.setRadius(10);
            shadow.setOffsetX(3);
            shadow.setOffsetY(3);

            // 创建发光效果
            glow = new Glow(0.3);

            // 设置特效
            setEffect(shadow);

            // 创建和添加图像视图
            imageView = new ImageView();
            imageView.setFitWidth(tileSize);
            imageView.setFitHeight(tileSize);
            imageView.setPreserveRatio(false);

            // 创建选择边框
            selectionBorder = new Rectangle(tileSize, tileSize);
            selectionBorder.setFill(null);
            selectionBorder.setStroke(Color.RED);
            selectionBorder.setStrokeWidth(3);
            selectionBorder.setVisible(false);
            selectionBorder.setArcWidth(10);
            selectionBorder.setArcHeight(10);

            // 创建名称标签
            nameLabel = new Text(tile.getName());
            nameLabel.setFill(Color.WHITE);
            nameLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
            nameLabel.setX(5);
            nameLabel.setY(tileSize - 10);

            // 添加名称标签的阴影以提高可读性
            DropShadow textShadow = new DropShadow();
            textShadow.setColor(Color.BLACK);
            textShadow.setRadius(2);
            nameLabel.setEffect(textShadow);

            getChildren().addAll(imageView, selectionBorder, nameLabel);

            updateTile();
        }

        /**
         * 更新瓦片显示
         */
        private void updateTile() {
            // 更新图像
            TileType tileType = tile.getType();

            if (tile.getState() == TileState.FLOODED) {
                Image image = floodedTileImages.get(tileType);
                if (image != null) {
                    imageView.setImage(image);
                    setStyle("-fx-border-color: #3498db; -fx-border-width: 2px; -fx-border-radius: 10px;");
                } else {
                    // 如果没有找到图像，使用备用方法
                    setStyle("-fx-background-color: linear-gradient(to bottom, #3498db, #2980b9); -fx-background-radius: 10px; -fx-border-radius: 10px;");
                }
            } else {
                Image image = tileImages.get(tileType);
                if (image != null) {
                    imageView.setImage(image);
                    setStyle("-fx-border-color: #16a085; -fx-border-width: 2px; -fx-border-radius: 10px;");
                } else {
                    // 如果没有找到图像，使用备用方法
                    setStyle("-fx-background-color: linear-gradient(to bottom, #27ae60, #16a085); -fx-background-radius: 10px; -fx-border-radius: 10px;");
                }
            }

            // 更新选择状态
            if (selected) {
                selectionBorder.setVisible(true);
            } else {
                selectionBorder.setVisible(false);
            }

            // 更新玩家标记
            updatePlayerMarkers();
        }

        /**
         * 更新玩家标记
         */
        private void updatePlayerMarkers() {
            // 清除现有的玩家标记
            getChildren().removeAll(playerMarkers);
            playerMarkers.clear();

            // 添加新的玩家标记
            List<Player> playersOnTile = tile.getPlayersOnTile();
            int playerCircleSize = 18;
            int spacing = 6;
            int startX = 5;
            int startY = 5;

            for (int i = 0; i < playersOnTile.size(); i++) {
                Player player = playersOnTile.get(i);
                Circle circle = new Circle(
                        startX + playerCircleSize/2 + i * (playerCircleSize + spacing),
                        startY + playerCircleSize/2,
                        playerCircleSize/2
                );
                circle.setFill(getRoleColor(player.getRole()));
                circle.setStroke(Color.BLACK);
                circle.setStrokeWidth(2);

                // 添加玩家标记的阴影
                DropShadow markerShadow = new DropShadow();
                markerShadow.setColor(Color.BLACK);
                markerShadow.setRadius(3);
                circle.setEffect(markerShadow);

                playerMarkers.add(circle);
            }

            getChildren().addAll(playerMarkers);
        }

        /**
         * 设置选中状态
         */
        public void setSelected(boolean selected) {
            this.selected = selected;
            if (selected) {
                selectionBorder.setVisible(true);
                // 添加发光效果
                DropShadow glow = new DropShadow();
                glow.setColor(Color.RED);
                glow.setRadius(15);
                glow.setSpread(0.4);
                setEffect(glow);
            } else {
                selectionBorder.setVisible(false);
                setEffect(shadow);
            }
        }

        public void setHoverEffect(boolean hover) {
            this.hover = hover;
            if (hover && !selected) {
                // 添加柔和的发光效果
                DropShadow hoverGlow = new DropShadow();
                hoverGlow.setColor(Color.BLUEVIOLET);
                hoverGlow.setRadius(12);
                hoverGlow.setSpread(0.2);
                setEffect(hoverGlow);
            } else if (!selected) {
                setEffect(shadow);
            }
        }

        /**
         * 获取瓦片
         */
        public Tile getTile() {
            return tile;
        }
    }
}