package com.menu.popupmenu;

import com.core.Process;

public class PopMenuItem {

	final String name;
	final int ID;
	final PopMenuType type;
	boolean enabled;
	private Process process;

	public PopMenuItem(String name, int id) {
		this(name, id, PopMenuType.BUTTON);
	}

	public PopMenuItem(String name, int id, PopMenuType type) {
		this.name = name;
		this.ID = id;
		this.type = type;
		this.enabled = true;
	}

	public Process getProcess() {
		return process;
	}

	public void addProcess(Process pro) {
		this.process = pro;
	}

	public PopMenuType getMenuType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public void setEnabled(boolean canSelect) {
		this.enabled = canSelect;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public int getID() {
		return ID;
	}
}
