package com;

import java.awt.geom.GeneralPath;

public class RenderUtils {

	public static GeneralPath addCir(GeneralPath path, double x, double y, double R) {
		double value = 0.55;
		path.moveTo(x, y - R);
		// TOP RIGHT
		path.curveTo(x + R * value, y - R, x + R, y - R * value, x + R, y);
		// BOTTOM RIGHT
		path.curveTo(x + R, y + R * value, x + R * value, y + R, x, y + R);
		// BOTTOM LEFT
		path.curveTo(x - R * value, y + R, x - R, y + R * value, x - R, y);
		// TOP LEFT
		path.curveTo(x - R, y - R * value, x - R * value, y - R, x, y - R);
		return path;
	}

	

}
