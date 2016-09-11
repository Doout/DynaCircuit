package com.utils;

/**
 * A Timer
 */
public class Timer {

    private long end;
    private final long start;
    private long period;
    private boolean nano;

    /**
     * Instantiates a new Timer with a given time period in milliseconds.
     *
     * @param period Time period in milliseconds.
     */
    public Timer(long period) {
        this(period, false);
    }

    /**
     * Instantiates a new Timer with a given time period in milliseconds.
     *
     * @param period Time period in milliseconds or milliseconds.
     * @param nano   Use nanoseconds or milliseconds
     */
    public Timer(long period, boolean nano) {
        this.period = period;
        this.nano = nano;
        if (nano)
            this.start = System.nanoTime();
        else
            this.start = System.currentTimeMillis();
        this.end = start + period;
    }

    /**
     * Returns the number of milliseconds elapsed since the start time.
     *
     * @return The elapsed time in milliseconds.
     */
    public long getElapsed() {
        if (nano)
            return (System.nanoTime() - start);
        return (System.currentTimeMillis() - start);
    }

    /**
     * Returns the number of milliseconds remaining until the timer is up.
     *
     * @return The remaining time in milliseconds.
     */
    public long getRemaining() {
        if (isRunning()) {
            if (nano)
                return (end - System.nanoTime());
            return (end - System.currentTimeMillis());
        }
        return 0;
    }

    /**
     * Returns the number of milliseconds that was pass.
     *
     * @return The time that was passed after it stop running.
     */
    public long getTimePassed() {
        if (!isRunning()) {
            if (nano)
                return -(end - System.nanoTime());
            return -(end - System.currentTimeMillis());
        }
        return 0;
    }

    /**
     * Returns <tt>true</tt> if this timer's time period has not yet elapsed.
     *
     * @return <tt>true</tt> if the time period has not yet passed.
     */
    public boolean isRunning() {
        if (nano)
            return (System.nanoTime() < end);
        return (System.currentTimeMillis() < end);
    }

    /**
     * Restarts this timer using its period.
     */
    public void reset() {
        if (nano)
            this.end = System.nanoTime() + period;
        else
            this.end = System.currentTimeMillis() + period;
    }

    /**
     * Sets the end time of this timer to a given number of milliseconds from
     * the time it is called. This does not edit the period of the timer (so
     * will not affect operation after reset).
     *
     * @param ms The number of milliseconds before the timer should stop
     *           running.
     * @return The new end time.
     */
    public long setEndIn(long ms) {
        if (nano)
            this.end = System.nanoTime() + ms;
        else
            this.end = System.currentTimeMillis() + ms;
        return this.end;
    }

    public void setPeriod(long period) {
        this.period = period;
    }

    public long getPeriod() {
        return period;
    }

    /**
     * Returns a formatted String of the time elapsed.
     *
     * @return The elapsed time formatted hh:mm:ss.
     */
    public String toElapsedString() {
        return format(getElapsed() / (nano ? 1000000 : 1));
    }

    /**
     * Returns a formatted String of the time remaining.
     *
     * @return The remaining time formatted hh:mm:ss.
     */
    public String toRemainingString() {
        return format(getRemaining() / (nano ? 1000000 : 1));
    }

    /**
     * Converts milliseconds to a String in the format hh:mm:ss.
     *
     * @param time The number of milliseconds.
     * @return The formatted String.
     */
    public static String format(long time) {
        StringBuilder t = new StringBuilder();
        long total_secs = time / 1000;
        long total_mins = total_secs / 60;
        long total_hrs = total_mins / 60;
        int secs = (int) total_secs % 60;
        int mins = (int) total_mins % 60;
        int hrs = (int) total_hrs % 60;
        if (hrs < 10) {
            t.append("0");
        }
        t.append(hrs);
        t.append(":");
        if (mins < 10) {
            t.append("0");
        }
        t.append(mins);
        t.append(":");
        if (secs < 10) {
            t.append("0");
        }
        t.append(secs);
        return t.toString();
    }
}