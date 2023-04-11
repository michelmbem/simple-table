package org.addy.simpletable.column.renderer;

import org.addy.simpletable.util.ButtonModel;

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
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
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
