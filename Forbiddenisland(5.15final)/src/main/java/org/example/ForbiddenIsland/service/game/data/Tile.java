package org.example.ForbiddenIsland.service.game.data;

// package org.example.ForbiddenIsland.service.game.data;

import org.example.ForbiddenIsland.gui.updater.LogManager;
import org.example.ForbiddenIsland.service.game.Game;
import org.example.ForbiddenIsland.service.game.component.adventurer.Adventurer;

import java.util.ArrayList;

/**
 * 表示游戏板上的一个瓦片
 * 管理瓦片的状态、图像和玩家位置
 */
public class Tile {
    // 瓦片数据
    private int tileId = -1;               // 瓦片ID
    private TileStatus status;             // 瓦片状态（正常、已淹没、已沉没）
    private String imagePath;              // 瓦片图像路径
    private String imageFolder;            // 图像文件夹
    private String imageFile;              // 图像文件名
    private boolean exists;                // 瓦片是否存在
    private ArrayList<Integer> playersOnTile;  // 在瓦片上的玩家ID
    private boolean captured = false;      // 宝藏是否已被捕获

    /**
     * 创建一个已有玩家在上面的瓦片
     * @param tileId 瓦片ID
     * @param playerId 玩家ID
     * @param exists 是否存在
     */
    public Tile(int tileId, int playerId, boolean exists) {
        this.playersOnTile = new ArrayList<>();
        this.tileId = tileId;
        this.status = TileStatus.NORMAL;
        this.imageFolder = "/tiles/";
        this.imageFile = tileId + ".png";
        this.imagePath = imageFolder + imageFile;
        this.playersOnTile.add(playerId);
        this.exists = exists;
    }

    /**
     * 创建一个没有玩家在上面的瓦片
     * @param tileId 瓦片ID
     * @param exists 是否存在
     */
    public Tile(int tileId, boolean exists) {
        this.playersOnTile = new ArrayList<>();
        this.tileId = tileId;
        this.status = TileStatus.NORMAL;
        this.imageFolder = "/tiles/";
        this.imageFile = tileId + ".png";
        this.imagePath = imageFolder + imageFile;
        this.exists = exists;
    }

    /**
     * 创建一个空白占位瓦片
     * @param exists 是否存在
     */
    public Tile(boolean exists) {
        this.exists = exists;
        this.playersOnTile = new ArrayList<>();
    }

    /**
     * 玩家移动到此瓦片
     * @param playerId 玩家ID
     */
    public void moveOnto(int playerId) {
        this.playersOnTile.add(playerId);
    }

    /**
     * 玩家离开此瓦片
     * @param adventurer 冒险家对象
     */
    public void moveOff(Adventurer adventurer) {
        playersOnTile.remove((Integer) adventurer.getId());
    }

    /**
     * 验证发送者和接收者是否在同一瓦片上
     * @param sender 发送者
     * @param receiver 接收者
     * @return 是否在同一瓦片上
     */
    public boolean canPassTo(Adventurer sender, Adventurer receiver) {
        return playersOnTile.contains(sender.getId()) && playersOnTile.contains(receiver.getId());
    }

    /**
     * 加固已淹没的瓦片
     */
    public void shoreUp() {
        if (status == TileStatus.FLOODED) {
            status = TileStatus.NORMAL;
            this.imageFolder = "/tiles/";
            this.imagePath = imageFolder + imageFile;
        } else {
            System.out.println("错误！瓦片未被淹没");
        }
    }

    /**
     * 淹没或沉没瓦片
     * @return 瓦片是否沉没
     */
    public boolean sink() {
        if (status == TileStatus.NORMAL) {
            status = TileStatus.FLOODED;
            imageFolder = "/flooded/";
            imagePath = imageFolder + imageFile;
            return false;
        } else if (status == TileStatus.FLOODED) {
            status = TileStatus.SUNK;
            imagePath = null;
            exists = false;

            // 检查是否是起飞点沉没（游戏结束条件）
            if (tileId == 14) {
                Game.gameOver(false);
                LogManager.logMessage("【警告！】起飞点已沉没！");
            }
            return true;
        } else {
            System.out.println("错误！此瓦片已沉没");
            return true;
        }
    }

    /**
     * 标记宝藏已被捕获
     */
    public void setCaptured() {
        captured = true;
        this.imageFile = tileId + 24 + ".png";
        this.imagePath = imageFolder + imageFile;
    }

    /**
     * 检查宝藏是否未被捕获
     * @return 宝藏是否未被捕获
     */
    public boolean isUncaptured() {
        return !captured;
    }

    // Getters
    public int getTileId() {
        return tileId;
    }

    public TileStatus getStatus() {
        return status;
    }

    public String getImagePath() {
        return imagePath;
    }

    public boolean exists() {
        return exists;
    }

    public ArrayList<Integer> getPlayersOnTile() {
        return playersOnTile;
    }
}