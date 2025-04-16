package fi.game.view;

import fi.game.controller.*;
import fi.game.model.*;
import javax.swing.*;
import java.awt.*;

/**
 * 行动按钮面板，用于游戏操作
 */
public class ActionPanel extends JPanel {
    private JButton moveButton;
    private JButton shoreUpButton;
    private JButton giveCardButton;
    private JButton captureTreasureButton;
    private JButton nextButton;
    private JButton specialActionButton;
    private JButton clearButton;
    private JButton liftOffButton;

    private GameController controller;

    public ActionPanel() {
        setPreferredSize(new Dimension(1200, 60));
        setBorder(BorderFactory.createTitledBorder("行动"));
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));

        // 创建按钮
        moveButton = new JButton("移动到");
        shoreUpButton = new JButton("加固");
        giveCardButton = new JButton("给予卡牌");
        captureTreasureButton = new JButton("捕获宝藏");
        nextButton = new JButton("下一步");
        specialActionButton = new JButton("特殊行动");
        clearButton = new JButton("清除选择");
        liftOffButton = new JButton("起飞");

        // 添加按钮到面板
        add(moveButton);
        add(shoreUpButton);
        add(giveCardButton);
        add(captureTreasureButton);
        add(nextButton);
        add(specialActionButton);
        add(clearButton);
        add(liftOffButton);

        // 设置action listeners
        moveButton.addActionListener(e -> {
            if (controller != null) controller.onMoveAction();
        });

        shoreUpButton.addActionListener(e -> {
            if (controller != null) controller.onShoreUpAction();
        });

        giveCardButton.addActionListener(e -> {
            if (controller != null) controller.onGiveCardAction();
        });

        captureTreasureButton.addActionListener(e -> {
            if (controller != null) controller.onCaptureTreasureAction();
        });

        nextButton.addActionListener(e -> {
            if (controller != null) controller.onNextAction();
        });

        specialActionButton.addActionListener(e -> {
            if (controller != null) controller.onSpecialAction();
        });

        clearButton.addActionListener(e -> {
            if (controller != null) controller.onClearSelections();
        });

        liftOffButton.addActionListener(e -> {
            if (controller != null) controller.onLiftOffAction();
        });

        // 初始禁用所有按钮
        updateButtons(GamePhase.SETUP, null);
    }

    /**
     * 更新按钮状态
     */
    public void updateButtons(GamePhase phase, Player currentPlayer) {
        boolean inActionPhase = (phase == GamePhase.PLAYER_ACTIONS);
        boolean inDrawPhase = (phase == GamePhase.DRAW_TREASURE_CARDS);
        boolean inFloodPhase = (phase == GamePhase.DRAW_FLOOD_CARDS);

        moveButton.setEnabled(inActionPhase);
        shoreUpButton.setEnabled(inActionPhase);
        giveCardButton.setEnabled(inActionPhase);
        captureTreasureButton.setEnabled(inActionPhase);

        specialActionButton.setEnabled(inActionPhase || inDrawPhase || inFloodPhase);
        clearButton.setEnabled(inActionPhase || inDrawPhase || inFloodPhase);

        nextButton.setEnabled(true);  // 始终启用下一步按钮

        // 只有在满足所有条件时才启用起飞按钮
        // 需要基于是否所有宝藏都已捕获且所有玩家都在愚者着陆点
        liftOffButton.setEnabled(false);  // 默认禁用
    }

    /**
     * 设置游戏控制器
     */
    public void setGameController(GameController controller) {
        this.controller = controller;
    }
}