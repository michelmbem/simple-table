package org.addy.simpletable.column.definition;

import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.Objects;

public class ColumnDefinition {
    private ColumnType columnType;
    private String headerText;
    private int width;
    private boolean resizable;
    private boolean sortable;
    private CellFormat headerFormat;
    private CellFormat cellFormat;
    private Object extraData;

    public ColumnDefinition(ColumnType columnType, String headerText, int width, boolean resizable, boolean sortable,
                            CellFormat headerFormat, CellFormat cellFormat, Object extraData) {

        this.columnType = Objects.requireNonNull(columnType);
        this.headerText = headerText;
        this.width = width;
        this.resizable = resizable;
        this.sortable = sortable;
        this.headerFormat = Objects.requireNonNull(headerFormat);
        this.cellFormat = Objects.requireNonNull(cellFormat);
        this.extraData = extraData;
    }

    public ColumnDefinition(ColumnType columnType, String headerText, int width, CellFormat headerFormat,
                            CellFormat cellFormat, Object extraData) {

        this(columnType, headerText, width, true, true, headerFormat, cellFormat, extraData);
    }

    public ColumnDefinition(ColumnType columnType, String headerText, int width, CellFormat headerFormat, CellFormat cellFormat) {
        this(columnType, headerText, width, true, true, headerFormat, cellFormat, null);
    }

    public ColumnDefinition(ColumnType columnType, String headerText, int width, boolean resizable, boolean sortable, Object extraData) {
        this(columnType, headerText, width, resizable, sortable, CellFormat.DEFAULT, CellFormat.DEFAULT, extraData);
    }

    public ColumnDefinition(ColumnType columnType, String headerText, int width, boolean resizable, boolean sortable) {
        this(columnType, headerText, width, resizable, sortable, CellFormat.DEFAULT, CellFormat.DEFAULT, null);
    }

    public ColumnDefinition(ColumnType columnType, String headerText, int width, Object extraData) {
        this(columnType, headerText, width, true, true, CellFormat.DEFAULT, CellFormat.DEFAULT, extraData);
    }

    public ColumnDefinition(ColumnType columnType, String headerText, int width) {
        this(columnType, headerText, width, true, true, CellFormat.DEFAULT, CellFormat.DEFAULT, null);
    }

    public ColumnDefinition(ColumnType columnType, String headerText) {
        this(columnType, headerText, -1, true, true, CellFormat.DEFAULT, CellFormat.DEFAULT, null);
    }

    public ColumnDefinition() {
        this(ColumnType.DEFAULT, null, -1, true, true, CellFormat.DEFAULT, CellFormat.DEFAULT, null);
    }

    public ColumnType getColumnType() {
        return columnType;
    }

    public void setColumnType(ColumnType columnType) {
        this.columnType = columnType;
    }

    public String getHeaderText() {
        return headerText;
    }

    public void setHeaderText(String headerText) {
        this.headerText = headerText;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public boolean isResizable() {
        return resizable;
    }

    public void setResizable(boolean resizable) {
        this.resizable = resizable;
    }

    public boolean isSortable() {
        return sortable;
    }

    public void setSortable(boolean sortable) {
        this.sortable = sortable;
    }

    public CellFormat getHeaderFormat() {
        return headerFormat;
    }

    public void setHeaderFormat(CellFormat headerFormat) {
        this.headerFormat = headerFormat;
    }

    public CellFormat getCellFormat() {
        return cellFormat;
    }

    public void setCellFormat(CellFormat cellFormat) {
        this.cellFormat = cellFormat;
    }

    public Object getExtraData() {
        return extraData;
    }

    public void setExtraData(Object extraData) {
        this.extraData = extraData;
    }
    
    public void applyTo(TableColumn column, JTable table, int columnIndex) {
        if (headerText != null) column.setHeaderValue(headerText);

        if (width >= 0) column.setPreferredWidth(width);

        if (resizable) {
            column.setMinWidth(0);
            column.setMaxWidth(Integer.MAX_VALUE);
        } else {
            column.setMinWidth(column.getPreferredWidth());
            column.setMaxWidth(column.getPreferredWidth());
        }

        TableRowSorter<?> rowSorter = (TableRowSorter<?>) table.getRowSorter();
        if (rowSorter != null) rowSorter.setSortable(columnIndex, sortable);

        // TODO: improve header management!
        if (column.getHeaderRenderer() instanceof Component)
            headerFormat.applyTo((Component) column.getHeaderRenderer());

        column.setCellRenderer(columnType.getCellRenderer(cellFormat, extraData));
        column.setCellEditor(columnType.getCellEditor(cellFormat, extraData));
    }

    public static Builder builder() {
        return new Builder();
    }


    public static class Builder {
        private final ColumnDefinition columnDefinition = new ColumnDefinition();

        public Builder columnType(ColumnType arg) {
            columnDefinition.setColumnType(arg);
            return this;
        }

        public Builder headerText(String arg) {
            columnDefinition.setHeaderText(arg);
            return this;
        }

        public Builder width(int arg) {
            columnDefinition.setWidth(arg);
            return this;
        }

        public Builder resizable(boolean arg) {
            columnDefinition.setResizable(arg);
            return this;
        }

        public Builder sortable(boolean arg) {
            columnDefinition.setSortable(arg);
            return this;
        }

        public Builder headerFormat(CellFormat arg) {
            columnDefinition.setHeaderFormat(arg);
            return this;
        }

        public Builder cellFormat(CellFormat arg) {
            columnDefinition.setCellFormat(arg);
            return this;
        }

        public Builder extraData(Object arg) {
            columnDefinition.setExtraData(arg);
            return this;
        }

        public ColumnDefinition build() {
            return columnDefinition;
        }
    }
}
