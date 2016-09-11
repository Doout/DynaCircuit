package com.logic;

import com.LogicGate;
import com.LogicState;
import com.Updatable;
import com.graphics.Renderable;
import com.menu.RenderMenuItem;
import com.utils.Bounds;
import com.utils.sprite.Sprite;
import com.wire.Wire;
import com.wire.WireInterface;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.io.Serializable;

/**
 * This class hold all the base info for each logic gate
 * ALl the action that a gate will in counted will get handle
 * <p>
 * This class hold all the method needed to render.
 */
public abstract class Logic extends Bounds implements Updatable, Renderable,
        WireInterface, RenderMenuItem, Serializable {

    /*Some render stuff*/
    public final transient static float dash1[] = {8.0f};
    public final transient static BasicStroke DASHED = new BasicStroke(1.0f,
            BasicStroke.CAP_BUTT,
            BasicStroke.JOIN_MITER,
            10.0f, dash1, 0.0f);
    public final transient static BasicStroke BASE = new BasicStroke(1.0f);

    private static final long serialVersionUID = -7883037761370339336L;

    protected int numberOfInput;
    protected int numberOfOutput;
    protected LogicState[] inputs; // logicState
    protected LogicState[] outputs;
    protected Point[] logicStateLocation;
    protected transient Sprite sprite;
    private boolean canUpdate = true;
    private boolean select = false;
    private Rectangle[] wireInputNode; // wire
    private Rectangle[] wireOutputNode;
    private GeneralPath[] fastpaths;
    private int selectIndexWire = -1;

    private int indexOfCopy;

    /**
     * @param numberOfInput  The number of input for this logic
     * @param numberOfOutput The number of output for this logic<br>
     *                       <br>
     *                       All out put get set to false
     */
    public Logic(int numberOfInput, int numberOfOutput) {
        this.numberOfInput = numberOfInput;
        this.numberOfOutput = numberOfOutput;
        this.inputs = new LogicState[numberOfInput];
        this.outputs = new LogicState[numberOfOutput];
        this.wireInputNode = new Rectangle[numberOfInput];
        this.wireOutputNode = new Rectangle[numberOfOutput];
        this.fastpaths = new GeneralPath[numberOfInput];
        for (int i = 0; i < numberOfOutput; i++)
            outputs[i] = new LogicState();
        for (int i = 0; i < numberOfInput; i++)
            wireInputNode[i] = new Rectangle(0, 0, Wire.CONNECTION_R, Wire.CONNECTION_R);
        for (int i = 0; i < numberOfOutput; i++)
            wireOutputNode[i] = new Rectangle(0, 0, Wire.CONNECTION_R, Wire.CONNECTION_R);
    }

    @Override
    public void render(Graphics2D g) {
        renderPath(g);
        Wire.renderConnection(this, g);
        if (sprite != null)
            g.drawImage(sprite.getImage(), getX(), getY(), getBounds().width,
                    getBounds().height, null);

        if (isSelect()) {
            g.setColor(Color.red);
            g.setStroke(DASHED);
            g.drawRect(getX() - 5, getY() - 5, getBounds().width + 10, getBounds().height
                    + 10);
            g.setStroke(BASE);
        }
    }

    /**
     * @return True if the logic have no free input left
     */
    public boolean isAllInputConnect() {
        for (LogicState g : getInputs())
            if (g == null)
                return false;
        return true;
    }

    /**
     * @return The index of the copy this logic was put into
     */
    public int getIndexOfCopy() {
        return indexOfCopy;
    }

    /**
     * Set the index copy
     *
     * @param indexOfCopy The index in the array this gate was putin
     */
    public void setIndexOfCopy(int indexOfCopy) {
        this.indexOfCopy = indexOfCopy;
    }

    /**
     * Set the location of the connection in the logic
     *
     * @param logicStateLocation The Location of all node
     */
    public void setLogicStateLocation(Point[] logicStateLocation) {
        this.logicStateLocation = logicStateLocation;
    }

    /**
     * Add a output to the index X
     *
     * @param g     The logicState to connect to
     * @param index The index of output to connect
     */
    public void addOutput(LogicState g, int index) {
        this.outputs[index] = g;
        requestPathUpdate();
    }

    /**
     * The logicState will be added to a free output only
     *
     * @param g The logicState to connect to
     */
    public boolean addOutput(LogicState g) {
        int index = -1;
        while (index < numberOfOutput && outputs[++index] != null)
            ;
        if (outputs[index] == null) {
            this.outputs[index] = g;
            requestPathUpdate();
            return true;
        }
        return false;
    }

    public void addInput(LogicState g, int index) {
        this.inputs[index] = g;
        LogicGate.getInstance().getUpdateQueue().push(this);
        g.addConnection(this);
        requestPathUpdate();
    }

    public void removeInput(int index) {
        if (this.inputs[index] == null)
            return;
        this.inputs[index].removeConnection(this, index);
        this.inputs[index] = null;
        // LogicGate.getInstance().getUpdateQueue().add();
        requestPathUpdate();
    }

    private void removeOutput(LogicState logicstate) {
        for (int i = 0; i < inputs.length; i++) {
            if (inputs[i] != null) {
                if (inputs[i].equals(logicstate))
                    inputs[i] = null;
            }
        }
    }

    public void removeSelf() {
        for (int i = 0; i < inputs.length; i++) {
            removeInput(i);
        }
        for (int i = 0; i < outputs.length; i++) {
            removeOutput(i);
        }
    }

    public void removeOutput(int index) {
        LogicState g = getOutputs()[index];
        for (Logic c : g.getLogicInput()) {
            c.removeOutput(g);
        }
        LogicState temp = getOutputs()[index];
        getOutputs()[index] = new LogicState();// reset it
        getOutputs()[index].setLocation(temp.getX(), temp.getY());
        requestPathUpdate();
        System.gc();
    }

    public boolean addInput(LogicState g) {
        int index = -1;
        while (index < numberOfInput && inputs[++index] != null)
            ;
        if (inputs[index] == null) {
            addInput(g, index);
            return true;
        }
        return false;
    }

    public void requestPathUpdate() {
        if (LogicGate.getInstance() == null)
            return;
        synchronized (fastpaths) {
            for (int i = 0; i < getInputs().length; i++) {
                if (inputs[i] == null)
                    continue;
                fastpaths[i] = Wire.drawWire(inputs[i].getX() + Wire.CONNECTION_R,
                        inputs[i].getY(),
                        wireInputNode[i].x - (i * 5) - 5,
                        wireInputNode[i].y + Wire.CONNECTION_R / 2, wireInputNode[i].x,
                        LogicGate
                                .getInstance().findBounds(
                                inputs[i]), this.getBounds());
            }
        }
    }

    @Override
    public void setLocation(int x, int y) {
        super.setLocation(x, y);
        requestUpdateConnection();
        requestPathUpdate();
    }

    public void requestUpdateConnection() {
        if (logicStateLocation == null)
            return;
        for (int i = 0; i < numberOfInput; i++) {
            if (logicStateLocation[i] == null)
                continue;
            wireInputNode[i].setLocation(getX() + logicStateLocation[i].x, getY()
                    + logicStateLocation[i].y);

        }

        for (int i = 0; i < numberOfOutput; i++) {
            wireOutputNode[i].setLocation(getX() + logicStateLocation[i
                    + numberOfInput].x, getY()
                    + logicStateLocation[i + numberOfInput].y);
        }
    }

    public void renderPath(Graphics2D g) {
        g.setStroke(new BasicStroke(2.0f));
        for (int i = 0; i < inputs.length; i++) {
            if (inputs[i] == null)
                continue;
            if (inputs[i].getState() == LogicState.ON)
                g.setColor(Color.green);
            else if (inputs[i].getState() == LogicState.OFF)
                g.setColor(Color.red);
            else {
                g.setColor(Color.BLACK);
            }
            if (fastpaths[i] != null)
                g.draw(fastpaths[i]);
        }

        g.setStroke(BASE);
    }

    @Override
    public Rectangle[] getWireInputNode() {
        return wireInputNode;
    }

    public Rectangle[] getWireOutputNode() {
        return wireOutputNode;
    }

    public int getNumberOfInput() {
        return numberOfInput;
    }

    public int getNumberOfOutput() {
        return numberOfOutput;
    }

    @Override
    public LogicState[] getInputs() {
        return this.inputs;
    }

    @Override
    public LogicState[] getOutputs() {
        return this.outputs;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
        if (sprite != null)
            setBounds(sprite.getBounds().width, sprite.getBounds().height);
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public boolean canUpdate() {
        return canUpdate;
    }

    public void setUpdate(boolean canUpdate) {
        this.canUpdate = canUpdate;
    }

    /**
     * Each level does a step. At this level the object location get change
     **/
    public Logic clone(Logic logic) {
        logic.setLogicStateLocation(logicStateLocation);
        logic.setLocation(getX(), getY());
        logic.update();
        return logic;
    }

    @Override
    public int getSelectIndexWire() {
        return selectIndexWire;
    }

    @Override
    public void setSelectIndexWire(int index) {
        this.selectIndexWire = index;
    }

    /**
     * The first part of the level in which Logic get clone call #link
     * {@link #clone(Logic)} before returning
     **/
    public abstract Logic clone();


}
