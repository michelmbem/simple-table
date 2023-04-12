package org.addy.simpletable.column.renderer;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class ProgressTableCellRenderer extends JProgressBar implements TableCellRenderer {
    public ProgressTableCellRenderer() {
        super();
    }

    public ProgressTableCellRenderer(int min, int max) {
        super(min, max);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (isSelected) {
            setForeground(table.getSelectionForeground());
            setBackground(table.getSelectionBackground());
        } else {
            setForeground(table.getForeground());
            setBackground(UIManager.getColor("ProgressBar.background"));
        }

        setValue(((Number) value).intValue());

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
            case "value":
            case "minimum":
            case "maximum":
                super.firePropertyChange(propertyName, oldValue, newValue);
                break;
            default:
                break;
        }
    }
}
