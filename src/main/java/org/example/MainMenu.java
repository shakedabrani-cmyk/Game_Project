package org.example;

import javax.swing.*;
import java.awt.*;

public class MainMenu extends JFrame {
    private static final int TITLE_WIDTH = 800;
    private static final int TITLE_HEIGHT = 150;
    private static final int TITLE_Y_POSITION = 150;
    private static final int TITLE_FONT_SIZE = 100;
    private static final int BUTTON_WIDTH = 300;
    private static final int BUTTON_HEIGHT = 70;
    private static final int START_BUTTON_Y = 400;
    private static final int INSTRUCTIONS_BUTTON_Y = 500;
    private static final int BUTTON_FONT_SIZE = 30;

    private static final Color TITLE_COLOR = new Color(205, 92, 92);
    private static final Color BUTTON_COLOR = new Color(255, 182, 193);

    private static final String MUSIC_PATH = "/The_Victory_Lap.wav";

    public MainMenu() {
        this.setTitle("Main menu- Sugar Rush");
        this.setSize(Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLocationRelativeTo(null);

        Utils.initializeMusic(MUSIC_PATH);

        BackgroundPanel backgroundPanel = new BackgroundPanel();
        this.setContentPane(backgroundPanel);

        JLabel titleLabel = new JLabel("Sugar Rush", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Comic Sans MS", Font.BOLD, TITLE_FONT_SIZE));
        titleLabel.setForeground(TITLE_COLOR);
        int titleX = (Main.WINDOW_WIDTH - TITLE_WIDTH) / 2;
        titleLabel.setBounds(titleX, TITLE_Y_POSITION, TITLE_WIDTH, TITLE_HEIGHT);
        backgroundPanel.add(titleLabel);

        int buttonX = (Main.WINDOW_WIDTH - BUTTON_WIDTH) / 2;
        JButton startButton = new JButton("Start");
        startButton.setFont(new Font("Arial", Font.BOLD, BUTTON_FONT_SIZE));
        startButton.setBackground(BUTTON_COLOR);
        startButton.setForeground(Color.WHITE);
        startButton.setFocusPainted(false);
        startButton.setBounds(buttonX, START_BUTTON_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
        startButton.addActionListener(e -> startGame(backgroundPanel));
        backgroundPanel.add(startButton);

        JButton instructionButton = new JButton("How To Play");
        instructionButton.setFont(new Font("Arial", Font.BOLD, BUTTON_FONT_SIZE));
        instructionButton.setBackground(BUTTON_COLOR);
        instructionButton.setForeground(Color.WHITE);
        instructionButton.setFocusPainted(false);
        instructionButton.setBounds(buttonX, INSTRUCTIONS_BUTTON_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
        backgroundPanel.add(instructionButton);

        JButton soundButton = Utils.createSoundButton();
        backgroundPanel.add(soundButton);

        this.revalidate();
        this.repaint();
        this.setVisible(true);
    }

    private void startGame(BackgroundPanel backgroundPanel) {
        backgroundPanel.stopAnimation();
        this.dispose();
        JFrame window = new JFrame("Sugar Rush");
        window.setSize(Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);
        window.setResizable(false);
        window.setLocationRelativeTo(null);
        window.setLayout(null);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.add(new MainScenePanel(0, 0, Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT));
        window.setVisible(true);
    }
}