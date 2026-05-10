package org.example;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.InputStream;

public class EnemyEggplant extends Enemy {

    public EnemyEggplant(int x, int y, int width, int height) {
        super(x, y, width, height);

        setFrontImage(loadImage("/Eggplant_Front.png"));
        setBackImage(loadImage("/Eggplant_Back.png"));
        setRightImage(loadImage("/Eggplant_Right.png"));
        setLeftImage(loadImage("/Eggplant_Left.png"));

        setCurrentImage(getFrontImage());
        setDirection(DOWN);
    }

    @Override
    public void move() {
        if (!isMoving()) return;
        boolean hitBoundary = false;

        if (getDirection() == DOWN) {
            if (!isAtBottomBoundary()) {
                setCurrentImage(getFrontImage());
                moveVertically(2);
            } else {
                hitBoundary = true;
            }
        } else if (getDirection() == UP) {
            if (!isAtTopBoundary()) {
                setCurrentImage(getBackImage());
                moveVertically(-2);
            } else {
                hitBoundary = true;
            }
        } else {
            setDirection(DOWN);
        }

        if (hitBoundary || getRandom().nextInt(250) == 0) {
            setDirection(getDirection() == DOWN ? UP : DOWN);
        }
    }
}