package org.addy.simpletable.column.editor;

import org.addy.simpletable.column.definition.CellFormat;
import org.addy.simpletable.column.definition.ColumnType;
import org.addy.simpletable.event.TableCellActionListener;
import org.addy.simpletable.model.ButtonModel;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.util.Collection;
import java.util.Vector;

public final class TableCellEditors {
    private TableCellEditors() {}

    public static TableCellEditor from(ColumnType columnType, CellFormat cellFormat, Object extraData) {
        DefaultCellEditor editor;

        switch (columnType) {
            case DATETIME:
                return new DateTimeTableCellEditor();
            case CHECKBOX:
                editor = new DefaultCellEditor(new JCheckBox());
                break;
            case COMBOBOX: {
                ComboBoxModel<?> comboBoxModel = getComboBoxModel(extraData);
                JComboBox<?> comboBox = new JComboBox<>(comboBoxModel);
                editor = new DefaultCellEditor(comboBox);
                break;
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
            case HYPERLINK: {
                HyperLinkTableCellEditor hyperLinkTableCellEditor;

                if (extraData == null) {
                    hyperLinkTableCellEditor = new HyperLinkTableCellEditor(null);
                } else if (extraData instanceof ButtonModel) {
                    ButtonModel buttonModel = (ButtonModel) extraData;
                    hyperLinkTableCellEditor = new HyperLinkTableCellEditor(buttonModel.getText(), buttonModel.getActionListener());
                } else if (extraData instanceof TableCellActionListener) {
                    hyperLinkTableCellEditor = new HyperLinkTableCellEditor((TableCellActionListener) extraData);
                } else {
                    hyperLinkTableCellEditor = new HyperLinkTableCellEditor(extraData.toString(), null);
                }

                return hyperLinkTableCellEditor;
            }
            default:
                editor = new DefaultCellEditor(new JTextField());
                break;
        }

        cellFormat.applyTo(editor.getComponent());

        return editor;
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
