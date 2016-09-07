package com.logic.gates;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.GeneralPath;

import com.LogicState;
import com.RenderUtils;
import com.logic.Logic;
import com.logic.LogicSettings;
import com.utils.sprite.Sprite;

public class XNORGate extends XORGate {

	public XNORGate(int numberOfInput) {
		super(numberOfInput);
		setSprite(Sprite.getSprite(LogicSettings.ID_XNOR));
	}

	@Override
	public void update() {
		super.update();
		if (!getOutput().isUnknown())
			getOutput().setState(getOutput().getToggle());
	}

	@Override
	public Shape getShape() {
		GeneralPath p = (GeneralPath) super.getShape();
		Rectangle rec = p.getBounds();
		double tempXOffset = (xOffset / 2.5);
		double x = rec.getX() + rec.getWidth() - tempXOffset + xOffset / 7;
		double y = rec.getY() + rec.getHeight() / 2;
		double R = xOffset / 8.5;
		RenderUtils.addCir(p, x, y, R);
		return p;
	}

	@Override
	public Logic clone() {
		Logic clo = new XNORGate(numberOfInput);
		return clone(clo);
	}
}
