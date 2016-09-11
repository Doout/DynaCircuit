package com.core.impl;

import com.ChangeHandler;
import com.LogicGate;
import com.core.CoreSettings;
import com.core.Process;
import com.core.ProcessData;
import com.graphics.Renderable;
import com.logic.Logic;
import com.logic.input.Input;
import com.menu.MenuItem;
import com.utils.SerialTransferable;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * This class handle the event for All event that need to happen for the logic
 * This class also handle the event of the popup menu.
 *
 * @author Baheer Kamal
 */
public class LogicCore {

    public static ArrayList<Logic> selectLogics;
    public static ProcessData keyTyped = new ProcessData((Object... data) -> {
        KeyEvent e = (KeyEvent) data[0];
        switch ((int) e.getKeyChar()) {
            case KeyEvent.VK_DELETE:
            case KeyEvent.VK_BACK_SPACE:
                synchronized (LogicGate.LOCK) {
                    if (!selectLogics.isEmpty())
                        while (!selectLogics.isEmpty()) {
                            LogicGate.getInstance().removeLogic(selectLogics.get(0));
                            selectLogics.remove(0);
                        }
                }
                break;
            case 19:
                LogicGate.getInstance().save();
                break;
            case 22: // Paste
                paste();
                break;
            case 3: // Copy
                copy();
                break;
            case 25: //redo
                ChangeHandler.redo();
                break;
            case 26: // undo
                ChangeHandler.undo();
                break;
            default:
                System.out.println((int) e.getKeyChar());
                break;
        }
        return null;
    });
    public static ProcessData mouseClicked = new ProcessData((Object... data) -> {
        MouseEvent e = (MouseEvent) data[0];
        if (PopUpMenuCore.isMenuUp() && PopUpMenuCore.isMouseOverMenu(e)) {
            return null;
        }
        if (e.getButton() == 1) {
            removeAllSelect();
            if (e.getButton() == 1) {
                Point p = LogicGate.getInstance().getRealPoint(e.getPoint());
                for (Input in : LogicGate.getInstance().getInputs()) {
                    if (in.getBounds().contains(p)) {
                        in.toggle();
                        LogicGate.getInstance().getUpdateQueue().push(in);
                        break;
                    }
                }
                for (Logic in : LogicGate.getInstance().getLogics()) {
                    if (in.getBounds().contains(p)) {
                        in.lockBound();
                        ChangeHandler.addUndo();
                        in.setSelect(true);
                        selectLogics.add(in);
                        break;
                    }
                }
                if (selectLogics.isEmpty()) {
                    // Skip this if something is select
                    for (Logic in : LogicGate.getInstance().getInputs()) {
                        if (in.getBounds().contains(p)) {
                            in.lockBound();
                            ChangeHandler.addUndo();
                            in.setSelect(true);
                            selectLogics.add(in);
                            break;
                        }
                    }
                }
            }
        }
        return null;
    });
    static Rectangle selectArea;
    // static ArrayList<Logic> copyGates;
    private static int startX, startY;
    public static ProcessData mousePressed = new ProcessData((Object... data) -> {
        MouseEvent e = (MouseEvent) data[0];
        if (e.getButton() == 1) {
            boolean removeAll = true;
            boolean move = false;
            startX = e.getX();
            startY = e.getY();
            if (selectLogics.size() >= 1) {
                for (Logic b : selectLogics) {
                    b.unlockBound(); // Set the new offset location
                    b.lockBound();
                    if (b.getBounds().contains(LogicGate.getInstance()
                            .getRealPoint(e.getPoint()))) {
                        ChangeHandler.addUndo();
                        move = true;
                        removeAll = false;
                    }
                }
            }
            if (!move) {
                for (Logic b : LogicGate.getInstance().getLogics()) {
                    if (b.getBounds().contains(LogicGate.getInstance()
                            .getRealPoint(e.getPoint()))) {
                        removeAllSelect();
                        b.lockBound();
                        selectLogics.add(b);
                        removeAll = false;
                        break;
                    }
                }
                for (Input b : LogicGate.getInstance().getInputs()) {
                    if (b.getBounds().contains(LogicGate.getInstance()
                            .getRealPoint(e.getPoint()))) {
                        removeAllSelect();
                        b.lockBound();
                        selectLogics.add(b);
                        removeAll = false;
                        break;
                    }
                }
            }
            if (removeAll)
                removeAllSelect();
            if (removeAll) {
                if (e.getX() > LogicGate.MENU_WIDTH)
                    selectArea = new Rectangle(e.getX(), e.getY(), 0, 0);

            }
        }
        return null;
    });
    private transient static Logic newLogic;
    /****/
    public static Renderable render = (Graphics2D g) -> {
        if (newLogic != null)
            newLogic.render(g);
        if (selectArea != null) {
            g.setColor(Color.black);
            g.setStroke(Logic.DASHED);
            g.draw(selectArea);
            g.setStroke(Logic.BASE);
        }
    };
    public static ProcessData mouseReleased = new ProcessData((Object... data) -> {
        // MouseEvent e = (MouseEvent) data[0];
        if (newLogic != null) {
            if (newLogic.canMoveTo()) {
                Logic clone = newLogic.clone();
                synchronized (LogicGate.LOCK) {
                    ChangeHandler.addUndo();
                    if (clone instanceof Input)
                        LogicGate.getInstance().getInputs().add((Input) clone);
                    else
                        LogicGate.getInstance().getLogics().add((Logic) clone);
                }
            }
            newLogic = null;
        }

		/*
         * for (Logic sel : selectLogics) if (sel != null) { /// if
		 * (!sel.canMoveTo()) { // sel.setLocation(startX, startY); // } // sel
		 * = null; }
		 */
        if (selectArea != null) {
            ChangeHandler.addUndo();
            for (Logic l : LogicGate.getInstance().getLogics()) {
                if (selectArea.contains(l.getBounds())) {
                    selectLogics.add(l);
                    l.setSelect(true);
                    l.lockBound();
                }
            }
            for (Logic l : LogicGate.getInstance().getInputs()) {
                if (selectArea.contains(l.getBounds())) {
                    selectLogics.add(l);
                    l.setSelect(true);
                    l.lockBound();
                }
            }
            selectArea = null;
        }

        return null;
    });
    public static ProcessData mouseDragged = new ProcessData((Object... data) -> {
        MouseEvent e = (MouseEvent) data[0];
        Point real = LogicGate.getInstance().getRealPoint(e.getPoint());
        if (newLogic != null) {
            newLogic.setLocation(real.x, real.y);
        }
        if (!selectLogics.isEmpty()) {
            int xOffset = real.x - startX;
            int yOffset = real.y - startY;
            for (Logic sel : selectLogics)
                if (sel != null) {
                    sel.setOffSet(xOffset, yOffset);
                }
        }
        if (selectArea != null) {
            if (real.x < startX && real.y < startY)
                selectArea.setBounds(real.x, real.y, startX - real.x, startY - real.y);
            else if (real.x < startX)
                selectArea.setBounds(real.x, startY, startX - real.x, real.y - startY);
            else if (real.y < startY)
                selectArea.setBounds(startX, real.y, real.x - startX, startY - real.y);
            else
                selectArea.setBounds(startX, startY, real.x - startX, real.y - startY);
        }
        return null;
    });
    /**
     * This should only be pass to the Menu class and not added to the main
     * event handler class
     */
    public static Process menuProcess = (Object... data) -> {
        // The item is an menu Item
        MenuItem menu = (MenuItem) data[0];
        MouseEvent e = (MouseEvent) data[1];
        // The menu item class have a var that is RenderMenuItem
        // since logic have an interface of type RenderMenuItem and the main
        // class pass logic
        newLogic = (Logic) menu.getSprite();
        if (newLogic == null)
            return null;
        newLogic.setLocation(e.getX(), e.getY());
        return null;
    };

