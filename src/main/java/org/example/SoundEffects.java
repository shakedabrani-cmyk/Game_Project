package org.example;

import javax.sound.sampled.*;
import java.net.URL;

public class SoundEffects {

    // מנגן אפקט קצר פעם אחת
    public static void play(String soundFileName) {
        try {
            URL soundURL = SoundEffects.class.getResource(soundFileName);

            if (soundURL != null) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(soundURL);
                Clip clip = AudioSystem.getClip();

                clip.open(audioInput);
                clip.start();
            } else {
                System.out.println("לא מצאתי את קובץ הסאונד: " + soundFileName);
            }
        } catch (Exception e) {
            System.out.println("שגיאה בניגון אפקט סאונד: " + soundFileName);
            e.printStackTrace();
        }
    }
}