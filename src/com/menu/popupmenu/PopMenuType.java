package com.menu.popupmenu;


public enum PopMenuType {
	BUTTON(0), LIST(1);

	PopMenuType(int type) {
		this.type = type;
	}

	int type;

	public int getType() {
		return type;
	}
	
}