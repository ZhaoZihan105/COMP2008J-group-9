package com.example.forbiddenisland_20250415.view;

import com.example.forbiddenisland_20250415.model.*;
import com.example.forbiddenisland_20250415.controller.*;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

import java.io.InputStream;
import java.util.*;
import java.util.List;

/**
 * 玩家面板，用于显示玩家信息和卡牌
 */
public class PlayerPanel extends TitledPane {
    private List<PlayerView> playerViews;
    private GameController controller;
    private VBox contentPane;

    // 选择跟踪
    private PlayerView selectedPlayerView;

    // 图像资源
    private Map<Role, Image> roleImages;
    private Map<CardType, Image> cardImages;

    public PlayerPanel() {
        setText("冒险者");
        setPrefWidth(250);
        setPrefHeight(450);

        contentPane = new VBox(15);
        contentPane.setPadding(new Insets(10));
        contentPane.setAlignment(Pos.TOP_CENTER);
        setContent(contentPane);
        setCollapsible(false);

        playerViews = new ArrayList<>();

        // 加载图像
        loadImages();
    }

    /**
     * 加载玩家图像
     */
    private void loadImages() {
        roleImages = new HashMap<>();
        cardImages = new HashMap<>();

        // 加载角色图像
        for (Role role : Role.values()) {
            try {
                String imagePath = "/roles/" + role.name().toLowerCase() + ".png";
                InputStream inputStream = getClass().getResourceAsStream(imagePath);
                if (inputStream != null) {
                    Image image = new Image(inputStream);
                    roleImages.put(role, image);
                    inputStream.close();
                } else {
                    System.err.println("未找到角色图像资源: " + imagePath);
                }
            } catch (Exception e) {
                System.err.println("无法加载角色图像: " + role.name() + ", " + e.getMessage());
            }
        }

        // 加载卡片图像
        for (CardType type : CardType.values()) {
            try {
                String imagePath = "/cards/" + type.name().toLowerCase() + ".png";
                InputStream inputStream = getClass().getResourceAsStream(imagePath);
                if (inputStream != null) {
                    Image image = new Image(inputStream);
                    cardImages.put(type, image);
                    inputStream.close();
                } else {
                    System.err.println("未找到卡片图像资源: " + imagePath);
                }
            } catch (Exception e) {
                System.err.println("无法加载卡片图像: " + type.name() + ", " + e.getMessage());
            }
        }
    }

    /**
     * 获取卡片颜色
     */
    private Color getCardColor(CardType type) {
        switch (type) {
            case EARTH: return Color.rgb(139, 69, 19); // 棕色
            case WIND: return Color.rgb(169, 169, 169); // 银色
            case FIRE: return Color.rgb(220, 20, 60); // 红色
            case WATER: return Color.rgb(30, 144, 255); // 蓝色
            case WATERS_RISE: return Color.rgb(0, 0, 139); // 深蓝色
            case HELICOPTER: return Color.rgb(255, 215, 0); // 金色
            case SANDBAG: return Color.rgb(210, 180, 140); // 棕褐色
            default: return Color.GRAY;
        }
    }

    /**
     * 更新玩家显示
     */
    public void updatePlayers(List<Player> players, Player currentPlayer) {
        contentPane.getChildren().clear();
        playerViews.clear();

        // 添加当前玩家指示器
        if (currentPlayer != null) {
            Label currentPlayerLabel = new Label("当前行动: " + currentPlayer.getRole().getTitle());
            currentPlayerLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
            currentPlayerLabel.setTextFill(Color.WHITE);
            currentPlayerLabel.setPadding(new Insets(0, 0, 5, 0));
            contentPane.getChildren().add(currentPlayerLabel);
        }

        // 为每个玩家创建视图
        for (Player player : players) {
            PlayerView playerView = new PlayerView(player, player == currentPlayer);
            playerViews.add(playerView);

            // 添加渐入动画
            FadeTransition fadeIn = new FadeTransition(Duration.millis(300), playerView);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();

            contentPane.getChildren().add(playerView);

            // 设置玩家视图的点击事件
            playerView.setOnMouseClicked(e -> {
                selectPlayer(player);
            });
        }
    }

