package com.logic.gates;

import com.LogicState;
import com.logic.Logic;
import com.logic.LogicSettings;
import com.utils.sprite.Sprite;

public class NANDGate extends ANDGate {

    private static final long serialVersionUID = -8108114304317666937L;


    public NANDGate(int numberOfInput) {
        super(numberOfInput);
        setSprite(Sprite.getSprite(LogicSettings.ID_NAND),false);
    }

    @Override
    public void update() {
        super.update();
        if (!getOutput().isUnknown())
            getOutput().setState(getOutput().getState() ^ LogicState.MASK);
    }


    @Override
    public Logic clone() {
        Logic clo = new NANDGate(numberOfInput);
        return clone(clo);
    }
}
