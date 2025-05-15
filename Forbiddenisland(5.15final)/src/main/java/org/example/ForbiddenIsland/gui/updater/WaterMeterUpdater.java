package org.example.ForbiddenIsland.gui.updater;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.example.ForbiddenIsland.service.game.GameData;
import org.example.ForbiddenIsland.utils.Constants;
import org.example.ForbiddenIsland.utils.ImageLoader;

/**
 * 水位计更新器
 * 负责更新水位计显示
 */
public class WaterMeterUpdater implements IUpdater {
    private static ImageView waterMeterImageView;

    public static void setWaterMeterImageView(ImageView imageView) {
        waterMeterImageView = imageView;
    }

    public WaterMeterUpdater() {
        if (waterMeterImageView != null) {
            Image waterMeterImage = ImageLoader.loadScaled(
                    GameData.getWaterMeterImage(),  // ✅ 方法名修正
                    Constants.WATER_METER_WIDTH,
                    Constants.WATER_METER_HEIGHT
            );
            waterMeterImageView.setImage(waterMeterImage);
        }
    }

    @Override
    public void updateUI() {
        if (waterMeterImageView == null) return;

        Platform.runLater(() -> {
            Image waterMeterImage = ImageLoader.loadScaled(
                    GameData.getWaterMeterImage(),  // ✅ 修正拼写错误
                    Constants.WATER_METER_WIDTH,
                    Constants.WATER_METER_HEIGHT
            );
            waterMeterImageView.setImage(waterMeterImage);
        });
    }

    @Override
    public void gameOver() {
        if (waterMeterImageView == null) return;

        Platform.runLater(() -> waterMeterImageView.setVisible(false));
    }
}
