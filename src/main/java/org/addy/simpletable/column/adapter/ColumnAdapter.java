package org.addy.simpletable.column.adapter;

/**
 *
 * @author Mike
 */
public interface ColumnAdapter {
    boolean isCellEditable(int columnIndex);
    Object getValueAt(Object item, int columnIndex);
    void setValueAt(Object item, int columnIndex, Object value);
}
