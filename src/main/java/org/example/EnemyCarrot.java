package org.example;

public class EnemyCarrot extends Enemy {

    public EnemyCarrot(int x, int y, int width, int height) {
        super(x, y, width, height);

        // משתמשים בפונקציות ה-set כדי לטעון את התמונות למחלקת האב
        setFrontImage(loadImage("/Carrot_Front.png"));
        setBackImage(loadImage("/Carrot_Back.png"));
        setRightImage(loadImage("/Carrot_Right.png"));
        setLeftImage(loadImage("/Carrot_Left.png"));

        // מגדירים מצב התחלתי
        setCurrentImage(getFrontImage());
        setDirection(DOWN);
    }
}