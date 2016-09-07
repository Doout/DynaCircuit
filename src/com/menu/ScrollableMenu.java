package com.menu;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;

import com.LogicGate;
import com.utils.Timer;

public abstract class ScrollableMenu<T extends Menu> extends Menu {

	private ArrayList<T> menus;

	protected int yOffSet = 0;
	protected int xOffSet = 0;
	protected int yOffSetLast = 0;
	protected int xOffSetLast = 0;

	protected int yOffSetMax = 0;
	protected int xOffSetMax = 0;
	protected int yOffSetMaxLast = 0;
	protected int xOffSetMaxLast = 0;

	protected int maxFrameHeight;
	protected int menuSize;
	protected int menuHeightOnSreen;

	private boolean updateLocation;
	private boolean isMoveable;
	protected boolean canMove;
	private double speed = 5;
	private Timer checkForOffset;

	public ScrollableMenu(int height) {
		maxFrameHeight = height;
		menus = new ArrayList<>();
		checkForOffset = new Timer(1000);
	}

	public void mouseWheelMoved(MouseWheelEvent e) {
		if (!isMoveable || !canMove)
			return;
		yOffSetLast = yOffSet;
		boolean directionUp = e.getPreciseWheelRotation() < 0;
		if (directionUp)
			yOffSet -= e.getScrollAmount() * speed;
		else
			yOffSet += e.getScrollAmount() * speed;
		if (yOffSet > 0) {
			yOffSet = 0;
			return;
		} else if (yOffSet < yOffSetMax) {
			yOffSet = yOffSetMax;
			return;
		}
		updateLocation = true;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// moveOffset(0, 1);
		for (T m : menus) {
			m.mouseClicked(e);
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		for (T m : menus) {
			m.mouseDragged(e);
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		for (T m : menus) {
			m.mousePressed(e);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		for (T m : menus) {
			m.mouseReleased(e);
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		for (T m : menus) {
			m.mouseMoved(e);
		}
	}

	public void setMaxOffset(int x, int y) {
		this.yOffSetMaxLast = yOffSetMax;
		this.xOffSetMaxLast = xOffSetMax;
		this.yOffSetMax = y;
		this.xOffSetMax = x;
	}

	public void add(T eMenu) {
		if (menus.size() > 0)
			eMenu.setLocation(eMenu.getX(), menus.get(menus.size() - 1).getY()
					+ menus.get(menus.size() - 1).getBounds().height);
		menus.add(eMenu);
		setMaxOffset(0, -((eMenu.getY() + eMenu.getBounds().height) - maxFrameHeight));
	}

	@Override
	public void render(Graphics2D g2) {
		g2.setClip(0, 0, LogicGate.MENU_WIDTH, maxFrameHeight);
		for (T m : menus) {
			m.render(g2);
		}
		g2.setClip(null);
	}

	@Override
	public void update() {
		for (int i = 0; i < menus.size(); i++) {
			if (i == 0) {
				menus.get(i).update();
			} else {
				menus.get(i).setLocation(
						menus.get(i).getX(),
						menus.get(i - 1).getY() + 2
								+ menus.get(i - 1).getBounds().height);
				menus.get(i).update();
			}
		}
		if (menus == null || menus.isEmpty())
			return;
		menuHeightOnSreen = (menus.get(menus.size() - 1).getY() + menus.get(
				menus.size() - 1).getBounds().height);
		// check if the overAll size have change. If there is a change then
		// update

		menuSize = menuHeightOnSreen - menus.get(0).getY();
		canMove = menuSize > maxFrameHeight;
		setMaxOffset(xOffSet, -(menuSize - maxFrameHeight));
		if (yOffSet != 0 && menuSize < maxFrameHeight) {
			yOffSet = 0;
			updateLocation = true;
		}
		/*
		 * if (yOffSetMax - yOffSetMaxLast != 0 && canMove && yOffSet < 0) {
		 * yOffSet += (yOffSetMax - yOffSetMaxLast); if (yOffSet > 0) { yOffSet
		 * = yOffSetLast; } updateLocation = true; }
		 */
		// Fix any bug with the menu if the yoffset is not in the right
		// place
		if (!checkForOffset.isRunning()) {
			int numberOfPixleOff = (getX() + yOffSet) - (menus.get(0).getY());
			if (numberOfPixleOff != 0) {
				System.out.println(numberOfPixleOff);
				updateLocation(0, numberOfPixleOff);
			}
			checkForOffset.reset();
		}
		if (updateLocation)
			updateLocation();
	}

	public boolean isMoveable() {
		return isMoveable;
	}

	public void resetLocation() {
		yOffSet = 0;
		updateLocation();
		yOffSetLast = 0;
	}

	public void setMoveable(boolean isMoveable) {
		this.isMoveable = isMoveable;
	}

	public void updateLocation() {
		updateLocation(xOffSet - xOffSetLast, yOffSet - yOffSetLast);
		yOffSetLast = yOffSet;
	}

	public void updateLocation(int x, int y) {
		for (T m : menus) {
			m.setLocation(m.getX() + x, m.getY() + y);
		}
	}
}
