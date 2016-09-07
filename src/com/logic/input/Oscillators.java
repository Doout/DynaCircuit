package com.logic.input;

import com.LogicGate;
import com.logic.Logic;
import com.logic.LogicSettings;
import com.utils.Timer;
import com.utils.sprite.Sprite;

public class Oscillators extends Input {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6752140308483914863L;
	private Timer time;

	public Oscillators() {
		this(1);

	}

	public Oscillators(double updatePerSecond) {
		area.setBounds(0, 0, 20, 20);
		time = new Timer((long) (1L / updatePerSecond) * 1000000000L, true);
		setUpdate(false);// No need as we will add ourself in it.
		if (updatePerSecond > 0)
			LogicGate.getInstance().getUpdatable().add(this);
		setSprite(Sprite.getSprite(LogicSettings.ID_OSCILLATORS));
	}

	@Override
	public void update() {
		if (!time.isRunning()) {
			super.toggle();
			LogicGate.getInstance().getUpdateQueue().push(this);
			time.reset();
		}
	}

	@Override
	public void removeSelf() {
		LogicGate.getInstance().getUpdatable().remove(this);
		super.removeSelf();// remove from the update list
	}

	@Override
	public void toggle() {
	}

	@Override
	public Logic clone() {
		return clone(new Oscillators());
	}

}
