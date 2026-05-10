package org.example;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.InputStream;
import java.util.Random;

public class Cake{
    private int x;
    private int y;
    private int width;
    private int height;
    private Image cakeImage;

    public Cake(int x, int y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        String[] imagePaths = {"/cake1.png", "/cake2.png", "/cake3.png", "/cake4.png"};
        Random random = new Random();
        int randomIndex = random.nextInt(imagePaths.length);
        try{
            String selectedImagePath = imagePaths[randomIndex];
            InputStream inputStream = Cake.class.getResourceAsStream(selectedImagePath);
            this.cakeImage = ImageIO.read(inputStream);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void paint(Graphics graphics){
        if(this.cakeImage != null){
            graphics.drawImage(this.cakeImage, this.x, this.y, this.width, this.height, null);
        }
    }
    public Rectangle getRect(){
        Rectangle rectangle = new Rectangle(this.x, this.y, this.width, this.height);
        return rectangle;
    }
}