package org.addy.simpletable.column.adapter;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

public final class ColumnAdapters {
    private ColumnAdapters() {}

    public static ColumnAdapter from(Class<?> itemType, String[] columnNames) {
        if (itemType.isArray())
            return new ArrayColumnAdapter();

        if (List.class.isAssignableFrom(itemType))
            return new ListColumnAdapter();

        if (Map.class.isAssignableFrom(itemType))
            return new MapColumnAdapter(columnNames);

        if (ResultSet.class.isAssignableFrom(itemType))
            return new ResultSetColumnAdapter(columnNames);

        return new BeanColumnAdapter(itemType, columnNames);
    }
}
