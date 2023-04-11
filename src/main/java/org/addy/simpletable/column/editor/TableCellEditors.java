package org.addy.simpletable.column.editor;

import org.addy.simpletable.column.definition.ColumnType;
import org.addy.simpletable.event.TableCellActionListener;
import org.addy.simpletable.util.ButtonModel;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.util.Collection;
import java.util.Vector;

public final class TableCellEditors {
    private TableCellEditors() {}

    public static TableCellEditor from(ColumnType columnType, int horizontalAlignment, int verticalAlignment, Object extraData) {
        switch (columnType) {
            case CHECKBOX: {
                JCheckBox checkBox = new JCheckBox();
                checkBox.setHorizontalAlignment(SwingConstants.CENTER);
                return new DefaultCellEditor(checkBox);
            }
            case COMBOBOX: {
                ComboBoxModel<?> comboBoxModel = getComboBoxModel(extraData);
                JComboBox<?> comboBox = new JComboBox<>(comboBoxModel);
                return new DefaultCellEditor(comboBox);
            }
            case BUTTON: {
                ButtonTableCellEditor buttonTableCellEditor;

                if (extraData == null) {
                    buttonTableCellEditor = new ButtonTableCellEditor(null);
                } else if (extraData instanceof ButtonModel) {
                    ButtonModel buttonModel = (ButtonModel) extraData;
                    buttonTableCellEditor = new ButtonTableCellEditor(buttonModel.getText(), buttonModel.getIcon(), buttonModel.getActionListener());
                } else if (extraData instanceof TableCellActionListener) {
                    buttonTableCellEditor = new ButtonTableCellEditor((TableCellActionListener) extraData);
                } else if (extraData instanceof Icon) {
                    buttonTableCellEditor = new ButtonTableCellEditor((Icon) extraData, null);
                } else {
                    buttonTableCellEditor = new ButtonTableCellEditor(extraData.toString(), null);
                }

                return buttonTableCellEditor;
            }
            default: {
                JTextField textField = new JTextField();
                applyAlignment(textField, horizontalAlignment, verticalAlignment);
                return new DefaultCellEditor(textField);
            }
        }
    }

    private static void applyAlignment(JTextField textField, int horizontalAlignment, int ignoredVerticalAlignment) {
        if (horizontalAlignment >= 0)
            textField.setHorizontalAlignment(horizontalAlignment);
    }

    private static ComboBoxModel<?> getComboBoxModel(Object data) {
        if (data == null)
            return new DefaultComboBoxModel<>();

        Class<?> dataType = data.getClass();

        if (ComboBoxModel.class.isAssignableFrom(dataType))
            return (ComboBoxModel<?>) data;

        if (dataType.isArray())
            return new DefaultComboBoxModel<>((Object[]) data);

        if (Collection.class.isAssignableFrom(dataType)) {
            Vector<?> vector = new Vector<>((Collection<?>) data);
            return new DefaultComboBoxModel<>(vector);
        }

        throw new IllegalArgumentException("Invalid dataSource for the ComboBox column");
    }
}
