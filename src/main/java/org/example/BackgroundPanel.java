package org.example;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class BackgroundPanel extends JPanel {
    private Image backgroundImage;

    // השינוי כאן: הבנאי מקבל את נתיב התמונה כפרמטר
    public BackgroundPanel(String imagePath) {
        this.setLayout(null);
        this.backgroundImage = loadImage(imagePath);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        if (this.backgroundImage != null) {
            graphics.drawImage(this.backgroundImage, 0, 0, this.getWidth(), this.getHeight(), this);
        }
    }

    private Image loadImage(String path) {
        try {
            URL resource = getClass().getResource(path);
            if (resource != null) {
                return new ImageIcon(resource).getImage();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

