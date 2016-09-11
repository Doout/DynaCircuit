package com.logic.gates;

import com.LogicState;
import com.logic.Logic;
import com.wire.Wire;

import java.awt.*;

/**
 * @author Baheer Kamal
 */

public abstract class Gate extends Logic {

    /**
     *
     */
    private static final long serialVersionUID = -1137069089101721106L;
    protected static int X_OFFSET = 10;
    protected static int Y_OFFSET = 15;
    /**
     * use for getShape in the subclasses
     */
    protected int yOffset;
    protected int xOffset;

    public Gate(int numberOfInput) {
        super(numberOfInput, 1);
        this.outputs[0] = new LogicState(false);
        yOffset = (Y_OFFSET * getNumberOfInput());
        xOffset = (X_OFFSET * getNumberOfInput());
        setBounds((int) (xOffset*1.3), yOffset);
        Point[] locations = new Point[numberOfInput + 1];
        for (int i = 0; i < numberOfInput; i++) {
            locations[i] = new Point(-Wire.CONNECTION_R / 2 - 5,
                    (i * 15 + 8) - (Wire.CONNECTION_R / 2));
        }
        locations[numberOfInput] = new Point(getBounds().width,
                getBounds().height / 2
                        - 1);
        getWireOutputNode()[0].setLocation(getOutput().getX(), getOutput().getY()
                - Wire.CONNECTION_R / 2);
        setLogicStateLocation(locations);
    }

    @Override
    public void setLocation(int x, int y) {
        super.setLocation(x, y);
        getOutput().setLocation(x + area.width + Wire.CONNECTION_R / 2, y + area.height
                / 2);
        getWireOutputNode()[0].setLocation(getOutput().getX(), getOutput().getY()
                - Wire.CONNECTION_R / 2);

    }

    public LogicState getOutput() {
        return this.outputs[0];
    }

}
