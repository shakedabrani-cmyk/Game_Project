package org.example;

import java.awt.*;
import java.util.Random;

public class EnemyManager {
    private static final int CAKE_SIZE = 50;
    private static final int ENEMY_SIZE = 46;

    private Player player;
    private Cake[] cakes;
    private int cakesCount;
    private Enemy[] enemies;

    public EnemyManager(Player player, Cake[] cakes, int cakesCount) {
        this.player = player;
        this.cakes = cakes;
        this.cakesCount = cakesCount;
    }

    // יוצר את כל האויבים לפי השלב
    public Enemy[] createEnemies(int level) {
        int difficultyTier = (level - 1) / 3;

        int normalEnemies = 3 + difficultyTier;
        int smartEnemies = Math.min(difficultyTier, 2);

        setupEnemiesForLevel(normalEnemies, smartEnemies);
        startEnemiesMovement();

        return this.enemies;
    }

    // מעדכן את האויבים ומחזיר אם השחקן נפגע
    public boolean updateEnemies() {
        for (int i = 0; i < this.enemies.length; i++) {
            if (this.enemies[i] == null) {
                continue;
            }

            int oldX = this.enemies[i].getX();
            int oldY = this.enemies[i].getY();

            this.enemies[i].move();

            if (enemyHitObstacle(i)) {
                moveEnemyBack(i, oldX, oldY);
            }

            if (checkCollision(this.player, this.enemies[i])) {
                return true;
            }
        }

        return false;
    }

    // בונה את מערך האויבים וממקם אותם במקומות תקינים
    private void setupEnemiesForLevel(int normalEnemies, int smartEnemies) {
        int totalEnemies = normalEnemies + smartEnemies;
        this.enemies = new Enemy[totalEnemies];

        Random random = new Random();

        int cols = Main.WINDOW_WIDTH / CAKE_SIZE;
        int rows = Main.WINDOW_HEIGHT / CAKE_SIZE;

        for (int i = 0; i < totalEnemies; i++) {
            Point spawnPoint = findEnemySpawnPoint(random, cols, rows);

            if (i < normalEnemies) {
                createRegularEnemy(i, spawnPoint.x, spawnPoint.y);
            } else {
                this.enemies[i] = new EnemyBellPepper(
                        spawnPoint.x,
                        spawnPoint.y,
                        ENEMY_SIZE,
                        ENEMY_SIZE,
                        this.player
                );
            }
        }
    }

    // מחפש נקודת התחלה תקינה לאויב
    private Point findEnemySpawnPoint(Random random, int cols, int rows) {
        int x;
        int y;

        do {
            int gridX = random.nextInt(cols - 2) + 1;
            int gridY = random.nextInt(rows - 2) + 1;

            x = (gridX * CAKE_SIZE) + 2;
            y = (gridY * CAKE_SIZE) + 2;

        } while (!isValidEnemyLocation(x, y));

        return new Point(x, y);
    }

    // בודק אם מיקום האויב פנוי מעוגות שחקן ואויבים אחרים
    private boolean isValidEnemyLocation(int x, int y) {
        Rectangle enemyRect = new Rectangle(x, y, ENEMY_SIZE, ENEMY_SIZE);

        if (touchesCake(enemyRect)) {
            return false;
        }

        Rectangle safeZone = new Rectangle(50, 50, 200, 200);

        if (enemyRect.intersects(safeZone)) {
            return false;
        }

        return !touchesOtherEnemy(enemyRect);
    }

    // בודק אם מלבן נוגע בעוגה
    private boolean touchesCake(Rectangle rect) {
        for (int i = 0; i < this.cakesCount; i++) {
            if (this.cakes[i] != null && rect.intersects(this.cakes[i].getRect())) {
                return true;
            }
        }

        return false;
    }

