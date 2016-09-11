package com.core;

import com.utils.Utils;

/**
 * DataCores is a class that let other class only need the method they
 * need and remove all other unwelcome method
 * <p>
 * Once the number of event is set and all the class that need even added to the
 * right opcode. Pass All the event eg Swing to DataCores.processData(opcode Objects) and all object added to
 * that opcode will get invoke it.
 * <p>
 * The size of the array are set to the size of Process in. If any new Process is added the array will upset by one
 * .
 */
public class DataCores {

    private final int maxIndex;
    private Process mainProcess;
    private ProcessData[][] data = null;

    public DataCores(int maxOPCode) {
        this.maxIndex = maxOPCode;
        data = new ProcessData[maxOPCode][];
    }

    /**
     * @param opcode  The event id
     * @param objects The param of the events
     **/
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

    /**
     * @param opcode      The Id of the event
     * @param processData The Process to invoke once the event is process
     **/
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

    /**
     * @param opcode The ID of the event
     * @param index  The index in the array to remove
     **/
    public void remove(int opcode, int index) {
        if (opcode >= maxIndex)
            throw new IndexOutOfBoundsException(opcode + " out of bounds");
        data[opcode] = Utils.removeObjectFromArray(data[opcode],
                new ProcessData[data[opcode].length - 1], index);
        for (int i = index; i < data[opcode].length; i++)
            data[opcode][i] = data[opcode][i].setIndex(data[opcode][i]
                    .getIndex() - 1);
    }

    /**
     * @param opcode The ID of the event
     * @param data   The ProcessData in which to remove
     */
    public void remove(int opcode, ProcessData data) {
        if (data == null)
            throw new NullPointerException();
        remove(opcode, data.getIndex());
    }

    /**
     * This Process will be invoke when any event is handle by this class
     *
     * @param mainProcess The Process which to invoke
     */
    public void setMainProcess(Process mainProcess) {
        this.mainProcess = mainProcess;
    }

    /**
     * Remove any null data type and
     * TODO Cut down the size of the array
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
