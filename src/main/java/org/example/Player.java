package org.example;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.Iterator;

public class Player {
    private static final int RIGHT = 1;
    private static final int LEFT = 2;
    private static final int UP = 3;
    private static final int DOWN = 4;

    private static final int OFFSET_RIGHT = GameSettings.WALL_RIGHT;
    private static final int OFFSET_LEFT = GameSettings.WALL_LEFT;
    private static final int OFFSET_BOTTOM = GameSettings.WALL_BOTTOM;
    private static final int OFFSET_TOP = GameSettings.WALL_TOP;

    private int x;
    private int y;
    private int width;
    private int height;

    private Image currentImage;
    private Image upImage;
    private Image downImage;
    private Image rightImage;
    private Image leftImage;

    private BufferedImage[] frames;
    private int currentFrameIndex = 0;
    private int animationCounter = 0;
    private int animationSpeed = 2;

    private boolean wasShowingGif = false;
    private boolean isMoving = false;

    private double gifScaleMultiplier = 2.8;

    private int gifDrawWidth;
    private int gifDrawHeight;
    private int gifOffsetX;
    private int gifOffsetY;

    private long lastMoveTime;
    private int lastDirection = DOWN;

    public Player(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        updateGifDimensions();

        this.downImage = loadImage("/Front_no background.png");
        this.upImage = loadImage("/Back_no background.png");
        this.rightImage = loadImage("/Right_no background.png");
        this.leftImage = loadImage("/Left_no background.png");

        this.currentImage = this.downImage;
        this.lastMoveTime = System.currentTimeMillis();

        loadGifFrames("/cupcake.gif");
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setIsMoving(boolean moving) {
        this.isMoving = moving;
    }

    public void updateLastMoveTime() {
        this.lastMoveTime = System.currentTimeMillis();
    }

    // טוען תמונה מתוך תיקיית המשאבים
    private Image loadImage(String imagePath) {
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

    // מחשב את הגודל והמיקום של הגיף ביחס לשחקן
    private void updateGifDimensions() {
        this.gifDrawWidth = (int) (this.width * gifScaleMultiplier);
        this.gifDrawHeight = (int) (this.height * gifScaleMultiplier);

        this.gifOffsetX = (this.width - this.gifDrawWidth) / 2;
        this.gifOffsetY = (this.height - this.gifDrawHeight) / 2;
    }

    // מחזיר מהירות תנועה רגילה או מהירה יותר בזמן שינוי כיוון
    private int getMovementSpeed(int newDirection) {
        this.lastMoveTime = System.currentTimeMillis();

        int speed = 5;

        if (this.lastDirection != newDirection) {
            speed = 8;
        }

        this.lastDirection = newDirection;

        return speed;
    }

    // מזיז את השחקן ימינה אם הוא לא עבר את גבול המסך
    public void moveRight() {
        int speed = getMovementSpeed(RIGHT);

        if (this.x + this.width < Main.WINDOW_WIDTH - OFFSET_RIGHT) {
            this.x += speed;
        }

        this.currentImage = this.rightImage;
    }

    // מזיז את השחקן שמאלה אם הוא לא עבר את גבול המסך
    public void moveLeft() {
        int speed = getMovementSpeed(LEFT);

        if (this.x > OFFSET_LEFT) {
            this.x -= speed;
        }

        this.currentImage = this.leftImage;
    }

    // מזיז את השחקן למטה אם הוא לא עבר את גבול המסך
    public void moveDown() {
        int speed = getMovementSpeed(DOWN);

        if (this.y + this.height < Main.WINDOW_HEIGHT - OFFSET_BOTTOM) {
            this.y += speed;
        }

        this.currentImage = this.downImage;
    }

    // מזיז את השחקן למעלה אם הוא לא עבר את גבול המסך
    public void moveUp() {
        int speed = getMovementSpeed(UP);

        if (this.y > OFFSET_TOP) {
            this.y -= speed;
        }

        this.currentImage = this.upImage;
    }

    // מפרק את קובץ הגיף לפריימים ושומר אותם במערך
    private void loadGifFrames(String path) {
        try {
            InputStream is = getClass().getResourceAsStream(path);

            if (is != null) {
                ImageInputStream stream = ImageIO.createImageInputStream(is);
                Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName("gif");

                if (readers.hasNext()) {
                    ImageReader reader = readers.next();
                    reader.setInput(stream);

                    int count = reader.getNumImages(true);
                    this.frames = new BufferedImage[count];

                    for (int i = 0; i < count; i++) {
                        this.frames[i] = reader.read(i);
                    }
                }
            } else {
                System.out.println("לא מצאתי את הקובץ: " + path);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // מעביר את הגיף לפריים הבא לפי קצב האנימציה
    private void updateAnimation() {
        if (this.frames == null || this.frames.length <= 1) {
            return;
        }

        this.animationCounter++;

        if (this.animationCounter >= this.animationSpeed) {
            this.animationCounter = 0;
            this.currentFrameIndex++;

            if (this.currentFrameIndex >= this.frames.length) {
                this.currentFrameIndex = 0;
            }
        }
    }

    // משנה את גודל השחקן ומחשב מחדש את גודל הגיף
    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
        updateGifDimensions();
    }

    // מצייר את השחקן כתמונה בזמן תנועה או כגיף אחרי עמידה במקום
    public void paint(Graphics graphics, boolean isPaused) {
        long idleTime = System.currentTimeMillis() - this.lastMoveTime;

        boolean shouldShowGif = idleTime >= 1000;

        if (isPaused) {
            shouldShowGif = this.wasShowingGif;

            if (!shouldShowGif) {
                this.lastMoveTime = System.currentTimeMillis();
            }
        } else {
            this.wasShowingGif = shouldShowGif;
        }

        if (!shouldShowGif) {
            if (this.currentImage != null) {
                graphics.drawImage(
                        this.currentImage,
                        this.x,
                        this.y,
                        this.width,
                        this.height,
                        null
                );
            }

            return;
        }

        if (!isPaused) {
            updateAnimation();
        }

        if (this.frames != null && this.frames.length > 0) {
            graphics.drawImage(
                    this.frames[currentFrameIndex],
                    this.x + this.gifOffsetX,
                    this.y + this.gifOffsetY,
                    this.gifDrawWidth,
                    this.gifDrawHeight,
                    null
            );
        }
    }

    // מחזיר מלבן פגיעה של השחקן
    public Rectangle getRect() {
        return new Rectangle(this.x, this.y, this.width, this.height);
    }
}