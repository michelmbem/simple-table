package org.addy.simpletable.column.editor;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractCellEditor;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.table.TableCellEditor;

import org.addy.simpletable.SimpleTable;
import org.addy.simpletable.event.TableCellActionEvent;
import org.addy.simpletable.event.TableCellActionListener;
import org.addy.simpletable.model.ButtonModel;
import org.addy.simpletable.util.HyperLink;

public class HyperLinkTableCellEditor extends AbstractCellEditor implements TableCellEditor {
    public static final String COMMAND = "LINK_CLICK";

    private final JLabel label;
    private final TableCellActionListener actionListener;
    private JTable table;
    private Object editedValue;
    private boolean isCurrentCellEditor;
    private boolean useCellValue = false;

    public HyperLinkTableCellEditor(TableCellActionListener actionListener) {
        label = new JLabel();
        label.addMouseListener(new LinkMouseAdapter());
        this.actionListener = actionListener;
        useCellValue = true;
    }

    public HyperLinkTableCellEditor(String text, TableCellActionListener actionListener) {
        label = new JLabel(HyperLink.format(text));
        label.addMouseListener(new LinkMouseAdapter());
        this.actionListener = actionListener;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if (this.table == null) setTable(table);

        Component c = table.getCellRenderer(row, column).getTableCellRendererComponent(table, value, isSelected, true, row, column);
        
        label.setForeground(c.getForeground());
        label.setBackground(c.getBackground());

        if (c instanceof JComponent) {
            Border focusBorder = ((JComponent) c).getBorder();

            if (table instanceof SimpleTable) {
                Border rendererBorder = ((SimpleTable) table).getColumnConfig(column).getCellFormat().getBorder();
                label.setBorder(new CompoundBorder(focusBorder, rendererBorder));
            } else {
                label.setBorder(focusBorder);
            }
        }

        setEditedValue(value);

        return label;
    }

    @Override
    public Object getCellEditorValue() {
        return editedValue;
    }

    protected void setTable(JTable table) {
        this.table = table;
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (table.isEditing() && table.getCellEditor() == HyperLinkTableCellEditor.this && !isCurrentCellEditor)
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
                label.setText("");
            } else if (editedValue instanceof ButtonModel) {
                ButtonModel buttonModel = (ButtonModel) editedValue;
                label.setText(HyperLink.format(buttonModel.getText()));
            } else {
                label.setText(HyperLink.format(editedValue));
            }
        }
    }


    protected class LinkMouseAdapter extends MouseAdapter {
        @Override
        public void mouseReleased(MouseEvent e) {
            int row = table.convertRowIndexToModel(table.getEditingRow());
            int column = table.convertColumnIndexToModel(table.getEditingColumn());

            fireEditingStopped();

            if (actionListener != null) {
                TableCellActionEvent e1 = new TableCellActionEvent(table, row, column, editedValue, COMMAND);
                actionListener.actionPerformed(e1);
            }
        }
    }
}
