package org.example;

public class MazeBuilder {
    private final int CAKE_SIZE = 50;
    private Cake[] cakes;
    private int cakesCount;

    public Cake[] buildMaze(int templateIndex, int width, int height, int difficulty) {
        this.cakes = new Cake[500]; // הגדלתי כדי שיהיה מקום למבוכים שפרוסים על כל המסך
        this.cakesCount = 0;

        int cols = width / CAKE_SIZE;
        int rows = height / CAKE_SIZE;

        switch (templateIndex) {
            case 0:
                drawExpandedFortress(cols, rows, difficulty);
                break;
            case 1:
                drawCityGrid(cols, rows, difficulty);
                break;
            case 2:
                drawFullSlalom(cols, rows, difficulty);
                break;
        }

        return this.cakes;
    }

    public int getCakesCount() {
        return this.cakesCount;
    }

    // --- תבנית 0: "מבצר פרוס" ---
    // קירות חיצוניים גדולים וקירות פנימיים שיוצרים מסדרונות היקפיים
    private void drawExpandedFortress(int cols, int rows, int difficulty) {
        int padding = 4; // מרחק מהקירות

        // מסגרת פנימית גדולה
        for (int i = padding; i < cols - padding; i++) {
            if (i < cols/2 - 2 || i > cols/2 + 2) { // פתחים רחבים באמצע הלמעלה/למטה
                addCake(i, padding);
                addCake(i, rows - padding - 1);
            }
        }
        for (int j = padding; j < rows - padding; j++) {
            if (j < rows/2 - 2 || j > rows/2 + 2) { // פתחים רחבים בצדדים
                addCake(padding, j);
                addCake(cols - padding - 1, j);
            }
        }

        // במרכז, מבנה קטן עם שינוי קושי
        int midX = cols / 2;
        int midY = rows / 2;
        addCake(midX - 2, midY - 2);
        addCake(midX + 2, midY - 2);
        addCake(midX - 2, midY + 2);
        addCake(midX + 2, midY + 2);

        if (difficulty >= 1) {
            addCake(midX, midY); // מכשול באמצע
        }
    }

    // --- תבנית 1: "עיר צפופה" ---
    // מפוזר על כל המסך בצורה שווה עם חסימות שזזות
    private void drawCityGrid(int cols, int rows, int difficulty) {
        int shift = difficulty % 2;

        for (int i = 5 + shift; i < cols - 3; i += 5) {
            for (int j = 3; j < rows - 3; j += 4) {
                // יוצר בלוקים קטנים בכל רחבי המסך
                addCake(i, j);
                addCake(i + 1, j);

                // הוספת קיר שמקשה על מעבר אנכי ברמות קשות
                if (difficulty >= 1 && j % 2 == 0) {
                    addCake(i, j + 1);
                }
            }
        }

        // שורת עוגות רצופה שתחתוך את המסך לרוחב (כמעט עד הסוף)
        for (int i = 3; i < cols - 6; i++) {
            if (i % 4 != 0) { // משאיר פתחים קטנים
                addCake(i, rows / 2);
            }
        }
    }

    // --- תבנית 2: סלאלום מלא מקצה לקצה ---
    private void drawFullSlalom(int cols, int rows, int difficulty) {
        int wallSpacing = 5; // מרווח בין העמודים שמאפשר מעבר קל
        boolean gapAtBottom = (difficulty % 2 == 0);

        for (int i = 5; i < cols - 2; i += wallSpacing) {
            if (gapAtBottom) {
                // חומה עד כמעט למטה
                for (int j = 1; j < rows - 4; j++) {
                    addCake(i, j);
                }
            } else {
                // חומה עד כמעט למעלה
                for (int j = 4; j < rows - 1; j++) {
                    addCake(i, j);
                }
            }
            gapAtBottom = !gapAtBottom; // הופך כיוון
        }
    }

    private void addCake(int gridX, int gridY) {
        // בועת הגנה ענקית סביב השחקן (שלא ייווצר עליו קיר בשום מצב)
        if (gridX >= 1 && gridX <= 4 && gridY >= 1 && gridY <= 4) {
            return;
        }

        if (cakesCount < cakes.length) {
            cakes[cakesCount++] = new Cake(gridX * CAKE_SIZE, gridY * CAKE_SIZE, CAKE_SIZE, CAKE_SIZE);
        }
    }
}