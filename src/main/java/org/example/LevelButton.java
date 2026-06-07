package org.example;

import javax.swing.*;
import java.awt.*;

public class LevelButton extends JButton {
    private int level;
    private boolean unlocked;

    public LevelButton(int level, boolean unlocked) {
        super("");
        this.level = level;
        this.unlocked = unlocked;

        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setOpaque(false); //הכפתור לא יצייר רקע רגיל של Java

        if (unlocked) {
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        } else {
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }
    // הפונקציה מציירת את הכפתור, Graphics2D נותן ציור חלק ומתקדם יותר

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D g2 = (Graphics2D) graphics;

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        if (unlocked) {
            g2.setColor(new Color(255, 180, 193));
        } else {
            g2.setColor(new Color(160, 125, 65));
        }

        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);

        g2.setColor(new Color(120, 80, 35));
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(2, 2, getWidth() - 5, getHeight() - 5, 25, 25);

        if (unlocked) {
            drawLevelNumber(g2);
        } else {
            drawLock(g2);
        }
    }

    private void drawLevelNumber(Graphics2D g2) {
        String text = String.valueOf(level);

        g2.setFont(new Font("Arial", Font.BOLD, 28));

        FontMetrics fm = g2.getFontMetrics();

        int textX = (getWidth() - fm.stringWidth(text)) / 2;
        int textY = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();

        g2.setColor(Color.BLACK);
        g2.drawString(text, textX + 2, textY + 2);

        g2.setColor(Color.WHITE);
        g2.drawString(text, textX, textY);
    }

    private void drawLock(Graphics2D g2) {
        int lockWidth = 28;
        int lockHeight = 24;

        int lockX = (getWidth() - lockWidth) / 2;
        int lockY = (getHeight() - lockHeight) / 2 + 7;

        g2.setStroke(new BasicStroke(5));
        g2.setColor(Color.WHITE);
        //מצייר את הקשת של המנעול
        g2.drawArc(
                lockX + 5,
                lockY - 18,
                lockWidth - 10,
                28,
                0,
                180
        );
        //מצייר את הריבוע של המנעול
        g2.fillRoundRect(
                lockX,
                lockY,
                lockWidth,
                lockHeight,
                6,
                6
        );

        g2.setColor(new Color(90, 60, 30));
        // את החור של המנעול
        g2.fillOval(lockX + 11, lockY + 8, 6, 6);
        g2.fillRect(lockX + 13, lockY + 13, 2, 7);
    }
}