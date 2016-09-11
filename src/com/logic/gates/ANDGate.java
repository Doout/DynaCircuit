package com.logic.gates;

import com.LogicState;
import com.logic.Logic;
import com.logic.LogicSettings;
import com.utils.sprite.Sprite;

import java.awt.*;
import java.awt.geom.GeneralPath;

public class ANDGate extends Gate {

    public ANDGate(int numberOfInput) {
        super(numberOfInput);
        setSprite(Sprite.getSprite(LogicSettings.ID_AND));
    }

    @Override
    public void update() {
        int temp = LogicState.ON;
        for (LogicState in : getInputs()) {
            if (in == null) {
                getOutput().setState(LogicState.UNKNOWN);
                return;
            }
            if (in.isOff()) {
                temp = LogicState.OFF;
            }
        }
        getOutput().setState(temp);
    }

    public Shape getShape() {
        GeneralPath p = new GeneralPath();
        p.moveTo(getX(), getY());
        p.lineTo(getX(), getY() + yOffset);
        p.lineTo(getX() + xOffset, getY() + yOffset);
        p.curveTo(getX() + xOffset, getY() + yOffset, getX() + xOffset + (xOffset / 1.5),
                getY() + yOffset / 2, getX() + xOffset, getY());
        p.lineTo(getX(), getY());
        return p;
    }

    @Override
    public Logic clone() {
        Logic clo = new ANDGate(numberOfInput);
        return clone(clo);
    }

}
