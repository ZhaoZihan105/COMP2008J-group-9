package org.example.ForbiddenIsland.gui.game;

import javafx.geometry.Insets;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import org.example.ForbiddenIsland.service.game.GameData;
import org.example.ForbiddenIsland.utils.Constants;
import org.example.ForbiddenIsland.utils.GameMap;
import org.example.ForbiddenIsland.utils.ImageLoader;

import java.util.ArrayList;

/**
 * 游戏板面板
 * 显示游戏的瓦片网格
 */
public class BoardPane extends GridPane {
    // 瓦片节点列表
    private final ArrayList<StackPane> tileNodes = new ArrayList<>();

        // 已有代码保持不变...

        public BoardPane() {
            // 保持原有的首选尺寸
            setPrefSize(Constants.BOARD_WIDTH, Constants.BOARD_HEIGHT);

            // 减小边距，减少空白区域
            setPadding(new Insets(2));

            // 减小格子间隔，让布局更紧凑
            setHgap(1);
            setVgap(1);

            // 背景图设置保持不变
            // String bgUrl = ImageLoader.getAsCssUrl(".jpg");
            // setStyle("-fx-background-image: url('" + bgUrl + "'); -fx-background-size: cover;");

            // 创建6x6的网格，瓦片尺寸保持不变
            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 6; j++) {
                    // 创建瓦片节点
                    StackPane tileNode = createTileNode(i, j);

                    // 如果是空白区域，设为不可见
                    if (GameMap.BLANK_LAYOUT.contains(i * 6 + j)) {
                        tileNode.setVisible(false);
                    } else {
                        // 否则添加到瓦片列表
                        tileNodes.add(tileNode);
                    }

                    // 添加到网格
                    add(tileNode, j, i);
                }
            }

            // 添加点击事件处理保持不变
            setupTileClickHandlers();
        }

    /**
     * 创建单个瓦片节点
     * @param row 行索引
     * @param col 列索引
     * @return 瓦片节点
     */
    private StackPane createTileNode(int row, int col) {
        StackPane tileNode = new StackPane();
        tileNode.setPrefSize(Constants.TILE_WIDTH, Constants.TILE_HEIGHT);

        // 确保鼠标事件可以被捕获
        tileNode.setPickOnBounds(true);

        // 添加初始图像视图
        ImageView imageView = new ImageView();
        imageView.setFitWidth(Constants.TILE_WIDTH);
        imageView.setFitHeight(Constants.TILE_HEIGHT);

        // 确保图像不会阻挡点击事件
        imageView.setPickOnBounds(false);

        tileNode.getChildren().add(imageView);

        return tileNode;
    }

    /**
     * 设置瓦片点击事件处理
     */
    private void setupTileClickHandlers() {
        for (int i = 0; i < tileNodes.size(); i++) {
            final int index = i;
            tileNodes.get(i).setOnMouseClicked(event -> {
                // 清除所有瓦片的选中状态
                for (StackPane tile : tileNodes) {
                    tile.setStyle("-fx-cursor: hand;");
                }

                // 设置当前瓦片为选中状态
                tileNodes.get(index).setStyle("-fx-cursor: hand; -fx-border-color: yellow; -fx-border-width: 3;");

                // 添加日志，帮助调试点击事件
                System.out.println("点击了瓦片: " + index);

                // 获取点击的瓦片坐标
                int[] coords = GameMap.getCoordinates(index);
                System.out.println("瓦片坐标: [" + coords[0] + ", " + coords[1] + "]");

                // 确保事件不会被其他元素消费
                event.consume();

                // 通知游戏数据处理瓦片选择
                GameData.selectTile(coords);
            });

            // 设置样式，让瓦片可以显示点击效果
            tileNodes.get(i).setStyle("-fx-cursor: hand;");
        }
    }

    /**
     * 获取瓦片节点列表
     * @return 瓦片节点列表
     */
    public ArrayList<StackPane> getTileNodes() {
        return tileNodes;
    }
}