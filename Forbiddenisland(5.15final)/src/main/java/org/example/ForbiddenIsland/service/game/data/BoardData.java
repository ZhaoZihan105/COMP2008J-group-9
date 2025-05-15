package org.example.ForbiddenIsland.service.game.data;

//package treasurehunter.service.game.data;

import org.example.ForbiddenIsland.gui.updater.LogManager;
import org.example.ForbiddenIsland.gui.updater.UpdateManager;
import org.example.ForbiddenIsland.service.game.Game;
import org.example.ForbiddenIsland.service.game.GameData;
import org.example.ForbiddenIsland.utils.GameMap;

import java.util.ArrayList;
import java.util.Arrays;
/**
 * 游戏板数据
 * 管理由瓦片组成的游戏板
 */
public class BoardData {
    // 游戏板数据
    private final Tile[][] tileMap;         // 瓦片地图
    private final ArrayList<Integer> tiles; // 瓦片ID列表
    private boolean canMove;                // 是否可以移动
    private boolean canShoreUp;             // 是否可以加固

    /**
     * 初始化游戏板
     * @param players 玩家列表
     * @param tiles 瓦片ID列表
     */
    public BoardData(ArrayList<Integer> players, ArrayList<Integer> tiles) {
        this.tiles = tiles;
        tileMap = new Tile[6][6];
        int tileIdx = 0;

        // 创建6x6的游戏板
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                // 检查位置是否是空白区域
                if (GameMap.BLANK_LAYOUT.contains(i * 6 + j)) {
                    tileMap[i][j] = new Tile(false);
                } else {
                    // 检查是否有玩家在瓦片上
                    if (players.contains(this.tiles.get(tileIdx) - 9)) {
                        tileMap[i][j] = new Tile(this.tiles.get(tileIdx), this.tiles.get(tileIdx) - 9, true);
                        GameData.getAdventurers()[players.indexOf(this.tiles.get(tileIdx) - 9)].setPos(i, j);
                    } else {
                        tileMap[i][j] = new Tile(this.tiles.get(tileIdx), true);
                    }
                    tileIdx++;
                }
            }
        }
    }

    /**
     * 沉没多个瓦片（修复越界问题）
     * @param sinkTiles 要沉没的瓦片ID列表
     */
    public void sinkTiles(ArrayList<Integer> sinkTiles) {
        for (int sinkTile : sinkTiles) {
            int tileIndex = this.tiles.indexOf(sinkTile);
            if (tileIndex == -1) {
                LogManager.logMessage("[错误] sinkTile ID " + sinkTile + " 不在 tile 列表中，已跳过此卡。");
                continue;
            }

            int[] coords = GameMap.getCoordinates(tileIndex);

            if (coords[0] < 0 || coords[1] < 0 || coords[0] >= 6 || coords[1] >= 6) {
                LogManager.logMessage("[错误] sinkTile ID " + sinkTile + " 的坐标非法: " + Arrays.toString(coords));
                continue;
            }

            // 如果瓦片沉没（而不仅是淹没）
            if (tileMap[coords[0]][coords[1]].sink()) {
                GameData.getFloodDeck().removeFloodCard(sinkTile);

                if (tileMap[coords[0]][coords[1]].getPlayersOnTile().size() != 0) {
                    for (int player : tileMap[coords[0]][coords[1]].getPlayersOnTile()) {
                        LogManager.logMessage(GameMap.getAdventurerName(player) + " 落入海中");
                    }
                    Game.setNeedToSave(true);
                    Game.setPlayersInWater(new ArrayList<>(tileMap[coords[0]][coords[1]].getPlayersOnTile()));
                    tileMap[coords[0]][coords[1]].getPlayersOnTile().clear();
                }
            }
        }

        if (Game.isNeedToSave()) {
            Game.setInFakeRound(true);
            Game.setFakeRoundNum(Game.getRoundNum());
            Game.rescuePlayersRound();
        }

        UpdateManager.getBoardUpdater().updateUI();
    }


    /**
     * 设置是否可以移动
     * @param canMove 是否可移动标志
     */
    public void setCanMove(boolean canMove) {
        this.canMove = canMove;
    }

    /**
     * 设置是否可以加固
     * @param canShoreUp 是否可加固标志
     */
    public void setCanShoreUp(boolean canShoreUp) {
        this.canShoreUp = canShoreUp;
    }

    /**
     * 检查所有宝藏点是否都已沉没
     * @return 如果任一对宝藏点都沉没未收集返回true
     */
    public boolean areTreasuresSunk() {
        int[] shrinesFlooded = {1, 1, 1, 1}; // 记录每种宝藏的两个瓦片的状态

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                switch (tileMap[i][j].getTileId()) {
                    case 1:
                    case 2:
                        if (!tileMap[i][j].exists() && tileMap[i][j].isUncaptured()) {
                            shrinesFlooded[0]--;
                        }
                        break;
                    case 3:
                    case 4:
                        if (!tileMap[i][j].exists() && tileMap[i][j].isUncaptured()) {
                            shrinesFlooded[1]--;
                        }
                        break;
                    case 5:
                    case 6:
                        if (!tileMap[i][j].exists() && tileMap[i][j].isUncaptured()) {
                            shrinesFlooded[2]--;
                        }
                        break;
                    case 7:
                    case 8:
                        if (!tileMap[i][j].exists() && tileMap[i][j].isUncaptured()) {
                            shrinesFlooded[3]--;
                        }
                        break;
                }
            }
        }

        // 如果任何一种宝藏的两个瓦片都沉没且宝藏未被收集
        for (int isFlooded : shrinesFlooded) {
            if (isFlooded == -1) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取指定位置的瓦片
     * @param x 行坐标
     * @param y 列坐标
     * @return 瓦片对象
     */
    public Tile getTile(int x, int y) {
        // 确保坐标在有效范围内
        if (x >= 0 && x < 6 && y >= 0 && y < 6) {
            return tileMap[x][y];
        }
        // 返回不存在的瓦片
        return new Tile(false);
    }

    // Getter方法
    public boolean isCanShoreUp() {
        return canShoreUp;
    }

    public boolean isCanMove() {
        return canMove;
    }

    public Tile[][] getTileMap() {
        return tileMap;
    }
}