package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MainScenePanel extends JPanel {
    private Player player;
    private Cake[] cakes;
    private Enemy[] enemies;
    private Prize[] prizes;

    private int score;
    private int cakesCount;
    private int currentLevel = 1;
    private int timeLeft = 60;
    private int timerCounter = 0;

    private final int MAX_LEVELS = GameSettings.MAX_LEVELS;

    private String[] candyImages;
    private LevelBackground levelsBackground;
    private JButton soundButton;
    private SoundManager tickingSound;
    private PrizeManager prizeManager;
    private EnemyManager enemyManager;

    private boolean isPaused = false;
    private boolean isLevelStarting = true;
    private boolean isGameRunning = true;

    public MainScenePanel(int x, int y, int width, int height) {
        this(x, y, width, height, 1);
    }

    // בנאי שמפעיל את המשחק משלב שנבחר במפת השלבים
    public MainScenePanel(int x, int y, int width, int height, int startLevel) {
        this.currentLevel = startLevel;

        initializeImages();
        initializePanel(x, y, width, height);
        initializeKeyListener();

        this.setDoubleBuffered(true);

        loadLevel(currentLevel);
        initializeMovementListener();
        initializeButtons(width);

        this.gameLoop();
    }

    // מאתחל את מערך התמונות של הסוכריות הרגילות
    private void initializeImages() {
        this.candyImages = new String[]{
                "/Blue_candy.png",
                "/Orange_candy.png",
                "/Pink_candy.png",
                "/Purple_candy.png",
                "/Yellow_candy.png"
        };
    }

    // מאתחל את הפאנל הרקע הסאונד והגדרות הבסיס של המסך
    private void initializePanel(int x, int y, int width, int height) {
        this.tickingSound = new SoundManager("/Clock_sound.wav");
        this.prizeManager = new PrizeManager();

        this.setBounds(x, y, width, height);
        this.setLayout(null);
        this.levelsBackground = new LevelBackground();

        this.setFocusable(true);
        this.requestFocus();
    }

    // מאזין ללחיצה על רווח כדי להתחיל שלב או לעצור משחק
    private void initializeKeyListener() {
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    togglePause();
                    repaint();
                }
            }
        });
    }

    // מחליף בין מצב עצירה למצב משחק פעיל
    private void togglePause() {
        if (isLevelStarting) {
            isLevelStarting = false;
            isPaused = false;
        } else {
            isPaused = !isPaused;
        }
    }

    // מוסיף מאזין תנועה לשחקן
    private void initializeMovementListener() {
        MovementListener movementListener = new MovementListener(this, this.player);
        this.addKeyListener(movementListener);
    }

    // מוסיף למסך כפתור סאונד חזרה ויציאה
    private void initializeButtons(int width) {
        this.soundButton = Utils.createSoundButton();
        this.add(this.soundButton);

        RoundedButton backButton = RoundedButton.createBackButton(width, this);
        this.add(backButton);

        RoundedButton exitButton = RoundedButton.createExitButton(width);
        this.add(exitButton);
    }

    public boolean isPaused() {
        return isPaused;
    }

    // עוצר את לולאת המשחק ואת צליל הטיימר
    public void stopGame() {
        this.isGameRunning = false;

        if (this.tickingSound != null) {
            this.tickingSound.stop();
        }
    }

    // טוען שלב חדש ומאתחל שחקן מבוך אויבים ופרסים
    private void loadLevel(int level) {
        resetLevelTimer(level);
        resetPlayerPosition();
        buildMaze(level);
        createEnemies(level);
        createPrizes(level);

        this.isPaused = true;
        this.isLevelStarting = true;
    }

    // מאפס את הטיימר לפי מספר השלב
    private void resetLevelTimer(int level) {
        if (this.tickingSound != null) {
            this.tickingSound.stop();
        }

        if (level >= 9 && level <= 15) {
            this.timeLeft = 90;
        } else {
            this.timeLeft = 60;
        }

        this.timerCounter = 0;
    }

    // מחזיר את השחקן לנקודת ההתחלה
    private void resetPlayerPosition() {
        if (this.player == null) {
            this.player = new Player(100, 100, 60, 60);
        } else {
            this.player.setX(100);
            this.player.setY(100);
        }
    }

    // בונה את המבוך לפי השלב ורמת הקושי
    private void buildMaze(int level) {
        int difficultyTier = (level - 1) / 3;
        int mazeTemplate = (level - 1) % 3;

        MazeBuilder mazeBuilder = new MazeBuilder();

        this.cakes = mazeBuilder.buildMaze(
                mazeTemplate,
                Main.WINDOW_WIDTH,
                Main.WINDOW_HEIGHT,
                difficultyTier
        );

        this.cakesCount = mazeBuilder.getCakesCount();
    }

    // יוצר אויבים דרך מחלקת ניהול אויבים
    private void createEnemies(int level) {
        this.enemyManager = new EnemyManager(
                this.player,
                this.cakes,
                this.cakesCount
        );

        this.enemies = this.enemyManager.createEnemies(level);
    }

    // יוצר סוכריות רגילות וסוכריה מיוחדת בשלב
    private void createPrizes(int level) {
        int difficultyTier = (level - 1) / 3;
        int amountOfCandies = 5 + (difficultyTier * 3);

        this.prizes = prizeManager.createPrizes(
                amountOfCandies,
                this.cakes,
                this.cakesCount,
                this.enemies,
                this.candyImages
        );
    }

    // בודק אם השחקן נוגע בעוגה כדי למנוע מעבר דרך קירות
    public boolean checkCakeCollision() {
        Rectangle characterRect = this.player.getRect();

        Rectangle smallCharacterRect = new Rectangle(
                characterRect.x + 14,
                characterRect.y + 22,
                characterRect.width - 28,
                characterRect.height - 27
        );

        for (int i = 0; i < this.cakesCount; i++) {
            Cake currentCake = this.cakes[i];

            if (currentCake != null) {
                Rectangle cakeRect = currentCake.getRect();

                Rectangle smallCakeRect = new Rectangle(
                        cakeRect.x + 4,
                        cakeRect.y + 4,
                        cakeRect.width - 8,
                        cakeRect.height - 8
                );

                if (smallCharacterRect.intersects(smallCakeRect)) {
                    return true;
                }
            }
        }

        return false;
    }

    // בודק איסוף סוכריות ומעבר לשלב הבא
    public void checkPrizeCollisions() {
        Rectangle playerHitbox = getPlayerPrizeHitbox();

        boolean allCollected = true;

        if (prizes != null) {
            for (int i = 0; i < prizes.length; i++) {
                if (prizes[i] != null && !prizes[i].isCollected()) {
                    if (playerHitbox.intersects(prizes[i].getBounds())) {
                        collectPrize(prizes[i]);
                    } else {
                        allCollected = false;
                    }
                }
            }
        }

        if (allCollected && prizes != null && prizes.length > 0) {
            repaint();
            Utils.sleep(100);
            goToNextLevel();
        }
    }

    // יוצר מלבן פגיעה קטן יותר לשחקן בזמן איסוף סוכריות
    private Rectangle getPlayerPrizeHitbox() {
        int padding = 22;

        return new Rectangle(
                player.getX() + padding,
                player.getY() + padding,
                player.getWidth() - padding * 2,
                player.getHeight() - padding * 2
        );
    }

    // מסמן סוכריה כנאספה מוסיף ניקוד ומשמיע צליל
    private void collectPrize(Prize prize) {
        prize.setCollected(true);
        this.score += prize.getPoints();
        SoundEffects.play("/Sweet_Reward.wav");
    }

    // עובר לשלב הבא או מפעיל ניצחון בסיום המשחק
    private void goToNextLevel() {
        currentLevel++;

        if (currentLevel <= MAX_LEVELS) {
            GameProgress.unlockLevel(currentLevel);
        }

        if (currentLevel > MAX_LEVELS) {
            handleVictory();
        } else {
            loadLevel(currentLevel);
        }
    }

    // מציג חלון ניצחון ומסיים את המשחק
    private void handleVictory() {
        stopGame();

        Utils.stopMusic();
        SoundEffects.play("/Victory_sound.wav");

        ImageIcon originalIcon = new ImageIcon(getClass().getResource("/TrophyIcon.png"));
        Image scaledImage = originalIcon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        ImageIcon trophyIcon = new ImageIcon(scaledImage);

        UIManager.put("OptionPane.background", Color.WHITE);
        UIManager.put("Panel.background", Color.WHITE);

        JOptionPane pane = new JOptionPane(
                "ניצחת במשחק כל הכבוד\nהניקוד שלך: " + this.score,
                JOptionPane.PLAIN_MESSAGE,
                JOptionPane.DEFAULT_OPTION,
                trophyIcon
        );

        Window parentWindow = SwingUtilities.windowForComponent(this);
        JDialog dialog = new JDialog(parentWindow, "Victory", Dialog.ModalityType.APPLICATION_MODAL);

        dialog.setUndecorated(true);
        dialog.getRootPane().setBorder(BorderFactory.createLineBorder(new Color(144, 238, 144), 8));

        dialog.setContentPane(pane);
        dialog.pack();
        dialog.setLocationRelativeTo(parentWindow);
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        pane.addPropertyChangeListener(e -> {
            if (JOptionPane.VALUE_PROPERTY.equals(e.getPropertyName())) {
                dialog.dispose();
            }
        });

        dialog.setVisible(true);

        System.exit(0);
    }

    // מפעיל את לולאת המשחק שמעדכנת אויבים פרסים טיימר וציור
    public void gameLoop() {
        new Thread(() -> {
            while (isGameRunning) {
                if (!isPaused) {
                    if (enemyManager.updateEnemies()) {
                        handleGameOver("אוי לא נתפסת על ידי הירקות", "Game Over");
                        stopGame();
                        return;
                    }

                    checkPrizeCollisions();

                    if (!updateTimer()) {
                        stopGame();
                        return;
                    }
                }

                repaint();
                Utils.sleep(16);
            }
        }).start();
    }

    // מעדכן את הטיימר ובודק אם הזמן נגמר
    private boolean updateTimer() {
        timerCounter++;

        if (timerCounter >= 60) {
            timeLeft--;
            timerCounter = 0;

            if (timeLeft == 10 && this.tickingSound != null) {
                this.tickingSound.playLoop();
            }

            if (timeLeft <= 0) {
                timeLeft = 0;
                repaint();

                return handleGameOver("אוי לא הזמן אזל אנא נסה שנית", "Time's Up");
            }
        }

        return true;
    }

    // מציג חלון הפסד ומטפל בבחירה של הפעלה מחדש או חזרה לתפריט
    private boolean handleGameOver(String message, String title) {
        Utils.stopMusic();

        if (this.tickingSound != null) {
            this.tickingSound.stop();
        }

        SoundEffects.play("/Losing_sound.wav");

        Object[] options = {"Restart Level", "Back to Menu"};

        ImageIcon originalIcon = new ImageIcon(getClass().getResource("/BellPepper_Front.png"));
        Image scaledImage = originalIcon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        ImageIcon pepperIcon = new ImageIcon(scaledImage);

        UIManager.put("OptionPane.background", Color.WHITE);
        UIManager.put("Panel.background", Color.WHITE);

        JOptionPane pane = new JOptionPane(
                message + "\nהניקוד שלך: " + this.score,
                JOptionPane.PLAIN_MESSAGE,
                JOptionPane.YES_NO_OPTION,
                pepperIcon,
                options,
                options[0]
        );

        Window parentWindow = SwingUtilities.windowForComponent(this);
        JDialog dialog = new JDialog(parentWindow, title, Dialog.ModalityType.APPLICATION_MODAL);

        dialog.setUndecorated(true);
        dialog.getRootPane().setBorder(BorderFactory.createLineBorder(Color.RED, 8));

        dialog.setContentPane(pane);
        dialog.pack();
        dialog.setLocationRelativeTo(parentWindow);
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        pane.addPropertyChangeListener(e -> {
            if (JOptionPane.VALUE_PROPERTY.equals(e.getPropertyName())) {
                dialog.dispose();
            }
        });

        dialog.setVisible(true);

        Object selectedValue = pane.getValue();

        if (selectedValue != null && selectedValue.equals(options[0])) {
            this.score = 0;
            loadLevel(this.currentLevel);

            Utils.playMusic();
            Utils.syncButtonIcon(this.soundButton);

            return true;
        } else {
            Utils.playMusic();

            if (parentWindow != null) {
                parentWindow.dispose();
            }

            new MainMenu();
            return false;
        }
    }

    // סוגר את חלון המשחק ופותח את התפריט הראשי
    private void closeWindowAndOpenMenu() {
        Window parentWindow = SwingUtilities.windowForComponent(this);

        if (parentWindow != null) {
            parentWindow.dispose();
        }

        new MainMenu();
    }

    // מצייר את כל מסך המשחק
    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        drawGameObjects(graphics);
        drawHud(graphics);
        drawPauseOverlay(graphics);
    }

    // מצייר רקע אויבים עוגות שחקן וסוכריות
    private void drawGameObjects(Graphics graphics) {
        if (this.levelsBackground != null) {
            this.levelsBackground.paint(graphics, this.getWidth(), this.getHeight());
        }

        if (this.enemies != null) {
            for (int i = 0; i < this.enemies.length; i++) {
                if (this.enemies[i] != null) {
                    this.enemies[i].paint(graphics);
                }
            }
        }

        if (this.cakes != null) {
            for (int i = 0; i < cakesCount; i++) {
                if (this.cakes[i] != null) {
                    this.cakes[i].paint(graphics);
                }
            }
        }

        if (this.player != null) {
            this.player.paint(graphics, this.isPaused);
        }

        if (this.prizes != null) {
            for (int i = 0; i < prizes.length; i++) {
                if (prizes[i] != null && !prizes[i].isCollected()) {
                    prizes[i].draw(graphics);
                }
            }
        }
    }

    // מצייר ניקוד שלב וטיימר
    private void drawHud(Graphics graphics) {
        int buttonX = 20;
        int buttonWidth = 50;
        int scoreX = buttonX + buttonWidth + 10;
        int scoreY = 55;

        graphics.setFont(new Font("Arial", Font.BOLD, 30));

        drawTextWithShadow(
                graphics,
                "Score: " + this.score,
                scoreX,
                scoreY,
                new Color(180, 140, 207)
        );

        drawTextWithShadow(
                graphics,
                "Level: " + this.currentLevel,
                scoreX + 200,
                scoreY,
                new Color(180, 244, 255)
        );

        String timeString = getTimeString();

        Color timerColor = Color.WHITE;

        if (this.timeLeft <= 10) {
            timerColor = Color.RED;
        }

        drawTextWithShadow(
                graphics,
                timeString,
                scoreX + 400,
                scoreY,
                timerColor
        );
    }

    // מצייר טקסט עם צל כדי שיהיה קריא יותר
    private void drawTextWithShadow(Graphics graphics, String text, int x, int y, Color color) {
        graphics.setColor(Color.BLACK);
        graphics.drawString(text, x + 2, y + 2);

        graphics.setColor(color);
        graphics.drawString(text, x, y);
    }

    // מחזיר את הזמן בפורמט דקות ושניות
    private String getTimeString() {
        int minutes = this.timeLeft / 60;
        int seconds = this.timeLeft % 60;

        return String.format("Time: %02d:%02d", minutes, seconds);
    }

    // מצייר שכבת עצירה או פתיחת שלב מעל המשחק
    private void drawPauseOverlay(Graphics graphics) {
        if (!isPaused) {
            return;
        }

        graphics.setColor(new Color(0, 0, 0, 200));
        graphics.fillRect(0, 0, getWidth(), getHeight());

        graphics.setColor(Color.WHITE);

        String text;

        if (isLevelStarting) {
            graphics.setFont(new Font("Arial", Font.BOLD, 40));
            text = "PRESS SPACE TO START";
        } else {
            graphics.setFont(new Font("Arial", Font.BOLD, 60));
            text = "PAUSED";
        }

        int x = (getWidth() - graphics.getFontMetrics().stringWidth(text)) / 2;
        int y = getHeight() / 2;

        graphics.drawString(text, x, y);
    }
}