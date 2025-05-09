package com.example.forbiddenisland_20250415.view;
// GameGUI.java (JavaFX version)

import com.example.forbiddenisland_20250415.model.*;
import com.example.forbiddenisland_20250415.controller.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.util.List;
import java.util.Optional;

/**
 * 游戏主界面类
 */
public class GameGUI {
    private IslandPanel islandPanel;
    private PlayerPanel playerPanel;
    private InfoPanel infoPanel;
    private ActionPanel actionPanel;
    private ConfigPanel configPanel;
    private LogPanel logPanel;
    private ControlPanel controlPanel;

    // 控制器引用
    private GameController gameController;

    // JavaFX舞台和场景
    private Stage stage;
    private Scene scene;

    /**
     * 构造函数
     */
    public GameGUI(Stage stage) {
        this.stage = stage;
        stage.setTitle("禁忌岛 - 神秘岛冒险");
        initializeGUI();
    }

    /**
     * 初始化GUI
     */
    private void initializeGUI() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));
        root.setBackground(new Background(new BackgroundFill(
                Color.rgb(58, 74, 90), CornerRadii.EMPTY, Insets.EMPTY
        )));

        // 创建面板
        configPanel = new ConfigPanel();
        islandPanel = new IslandPanel();
        playerPanel = new PlayerPanel();
        infoPanel = new InfoPanel();
        actionPanel = new ActionPanel();
        logPanel = new LogPanel();
        controlPanel = new ControlPanel();

        // 应用直接样式，不依赖CSS
        applyPanelStyles();

        // 添加一些间距和阴影效果
        DropShadow panelShadow = new DropShadow();
        panelShadow.setColor(Color.rgb(0, 0, 0, 0.5));
        panelShadow.setRadius(10);
        panelShadow.setOffsetY(3);

        VBox leftPanel = new VBox(15);
        leftPanel.setPadding(new Insets(0, 0, 0, 0));
        leftPanel.getChildren().addAll(playerPanel, logPanel, controlPanel);
        VBox.setVgrow(playerPanel, Priority.ALWAYS);
        leftPanel.setPrefWidth(250);
        leftPanel.setEffect(panelShadow);

        VBox rightPanel = new VBox(15);
        rightPanel.setPadding(new Insets(0, 0, 0, 0));
        rightPanel.getChildren().add(infoPanel);
        rightPanel.setPrefWidth(250);
        rightPanel.setEffect(panelShadow);

        // 为顶部和底部面板添加边距
        BorderPane.setMargin(configPanel, new Insets(0, 0, 10, 0));
        BorderPane.setMargin(actionPanel, new Insets(10, 0, 0, 0));

        // 为左侧和右侧面板添加边距
        BorderPane.setMargin(leftPanel, new Insets(0, 10, 0, 0));
        BorderPane.setMargin(rightPanel, new Insets(0, 0, 0, 10));

        BorderPane.setMargin(islandPanel, new Insets(-10, 0, 0, 0)); // 向上偏移10像素
        // 添加面板到布局
        root.setTop(configPanel);
        root.setLeft(leftPanel);
        root.setCenter(islandPanel);
        root.setRight(rightPanel);
        root.setBottom(actionPanel);

        // 应用阴影效果到中心面板
        islandPanel.setEffect(panelShadow);
        configPanel.setEffect(panelShadow);
        actionPanel.setEffect(panelShadow);

        // 设置场景
        scene = new Scene(root, 1200, 950);

        // 应用基本样式 - 不使用外部CSS
        applySceneStyles();

        stage.setScene(scene);
        stage.setResizable(true);
        stage.centerOnScreen();
    }

    /**
     * 应用面板样式
     */
    /**
     * 应用场景样式
     */
    private void applySceneStyles() {
        scene.getRoot().setStyle(
                "-fx-font-family: 'Segoe UI', Arial, sans-serif;" +
                        "-fx-font-size: 12px;"
        );

        // 为按钮添加通用样式
        String buttonStyle =
                "-fx-background-color: #34495e;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 8px 12px;" +
                        "-fx-background-radius: 5px;" +
                        "-fx-border-radius: 5px;";

        // 为特殊按钮添加样式 - 可以在ActionPanel里直接使用
        String nextButtonStyle =
                "-fx-background-color: linear-gradient(to bottom, #2ecc71, #27ae60);" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;";

        String specialButtonStyle =
                "-fx-background-color: linear-gradient(to bottom, #e67e22, #d35400);" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;";

        String liftOffButtonStyle =
                "-fx-background-color: linear-gradient(to bottom, #9b59b6, #8e44ad);" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;";

        // 为ComboBox添加样式
        String comboBoxStyle =
                "-fx-background-color: #34495e;" +
                        "-fx-text-fill: white;" +
                        "-fx-mark-color: white;";

        // 为TextArea添加样式
        String textAreaStyle =
                "-fx-text-fill: #ecf0f1;" +
                        "-fx-control-inner-background: #2c3e50;" +
                        "-fx-background-insets: 0;" +
                        "-fx-background-radius: 5;" +
                        "-fx-border-radius: 5;";
    }

    /**
     * 应用面板样式
     */
    private void applyPanelStyles() {
        String titledPaneStyle =
                "-fx-text-fill: white;" +
                        "-fx-background-color: #2c3e50;" +
                        "-fx-border-color: #1abc9c;" +
                        "-fx-border-width: 2px;" +
                        "-fx-border-radius: 5px;";

        // 应用样式到所有TitledPane
        configPanel.setStyle(titledPaneStyle);
        islandPanel.setStyle(titledPaneStyle);
        playerPanel.setStyle(titledPaneStyle);
        infoPanel.setStyle(titledPaneStyle);
        actionPanel.setStyle(titledPaneStyle);
        logPanel.setStyle(titledPaneStyle);
        controlPanel.setStyle(titledPaneStyle);

        // 使用Platform.runLater等待JavaFX完成标题节点的渲染
        javafx.application.Platform.runLater(() -> {
            String titledPaneTitleStyle =
                    "-fx-background-color: linear-gradient(to bottom, #3498db, #2980b9);" +
                            "-fx-font-size: 14px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-text-fill: white;" +
                            "-fx-border-radius: 5px 5px 0 0;" +
                            "-fx-background-radius: 5px 5px 0 0;" +
                            "-fx-padding: 8 10 8 10;";

            // 尝试应用标题样式，如果节点不为null
            try {
                if (configPanel.lookup(".title") != null)
                    configPanel.lookup(".title").setStyle(titledPaneTitleStyle);
                if (islandPanel.lookup(".title") != null)
                    islandPanel.lookup(".title").setStyle(titledPaneTitleStyle);
                if (playerPanel.lookup(".title") != null)
                    playerPanel.lookup(".title").setStyle(titledPaneTitleStyle);
                if (infoPanel.lookup(".title") != null)
                    infoPanel.lookup(".title").setStyle(titledPaneTitleStyle);
                if (actionPanel.lookup(".title") != null)
                    actionPanel.lookup(".title").setStyle(titledPaneTitleStyle);
                if (logPanel.lookup(".title") != null)
                    logPanel.lookup(".title").setStyle(titledPaneTitleStyle);
                if (controlPanel.lookup(".title") != null)
                    controlPanel.lookup(".title").setStyle(titledPaneTitleStyle);
            } catch (Exception e) {
                System.err.println("无法应用标题样式: " + e.getMessage());
            }
        });
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
    }

    /**
     * 显示消息对话框
     */
    public void showMessage(String title, String message) {
        Alert alert = DialogStyler.createInfoAlert(title, message);
        alert.showAndWait();
    }

    /**
     * 显示错误对话框
     */
    public void showError(String message) {
        Alert alert = DialogStyler.createErrorAlert("错误", message);
        alert.showAndWait();
    }

    /**
     * 确认对话框
     */
    public boolean confirmDialog(String title, String message) {
        Alert alert = DialogStyler.createConfirmAlert(title, message);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    /**
     * 显示GUI
     */
    public void show() {
        stage.show();
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

    public ControlPanel getControlPanel() {
        return controlPanel;
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