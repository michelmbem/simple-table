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
}
