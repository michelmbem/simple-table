package org.addy.simpletable.column.editor;

import org.addy.swing.JCalendarCombo;
import org.addy.util.TypeConverter;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class DateTimeTableCellEditor extends AbstractCellEditor implements TableCellEditor {
    private final JCalendarCombo calendarCombo = new JCalendarCombo();
    private Class<?> columnClass = null;

    public DateTimeTableCellEditor(String dateFormat) {
        initDatePicker(dateFormat);
    }

    public DateTimeTableCellEditor() {
        initDatePicker("yyyy-MM-dd");
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        TableCellRenderer renderer = table.getCellRenderer(row, column);
        Component c = renderer.getTableCellRendererComponent(table, value, isSelected, true, row, column);

        if (c instanceof JComponent) {
            calendarCombo.setBorder(((JComponent) c).getBorder());
        }

        if (columnClass == null) {
            columnClass = table.getModel().getColumnClass(column);
        }

        if (value != null) {
            calendarCombo.setDate(TypeConverter.toDate(value));
        } else {
            calendarCombo.setChecked(false);
        }

        return calendarCombo;
    }

    @Override
    public Object getCellEditorValue() {
        return calendarCombo.isChecked()
                ? TypeConverter.toType(calendarCombo.getDate(), columnClass)
                : null;
    }

    private void initDatePicker(String dateFormat) {
        calendarCombo.setDateFormat(mapFormat(dateFormat));
        calendarCombo.setCheckBoxVisible(true);
        calendarCombo.getSpinner().setBorder(null);
    }

    private String mapFormat(String dateFormat) {
        switch (dateFormat) {
            case "g":
                return "yyyy-MM-dd HH:mm:ss";
            case "dt":
            case "datetime":
                return "dd/MM/yyyy HH:mm:ss";
            case "d":
            case "date":
                return "dd/MM/yyyy";
            case "t":
            case "time":
                return "HH:mm:ss";
            default:
                return dateFormat;
        }
    }
}
