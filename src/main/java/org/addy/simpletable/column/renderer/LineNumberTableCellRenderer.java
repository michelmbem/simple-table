package org.addy.simpletable.column.renderer;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.net.URL;
import java.util.Objects;

public class LineNumberTableCellRenderer extends DefaultTableCellRenderer {
    private static final Icon SELECTION_ICON;

    static {
        URL iconURL = LineNumberTableCellRenderer.class.getClassLoader().getResource("images/icons/bullet_arrow_right.png");
        SELECTION_ICON = new ImageIcon(Objects.requireNonNull(iconURL));
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                   boolean hasFocus, int row, int column) {

        JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        label.setText(String.valueOf(row + 1));
        label.setIcon(row == table.getSelectedRow() ? SELECTION_ICON : null);

        return label;
    }
}
