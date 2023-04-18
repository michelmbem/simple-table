package org.addy.simpletable.column.editor;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractCellEditor;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.table.TableCellEditor;

import org.addy.simpletable.SimpleTable;
import org.addy.simpletable.event.TableCellActionEvent;
import org.addy.simpletable.event.TableCellActionListener;
import org.addy.simpletable.model.ButtonModel;

public class ButtonTableCellEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
    public static final String COMMAND = "BUTTON_CLICK";

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

        Component c = table.getCellRenderer(row, column).getTableCellRendererComponent(table, value, isSelected, true, row, column);
        
        button.setForeground(c.getForeground());
        button.setBackground(c.getBackground());

        if (c instanceof JComponent) {
            Border focusBorder = ((JComponent) c).getBorder();

            if (table instanceof SimpleTable) {
                Border rendererBorder = ((SimpleTable) table).getColumnConfig(column).getCellFormat().getBorder();
                button.setBorder(new CompoundBorder(focusBorder, rendererBorder));
            } else {
                button.setBorder(focusBorder);
            }
        }

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
            TableCellActionEvent e1 = new TableCellActionEvent(table, row, column, editedValue, COMMAND);
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
}
