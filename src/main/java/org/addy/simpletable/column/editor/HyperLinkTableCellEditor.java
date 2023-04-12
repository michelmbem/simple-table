package org.addy.simpletable.column.editor;

import org.addy.simpletable.event.TableCellActionEvent;
import org.addy.simpletable.event.TableCellActionListener;
import org.addy.simpletable.model.ButtonModel;
import org.addy.simpletable.util.HyperLink;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class HyperLinkTableCellEditor extends AbstractCellEditor
        implements TableCellEditor, MouseListener {

    public static final String COMMAND = "LINK_CLICK";

    private final JLabel label;
    private final TableCellActionListener actionListener;
    private JTable table;
    private Object editedValue = null;
    private boolean useCellValue = false;
    private boolean isLinkColumnEditor;

    public HyperLinkTableCellEditor(TableCellActionListener actionListener) {
        label = new JLabel();
        label.addMouseListener(this);
        useCellValue = true;
        this.actionListener = actionListener;
    }

    public HyperLinkTableCellEditor(String text, TableCellActionListener actionListener) {
        label = new JLabel(HyperLink.format(text));
        label.addMouseListener(this);
        this.actionListener = actionListener;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if (isSelected) {
            label.setForeground(table.getSelectionForeground());
            label.setBackground(table.getSelectionBackground());
        } else {
            label.setForeground(table.getForeground());
            label.setBackground(UIManager.getColor("Button.background"));
        }

        if (this.table == null) setTable(table);

        if (useCellValue) setEditedValue(value);

        return label;
    }

    @Override
    public Object getCellEditorValue() {
        return editedValue;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getSource() == table) {
            if (table.isEditing() && table.getCellEditor() == this)
                isLinkColumnEditor = true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getSource() == table) {
            if (isLinkColumnEditor && table.isEditing())
                table.getCellEditor().stopCellEditing();

            isLinkColumnEditor = false;
        } else if (isLinkColumnEditor) {
            int row = table.convertRowIndexToModel(table.getEditingRow());
            int column = table.convertColumnIndexToModel(table.getEditingColumn());

            fireEditingStopped();

            if (actionListener != null) {
                TableCellActionEvent e1 = new TableCellActionEvent(table, row, column, editedValue, COMMAND);
                actionListener.actionPerformed(e1);
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // Unused
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // Unused
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // Unused
    }

    protected void setTable(JTable table) {
        this.table = table;
        table.addMouseListener(this);
    }

    protected void setEditedValue(Object editedValue) {
        this.editedValue = editedValue;

        if (editedValue == null) {
            label.setText("");
        } else if (editedValue instanceof ButtonModel) {
            ButtonModel buttonModel = (ButtonModel) editedValue;
            label.setText(HyperLink.format(buttonModel.getText()));
        } else {
            label.setText(HyperLink.format(editedValue));
        }
    }
}
