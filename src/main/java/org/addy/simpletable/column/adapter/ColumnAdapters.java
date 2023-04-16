package org.addy.simpletable.column.adapter;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

public final class ColumnAdapters {
    private ColumnAdapters() {}

    public static ColumnAdapter from(Class<?> itemClass, String[] columnNames) {
        if (itemClass.isArray())
            return new ArrayColumnAdapter();

        if (List.class.isAssignableFrom(itemClass))
            return new ListColumnAdapter();

        if (Map.class.isAssignableFrom(itemClass))
            return new MapColumnAdapter(columnNames);

        if (ResultSet.class.isAssignableFrom(itemClass))
            return new ResultSetColumnAdapter(columnNames);

        return new BeanColumnAdapter(itemClass, columnNames);
    }
}
