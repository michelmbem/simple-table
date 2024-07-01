package org.addy.simpletable.column.spec;

import org.apache.commons.lang3.ClassUtils;

import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.time.temporal.Temporal;
import java.util.Date;
import java.util.Objects;

public class ColumnSpec {
    private ColumnType columnType;
    private String headerText;
    private int width;
    private boolean resizable;
    private boolean sortable;
    private CellFormat headerFormat;
    private CellFormat cellFormat;
    private Object extraData;

    public ColumnSpec(ColumnType columnType, String headerText, int width, boolean resizable, boolean sortable,
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

    public ColumnSpec(ColumnType columnType, String headerText, int width, boolean resizable, boolean sortable, Object extraData) {
        this(columnType, headerText, width, resizable, sortable, CellFormat.DEFAULT, CellFormat.DEFAULT, extraData);
    }

    public ColumnSpec(ColumnType columnType, String headerText, int width, boolean resizable, boolean sortable) {
        this(columnType, headerText, width, resizable, sortable, CellFormat.DEFAULT, CellFormat.DEFAULT, null);
    }

    public ColumnSpec(ColumnType columnType, String headerText, int width, CellFormat headerFormat,
                      CellFormat cellFormat, Object extraData) {

        this(columnType, headerText, width, true, true, headerFormat, cellFormat, extraData);
    }

    public ColumnSpec(ColumnType columnType, String headerText, int width, CellFormat headerFormat, CellFormat cellFormat) {
        this(columnType, headerText, width, true, true, headerFormat, cellFormat, null);
    }

    public ColumnSpec(ColumnType columnType, String headerText, int width, Object extraData) {
        this(columnType, headerText, width, true, true, CellFormat.DEFAULT, CellFormat.DEFAULT, extraData);
    }

    public ColumnSpec(ColumnType columnType, String headerText, int width) {
        this(columnType, headerText, width, true, true, CellFormat.DEFAULT, CellFormat.DEFAULT, null);
    }

    public ColumnSpec(ColumnType columnType, String headerText, Object extraData) {
        this(columnType, headerText, -1, true, true, CellFormat.DEFAULT, CellFormat.DEFAULT, extraData);
    }

    public ColumnSpec(ColumnType columnType, String headerText) {
        this(columnType, headerText, -1, true, true, CellFormat.DEFAULT, CellFormat.DEFAULT, null);
    }

    public ColumnSpec() {
        this(ColumnType.DEFAULT, null, -1, true, true, CellFormat.DEFAULT, CellFormat.DEFAULT, null);
    }

    public static ColumnSpec lineNumbers(String headerText, int width) {
        return new ColumnSpec(ColumnType.LINENUMBER, headerText, width, false, false, CellFormat.DEFAULT, CellFormat.LINE_END, null);
    }

    public static ColumnSpec of(Class<?> columnClass) {
        if (ClassUtils.isAssignable(columnClass, Boolean.class, true))
            return new ColumnSpec(ColumnType.CHECKBOX, null);

        if (ClassUtils.isAssignable(columnClass, Number.class, true))
            return new ColumnSpec(ColumnType.NUMBER, null);

        if (ClassUtils.isAssignable(columnClass, Date.class) || ClassUtils.isAssignable(columnClass, Temporal.class))
            return new ColumnSpec(ColumnType.DATETIME, null);

        if (ClassUtils.isAssignable(columnClass, Image.class) || ClassUtils.isAssignable(columnClass, ImageIcon.class))
            return new ColumnSpec(ColumnType.IMAGE, null);

        if (columnClass.isEnum())
            return new ColumnSpec(ColumnType.COMBOBOX, null);

        return  new ColumnSpec();
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

        column.setCellRenderer(columnType.getRenderer(extraData));
        column.setCellEditor(columnType.getEditor(extraData));
    }

    public static Builder builder() {
        return new Builder();
    }


    public static class Builder {
        private final ColumnSpec columnSpec = new ColumnSpec();

        public Builder columnType(ColumnType arg) {
            columnSpec.setColumnType(arg);
            return this;
        }

        public Builder headerText(String arg) {
            columnSpec.setHeaderText(arg);
            return this;
        }

        public Builder width(int arg) {
            columnSpec.setWidth(arg);
            return this;
        }

        public Builder resizable(boolean arg) {
            columnSpec.setResizable(arg);
            return this;
        }

        public Builder sortable(boolean arg) {
            columnSpec.setSortable(arg);
            return this;
        }

        public Builder headerFormat(CellFormat arg) {
            columnSpec.setHeaderFormat(arg);
            return this;
        }

        public Builder cellFormat(CellFormat arg) {
            columnSpec.setCellFormat(arg);
            return this;
        }

        public Builder extraData(Object arg) {
            columnSpec.setExtraData(arg);
            return this;
        }

        public ColumnSpec build() {
            return columnSpec;
        }
    }
}
