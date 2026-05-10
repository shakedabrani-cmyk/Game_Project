package org.example;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.InputStream;

public class EnemyBroccoli extends Enemy {

    public EnemyBroccoli(int x, int y, int width, int height) {
        super(x, y, width, height);

        setFrontImage(loadImage("/Broccoli_Front.png"));
        setBackImage(loadImage("/Broccoli_Back.png"));
        setRightImage(loadImage("/Broccoli_Right.png"));
        setLeftImage(loadImage("/Broccoli_Left.png"));
        setCurrentImage(getRightImage());
        setDirection(RIGHT);
    }

    @Override
    public void move() {
        if (!isMoving()) return;
        boolean hitBoundary = false;
        if (getDirection() == RIGHT) {
            if (!isAtRightBoundary()) {
                setCurrentImage(getRightImage());
                moveHorizontally(2);
            } else {
                hitBoundary = true;
            }
        } else if (getDirection() == LEFT) {
            if (!isAtLeftBoundary()) {
                setCurrentImage(getLeftImage());
                moveHorizontally(-2);
            } else {
                hitBoundary = true;
            }
        } else {
            setDirection(RIGHT);
        }

        if (hitBoundary || getRandom().nextInt(250) == 0) {
            setDirection(getDirection() == RIGHT ? LEFT : RIGHT);
        }
    }
}