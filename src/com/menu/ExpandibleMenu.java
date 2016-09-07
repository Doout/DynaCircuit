package com.menu;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import com.core.Process;
import com.utils.Timer;

public class ExpandibleMenu extends Menu {

	private String title;
	private Rectangle menuTitleBar;
	private ArrayList<MenuItem> items;
	private MenuItem hoverMenu;
	private final int horizontalItemCount;
	private int maxHeight;
	private final int spaceBetweenItemsX;
	private final int spaceBetweenItemsY;
	private final static int ANIMATION_TIME = 500;
	private com.core.Process process;

	private Timer timer;
	private boolean collapsed = true;

	/**
	 * @param title
	 *            The name of the menu
	 * @param HIC
	 *            Number of items per row
	 * @param XSpace
	 *            The space between the items in the x-axe
	 * @param YSpace
	 *            The space between the items in the y-axe
	 * @param width
	 *            The width of this menu
	 **/
	public ExpandibleMenu(String title, int HIC, int XSpace, int YSpace, int width) {
		this.title = title;
		// maxHeight = height;
		menuTitleBar = new Rectangle(getX(), getY(), width, 20);
		this.setBounds(width, menuTitleBar.height);
		items = new ArrayList<MenuItem>();
		this.horizontalItemCount = HIC;
		spaceBetweenItemsX = XSpace;
		spaceBetweenItemsY = YSpace;
		timer = new Timer(ANIMATION_TIME);
		timer.setEndIn(0);
	}

	@Override
	public void setLocation(int x, int y) {
		int tempx = getX(), tempy = getY();
		super.setLocation(x, y);
		for (MenuItem m : items) {
			m.setLocation(m.getX() + (x - tempx), m.getY() + (y - tempy));
		}
		menuTitleBar.setLocation(getX(), getY());
	}

	public void add(String str, RenderMenuItem l) {
		int tempX = items.size() % horizontalItemCount;
		int offsetX = spaceBetweenItemsX + tempX * (spaceBetweenItemsX + MenuItem.WIDTH);
		int tempY = items.size() / horizontalItemCount;
		int offsetY = spaceBetweenItemsY + tempY * (spaceBetweenItemsY + MenuItem.HEIGHT)
				+ menuTitleBar.height;
		items.add(new MenuItem(str, offsetX, offsetY, l));
		// setMaxOffset(0, offsetY + MenuItem.HEIGHT + spaceBetweenItemsY);
		maxHeight = offsetY + MenuItem.HEIGHT + spaceBetweenItemsY;
		setBounds(getBounds().width, maxHeight);
	}

	@Override
	public void render(Graphics2D g2) {
		g2.setClip(getX(), getY(), getBounds().width, getBounds().height + 1);
		g2.draw(this.getBounds());
		for (MenuItem i : items) {
			if (i.getY() < getY() + getBounds().height)
				i.render(g2);
		}
		g2.draw(menuTitleBar);
		g2.setFont(new Font("TimesRoman", Font.PLAIN, 12));
		int x = getX() + ((menuTitleBar.width - g2.getFontMetrics().stringWidth(title))
				/ 2);
		int y = getY() + (menuTitleBar.height - g2.getFontMetrics().getAscent() / 2);
		g2.drawString(title, x, y);
	}

	@Override
	public void update() {
		if (timer.isRunning()) {
			int h = collapsed ? (int) (maxHeight * timer.getRemaining()
					/ ANIMATION_TIME) : (int) (maxHeight * (ANIMATION_TIME
							- timer.getRemaining()) / ANIMATION_TIME);
			if (h > menuTitleBar.height)
				this.setBounds(getBounds().width, h);
			else
				this.setBounds(getBounds().width, menuTitleBar.height);
		}
		if (timer.getTimePassed() > 0 && timer.getTimePassed() < 1000) {
			if (collapsed)
				this.setBounds(getBounds().width, menuTitleBar.height);
			else
				this.setBounds(getBounds().width, maxHeight);
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (!getBounds().contains(e.getPoint()))
			return;

		if (hoverMenu != null && !hoverMenu.getBounds().contains(e.getPoint())) {
			hoverMenu.setHover(false);
			hoverMenu = null;
		}
		if (hoverMenu == null || !hoverMenu.getBounds().contains(e.getPoint())) {
			if (hoverMenu != null) // double check this since it can still pass
									// with the location
				hoverMenu.setHover(false);
			for (MenuItem i : items) {
				if (i.getBounds().contains(e.getPoint())) {
					hoverMenu = i;
					hoverMenu.setHover(true);
					break;
				}
			}
		}
	}

	/**
	 * Set the event that should happen when the mouse pressed event is click on
	 * an item in the list <br>
	 * The data in which {@link Process} get is the ItemMenu that was pressed
	 * and the MouseEvent as well
	 * 
	 **/
	public void setProcess(com.core.Process process) {
		this.process = process;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (hoverMenu != null && hoverMenu.isHover()) {
			if (process != null) {
				hoverMenu.setHover(false);
				process.processData(hoverMenu, e);
				hoverMenu = null;
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (!timer.isRunning() && menuTitleBar.contains(e.getPoint())) {
			timer.reset();
			collapsed = !collapsed;
		}
	}

}
