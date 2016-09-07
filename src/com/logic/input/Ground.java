package com.logic.input;

import com.logic.Logic;

public class Ground extends Input {

	public Ground() {
		setSprite(null);
	}

	@Override
	public void update() {
	}

	@Override
	public Logic clone() {
		return clone(new Ground());
	}

}
