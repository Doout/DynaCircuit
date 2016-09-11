package com.core;

import com.sun.istack.internal.NotNull;

/**
 * The class that hold the info the Process that need to be process by DataCores
 */
public class ProcessData {

    private byte index;
    private Process process;

    /**
     * @param p The Process to execute once this ProcessData is process.
     */
    public ProcessData(@NotNull Process p) {
        this.process = p;
    }

    /**
     * @return The index in which this process is in the arrays of other process
     */
    public final int getIndex() {
        return index;
    }

    /**
     * @param index The index of the array this process is in
     */
    protected final ProcessData setIndex(int index) {
        this.index = (byte) (index & 0xFF);
        return this;
    }

    /**
     * @return The process in which this class hold
     */
    public Process getProcess() {
        return process;
    }
}
