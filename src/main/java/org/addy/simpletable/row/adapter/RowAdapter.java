package org.addy.simpletable.row.adapter;

public interface RowAdapter {
    boolean isCellEditable(int rowIndex);
    int getRowCount(Object itemSource);
    Object getRowAt(Object itemSource, int rowIndex);
}
