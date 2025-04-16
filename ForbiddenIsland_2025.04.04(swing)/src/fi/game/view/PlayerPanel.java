package fi.game.view;

import fi.game.controller.*;
import fi.game.model.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import javax.imageio.ImageIO;

/**
 * 玩家面板，用于显示玩家信息和卡牌
 */
public class PlayerPanel extends JPanel {
    private List<PlayerView> playerViews;
    private GameController controller;

    // 选择跟踪
    private PlayerView selectedPlayerView;

    // 图像资源
    private Map<Role, ImageIcon> roleImages;
    private Map<CardType, ImageIcon> cardImages;

    public PlayerPanel() {
        setPreferredSize(new Dimension(250, 450));
        setBorder(BorderFactory.createTitledBorder("玩家"));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

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
                String imagePath = "resources/roles/" + role.name().toLowerCase() + ".png";
                Image image = ImageIO.read(getClass().getClassLoader().getResourceAsStream(imagePath));
                roleImages.put(role, new ImageIcon(image));
            } catch (Exception e) {
                System.err.println("无法加载角色图像: " + role.name() + ", " + e.getMessage());
                // 创建默认图像
                BufferedImage defaultImage = new BufferedImage(40, 40, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = defaultImage.createGraphics();
                g.setColor(getRoleColor(role));
                g.fillOval(0, 0, 40, 40);
                g.dispose();
                roleImages.put(role, new ImageIcon(defaultImage));
            }
        }

        // 加载卡片图像
        for (CardType type : CardType.values()) {
            try {
                String imagePath = "resources/cards/" + type.name().toLowerCase() + ".png";
                Image image = ImageIO.read(getClass().getClassLoader().getResourceAsStream(imagePath));
                cardImages.put(type, new ImageIcon(image));
            } catch (Exception e) {
                System.err.println("无法加载卡片图像: " + type.name() + ", " + e.getMessage());
                // 创建默认图像
                BufferedImage defaultImage = new BufferedImage(45, 70, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = defaultImage.createGraphics();
                g.setColor(getCardColor(type));
                g.fillRect(0, 0, 45, 70);
                g.setColor(Color.BLACK);
                g.drawRect(0, 0, 44, 69);
                g.drawString(type.name(), 5, 35);
                g.dispose();
                cardImages.put(type, new ImageIcon(defaultImage));
            }
        }
    }

    /**
     * 获取卡片颜色
     */
    private Color getCardColor(CardType type) {
        switch (type) {
            case EARTH: return new Color(139, 69, 19); // 棕色
            case WIND: return new Color(169, 169, 169); // 银色
            case FIRE: return new Color(220, 20, 60); // 红色
            case WATER: return new Color(30, 144, 255); // 蓝色
            case WATERS_RISE: return new Color(0, 0, 139); // 深蓝色
            case HELICOPTER: return new Color(255, 215, 0); // 金色
            case SANDBAG: return new Color(210, 180, 140); // 棕褐色
            default: return Color.GRAY;
        }
    }

    /**
     * 更新玩家显示
     */
    public void updatePlayers(List<Player> players, Player currentPlayer) {
        removeAll();
        playerViews.clear();

        // 为每个玩家创建视图
        for (Player player : players) {
            PlayerView playerView = new PlayerView(player, player == currentPlayer);
            playerViews.add(playerView);
            add(playerView);

            // 添加一些面板之间的间距
            add(Box.createRigidArea(new Dimension(0, 10)));
        }

        revalidate();
        repaint();
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
                break;
            }
        }

        // 通知控制器
        if (controller != null && selectedPlayerView != null) {
            controller.onPlayerSelected(selectedPlayerView.getPlayer());
        }

        repaint();
    }

    /**
     * 清除选择
     */
    public void clearSelection() {
        if (selectedPlayerView != null) {
            selectedPlayerView.setSelected(false);
            selectedPlayerView = null;
            repaint();
        }
    }

    /**
     * 设置游戏控制器
     */
    public void setGameController(GameController controller) {
        this.controller = controller;

        // 为所有玩家视图设置鼠标监听器
        for (PlayerView playerView : playerViews) {
            playerView.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    selectPlayer(playerView.getPlayer());
                }
            });
        }
    }

    /**
     * 获取角色对应的颜色
     */
    private Color getRoleColor(Role role) {
        switch (role) {
            case ENGINEER: return Color.RED;
            case EXPLORER: return Color.GREEN;
            case DIVER: return Color.BLACK;
            case PILOT: return Color.BLUE;
            case MESSENGER: return Color.LIGHT_GRAY;
            case NAVIGATOR: return Color.YELLOW;
            default: return Color.GRAY;
        }
    }

    /**
     * 图片缩放方法
     */
    private ImageIcon getScaledImage(ImageIcon icon, int width, int height) {
        Image img = icon.getImage();
        Image scaledImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImg);
    }

    /**
     * 玩家视图内部类
     */
    class PlayerView extends JPanel {
        private Player player;
        private boolean isCurrentPlayer;
        private boolean selected;

        public PlayerView(Player player, boolean isCurrentPlayer) {
            this.player = player;
            this.isCurrentPlayer = isCurrentPlayer;
            this.selected = false;

            setPreferredSize(new Dimension(230, 100));
            setBorder(BorderFactory.createLineBorder(Color.BLACK));
            setLayout(new BorderLayout());

            // 设置组件
            JPanel headerPanel = new JPanel(new BorderLayout());
            headerPanel.setBackground(getRoleColor(player.getRole()));

            JLabel roleLabel = new JLabel(player.getRole().getTitle(), JLabel.CENTER);
            roleLabel.setForeground(Color.WHITE);
            headerPanel.add(roleLabel, BorderLayout.CENTER);

            JPanel cardsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            for (TreasureCard card : player.getHand()) {
                JLabel cardLabel = new JLabel();
                ImageIcon cardIcon = cardImages.get(card.getType());
                if (cardIcon != null) {
                    // 缩放图片并保持比例
                    ImageIcon scaledIcon = getScaledImage(cardIcon, 45, 70);
                    cardLabel.setIcon(scaledIcon);
                } else {
                    cardLabel.setText(card.getName());
                }
                cardLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                cardLabel.setPreferredSize(new Dimension(45, 70));
                cardLabel.setHorizontalAlignment(JLabel.CENTER);
                cardLabel.setVerticalAlignment(JLabel.CENTER);
                cardsPanel.add(cardLabel);

                // 为卡片添加鼠标监听器
                cardLabel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (controller != null) {
                            controller.onCardSelected(card);
                        }
                    }
                });
            }

            add(headerPanel, BorderLayout.NORTH);
            add(cardsPanel, BorderLayout.CENTER);

            // 为玩家选择添加鼠标监听器
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (controller != null) {
                        controller.onPlayerSelected(player);
                    }
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // 如果这是当前玩家，绘制高亮
            if (isCurrentPlayer) {
                g.setColor(Color.RED);
                g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
                g.drawRect(1, 1, getWidth() - 3, getHeight() - 3);
            }

            // 绘制选择边框
            if (selected) {
                g.setColor(Color.BLUE);
                g.drawRect(2, 2, getWidth() - 5, getHeight() - 5);
                g.drawRect(3, 3, getWidth() - 7, getHeight() - 7);
            }
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        public Player getPlayer() {
            return player;
        }
    }
}