package com.logic.controller;

import com.LogicState;
import com.logic.Logic;
import com.logic.LogicSettings;
import com.utils.sprite.Sprite;

/****/
public class JKFlipFlop extends FlipFlop {

    public JKFlipFlop() {
        super();
        setSprite(Sprite.getSprite(LogicSettings.ID_JKFLIPFLOP));
    }

    /**
     * input 0 is the clock <br>
     * input 1 - J<br>
     * input 2 - K<br>
     * J k | Q (n+1)<br>
     * 0 0 | Q(n) <br>
     * 0 1 | 0 <br>
     * 1 0 | 1 <br>
     * 1 1 | !Q <br>
     **/

    @Override
    public void update() {
        if (!unknownOutput())
            if (isEnable()) {
                if (getInputState(1) == LogicState.ON) {
                    if (getInputState(2) == LogicState.ON) {
                        getOutputs()[1].setState(getOutputs()[0].getState());
                        getOutputs()[0].setState(getOutputs()[0].getToggle());
                    } else {
                        getOutputs()[0].setState(LogicState.ON);
                        getOutputs()[1].setState(LogicState.OFF);
                    }
                } else {
                    if (getInputState(2) == LogicState.ON) {
                        getOutputs()[0].setState(LogicState.OFF);
                        getOutputs()[1].setState(LogicState.ON);
                    }
                    // NO CHANGE IF BOTH ARE OFF

                }
            }

    }

    @Override
    public Logic clone() {
        return clone(new JKFlipFlop());
    }

}
