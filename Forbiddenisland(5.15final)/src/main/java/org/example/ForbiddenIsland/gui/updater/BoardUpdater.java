package org.example.ForbiddenIsland.gui.updater;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import org.example.ForbiddenIsland.gui.game.BoardPane;
import org.example.ForbiddenIsland.service.game.GameData;
import org.example.ForbiddenIsland.service.game.data.Tile;
import org.example.ForbiddenIsland.utils.Constants;
import org.example.ForbiddenIsland.utils.GameMap;
import org.example.ForbiddenIsland.utils.ImageLoader;

import java.util.ArrayList;

/**
 * 棋盘更新器
 * 负责更新游戏棋盘界面
 */
public class BoardUpdater implements IUpdater {
    // 棋盘UI引用
    private static BoardPane boardPane;

    /**
     * 设置棋盘UI引用
     * @param pane 棋盘面板
     */
    public static void setBoardPane(BoardPane pane) {
        boardPane = pane;
    }

    /**
     * 初始化棋盘并进行首次更新
     */
    public BoardUpdater() {
        if (boardPane != null) {
            ArrayList<Integer> tiles = new ArrayList<>(GameData.getTiles());
            for (int i = 0; i < boardPane.getTileNodes().size(); i++) {
                StackPane tileNode = boardPane.getTileNodes().get(i);
                tileNode.setDisable(false);
                tileNode.setVisible(true);

                // ✅ 使用 ImageLoader 加载瓦片图像
                Image tileImage = ImageLoader.loadScaled(
                        "tiles/" + tiles.get(i) + ".png",
                        Constants.TILE_WIDTH, Constants.TILE_HEIGHT);

                ((ImageView) tileNode.getChildren().get(0)).setImage(tileImage);
            }

            for (int i = 0; i < GameData.getAdventurers().length; i++) {
                int pos = GameData.getAdventurers()[i].getPos();
                StackPane tileNode = boardPane.getTileNodes().get(pos);

                String pawnPath = GameData.getAdventurers()[i].getPawnImage();
                System.out.println("🧩 加载棋子图片路径：" + pawnPath);  // 加这一行
                // ✅ 使用 ImageLoader 加载玩家棋子图像
                Image pawnImage = ImageLoader.loadScaled(
                        GameData.getAdventurers()[i].getPawnImage(),
                        Constants.TILE_WIDTH / 2, Constants.TILE_HEIGHT / 2);

                ImageView pawnView = new ImageView(pawnImage);
                pawnView.setTranslateX((i % 2) * 20 - 10);
                pawnView.setTranslateY((i / 2) * 20 - 10);

                tileNode.getChildren().add(pawnView);
            }
        }
    }

    @Override
    public void updateUI() {
        if (boardPane == null) return;

        Platform.runLater(() -> {
            Tile[][] tileMap = GameData.getBoard().getTileMap();
            int idx = 0;

            for (int i = 0; i < GameMap.ROWS; i++) {
                for (int j = 0; j < GameMap.COLS; j++) {
                    if (GameMap.BLANK_LAYOUT.contains(i * 6 + j)) continue;

                    Tile tile = tileMap[i][j];
                    StackPane tileNode = boardPane.getTileNodes().get(idx);

                    // 检查是否是当前选中的瓦片
                    boolean isSelected = GameData.getSpecialActionTile()[0] == i &&
                            GameData.getSpecialActionTile()[1] == j;

                    if (!tile.exists()) {
                        tileNode.setVisible(false);
                        tileNode.setDisable(true);
                    } else {
                        tileNode.setVisible(true);
                        tileNode.setDisable(false);

                        // 设置样式，选中的瓦片有黄色边框
                        if (isSelected) {
                            tileNode.setStyle("-fx-cursor: hand; -fx-border-color: yellow; -fx-border-width: 3;");
                        } else {
                            tileNode.setStyle("-fx-cursor: hand;");
                        }

                        // ✅ 使用 ImageLoader 加载瓦片图像
                        Image tileImage = ImageLoader.loadScaled(
                                tile.getImagePath(),
                                Constants.TILE_WIDTH, Constants.TILE_HEIGHT);
                        ((ImageView) tileNode.getChildren().get(0)).setImage(tileImage);

                        while (tileNode.getChildren().size() > 1) {
                            tileNode.getChildren().remove(1);
                        }

                        for (int k = 0; k < tile.getPlayersOnTile().size(); k++) {
                            int playerId = tile.getPlayersOnTile().get(k);
                            String pawnImagePath = "pawns/" + GameMap.getAdventurerName(playerId).toLowerCase() + ".png";

                            // ✅ 使用 ImageLoader 加载棋子图像
                            Image pawnImage = ImageLoader.loadScaled(
                                    pawnImagePath,
                                    Constants.TILE_WIDTH / 2, Constants.TILE_HEIGHT / 2);

                            ImageView pawnView = new ImageView(pawnImage);
                            pawnView.setTranslateX((k % 2) * 20 - 10);
                            pawnView.setTranslateY((k / 2) * 20 - 10);

                            tileNode.getChildren().add(pawnView);
                        }
                    }
                    idx++;
                }
            }
        });
    }

    @Override
    public void gameOver() {
        if (boardPane == null) return;

        Platform.runLater(() -> {
            for (StackPane tileNode : boardPane.getTileNodes()) {
                tileNode.setDisable(true);
            }
        });
    }
}
