package com.logic.gates;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.GeneralPath;

import com.LogicGate;
import com.LogicState;
import com.RenderUtils;
import com.logic.Logic;
import com.logic.LogicSettings;
import com.utils.sprite.Sprite;

public class NORGate extends ORGate {

	public NORGate(int numberOfInput) {
		super(numberOfInput);
		setSprite(Sprite.getSprite(LogicSettings.ID_NOR));
	}

	@Override
	public void update() {
		super.update();
		if (!getOutput().isUnknown())
			getOutput().setState(getOutput().getState() ^ LogicState.MASK);
	}

	@Override
	public Logic clone() {
		Logic clo = new NORGate(numberOfInput);
		return clone(clo);
	}
}
