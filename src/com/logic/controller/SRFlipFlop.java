package com.logic.controller;

import com.logic.Logic;
import com.logic.LogicSettings;
import com.utils.sprite.Sprite;

public class SRFlipFlop extends FlipFlop {

    public SRFlipFlop() {
        super();
        setSprite(Sprite.getSprite(LogicSettings.ID_SRFLIPFLOP));
    }

    @Override
    public void update() {
        // TODO Auto-generated method stub

    }

    @Override
    public Logic clone() {
        return clone(new SRFlipFlop());
    }

}
