package org.example;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MovementListener implements KeyListener {
    private Player player;
    private MainScenePanel panel; // הוספנו את לוח המשחק כדי שנוכל לבדוק התנגשות בעוגות

    // עדכנו את הבנאי כך שיקבל גם את הלוח וגם את השחקן
    public MovementListener(MainScenePanel panel, Player player) {
        this.panel = panel;
        this.player = player;
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        if (this.panel.isPaused()) {
            return;
        }

        // 1. שומרים את המיקום הישן של השחקן לפני התזוזה
        int oldX = this.player.getX();
        int oldY = this.player.getY();

        if (e.getKeyCode() == KeyEvent.VK_RIGHT ||
                e.getKeyCode() == KeyEvent.VK_LEFT||
                e.getKeyCode() == KeyEvent.VK_DOWN||
                e.getKeyCode() == KeyEvent.VK_UP) {
            this.player.setIsMoving(true);
        }

        // 2. מזיזים את השחקן לפי החץ שנלחץ
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            this.player.moveRight();
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            this.player.moveLeft();
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            this.player.moveDown();
        } else if (e.getKeyCode() == KeyEvent.VK_UP) {
            this.player.moveUp();
        }

        // 3. --- בדיקת ההתנגשות! ---
        // שואלים את הלוח אם השחקן נגע עכשיו בעוגה
        if (this.panel.checkCakeCollision()) {
            // אם כן, מחזירים אותו מיד למיקום הישן (כך שהוא "ייתקע" בקיר)
            this.player.setX(oldX);
            this.player.setY(oldY);
        }
    }

    public void keyReleased(KeyEvent e) {
        if (this.panel.isPaused()) {
            return;
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT ||
                e.getKeyCode() == KeyEvent.VK_LEFT ||
                e.getKeyCode() == KeyEvent.VK_DOWN ||
                e.getKeyCode() == KeyEvent.VK_UP) {
            this.player.setIsMoving(false);
        }
    }
}