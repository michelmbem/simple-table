package org.addy.simpletable.rows;

/**
 *
 * @author Mike
 */
public abstract class RowAdapterWithColumnNames extends BasicRowAdapter {
    
    protected String[] columnNames;
    
    public RowAdapterWithColumnNames(String... columnNames) {
        this.columnNames = columnNames;
    }

    public String[] getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(String[] columnNames) {
        this.columnNames = columnNames;
    }

}
