package org.example;

public class GameSettings {
    public static final int MAX_LEVELS = 15;
    public static final int WALL_LEFT = 50;
    public static final int WALL_RIGHT = 48;
    public static final int WALL_TOP = 35;
    public static final int WALL_BOTTOM = 50;

    public static boolean isInsidePlayArea(int x, int y, int width, int height) {
        return x >= WALL_LEFT &&
                y >= WALL_TOP &&
                x + width <= Main.WINDOW_WIDTH - WALL_RIGHT &&
                y + height <= Main.WINDOW_HEIGHT - WALL_BOTTOM;
    }
}