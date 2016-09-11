package com.menu;

import com.LogicGate;
import com.Updatable;
import com.core.CoreSettings;
import com.core.ProcessData;
import com.graphics.Renderable;
import com.utils.Bounds;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public abstract class Menu extends Bounds implements Renderable, Updatable {

    public void addListener() {
        LogicGate.getInstance().getCores().add(CoreSettings.MOUSE_MOVED, new ProcessData(
                (Object... data) -> {
                    mouseMoved((MouseEvent) data[0]);
                    return null;
                }));
        LogicGate.getInstance().getCores().add(CoreSettings.MOUSE_CLICKED,
                new ProcessData(
                        (Object... data) -> {
                            mouseClicked((MouseEvent) data[0]);
                            return null;
                        }));
        LogicGate.getInstance().getCores().add(CoreSettings.MOUSE_DRAGGED,
                new ProcessData(
                        (Object... data) -> {
                            mouseDragged((MouseEvent) data[0]);
                            return null;
                        }));
        LogicGate.getInstance().getCores().add(CoreSettings.MOUSE_PRESSED,
                new ProcessData(
                        (Object... data) -> {
                            mousePressed((MouseEvent) data[0]);
                            return null;
                        }));
        LogicGate.getInstance().getCores().add(CoreSettings.MOUSE_RELEASED,
                new ProcessData(
                        (Object... data) -> {
                            mouseReleased((MouseEvent) data[0]);
                            return null;
                        }));
        LogicGate.getInstance().getCores().add(CoreSettings.MOUSE_WHEEL_MOVED,
                new ProcessData(
                        (Object... data) -> {
                            mouseWheelMoved((MouseWheelEvent) data[0]);
                            return null;
                        }));
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
    }

    public void mouseMoved(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseWheelMoved(MouseWheelEvent e) {
    }
}
