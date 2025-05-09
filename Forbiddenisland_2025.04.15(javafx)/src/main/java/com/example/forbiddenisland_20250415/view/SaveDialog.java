package com.example.forbiddenisland_20250415.view;

import com.example.forbiddenisland_20250415.model.SaveManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class SaveDialog {
    private static final DateTimeFormatter DISPLAY_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static File selectedSaveFile = null;

    public static File show() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.UTILITY);
        dialog.setTitle("存档管理");

        // 创建主容器
        VBox mainContainer = new VBox(20);
        mainContainer.setPadding(new Insets(20));
        mainContainer.setStyle("-fx-background-color: linear-gradient(to bottom, #2c3e50, #3498db);");

        // 创建标题
        Label titleLabel = new Label("选择存档");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");

        // 创建存档列表
        ListView<String> saveListView = new ListView<>();
        saveListView.setPrefHeight(300);
        saveListView.setStyle("-fx-background-color: rgba(255, 255, 255, 0.9); -fx-background-radius: 5;");

        // 更新存档列表
        updateSaveList(saveListView);

        // 创建按钮容器
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        // 创建加载按钮
        Button loadButton = new Button("加载");
        loadButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 8 20;");
        loadButton.setOnAction(e -> {
            int selectedIndex = saveListView.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                List<File> saveFiles = SaveManager.getSaveFiles();
                if (selectedIndex < saveFiles.size()) {
                    selectedSaveFile = saveFiles.get(selectedIndex);
                    dialog.close();
                }
            }
        });

        // 创建删除按钮
        Button deleteButton = new Button("删除");
        deleteButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 8 20;");
        deleteButton.setOnAction(e -> {
            int selectedIndex = saveListView.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                List<File> saveFiles = SaveManager.getSaveFiles();
                if (selectedIndex < saveFiles.size()) {
                    File saveFile = saveFiles.get(selectedIndex);
                    // 显示确认对话框
                    Alert confirmDialog = DialogStyler.createConfirmAlert("确认删除", 
                        "确定要删除存档 \"" + saveFile.getName() + "\" 吗？\n此操作不可恢复。");
                    Optional<ButtonType> result = confirmDialog.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        SaveManager.deleteSave(saveFile);
                        updateSaveList(saveListView);
                        // 显示成功消息
                        Alert successDialog = DialogStyler.createInfoAlert("删除成功", "存档已成功删除。");
                        successDialog.showAndWait();
                    }
                }
            } else {
                // 显示错误消息
                Alert errorDialog = DialogStyler.createErrorAlert("错误", "请先选择一个存档。");
                errorDialog.showAndWait();
            }
        });

        // 创建取消按钮
        Button cancelButton = new Button("取消");
        cancelButton.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 8 20;");
        cancelButton.setOnAction(e -> {
            selectedSaveFile = null;
            dialog.close();
        });

        // 添加按钮到按钮容器
        buttonBox.getChildren().addAll(loadButton, deleteButton, cancelButton);

        // 添加所有组件到主容器
        mainContainer.getChildren().addAll(titleLabel, saveListView, buttonBox);

        // 创建场景
        Scene scene = new Scene(mainContainer);
        dialog.setScene(scene);
        dialog.showAndWait();

        return selectedSaveFile;
    }

    private static void updateSaveList(ListView<String> saveListView) {
        saveListView.getItems().clear();
        List<File> saveFiles = SaveManager.getSaveFiles();
        for (File saveFile : saveFiles) {
            String fileName = saveFile.getName();
            // 从文件名中提取时间戳（去掉"save_"前缀和".sav"后缀）
            String timestamp = fileName.substring(5, fileName.length() - 4);
            try {
                LocalDateTime dateTime = LocalDateTime.parse(timestamp, DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
                String displayTime = dateTime.format(DISPLAY_DATE_FORMAT);
                saveListView.getItems().add("存档时间: " + displayTime);
            } catch (Exception e) {
                // 如果解析失败，直接显示文件名
                saveListView.getItems().add("存档: " + fileName);
            }
        }
    }
} 