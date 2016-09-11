package com.logic.output;

import com.logic.Logic;
import com.logic.LogicSettings;
import com.utils.sprite.Sprite;

import java.awt.*;

public class LED extends Output {

    public LED() {
        setBounds(20, 20);
        setLogicStateLocation(new Point[]{new Point(-8, 6)});
        setSprite(Sprite.getSprite(LogicSettings.ID_LED_OFF));
    }

    @Override
    public void update() {
        if (getInputs()[0] != null)
            if (getInputs()[0].isOn())
                setSprite(Sprite.getSprite(LogicSettings.ID_LED_ON));
            else
                setSprite(Sprite.getSprite(LogicSettings.ID_LED_OFF));
    }

    @Override
    public Logic clone() {
        Logic clo = new LED();
        return clone(clo);
    }

}
