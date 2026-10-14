package org.addy.simpletable.row.adapter;

import java.lang.reflect.Array;

public class ArrayRowAdapter implements RowAdapter {
    @Override
    public int getRowCount(Object itemSource) {
        return Array.getLength(itemSource);
    }

    @Override
    public Object getRowAt(Object itemSource, int rowIndex) {
        return Array.get(itemSource, rowIndex);
    }

    @Override
    public void setRowAt(Object itemSource, int rowIndex, Object item) {
        Array.set(itemSource, rowIndex, item);
    }
}
