package com.logic.input;

import com.logic.Logic;
import com.logic.LogicSettings;
import com.utils.sprite.Sprite;

public class Switch extends Input {

	public Switch() {
		setSprite(Sprite.getSprite(LogicSettings.ID_SWITCH_OFF));

	}

	public void setState(boolean b) {
		getOutput().setState(b ? 1 : 0);
	}

	@Override
	public void toggle() {
		super.toggle();
		update();
	}

	@Override
	public Logic clone() {
		Logic clo = new Switch();
		return clone(clo);
	}

	@Override
	public void update() {
		if (getOutput().isOn())
			setSprite(Sprite.getSprite(LogicSettings.ID_SWITCH_ON));
		else
			setSprite(Sprite.getSprite(LogicSettings.ID_SWITCH_OFF));
	}

}
