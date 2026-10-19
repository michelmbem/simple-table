package org.addy.simpletable;

import org.addy.simpletable.column.adapter.*;
import org.addy.simpletable.column.converter.CellConverter;
import org.addy.simpletable.column.definition.ColumnDefinition;
import org.addy.simpletable.column.validator.CellValidator;
import org.addy.simpletable.column.validator.ValidationException;
import org.addy.simpletable.column.validator.ValidationResult;
import org.addy.simpletable.row.adapter.ArrayRowAdapter;
import org.addy.simpletable.row.adapter.ListRowAdapter;
import org.addy.simpletable.row.adapter.ResultSetRowAdapter;
import org.addy.simpletable.row.adapter.RowAdapter;
import org.addy.util.ArrayUtil;
import org.addy.util.CollectionUtil;

import javax.swing.table.AbstractTableModel;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.addy.util.ArrayUtil.requiredFirst;

/**
 * A generic implementation of the {@link javax.swing.table.TableModel} interface that virtually
 * accepts any kind of data sources.
 * <p>
 * {@code SimpleTableModel} uses row and column adapters to extract rows and cells from the given data source.<br>
 * It can automatically detect which row or column adapter to use based on the constructor used to create an instance.
 * </p>
 * 
 * @author Michel Mbem
 */
public class SimpleTableModel extends AbstractTableModel {
    private Object itemSource;
    private ColumnDefinition[] columns;
    private RowAdapter rowAdapter;
    private ColumnAdapter columnAdapter;
    private boolean editable = true;

    /**
     * Initializes a new instance of {@link SimpleTableModel} by explicitly setting all its properties.
     *
     * @param itemSource The data source of this {@code SimpleTableModel}. It can be of any type.
     *                   Extracting rows and cell values from it is the responsibility of adapters.
     * @param columns The list of columns of this {@code SimpleTableModel}.
     *                Each column is defined as an instance of {@link ColumnDefinition},
     *                thus providing information like a name, a data type, an editable flag,
     *                a {@link CellConverter}, and a {@link CellValidator}.
     * @param rowAdapter The row adapter of this {@code SimpleTableModel}.
     *                   It's responsible for telling how many rows the {@code itemSource} has,
     *                   and how to extract each row.
     * @param columnAdapter The column adapter used by this instance of {@code SimpleTableModel}.
     *                      It's responsible for extracting the value of the cell corresponding
     *                      to each column of the table for a given row.
     */
    public SimpleTableModel(Object itemSource, ColumnDefinition[] columns,
                            RowAdapter rowAdapter, ColumnAdapter columnAdapter) {

        this.itemSource = itemSource;
        this.columns = columns;
        this.rowAdapter = rowAdapter;
        this.columnAdapter = columnAdapter;
        configureColumnAdapter();
    }

    /**
     * Initializes a new instance of {@link SimpleTableModel} by explicitly setting all its properties.
     * <p>
     *     To simplify the initialization process, this constructor only requires column names.<br/>
     *     Other properties can be set furtherly for each individual column.
     * </p>
     *
     * @param itemSource The data source of this {@code SimpleTableModel}. It can be of any type.
     *                   Extracting rows and cell values from it is the responsibility of adapters.
     * @param columnNames The names of the columns of this {@code SimpleTableModel}.
     *                    The constructor will automatically generate columns with the given names,
     *                    and attach them to the  {@code SimpleTableModel}.
     * @param rowAdapter The row adapter of this {@code SimpleTableModel}.
     *                   It's responsible for telling how many rows the {@code itemSource} has,
     *                   and how to extract each row.
     * @param columnAdapter The column adapter used by this instance of {@code SimpleTableModel}.
     *                      It's responsible for extracting the value of the cell corresponding
     *                      to each column of the table for a given row.
     */
    public SimpleTableModel(Object itemSource, String[] columnNames,
                            RowAdapter rowAdapter, ColumnAdapter columnAdapter) {

        this(itemSource, ColumnDefinition.ofNames(columnNames), rowAdapter, columnAdapter);
    }

