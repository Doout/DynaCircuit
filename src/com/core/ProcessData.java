package com.core;

public class ProcessData {

	private byte index;
	private Process process;

	public ProcessData(Process p) {
		this.process = p;
	}

	public final ProcessData setIndex(int index) {
		this.index = (byte) (index & 0xFF);
		return this;
	};

	public final int getIndex() {
		return index;
	};

	public Process getProcess() {
		return process;
	}
}