    /**
     * Add the event to the datacores
     */
    public static void init() {
        LogicGate.getInstance().getCores().add(CoreSettings.MOUSE_DRAGGED, mouseDragged);
        LogicGate.getInstance().getCores().add(CoreSettings.MOUSE_RELEASED,
                mouseReleased);
        LogicGate.getInstance().getCores().add(CoreSettings.MOUSE_CLICKED, mouseClicked);
        LogicGate.getInstance().getCores().add(CoreSettings.MOUSE_PRESSED, mousePressed);
        LogicGate.getInstance().getCores().add(CoreSettings.KEY_TYPED, keyTyped);
        LogicGate.getInstance().getRenderList().add(render);
        selectLogics = new ArrayList<>(5);

    }

    public static void copy() {
        ArrayList<Logic> copyGates = new ArrayList<>();
        if (!selectLogics.isEmpty()) {
            copyGates.addAll(selectLogics);
            LogicGate.getInstance().getMenubar().getPaste().setEnabled(true);
            PopUpMenuCore.getPaste().setEnabled(true);
            try {
                SerialTransferable sf = new SerialTransferable(copyGates);
                Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
                cb.setContents(sf, null);
            } catch (Exception e) {
            }
        }

    }

    /**
     * Get the data flovor use when the logic gates are copy
     *
     * @return The data flavor of a serialized ArrayList.
     */
    public static DataFlavor getLogicDataFlavor() {
        try {
            return new DataFlavor(
                    "application/x-java-serialized-object;class=" + ArrayList.class
                            .getName());
        } catch (Exception e) { // nothing we can do if fail
            e.printStackTrace();
            return null;
        }

    }

