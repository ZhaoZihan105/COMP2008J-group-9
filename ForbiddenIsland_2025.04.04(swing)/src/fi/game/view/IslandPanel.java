package fi.game.view;

import fi.game.controller.*;
import fi.game.model.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import java.util.List;
import javax.imageio.ImageIO;

/**
 * 岛屿面板，负责显示游戏地图
 */
public class IslandPanel extends JPanel {
    private List<TileView> tileViews;
    private GameController controller;

    // 选择跟踪
    private TileView selectedTileView;

    // 图像资源
    private Map<TileType, ImageIcon> tileImages;
    private Map<TileType, ImageIcon> floodedTileImages;

    public IslandPanel() {
        setPreferredSize(new Dimension(700, 600));
        setBorder(BorderFactory.createTitledBorder("禁忌岛"));
        setLayout(null); // 使用绝对定位
        setBackground(new Color(173, 216, 230)); // 淡蓝色背景

        tileViews = new ArrayList<>();

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
                String imagePath = "resources/tiles/" + type.name().toLowerCase() + ".png";
                String floodedImagePath = "resources/tiles/" + type.name().toLowerCase() + "_flooded.png";

                // 加载正常瓦片图像
                Image image = ImageIO.read(getClass().getClassLoader().getResourceAsStream(imagePath));
                tileImages.put(type, new ImageIcon(image));

                // 加载被淹没瓦片图像
                Image floodedImage = ImageIO.read(getClass().getClassLoader().getResourceAsStream(floodedImagePath));
                floodedTileImages.put(type, new ImageIcon(floodedImage));
            } catch (Exception e) {
                System.err.println("无法加载瓦片图像: " + type.name() + ", " + e.getMessage());

                // 创建默认图像
                BufferedImage defaultImage = new BufferedImage(80, 80, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = defaultImage.createGraphics();
                g.setColor(new Color(34, 139, 34)); // 绿色
                g.fillRect(0, 0, 80, 80);
                g.setColor(Color.BLACK);
                g.drawRect(0, 0, 79, 79);
                g.drawString(type.getName(), 5, 40);
                g.dispose();
                tileImages.put(type, new ImageIcon(defaultImage));

                BufferedImage floodedDefault = new BufferedImage(80, 80, BufferedImage.TYPE_INT_ARGB);
                g = floodedDefault.createGraphics();
                g.setColor(new Color(100, 149, 237)); // 蓝色
                g.fillRect(0, 0, 80, 80);
                g.setColor(Color.BLACK);
                g.drawRect(0, 0, 79, 79);
                g.drawString(type.getName(), 5, 40);
                g.dispose();
                floodedTileImages.put(type, new ImageIcon(floodedDefault));
            }
        }
    }

    /**
     * 更新岛屿显示
     */
    public void updateIsland(List<Tile> tiles) {
        removeAll();
        tileViews.clear();

        // 为每个瓦片创建视图
        for (Tile tile : tiles) {
            if (tile.getState() != TileState.SUNK) {
                TileView tileView = new TileView(tile);
                tileViews.add(tileView);
                add(tileView);

                // 添加瓦片点击监听器
                tileView.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        selectTile(tileView);
                    }
                });
            }
        }

        revalidate();
        repaint();
    }

    /**
     * 选择瓦片
     */
    private void selectTile(TileView tileView) {
        // 取消选择先前的瓦片
        if (selectedTileView != null) {
            selectedTileView.setSelected(false);
        }

        // 选择新瓦片
        selectedTileView = tileView;
        selectedTileView.setSelected(true);

        // 通知控制器
        if (controller != null) {
            controller.onTileSelected(tileView.getTile());
        }

        repaint();
    }

    /**
     * 清除选择
     */
    public void clearSelection() {
        if (selectedTileView != null) {
            selectedTileView.setSelected(false);
            selectedTileView = null;
            repaint();
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
     * 瓦片视图内部类
     */
    class TileView extends JPanel {
        private Tile tile;
        private boolean selected;

        public TileView(Tile tile) {
            this.tile = tile;
            this.selected = false;

            // 基于瓦片坐标设置大小和位置
            int tileSize = 80;
            int spacing = 10;

            // 布局计算
            int row = tile.getRow();
            int col = tile.getCol();

            int x = col * (tileSize + spacing) + 50;
            int y = row * (tileSize + spacing) + 50;

            setBounds(x, y, tileSize, tileSize);
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // 根据状态绘制瓦片图像
            ImageIcon image;
            TileType tileType = tile.getType();

            if (tile.getState() == TileState.FLOODED) {
                image = floodedTileImages.get(tileType);
            } else {
                image = tileImages.get(tileType);
            }

            // 如果有图像则绘制，否则绘制占位符
            if (image != null) {
                g.drawImage(image.getImage(), 0, 0, getWidth(), getHeight(), this);
            } else {
                // 占位符
                g.setColor(tile.getState() == TileState.FLOODED ?
                        new Color(100, 149, 237) : new Color(34, 139, 34));
                g.fillRect(0, 0, getWidth(), getHeight());
                g.setColor(Color.BLACK);
                g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
                g.drawString(tile.getName(), 5, getHeight() / 2);
            }

            // 绘制选择边框
            if (selected) {
                g.setColor(Color.RED);
                g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
                g.drawRect(1, 1, getWidth() - 3, getHeight() - 3);
            }

            // 绘制该瓦片上的玩家标记
            List<Player> playersOnTile = tile.getPlayersOnTile();
            int playerCircleSize = 15;
            int spacing = 5;
            int startX = 5;
            int startY = 5;

            for (int i = 0; i < playersOnTile.size(); i++) {
                Player player = playersOnTile.get(i);
                g.setColor(getRoleColor(player.getRole()));
                g.fillOval(startX + i * (playerCircleSize + spacing), startY,
                        playerCircleSize, playerCircleSize);
                g.setColor(Color.BLACK);
                g.drawOval(startX + i * (playerCircleSize + spacing), startY,
                        playerCircleSize, playerCircleSize);
            }
        }

        /**
         * 设置选中状态
         */
        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        /**
         * 获取瓦片
         */
        public Tile getTile() {
            return tile;
        }
    }
}