package org.addy.simpletable.column.adapter;

/**
 * The base class of all column adapters that can map columns by name.
 *
 * @author Michel Mbem
 */
public abstract class AssociativeColumnAdapter implements ColumnAdapter {
    protected String[] columnNames;
    
    protected AssociativeColumnAdapter(String... columnNames) {
        this.columnNames = columnNames;
    }

    public String[] getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(String[] columnNames) {
        this.columnNames = columnNames;
    }
}