    /**
     * 选择玩家
     */
    public void selectPlayer(Player player) {
        // 取消选择先前的玩家
        if (selectedPlayerView != null) {
            selectedPlayerView.setSelected(false);
        }

        // 寻找并选择新玩家
        for (PlayerView playerView : playerViews) {
            if (playerView.getPlayer() == player) {
                selectedPlayerView = playerView;
                selectedPlayerView.setSelected(true);

                // 添加选中动画
                ScaleTransition scaleUp = new ScaleTransition(Duration.millis(150), selectedPlayerView);
                scaleUp.setToX(1.03);
                scaleUp.setToY(1.03);
                scaleUp.play();

                break;
            }
        }

        // 通知控制器
        if (controller != null && selectedPlayerView != null) {
            controller.onPlayerSelected(selectedPlayerView.getPlayer());
        }
    }

    /**
     * 清除选择
     */
    public void clearSelection() {
        if (selectedPlayerView != null) {
            selectedPlayerView.setSelected(false);

            // 恢复原始大小
            ScaleTransition scaleDown = new ScaleTransition(Duration.millis(150), selectedPlayerView);
            scaleDown.setToX(1.0);
            scaleDown.setToY(1.0);
            scaleDown.play();

            selectedPlayerView = null;
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
     * 玩家视图内部类
     */
    class PlayerView extends VBox {
        private Player player;
        private boolean isCurrentPlayer;
        private boolean selected;
        private HBox cardsPane;
        private Label roleLabel;
        private Label actionCountLabel;
        private ImageView roleImageView;

        // 特效
        private DropShadow normalShadow;
        private DropShadow selectedShadow;
        private DropShadow currentPlayerShadow;

        public PlayerView(Player player, boolean isCurrentPlayer) {
            this.player = player;
            this.isCurrentPlayer = isCurrentPlayer;
            this.selected = false;

            setPadding(new Insets(10));
            setSpacing(8);

            // 直接应用样式而不是使用CSS类
            setStyle(
                    "-fx-background-color: #34495e;" +
                            "-fx-background-radius: 5px;" +
                            "-fx-border-radius: 5px;" +
                            "-fx-border-color: #2c3e50;" +
                            "-fx-border-width: 2px;"
            );

            // 如果是当前玩家，修改边框颜色
            if (isCurrentPlayer) {
                setStyle(getStyle().replace("-fx-border-color: #2c3e50;", "-fx-border-color: #e74c3c;"));
            }

            // 创建阴影效果
            normalShadow = new DropShadow();
            normalShadow.setColor(Color.BLACK);
            normalShadow.setRadius(5);
            normalShadow.setOffsetY(2);

            selectedShadow = new DropShadow();
            selectedShadow.setColor(Color.rgb(243, 156, 18));
            selectedShadow.setRadius(10);
            selectedShadow.setSpread(0.4);

            currentPlayerShadow = new DropShadow();
            currentPlayerShadow.setColor(Color.rgb(231, 76, 60));
            currentPlayerShadow.setRadius(10);
            currentPlayerShadow.setSpread(0.4);

            // 设置初始阴影
            setEffect(isCurrentPlayer ? currentPlayerShadow : normalShadow);

            // 创建角色标题栏
            HBox titleBar = new HBox(10);
            titleBar.setPadding(new Insets(8));
            titleBar.setAlignment(Pos.CENTER_LEFT);
            titleBar.setBackground(new Background(new BackgroundFill(
                    getRoleColor(player.getRole()), new CornerRadii(5), Insets.EMPTY
            )));

            // 尝试加载角色图像
            if (roleImages.containsKey(player.getRole())) {
                roleImageView = new ImageView(roleImages.get(player.getRole()));
                roleImageView.setFitWidth(30);
                roleImageView.setFitHeight(30);
                roleImageView.setPreserveRatio(true);

                // 为图像添加内阴影效果
                InnerShadow innerShadow = new InnerShadow();
                innerShadow.setOffsetX(1);
                innerShadow.setOffsetY(1);
                innerShadow.setColor(Color.BLACK);
                roleImageView.setEffect(innerShadow);

                titleBar.getChildren().add(roleImageView);
            }

            roleLabel = new Label(player.getRole().getTitle());
            roleLabel.setTextFill(Color.WHITE);
            roleLabel.setFont(Font.font("System", FontWeight.BOLD, 14));

            // 添加剩余行动次数显示
            VBox roleInfo = new VBox(2);
            roleInfo.getChildren().add(roleLabel);

            actionCountLabel = new Label("行动次数: " + (isCurrentPlayer ? "4/4" : "-"));
            actionCountLabel.setTextFill(Color.WHITE);
            actionCountLabel.setFont(Font.font("System", FontWeight.NORMAL, 10));
            roleInfo.getChildren().add(actionCountLabel);

            titleBar.getChildren().add(roleInfo);
            HBox.setHgrow(roleInfo, Priority.ALWAYS);

            // 添加角色技能提示
            Tooltip roleTooltip = new Tooltip(getRoleDescription(player.getRole()));
            Tooltip.install(titleBar, roleTooltip);

            // 创建卡片标题
            Label cardsTitle = new Label("手牌");
            cardsTitle.setFont(Font.font("System", FontWeight.BOLD, 12));
            cardsTitle.setTextFill(Color.WHITE);
            cardsTitle.setPadding(new Insets(5, 0, 0, 0));
            cardsTitle.setStyle("-fx-underline: true;");

            // 创建卡片区域
            cardsPane = new HBox(8);
            cardsPane.setPadding(new Insets(5));
            cardsPane.setAlignment(Pos.CENTER);
            cardsPane.setStyle(
                    "-fx-background-color: rgba(26, 36, 47, 0.5);" +
                            "-fx-background-radius: 3px;" +
                            "-fx-border-radius: 3px;" +
                            "-fx-border-color: #2c3e50;" +
                            "-fx-border-width: 1px;"
            );

            updateCards();

            // 添加到视图
            getChildren().addAll(titleBar, cardsTitle, cardsPane);

            // 添加鼠标悬停效果
            setOnMouseEntered(e -> {
                if (!selected && !isCurrentPlayer) {
                    // 添加发光效果
                    setEffect(new Glow(0.3));
                }
            });

            setOnMouseExited(e -> {
                if (!selected && !isCurrentPlayer) {
                    setEffect(normalShadow);
                }
            });
        }

        /**
         * 获取角色描述信息
         */
        private String getRoleDescription(Role role) {
            switch (role) {
                case ENGINEER: return "工程师: 可以用一个行动点修复两个相邻的洪水瓦片";
                case EXPLORER: return "探险家: 可以对角线移动和加固瓦片";
                case DIVER: return "潜水员: 可以穿过已沉没的瓦片移动";
                case PILOT: return "飞行员: 每回合可以飞行到任意瓦片一次";
                case MESSENGER: return "信使: 可以将手牌给予任意位置的玩家";
                case NAVIGATOR: return "领航员: 可以移动其他玩家额外的步数";
                default: return "未知角色";
            }
        }

        private void updateCards() {
            cardsPane.getChildren().clear();

            if (player.getHand().isEmpty()) {
                Label noCardsLabel = new Label("无卡牌");
                noCardsLabel.setTextFill(Color.LIGHTGRAY);
                noCardsLabel.setFont(Font.font("System", FontWeight.NORMAL, 12));
                cardsPane.getChildren().add(noCardsLabel);
                return;
            }

            for (TreasureCard card : player.getHand()) {
                VBox cardView = new VBox();
                cardView.setPrefSize(50, 75);
                cardView.setStyle(
                        "-fx-background-color: #2c3e50;" +
                                "-fx-background-radius: 5px;" +
                                "-fx-border-radius: 5px;" +
                                "-fx-border-color: #34495e;" +
                                "-fx-border-width: 1px;"
                );

                // 添加卡片阴影
                DropShadow cardShadow = new DropShadow();
                cardShadow.setColor(Color.BLACK);
                cardShadow.setRadius(3);
                cardShadow.setOffsetY(1);
                cardView.setEffect(cardShadow);

                if (cardImages.containsKey(card.getType())) {
                    ImageView cardImageView = new ImageView(cardImages.get(card.getType()));
                    cardImageView.setFitWidth(48);
                    cardImageView.setFitHeight(60);
                    cardImageView.setPreserveRatio(true);

                    // 添加卡片名称标签
                    Label cardLabel = new Label(getShortCardName(card.getType()));
                    cardLabel.setFont(Font.font("System", FontWeight.BOLD, 10));
                    cardLabel.setTextFill(Color.WHITE);
                    cardLabel.setAlignment(Pos.CENTER);
                    cardLabel.setMaxWidth(Double.MAX_VALUE);
                    cardLabel.setBackground(new Background(new BackgroundFill(
                            Color.rgb(0, 0, 0, 0.7), new CornerRadii(0, 0, 5, 5, false), Insets.EMPTY
                    )));
                    cardLabel.setPadding(new Insets(2));

                    cardView.getChildren().addAll(cardImageView, cardLabel);
                } else {
                    // 创建备用显示
                    Pane colorPane = new Pane();
                    colorPane.setPrefSize(48, 60);
                    colorPane.setBackground(new Background(new BackgroundFill(
                            getCardColor(card.getType()), new CornerRadii(5, 5, 0, 0, false), Insets.EMPTY
                    )));

                    Label nameLabel = new Label(card.getName());
                    nameLabel.setFont(Font.font("System", FontWeight.BOLD, 10));
                    nameLabel.setTextFill(Color.WHITE);
                    nameLabel.setMaxWidth(Double.MAX_VALUE);
                    nameLabel.setAlignment(Pos.CENTER);
                    nameLabel.setBackground(new Background(new BackgroundFill(
                            Color.rgb(0, 0, 0, 0.7), new CornerRadii(0, 0, 5, 5, false), Insets.EMPTY
                    )));
                    nameLabel.setPadding(new Insets(2));

                    cardView.getChildren().addAll(colorPane, nameLabel);
                }

                // 添加卡片信息提示
                Tooltip cardTooltip = new Tooltip(card.getName());
                Tooltip.install(cardView, cardTooltip);

                // 为卡片添加点击事件
                cardView.setOnMouseClicked(e -> {
                    if (controller != null) {
                        controller.onCardSelected(card);
                    }
                    e.consume(); // 防止事件冒泡到玩家视图
                });

                // 为卡片添加悬停效果
                cardView.setOnMouseEntered(event -> {
                    ScaleTransition scale = new ScaleTransition(Duration.millis(150), cardView);
                    scale.setToX(1.1);
                    scale.setToY(1.1);
                    scale.play();
                });

                cardView.setOnMouseExited(event -> {
                    ScaleTransition scale = new ScaleTransition(Duration.millis(150), cardView);
                    scale.setToX(1.0);
                    scale.setToY(1.0);
                    scale.play();
                });

                cardsPane.getChildren().add(cardView);
            }
        }

        /**
         * 获取卡片的简短名称，适合在小标签中显示
         */
        private String getShortCardName(CardType type) {
            switch (type) {
                case EARTH: return "地";
                case WIND: return "风";
                case FIRE: return "火";
                case WATER: return "水";
                case WATERS_RISE: return "水涨";
                case HELICOPTER: return "直升机";
                case SANDBAG: return "沙袋";
                default: return "?";
            }
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
            if (selected) {
                // 更新边框颜色为选中状态的橙色
                setStyle(getStyle().replaceAll("-fx-border-color: #[0-9a-f]{6};", "-fx-border-color: #f39c12;"));
                setEffect(selectedShadow);
            } else {
                // 还原边框颜色
                if (isCurrentPlayer) {
                    setStyle(getStyle().replaceAll("-fx-border-color: #[0-9a-f]{6};", "-fx-border-color: #e74c3c;"));
                    setEffect(currentPlayerShadow);
                } else {
                    setStyle(getStyle().replaceAll("-fx-border-color: #[0-9a-f]{6};", "-fx-border-color: #2c3e50;"));
                    setEffect(normalShadow);
                }
            }
        }

        public Player getPlayer() {
            return player;
        }
    }
}