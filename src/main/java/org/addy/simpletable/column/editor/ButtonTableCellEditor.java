package org.addy.simpletable.column.editor;

import org.addy.simpletable.event.TableCellActionEvent;
import org.addy.simpletable.event.TableCellActionListener;
import org.addy.simpletable.model.ButtonModel;
import org.addy.simpletable.util.UIHelper;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ButtonTableCellEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
    public static final String COMMAND = "BUTTON CLICK";

    private final JButton button;
    private final TableCellActionListener actionListener;
    private JTable table;
    private Object editedValue;
    private boolean isCurrentCellEditor;
    private boolean useCellValue = false;

    public ButtonTableCellEditor(TableCellActionListener actionListener) {
        button = new JButton();
        button.addActionListener(this);
        this.actionListener = actionListener;
        useCellValue = true;
    }

    public ButtonTableCellEditor(String text, TableCellActionListener actionListener) {
        button = new JButton(text);
        button.addActionListener(this);
        this.actionListener = actionListener;
    }

    public ButtonTableCellEditor(Icon icon, TableCellActionListener actionListener) {
        button = new JButton(icon);
        button.addActionListener(this);
        this.actionListener = actionListener;
    }

    public ButtonTableCellEditor(String text, Icon icon, TableCellActionListener actionListener) {
        button = new JButton(text, icon);
        button.addActionListener(this);
        this.actionListener = actionListener;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if (this.table == null) setTable(table);

        UIHelper.prepareEditor(button, table, value, isSelected, row, column);
        setEditedValue(value);

        return button;
    }

    @Override
    public Object getCellEditorValue() {
        return editedValue;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int row = table.convertRowIndexToModel(table.getEditingRow());
        int column = table.convertColumnIndexToModel(table.getEditingColumn());

        fireEditingStopped();

        if (actionListener != null) {
            var e1 = new TableCellActionEvent(table, row, column, editedValue, COMMAND);
            actionListener.actionPerformed(e1);
        }
    }

    protected void setTable(JTable table) {
        this.table = table;
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (table.isEditing() && table.getCellEditor() == ButtonTableCellEditor.this && !isCurrentCellEditor)
                    isCurrentCellEditor = true;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (isCurrentCellEditor && table.isEditing())
                    table.getCellEditor().stopCellEditing();

                isCurrentCellEditor = false;
            }
        });
    }

    protected void setEditedValue(Object editedValue) {
        this.editedValue = editedValue;

        if (useCellValue) {
            if (editedValue == null) {
                button.setText("");
                button.setIcon(null);
            } else if (editedValue instanceof ButtonModel bm) {
                button.setText(bm.getText());
                button.setIcon(bm.getIcon());
            } else if (editedValue instanceof Icon icon) {
                button.setText("");
                button.setIcon(icon);
            } else {
                button.setText(editedValue.toString());
                button.setIcon(null);
            }
        }
    }
}
