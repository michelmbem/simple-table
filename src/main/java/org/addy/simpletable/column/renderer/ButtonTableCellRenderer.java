package org.addy.simpletable.column.renderer;

import org.addy.simpletable.model.ButtonModel;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class ButtonTableCellRenderer extends JButton implements TableCellRenderer {
    private boolean useCellValue = false;

    public ButtonTableCellRenderer() {
        super();
        useCellValue = true;
    }

    public ButtonTableCellRenderer(String text) {
        super(text);
    }

    public ButtonTableCellRenderer(Icon icon) {
        super(icon);
    }

    public ButtonTableCellRenderer(String text, Icon icon) {
        super(text, icon);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                   boolean hasFocus, int row, int column) {

        if (isSelected) {
            setForeground(table.getSelectionForeground());
            setBackground(table.getSelectionBackground());
        } else {
            setForeground(table.getForeground());
            setBackground(UIManager.getColor("Button.background"));
        }

        if (useCellValue) setValue(value);

        return this;
    }

    @Override
    public void invalidate() {
        // Unused!
    }

    @Override
    public void validate() {
        // Unused!
    }

    @Override
    public void revalidate() {
        // Unused!
    }

    @Override
    public void repaint(long tm, int x, int y, int width, int height) {
        // Unused!
    }

    @Override
    public void repaint(Rectangle r) {
        // Unused!
    }

    @Override
    public void repaint() {
        // Unused!
    }

    @Override
    public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
        // Unused!
    }

    @Override
    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        switch (propertyName) {
            case "text":
            case "displayedMnemonic":
            case "icon":
                super.firePropertyChange(propertyName, oldValue, newValue);
                break;
            case "font":
            case "foreground":
                if (oldValue != newValue && getClientProperty(javax.swing.plaf.basic.BasicHTML.propertyKey) != null)
                    super.firePropertyChange(propertyName, oldValue, newValue);
                break;
            default:
                break;
        }
    }

    private void setValue(Object value) {
        if (value == null) {
            setText("");
            setIcon(null);
        } else if (value instanceof ButtonModel) {
            ButtonModel buttonModel = (ButtonModel) value;
            setText(buttonModel.getText());
            setIcon(buttonModel.getIcon());
        } else if (value instanceof Icon) {
            setText("");
            setIcon((Icon) value);
        } else {
            setText(value.toString());
            setIcon(null);
        }
    }
}
