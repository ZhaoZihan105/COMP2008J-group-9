package com.example.forbiddenisland_20250415.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class RulesDialog {
    public static void show() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Game Rules");

        // 创建主容器
        VBox mainContainer = new VBox(20);
        mainContainer.setPadding(new Insets(20));
        mainContainer.setStyle("-fx-background-color: linear-gradient(to bottom, #2c3e50, #3498db);");

        // 创建标题
        Text title = new Text("Forbidden Island Rules");
        title.setFont(Font.font("System", FontWeight.BOLD, 24));
        title.setFill(Color.WHITE);

        // 创建内容容器
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        content.setStyle("-fx-background-color: rgba(255, 255, 255, 0.9);" +
                "-fx-background-radius: 10;" +
                "-fx-border-radius: 10;" +
                "-fx-border-color: #34495e;" +
                "-fx-border-width: 2;");

        // 创建各个部分的样式
        String sectionStyle = "-fx-font-weight: bold; -fx-font-size: 16; -fx-fill: #2c3e50;";
        String contentStyle = "-fx-font-size: 14; -fx-fill: #34495e;";

        // Game Overview
        Text overviewTitle = new Text("Game Overview");
        overviewTitle.setStyle(sectionStyle);
        Text overview = new Text("Forbidden Island is a cooperative game where players work together to capture four treasures and escape from a sinking island. Players must balance their actions between capturing treasures, shoring up sinking tiles, and keeping the island afloat.");
        overview.setStyle(contentStyle);
        overview.setWrappingWidth(700);

        // Setup
        Text setupTitle = new Text("Setup");
        setupTitle.setStyle(sectionStyle);
        Text setup = new Text("1. Place the 24 island tiles in the specified pattern\n" +
                "2. Each player chooses a role and places their pawn on the corresponding starting tile\n" +
                "3. Deal 2 treasure cards to each player\n" +
                "4. Set the water level marker to the chosen difficulty level\n" +
                "5. Draw and flip 6 flood cards to start the game");
        setup.setStyle(contentStyle);
        setup.setWrappingWidth(700);

        // Game Phases
        Text phasesTitle = new Text("Game Phases");
        phasesTitle.setStyle(sectionStyle);
        Text phases = new Text("Each player's turn consists of three phases:\n" +
                "1. Take up to 3 actions\n" +
                "2. Draw 2 treasure cards\n" +
                "3. Draw flood cards equal to the current water level");
        phases.setStyle(contentStyle);
        phases.setWrappingWidth(700);

        // Actions
        Text actionsTitle = new Text("Available Actions");
        actionsTitle.setStyle(sectionStyle);
        Text actions = new Text("• Move: Move to an adjacent tile\n" +
                "• Shore Up: Remove water from an adjacent tile\n" +
                "• Give a Treasure Card: Give a card to a player on the same tile\n" +
                "• Capture a Treasure: Turn in 4 matching treasure cards while on the corresponding treasure tile");
        actions.setStyle(contentStyle);
        actions.setWrappingWidth(700);

        // Special Cards
        Text specialCardsTitle = new Text("Special Cards");
        specialCardsTitle.setStyle(sectionStyle);
        Text specialCards = new Text("• Helicopter Lift: Move any number of players to any tile\n" +
                "• Sandbag: Shore up any tile\n" +
                "• Waters Rise: Increase the water level and reshuffle the flood discard pile");
        specialCards.setStyle(contentStyle);
        specialCards.setWrappingWidth(700);

        // Roles
        Text rolesTitle = new Text("Character Roles");
        rolesTitle.setStyle(sectionStyle);
        Text roles = new Text("• Explorer: Can move and shore up diagonally\n" +
                "• Pilot: Can fly to any tile once per turn\n" +
                "• Diver: Can move through one or more adjacent flooded or sunk tiles\n" +
                "• Engineer: Can shore up 2 tiles for 1 action\n" +
                "• Navigator: Can move other players 2 tiles for 1 action\n" +
                "• Messenger: Can give treasure cards to any player");
        roles.setStyle(contentStyle);
        roles.setWrappingWidth(700);

        // Winning and Losing
        Text winLoseTitle = new Text("Winning and Losing");
        winLoseTitle.setStyle(sectionStyle);
        Text winLose = new Text("Players win by:\n" +
                "• Capturing all four treasures\n" +
                "• Having all players on Fools' Landing\n" +
                "• Using a helicopter card to escape\n\n" +
                "Players lose if:\n" +
                "• Fools' Landing sinks\n" +
                "• A player drowns (no valid move when their tile sinks)\n" +
                "• Both treasure tiles of the same type sink before the treasure is captured\n" +
                "• The water level reaches the skull and crossbones");
        winLose.setStyle(contentStyle);
        winLose.setWrappingWidth(700);

        // 添加所有内容到容器
        content.getChildren().addAll(
                overviewTitle, overview,
                setupTitle, setup,
                phasesTitle, phases,
                actionsTitle, actions,
                specialCardsTitle, specialCards,
                rolesTitle, roles,
                winLoseTitle, winLose
        );

        // 创建滚动面板
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(600);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        // 创建关闭按钮
        Button closeButton = new Button("Close");
        closeButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold; " +
                "-fx-padding: 10 20; -fx-background-radius: 5;");
        closeButton.setOnAction(e -> dialog.close());
        closeButton.setOnMouseEntered(e -> closeButton.setStyle("-fx-background-color: #c0392b; -fx-text-fill: white; " +
                "-fx-font-weight: bold; -fx-padding: 10 20; -fx-background-radius: 5;"));
        closeButton.setOnMouseExited(e -> closeButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; " +
                "-fx-font-weight: bold; -fx-padding: 10 20; -fx-background-radius: 5;"));

        // 添加所有组件到主容器
        mainContainer.getChildren().addAll(title, scrollPane, closeButton);
        mainContainer.setAlignment(Pos.CENTER);

        Scene scene = new Scene(mainContainer, 800, 700);
        dialog.setScene(scene);
        dialog.showAndWait();
    }
} 