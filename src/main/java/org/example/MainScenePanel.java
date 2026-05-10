package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class MainScenePanel extends JPanel {
    private Player player;
    private Cake[] cakes;
    private Enemy[] enemies;
    private int score;
    private Prize[] prizes;
    private String[] candyImages;
    private int cakesCount;
    private final int CAKE_SIZE = 50;
    private LevelsBackground levelsBackground;

    private int currentLevel = 1;
    private final int MAX_LEVELS = 9;

    private boolean isPaused = false;
    private boolean isLevelStarting = true;

    // --- משתני טיימר ---
    private int timeLeft = 60; // כמות השניות הכוללת (דקה)
    private int timerCounter = 0; // מונה שסופר את הריצות של הלולאה

    public MainScenePanel(int x, int y, int width, int height) {
        this.candyImages = new String[]{
                "/Blue_candy.png",
                "/Orange_candy.png",
                "/Pink_candy.png",
                "/Purple_candy.png",
                "/Yellow_candy.png"
        };
        this.setBounds(x, y, width, height);
        this.setLayout(null);
        this.levelsBackground = new LevelsBackground();

        this.setFocusable(true);
        this.requestFocus();

        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    if (isLevelStarting) {
                        // אם אנחנו במסך ההמתנה של תחילת השלב, נשחרר את העצירה ונתחיל לשחק!
                        isLevelStarting = false;
                        isPaused = false;
                    } else {
                        // אם אנחנו במהלך משחק רגיל, נעשה עצירה רגילה או נחזור ממנה
                        isPaused = !isPaused;
                    }
                    repaint(); // ציור מחדש
                }
            }
        });

        this.setDoubleBuffered(true);

        loadLevel(currentLevel);

        MovementListener movementListener = new MovementListener(this, this.player);
        this.addKeyListener(movementListener);

        JButton soundButton = Utils.createSoundButton();
        this.add(soundButton);

        RoundedButton backButton = RoundedButton.createBackButton(width - 135, 12, this);
        this.add(backButton);

        RoundedButton exitButton = RoundedButton.createExitButton(width - 80, 12);
        this.add(exitButton);

        this.gameLoop();
    }

    public boolean isPaused() {
        return isPaused;
    }

    private void loadLevel(int level) {
    // איפוס הטיימר ל-60 שניות בכל טעינת שלב
        this.timeLeft = 60;
        this.timerCounter = 0;

        if (this.player == null) {
            this.player = new Player(100, 100, 60, 60);
        } else {
            this.player.setX(100);
            this.player.setY(100);
        }

        int difficultyTier = (level - 1) / 3;
        int mazeTemplate = (level - 1) % 3;

        int amountOfCandies = 5 + (difficultyTier * 3);
        // תמיד לפחות 3 ירקות רגילים!
        int normalEnemies = 3 + difficultyTier;
        int smartEnemies = difficultyTier;

        MazeBuilder mazeBuilder = new MazeBuilder();
        // מעבירים גם את הקושי לבונה המבוכים
        this.cakes = mazeBuilder.buildMaze(mazeTemplate, Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT, difficultyTier);
        this.cakesCount = mazeBuilder.getCakesCount();

        spawnPrizes(amountOfCandies);

        // שולחים למחלקת האויבים גם את התבנית כדי שידעו איפה לחסום
        setupEnemiesForLevel(normalEnemies, smartEnemies, mazeTemplate);

        for (int i = 0; i < this.enemies.length; i++) {
            if (this.enemies[i] != null) {
                this.enemies[i].setIsMoving(true);
            }
        }

        this.isPaused = true;
        this.isLevelStarting = true;
    }

    private void setupEnemiesForLevel(int normalEnemies, int smartEnemies, int mazeTemplate) {
        int totalEnemies = normalEnemies + smartEnemies;
        this.enemies = new Enemy[totalEnemies];

        int[] spawnX = {300, 500, 700, 900, 1100, 1300};
        int[] spawnY = {300, 300, 300, 500, 500, 500};

        int enemyIndex = 0;

        for (int i = 0; i < normalEnemies; i++) {
            int currentX = spawnX[enemyIndex % spawnX.length];
            int currentY = spawnY[enemyIndex % spawnY.length];

            int type = i % 4;

            if (type == 0) {
                this.enemies[enemyIndex] = new EnemyBroccoli(currentX, currentY, 46, 46);
            } else if (type == 1) {
                this.enemies[enemyIndex] = new EnemyEggplant(currentX, currentY, 46, 46);
            } else if (type == 2) {
                this.enemies[enemyIndex] = new EnemyCarrot(currentX, currentY, 46, 46);
            } else {
                this.enemies[enemyIndex] = new EnemyCorn(currentX, currentY, 46, 46);
            }

            enemyIndex++;
        }

        for (int i = 0; i < smartEnemies; i++) {
            int currentX = spawnX[enemyIndex % spawnX.length];
            int currentY = spawnY[enemyIndex % spawnY.length];

            this.enemies[enemyIndex] =
                    new EnemyBellPepper(currentX, currentY, 46, 46, this.player);

            enemyIndex++;
        }
    }

    private void spawnPrizes(int amount) {
        prizes = new Prize[amount];
        Random random = new Random();

        int safeMargin = 20;
        int minX = 90 + safeMargin;
        int maxX = Main.WINDOW_WIDTH - 48 - 15 - safeMargin;
        int minY = 35 + safeMargin;
        int maxY = Main.WINDOW_HEIGHT - 50 - 36 - safeMargin;

        for (int i = 0; i < prizes.length; i++) {
            int x, y;
            do {
                x = random.nextInt(maxX - minX) + minX;
                y = random.nextInt(maxY - minY) + minY;
            } while (!isValidPrizeLocation(x, y, i));

            String randomCandy = candyImages[random.nextInt(candyImages.length)];
            prizes[i] = new Prize(x, y, 15, 36, randomCandy);
        }
    }

    private boolean isValidPrizeLocation(int x, int y, int currentPrizeIndex) {
        Rectangle tempPrizeRect = new Rectangle(x, y, 15, 36);

        for (int j = 0; j < cakesCount; j++) {
            if (cakes[j] != null && tempPrizeRect.intersects(cakes[j].getRect())) {
                return false;
            }
        }

        int padding = 40;
        Rectangle safeZone = new Rectangle(x - padding, y - padding, 15 + (padding * 2), 36 + (padding * 2));

        for (int k = 0; k < currentPrizeIndex; k++) {
            if (prizes[k] != null && safeZone.intersects(prizes[k].getBounds())) {
                return false;
            }
        }
        return true;
    }

    public boolean checkCakeCollision() {
        Rectangle characterRect = this.player.getRect();
        int padLeft = 14;
        int padRight = 14;
        int padTop = 22;
        int padBottom = 5;

        Rectangle smallCharacterRect = new Rectangle(
                characterRect.x + padLeft,
                characterRect.y + padTop,
                characterRect.width - (padLeft + padRight),
                characterRect.height - (padTop + padBottom)
        );

        for (int i = 0; i < this.cakesCount; i++) {
            Cake currentCake = this.cakes[i];
            if (currentCake != null) {
                Rectangle cakeRect = currentCake.getRect();

                int cakePadding = 4;
                Rectangle smallCakeRect = new Rectangle(
                        cakeRect.x + cakePadding,
                        cakeRect.y + cakePadding,
                        cakeRect.width - (cakePadding * 2),
                        cakeRect.height - (cakePadding * 2)
                );

                if (smallCharacterRect.intersects(smallCakeRect)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkCollision(Player player, Enemy enemy) {
        // מגלחים 15 פיקסלים מכל צד של השחקן כדי להתעלם מהרקע השקוף
        int playerPadding = 15;
        Rectangle playerHitbox = new Rectangle(
                player.getX() + playerPadding,
                player.getY() + playerPadding,
                player.getWidth() - (playerPadding * 2),
                player.getHeight() - (playerPadding * 2)
        );
        // מגלחים 10 פיקסלים מכל צד של הירק
        int enemyPadding = 10;
        Rectangle enemyHitbox = new Rectangle(
                enemy.getX() + enemyPadding,
                enemy.getY() + enemyPadding,
                enemy.getWidth() - (enemyPadding * 2),
                enemy.getHeight() - (enemyPadding * 2)
        );
        return playerHitbox.intersects(enemyHitbox);
    }

    private boolean checkEnemyCollision(Enemy enemy1, Enemy enemy2) {
        return (enemy1.getX() + enemy1.getWidth() > enemy2.getX()) &&
                (enemy1.getX() < enemy2.getX() + enemy2.getWidth()) &&
                (enemy1.getY() + enemy1.getHeight() > enemy2.getY()) &&
                (enemy1.getY() < enemy2.getY() + enemy2.getHeight());
    }

    private boolean checkEnemyCakeCollision(Enemy enemy) {
        Rectangle enemyRect = enemy.getRect();
        for (int i = 0; i < this.cakesCount; i++) {
            if (cakes[i] != null) {
                if (enemyRect.intersects(cakes[i].getRect())) {
                    return true;
                }
            }
        }
        return false;
    }

    public void checkPrizeCollisions() {
        Rectangle playerRect = player.getRect();
        boolean allCollected = true;

        if (prizes != null) {
            for (int i = 0; i < prizes.length; i++) {
                if (prizes[i] != null && !prizes[i].isCollected()) {
                    if (playerRect.intersects(prizes[i].getBounds())) {
                        prizes[i].setCollected(true);
                        this.score += 10;
                    } else {
                        allCollected = false;
                    }
                }
            }
        }

        if (allCollected && prizes != null && prizes.length > 0) {
            currentLevel++;
            if (currentLevel > MAX_LEVELS) {
                ImageIcon originalIcon = new ImageIcon(getClass().getResource("/Trophy_Icon.png"));

                Image scaledImage = originalIcon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
                ImageIcon TrophyIcon = new ImageIcon(scaledImage);

                JOptionPane.showMessageDialog(null, "ניצחת במשחק! כל הכבוד!", "Victory", JOptionPane.PLAIN_MESSAGE,TrophyIcon);
                System.exit(0);
            } else {
                loadLevel(currentLevel);
            }
        }
    }

    public void gameLoop() {
        new Thread(() -> {
            while (true) {
                if (!isPaused) {
                    for (int i = 0; i < this.enemies.length; i++) {
                        if (this.enemies[i] == null) continue;

                        int oldX = this.enemies[i].getX();
                        int oldY = this.enemies[i].getY();

                        this.enemies[i].move();

                        // בדיקה אם האויב נתקע בקיר או באויב אחר
                        boolean hitSomething = checkEnemyCakeCollision(this.enemies[i]);
                        if (!hitSomething) {
                            for (int j = 0; j < this.enemies.length; j++) {
                                if (i != j && this.enemies[j] != null && checkEnemyCollision(this.enemies[i], this.enemies[j])) {
                                    hitSomething = true;
                                    break;
                                }
                            }
                        }

                        // אם האויב נתקע - נחזיר אותו אחורה ונהפוך לו כיוון
                        if (hitSomething) {
                            this.enemies[i].setX(oldX);
                            this.enemies[i].setY(oldY);
                            this.enemies[i].reverseDirection();
                        }

                        // --- בדיקת פסילה: התנגשות בין השחקן לאויב ---
                        if (checkCollision(this.player, this.enemies[i])) {

                            // 1. הגדרת הכפתורים המותאמים אישית
                            Object[] options = {"Restart Level", "Back to Menu"};

                            ImageIcon originalIcon = new ImageIcon(getClass().getResource("/BellPepper_Front.png"));

                            Image scaledImage = originalIcon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
                            ImageIcon pepperIcon = new ImageIcon(scaledImage);

                            // 2. בניית התוכן של חלון הדיאלוג
                            JOptionPane pane = new JOptionPane(
                                    "אוי לא! נתפסת על ידי הירקות!\nהניקוד שלך: " + this.score,
                                    JOptionPane.PLAIN_MESSAGE,
                                    JOptionPane.YES_NO_OPTION,
                                    pepperIcon,
                                    options,
                                    options[0] // כפתור ה-Restart יהיה מסומן כברירת מחדל
                            );

                            // 3. יצירת חלון אמיתי ונטרול ה-X!
                            JDialog dialog = pane.createDialog(SwingUtilities.windowForComponent(this), "Game Over");
                            dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE); // הקסם שמבטל את ה-X
                            dialog.setVisible(true); // מציג את החלון ועוצר את המשחק עד שתהיה תשובה

                            // 4. בדיקה מה המשתמש בחר
                            Object selectedValue = pane.getValue();

                            if (selectedValue != null && selectedValue.equals(options[0])) {
                                // בחרו ב- Restart Level
                                this.score = 0; // מאפסים את הניקוד
                                loadLevel(this.currentLevel); // טוענים מחדש את השלב הנוכחי!
                                break; // קריטי: יוצאים מיד מלולאת האויבים כדי להתחיל מחדש
                            } else {
                                // בחרו ב- Back to Menu
                                Window parentWindow = SwingUtilities.windowForComponent(this);
                                if (parentWindow != null) {
                                    parentWindow.dispose(); // סוגר את חלון המשחק לחלוטין
                                }
                                new MainMenu(); // פותח מחדש את התפריט הראשי
                                return; // קריטי: מסיים את הלולאה האינסופית והורג את ה-Thread של המשחק!
                            }
                        }
                    }

                    checkPrizeCollisions();
                    timerCounter++;
                    if (timerCounter >= 60) { // עברה בערך שנייה אחת
                        timeLeft--; // מורידים שנייה מהטיימר
                        timerCounter = 0; // מאפסים את המונה לשנייה הבאה

                        // מה קורה כשנגמר הזמן? (פסילה)
                        if (timeLeft <= 0) {
                            Object[] options = {"Restart Level", "Back to Menu"};

                        // 2. טעינת התמונה של הפלפל (שנה את שם הקובץ לשם האמיתי שיש לך בתיקייה)
                            ImageIcon originalIcon = new ImageIcon(getClass().getResource("/BellPepper_Front.png"));

                        // 3. שינוי גודל התמונה כדי שתתאים יפה לחלון (גודל 60 על 60 פיקסלים)
                            Image scaledImage = originalIcon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
                            ImageIcon pepperIcon = new ImageIcon(scaledImage);

                            // 4. בניית חלון הדיאלוג עם תמונת הפלפל!
                            JOptionPane pane = new JOptionPane(
                                    "אוי לא! הזמן אזל אנא נסה שנית \nהניקוד שלך: " + this.score,
                                    JOptionPane.PLAIN_MESSAGE, // שינינו ל-PLAIN כדי למחוק את סמל האזהרה הצהוב
                                    JOptionPane.YES_NO_OPTION,
                                    pepperIcon, // <--- כאן החלפנו את ה-null במשתנה של התמונה שלנו!
                                    options,
                                    options[0]
                            );

                            JDialog dialog = pane.createDialog(SwingUtilities.windowForComponent(this), "Time's Up");
                            dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
                            dialog.setVisible(true);

                            Object selectedValue = pane.getValue();

                            if (selectedValue != null && selectedValue.equals(options[0])) {
                                this.score = 0;
                                loadLevel(this.currentLevel);
                            } else {
                                Window parentWindow = SwingUtilities.windowForComponent(this);
                                if (parentWindow != null) {
                                    parentWindow.dispose();
                                }
                                new MainMenu();
                                return;
                            }
                        }
                    } // סוף לוגיקת הטיימר// סוף התנאי של (!isPaused)
                }
                repaint();
                Utils.sleep(16);
            }
        }).start();
    }


    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        if (this.levelsBackground != null) {
            this.levelsBackground.paint(graphics, this.getWidth(), this.getHeight());
        }

        for (int i = 0; i < this.enemies.length; i++) {
            if (this.enemies[i] != null) {
                this.enemies[i].paint(graphics);
            }
        }

        if (cakes != null) {
            for (int i = 0; i < cakesCount; i++) {
                if (cakes[i] != null) {
                    cakes[i].paint(graphics);
                }
            }
        }

        if (this.player != null) {
            this.player.paint(graphics,this.isPaused);
        }

        if (prizes != null) {
            for (int i = 0; i < prizes.length; i++) {
                if (prizes[i] != null && !prizes[i].isCollected()) {
                    prizes[i].draw(graphics);
                }
            }
        }

        int buttonX = 20;
        int buttonWidth = 50;
        int scoreX = buttonX + buttonWidth + 10;
        int scoreY = 55;

        graphics.setFont(new Font("Arial", Font.BOLD, 30));

        graphics.setColor(Color.BLACK);
        graphics.drawString("Score: " + this.score, scoreX + 2, scoreY + 2);
        graphics.setColor(new Color(180, 140, 207));
        graphics.drawString("Score: " + this.score, scoreX, scoreY);

        graphics.setColor(Color.BLACK);
        graphics.drawString("Level: " + this.currentLevel, scoreX + 202, scoreY + 2);
        graphics.setColor(new Color(255, 180, 193));
        graphics.drawString("Level: " + this.currentLevel, scoreX + 200, scoreY);

        // --- ציור הטיימר ---
        // חישוב דקות ושניות כדי להציג בפורמט של 01:30
        int minutes = this.timeLeft / 60;
        int seconds = this.timeLeft % 60;
        // String.format מאפשר לנו להוסיף אפס מוביל אם המספר קטן מ-10
        String timeString = String.format("Time: %02d:%02d", minutes, seconds);

        graphics.setColor(Color.BLACK);
        graphics.drawString(timeString, scoreX + 402, scoreY + 2);

        // נשנה את הצבע לאדום כשנשארות 10 שניות או פחות כדי להלחיץ את השחקן!
        if (this.timeLeft <= 10) {
            graphics.setColor(Color.RED);
        } else {
            graphics.setColor(Color.WHITE);
        }
        graphics.drawString(timeString, scoreX + 400, scoreY);

        if (isPaused) {
            graphics.setColor(new Color(0, 0, 0, 200));
            graphics.fillRect(0, 0, getWidth(), getHeight());

            graphics.setColor(Color.WHITE);

            String text;
            if (isLevelStarting) {
                // אם זה תחילת שלב
                graphics.setFont(new Font("Arial", Font.BOLD, 40));
                text = "PRESS SPACE TO START";
            } else {
                // אם זו עצירה רגילה
                graphics.setFont(new Font("Arial", Font.BOLD, 60));
                text = "PAUSED";
            }

            int x = (getWidth() - graphics.getFontMetrics().stringWidth(text)) / 2;
            int y = getHeight() / 2;

            graphics.drawString(text, x, y);
        }
    }
}