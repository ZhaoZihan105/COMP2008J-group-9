package org.example.ForbiddenIsland.utils;

import javafx.scene.image.Image;

import java.io.File;

public class ImageLoader {

    public static Image load(String relativePath) {
        // 构造绝对路径
        File file = new File("src/main/java/org/example/ForbiddenIsland/image/" + relativePath);
        if (!file.exists()) {
            System.err.println("❌ 图片文件未找到: " + file.getAbsolutePath());
            throw new RuntimeException("图片文件未找到: " + relativePath);
        }
        return new Image(file.toURI().toString());
    }
    public static Image loadScaled(String relativePath, double width, double height) {
        if (relativePath == null) {
            throw new RuntimeException("❌ 图片路径为 null！");
        }
        File file = new File("src/main/java/org/example/ForbiddenIsland/image/" + relativePath);
        if (!file.exists()) {
            System.err.println("❌ 图片文件未找到: " + file.getAbsolutePath());
            throw new RuntimeException("图片文件未找到: " + relativePath);
        }
        return new Image(file.toURI().toString(), width, height, true, true);
    }

    public static String getAsCssUrl(String relativePath) {
        File file = new File("src/main/java/org/example/ForbiddenIsland/image/" + relativePath);
        if (!file.exists()) {
            throw new RuntimeException("❌ CSS 背景图不存在: " + file.getAbsolutePath());
        }
        return file.toURI().toString();
    }
}
