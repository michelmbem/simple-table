package org.addy.simpletable.row.adapter;

public class ArrayRowAdapter implements RowAdapter {
    @Override
    public int getRowCount(Object itemSource) {
        return ((Object[]) itemSource).length;
    }

    @Override
    public Object getRowAt(Object itemSource, int rowIndex) {
        return ((Object[]) itemSource)[rowIndex];
    }

    @Override
    public void setRowAt(Object itemSource, int rowIndex, Object item) {
        ((Object[]) itemSource)[rowIndex] = item;
    }
}
