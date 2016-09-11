package com.menu;

import java.awt.*;

public class MenuItem extends Menu {

    private String name;
    final static int WIDTH = 60;
    final static int HEIGHT = 50;
    private RenderMenuItem sprite;
    // the offset of the icon. So that the icon is center
    private int yIconOffset;
    private int xIconOffset;
    private int xTextOffset;
    private int yTextOffset;
    private Font font;
    private boolean hover;

    public MenuItem(String hoverName, int x, int y, RenderMenuItem icon) {
        this.name = hoverName;
        this.setLocation(x, y);
        this.setBounds(WIDTH, HEIGHT);
        yTextOffset = HEIGHT - 5;
        font = new Font("TimesRoman", Font.PLAIN, 10);
        if (icon != null && icon.getSprite().getIconImage() != null) {
            this.sprite = icon;
            xIconOffset = WIDTH / 2 - icon.getSprite().getIconImage().getWidth() / 2;
            yIconOffset = HEIGHT / 2 - icon.getSprite().getIconImage().getHeight() / 2;
            yTextOffset = yIconOffset + icon.getSprite().getIconImage().getHeight() + font
                    .getSize();
        }
    }

    public MenuItem(String hoverName, int x, int y, int xIconOffset, int yIconOffset,
                    RenderMenuItem icon) {
        this.name = hoverName;
        this.setLocation(x, y);
        this.setBounds(WIDTH, HEIGHT);
        yTextOffset = HEIGHT - 5;
        this.xIconOffset = xIconOffset;
        this.yIconOffset = yIconOffset;
        font = new Font("TimesRoman", Font.PLAIN, 10);

    }


    @Override
    public void render(Graphics2D g2) {

        // g2.drawImage(shape.getSprite().getImage(), getX() + 10 , getY() + 10,
        // shape.getBounds().width , shape.getBounds().height , null);
        if (sprite != null)
            g2.drawImage(sprite.getSprite().getIconImage(), getX() + xIconOffset, getY()
                            + yIconOffset,
                    null);
        else
            g2.draw(getBounds());
        if (hover) {
            g2.setFont(font);
            if (xTextOffset == 0)
                xTextOffset = ((WIDTH - g2.getFontMetrics().stringWidth(name)) / 2);
            g2.drawString(name, getX() + xTextOffset, getY() + yTextOffset);
        }
    }

    @Override
    public void update() {
    }

    public void setHover(boolean hover) {
        this.hover = hover;
    }

    public boolean isHover() {
        return hover;
    }

    public RenderMenuItem getSprite() {
        return sprite;
    }
}
