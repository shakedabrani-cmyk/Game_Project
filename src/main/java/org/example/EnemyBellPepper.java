package org.example;

public class EnemyBellPepper extends Enemy {

    private Player targetPlayer;

    private int rightBlockedTimer = 0;
    private int leftBlockedTimer = 0;
    private int upBlockedTimer = 0;
    private int downBlockedTimer = 0;

    private int escapeDirection = 0;
    private int escapeTimer = 0;

    //יוצר פלפל חדש שומר את השחקן כמטרה וטוען תמונות לפי כיוון
    public EnemyBellPepper(int x, int y, int width, int height, Player player) {
        super(x, y, width, height);
        this.targetPlayer = player;

        setFrontImage(loadImage("/BellPepper_Front.png"));
        setBackImage(loadImage("/BellPepper_Back.png"));
        setRightImage(loadImage("/BellPepper_Right.png"));
        setLeftImage(loadImage("/BellPepper_Left.png"));
        setCurrentImage(getFrontImage());
    }
    //מופעל כשהפלפל נתקע שומר את הכיוון החסום ובוחר כיוון עקיפה זמני
    public void suspendTracking(int frames) {
        int blockedDirection = getDirection();

        blockDirection(blockedDirection, frames);

        this.escapeDirection = chooseEscapeDirection(blockedDirection);
        this.escapeTimer = frames;
    }
    //מריץ את תנועת הפלפל בכל פריים קודם עקיפה אם קיימת אחרת רדיפה אחרי השחקן
    @Override
    public void move() {
        if (!isMoving() || this.targetPlayer == null) {
            return;
        }

        decreaseBlockedTimers();

        if (escapeTimer > 0) {
            escapeTimer--;

            if (tryMove(escapeDirection)) {
                return;
            }

            escapeTimer = 0;
        }

        int diffX = this.targetPlayer.getX() - this.getX();
        int diffY = this.targetPlayer.getY() - this.getY();

        int firstDirection;
        int secondDirection;
        int thirdDirection;
        int fourthDirection;

        if (Math.abs(diffX) > Math.abs(diffY)) {
            firstDirection = diffX > 0 ? RIGHT : LEFT;
            secondDirection = diffY > 0 ? DOWN : UP;
            thirdDirection = getOppositeDirection(secondDirection);
            fourthDirection = getOppositeDirection(firstDirection);
        } else {
            firstDirection = diffY > 0 ? DOWN : UP;
            secondDirection = diffX > 0 ? RIGHT : LEFT;
            thirdDirection = getOppositeDirection(secondDirection);
            fourthDirection = getOppositeDirection(firstDirection);
        }

        if (tryMove(firstDirection)) {
            return;
        }

        if (tryMove(secondDirection)) {
            return;
        }

        if (tryMove(thirdDirection)) {
            return;
        }

        tryMove(fourthDirection);
    }

    //בוחר כיוון עקיפה לפי הכיוון שנחסם והמיקום של השחקן
    private int chooseEscapeDirection(int blockedDirection) {
        int diffX = this.targetPlayer.getX() - this.getX();
        int diffY = this.targetPlayer.getY() - this.getY();

        if (blockedDirection == UP || blockedDirection == DOWN) {
            if (diffX >= 0 && !isDirectionBlocked(RIGHT)) {
                return RIGHT;
            }

            if (diffX < 0 && !isDirectionBlocked(LEFT)) {
                return LEFT;
            }

            if (!isDirectionBlocked(RIGHT)) {
                return RIGHT;
            }

            return LEFT;
        }

        if (blockedDirection == LEFT || blockedDirection == RIGHT) {
            if (diffY >= 0 && !isDirectionBlocked(DOWN)) {
                return DOWN;
            }

            if (diffY < 0 && !isDirectionBlocked(UP)) {
                return UP;
            }

            if (!isDirectionBlocked(DOWN)) {
                return DOWN;
            }

            return UP;
        }

        return DOWN;
    }

    //מסמן כיוון מסוים כחסום למשך מספר פריימים
    private void blockDirection(int direction, int frames) {
        if (direction == RIGHT) {
            rightBlockedTimer = frames;
        } else if (direction == LEFT) {
            leftBlockedTimer = frames;
        } else if (direction == UP) {
            upBlockedTimer = frames;
        } else if (direction == DOWN) {
            downBlockedTimer = frames;
        }
    }
    //מוריד כל פריים את זמן החסימה מכל כיוון
    private void decreaseBlockedTimers() {
        if (rightBlockedTimer > 0) {
            rightBlockedTimer--;
        }

        if (leftBlockedTimer > 0) {
            leftBlockedTimer--;
        }

        if (upBlockedTimer > 0) {
            upBlockedTimer--;
        }

        if (downBlockedTimer > 0) {
            downBlockedTimer--;
        }
    }
    //מנסה להזיז את הפלפל לכיוון מסוים מעדכן תמונה ומחזיר אם התנועה הצליחה
    private boolean tryMove(int direction) {
        if (isDirectionBlocked(direction)) {
            return false;
        }

        if (direction == RIGHT) {
            if (isAtRightBoundary()) {
                return false;
            }

            setDirection(RIGHT);
            setCurrentImage(getRightImage());
            moveHorizontally(1);
            return true;
        }

        if (direction == LEFT) {
            if (isAtLeftBoundary()) {
                return false;
            }

            setDirection(LEFT);
            setCurrentImage(getLeftImage());
            moveHorizontally(-1);
            return true;
        }

        if (direction == UP) {
            if (isAtTopBoundary()) {
                return false;
            }

            setDirection(UP);
            setCurrentImage(getBackImage());
            moveVertically(-1);
            return true;
        }

        if (direction == DOWN) {
            if (isAtBottomBoundary()) {
                return false;
            }

            setDirection(DOWN);
            setCurrentImage(getFrontImage());
            moveVertically(1);
            return true;
        }

        return false;
    }
    //בודק אם הכיוון המבוקש חסום כרגע
    private boolean isDirectionBlocked(int direction) {
        if (direction == RIGHT) {
            return rightBlockedTimer > 0;
        }

        if (direction == LEFT) {
            return leftBlockedTimer > 0;
        }

        if (direction == UP) {
            return upBlockedTimer > 0;
        }

        if (direction == DOWN) {
            return downBlockedTimer > 0;
        }

        return false;
    }
    //מחזיר את הכיוון ההפוך לכיוון שנשלח
    private int getOppositeDirection(int direction) {
        if (direction == RIGHT) {
            return LEFT;
        }

        if (direction == LEFT) {
            return RIGHT;
        }

        if (direction == UP) {
            return DOWN;
        }

        return UP;
    }
}