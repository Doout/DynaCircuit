package com.logic.controller;

import java.awt.Point;

import com.LogicState;
import com.logic.Logic;
import com.logic.LogicSettings;
import com.utils.sprite.Sprite;
import com.wire.Wire;

/*****/
public class MUX extends Controller {

	int numberOfSelecter;

	public MUX(int numberOfSelecter) {
		super((int) (numberOfSelecter + Math.pow(2, numberOfSelecter)), 1);
		setSprite(Sprite.getSprite(LogicSettings.ID_MUX));
		this.numberOfSelecter = numberOfSelecter;
		resize();
	}

	protected void resize() {
		// The first part of the input are for selecter
		int h = 34 + 15 * (numberOfInput - numberOfSelecter - 1);
		int w = 20 + 10 * (numberOfSelecter - 1);
		setBounds(w, h);
		int yStartInput = 17;
		int xStartSelecter = 10;
		Point[] location = new Point[numberOfInput + 1];
		// h2 is the height of the triangle at the bottom of the mux
		double h2 = (double) (0.36734693878 * (double) h) / 2d;
		double angle = Math.tanh(h2 / (double) w);
		int x;
		for (int i = 0; i < numberOfSelecter; i++) {
			x = xStartSelecter + xStartSelecter * i;
			location[i] = new Point(x - Wire.CONNECTION_R / 2, h - getYOffsetOfSelecter(
					angle, x) + 3);
		}

		for (int i = numberOfSelecter; i < numberOfInput; i++) {
			x = i - numberOfSelecter;
			location[i] = new Point(-10, yStartInput + (15 * x));
		}
		location[location.length - 1] = new Point(w + 5, h / 2);
		// getOutputs()[0].setLocation(w + 5, h / 2);
		setLogicStateLocation(location);
	}

	private int getYOffsetOfSelecter(double angle, int x) {
		return (int) (Math.tan(angle) * (double) x);
	}

	@Override
	public void update() {
		// The first bit of the input are selecter
		int inputIndex = encode(getInputs(), numberOfSelecter) + numberOfSelecter;
		if (inputIndex != -1)
			if (getInputs()[inputIndex] != null) {
				getOutputs()[0].setState(getInputs()[inputIndex].getState());
			} else
				getOutputs()[0].setState(LogicState.OFF);
		else
			getOutputs()[0].setState(LogicState.UNKNOWN);
	}

	@Override
	public Logic clone() {
		MUX k = new MUX(numberOfSelecter);
		return clone(k);
	}

}
