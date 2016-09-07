package com.logic.controller;

import com.logic.Logic;
import com.logic.LogicSettings;
import com.utils.sprite.Sprite;

public class TFlipFlop extends FlipFlop {

	public TFlipFlop() {
		super(2, 2);
		setPointLocation(true, false);
		setSprite(Sprite.getSprite(LogicSettings.ID_TFLIPFLOP));
	}

	@Override
	public void update() {
		if (!unknownOutput() && isEnable()) {
			getOutputs()[0].setState(getInputs()[1].getToggle()); // NOT Q
			getOutputs()[1].setState(getInputs()[1].getState()); // Q
		}
	}

	@Override
	public Logic clone() {
		return clone(new TFlipFlop());
	}

}
