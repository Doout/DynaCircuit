package com.logic.gates;

import com.LogicState;
import com.logic.Logic;
import com.logic.LogicSettings;
import com.utils.sprite.Sprite;

public class XORGate extends Gate {

    public XORGate(int numberOfInput) {
        super(numberOfInput);
        setSprite(Sprite.getSprite(LogicSettings.ID_XOR));
    }

    @Override
    public void update() {
        int number = 0;
        for (LogicState in : getInputs()) {
            if (in == null) {
                getOutput().setState(LogicState.UNKNOWN);
                return;
            }
            if (in.isOn())
                number++;
        }
        getOutput().setState(number % 2);

    }

    @Override
    public Logic clone() {
        Logic clo = new XORGate(numberOfInput);
        return clone(clo);
    }

}
