package org.example.ForbiddenIsland.service.game.component.cards;

//package treasurehunter.service.game.component.cards;

/**
 * 宝藏类型枚举
 * 定义游戏中四种可收集的宝藏
 */
public enum TreasureFigurines {
    EARTH("Earth", "src/main/java/org/example/ForbiddenIsland/image/treasures/earth.png"),

    WIND("Wind", "src/main/java/org/example/ForbiddenIsland/image/treasures/wind.png"),
    FIRE("Fire", "src/main/java/org/example/ForbiddenIsland/image/treasures/fire.png"),
    OCEAN("Ocean", "src/main/java/org/example/ForbiddenIsland/image/treasures/ocean.png");

    private final String name;      // 宝藏名称
    private final String imagePath; // 宝藏图像路径

    /**
     * 初始化宝藏类型
     * @param name 宝藏名称
     * @param imagePath 宝藏图像路径
     */
    TreasureFigurines(String name, String imagePath) {
        this.name = name;
        this.imagePath = imagePath;
    }

    /**
     * 获取宝藏名称
     * @return 宝藏名称
     */
    public String getName() {
        return name;
    }

    /**
     * 获取宝藏图像路径
     * @return 图像路径
     */
    public String getImagePath() {
        return imagePath;
    }
}