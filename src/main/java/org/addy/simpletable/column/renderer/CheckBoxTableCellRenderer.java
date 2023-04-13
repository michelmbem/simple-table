package org.addy.simpletable.column.renderer;

import org.addy.simpletable.util.TypeConverter;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class CheckBoxTableCellRenderer extends JCheckBox implements TableCellRenderer {
    private static final Border NO_FOCUS_BORDER = BorderFactory.createEmptyBorder(1, 1, 1, 1);

    public CheckBoxTableCellRenderer() {
        super();
        setHorizontalAlignment(SwingConstants.CENTER);
        setBorderPainted(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (isSelected) {
            setForeground(table.getSelectionForeground());
            setBackground(table.getSelectionBackground());
        } else {
            setForeground(table.getForeground());
            setBackground(table.getBackground());
        }

        setBorder(hasFocus ? UIManager.getBorder("Table.focusCellHighlightBorder") : NO_FOCUS_BORDER);
        setSelected(TypeConverter.toBoolean(value));

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
            case "selected":
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
}
