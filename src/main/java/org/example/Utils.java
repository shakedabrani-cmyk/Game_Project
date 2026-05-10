package org.example;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class Utils {

    public static SoundManager backgroundMusic;
    public static boolean isMusicPlaying = true;

    // משתנים לשמירת התמונות כדי שלא נצטרך לטעון אותן מחדש כל פעם
    private static ImageIcon soundOnIcon;
    private static ImageIcon soundOffIcon;

    public static void sleep(int milliseconds){
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void initializeMusic(String path) {
        if (backgroundMusic == null) // צריך לבדוק
        {
            backgroundMusic = new SoundManager(path);
            backgroundMusic.playLoop();
        }
    }

    // פונקציית הקסם: יוצרת ומחזירה כפתור מוזיקה פעיל לחלוטין!
    public static JButton createSoundButton() {
        if (soundOnIcon == null || soundOffIcon == null) //צריך לבדוק
        {
            soundOnIcon = resizeIcon("/sound_on.png", 50, 50);
            soundOffIcon = resizeIcon("/sound_off.png", 50, 50);
        }
        JButton soundButton = new JButton();
        soundButton.setBounds(20, 20, 50, 50);
        soundButton.setFocusPainted(false);
        soundButton.setContentAreaFilled(false);
        soundButton.setBorderPainted(false);
        soundButton.setFocusable(false);

        // הגדרת תמונה התחלתית לפי המצב הנוכחי של המוזיקה
        if (isMusicPlaying && soundOnIcon != null) {
            soundButton.setIcon(soundOnIcon);
        } else if (!isMusicPlaying && soundOffIcon != null) {
            soundButton.setIcon(soundOffIcon);
        } else {
            soundButton.setText(isMusicPlaying ? "Sound" : "Mute");
            soundButton.setContentAreaFilled(true);
            soundButton.setBackground(Color.WHITE);
        }

        // פעולת הלחיצה
        soundButton.addActionListener(e -> {
            if (isMusicPlaying) {
                backgroundMusic.stop();
                if (soundOffIcon != null) soundButton.setIcon(soundOffIcon);
                else soundButton.setText("Mute");
                isMusicPlaying = false;
            } else {
                backgroundMusic.playLoop();
                if (soundOnIcon != null) soundButton.setIcon(soundOnIcon);
                else soundButton.setText("Sound");
                isMusicPlaying = true;
            }
        });
        return soundButton;
    }

    // העברנו את זה לכאן כי זו פונקציית עזר כללית
    private static ImageIcon resizeIcon(String path, int width, int height) {
        URL imgUrl = Utils.class.getResource(path);
        if (imgUrl != null) {
            ImageIcon originalIcon = new ImageIcon(imgUrl);
            Image img = originalIcon.getImage();
            Image resizedImage = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(resizedImage);
        } else {
            System.out.println("לא מצאתי את קובץ האייקון: " + path);
            return null;
        }
    }
}