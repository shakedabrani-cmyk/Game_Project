package org.example;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class Prize {
    private int x;
    private int y;
    private int width;
    private int height;
    private Image image;
    private boolean isCollected;
    private int points;

    public Prize(int x, int y, int width, int height, String imagePath) {
        this(x, y, width, height, imagePath, 10);
    }

    public Prize(int x, int y, int width, int height, String imagePath, int points) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.points = points;
        this.isCollected = false;

        URL resource = getClass().getResource(imagePath);

        if (resource != null) {
            this.image = new ImageIcon(resource).getImage();
        } else {
            System.out.println("לא מצאתי את קובץ התמונה של הפרס: " + imagePath);
        }
    }

    public void draw(Graphics g) {
        if (!isCollected && image != null) {
            g.drawImage(image, x, y, width, height, null);
        }
    }

    public Rectangle getBounds() {
        if (points == 20) {
            int hitWidth = width / 3;
            int hitHeight = (int) (height * 0.70);

            int hitX = x + (width - hitWidth) / 2;
            int hitY = y + 4;

            return new Rectangle(
                    hitX,
                    hitY,
                    hitWidth,
                    hitHeight
            );
        }

        int trimX = 3;
        int trimY = 5;

        return new Rectangle(
                x + trimX,
                y + trimY,
                width - (2 * trimX),
                height - (2 * trimY)
        );
    }

    public int getPoints() {
        return this.points;
    }

    public boolean isCollected() {
        return isCollected;
    }

    public void setCollected(boolean collected) {
        this.isCollected = collected;
    }
}