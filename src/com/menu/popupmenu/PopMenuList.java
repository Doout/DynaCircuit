package com.menu.popupmenu;

import java.awt.Rectangle;
import java.util.ArrayList;

import com.utils.Timer;

public class PopMenuList extends PopMenuItem {

	protected final int ITEM_HEIGHT = 15;
	protected ArrayList<PopMenuItem> items;
	protected Rectangle bounds;
	protected boolean show;
	protected Timer timer;

	public PopMenuList() {
		this(null, -1);
	}

	public PopMenuList(String name, int id) {
		super(name, id, PopMenuType.LIST);
		bounds = new Rectangle(10, 10, 0, 0);
		items = new ArrayList<>();
		timer = new Timer(500);
		timer.setEndIn(0);
	}

	public void addItem(PopMenuItem menu) {
		items.add(menu);
		if ((bounds.width - 10) < menu.getName().length() * 12)
			bounds.setSize(menu.getName().length() * 12 + 10, 0);
		bounds.setSize(bounds.width, items.size() * ITEM_HEIGHT + 5);
	}

	public void addItem(String name, int id, PopMenuType type) {
		items.add(new PopMenuItem(name, id, type));
		if ((bounds.width - 10) < name.length() * 12)
			bounds.setSize(name.length() * 12 + 10, 0);
		bounds.setSize(bounds.width, items.size() * ITEM_HEIGHT + 5);
	}

	public void addItem(String name, int id) {
		addItem(name, id, PopMenuType.BUTTON);
	}
}
