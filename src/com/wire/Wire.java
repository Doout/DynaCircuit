package com.wire;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.xml.stream.events.EndDocument;

import com.ChangeHandler;
import com.Display;
import com.LogicGate;
import com.LogicState;
import com.core.ProcessData;
import com.logic.Logic;
import com.logic.controller.Controller;
import com.logic.controller.MUX;
import com.logic.input.Input;
import com.logic.output.Output;
import com.utils.Bounds;
import com.utils.Timer;

@SuppressWarnings("unused")
/****/
public class Wire {
 
	public final static int CONNECTION_R = 6;
	public final static int CONNECTION_R2_OFFSET = 8;

	public static Point drag = new Point(0, 0);
	private static Point start = new Point(0, 0);;
	private static Timer timer = new Timer(10);

	public static WireInterface object;

	public static boolean inputState;
	public static int stateIndex;

	public static boolean getSelectNode(Point point) {
		inputState = false;
		boolean found = getSelectNode(LogicGate.getInstance().getLogics(), point) ? true
				: getSelectNode(LogicGate.getInstance().getInputs(), point);
		return found;
	}

	public static WireInterface getLogicState() {
		return object;
	}

	public static boolean isInputState() {
		return inputState;
	}

	public static boolean isStartNodeFound() {
		return object != null;
	}

	private static boolean getSelectNode(Rectangle[] in, Point point, boolean check) {
		if (in != null)
			if (!check || canConnect(in, point)) {
				for (int i = 0; i < in.length; i++) {
					if (Point2D.distance(in[i].getCenterX(), in[i].getCenterY(), point.x,
							point.y) < CONNECTION_R2_OFFSET) {
						if (object == null) {
							start = in[i].getLocation();
							drag = start;
						}
						stateIndex = i;
						return true;
					}
				}
			}
		return false;
	}

	private static boolean getSelectNode(WireInterface in, Point point) {
		boolean check = !(in instanceof Controller);
		if ((inputState = getSelectNode(in.getWireInputNode(), point, check)) ||
				getSelectNode(in.getWireOutputNode(), point, true)) {
			object = in;
			return true;
		}
		return false;
	}

	/**
	 * If the Point is selecting the same location than return false and object
	 * is not null
	 **/
	public static boolean join(Point p) {
		WireInterface w = object;
		int index = stateIndex;
		boolean input = inputState;
		// check if the node is trying to connect to it self.
		if (input ? w.getWireInputNode()[index].contains(p)
				: w.getWireOutputNode()[index].contains(p)) {
			return false;
		}
		//
		boolean found = getSelectNode(p);
		// System.out.println(found);
		if (!found || (input == inputState)) {
			object = null;
			return false;
		}
		ChangeHandler.addUndo();
		if (input) { // index is an input
			join(w, index, object, stateIndex);
		} else {
			join(object, stateIndex, w, index);
		}
		object = null;
		return true;
	}

	/**
	 * This method cast the logic and add the node
	 * 
	 * @param addTo
	 *            The Logic in which to add this connection
	 * @param index
	 *            The index of the connection. Which position of the
	 *            input/output node was it trying to connect to
	 * @param output
	 *            The WireInterface that output the wire
	 * @param index2
	 *            The location in the list of wire from output to connect
	 */
	private static void join(WireInterface addTo, int index, WireInterface output,
			int index2) {
		((Logic) addTo).addInput(output.getOutputs()[index2], index);

	}

	/**
	 * @param in
	 *            The list of WireInterface
	 */
	private static boolean getSelectNode(ArrayList<? extends WireInterface> in,
			Point point) {
		for (WireInterface i : in) {
			if (getSelectNode(i, point))
				return true;
		}
		return false;

	}

	/**
	 * Check if the mouse location can connect to this gate or no <br>
	 * 
	 * @param wireInputNode
	 *            The bounds of the wire node
	 * @param p
	 *            The point in which was selected
	 * 
	 */
	private static boolean canConnect(Rectangle[] wireInputNode, Point p) {
		if (wireInputNode.length <= 0)
			return false;
		if ((wireInputNode[0].x - CONNECTION_R2_OFFSET) < p.x && wireInputNode[0].x
				+ CONNECTION_R2_OFFSET
				+ wireInputNode[0].width > p.x
				&& wireInputNode[0].y - CONNECTION_R2_OFFSET < p.y
				&& wireInputNode[wireInputNode.length - 1].y
						+ wireInputNode[0].height + CONNECTION_R2_OFFSET > p.y)
			return true;
		return false;
	}

	private int getSelectIndex(Rectangle[] wireInputNode, Point point) {
		for (int i = 0; i < wireInputNode.length; i++) {
			if (Point2D.distance(wireInputNode[i].x, wireInputNode[i].y, point.x,
					point.y) < CONNECTION_R2_OFFSET)
				return i;
		}
		return -1;
	}

	/**
	 * @param g
	 *            The Graphics of the canvas
	 */
	public static void render(Graphics2D g) {
		if (timer.isRunning())
			return;
		timer.reset();
		g.setColor(Color.black);
		if (isStartNodeFound())
			g.draw(drawWire(start.x, start.y, drag.x, drag.y, drag.x, ((Bounds) object)
					.getBounds(),
					new Rectangle()));
	}

