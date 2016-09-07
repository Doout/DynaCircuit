package com.core;

import com.utils.Utils;

public class DataCores {

	private Process mainProcess;
	private ProcessData[][] data = null;
	private final int maxIndex;

	public DataCores(int maxOPCode) {
		this.maxIndex = maxOPCode;
		data = new ProcessData[maxOPCode][];
	}

	public Object[] processData(int opcode, Object... objects) {
		if (opcode >= maxIndex)
			throw new IndexOutOfBoundsException("OP Code " + opcode + " out of bounds");
		if (data[opcode] == null)
			return null;
		if (mainProcess != null)
			mainProcess.processData(opcode, objects);
		Object[] ret = new Object[data[opcode].length];
		for (int i = 0; i < data[opcode].length; i++) {
			ret[i] = data[opcode][i].getProcess().processData(objects);
		}
		return ret;
	}

	public void add(int opcode, ProcessData processData) {
		if (opcode >= maxIndex)
			throw new IndexOutOfBoundsException("OP Code " + opcode + " out of bounds");
		if (processData == null)
			throw new NullPointerException("processData null");
		if (data[opcode] == null) {
			data[opcode] = new ProcessData[1];
			data[opcode][0] = processData.setIndex(0);
			return;
		} else {
			data[opcode] = Utils.copyArray(data[opcode],
					new ProcessData[data[opcode].length + 1], processData);
			processData.setIndex((byte) ((data[opcode].length - 1) & 0xFF));
		}
	}

	public void remove(int opcode, int index) {
		if (opcode >= maxIndex)
			throw new IndexOutOfBoundsException(opcode + " out of bounds");
		data[opcode] = Utils.removeObjectFromArray(data[opcode],
				new ProcessData[data[opcode].length - 1], index);
		for (int i = index; i < data[opcode].length; i++)
			data[opcode][i] = data[opcode][i].setIndex(data[opcode][i]
					.getIndex() - 1);
	}

	public void remove(int opcode, ProcessData data) {
		if (data == null)
			throw new NullPointerException();
		remove(opcode, data.getIndex());
	}

	public void setMainProcess(Process mainProcess) {
		this.mainProcess = mainProcess;
	}

	/**
	 * Remove any null data type and
	 */
	public void clearOpcode() {
		if (data == null)
			return;
		for (int i = 0; i < data.length; i++) {
			if (data[i] != null)
				if (data[i].length == 0)
					data[i] = null;
		}
	}
}
