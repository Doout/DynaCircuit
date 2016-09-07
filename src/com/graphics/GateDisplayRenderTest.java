package com.graphics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.Display;
import com.logic.Logic;
import com.logic.gates.*;
import com.logic.output.LED;

/*
 * The point of this class is to fine turn the Finder (Path finder)
 * */public class GateDisplayRenderTest extends Display {

	private static final long serialVersionUID = -1203675228812484900L;
	private static Gate gate;

	public GateDisplayRenderTest(int width, int height) {
		super(width, height);
	}

	public static void main(String[] args) throws IOException {

		GateDisplayRenderTest log = new GateDisplayRenderTest(300, 300);
		Display.CreateFrame(log, "Path Finder",null);
		log.start();

	//	gate = new ORGate(2);
	//	gate.setLocation(100, 100);

		/*
		 * Logic[] gates = { new LED() }; for (Logic save : gates) {
		 * BufferedImage img = new BufferedImage(save.getBounds().width + 5,
		 * save .getBounds().height + 5, BufferedImage.TYPE_INT_ARGB);
		 * Graphics2D g = img.createGraphics();
		 * g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		 * RenderingHints.VALUE_ANTIALIAS_ON); g.setColor(Color.black);
		 * g.setStroke(new BasicStroke(2f)); g.fillOval(0, 0, 16, 16); //
		 * g.draw(save.getShape()); g.dispose(); ImageIO.write(img, "PNG", new
		 * File( "C:/Users/Baheer/Dropbox/java/Logic Gate/res/" +
		 * save.getClass() .getSimpleName().replace("Gate", "").toLowerCase() +
		 * ".png")); }
		 */
	}

	@Override
	public void render(Graphics2D g) {
		if (gate == null)
			return;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(Color.white);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.black);
		g.draw(gate.getShape());
	}

	@Override
	public void init() {
	}

	@Override
	public void update() {

	}

}
