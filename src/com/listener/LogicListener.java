package com.listener;

import com.LogicGate;
import com.core.CoreSettings;

import java.awt.event.*;

/**
 * This class is a middle man. All event invoke get process by the class DataCores
 *
 * @author Baheer Kamal
 **/
public class LogicListener implements MouseListener, MouseMotionListener,
        MouseWheelListener, KeyListener {

    @Override
    public void mouseReleased(MouseEvent e) {
        LogicGate.getInstance().getCores().processData(CoreSettings.MOUSE_RELEASED, e);

    }

    @Override
    public void mousePressed(MouseEvent e) {
        LogicGate.getInstance().getCores().processData(CoreSettings.MOUSE_PRESSED, e);
    }

    @Override
    public void mouseExited(MouseEvent arg0) {
        LogicGate.getInstance().getCores().processData(CoreSettings.MOUSE_EXITED, arg0);

    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
        LogicGate.getInstance().getCores().processData(CoreSettings.MOUSE_ENTERED, arg0);

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        LogicGate.getInstance().getCores().processData(CoreSettings.MOUSE_CLICKED, e);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        LogicGate.getInstance().getCores().processData(CoreSettings.MOUSE_DRAGGED, e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        LogicGate.getInstance().getCores().processData(CoreSettings.MOUSE_MOVED, e);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        LogicGate.getInstance().getCores().processData(CoreSettings.MOUSE_WHEEL_MOVED, e);
    }
    // TODO Make it support mult key press

    @Override
    public void keyPressed(KeyEvent e) {
        LogicGate.getInstance().getCores().processData(CoreSettings.KEY_PRESSED, e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        LogicGate.getInstance().getCores().processData(CoreSettings.KEY_RELEASED, e);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        LogicGate.getInstance().getCores().processData(CoreSettings.KEY_TYPED, e);
    }

}
