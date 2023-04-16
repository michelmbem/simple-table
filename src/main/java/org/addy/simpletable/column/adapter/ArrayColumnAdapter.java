package org.addy.simpletable.column.adapter;

/**
 *
 * @author Mike
 */
public class ArrayColumnAdapter extends BasicColumnAdapter {
    @Override
    public Object getValueAt(Object item, int columnIndex) {
        return ((Object[]) item)[columnIndex];
    }

    @Override
    public void setValueAt(Object item, int columnIndex, Object value) {
        ((Object[]) item)[columnIndex] = value;
    }
}
