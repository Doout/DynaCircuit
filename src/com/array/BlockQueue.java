package com.array;

public class BlockQueue<T> {

	private Object[] items;
	private int index;

	private int size;
	private int maxSize;

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

	public synchronized void push(T item) {
		synchronized (items) {
			if (size == maxSize) {
				upSize();
			}
			items[(index + size++) % maxSize] = item;
		}
	}

	public int size() {
		return size;
	}

	public boolean isEmpty() {
		return size == 0;
	}

}
