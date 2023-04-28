package org.addy.simpletable.util;

import org.addy.simpletable.SimpleTable;
import org.addy.simpletable.column.spec.ColumnSpec;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import java.awt.*;

public final class UIHelper {
    private UIHelper() {}

    public static void prepareEditor(JComponent editor, JTable table, Object value, boolean isSelected, int row, int column) {
        Component c = table.getCellRenderer(row, column).getTableCellRendererComponent(table, value, isSelected, true, row, column);

        editor.setForeground(c.getForeground());
        editor.setBackground(c.getBackground());

        if (c instanceof JComponent) {
            Border focusBorder = ((JComponent) c).getBorder();

            if (table instanceof SimpleTable) {
                ColumnSpec columnSpec = ((SimpleTable) table).getColumnSpec(column);

                if (columnSpec != null) {
                    Border rendererBorder = columnSpec.getCellFormat().getBorder();
                    editor.setBorder(new CompoundBorder(focusBorder, rendererBorder));
                } else {
                    editor.setBorder(focusBorder);
                }
            } else {
                editor.setBorder(focusBorder);
            }
        }
    }
}