    public static void paste() {
        ChangeHandler.addUndo();
        removeAllSelect();
        Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
        ArrayList<Logic> copyGates = null;

        DataFlavor flavor = getLogicDataFlavor();
        if (!cb.isDataFlavorAvailable(flavor)) {
            return;
        }
        try {
            copyGates = (ArrayList<Logic>) cb.getData(flavor);
        } catch (Exception e) {
            return;
        }
        ArrayList<Logic> log = new ArrayList<>(copyGates.size());
        ArrayList<Input> in = new ArrayList<>(copyGates.size());

        // Set the logic to be moveable and set the copy index
        //The copy index is use to reconnect the logic
        for (Logic l : copyGates) {
            Logic c = l.clone();
            c.setLocation(c.getX() - 5, c.getY() - 5);
            c.lockBound();
            c.setSelect(true);
            selectLogics.add(c);
            if (c instanceof Input) {
                in.add((Input) c);
                l.setIndexOfCopy(-in.size());
            } else {
                log.add(c);
                l.setIndexOfCopy(log.size());
            }
        }
        for (Logic l : copyGates) {
            // The copy logic
            Logic w = l.getIndexOfCopy() < 0 ? in.get(-l.getIndexOfCopy() - 1)
                    : log.get(l.getIndexOfCopy() - 1);
            for (int i = 0; i < l.getOutputs().length; i++) {
                if (l.getOutputs()[i] == null)
                    continue;
                for (Logic g : l.getOutputs()[i].getLogicInput()) {
                    if (g.getIndexOfCopy() <= 0)
                        continue;
                    Logic w2 = log.get(g.getIndexOfCopy() - 1);
                    int index = 0;
                    for (index = 0; index < g.getNumberOfInput(); index++) {
                        if (g.getInputs()[index] != null)
                            if (g.getInputs()[index].equals(l.getOutputs()[i]))
                                if (w2.getInputs()[index] == null)
                                    break;
                    }
                    w2.addInput(w.getOutputs()[i], index);
                }
            }
        }
        // now to join up the gates connection
        synchronized (LogicGate.LOCK) {
            for (Logic g : log) {
                g.setIndexOfCopy(0);
                LogicGate.getInstance().getLogics().add(g);
            }
            for (Input g : in) {
                g.setIndexOfCopy(0);
                LogicGate.getInstance().getInputs().add(g);
            }

        }
    }

    public static void removeAllSelect() {

        while (!selectLogics.isEmpty()) {
            selectLogics.get(0).unlockBound();
            selectLogics.get(0).setSelect(false);
            selectLogics.remove(0);
        }
    }
}
