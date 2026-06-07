package org.example;

import javax.swing.*;
import java.awt.*;

public class LevelMapPanel extends BackgroundPanel {
    private static final int LEVEL_BUTTON_WIDTH = 80;
    private static final int LEVEL_BUTTON_HEIGHT = 65;

    private static final int LEVEL_BUTTON_GAP_X = 25;
    private static final int LEVEL_BUTTON_GAP_Y = 22;

    private static final int COLUMNS = 5;
    private static final int START_Y = 300;

    private MainMenu mainMenu;

    public LevelMapPanel(int width, int height, BackgroundPanel menuPanel, MainMenu mainMenu) {
        super("/LevelsBackground.png");

        this.mainMenu = mainMenu;

        this.setBounds(0, 0, width, height);
        this.setLayout(null);

        RoundedButton backButton = RoundedButton.createPanelBackButton(width, mainMenu, menuPanel);
        this.add(backButton);

        RoundedButton exitButton = RoundedButton.createExitButton(width);
        this.add(exitButton);

        createLevelButtons(width);
    }

    private void createLevelButtons(int screenWidth) {
        int totalWidth =
                COLUMNS * LEVEL_BUTTON_WIDTH +
                        (COLUMNS - 1) * LEVEL_BUTTON_GAP_X;

        int startX = (screenWidth - totalWidth) / 2;

        for (int level = 1; level <= GameSettings.MAX_LEVELS; level++) {
            int index = level - 1;

            int row = index / COLUMNS;
            int col = index % COLUMNS;

            int x = startX + col * (LEVEL_BUTTON_WIDTH + LEVEL_BUTTON_GAP_X);
            int y = START_Y + row * (LEVEL_BUTTON_HEIGHT + LEVEL_BUTTON_GAP_Y);

            boolean unlocked = GameProgress.isLevelUnlocked(level);

            LevelButton levelButton = new LevelButton(level, unlocked);
            levelButton.setBounds(x, y, LEVEL_BUTTON_WIDTH, LEVEL_BUTTON_HEIGHT);

            if (unlocked) {
                int selectedLevel = level;

                levelButton.addActionListener(e -> {
                    openGame(selectedLevel);
                });
            }

            this.add(levelButton);
        }
    }

    private void openGame(int selectedLevel) {
        mainMenu.dispose();
        JFrame window = new JFrame("Sugar Rush");
        window.setSize(Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);
        window.setUndecorated(true);
        window.setResizable(false);
        window.setLocationRelativeTo(null);
        window.setLayout(null);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.add(
                new MainScenePanel(
                        0,
                        0,
                        Main.WINDOW_WIDTH,
                        Main.WINDOW_HEIGHT,
                        selectedLevel
                )
        );

        window.setVisible(true);
    }
}