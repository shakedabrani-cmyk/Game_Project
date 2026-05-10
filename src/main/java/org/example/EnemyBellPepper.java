package org.example;

public class EnemyBellPepper extends Enemy {

    private Player targetPlayer;

    public EnemyBellPepper(int x, int y, int width, int height, Player player) {
        super(x, y, width, height);
        this.targetPlayer = player;

        setFrontImage(loadImage("/BellPepper_Front.png"));
        setBackImage(loadImage("/BellPepper_Back.png"));
        setRightImage(loadImage("/BellPepper_Right.png"));
        setLeftImage(loadImage("/BellPepper_Left.png"));
        setCurrentImage(getFrontImage());
    }

    @Override
    public void move() {
        if (!isMoving() || this.targetPlayer == null) return;

        int diffX = this.targetPlayer.getX() - this.getX();
        int diffY = this.targetPlayer.getY() - this.getY();

        if (Math.abs(diffX) > Math.abs(diffY)) { // הוא לא זז באלכסון, ולכן בודק איזה מרחק גדול יותר
            if (diffX > 0) {
                setDirection(RIGHT);
                setCurrentImage(getRightImage());
                if (!isAtRightBoundary()) {
                    moveHorizontally(1);
                }
            } else if (diffX < 0) {
                setDirection(LEFT);
                setCurrentImage(getLeftImage());
                if (!isAtLeftBoundary()) {
                    moveHorizontally(-1);
                }
            }
        } else {
            if (diffY > 0) {
                setDirection(DOWN);
                setCurrentImage(getFrontImage());
                if (!isAtBottomBoundary()) {
                    moveVertically(1);
                }
            } else if (diffY < 0) {
                setDirection(UP);
                setCurrentImage(getBackImage());
                if (!isAtTopBoundary()) {
                    moveVertically(-1);
                }
            }
        }
    }
}