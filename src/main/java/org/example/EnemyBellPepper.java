package org.example;

public class EnemyBellPepper extends Enemy {

    private Player targetPlayer;
    private int confusedTimer = 0; // מונה בלבול כשהגמבה נתקעת בקיר

    public EnemyBellPepper(int x, int y, int width, int height, Player player) {
        super(x, y, width, height);
        this.targetPlayer = player;

        setFrontImage(loadImage("/BellPepper_Front.png"));
        setBackImage(loadImage("/BellPepper_Back.png"));
        setRightImage(loadImage("/BellPepper_Right.png"));
        setLeftImage(loadImage("/BellPepper_Left.png"));
        setCurrentImage(getFrontImage());
    }

    // פונקציה שהמשחק קורא לה כדי להודיע לגמבה שהיא נתקעה
    public void suspendTracking(int frames) {
        this.confusedTimer = frames;
    }

    @Override
    public void move() {
        if (!isMoving() || this.targetPlayer == null) return;
// אם הגמבה נתקעה בקיר, זה עושה שלא תזוז לכיוון שהפנו אותה
        if (confusedTimer > 0) {
            confusedTimer--;
            if (getDirection() == RIGHT && !isAtRightBoundary()) moveHorizontally(1);
            else if (getDirection() == LEFT && !isAtLeftBoundary()) moveHorizontally(-1);
            else if (getDirection() == UP && !isAtTopBoundary()) moveVertically(-1);
            else if (getDirection() == DOWN && !isAtBottomBoundary()) moveVertically(1);
            return;
        }
        // חישוב מרחקים אנכי ואופקי בין הגמבה לשחקן
        int diffX = this.targetPlayer.getX() - this.getX();
        int diffY = this.targetPlayer.getY() - this.getY();

        if (Math.abs(diffX) > Math.abs(diffY)) {
            if (diffX > 0) {
                setDirection(RIGHT);
                setCurrentImage(getRightImage());
                if (!isAtRightBoundary()) moveHorizontally(1);
            } else if (diffX < 0) {
                setDirection(LEFT);
                setCurrentImage(getLeftImage());
                if (!isAtLeftBoundary()) moveHorizontally(-1);
            }
        } else {
            if (diffY > 0) {
                setDirection(DOWN);
                setCurrentImage(getFrontImage());
                if (!isAtBottomBoundary()) moveVertically(1);
            } else if (diffY < 0) {
                setDirection(UP);
                setCurrentImage(getBackImage());
                if (!isAtTopBoundary()) moveVertically(-1);
            }
        }
    }
}