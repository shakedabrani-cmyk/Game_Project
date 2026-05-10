package org.example;

import javax.sound.sampled.*;
import java.net.URL;

public class SoundManager {
    private Clip clip;

    public SoundManager(String filePath) {
        try {
            URL soundURL = getClass().getResource(filePath);
            if (soundURL != null) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(soundURL);
                clip = AudioSystem.getClip();
                clip.open(audioInput);
            } else {
                System.out.println("לא מצאתי את קובץ השמע: " + filePath);
            }
        } catch (Exception e) {
            System.out.println("שגיאה בטעינת המוזיקה: " + filePath);
            e.printStackTrace();
        }
    }

    public void playLoop() {
        if (clip != null) {
            clip.setFramePosition(0);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }
}