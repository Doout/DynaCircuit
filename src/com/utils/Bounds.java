package com.utils;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.Serializable;

import com.ChangeHandler;
import com.LogicGate;
import com.logic.Logic;
import com.logic.input.Input;

public abstract class Bounds implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4732267940493865273L;
	protected Rectangle area;
	private Point lockLoaction;
	private boolean lock;

	public Bounds() {
		area = new Rectangle();
	}

	public boolean canMoveTo() {
		for (Input in : LogicGate.getInstance().getInputs()) {
			if (!in.getBounds().equals(getBounds()))
				if (in.getBounds().intersects(getBounds()))
					return false;
		}
		for (Logic in : LogicGate.getInstance().getLogics()) {
			if (!in.getBounds().equals(getBounds()))
				if (in.getBounds().intersects(getBounds()))
					return false;
		}
		if (getX() < LogicGate.MENU_WIDTH || getX() > LogicGate.getInstance().WIDTH
				|| getY() < 0 || getY() > LogicGate.getInstance().HEIGHT)
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((area == null) ? 0 : area.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Bounds other = (Bounds) obj;
		if (area == null) {
			if (other.area != null)
				return false;
		} else if (!area.equals(other.area))
			return false;
		return true;
	}

	public void setOffSet(int x, int y) {
		if (lockLoaction == null)
			return;
		lock = false;
		setLocation(lockLoaction.x + x, lockLoaction.y + y);
		lock = true;
	}

	/**
	 * Set the offset to 0 and update the location as while unlocking the bound
	 */
	public void resetLock() {
		if (lockLoaction != null) {
			setOffSet(0, 0);
			lockLoaction = null;
			lock = false;
		}
	}

	/** Bound can not be set with setLocation */
	public void lockBound() {
		lockLoaction = new Point(getX(), getY());
		lock = true;

	}

	/** Unlock the bound and set the location to the old position + offset **/
	public void unlockBound() {
		setLocation(getX(), getY());
		lockLoaction = null;
		lock = false;
	}

	/**
	 * @param x
	 *            The x location
	 * @param y
	 *            The y Location <br>
	 *            <br>
	 * 
	 *            Set the position of this bound and can only be done if and
	 *            only if the bound is unlock
	 **/
	public void setLocation(int x, int y) {
		if (!lock)
			area.setLocation(x, y);
	}

	public Rectangle getBounds() {
		return area;
	}

	public int getX() {
		return area.x;
	}

	public int getY() {
		return area.y;
	}
	
	public void setBounds(int width, int height) {
		this.area.setBounds(getX(), getY(), width, height);
	}

}
