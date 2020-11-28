package org.addy.simpletable;

/**
 *
 * @author Mike
 */
public interface RowAdapter {
    
    public boolean isCellEditable(int columnIndex);
    public Object getValueAt(Object item, int columnIndex, Class columnClass);
    public void setValueAt(Object item, int columnIndex, Class columnClass, Object value);

}
