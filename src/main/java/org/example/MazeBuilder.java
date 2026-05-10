package org.example;

public class MazeBuilder {
    private final int CAKE_SIZE = 50;
    private Cake[] cakes;
    private int cakesCount;

    public Cake[] buildMaze(int templateIndex, int width, int height, int difficulty) {
        this.cakes = new Cake[300];
        this.cakesCount = 0;

        int cols = width / CAKE_SIZE;
        int rows = height / CAKE_SIZE;
        int midX = cols / 2;
        int midY = rows / 2;

        switch (templateIndex) {
            case 0:
                drawCenterFortressMaze(cols, rows, midX, midY, difficulty);
                break;
            case 1:
                drawDenseMaze(cols, rows, midX, midY, difficulty);
                break;
            case 2:
                drawSlalomMaze(cols, rows, difficulty);
                break;
        }

        return this.cakes;
    }

    public int getCakesCount() {
        return this.cakesCount;
    }

    // --- תבנית 0: "זירת המרכז" - פתוחה, מרווחת ומרוכזת ---
    private void drawCenterFortressMaze(int cols, int rows, int midX, int midY, int difficulty) {
        // קירות אופקיים של הזירה (למעלה ולמטה) עם פתח רחב מאוד באמצע!
        for (int i = midX - 5; i <= midX + 5; i++) {
            if (i < midX - 1 || i > midX + 1) { // פתח ענק של 3 משבצות
                addCake(i, midY - 3);
                addCake(i, midY + 3);
            }
        }

        // קירות אנכיים של הזירה (צדדים) עם פתח פתוח לחלוטין
        for (int j = midY - 2; j <= midY + 2; j++) {
            if (j != midY) { // פתח באמצע הקיר
                addCake(midX - 5, j);
                addCake(midX + 5, j);
            }
        }

        // שינויים דינמיים כשהקושי עולה (מוסיף עניין)
        if (difficulty >= 1) {
            // הוספת עמודי פינות קטנים
            addCake(3, 3);
            addCake(cols - 4, 3);
            addCake(3, rows - 4);
            addCake(cols - 4, rows - 4);
        }
        if (difficulty >= 2) {
            // הצרת הפתחים המרכזיים טיפה ברמות הקשות
            addCake(midX - 1, midY - 3);
            addCake(midX + 1, midY + 3);
        }
    }

    // --- תבנית 1: הצפופה (עם שינויים לפי קושי) ---
    private void drawDenseMaze(int cols, int rows, int midX, int midY, int difficulty) {
        // מתחילים את צורות ה-L במיקומים שונים לפי הקושי!
        int shift = difficulty % 2; // יזיז את כל המבוך משבצת אחת ימינה ברמות מתקדמות

        for (int i = 4 + shift; i < cols - 3; i += 4) {
            for (int j = 4; j < rows - 3; j += 4) {
                addCake(i, j);
                addCake(i + 1, j);
                addCake(i, j + 1);
            }
        }

        // חגורות מרכזיות
        for (int i = midX - 4; i <= midX + 4; i++) {
            if (i != midX && i != midX - 1) {
                addCake(i, midY - 2);
            }
        }

        if (difficulty >= 1) {
            // הוספת חסימה נוספת למטה ברמות קשות
            for (int i = midX - 3; i <= midX + 3; i++) {
                if (i != midX) addCake(i, midY + 3);
            }
        }
    }

    // --- תבנית 2: סלאלום זיגזג ---
    private void drawSlalomMaze(int cols, int rows, int difficulty) {
        int wallSpacing = 4;

        // טריק מעולה: כל פעם שעולים בקושי, הזיגזג מתהפך! פעם מתחיל מלמעלה ופעם מלמטה
        boolean gapAtBottom = (difficulty % 2 == 0);

        // מזיזים את תחילת הקירות לפי הקושי כדי שזה לא יהיה בדיוק אותו מבוך
        int startCol = 4 + (difficulty % 3);

        for (int i = startCol; i < cols - 2; i += wallSpacing) {
            if (gapAtBottom) {
                for (int j = 1; j < rows - 3; j++) {
                    addCake(i, j);
                }
            } else {
                for (int j = 3; j < rows - 1; j++) {
                    addCake(i, j);
                }
            }
            gapAtBottom = !gapAtBottom; // הופך כל פעם כדי ליצור זיגזג
        }
    }
    private void addCake(int gridX, int gridY) {

        if (gridX >= 1 && gridX <= 4 && gridY >= 1 && gridY <= 4) {
            return;
        }

        int[] blockedX = {6, 10, 14, 18, 22, 26};
        int[] blockedY = {6, 6, 6, 10, 10, 10};

        for (int i = 0; i < blockedX.length; i++) {
            if (gridX == blockedX[i] && gridY == blockedY[i]) {
                return;
            }
        }

        if (cakesCount < cakes.length) {
            cakes[cakesCount++] =
                    new Cake(gridX * CAKE_SIZE, gridY * CAKE_SIZE, CAKE_SIZE, CAKE_SIZE);
        }
    }
}