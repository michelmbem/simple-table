package org.addy.simpletable.model;

import org.addy.simpletable.event.TableCellActionListener;

import javax.swing.*;

public class ButtonModel {
    private String text;
    private Icon icon;

    private TableCellActionListener actionListener;

    public ButtonModel() {}

    public ButtonModel(String text, Icon icon, TableCellActionListener actionListener) {
        this.text = text;
        this.icon = icon;
        this.actionListener = actionListener;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Icon getIcon() {
        return icon;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    public TableCellActionListener getActionListener() {
        return actionListener;
    }

    public void setActionListener(TableCellActionListener actionListener) {
        this.actionListener = actionListener;
    }
}
