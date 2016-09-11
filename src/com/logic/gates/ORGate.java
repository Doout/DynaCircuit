package com.logic.gates;

import com.LogicState;
import com.logic.Logic;
import com.logic.LogicSettings;
import com.utils.sprite.Sprite;

public class ORGate extends Gate {

    public ORGate(int numberOfInput) {
        super(numberOfInput);
        setSprite(Sprite.getSprite(LogicSettings.ID_OR));
    }

    @Override
    public void update() {
        byte on = LogicState.OFF;
        for (LogicState in : getInputs()) {
            if (in == null) {
                on = LogicState.UNKNOWN;
                break;
            }
            if (in.isOn()) {
                on = LogicState.ON;
            }
        }
        getOutput().setState(on);
    }

    @Override
    public Logic clone() {
        Logic clo = new ORGate(numberOfInput);
        return clone(clo);
    }

}
