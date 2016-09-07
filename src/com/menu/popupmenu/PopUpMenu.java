package com.menu.popupmenu;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import com.LogicGate;
import com.core.CoreSettings;
import com.core.DataCores;
import com.core.ProcessData;
import com.core.impl.LogicCore;

//To make this class stand alone the main class have to check this class for any update
public class PopUpMenu extends PopMenuList {

	int mouseX, mouseY;
	Queue<Integer> updatelist;
	ArrayList<PopMenuList> submenu;
	private Process clickProcess;

	public PopUpMenu() {
		updatelist = new ArrayBlockingQueue<>(20);
		submenu = new ArrayList<>(5);// 5 seem like a good number for submenu
	}

	public void render(Graphics2D g) {
		if (!show())
			return;
		g.setColor(new Color(200, 200, 200, 100));
		g.fill(bounds);
		g.setColor(Color.black);
		renderMenu(g, this);
		for (PopMenuList l : submenu) {
			renderMenu(g, l);
		}
	}

	public void renderMenu(Graphics2D g, PopMenuList menu) {
		int itemY;
		for (int i = 0; i < items.size(); i++) {
			itemY = menu.bounds.y + ITEM_HEIGHT * (i + 1);
			if (mouseY > itemY - ITEM_HEIGHT && mouseY < itemY) {
				g.drawRect(menu.bounds.x, itemY - ITEM_HEIGHT + 2, menu.bounds.width, 15);
			}
			if (items.get(i).getMenuType().getType() == PopMenuType.LIST.getType())
				drawArrow(g, menu.bounds.x + menu.bounds.width, itemY);
			if (menu.items.get(i).isEnabled())
				g.setColor(Color.black);
			else
				g.setColor(Color.gray);
			g.drawString(menu.items.get(i).getName(), menu.bounds.x + 5, itemY);
		}
	}

	public void drawArrow(Graphics2D g, int x, int y) {
		// g.drawLine(x - 10, y - ITEM_HEIGHT / 2 + 2, x - 1, y - ITEM_HEIGHT /
		// 2 + 2);
		g.drawLine(x - 5, y - ITEM_HEIGHT / 2 + 5, x - 2, y - ITEM_HEIGHT / 2 + 2);
		g.drawLine(x - 5, y - ITEM_HEIGHT / 2 - 1, x - 2, y - ITEM_HEIGHT / 2 + 2);
	}

	public void mouseClicked(MouseEvent e) {
		if (e.getX() <= LogicGate.MENU_WIDTH)
			return;
		if (e.getButton() == 3) {
			if (show && bounds.contains(e.getPoint()))
				return;
			show = true;
			bounds.setLocation(e.getX(), e.getY());
			timer.reset();
			return;
		}
		if (!show)
			return;
		if (bounds.contains(e.getPoint())) { // fail safe
			int index = (e.getY() - bounds.y) / ITEM_HEIGHT;
			if (items.get(index).getMenuType().getType() == PopMenuType.BUTTON
					.getType()) {
				if (items.get(index).isEnabled() && items.get(index)
						.getProcess() != null) {
					items.get(index).getProcess().processData(e);
					show = false;
					timer.setEndIn(0);
					return;
				}
			}
		}
	}

	public void mouseMove(MouseEvent e) {
		if (e.getX() <= LogicGate.MENU_WIDTH)
			return;
		this.mouseX = e.getX();
		this.mouseY = e.getY();
		if (!bounds.contains(e.getPoint())) {
			show = false;
		} else {
			if (timer.isRunning())
				timer.reset();
		}
	}

	public boolean isMouseOver(MouseEvent e) {
		if (bounds == null)
			return false;
		return bounds.contains(e.getPoint());
	}

	public boolean show() {
		return (show || timer.isRunning());
	}

	public void init(DataCores core) {
		core.add(CoreSettings.MOUSE_CLICKED,
				new ProcessData((Object... o) -> {
					mouseClicked((MouseEvent) o[0]);
					return null;
				}));
		core.add(CoreSettings.MOUSE_MOVED,
				new ProcessData((Object... o) -> {
					mouseMove((MouseEvent) o[0]);
					return null;
				}));
	}

}
