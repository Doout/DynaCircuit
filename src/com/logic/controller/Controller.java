package com.logic.controller;

import com.LogicState;
import com.logic.Logic;
import com.wire.Wire;

/**
 * @author Baheer Kamal
 **/
public abstract class Controller extends Logic {

    public Controller(int numberOfInput, int numberOfOutput) {
        super(numberOfInput, numberOfOutput);

    }

    /**
     * for each logicstae the encode number is each 0 or 1 in the at the locution of the index.
     * Using bitwise keep adding each 0 or 1 to the number. <br>
     * <ul></.>For each logicState
     * number |= (Logicstate << LogicState Index)
     * </ul>
     *
     * @param logicState The logicstae that need to get encode
     * @param length     How many should get encode
     */
    public static int encode(LogicState[] logicState, int length) {
        int index = 0;
        for (int i = 0; i < length; i++) {
            if (logicState[i].getState() == LogicState.UNKNOWN)
                return -1;
            if (logicState[i] == null)
                index |= (0 << i);
            else
                index |= ((logicState[i].getState()) << i);
        }
        return index;
    }

    /**
     * Set the location of the logic
     */
    @Override
    public void setLocation(int x, int y) {
        super.setLocation(x, y);
        for (int i = 0; i < getWireOutputNode().length; i++) {
            if (getOutputs()[i] != null)
                getOutputs()[i].setLocation(getWireOutputNode()[i].x,
                        getWireOutputNode()[i].y + Wire.CONNECTION_R / 2);
        }
    }

    /**
     * @return The state of the input at index X in the array
     **/
    protected int getInputState(int index) {
        if (getInputs()[index] == null)
            return LogicState.UNKNOWN;
        return getInputs()[index].getState();
    }

}
