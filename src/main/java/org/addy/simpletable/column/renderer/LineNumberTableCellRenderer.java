package org.addy.simpletable.column.renderer;

import org.addy.swing.KnownColor;
import org.addy.swing.icon.ArrowDirection;
import org.addy.swing.icon.ArrowIcon;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class LineNumberTableCellRenderer extends DefaultTableCellRenderer {
    private static final Icon SELECTION_ICON = new ArrowIcon(ArrowDirection.RIGHT, KnownColor.SLATEGRAY, 12, 12);

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                   boolean hasFocus, int row, int column) {

        var label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        label.setText(String.valueOf(row + 1));
        label.setIcon(row == table.getSelectedRow() ? SELECTION_ICON : null);

        return label;
    }
}
