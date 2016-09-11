package com.core.impl;

import com.LogicGate;
import com.core.CoreSettings;
import com.core.ProcessData;
import com.logic.Logic;
import com.wire.Wire;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * @author Baheer Kamal
 **/
public class WireCore {

    public static ProcessData mouseReleased = new ProcessData((Object... data) -> {
        MouseEvent e = (MouseEvent) data[0];
        Point realPoint = LogicGate.getInstance().getRealPoint(e.getPoint());
        if (Wire.isStartNodeFound()) {
            // check if it still in the same place
            if (!Wire.join(realPoint)) {
                // if return false than object is null and no select node is
                // found
                if (Wire.isStartNodeFound()) {
                    Wire.object.setSelectIndexWire(Wire.stateIndex);
                }
            }
        }
        return null;
    });
    public static ProcessData mousePressed = new ProcessData((Object... data) -> {
        MouseEvent e = (MouseEvent) data[0];
        Point realPoint = LogicGate.getInstance().getRealPoint(e.getPoint());
        if (Wire.isStartNodeFound()) {
            Wire.object.setSelectIndexWire(-1);
            Wire.object = null;
        }
        Wire.getSelectNode(realPoint);
        if (Wire.isStartNodeFound()) {
            LogicCore.removeAllSelect();
            LogicCore.selectArea = null;
        }
        return null;
    });
    public static ProcessData mouseDragged = new ProcessData((Object... data) -> {
        MouseEvent e = (MouseEvent) data[0];
        Point real = LogicGate.getInstance().getRealPoint(e.getPoint());
        Wire.drag = real;
        return null;

    });
    public static ProcessData keyTyped = new ProcessData((Object... data) -> {
        KeyEvent e = (KeyEvent) data[0];
        switch ((int) e.getKeyChar()) {
            case KeyEvent.VK_DELETE:
            case KeyEvent.VK_BACK_SPACE:
                if (Wire.object != null) {
                    Logic c = (Logic) Wire.object;
                    if (c.getSelectIndexWire() == -1)
                        break;
                    if (!Wire.inputState)
                        c.removeOutput(c.getSelectIndexWire());
                    else
                        c.removeInput(c.getSelectIndexWire());
                    c.setSelectIndexWire(-1);
                }
                break;
            default:
                break;
        }
        return null;
    });

    public static void init() {
        LogicGate.getInstance().getCores().add(CoreSettings.MOUSE_RELEASED,
                mouseReleased);
        LogicGate.getInstance().getCores().add(CoreSettings.MOUSE_PRESSED,
                mousePressed);
        LogicGate.getInstance().getCores().add(CoreSettings.MOUSE_DRAGGED,
                mouseDragged);
        LogicGate.getInstance().getCores().add(CoreSettings.KEY_TYPED, keyTyped);
    }
}