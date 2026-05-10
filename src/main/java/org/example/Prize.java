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

    // משתנה שזוכר האם הקאפקייק כבר אסף את הסוכריה הזו
    private boolean isCollected;

    public Prize(int x, int y, int width, int height, String imagePath) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.isCollected = false; // בהתחלה שום סוכריה עדיין לא נאספה

        // טעינת התמונה של הסוכריה מתוך תיקיית resources
        URL resource = getClass().getResource(imagePath);
        if (resource != null) {
            this.image = new ImageIcon(resource).getImage();
        } else {
            System.out.println("לא מצאתי את קובץ התמונה של הפרס: " + imagePath);
        }
    }

    // פונקציה שאחראית לצייר את הסוכריה על המסך
    public void draw(Graphics g) {
        // אנחנו נצייר את הסוכריה רק אם היא עדיין לא נאספה!
        if (!isCollected && image != null) {
            g.drawImage(image, x, y, width, height, null);

        }
    }

    public Rectangle getBounds() {
        // אנחנו "מגלחים" פיקסלים מהשוליים של התמונה כדי שהמלבן יהיה צמוד יותר לסוכריה עצמה
        int trimX = 3;  // מורידים פיקסלים משמאל וימין (כי הסוכריה צרה)
        int trimY = 5; // מורידים פיקסלים מלמעלה ולמטה (כדי להתעלם מקצוות העטיפה)

        // יוצרים מלבן חדש: הוא מתחיל קצת יותר פנימה, והרוחב/גובה שלו קטנים יותר
        return new Rectangle(x + trimX, y + trimY, width - (2 * trimX), height - (2 * trimY));
    }

    // --- Getters & Setters ---

    public boolean isCollected() {
        return isCollected;
    }

    public void setCollected(boolean collected) {
        this.isCollected = collected;
    }
}