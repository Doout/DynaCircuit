package com;

import java.awt.Point;
import java.io.Serializable;

import com.logic.Logic;
import com.utils.Utils;

public class LogicState implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4919670383798350778L;

	public static final byte MASK = 1;
	public static final byte OFF = 0;
	public static final byte ON = 1;
	public static final byte UNKNOWN = 2;

	private byte states;
	private boolean locationUpdate;
	private Logic[] logicInput;
	private int x, y; // location of the logic input/output

	public LogicState() {
		this(false);
	}

	public LogicState(boolean state) {
		this.states |= state ? 1 : 0;
		this.logicInput = new Logic[0];
	}

	public void addConnection(Logic logic) {
		Utils.copyArray(logicInput, logicInput = new Logic[logicInput.length + 1], logic);
	}

	public void removeConnection(int index) {
		Utils.removeObjectFromArray(logicInput, logicInput = new Logic[logicInput.length
				- 1], index);
	}

	public void removeConnection(Logic remove, int index) {
		for (int i = 0; i < logicInput.length; i++) {
			if (logicInput[i].equals(remove)) {
				if (logicInput[i].getInputs().length > index && logicInput[i]
						.getInputs()[index].equals(remove.getInputs()[index])) {
					removeConnection(i);
					return;
				}
			}
		}
	}

	public boolean isOn() {
		return getState() == ON;
	}

	public boolean isOff() {
		return getState() == OFF;
	}

	public boolean isUnknown() {
		return getState() == UNKNOWN;
	}

	public int getToggle() {
		return (getState() ^ LogicState.MASK);
	}

	public byte getState() {
		return (byte) (states & 0b11);
	}

	public boolean stateUpdate() {
		return (states >> 2) != getState();
	}

	public void setState(int state) {
		this.states = (byte) ((getState() << 2) | state);
	}

	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
		locationUpdate = true;
	}

	public Point getPoint() {
		return new Point(getX(), getY());
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public boolean isLocationUpdate() {
		return locationUpdate;
	}

	public void setLocationUpdate(boolean locationUpdate) {
		this.locationUpdate = locationUpdate;
	}

	public Logic[] getLogicInput() {
		return logicInput;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LogicState other = (LogicState) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

}
