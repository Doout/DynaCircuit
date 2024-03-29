package com.logic.gates;

import com.LogicState;
import com.logic.Logic;
import com.logic.LogicSettings;
import com.utils.sprite.Sprite;

public class NORGate extends ORGate {

    public NORGate(int numberOfInput) {
        super(numberOfInput);
        setSprite(Sprite.getSprite(LogicSettings.ID_NOR),false);
    }

    @Override
    public void update() {
        super.update();
        if (!getOutput().isUnknown())
            getOutput().setState(getOutput().getState() ^ LogicState.MASK);
    }

    @Override
    public Logic clone() {
        Logic clo = new NORGate(numberOfInput);
        return clone(clo);
    }
}
