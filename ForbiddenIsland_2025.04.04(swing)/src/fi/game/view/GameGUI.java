package fi.game.view;

import fi.game.controller.*;
import fi.game.model.*;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * 游戏主界面类
 */
public class GameGUI extends JFrame {
    private IslandPanel islandPanel;
    private PlayerPanel playerPanel;
    private InfoPanel infoPanel;
    private ActionPanel actionPanel;
    private ConfigPanel configPanel;
    private LogPanel logPanel;

    // 背景音乐
    private Clip backgroundMusic;

    // 控制器引用
    private GameController gameController;

    /**
     * 构造函数
     */
    public GameGUI() {
        super("禁忌岛");
        initializeGUI();
    }

    /**
     * 初始化GUI
     */
    private void initializeGUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 创建面板
        configPanel = new ConfigPanel();
        islandPanel = new IslandPanel();
        playerPanel = new PlayerPanel();
        infoPanel = new InfoPanel();
        actionPanel = new ActionPanel();
        logPanel = new LogPanel();

        // 布局面板
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(playerPanel, BorderLayout.CENTER);
        leftPanel.add(logPanel, BorderLayout.SOUTH);
        leftPanel.setPreferredSize(new Dimension(250, 600));

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(infoPanel, BorderLayout.CENTER);
        rightPanel.setPreferredSize(new Dimension(250, 600));

        // 添加面板到框架
        add(configPanel, BorderLayout.NORTH);
        add(leftPanel, BorderLayout.WEST);
        add(islandPanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);
        add(actionPanel, BorderLayout.SOUTH);

        // 设置框架属性
        pack();
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * 启动背景音乐
     */
    public void startBackgroundMusic() {
        try {
            File musicFile = new File("resources/sounds/background_music.wav");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(musicFile);
            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(audioStream);
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("播放背景音乐时出错: " + e.getMessage());
        }
    }

    /**
     * 停止背景音乐
     */
    public void stopBackgroundMusic() {
        if (backgroundMusic != null && backgroundMusic.isRunning()) {
            backgroundMusic.stop();
            backgroundMusic.close();
        }
    }

    /**
     * 基于当前游戏状态更新整个GUI
     */
    public void updateGUI(GameData gameData, List<Player> players, Player currentPlayer,
                          WaterMeter waterMeter, GamePhase phase) {
        islandPanel.updateIsland(gameData.getIsland());
        playerPanel.updatePlayers(players, currentPlayer);
        infoPanel.updateInfo(gameData, waterMeter);
        actionPanel.updateButtons(phase, currentPlayer);

        repaint();
    }

    /**
     * 显示消息对话框
     */
    public void showMessage(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * 显示错误对话框
     */
    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "错误", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * 确认对话框
     */
    public boolean confirmDialog(String title, String message) {
        int result = JOptionPane.showConfirmDialog(this, message, title, JOptionPane.YES_NO_OPTION);
        return result == JOptionPane.YES_OPTION;
    }

    // Getters
    public IslandPanel getIslandPanel() {
        return islandPanel;
    }

    public PlayerPanel getPlayerPanel() {
        return playerPanel;
    }

    public InfoPanel getInfoPanel() {
        return infoPanel;
    }

    public ActionPanel getActionPanel() {
        return actionPanel;
    }

    public ConfigPanel getConfigPanel() {
        return configPanel;
    }

    public LogPanel getLogPanel() {
        return logPanel;
    }

    /**
     * 设置控制器
     */
    public void setGameController(GameController controller) {
        this.gameController = controller;

        // 为所有面板设置action listeners
        islandPanel.setGameController(controller);
        playerPanel.setGameController(controller);
        actionPanel.setGameController(controller);
        configPanel.setGameController(controller);
    }
}