    /**
     * Initializes a new instance of {@link SimpleTableModel} with an array of objects as its data source.<br/>
     * Automatically selects {@link ArrayRowAdapter} for this {@code SimpleTableModel}.
     * <p>
     *     To simplify the initialization process, this constructor only requires column names.<br/>
     *     Other properties can be set furtherly for each individual column.
     * </p>
     *
     * @param items An array of objects (of any type) used to set the data source of this {@code SimpleTableModel}.
     * @param columnNames The names of the columns of this {@code SimpleTableModel}.
     *                    The constructor will automatically generate columns with the given names,
     *                    and attach them to the  {@code SimpleTableModel}.
     * @param columnAdapter The column adapter used by this instance of {@code SimpleTableModel}.
     *                      It's responsible for extracting the value of the cell corresponding
     *                      to each column of the table for a given row.
     */
    public SimpleTableModel(Object[] items, String[] columnNames, ColumnAdapter columnAdapter) {
        this(items, ColumnDefinition.ofNames(columnNames), new ArrayRowAdapter(), columnAdapter);
    }

    /**
     * Initializes a new instance of {@link SimpleTableModel} with an array of objects as its data source.<br/>
     * Automatically selects {@link ArrayRowAdapter} for this {@code SimpleTableModel}.<br/>
     * Detects which {@link ColumnAdapter} to use based on the content of {@code items}.
     * <p>
     *     To simplify the initialization process, this constructor only requires column names.<br/>
     *     Other properties can be set furtherly for each individual column.<br/>
     *     The {@code items} array should not be empty. Otherwise {@code SimpleTableModel} will be unable
     *     to guess which {@link ColumnAdapter} to use.
     * </p>
     *
     * @param items An array of objects (of any type) used to set the data source of this {@code SimpleTableModel}.
     * @param columnNames The names of the columns of this {@code SimpleTableModel}.
     *                    The constructor will automatically generate columns with the given names,
     *                    and attach them to the  {@code SimpleTableModel}.
     */
    public SimpleTableModel(Object[] items, String... columnNames) {
        this(items, ColumnDefinition.ofNames(columnNames), new ArrayRowAdapter(),
                ColumnAdapters.from(requiredFirst(items).getClass(), columnNames));
    }

    /**
     * Initializes a new instance of {@link SimpleTableModel} with an array of objects as its data source.<br/>
     * Automatically selects {@link ArrayRowAdapter} for this {@code SimpleTableModel}.
     * <p>
     *     To simplify the initialization process, this constructor only requires column names.<br/>
     *     Other properties can be set furtherly for each individual column.
     * </p>
     *
     * @param items An array of objects (of any type) used to set the data source of this {@code SimpleTableModel}.
     * @param columnNames The names of the columns of this {@code SimpleTableModel}.
     *                    The constructor will automatically generate columns with the given names,
     *                    and attach them to the  {@code SimpleTableModel}.
     */
    public SimpleTableModel(Object[][] items, String... columnNames) {
        this(items, ColumnDefinition.ofNames(columnNames), new ArrayRowAdapter(), new ArrayColumnAdapter());
    }

    /**
     * Initializes a new instance of {@link SimpleTableModel} with a {@link Collection} of objects as its data source.<br/>
     * Wraps {@code items} in a {@link List} and automatically selects {@link ListRowAdapter} for this {@code SimpleTableModel}.
     * <p>
     *     To simplify the initialization process, this constructor only requires column names.<br/>
     *     Other properties can be set furtherly for each individual column.
     * </p>
     *
     * @param items A {@link Collection} of objects (of any type) used to set the data source of this {@code SimpleTableModel}.
     * @param columnNames The names of the columns of this {@code SimpleTableModel}.
     *                    The constructor will automatically generate columns with the given names,
     *                    and attach them to the  {@code SimpleTableModel}.
     * @param columnAdapter The column adapter used by this instance of {@code SimpleTableModel}.
     *                      It's responsible for extracting the value of the cell corresponding
     *                      to each column of the table for a given row.
     */
    public SimpleTableModel(Collection<?> items, String[] columnNames, ColumnAdapter columnAdapter) {
        this(CollectionUtil.toList(items), ColumnDefinition.ofNames(columnNames),
                new ListRowAdapter(), columnAdapter);
    }

