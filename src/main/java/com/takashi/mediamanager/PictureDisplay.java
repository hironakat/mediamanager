package com.takashi.mediamanager;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PictureDisplay extends Canvas {
    Image   pictureImage;
    int x, y;
    public PictureDisplay(String filename){
        super();
        pictureImage = getToolkit().getImage(filename);
        try {
            BufferedImage bimg = ImageIO.read(new File(filename));
            x = bimg.getWidth();
            y = bimg.getHeight();
        }catch(IOException e){
            errPrint(e);
        }

    }

    @Override
    public void paint(Graphics g) {
        if (pictureImage!=null){
            g.drawImage(pictureImage,0,0,this);
        }
    }

    public int getX(){return x;}
    public int getY(){return y;}

    private void errPrint(Exception e){
        System.err.println("EXCEPTION: " + e);
        e.printStackTrace();
    }
}
