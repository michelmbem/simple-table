package org.addy.simpletable.column.adapter;

/**
 *
 * @author Mike
 */
public interface ColumnAdapter {
    Object getValueAt(Object item, int columnIndex);
    void setValueAt(Object item, int columnIndex, Object value);

    default boolean isCellEditable(int columnIndex) {
        return true;
    }
}
