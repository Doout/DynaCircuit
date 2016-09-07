package com.logic.gates;

import java.awt.Shape;
import java.awt.geom.GeneralPath;

import com.LogicState;
import com.logic.Logic;
import com.logic.LogicSettings;
import com.utils.sprite.Sprite;

public class ORGate extends Gate {

	public ORGate(int numberOfInput) {
		super(numberOfInput);
		setSprite(Sprite.getSprite(LogicSettings.ID_OR));
	}

	@Override
	public void update() {
		byte on = LogicState.OFF;
		for (LogicState in : getInputs()) {
			if (in == null) {
				on = LogicState.UNKNOWN;
				break;
			}
			if (in.isOn()) {
				on = LogicState.ON;
			}
		}
		getOutput().setState(on);
	}

	@Override
	public Shape getShape() {
		GeneralPath p = new GeneralPath();
		p.moveTo(getX(), getY());
		p.curveTo(getX(), getY(), getX() + (xOffset / 1.5), getY() + yOffset / 2, getX(),
				getY() + yOffset);
		p.lineTo(getX() + xOffset, getY() + yOffset);
		p.curveTo(getX() + xOffset, getY() + yOffset, getX() + xOffset + (xOffset / 1.5),
				getY() + yOffset / 2, getX() + xOffset, getY());
		p.lineTo(getX(), getY());

		return p;
	}

	@Override
	public Logic clone() {
		Logic clo = new ORGate(numberOfInput);
		return clone(clo);
	}

}
