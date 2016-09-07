package com.logic.gates;

import java.awt.Shape;
import java.awt.geom.GeneralPath;

import com.LogicState;
import com.RenderUtils;
import com.logic.Logic;
import com.logic.LogicSettings;
import com.utils.sprite.Sprite;

public class NOTGate extends Gate {

	public NOTGate() {
		super(1);
		setSprite(Sprite.getSprite(LogicSettings.ID_NOT));
		logicStateLocation[1].setLocation(logicStateLocation[1].x + 10,
				logicStateLocation[1].y);
	}

	@Override
	public void update() {
		if (inputs[0] != null)
			getOutput().setState(inputs[0].getToggle());
		else
			getOutput().setState(LogicState.UNKNOWN);
	}

	@Override
	public Logic clone() {
		Logic clo = new NOTGate();
		return clone(clo);
	}

}
