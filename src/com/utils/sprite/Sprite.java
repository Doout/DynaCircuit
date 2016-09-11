package com.utils.sprite;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class Sprite {
    /**
     * TODO Make this class use less memory over all by only making one copy of
     * the image
     */

    private static HashMap<Integer, String> IDLIST;
    private static Sprite[] list;
    private BufferedImage sprite;
    private BufferedImage spriteIcon;
    private Rectangle rect;

    private Sprite() {// NO call from outside from class
    }

    public BufferedImage getImage() {
        return sprite;
    }

    /**
     * @return If there is no icon loaded for this sprite return the regular sprite.
     **/
    public BufferedImage getIconImage() {
        if (spriteIcon == null)
            return sprite;
        return spriteIcon;
    }

    /**@return The bound of the sprite*/
    public Rectangle getBounds() {
        return rect;
    }

    private void makeBound() {
        rect = new Rectangle(0, 0, sprite.getWidth(), sprite.getHeight());
    }

    public static void setIDList(HashMap<Integer, String> IDLIST) {
        Sprite.IDLIST = IDLIST;
        Sprite.list = new Sprite[IDLIST.size()];
    }

    public static Sprite getSprite(int id) {
        if (list == null)
            return null;
        if (list[id] == null)
            list[id] = loadSprite(IDLIST.get(id));
        return list[id];

    }

    private static Sprite loadSprite(String fileName) {
        InputStream in = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream(fileName);
        InputStream in2 = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream(fileName.substring(0, fileName.length() - 3)
                        + "-icon.png");
        if (in == null) {
            System.out.println("can't load " + fileName + "\n");
            return null;
        }
        Sprite s = new Sprite();
        try {
            s.sprite = ImageIO.read(in);
            if (in2 != null)
                s.spriteIcon = ImageIO.read(in2);
            s.makeBound();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return s;
    }
}