	/**
	 * Render the connection but will change the color to match the state that
	 * the wire is in.
	 * 
	 * @param in
	 *            The wire to render
	 * @param g
	 *            The graphics of the canvas
	 */
	public static void renderConnection(WireInterface in, Graphics2D g) {
		g.setColor(Color.BLACK);
		if (in.getWireInputNode() != null)
			for (Rectangle rect : in.getWireInputNode())
				g.fillOval(rect.x, rect.y, CONNECTION_R, CONNECTION_R);
		if (in.getWireOutputNode() != null)
			for (Rectangle rect : in.getWireOutputNode())
				g.fillOval(rect.x, rect.y, CONNECTION_R, CONNECTION_R);
		if (in.getSelectIndexWire() >= 0) {
			g.setColor(Color.red);
			if (inputState)
				g.fillOval(in.getWireInputNode()[in.getSelectIndexWire()].x, in
						.getWireInputNode()[in.getSelectIndexWire()].y, CONNECTION_R,
						CONNECTION_R);
			else
				g.fillOval(in.getWireOutputNode()[in.getSelectIndexWire()].x, in
						.getWireOutputNode()[in.getSelectIndexWire()].y, CONNECTION_R,
						CONNECTION_R);
		}
	}

	/**
	 * This method will allow the progrom to go around the bound of any logic in
	 * the way. TODO
	 */
	private static void goAroundBound(GeneralPath path, Rectangle box, int finalX,
			int finalY) {
		// TODO
	}

	/**
	 * This method draw a line from one node to the next node. It will go around
	 * the two logic that it is going to connect to.
	 * 
	 * @param x1
	 *            The location x of the first node.
	 * @param y1
	 *            The location y of the first node
	 * @param x2
	 *            The location x of the second node
	 * @param y2
	 *            The location y of the second node
	 * @param endX
	 *            The location of the second node to end at. Can be set to x2 if
	 *            no gap is needed
	 * @param box
	 *            The bound of the fist logic that the node is connect to.
	 * @param box2
	 *            The bound of the second logic that the second node is connect
	 *            to.
	 **/
	public static GeneralPath drawWire(int x1, int y1, int x2, int y2, int endX,
			Rectangle box, Rectangle box2) {
		if (box == null || box2 == null)
			return null;

		GeneralPath p = new GeneralPath();
		Point2D curr;
		int mid = (x2 - x1) / 2;
		p.moveTo(x1, y1);
		if (mid > 0) {
			p.lineTo(x1 + mid, y1);
			p.lineTo(x1 + mid, y2);
		} else {
			if (x2 - x1 < -4) {
				if (y1 > y2) { // going up
					p.lineTo(x1 + 3, y1); // going to the right
					p.lineTo(p.getCurrentPoint().getX(), y1 - box.getHeight() / 2 - 3);
					// going to the left
					double temp = x1 - box.getWidth() - 3;
					if (temp > x2)
						p.lineTo(x1 - box.getWidth() - 3, p.getCurrentPoint().getY());
					curr = p.getCurrentPoint();
					if (!(curr.getX() > x2 && box2.y < curr.getY()
							&& box2.y + box2.height > curr.getY()))
						p.lineTo(x2, p.getCurrentPoint().getY());
					else
						p.lineTo(box2.getX() + box2.getWidth() + 7,
								p.getCurrentPoint().getY());
				} else { // going down
					p.lineTo(x1 + 3, y1); // going to the right
					p.lineTo(p.getCurrentPoint().getX(), y1 + box.getHeight() / 2 + 3);
					// going to the left
					double temp = x1 + box.getWidth() + 3;
					if (temp < x2)
						p.lineTo(temp, p.getCurrentPoint().getY());
					curr = p.getCurrentPoint();
					if (!(curr.getX() > x2 && box2.y < curr.getY()
							&& box2.y + box2.height > curr.getY()))
						p.lineTo(x2, y1 + box.getHeight() / 2 + 3);
					else
						p.lineTo(box2.getX() + box2.getWidth() + 7,
								y1 + box.getHeight() / 2 + 3);
				}
			} else {
				p.lineTo(x2, y1);
			}
		}
		curr = p.getCurrentPoint();
		if (curr.getX() > x2 && box2.y < curr.getY()
				&& box2.y + box2.height > curr.getY()) {
			double val = curr.getY() - box2.getY();
			if (val > box2.getHeight() / 2) {
				// going down
				p.lineTo(curr.getX(), curr.getY() + ((box2.getY() + box2.height) - curr
						.getY()) + 3);
			} else {
				// going up
				p.lineTo(curr.getX(), curr.getY() - val - 3);
			}
			curr = p.getCurrentPoint();
			p.lineTo(x2, curr.getY());
			p.lineTo(x2, y2);
		} else {
			p.lineTo(x2, y2);
		}
		p.lineTo(endX, y2);
		return p;
	}

}
