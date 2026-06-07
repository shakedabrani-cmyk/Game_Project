package org.example;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.InputStream;

public class LevelBackground {
    private Image backgroundImage;

    public LevelBackground(){
        try{
            InputStream inputStream = LevelBackground.class.getResourceAsStream("/background_level.jpeg");
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