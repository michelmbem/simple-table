package org.addy.simpletable.column.editor;

import org.addy.swing.JCalendarCombo;
import org.addy.util.TypeConverter;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class DateTimeTableCellEditor extends AbstractCellEditor
        implements TableCellEditor, PropertyChangeListener {

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
        if (columnClass == null)
            columnClass = table.getModel().getColumnClass(column);

        if (value != null) {
            calendarCombo.setDateTime(TypeConverter.toLocalDateTime(value));
        } else {
            calendarCombo.setChecked(false);
        }

        return calendarCombo;
    }

    @Override
    public Object getCellEditorValue() {
        return calendarCombo.isChecked()
                ? TypeConverter.toType(calendarCombo.getDateTime(), columnClass)
                : null;
    }

    @Override
    public void propertyChange(PropertyChangeEvent e) {
        if (e.getSource() == calendarCombo.getCalendar() && "selectedDateTime".equals(e.getPropertyName())) {
            stopCellEditing();
        }
    }

    private void initDatePicker(String dateFormat) {
        calendarCombo.setDateTimeFormat(mapFormat(dateFormat));
        calendarCombo.setCheckBoxVisible(true);
        calendarCombo.getCalendar().addPropertyChangeListener(this);
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