    /**
     * Initializes a new instance of {@link SimpleTableModel} with a {@link Collection} of objects as its data source.<br/>
     * Wraps {@code items} in a {@link List} and automatically selects {@link ListRowAdapter} for this {@code SimpleTableModel}.
     * Detects which {@link ColumnAdapter} to use based on the content of {@code items}.
     * <p>
     *     To simplify the initialization process, this constructor only requires column names.<br/>
     *     Other properties can be set furtherly for each individual column.<br/>
     *     The {@code items} collection should not be empty. Otherwise {@code SimpleTableModel} will be unable
     *     to guess which {@link ColumnAdapter} to use.
     * </p>
     *
     * @param items A {@link Collection} of objects (of any type) used to set the data source of this {@code SimpleTableModel}.
     * @param columnNames The names of the columns of this {@code SimpleTableModel}.
     *                    The constructor will automatically generate columns with the given names,
     *                    and attach them to the  {@code SimpleTableModel}.
     */
    public SimpleTableModel(Collection<?> items, String... columnNames) {
        this(CollectionUtil.toList(items), ColumnDefinition.ofNames(columnNames), new ListRowAdapter(),
                ColumnAdapters.from(CollectionUtil.requiredFirst(items).getClass(), columnNames));
    }

    /**
     * Initializes a new instance of {@link SimpleTableModel} that will use a {@link List} of {@code itemType}
     * instances as its data source.<br/>
     * Automatically selects {@link ListRowAdapter} and {@link BeanColumnAdapter} for this {@code SimpleTableModel}.
     *
     * @param itemType The type of elements for the {@link List} that will be set as a data source
     *                 for this {@code SimpleTableModel}.
     * @param propertyNames The names of some properties of {@code itemType} that will be bound
     *                      to the columns of this {@code SimpleTableModel}.
     */
    public SimpleTableModel(Class<?> itemType, String... propertyNames) {
        this(null, ColumnDefinition.ofNames(propertyNames), new ListRowAdapter(),
                ColumnAdapters.from(Objects.requireNonNull(itemType), propertyNames));
    }

    /**
     * Initializes a new instance of {@link SimpleTableModel} that uses a {@link ResultSet} as its data source.
     * <p>
     *     For a {@code ResultSet} to properly work with {@code SimpleTableModel}, it has to be scrollable
     *     and optionally, updatable. Meaning that the {@link java.sql.Statement} instance used to create
     *     that {@code ResultSet} has to be initialized with the {@code ResultSet.TYPE_SCROLL_SENSITIVE},
     *     and optionally, the {@code ResultSet.CONCUR_UPDATABLE} flags.<br/>
     *     This constructor binds the {@code SimpleTableModel} to a specific subset of the {@code ResultSet}
     *     columns and maps them by name.
     * </p>
     *
     * @param resultSet The {@link ResultSet} attached to this {@code SimpleTableModel}.
     * @param columnNames The names of the {@code ResultSet} columns that should be bound
     *                    to the columns of this {@code SimpleTableModel}.
     */
    public SimpleTableModel(ResultSet resultSet, String... columnNames) {
        this(resultSet, ColumnDefinition.fromResultSet(resultSet, columnNames),
                new ResultSetRowAdapter(), new ResultSetColumnAdapter(columnNames));
    }

    /**
     * Initializes a new instance of {@link SimpleTableModel} that uses a {@link ResultSet} as its data source.
     * <p>
     *     For a {@code ResultSet} to properly work with {@code SimpleTableModel}, it has to be scrollable
     *     and optionally, updatable. Meaning that the {@link java.sql.Statement} instance used to create
     *     that {@code ResultSet} has to be initialized with the {@code ResultSet.TYPE_SCROLL_SENSITIVE},
     *     and optionally, the {@code ResultSet.CONCUR_UPDATABLE} flags.<br/>
     *     This constructor binds the {@code SimpleTableModel} to all {@code ResultSet} columns
     *     and maps them by position.
     * </p>
     *
     * @param resultSet The {@link ResultSet} attached to this {@code SimpleTableModel}.
     */
    public SimpleTableModel(ResultSet resultSet) {
        this(resultSet, ColumnDefinition.fromResultSet(resultSet),
                new ResultSetRowAdapter(), new ResultSetColumnAdapter());
    }

