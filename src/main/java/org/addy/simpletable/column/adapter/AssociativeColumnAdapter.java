package org.addy.simpletable.column.adapter;

/**
 *
 * @author Mike
 */
public abstract class AssociativeColumnAdapter extends BasicColumnAdapter {
    protected String[] columnNames;
    
    public AssociativeColumnAdapter(String... columnNames) {
        this.columnNames = columnNames;
    }

    public String[] getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(String[] columnNames) {
        this.columnNames = columnNames;
    }
}
