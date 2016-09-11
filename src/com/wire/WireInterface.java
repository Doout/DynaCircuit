package com.wire;

import com.LogicState;

import java.awt.*;

public interface WireInterface {

    public int getSelectIndexWire();

    public void setSelectIndexWire(int index);

    public Rectangle[] getWireInputNode();

    public LogicState[] getInputs();

    public LogicState[] getOutputs();

    public Rectangle[] getWireOutputNode();
}