    /**
     * Initializes a new instance of {@link SimpleTableModel} with the default configuration.<br/>
     * Each property has to be set individually for the {@code SimpleTableModel} to become operational.
     */
    public SimpleTableModel() {
        this(null,(ColumnDefinition[]) null, null, null);
    }

    /**
     * Gets a reference to the object used as a data source for this {@link SimpleTableModel}.
     * @return An {@link Object}.
     */
    public Object getItemSource() {
        return itemSource;
    }

    /**
     * Sets the data source of this {@link SimpleTableModel}.
     * @param itemSource an {@link Object} of any type.
     *                   Extracting rows and cell values from it is the responsibility of adapters.
     */
    public void setItemSource(Object itemSource) {
        this.itemSource = itemSource;
        fireTableDataChanged();
    }

    /**
     * Gets the column definitions of this {@link SimpleTableModel}.
     * @return An array of instances of {@link ColumnDefinition}.
     */
    public ColumnDefinition[] getColumns() {
        return columns;
    }

    /**
     * Sets the column definitions of this {@link SimpleTableModel}.
     * @param columns An array of instances of {@link ColumnDefinition}.
     */
    public void setColumns(ColumnDefinition[] columns) {
        this.columns = columns;
        configureColumnAdapter();
        fireTableStructureChanged();
    }

    /**
     * Get the row adapter attached to this instance of {@link SimpleTableModel}.
     * <p>
     *     Row adapters have the responsibility of telling how many rows are contained in the {@code itemSource}
     *     and how to extract each of them. They also tell whether a row is editable or not.
     * </p>
     * @return An instance of a class that implements the {@link RowAdapter} interface.
     */
    public RowAdapter getRowAdapter() {
        return rowAdapter;
    }

    /**
     * Sets the row adapter of this instance of {@link SimpleTableModel}.
     * @param rowAdapter An instance of a class that implements the {@link RowAdapter} interface.
     */
    public void setRowAdapter(RowAdapter rowAdapter) {
        this.rowAdapter = rowAdapter;
        fireTableDataChanged();
    }

    /**
     * Get the column adapter attached to this instance of {@link SimpleTableModel}.
     * <p>
     *     Column adapters have the responsibility of reading and writing cell values for each row of a table.
     *     Each cell naturally corresponds to a column. They contribute to tell whether a column is editable or not.
     * </p>
     * @return An instance of a class that implements the {@link ColumnAdapter} interface.
     */
    public ColumnAdapter getColumnAdapter() {
        return columnAdapter;
    }

    /**
     * Sets the column adapter of this instance of {@link SimpleTableModel}.
     * @param columnAdapter An instance of a class that implements the {@link ColumnAdapter} interface.
     */
    public void setColumnAdapter(ColumnAdapter columnAdapter) {
        this.columnAdapter = columnAdapter;
        configureColumnAdapter();
        fireTableStructureChanged();
    }

    /**
     * Gets a value indicating whether this {@link SimpleTableModel} globally supports cell editing or not.
     * @return A {@code boolean}.
     */
    public boolean isEditable() {
        return editable;
    }

    /**
     * Sets a value indicating whether this {@link SimpleTableModel} globally supports cell editing or not.
     * @param editable A {@code boolean}.
     */
    public void setEditable(boolean editable) {
        this.editable = editable;
        fireTableStructureChanged();
    }

    @Override
    public int getRowCount() {
        return !(itemSource == null || rowAdapter == null) ? rowAdapter.getRowCount(itemSource) : 0;
    }

