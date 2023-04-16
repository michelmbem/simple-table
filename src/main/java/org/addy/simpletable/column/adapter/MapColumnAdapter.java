package org.addy.simpletable.column.adapter;

import java.util.Map;

/**
 *
 * @author Mike
 */
public class MapColumnAdapter extends AssociativeColumnAdapter {
    public MapColumnAdapter(String... columnNames) {
        super(columnNames);
    }

    @Override
    public Object getValueAt(Object item, int columnIndex) {
        return ((Map) item).get(columnNames[columnIndex]);
    }

    @Override
    public void setValueAt(Object item, int columnIndex, Object value) {
        ((Map) item).put(columnNames[columnIndex], value);
    }
}
