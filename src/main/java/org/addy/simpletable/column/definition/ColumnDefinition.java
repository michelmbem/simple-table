package org.addy.simpletable.column.definition;

import org.addy.simpletable.column.converter.CellConverter;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Objects;
import java.util.stream.Stream;

public class ColumnDefinition implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String name;
    private Class<?> type;
    private boolean editable;
    private CellConverter converter;

    public ColumnDefinition(String name, Class<?> type, boolean editable, CellConverter converter) {
        this.name = Objects.requireNonNull(name);
        this.type = type;
        this.editable = editable;
        this.converter = converter;
    }

    public ColumnDefinition(String name, Class<?> type) {
        this(name, type, true, null);
    }

    public ColumnDefinition(String name) {
        this(name, null, true, null);
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

    public CellConverter getConverter() {
        return converter;
    }

    public void setConverter(CellConverter converter) {
        this.converter = converter;
    }

    public static ColumnDefinition[] fromNames(String[] names) {
        return Stream.of(names).map(ColumnDefinition::new).toArray(ColumnDefinition[]::new);
    }

    public static ColumnDefinition[] fromResultSetAndNames(ResultSet resultSet, String[] names) {
        try {
            ResultSetMetaData metaData = resultSet.getMetaData();

            return Stream.of(names)
                    .map(name -> fromResultSetMetaData(metaData, name))
                    .toArray(ColumnDefinition[]::new);
        } catch (SQLException e) {
            throw new IllegalArgumentException("Could not extract meta data from the given ResultSet", e);
        }
    }

    private static ColumnDefinition fromResultSetMetaData(ResultSetMetaData metaData, String name) {
        try {
            int columnCount = metaData.getColumnCount();

            for (int i = 1; i <= columnCount; ++i) {
                if (name.equals(metaData.getColumnName(i)))
                    return new ColumnDefinition(
                            name,
                            Class.forName(metaData.getColumnClassName(i)),
                            !metaData.isReadOnly(i),
                            null);
            }

            throw new IllegalArgumentException("Could not find a column with name " + name + " in the ResultSet");
        } catch (SQLException | ClassNotFoundException e) {
            throw new IllegalArgumentException("An error occurred while introspecting column " + name, e);
        }
    }
}
