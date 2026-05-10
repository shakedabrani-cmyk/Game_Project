package org.example;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.InputStream;
import java.util.Random;

public class Enemy {

    public static final int RIGHT = 1;
    public static final int LEFT = 2;
    public static final int UP = 3;
    public static final int DOWN = 4;

    private int x;
    private int y;
    private int width;
    private int height;
    private int direction;

    private Image currentImage;
    private Image backImage;
    private Image frontImage;
    private Image rightImage;
    private Image leftImage;

    private boolean isMoving = false;
    private Random random = new Random();

    private int offsetLeft;
    private int offsetRight;
    private int offsetTop;
    private int offsetBottom;

    public Enemy(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.direction = DOWN;
        this.offsetLeft = 50;
        this.offsetRight = 48;
        this.offsetTop = 35;
        this.offsetBottom = 50;
    }

    // הילדים קוראים לפונקציות האלה כדי לזוז, בלי לדעת איך זה עובד מאחורי הקלעים!
    public void moveHorizontally(int amount) {
        this.x += amount;
    }

    public void moveVertically(int amount) {
        this.y += amount;
    }

    // בודק אם האויב על הגבולות
    public boolean isAtRightBoundary() {
        return this.x + this.width >= Main.WINDOW_WIDTH - offsetRight;
    }
    public boolean isAtLeftBoundary() {
        return this.x <= offsetLeft;
    }
    public boolean isAtTopBoundary() {
        return this.y <= offsetTop;
    }
    public boolean isAtBottomBoundary() {
        return this.y + this.height >= Main.WINDOW_HEIGHT - offsetBottom;
    }

    // --- Getters & Setters ---
    public int getX() {return this.x;}
    public int getY() {return this.y;}
    public int getWidth() {return this.width;}
    public int getHeight() {return this.height;}
    public int getDirection() {return this.direction;}
    public void setX(int x) {this.x = x;}
    public void setY(int y) {this.y = y;}
    public void setDirection(int direction) {this.direction = direction;}
    public boolean isMoving() {return this.isMoving;}
    public void setIsMoving(boolean moving) {this.isMoving = moving;}
    public Random getRandom() {return this.random;}

    // ניהול תמונות
    public void setCurrentImage(Image img) {this.currentImage = img;}
    public void setFrontImage(Image img) {this.frontImage = img;}
    public void setBackImage(Image img) {this.backImage = img;}
    public void setRightImage(Image img) {this.rightImage = img;}
    public void setLeftImage(Image img) {this.leftImage = img;}
    public Image getFrontImage() {return this.frontImage;}
    public Image getBackImage() {return this.backImage;}
    public Image getRightImage() {return this.rightImage;}
    public Image getLeftImage() {return this.leftImage;}

    public Rectangle getRect() {
        return new Rectangle(this.x, this.y, this.width, this.height);
    }

    public void move() {
        if (!isMoving) {
            return;
        }
        boolean hitBoundary = false;
        switch (this.direction) {
            case RIGHT:
                if (this.x + this.width < Main.WINDOW_WIDTH - this.offsetRight) {
                    this.currentImage = rightImage;
                    this.x += 2;
                } else {
                    hitBoundary = true;
                }
                break;
            case LEFT:
                if (this.x > this.offsetLeft) {
                    this.currentImage = leftImage;
                    this.x -= 2;
                } else {
                    hitBoundary = true;
                }
                break;
            case UP:
                if (this.y > this.offsetTop) {
                    this.currentImage = backImage;
                    this.y -= 2;
                } else {
                    hitBoundary = true;
                }
                break;
            case DOWN:
                if (this.y + this.height < Main.WINDOW_HEIGHT - this.offsetBottom) {
                    this.currentImage = frontImage;
                    this.y += 2;
                } else {
                    hitBoundary = true;
                }
                break;
        }
        if (random.nextInt(200) == 0) {
            this.direction = random.nextInt(1, 5);
        }

        if (hitBoundary || random.nextInt(250) == 0) {
            reverseDirection();
        }
    }

    public void reverseDirection() {
        switch (this.direction) {
            case RIGHT:
                this.direction = LEFT;
                break;
            case LEFT:
                this.direction = RIGHT;
                break;
            case UP:
                this.direction = DOWN;
                break;
            case DOWN:
                this.direction = UP;
                break;
        }
    }

    public void paint(Graphics graphics) {
        if (this.currentImage != null) {
            graphics.drawImage(this.currentImage, this.x, this.y, this.width, this.height, null);
        }
    }

    public Image loadImage(String imagePath) {
        try {
            InputStream imageStream = getClass().getResourceAsStream(imagePath);
            if (imageStream != null) {
                return ImageIO.read(imageStream);
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}