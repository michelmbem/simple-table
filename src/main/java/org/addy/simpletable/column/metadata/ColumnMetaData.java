package org.addy.simpletable.column.metadata;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Objects;
import java.util.stream.Stream;

public class ColumnMetaData implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String name;
    private Class<?> type;
    private boolean editable;

    public ColumnMetaData(String name, Class<?> type, boolean editable) {
        this.name = Objects.requireNonNull(name);
        this.type = type;
        this.editable = editable;
    }

    public ColumnMetaData(String name, Class<?> type) {
        this(name, type, true);
    }

    public ColumnMetaData(String name, boolean editable) {
        this(name, null, editable);
    }

    public ColumnMetaData(String name) {
        this(name, null, true);
    }

    public String getName() {
        return name;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public static ColumnMetaData[] fromNames(String[] names) {
        return Stream.of(names).map(ColumnMetaData::new).toArray(ColumnMetaData[]::new);
    }

    public static ColumnMetaData[] fromResultSetAndNames(ResultSet resultSet, String[] names) {
        try {
            ResultSetMetaData rsMetaData = resultSet.getMetaData();

            return Stream.of(names)
                    .map(name -> fromResultSetMetaData(rsMetaData, name))
                    .toArray(ColumnMetaData[]::new);
        } catch (SQLException e) {
            throw new IllegalArgumentException("Could not extract meta data from the given ResultSet", e);
        }
    }

    private static ColumnMetaData fromResultSetMetaData(ResultSetMetaData rsMetaData, String name) {
        try {
            int columnCount = rsMetaData.getColumnCount();

            for (int i = 0; i < columnCount; ++i) {
                if (name.equals(rsMetaData.getColumnName(i)))
                    return new ColumnMetaData(
                            name,
                            Class.forName(rsMetaData.getColumnClassName(i)),
                            !rsMetaData.isReadOnly(i));
            }

            throw new IllegalArgumentException("Could not find a column with name " + name + " in the ResultSet");
        } catch (SQLException | ClassNotFoundException e) {
            throw new IllegalArgumentException("An error occurred while introspecting column " + name, e);
        }
    }
}
