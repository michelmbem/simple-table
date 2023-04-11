package org.addy.simpletable.column.editor;

import org.addy.simpletable.event.TableCellActionEvent;
import org.addy.simpletable.event.TableCellActionListener;
import org.addy.simpletable.util.ButtonModel;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ButtonTableCellEditor extends AbstractCellEditor
        implements TableCellEditor, ActionListener, MouseListener {

    public static final String COMMAND = "BUTTON_CLICK";

    private final JButton button;
    private final TableCellActionListener actionListener;
    private JTable table;
    private Object editedValue = null;
    private boolean useCellValue = false;
    private boolean isButtonColumnEditor;

    public ButtonTableCellEditor(TableCellActionListener actionListener) {
        button = new JButton();
        button.addActionListener(this);
        useCellValue = true;
        this.actionListener = actionListener;
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
        if (isSelected) {
            button.setForeground(table.getSelectionForeground());
            button.setBackground(table.getSelectionBackground());
        } else {
            button.setForeground(table.getForeground());
            button.setBackground(UIManager.getColor("Button.background"));
        }

        if (this.table == null) setTable(table);

        if (useCellValue) setEditedValue(value);

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
            TableCellActionEvent e1 = new TableCellActionEvent(table, row, column, editedValue, COMMAND);
            actionListener.actionPerformed(e1);
        }
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        if (table.isEditing() &&  table.getCellEditor() == this)
            isButtonColumnEditor = true;
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        if (isButtonColumnEditor &&  table.isEditing())
            table.getCellEditor().stopCellEditing();

        isButtonColumnEditor = false;
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {}

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {}

    @Override
    public void mouseExited(MouseEvent mouseEvent) {}

    protected void setTable(JTable table) {
        this.table = table;
        table.addMouseListener(this);
    }

    protected void setEditedValue(Object editedValue) {
        this.editedValue = editedValue;

        if (editedValue == null) {
            button.setText("");
            button.setIcon(null);
        } else if (editedValue instanceof ButtonModel) {
            ButtonModel buttonModel = (ButtonModel) editedValue;
            button.setText(buttonModel.getText());
            button.setIcon(buttonModel.getIcon());
        } else if (editedValue instanceof Icon) {
            button.setText("");
            button.setIcon((Icon) editedValue);
        } else {
            button.setText(editedValue.toString());
            button.setIcon(null);
        }
    }
}
