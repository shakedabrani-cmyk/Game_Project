package org.example;

public class EnemyCorn extends Enemy {

    public EnemyCorn(int x, int y, int width, int height) {
        super(x, y, width, height);

        // משתמשים בפונקציות ה-set כדי לטעון את התמונות למחלקת האב
        setFrontImage(loadImage("/Corn_Front.png"));
        setBackImage(loadImage("/Corn_Back.png"));
        setRightImage(loadImage("/Corn_Right.png"));
        setLeftImage(loadImage("/Corn_Left.png"));

        // מגדירים מצב התחלתי
        setCurrentImage(getFrontImage());
        setDirection(DOWN);
    }
}