    // בודק אם מלבן נוגע באויב אחר
    private boolean touchesOtherEnemy(Rectangle enemyRect) {
        if (this.enemies == null) {
            return false;
        }

        for (int i = 0; i < this.enemies.length; i++) {
            if (this.enemies[i] != null && enemyRect.intersects(this.enemies[i].getRect())) {
                return true;
            }
        }

        return false;
    }

    // יוצר אויב רגיל לפי סוג משתנה
    private void createRegularEnemy(int index, int x, int y) {
        int type = index % 4;

        if (type == 0) {
            this.enemies[index] = new EnemyBroccoli(x, y, ENEMY_SIZE, ENEMY_SIZE);
        } else if (type == 1) {
            this.enemies[index] = new EnemyEggplant(x, y, ENEMY_SIZE, ENEMY_SIZE);
        } else if (type == 2) {
            this.enemies[index] = new EnemyGeneric(x, y, ENEMY_SIZE, ENEMY_SIZE, "Carrot");
        } else {
            this.enemies[index] = new EnemyGeneric(x, y, ENEMY_SIZE, ENEMY_SIZE, "Corn");
        }
    }

    // מפעיל תנועה לכל האויבים
    private void startEnemiesMovement() {
        for (int i = 0; i < this.enemies.length; i++) {
            if (this.enemies[i] != null) {
                this.enemies[i].setIsMoving(true);
            }
        }
    }

    // בודק אם אויב פגע בעוגה או באויב אחר
    private boolean enemyHitObstacle(int enemyIndex) {
        if (checkEnemyCakeCollision(this.enemies[enemyIndex])) {
            return true;
        }

        for (int i = 0; i < this.enemies.length; i++) {
            if (i != enemyIndex &&
                    this.enemies[i] != null &&
                    checkEnemyCollision(this.enemies[enemyIndex], this.enemies[i])) {
                return true;
            }
        }

        return false;
    }

    // מחזיר אויב למיקום הקודם ומטפל בכיוון אחרי פגיעה
    private void moveEnemyBack(int enemyIndex, int oldX, int oldY) {
        this.enemies[enemyIndex].setX(oldX);
        this.enemies[enemyIndex].setY(oldY);

        if (this.enemies[enemyIndex] instanceof EnemyBellPepper) {
            ((EnemyBellPepper) this.enemies[enemyIndex]).suspendTracking(140);
            return;
        }

        this.enemies[enemyIndex].reverseDirection();
    }

    // בודק התנגשות בין השחקן לאויב
    private boolean checkCollision(Player player, Enemy enemy) {
        int playerPadding = 15;

        Rectangle playerHitbox = new Rectangle(
                player.getX() + playerPadding,
                player.getY() + playerPadding,
                player.getWidth() - playerPadding * 2,
                player.getHeight() - playerPadding * 2
        );

        int enemyPadding = 10;

        Rectangle enemyHitbox = new Rectangle(
                enemy.getX() + enemyPadding,
                enemy.getY() + enemyPadding,
                enemy.getWidth() - enemyPadding * 2,
                enemy.getHeight() - enemyPadding * 2
        );

        return playerHitbox.intersects(enemyHitbox);
    }

    // בודק התנגשות בין שני אויבים
    private boolean checkEnemyCollision(Enemy enemy1, Enemy enemy2) {
        return (enemy1.getX() + enemy1.getWidth() > enemy2.getX()) &&
                (enemy1.getX() < enemy2.getX() + enemy2.getWidth()) &&
                (enemy1.getY() + enemy1.getHeight() > enemy2.getY()) &&
                (enemy1.getY() < enemy2.getY() + enemy2.getHeight());
    }

    // בודק אם אויב נוגע בעוגה
    private boolean checkEnemyCakeCollision(Enemy enemy) {
        Rectangle enemyRect = enemy.getRect();

        for (int i = 0; i < this.cakesCount; i++) {
            if (cakes[i] != null && enemyRect.intersects(cakes[i].getRect())) {
                return true;
            }
        }

        return false;
    }
}