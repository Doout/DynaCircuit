package com;

import java.io.IOException;
import java.util.LinkedList;

public class ChangeHandler {

	// LAZY way of doing shit
	/**
	 * TODO Find out what change and only save that bit of info vs the whole
	 * list.
	 */
	static LinkedList<byte[]> undo = new LimitedStack(10);
	static LinkedList<byte[]> redo = new LimitedStack(10);

	public static void undo() {
		try {
			if (undo.size() == 0)
				return;
			byte[] b = undo.getFirst();
			redo.addFirst(LogicGate.getInstance().getSaveBytes());
			LogicGate.getInstance().loadSave(b);
			undo.remove(0);
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void redo() {
		try {
			if (redo.size() == 0)
				return;
			byte[] b = redo.getFirst();
			undo.addFirst(LogicGate.getInstance().getSaveBytes());
			LogicGate.getInstance().loadSave(b);
			redo.remove(0);
		} catch (IOException | ClassNotFoundException e) {

		}
	}

	public static void addUndo() {
		cleanRedoList();
		try {
			byte[] save = LogicGate.getInstance().getSaveBytes();
			if (undo.isEmpty() || undo.getFirst().length != save.length) {
				undo.addFirst(save);
			}

		} catch (IOException e) {
		}
	}

	public static void cleanRedoList() {
		while (!redo.isEmpty())
			redo.remove();
	}

	/** Item at the bottom of the stack get remove if limit is reach **/
	public static class LimitedStack<E> extends LinkedList<E> {

		private int limit;

		public LimitedStack(int limit) {
			this.limit = limit;
		}

		@Override
		public void addFirst(E o) {
			super.addFirst(o);
			while (size() > limit) {
				super.removeLast();
			}
		}
	}
}