    @Override
    public int getColumnCount() {
        return columns != null ? columns.length : 0;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columns[columnIndex].getName();
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        ColumnDefinition column = columns[columnIndex];

        if (column.getType() == null) {
            if (getRowCount() > 0)
                column.setType(getValueAt(0, columnIndex).getClass());
            else if (columnAdapter instanceof BeanColumnAdapter bca)
                column.setType(bca.getColumnClassAt(columnIndex));
            else
                column.setType(Object.class);
        }

        return column.getType();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object row = rowAdapter.getRowAt(itemSource, rowIndex);
        Object value = row != null ? columnAdapter.getValueAt(row, columnIndex) : null;
        CellConverter converter = columns[columnIndex].getConverter();
        return converter != null ? converter.model2view(value, row) : value;
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        Object row = rowAdapter.getRowAt(itemSource, rowIndex);
        if (row == null) return;

        CellConverter converter = columns[columnIndex].getConverter();
        if (converter != null) value = converter.view2model(value, row);

        CellValidator validator = columns[columnIndex].getValidator();
        if (validator != null) {
            ValidationResult result = validator.validate(value, row);
            if (!result.isValid()) throw new ValidationException(result.getMessage());
        }

        columnAdapter.setValueAt(row, columnIndex, value);
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return editable
                && columns[columnIndex].isEditable()
                && rowAdapter.isCellEditable(rowIndex)
                && columnAdapter.isCellEditable(columnIndex);
    }

    public void addRow(Object row) {
        rowAdapter.addRow(itemSource, row);
        fireTableDataChanged();
    }

    public void insertRowAt(int index, Object row) {
        rowAdapter.insertRowAt(itemSource, index, row);
        fireTableDataChanged();
    }

    public Object getRowAt(int index) {
        return rowAdapter.getRowAt(itemSource, index);
    }

    public void setRowAt(int index, Object row) {
        rowAdapter.setRowAt(itemSource, index, row);
        fireTableDataChanged();
    }

    public void removeRowAt(int index) {
        rowAdapter.removeRowAt(itemSource, index);
        fireTableDataChanged();
    }

    public void removeAllRows() {
        rowAdapter.removeAllRows(itemSource);
        fireTableDataChanged();
    }

