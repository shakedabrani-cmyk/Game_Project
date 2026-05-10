package org.example;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.net.URL;

public class LevelsBackground {
    private Image backgroundImage;

    public LevelsBackground(){
        try{
            InputStream inputStream = LevelsBackground.class.getResourceAsStream("/background_level.jpeg");
            this.backgroundImage = ImageIO.read(inputStream);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void paint(Graphics graphics, int width, int height){
        if(this.backgroundImage != null){
            graphics.drawImage(this.backgroundImage, 0,0,width,height,null);
        }
    }
}