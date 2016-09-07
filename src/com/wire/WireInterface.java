package com.wire;

import java.awt.Rectangle;

import com.LogicState;

public interface WireInterface {

	public int getSelectIndexWire();

	public void setSelectIndexWire(int index);

	public Rectangle[] getWireInputNode();

	public LogicState[] getInputs();

	public LogicState[] getOutputs();

	public Rectangle[] getWireOutputNode();
}
