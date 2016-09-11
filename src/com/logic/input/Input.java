package com.logic.input;

import com.LogicState;
import com.logic.Logic;
import com.wire.Wire;

import java.awt.*;

public abstract class Input extends Logic {

    /**
     *
     */
    private static final long serialVersionUID = 4173820293065985537L;
    private Rectangle wireOutputNode;

    public Input() {
        super(0, 1);
        wireOutputNode = new Rectangle(0, 0, Wire.CONNECTION_R, Wire.CONNECTION_R);
    }

    public void toggle() {
        getOutput().setState(getOutput().getState() ^ 0b1);
    }

    @Override
    public void setLocation(int x, int y) {
        super.setLocation(x, y);
        getOutput().setLocation(x + area.width + Wire.CONNECTION_R / 2, y + area.height
                / 2);
        wireOutputNode.setLocation(getOutput().getX(), getOutput().getY()
                - Wire.CONNECTION_R / 2);

    }

    public LogicState getOutput() {
        return this.outputs[0];
    }

    @Override
    public Rectangle[] getWireInputNode() {
        return null;
    }

    @Override
    public Rectangle[] getWireOutputNode() {
        return new Rectangle[]{wireOutputNode};
    }

    public final void render(Graphics2D g) {
        g.setColor(Color.black);
        Wire.renderConnection(this, g);
        renderInput(g);
        super.render(g);
    }

    public void renderInput(Graphics2D g) {
    }

    ;

}
