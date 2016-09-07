package com.core.impl;

import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.event.MouseEvent;

import com.LogicGate;
import com.graphics.Renderable;
import com.logic.Logic;
import com.menu.popupmenu.PopMenuItem;
import com.menu.popupmenu.PopUpMenu;

/**
 * Everything to do with the popup menu is done in this class
 **/
public class PopUpMenuCore {

	private static PopUpMenu menu = new PopUpMenu();

	private static final int ID_COPY = 0;
	private static final int ID_PASTE = 1;

	private static PopMenuItem paste;

	public static void init() {
		menu.init(LogicGate.getInstance().getCores());
		LogicGate.getInstance().getRenderList().add(render);
		PopMenuItem copy = new PopMenuItem("Copy", ID_COPY);
		copy.addProcess((Object... o) -> {
			LogicCore.copy();
			return null;
		});

		paste = new PopMenuItem("Paste", ID_PASTE);
		Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
		paste.setEnabled(cb.isDataFlavorAvailable(LogicCore.getLogicDataFlavor()));
		paste.addProcess((Object... o) -> {
			LogicCore.paste();
			return null;
		});
		menu.addItem(copy);
		menu.addItem(paste);
	}

	public static boolean isMenuUp() {
		return menu.show();
	}

	public static boolean isMouseOverMenu(MouseEvent e) {
		return menu.isMouseOver(e);
	}

	public static PopMenuItem getPaste() {
		return paste;
	}

	private static Renderable render = (Graphics2D g) -> {
		Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
		paste.setEnabled(cb.isDataFlavorAvailable(LogicCore.getLogicDataFlavor()));
		menu.render(g);
	};

}
