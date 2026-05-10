package org.example;

public class EnemyGeneric extends Enemy {

    public EnemyGeneric(int x, int y, int width, int height, String vegType) {
        super(x, y, width, height);

        setFrontImage(loadImage("/" + vegType + "_Front.png"));
        setBackImage(loadImage("/" + vegType + "_Back.png"));
        setRightImage(loadImage("/" + vegType + "_Right.png"));
        setLeftImage(loadImage("/" + vegType + "_Left.png"));

        //  מצב התחלתי
        setCurrentImage(getFrontImage());
        setDirection(DOWN);
    }
}