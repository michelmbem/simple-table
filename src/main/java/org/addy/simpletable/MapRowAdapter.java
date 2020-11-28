package org.addy.simpletable;

import java.util.Map;

/**
 *
 * @author Mike
 */
public class MapRowAdapter extends RowAdapterWithColumnNames {
    
    public MapRowAdapter(String... columnNames) {
        super(columnNames);
    }

    @Override
    public Object getValueAt(Object item, int columnIndex, Class columnClass) {
        return ((Map) item).get(columnNames[columnIndex]);
    }

    @Override
    public void setValueAt(Object item, int columnIndex, Class columnClass, Object value) {
        ((Map) item).put(columnNames[columnIndex], value);
    }

}
