package org.example;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class Utils {

    public static SoundManager backgroundMusic;
    public static boolean isMusicPlaying = true;

    private static ImageIcon soundOnIcon;
    private static ImageIcon soundOffIcon;

    public static void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    // טוען את מוזיקת הרקע פעם אחת ומפעיל אותה
    public static void initializeMusic(String path) {
        if (backgroundMusic == null) {
            backgroundMusic = new SoundManager(path);
            backgroundMusic.playLoop();
        }
    }

    // עוצר את מוזיקת הרקע
    public static void stopMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.stop();
            isMusicPlaying = false;
        }
    }

    // מפעיל את מוזיקת הרקע מחדש
    public static void playMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.playLoop();
            isMusicPlaying = true;
        }
    }

    //מסנכרן את תמונת כפתור הסאונד לפי מצב המוזיקה
    public static void syncButtonIcon(JButton button) {
        if (isMusicPlaying && soundOnIcon != null) {
            button.setIcon(soundOnIcon);
        } else if (!isMusicPlaying && soundOffIcon != null) {
            button.setIcon(soundOffIcon);
        }
    }

    //יוצר כפתור שמדליק ומכבה מוזיקת רקע
    public static JButton createSoundButton() {
        if (soundOnIcon == null || soundOffIcon == null) {
            soundOnIcon = resizeIcon("/sound_on.png", 50, 50);
            soundOffIcon = resizeIcon("/sound_off.png", 50, 50);
        }

        JButton soundButton = new JButton();
        soundButton.setBounds(20, 20, 50, 50);
        soundButton.setFocusPainted(false);
        soundButton.setContentAreaFilled(false);
        soundButton.setBorderPainted(false);
        soundButton.setFocusable(false);

        syncButtonIcon(soundButton);

        soundButton.addActionListener(e -> {
            if (isMusicPlaying) {
                stopMusic();
            } else {
                playMusic();
            }

            syncButtonIcon(soundButton);
        });

        return soundButton;
    }

    // משנה גודל של אייקון
    private static ImageIcon resizeIcon(String path, int width, int height) {
        URL imgUrl = Utils.class.getResource(path);

        if (imgUrl != null) {
            ImageIcon originalIcon = new ImageIcon(imgUrl);
            Image img = originalIcon.getImage();
            Image resizedImage = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);

            return new ImageIcon(resizedImage);
        }

        System.out.println("לא מצאתי את קובץ האייקון: " + path);
        return null;
    }
}