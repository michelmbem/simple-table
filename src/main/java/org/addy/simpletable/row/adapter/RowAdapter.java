package org.addy.simpletable.row.adapter;

public interface RowAdapter {
    boolean isCellEditable(int rowIndex);
    int getRowCount(Object itemSource);
    Object getRowAt(Object itemSource, int rowIndex);

    default void setRowAt(Object itemSource, int rowIndex, Object item) {
        throw new IllegalStateException("itemSource doesn't support items modification");
    }

    default void addRow(Object itemSource, Object item) {
        throw new IllegalStateException("itemSource doesn't support adding items");
    }

    default void insertRowAt(Object itemSource, int rowIndex, Object item) {
        throw new IllegalStateException("itemSource doesn't support inserting items");
    }

    default void removeRowAt(Object itemSource, int rowIndex) {
        throw new IllegalStateException("itemSource doesn't support removing items");
    }

    default void removeAllRows(Object itemSource) {
        throw new IllegalStateException("itemSource doesn't support removing items");
    }
}
