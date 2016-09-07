package com.utils.sprite;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;

import javax.imageio.ImageIO;

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

	private Sprite() {// No one allow to be call outside of this class
	}

	public BufferedImage getImage() {
		return sprite;
	}

	/** ICON IMAGE same as the reg one for now **/
	public BufferedImage getIconImage() {
		if (spriteIcon == null)
			return sprite;
		return spriteIcon;
	}

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

	public static void main(String[] args) throws InterruptedException {
		Long u = 20l;
		Thread.sleep(20000);
	}
}
