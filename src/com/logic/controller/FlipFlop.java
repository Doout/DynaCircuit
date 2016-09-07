package com.logic.controller;

import java.awt.Point;

import com.LogicState;
import com.wire.Wire;

public abstract class FlipFlop extends Controller {

	public FlipFlop() {
		super(3, 2);
		setPointLocation(false, false);
	}

	protected FlipFlop(int numberOfInput, int numberOfOutput) {
		super(numberOfInput, numberOfOutput);
	}

	/** Deal with unknown output **/
	public boolean unknownOutput() {
		if (getInputState(1) == LogicState.UNKNOWN || getInputState(
				2) == LogicState.UNKNOWN) {
			getOutputs()[0].setState(LogicState.UNKNOWN);
			getOutputs()[1].setState(LogicState.UNKNOWN);
			return true;
		}
		if (getOutputs()[0].isUnknown())
			getOutputs()[0].setState(LogicState.OFF);
		return false;
	}

	protected boolean isEnable() {
		if (getInputs()[0] == null)
			return false;
		return getInputs()[0].isOn();
	}

	/***
	 * @param type
	 *            False 0 is for 2 input flipflop like JK <br>
	 *            True 1 is for single input like T flipflop <br>
	 * 
	 * @param reset
	 *            If the flipflop should have an reset location
	 * 
	 *            Input should be order in this way. <br>
	 *            clock - reset (if have) - rest
	 */
	public void setPointLocation(boolean singleInptut, boolean reset) {
		Point[] points = new Point[numberOfInput + numberOfOutput];
		int index = 0;
		final int offsetFromArea = 5;
		points[index++] = new Point(25 - Wire.CONNECTION_R / 2, 50 + offsetFromArea
				- Wire.CONNECTION_R / 2);
		if (reset)
			points[index++] = new Point(25 - Wire.CONNECTION_R / 2, -offsetFromArea
					- Wire.CONNECTION_R / 2);
		points[index++] = new Point(-offsetFromArea - Wire.CONNECTION_R / 2, 13
				- Wire.CONNECTION_R / 2);
		if (!singleInptut)
			points[index++] = new Point(-offsetFromArea - Wire.CONNECTION_R / 2, 37
					- Wire.CONNECTION_R / 2);
		// now the output
		points[index++] = new Point(50 + offsetFromArea - Wire.CONNECTION_R / 2, 13
				- Wire.CONNECTION_R / 2);
		points[index++] = new Point(50 + offsetFromArea - Wire.CONNECTION_R / 2, 37
				- Wire.CONNECTION_R / 2);
		setLogicStateLocation(points);
	}

}
