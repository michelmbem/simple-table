package org.addy.simpletable.column.editor;

import org.addy.swing.JCalendarCombo;
import org.addy.util.TypeConverter;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class DateTimeTableCellEditor extends AbstractCellEditor implements TableCellEditor {
    private final JCalendarCombo calendarCombo;
    private Class<?> columnClass = null;

    public DateTimeTableCellEditor() {
        calendarCombo = new JCalendarCombo();
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if (isSelected) {
            calendarCombo.setForeground(table.getSelectionForeground());
            calendarCombo.setBackground(table.getSelectionBackground());
        } else {
            calendarCombo.setForeground(table.getForeground());
            calendarCombo.setBackground(table.getBackground());
        }

        TableCellRenderer renderer = table.getCellRenderer(row, column);
        Component c = renderer.getTableCellRendererComponent(table, value, isSelected, true, row, column);

        if (c instanceof JComponent) {
            calendarCombo.setBorder(((JComponent) c).getBorder());
        }

        if (columnClass == null) {
            columnClass = table.getModel().getColumnClass(column);
        }

        calendarCombo.setDate(TypeConverter.toDate(value));

        return calendarCombo;
    }

    @Override
    public Object getCellEditorValue() {
        return TypeConverter.toType(calendarCombo.getDate(), columnClass);
    }
}
