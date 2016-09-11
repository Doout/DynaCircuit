package com;

import com.logic.Logic;
import com.utils.Utils;

import java.awt.*;
import java.io.Serializable;

public class LogicState implements Serializable {

    public static final byte MASK = 1;
    public static final byte OFF = 0;
    public static final byte ON = 1;
    public static final byte UNKNOWN = 2;
    /**
     *
     */
    private static final long serialVersionUID = -4919670383798350778L;
    private byte states;
    private boolean locationUpdate;
    private Logic[] logicInput;
    private int x, y; // location of the logic input/output

    /**
     * the state is set to 0
     */
    public LogicState() {
        this(false);
    }

    /**
     * @param state The state to set this input/output to
     */
    public LogicState(boolean state) {
        this.states |= state ? 1 : 0;
        this.logicInput = new Logic[0];
    }

    /**
     * Add connection to this state
     **/
    public void addConnection(Logic logic) {
        Utils.copyArray(logicInput, logicInput = new Logic[logicInput.length + 1], logic);
    }

    /**
     * remove the connection at the index
     *
     * @param index The index of the connection to remove
     */
    public void removeConnection(int index) {
        Utils.removeObjectFromArray(logicInput, logicInput = new Logic[logicInput.length
                - 1], index);
    }

    /**
     * Remove a connection from remove where index is the location to remove.
     *
     * @param remove The logic connection to remove from
     * @param index  The location of this state connection to remove
     */
    public void removeConnection(Logic remove, int index) {
        for (int i = 0; i < logicInput.length; i++) {
            if (logicInput[i].equals(remove)) {
                if (logicInput[i].getInputs().length > index && logicInput[i]
                        .getInputs()[index].equals(remove.getInputs()[index])) {
                    removeConnection(i);
                    return;
                }
            }
        }
    }

    /**
     * @return True if the connection is live<br>False if the connection is low
     */
    public boolean isOn() {
        return getState() == ON;
    }

    /**
     * @return False if the connection is low<br> True if the connection is live
     */
    public boolean isOff() {
        return getState() == OFF;
    }

    /**
     * @return True if the connection is either low or high, else False
     */
    public boolean isUnknown() {
        return getState() == UNKNOWN;
    }

    /**
     * @return The flip state
     **/
    public int getToggle() {
        return (getState() ^ LogicState
                .MASK);
    }

    /**
     * @return The state of this connection. Number will be
     */
    public byte getState() {
        return (byte) (states & 0b11);
    }

    /**
     * @param state The state this node is at
     */
    public void setState(int state) {
        this.states = (byte) ((getState() << 2) | state);
    }

    /**
     * Compare the old state and the state that it is current in.
     *
     * @return True if the old state and the current state are not the same
     */
    public boolean stateUpdate() {
        return (states >> 2) != getState();
    }

    /**
     * Set the location of the connection
     *
     * @param x The X location of the point
     * @param y The Y location of the point
     */
    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
        locationUpdate = true;
    }

    public Point getPoint() {
        return new Point(getX(), getY());
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    /**
     * Check if the location was move since was last set
     *
     * @return True if the loaction was cheange
     */
    public boolean isLocationUpdate() {
        return locationUpdate;
    }

    /**
     * @param locationUpdate set the location to be updated
     */
    public void setLocationUpdate(boolean locationUpdate) {
        this.locationUpdate = locationUpdate;
    }

    /**
     * @return The list of Logic this node was connect to.
     **/
    public Logic[] getLogicInput() {
        return logicInput;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        LogicState other = (LogicState) obj;
        if (x != other.x)
            return false;
        if (y != other.y)
            return false;
        return true;
    }

}
