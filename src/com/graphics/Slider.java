package com.graphics;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

public class Slider {

	private final double maxValue;
	private Rectangle rect;
	private Rectangle sliderbar;
	private double value;
	private double xOffsetPerPixel;

	public Slider(Rectangle rect, double maxValue) {
		this.maxValue = maxValue;
		this.rect = rect;
		this.xOffsetPerPixel = maxValue / rect.getWidth();
		this.sliderbar = new Rectangle(
				(int) ((rect.x + (rect.width * (value / maxValue)))) - 5, rect.y, 11,
				rect.height);
	}

	private void updateLocation() {
		sliderbar.setLocation((int) ((rect.x + (rect.width * (value / maxValue)))) - 5,
				rect.y);
	}

	public boolean contains(Point p) {
		return sliderbar.contains(p);
	}

	public double getValue() {
		return value;
	}

	public void addValue(double i) {
		value += i;
		if (value < 0)
			value = 0;
		if (value > maxValue)
			value = maxValue;
		updateLocation();
	}

	public void setValue(int value) {
		this.value = value;
		updateLocation();
	}

	
	public void addOne() {
		addValue(-xOffsetPerPixel);
	}

	public void drawBar(Graphics2D g) {
		g.draw(rect);
		g.draw(sliderbar);
		// g.drawLine(rect.x, rect.y + rect.height / 2, rect.x + rect.width,
		// rect.y + rect.height / 2);
		// g.drawLine((int) ((rect.x + (rect.width * (value / maxValue)))),
		// rect.y,
		// (int) ((rect.x + (rect.width * (value / maxValue)))),
		// rect.y + rect.height);
	}
}
