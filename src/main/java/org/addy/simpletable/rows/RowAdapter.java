package org.addy.simpletable.rows;

/**
 *
 * @author Mike
 */
public interface RowAdapter {
    
    boolean isCellEditable(int columnIndex);
    Object getValueAt(Object item, int columnIndex);
    void setValueAt(Object item, int columnIndex, Object value);

}
