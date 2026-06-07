package org.example;

public class GameProgress {
    private static int highestUnlockedLevel = 1;

    public static boolean isLevelUnlocked(int level) {
        return level <= highestUnlockedLevel;
    }

    public static void unlockLevel(int level) {
        if (level > highestUnlockedLevel && level <= GameSettings.MAX_LEVELS) {
            highestUnlockedLevel = level;
        }
    }

}