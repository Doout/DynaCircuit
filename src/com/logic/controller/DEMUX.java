package com.logic.controller;

import com.logic.Logic;
import com.logic.LogicSettings;
import com.utils.sprite.Sprite;
import com.wire.Wire;

import java.awt.*;

public class DEMUX extends Controller {

    private int numberOfSelecter;

    // TODO
    public DEMUX(int numberOfSelecter) {
        super(numberOfSelecter + 1, (int) Math.pow(2, numberOfSelecter));
        setSprite(Sprite.getSprite(LogicSettings.ID_DEMUX));
        this.numberOfSelecter = numberOfSelecter;
        resize();

    }

    /**
     * This method set node location from the base location
     */
    protected void resize() {
        // The first part of the input are for selecter
        int h = 34 + 15 * (numberOfOutput - 1);
        int w = 20 + 10 * (numberOfSelecter - 1);
        setBounds(w, h);
        int yStartInput = 17;
        int xStartSelecter = 10;
        Point[] location = new Point[numberOfInput + numberOfOutput];
        // h2 is the height of the triangle at the bottom of the mux
        double h2 = (double) (0.36734693878 * (double) h) / 2d;
        double angle = Math.tanh(h2 / (double) w);
        int x;

        for (int i = 0; i < numberOfSelecter; i++) {
            x = xStartSelecter + xStartSelecter * i;
            location[i] = new Point(x - Wire.CONNECTION_R / 2, h - getYOffsetOfSelecter(
                    angle, x) + 3);
        }
        location[numberOfSelecter] = new Point(-10, h / 2);
        for (int i = 0; i < numberOfOutput; i++) {
            location[i + numberOfInput] = new Point(w + 5, yStartInput + (15 * i));
        }
        // getOutputs()[0].setLocation(w + 5, h / 2);
        setLogicStateLocation(location);
    }

    private int getYOffsetOfSelecter(double angle, int x) {
        return (int) (Math.tan(angle) * (double) x);
    }

    @Override
    public Logic clone() {
        Logic k = new DEMUX(numberOfSelecter);
        return clone(k);
    }

    @Override
    public void update() {
        int index = encode(getInputs(), numberOfSelecter);
        if (index == -1) {

        }

    }
}
