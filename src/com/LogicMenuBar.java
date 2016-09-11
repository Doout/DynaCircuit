package com;

import com.core.impl.LogicCore;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.event.ActionEvent;

public class LogicMenuBar extends JMenuBar {
    /**
     *
     */
    private static final long serialVersionUID = -9035800096101673434L;
    // to set if there can be enable or not
    public JMenuItem undo;
    public JMenuItem redo;
    public JMenuItem paste;

    public LogicMenuBar() {
        this.add(getFile());
        this.add(getEdit());
    }

    private JMenu getEdit() {
        JMenu edit = new JMenu("Edit");
        undo = new JMenuItem("Undo");
        undo.addActionListener((ActionEvent e) -> {
        });
        undo.setEnabled(false);
        redo = new JMenuItem("Redo");
        redo.addActionListener((ActionEvent e) -> {
        });
        redo.setEnabled(false);
        JMenuItem copy = new JMenuItem("Copy");
        paste = new JMenuItem("Paste");
        Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
        paste.setEnabled(cb.isDataFlavorAvailable(LogicCore.getLogicDataFlavor()));
        copy.addActionListener((ActionEvent e) -> {
            LogicCore.copy();
            paste.setEnabled(true);
        });

        paste.addActionListener((ActionEvent e) -> {
            LogicCore.paste();
        });

        edit.add(undo);
        edit.add(redo);
        edit.add(copy);
        edit.add(paste);
        return edit;
    }

    public JMenu getFile() {
        JMenu file = new JMenu("File");
        JMenuItem ne = new JMenuItem("New");
        ne.addActionListener((ActionEvent e) -> {
            while (!LogicGate.getInstance().getInputs().isEmpty()) {
                LogicGate.getInstance().getInputs().remove(0);
            }
            while (!LogicGate.getInstance().getLogics().isEmpty()) {
                LogicGate.getInstance().getLogics().remove(0);
            }
        });

        JMenuItem open = new JMenuItem("Open");
        open.addActionListener((ActionEvent e) -> {
            LogicGate.SAVE_LOCATION = LogicGate.getInstance().getFileSaveLocation(false);
            LogicGate.getInstance().load();

        });

        JMenuItem save = new JMenuItem("Save");
        save.addActionListener((ActionEvent e) -> {
            if (LogicGate.SAVE_LOCATION == null)
                LogicGate.SAVE_LOCATION = LogicGate.getInstance().getFileSaveLocation(
                        true);
            LogicGate.getInstance().save();

        });
        JMenuItem saveas = new JMenuItem("Save As");
        saveas.addActionListener((ActionEvent e) -> {
            LogicGate.SAVE_LOCATION = LogicGate.getInstance().getFileSaveLocation(true);
            LogicGate.getInstance().save();
        });
        file.add(ne);
        file.add(open);
        file.add(save);
        file.add(saveas);
        return file;
    }

    public JMenuItem getPaste() {
        return paste;
    }

    public JMenuItem getUndo() {
        return undo;
    }

    public JMenuItem getRedo() {
        return redo;
    }

}
