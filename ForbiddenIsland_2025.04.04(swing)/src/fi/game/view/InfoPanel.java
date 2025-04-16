package fi.game.view;

import fi.game.model.*;  // 确保正确导入model包中的所有类
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import java.awt.Graphics2D;

/**
 * 游戏信息面板
 */
class InfoPanel extends JPanel {
    private WaterMeter waterMeter;
    private GameData gameData;

    // UI组件
    private JLabel waterLevelLabel;
    private JProgressBar waterLevelBar;
    private JPanel treasuresPanel;
    private JPanel deckInfoPanel;

    // 图像资源
    private Map<Treasure, ImageIcon> treasureImages;

    public InfoPanel() {
        setPreferredSize(new Dimension(250, 600));
        setBorder(BorderFactory.createTitledBorder("游戏信息"));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // 初始化组件
        waterLevelLabel = new JLabel("水位: 1");
        waterLevelBar = new JProgressBar(0, 10);
        waterLevelBar.setValue(1);
        waterLevelBar.setStringPainted(true);

        treasuresPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        treasuresPanel.setBorder(BorderFactory.createTitledBorder("宝藏"));

        deckInfoPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        deckInfoPanel.setBorder(BorderFactory.createTitledBorder("牌组信息"));

        // 加载宝藏图像
        loadTreasureImages();

        // 添加宝藏占位符
        for (Treasure treasure : Treasure.values()) {
            JPanel treasurePanel = new JPanel(new BorderLayout());
            treasurePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            ImageIcon treasureIcon = treasureImages.get(treasure);
            if (treasureIcon != null) {
                treasurePanel.add(new JLabel(treasureIcon), BorderLayout.CENTER);
            } else {
                treasurePanel.add(new JLabel(treasure.getName(), JLabel.CENTER), BorderLayout.CENTER);
            }

            treasuresPanel.add(treasurePanel);
        }

        // 添加牌组信息
        JLabel treasureDeckLabel = new JLabel("宝藏牌组: 0 张");
        JLabel floodDeckLabel = new JLabel("洪水牌组: 0 张");
        deckInfoPanel.add(treasureDeckLabel);
        deckInfoPanel.add(floodDeckLabel);

        // 添加组件到面板
        add(waterLevelLabel);
        add(waterLevelBar);
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(treasuresPanel);
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(deckInfoPanel);
    }

    /**
     * 加载宝藏图像
     */
    private void loadTreasureImages() {
        treasureImages = new HashMap<>();

        for (Treasure treasure : Treasure.values()) {
            try {
                String imagePath = "src/resources/treasures/" + treasure.name().toLowerCase() + ".png";
                Image image = ImageIO.read(new File(imagePath));
                treasureImages.put(treasure, new ImageIcon(image));
            } catch (IOException e) {
                // 错误处理保持不变
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
                return new Color(139, 69, 19); // 棕色
            case THE_STATUE_OF_THE_WIND:
                return new Color(169, 169, 169); // 银色
            case THE_CRYSTAL_OF_FIRE:
                return new Color(220, 20, 60); // 红色
            case THE_OCEANS_CHALICE:
                return new Color(30, 144, 255); // 蓝色
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
        waterLevelLabel.setText("水位: " + level);
        waterLevelBar.setValue(level);

        // 更新牌组信息
        JLabel treasureDeckLabel = (JLabel) deckInfoPanel.getComponent(0);
        JLabel floodDeckLabel = (JLabel) deckInfoPanel.getComponent(1);
        treasureDeckLabel.setText("宝藏牌组: " + gameData.getTreasureDeckSize() + " 张");
        floodDeckLabel.setText("洪水牌组: " + gameData.getFloodDeckSize() + " 张");

        // 更新宝藏状态（显示哪些已被捕获）
        for (int i = 0; i < Treasure.values().length; i++) {
            Treasure treasure = Treasure.values()[i];
            JPanel treasurePanel = (JPanel) treasuresPanel.getComponent(i);

            if (gameData.isTreasureCaptured(treasure)) {
                treasurePanel.setBackground(Color.GREEN);
            } else {
                treasurePanel.setBackground(Color.WHITE);
            }
        }

        revalidate();
        repaint();
    }
}
