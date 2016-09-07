package com.logic.controller;

import com.LogicGate;
import com.LogicState;
import com.logic.Logic;
import com.logic.LogicSettings;
import com.utils.sprite.Sprite;

public class DFlipFlop extends FlipFlop {

	public DFlipFlop() {
		super(2, 2);
		setPointLocation(true, false);
		setSprite(Sprite.getSprite(LogicSettings.ID_DFLIPFLOP));
	}

	@Override
	public void update() {
		if (!unknownOutput())
			if (isEnable()) {
				getOutputs()[0].setState(getInputs()[1].getState());
				getOutputs()[1].setState(getInputs()[1].getToggle());
			}

	}

	@Override
	public Logic clone() {
		return clone(new DFlipFlop());
	}

}
