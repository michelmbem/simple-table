package org.addy.simpletable.column.adapter;

import org.addy.util.ArrayUtil;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

/**
 * A factory and utility class for column adapters.
 *
 * @author Michel Mbem
 */
public final class ColumnAdapters {
    private ColumnAdapters() {}

    /**
     * Determines which ColumnAdapter to use based on the type of items present in the table model.
     *
     * @param itemType The type of item associated with each row of the table.
     * @param columnNames The names of table columns.
     * @return An ArrayColumnAdapter if itemType is a type of array,
     *      a ListColumnAdapter if itemType implements the java.util.List interface,
     *      a MapColumnAdapter if itemType implements the java.util.Map interface,
     *      a ResultSetColumnAdapter if itemType implements the java.sql.ResultSet interface,
     *      a BeanColumnAdapter in any other case.
     */
    public static ColumnAdapter from(Class<?> itemType, String[] columnNames) {
        if (itemType.isArray())
            return new ArrayColumnAdapter();

        if (List.class.isAssignableFrom(itemType))
            return new ListColumnAdapter();

        if (Map.class.isAssignableFrom(itemType))
            return new MapColumnAdapter(columnNames);

        if (ResultSet.class.isAssignableFrom(itemType))
            return ArrayUtil.isEmpty(columnNames)
                    ? new ResultSetColumnAdapter()
                    : new ResultSetColumnAdapter(columnNames);

        return new BeanColumnAdapter(itemType, columnNames);
    }
}