    private void configureColumnAdapter() {
        if (columnAdapter instanceof AssociativeColumnAdapter aca
                && ArrayUtil.isEmpty(aca.getColumnNames())
                && !ArrayUtil.isEmpty(columns)) {

            aca.setColumnNames(Stream.of(columns)
                    .map(ColumnDefinition::getName)
                    .toArray(String[]::new));
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public Builder toBuilder() {
        return builder()
                .itemSource(itemSource)
                .addColumns(columns)
                .rowAdapter(rowAdapter)
                .columnAdapter(columnAdapter)
                .editable(editable);
    }

    public static class Builder {
        private final SimpleTableModel tableModel = new SimpleTableModel();
        private final List<ColumnDefinition> tableColumns = new ArrayList<>();

        public Builder itemSource(Object arg) {
            tableModel.setItemSource(arg);
            return this;
        }

        public ColumnBuilder addColumn(ColumnDefinition arg) {
            var columnBuilder = new ColumnBuilder(arg);
            tableColumns.add(columnBuilder.column);
            return columnBuilder;
        }

        public ColumnBuilder addColumn(String arg0, Class<?> arg1) {
            var columnBuilder = new ColumnBuilder(arg0, arg1);
            tableColumns.add(columnBuilder.column);
            return columnBuilder;
        }

        public ColumnBuilder addColumn(String arg) {
            var columnBuilder = new ColumnBuilder(arg);
            tableColumns.add(columnBuilder.column);
            return columnBuilder;
        }

        public Builder addColumns(ColumnDefinition... arg) {
            Stream.of(arg).forEach(this::addColumn);
            return this;
        }

        public Builder addColumns(String... arg) {
            Stream.of(arg).forEach(this::addColumn);
            return this;
        }

        public ColumnBuilder column(int arg) {
            return new ColumnBuilder(tableColumns.get(arg));
        }

        public ColumnBuilder column(String arg) {
            ColumnDefinition column = tableColumns.stream()
                    .filter(c -> c.getName().equals(arg))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Could not find a column with name '" + arg + "' in the table model"));

            return new ColumnBuilder(column);
        }

        public Builder removeColumn(int arg) {
            tableColumns.remove(arg);
            return this;
        }

        public Builder removeColumn(String arg) {
            ColumnDefinition column = tableColumns.stream()
                    .filter(c -> c.getName().equals(arg))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Could not find a column with name '" + arg + "' in the table model"));

            tableColumns.remove(column);
            return this;
        }

        public Builder removeColumns(int arg0, int arg1) {
            for (int i = 0; i < arg1; i++) {
                tableColumns.remove(arg0);
            }

            return this;
        }

        public Builder clearColumns() {
            tableColumns.clear();
            return this;
        }

        public Builder rowAdapter(RowAdapter arg) {
            tableModel.setRowAdapter(arg);
            return this;
        }

        public Builder rowAdapter(Supplier<? extends RowAdapter> arg) {
            tableModel.setRowAdapter(arg.get());
            return this;
        }

        public Builder columnAdapter(ColumnAdapter arg) {
            tableModel.setColumnAdapter(arg);
            return this;
        }

        public Builder columnAdapter(Supplier<? extends ColumnAdapter> arg) {
            tableModel.setColumnAdapter(arg.get());
            return this;
        }

        public Builder editable(boolean arg) {
            tableModel.setEditable(arg);
            return this;
        }

        /**
         * Automatically attaches a suitable {@link RowAdapter} and/or {@link ColumnAdapter}
         * to the built {@link SimpleTableModel} instance if none was explicitly assigned.
         * <p>
         * This method expects the {@link #itemSource} property to be set to a non-null object.
         * It should typically be the last to be invoked in the construction chain before
         * the call to the {@link #build} method.
         * </p>
         * @return the target {@link SimpleTableModel.Builder} instance.
         */
        public Builder generateAdapters() {
            Object itemSource = tableModel.getItemSource();
            if (itemSource == null) return this;

            if (tableModel.getRowAdapter() == null) {
                Class<?> itemSourceType = itemSource.getClass();
                if (itemSourceType.isArray()) {
                    tableModel.setRowAdapter(new ArrayRowAdapter());
                } else if (List.class.isAssignableFrom(itemSourceType)) {
                    tableModel.setRowAdapter(new ListRowAdapter());
                } else if (Collection.class.isAssignableFrom(itemSourceType)) {
                    var collection = (Collection<?>) itemSource;
                    tableModel.setItemSource(CollectionUtil.toList(collection));
                    tableModel.setRowAdapter(new ListRowAdapter());
                } else if (ResultSet.class.isAssignableFrom(itemSourceType)) {
                    tableModel.setRowAdapter(new ResultSetRowAdapter());
                }
            }

            RowAdapter rowAdapter = tableModel.getRowAdapter();
            if (rowAdapter != null
                    && rowAdapter.getRowCount(itemSource) > 0
                    && tableModel.getColumnAdapter() == null) {

                Class<?> itemType = rowAdapter.getRowAt(itemSource, 0).getClass();
                String[] columnNames = tableColumns.stream()
                        .map(ColumnDefinition::getName)
                        .toArray(String[]::new);
                tableModel.setColumnAdapter(ColumnAdapters.from(itemType, columnNames));
            }

            return this;
        }

        public SimpleTableModel build() {
            tableModel.setColumns(tableColumns.toArray(ColumnDefinition[]::new));
            return tableModel;
        }

        public class ColumnBuilder {
            private final ColumnDefinition column;

            public ColumnBuilder(ColumnDefinition column) {
                this.column = column;
            }

            public ColumnBuilder(String name, Class<?> type) {
                column = new ColumnDefinition(name, type);
            }

            public ColumnBuilder(String name) {
                column = new ColumnDefinition(name);
            }

            public ColumnBuilder type(Class<?> arg) {
                column.setType(arg);
                return this;
            }

            public ColumnBuilder editable(boolean arg) {
                column.setEditable(arg);
                return this;
            }

            public ColumnBuilder converter(CellConverter arg) {
                column.setConverter(arg);
                return this;
            }

            public ColumnBuilder converter(Supplier<? extends CellConverter> arg) {
                column.setConverter(arg.get());
                return this;
            }

            public ColumnBuilder validator(CellValidator arg) {
                column.setValidator(arg);
                return this;
            }

            public ColumnBuilder validator(Supplier<? extends CellValidator> arg) {
                column.setValidator(arg.get());
                return this;
            }

            public Builder end() {
                return Builder.this;
            }
        }
    }
}
