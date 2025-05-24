package org.example.ForbiddenIsland.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * 游戏地图工具类
 * 管理游戏板布局和坐标转换
 */
public class GameMap {
    // 游戏板布局数据

    // 空白区域（不放置瓦片的位置）
    public static final ArrayList<Integer> BLANK_LAYOUT = new ArrayList<Integer>() {{
        add(0);  add(1);  add(4);  add(5);
        add(6);  add(11); add(24); add(29);
        add(30); add(31); add(34); add(35);
    }};

    // 游戏板尺寸
    public static final int ROWS = 6;
    public static final int COLS = 6;

    // 坐标映射
    public static final HashMap<Integer, int[]> COORDINATES_MAP = new HashMap<>();  // 索引到坐标的映射
    public static final HashMap<String, Integer> POSITION_MAP = new HashMap<>();    // 坐标到索引的映射
    public static final HashMap<Integer, String> ADVENTURER_MAP = new HashMap<>();  // 冒险家ID到名称的映射

    /**
     * 设置所有映射关系
     */
    public static void setupMatchers() {
        // 索引到坐标的映射
        COORDINATES_MAP.put(0, new int[]{0, 2});
        COORDINATES_MAP.put(1, new int[]{0, 3});
        COORDINATES_MAP.put(2, new int[]{1, 1});
        COORDINATES_MAP.put(3, new int[]{1, 2});
        COORDINATES_MAP.put(4, new int[]{1, 3});
        COORDINATES_MAP.put(5, new int[]{1, 4});
        COORDINATES_MAP.put(6, new int[]{2, 0});
        COORDINATES_MAP.put(7, new int[]{2, 1});
        COORDINATES_MAP.put(8, new int[]{2, 2});
        COORDINATES_MAP.put(9, new int[]{2, 3});
        COORDINATES_MAP.put(10, new int[]{2, 4});
        COORDINATES_MAP.put(11, new int[]{2, 5});
        COORDINATES_MAP.put(12, new int[]{3, 0});
        COORDINATES_MAP.put(13, new int[]{3, 1});
        COORDINATES_MAP.put(14, new int[]{3, 2});
        COORDINATES_MAP.put(15, new int[]{3, 3});
        COORDINATES_MAP.put(16, new int[]{3, 4});
        COORDINATES_MAP.put(17, new int[]{3, 5});
        COORDINATES_MAP.put(18, new int[]{4, 1});
        COORDINATES_MAP.put(19, new int[]{4, 2});
        COORDINATES_MAP.put(20, new int[]{4, 3});
        COORDINATES_MAP.put(21, new int[]{4, 4});
        COORDINATES_MAP.put(22, new int[]{5, 2});
        COORDINATES_MAP.put(23, new int[]{5, 3});

        // 坐标到索引的映射
        POSITION_MAP.put(Arrays.toString(new int[]{0, 2}), 0);
        POSITION_MAP.put(Arrays.toString(new int[]{0, 3}), 1);
        POSITION_MAP.put(Arrays.toString(new int[]{1, 1}), 2);
        POSITION_MAP.put(Arrays.toString(new int[]{1, 2}), 3);
        POSITION_MAP.put(Arrays.toString(new int[]{1, 3}), 4);
        POSITION_MAP.put(Arrays.toString(new int[]{1, 4}), 5);
        POSITION_MAP.put(Arrays.toString(new int[]{2, 0}), 6);
        POSITION_MAP.put(Arrays.toString(new int[]{2, 1}), 7);
        POSITION_MAP.put(Arrays.toString(new int[]{2, 2}), 8);
        POSITION_MAP.put(Arrays.toString(new int[]{2, 3}), 9);
        POSITION_MAP.put(Arrays.toString(new int[]{2, 4}), 10);
        POSITION_MAP.put(Arrays.toString(new int[]{2, 5}), 11);
        POSITION_MAP.put(Arrays.toString(new int[]{3, 0}), 12);
        POSITION_MAP.put(Arrays.toString(new int[]{3, 1}), 13);
        POSITION_MAP.put(Arrays.toString(new int[]{3, 2}), 14);
        POSITION_MAP.put(Arrays.toString(new int[]{3, 3}), 15);
        POSITION_MAP.put(Arrays.toString(new int[]{3, 4}), 16);
        POSITION_MAP.put(Arrays.toString(new int[]{3, 5}), 17);
        POSITION_MAP.put(Arrays.toString(new int[]{4, 1}), 18);
        POSITION_MAP.put(Arrays.toString(new int[]{4, 2}), 19);
        POSITION_MAP.put(Arrays.toString(new int[]{4, 3}), 20);
        POSITION_MAP.put(Arrays.toString(new int[]{4, 4}), 21);
        POSITION_MAP.put(Arrays.toString(new int[]{5, 2}), 22);
        POSITION_MAP.put(Arrays.toString(new int[]{5, 3}), 23);

        // 冒险家ID到名称的映射
        ADVENTURER_MAP.put(0, "Diver");
        ADVENTURER_MAP.put(1, "Engineer");
        ADVENTURER_MAP.put(2, "Explorer");
        ADVENTURER_MAP.put(3, "Messenger");
        ADVENTURER_MAP.put(4, "Navigator");
        ADVENTURER_MAP.put(5, "Pilot");
    }

    /**
     * 根据索引获取坐标
     * @param index 位置索引
     * @return 坐标数组 [x, y]
     */
    public static int[] getCoordinates(int index) {
        return COORDINATES_MAP.getOrDefault(index, new int[]{-1, -1});
    }

    /**
     * 根据坐标获取索引
     * @param coords 坐标数组 [x, y]
     * @return 位置索引
     */
    public static int getPositionIndex(int[] coords) {
        return POSITION_MAP.getOrDefault(Arrays.toString(coords), -1);
    }

    /**
     * 根据冒险家ID获取名称
     * @param id 冒险家ID
     * @return 冒险家名称
     */
    public static String getAdventurerName(int id) {
        return ADVENTURER_MAP.getOrDefault(id, "Unknown");
    }
}