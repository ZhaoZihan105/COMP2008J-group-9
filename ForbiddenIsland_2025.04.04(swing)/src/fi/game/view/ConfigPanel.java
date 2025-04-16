package fi.game.view;

import fi.game.controller.*;
import javax.swing.*;
import java.awt.*;

/**
 * 游戏配置面板，用于设置游戏参数并启动游戏
 */
public class ConfigPanel extends JPanel {
    private JComboBox<Integer> playerCountComboBox;
    private JComboBox<String> difficultyComboBox;
    private JButton startButton;

    private GameController controller;

    public ConfigPanel() {
        setPreferredSize(new Dimension(800, 50));
        setBorder(BorderFactory.createTitledBorder("游戏配置"));
        setLayout(new FlowLayout());

        // 创建组件
        playerCountComboBox = new JComboBox<>(new Integer[]{2, 3, 4});
        difficultyComboBox = new JComboBox<>(new String[]{"新手", "普通", "精英", "传奇"});
        startButton = new JButton("开始");

        // 添加带标签的组件
        add(new JLabel("-----玩家数-----"));
        add(playerCountComboBox);
        add(new JLabel("---难度级别---"));
        add(difficultyComboBox);
        add(startButton);

        // 设置action listener
        startButton.addActionListener(e -> {
            if (controller != null) {
                int playerCount = (Integer) playerCountComboBox.getSelectedItem();
                int difficultyLevel = difficultyComboBox.getSelectedIndex() + 1;
                controller.startGame(playerCount, difficultyLevel);
                startButton.setEnabled(false);
            }
        });
    }

    /**
     * 设置游戏控制器
     */
    public void setGameController(GameController controller) {
        this.controller = controller;
    }

    /**
     * 重置配置面板
     * 这是一个公共方法，在游戏结束时调用
     */
    public void reset() {
        startButton.setEnabled(true);
    }
}