package com.logic.gates;

import java.awt.Shape;
import java.awt.geom.GeneralPath;

import com.LogicState;
import com.logic.Logic;
import com.logic.LogicSettings;
import com.utils.sprite.Sprite;

public class XORGate extends Gate {

	public XORGate(int numberOfInput) {
		super(numberOfInput);
		setSprite(Sprite.getSprite(LogicSettings.ID_XOR));
	}

	@Override
	public void update() {
		int number = 0;
		for (LogicState in : getInputs()) {
			if (in == null) {
				getOutput().setState(LogicState.UNKNOWN);
				return;
			}
			if (in.isOn())
				number++;
		}
		getOutput().setState(number % 2);

	}

	@Override
	public Shape getShape() {
		GeneralPath p = new GeneralPath();
		p.moveTo(getX(), getY());
		double offset = xOffset / 3;
		p.curveTo(getX(), getY(), getX() + (xOffset / 1.5), getY() + yOffset / 2, getX(),
				getY() + yOffset);

		p.moveTo(getX() + offset, getY());
		p.curveTo(getX() + offset, getY(), getX() + (xOffset / 1.5) + offset,
				getY() + yOffset / 2, getX() + offset, getY() + yOffset);

		p.lineTo(getX() + xOffset + offset, getY() + yOffset);
		p.curveTo(getX() + xOffset + offset, getY() + yOffset,
				getX() + xOffset + (xOffset / 1.5) + offset, getY() + yOffset / 2,
				getX() + xOffset + offset, getY());
		p.lineTo(getX() + offset, getY());
		return p;
	}

	@Override
	public Logic clone() {
		Logic clo = new XORGate(numberOfInput);
		return clone(clo);
	}

}
