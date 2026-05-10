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

    private int x;
    private int y;
    private int width;
    private int height;
    private Image currentImage;
    private Image upImage;
    private Image downImage;
    private Image rightImage;
    private Image leftImage;

    // --- משתנים שקשורים רק לגיף ---

    // המערך הרגיל שלנו - פה נשמור את כל התמונות הבודדות (הפריימים) שמרכיבות את הגיף
    private BufferedImage[] frames;

    // משתנה שזוכר באיזו תמונה (איזה תא במערך) אנחנו נמצאים כרגע כדי להציג אותה
    private int currentFrameIndex = 0;

    // טיימר (מונה) שסופר את הזמן שעובר כדי לדעת מתי להחליף לתמונה הבאה במערך
    private int animationCounter = 0;

    // קובע את קצב ההחלפה - כמה זמן נחכה עד שנעבור לתא הבא במערך (אפשר לשנות את המספר כדי להאיץ/להאט)
    private int animationSpeed = 2;

    private boolean wasShowingGif = false;

    private boolean isMoving = false;

    // מקדם הגדלה: מגדיר פי כמה נרצה להגדיל את התמונה של הגיף כדי שתיראה טוב יותר על המסך
    private double gifScaleMultiplier = 2.8;

    // משתנים שישמרו את הרוחב והגובה הסופיים של הגיף אחרי ההגדלה
    private int gifDrawWidth;
    private int gifDrawHeight;

    // משתנים שישמרו את המיקום המדויק בצירים כדי שהגיף יצויר בדיוק באמצע של השחקן
    private int gifOffsetX;
    private int gifOffsetY;

    private long lastMoveTime;

    private int lastDirection = DOWN;


    public Player(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;



        // קוראים לפונקציה שמחשבת את הגודל של הגיף כבר בתחילת המשחק
        updateGifDimensions();

        this.downImage = loadImage("/Front_no background.png");
        this.upImage = loadImage("/Back_no background.png");
        this.rightImage = loadImage("/Right_no background.png");
        this.leftImage = loadImage("/Left_no background.png");

        this.currentImage = this.downImage;
        this.lastMoveTime = System.currentTimeMillis();
        // קוראים לפונקציה שתחלץ את התמונות מה-GIF ישר לתוך המערך שבנינו
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
    public int getWidth(){
        return this.width;
    }
    public int getHeight(){
        return this.height;
    }
    public void setIsMoving(boolean moving) {
        this.isMoving = moving;
    }
    public void updateLastMoveTime() {
        this.lastMoveTime = System.currentTimeMillis();
    }

    private Image loadImage(String imagePath) {
        try {
            InputStream imageStream = getClass().getResourceAsStream(imagePath);
            if (imageStream != null) {
                return ImageIO.read(imageStream);
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    // --- פונקציה שקשורה רק לגיף ---
    private void updateGifDimensions() {
        // חישוב הגודל החדש של הגיף: מכפילים את הרוחב והגובה הרגילים של השחקן במקדם ההגדלה
        this.gifDrawWidth = (int) (this.width * gifScaleMultiplier);
        this.gifDrawHeight = (int) (this.height * gifScaleMultiplier);

        // חישוב המיקום: מוצאים את ההפרש בין גודל השחקן לגודל הגיף, ומחלקים ב-2 כדי שהגיף ישב בול באמצע
        this.gifOffsetX = (this.width - this.gifDrawWidth) / 2;
        this.gifOffsetY = (this.height - this.gifDrawHeight) / 2;
    }


    private int getMovementSpeed(int newDirection) {

        this.lastMoveTime = System.currentTimeMillis();

        int speed = 5;

        if (this.lastDirection != newDirection) {
            speed = 8;
        }

        this.lastDirection = newDirection;

        return speed;
    }

    int offsetRight = 48;
    public void moveRight() {

        int speed = getMovementSpeed(RIGHT);

        if (this.x + this.width < Main.WINDOW_WIDTH - offsetRight) {
            this.x += speed;
        }

        this.currentImage = this.rightImage;
    }

    int offsetLeft = 50;
    public void moveLeft() {

        int speed = getMovementSpeed(LEFT);

        if (this.x > offsetLeft) {
            this.x -= speed;
        }

        this.currentImage = this.leftImage;
    }

    int offsetBottom = 50;
    public void moveDown() {

        int speed = getMovementSpeed(DOWN);

        if (this.y + this.height < Main.WINDOW_HEIGHT - offsetBottom) {
            this.y += speed;
        }

        this.currentImage = this.downImage;
    }

    int offsetTop = 35;
    public void moveUp() {

        int speed = getMovementSpeed(UP);

        if (this.y > offsetTop) {
            this.y -= speed;
        }

        this.currentImage = this.upImage;
    }
    // --- פונקציה שקשורה רק לגיף ---
    private void loadGifFrames(String path) {
        try {
            InputStream is = getClass().getResourceAsStream(path);
            if (is != null) {
                // פותחים זרם נתונים לקריאת קובץ התמונה
                ImageInputStream stream = ImageIO.createImageInputStream(is);
                // מבקשים מ-Java כלי שמיועד ספציפית לפענוח קבצי GIF
                Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName("gif");

                if (readers.hasNext()) {
                    ImageReader reader = readers.next();
                    reader.setInput(stream);

                    // השלב הקריטי: שואלים את הכלי כמה תמונות (פריימים) יש בסך הכל בגיף הזה
                    int count = reader.getNumImages(true);

                    // יוצרים את המערך הרגיל שלנו, בדיוק בגודל של כמות התמונות שמצאנו
                    this.frames = new BufferedImage[count];

                    // עוברים בלולאה פשוטה מ-0 ועד כמות התמונות
                    for (int i = 0; i < count; i++) {
                        // קוראים את התמונה הנוכחית ושומרים אותה בתוך התא המתאים (i) במערך שלנו
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

    // --- פונקציה שקשורה רק לגיף ---
    private void updateAnimation() {
        // אם המערך ריק או שיש בו רק תמונה אחת, אין טעם באנימציה אז יוצאים מהפונקציה
        if (this.frames == null || this.frames.length <= 1) return;

        // מגדילים את המונה שלנו בעוד צעד
        this.animationCounter++;

        // בודקים אם המונה הגיע למהירות שהגדרנו. אם כן, הגיע הזמן להחליף תמונה!
        if (this.animationCounter >= this.animationSpeed) {

            // מאפסים את המונה חזרה ל-0 לקראת התמונה הבאה
            this.animationCounter = 0;

            // מקדמים את האינדקס באחד, כדי שנעבור לתא הבא במערך (התמונה הבאה)
            this.currentFrameIndex++;

            // בודקים: האם הגענו לסוף המערך? אם כן, מאפסים את האינדקס ל-0 כדי להתחיל מחדש את האנימציה
            if (this.currentFrameIndex >= this.frames.length) {
                this.currentFrameIndex = 0;
            }
        }
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
        // גם כאן אנחנו קוראים לפונקציה של הגיף כדי לעדכן את המידות שלו אם גודל השחקן משתנה
        updateGifDimensions();
    }

    public void paint(Graphics graphics , boolean isPaused) {

        long idleTime = System.currentTimeMillis() - this.lastMoveTime;

        boolean shouldShowGif = (idleTime >= 1000);

        // --- התיקון החדש לבעיית הקפיצה ---
        if (isPaused) {
            // אם המשחק בעצירה, אנחנו מתעלמים מהזמן ומשתמשים בזיכרון שלנו
            shouldShowGif = this.wasShowingGif;

            // טריק: אם היינו בתמונת תנועה כשעצרנו, נמשוך את זמן התזוזה קדימה
            // כדי שברגע שנחזור מהעצירה, הטיימר לא יקפוץ מיד לגיף!
            if (!shouldShowGif) {
                this.lastMoveTime = System.currentTimeMillis();
            }
        } else {
            // אם המשחק רץ, נשמור את המצב הנוכחי בזיכרון למקרה שנעצור פתאום
            this.wasShowingGif = shouldShowGif;
        }
        // ------------------------------------

        // שלב ב': ציור תמונת התנועה הסטטית (אם הוחלט לא להראות גיף)
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
            return; // סיימנו לצייר, יוצאים מהפונקציה
        }

        // שלב ג': ציור אנימציית הגיף
        if (!isPaused) {
            updateAnimation(); // מקדמים את האנימציה רק אם אנחנו לא בעצירה
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

    public Rectangle getRect(){
        Rectangle rectangle = new Rectangle(this.x, this.y, this.width, this.height);
        return rectangle;
    }
}