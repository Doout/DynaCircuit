package com.array;

import com.sun.istack.internal.NotNull;

/**
 * This class will only allow one Thread to edit the array at a time
 *
 * @author Baheer Kamal
 */
public class BlockQueue<T> {

    private Object[] items;
    private int index;

    private int size;
    private int maxSize;

    /**
     * @param initSize The size of the array needed.
     */
    public BlockQueue(int initSize) {
        if (initSize < 10)
            initSize = 10;
        this.maxSize = initSize - 1;
        index = 0;
        size = 0;
        items = new Object[initSize];
    }

    private void upSize() {
        Object[] neitems = new Object[(int) (Math.ceil(items.length * 1.5))];
        int i;
        for (i = 0; !isEmpty(); i++) {
            neitems[i] = pop();
        }
        this.size = i;
        this.maxSize = neitems.length - 1;
        this.index = 0;
        this.items = neitems;
    }

    public Object[] toArray() {
        return items;
    }

    public <K extends T> K[] toArray(@NotNull K[] arrays) {
        for (int i = 0; i < arrays.length; i++) {
            arrays[i] = (K) items[i];
        }
        return arrays;
    }

    /**
     * @return Pop and return the items<br> null if the queue is empty
     */
    public synchronized T pop() {
        if (isEmpty())
            return null;
        if (index >= maxSize)
            index %= maxSize;
        synchronized (items) {
            size--;
            return (T) items[index++];
        }
    }

    /**
     * @param item The object to add to the queue
     */
    public synchronized void push(T item) {
        synchronized (items) {
            if (size == maxSize) {
                upSize();
            }
            items[(index + size++) % maxSize] = item;
        }
    }

    /**
     * @return The size of the queue
     **/
    public int size() {
        return size;
    }

    /**
     * @return True if the queue size is 0
     */
    public boolean isEmpty() {
        return size == 0;
    }

}
