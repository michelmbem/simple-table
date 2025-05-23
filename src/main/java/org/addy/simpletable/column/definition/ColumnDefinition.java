package org.addy.simpletable.column.definition;

import org.addy.simpletable.column.converter.CellConverter;
import org.addy.simpletable.column.validator.CellValidator;

import java.io.Serial;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Objects;
import java.util.stream.Stream;

public class ColumnDefinition implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final String name;
    private Class<?> type;
    private boolean editable;
    private CellConverter converter;
    private CellValidator validator;

    public ColumnDefinition(String name, Class<?> type, boolean editable, CellConverter converter, CellValidator validator) {
        this.name = Objects.requireNonNull(name);
        this.type = type;
        this.editable = editable;
        this.converter = converter;
        this.validator = validator;
    }

    public ColumnDefinition(String name, Class<?> type) {
        this(name, type, true, null, null);
    }

    public ColumnDefinition(String name) {
        this(name, null, true, null, null);
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

    public CellValidator getValidator() {
        return validator;
    }

    public void setValidator(CellValidator validator) {
        this.validator = validator;
    }

    public static ColumnDefinition[] fromNames(String[] names) {
        return Stream.of(names).map(ColumnDefinition::new).toArray(ColumnDefinition[]::new);
    }

    public static ColumnDefinition[] fromResultSet(ResultSet resultSet) {
        try {
            ResultSetMetaData metaData = resultSet.getMetaData();
            var columns = new ColumnDefinition[metaData.getColumnCount()];

            for (int i = 1; i <= columns.length; ++i) {
                columns[i - 1] = new ColumnDefinition(
                        metaData.getColumnName(i),
                        Class.forName(metaData.getColumnClassName(i)),
                        !metaData.isReadOnly(i),
                        null,
                        null
                );
            }

            return columns;
        } catch (SQLException | ClassNotFoundException e) {
            throw new IllegalArgumentException("Could not extract meta data from the given ResultSet", e);
        }
    }

    public static ColumnDefinition[] fromResultSet(ResultSet resultSet, String[] columnNames) {
        try {
            ResultSetMetaData metaData = resultSet.getMetaData();

            return Stream.of(columnNames)
                    .map(columnName -> fromResultSetMetaData(metaData, columnName))
                    .toArray(ColumnDefinition[]::new);
        } catch (SQLException e) {
            throw new IllegalArgumentException("Could not extract meta data from the given ResultSet", e);
        }
    }

    private static ColumnDefinition fromResultSetMetaData(ResultSetMetaData metaData, String columnName) {
        try {
            int columnCount = metaData.getColumnCount();

            for (int i = 1; i <= columnCount; ++i) {
                if (columnName.equalsIgnoreCase(metaData.getColumnName(i)))
                    return new ColumnDefinition(
                            columnName,
                            Class.forName(metaData.getColumnClassName(i)),
                            !metaData.isReadOnly(i),
                            null,
                            null
                    );
            }

            throw new IllegalArgumentException("Could not find a column with the name " + columnName + " in the ResultSet");
        } catch (SQLException | ClassNotFoundException e) {
            throw new IllegalArgumentException("An error occurred while introspecting column " + columnName, e);
        }
    }
}
