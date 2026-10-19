package org.addy.simpletable.column.adapter;

import java.lang.reflect.Array;

/**
 * A ColumnAdapter that assumes that the given item is an array.
 *
 * @author Michel Mbem
 */
public class ArrayColumnAdapter implements ColumnAdapter {
    @Override
    public Object getValueAt(Object item, int columnIndex) {
        return Array.get(item, columnIndex);
    }

    @Override
    public void setValueAt(Object item, int columnIndex, Object value) {
        Array.set(item, columnIndex, value);
    }
}
