package com.logic.gates;

import com.logic.Logic;
import com.logic.LogicSettings;
import com.utils.sprite.Sprite;

public class XNORGate extends XORGate {

    public XNORGate(int numberOfInput) {
        super(numberOfInput);
        setSprite(Sprite.getSprite(LogicSettings.ID_XNOR),false);
    }

    @Override
    public void update() {
        super.update();
        if (!getOutput().isUnknown())
            getOutput().setState(getOutput().getToggle());
    }


    @Override
    public Logic clone() {
        Logic clo = new XNORGate(numberOfInput);
        return clone(clo);
    }
}
