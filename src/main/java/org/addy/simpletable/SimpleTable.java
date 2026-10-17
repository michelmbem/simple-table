package org.addy.simpletable;

import org.addy.simpletable.column.spec.CellFormat;
import org.addy.simpletable.column.spec.ColumnSpec;
import org.addy.simpletable.column.spec.ColumnType;
import org.addy.simpletable.row.header.RowHeaderView;
import org.addy.swing.KnownColor;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * An easily configurable JTable.<br>
 * Manages alternated row backgrounds as well as row highlighting on mouse hover and automatic sorting.<br>
 * Automatically generates cell renderers and editors for columns based on the given columnSpec specifications.
 *
 * @author Mike
 */
public class SimpleTable extends JTable {
    public static final int DEFAULT_ROW_HEIGHT = 20;

    private Color alternateBackground;
    private Color rolloverBackground;
    private int rolloverRowIndex = -1;
    private ColumnSpec[] columnSpecs = null;
    private RowHeaderView rowHeaderView = null;

    public SimpleTable() {
        super();
        setRowHeight(DEFAULT_ROW_HEIGHT);
        setAutoResizeMode(AUTO_RESIZE_OFF);
        setAlternateBackground(KnownColor.WHITE_BLUE);
        setRolloverBackground(KnownColor.IVORY);
        setAutoCreateRowSorter(true);
        createRolloverListener();
    }

    public SimpleTable(TableModel model) {
        this();
        setModel(model);
    }

    public SimpleTable(TableModel tm, TableColumnModel cm) {
        this(tm);
        setColumnModel(cm);
    }

    public SimpleTable(TableModel tm, TableColumnModel cm, ListSelectionModel sm) {
        this(tm, cm);
        setSelectionModel(sm);
    }

    public Color getAlternateBackground() {
        return alternateBackground;
    }

    public void setAlternateBackground(Color alternateBackground) {
        this.alternateBackground = alternateBackground;
        repaint();
    }

    public Color getRolloverBackground() {
        return rolloverBackground;
    }

    public void setRolloverBackground(Color rolloverBackground) {
        this.rolloverBackground = rolloverBackground;
    }

    public ColumnSpec[] getColumnSpecs() {
        return columnSpecs;
    }

    public void setColumnSpecs(ColumnSpec... columnSpecs) {
        this.columnSpecs = Objects.requireNonNull(columnSpecs);
        applyColumnSpecs();
    }

    public ColumnSpec getColumnSpec(int columnIndex) {
        return columnSpecs != null && columnIndex < columnSpecs.length
                ? columnSpecs[columnIndex]
                : null;
    }

    public void setColumnSpec(int columnIndex, ColumnSpec columnSpec) {
        if (columnSpecs != null && columnIndex < columnSpecs.length) {
            columnSpecs[columnIndex] = columnSpec;
            columnSpec.applyTo(getColumnModel().getColumn(columnIndex), this, columnIndex);
        } else {
            throw new ArrayIndexOutOfBoundsException(columnIndex);
        }
    }

    public void generateColumnSpecs() {
        TableModel model = getModel();
        if (model == null) return;

        columnSpecs = new ColumnSpec[getColumnCount()];
        for (int i = 0; i < columnSpecs.length; ++i) {
            columnSpecs[i] = ColumnSpec.of(model.getColumnClass(i));
        }

        applyColumnSpecs();
    }

    public void applyColumnSpecs() {
        if (columnSpecs == null) return;

        TableColumnModel columnModel = getColumnModel();
        int columnCount = columnModel.getColumnCount();

        for (int i = 0; i < columnSpecs.length && i < columnCount; ++i) {
            columnSpecs[i].applyTo(columnModel.getColumn(i), this, i);
        }
    }

    public RowHeaderView getRowHeaderView() {
        return rowHeaderView;
    }

    public void createRowHeaders(JScrollPane scrollPane, ColumnType columnType,
                                 String headerText, int width, CellFormat cellFormat) {

        rowHeaderView = new RowHeaderView(this, columnType, headerText, width, cellFormat);
        var rowHeader = new JViewport();
        rowHeader.setView(rowHeaderView);
        rowHeader.setPreferredSize(rowHeaderView.getPreferredSize());
        scrollPane.setRowHeader(rowHeader);
        scrollPane.setCorner(ScrollPaneConstants.UPPER_LEFT_CORNER, rowHeaderView.getTableHeader());
    }

    @Override
    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
        var component = (JComponent) super.prepareRenderer(renderer, row, column);
        ColumnSpec colSpec = getColumnSpec(column);

        if (colSpec != null)
            colSpec.getCellFormat().applyTo(component, false);

        if (!component.getBackground().equals(getSelectionBackground())) {
            if (rolloverBackground != null && row == rolloverRowIndex) {
                component.setForeground(getForeground());
                component.setBackground(rolloverBackground);
            } else if (alternateBackground != null) {
                if (!(colSpec == null || colSpec.getCellFormat().getBackground() == null)) {
                    component.setBackground(colSpec.getCellFormat().getBackground());
                } else {
                    component.setBackground(row % 2 == 1 ? alternateBackground : getBackground());
                }
            }
        }

        return component;
    }

    @Override
    public Component prepareEditor(TableCellEditor editor, int row, int column) {
        var component = (JComponent) super.prepareEditor(editor, row, column);
        ColumnSpec colSpec = getColumnSpec(column);

        if (colSpec != null)
            colSpec.getCellFormat().applyTo(component, true);

        return component;
    }

    protected void createRolloverListener() {
        var rolloverListener = new RolloverListener();
        addMouseMotionListener(rolloverListener);
        addMouseListener(rolloverListener);
    }


    protected class RolloverListener extends MouseInputAdapter {
        @Override
        public void mouseExited(MouseEvent e) {
            rolloverRowIndex = -1;
            repaint();
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            int row = rowAtPoint(e.getPoint());
            if (row != rolloverRowIndex) {
                rolloverRowIndex = row;
                repaint();
            }
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public Builder toBuilder() {
        return builder()
                .model(getModel())
                .rowHeight(getRowHeight())
                .alternateBackground(alternateBackground)
                .rolloverBackground(rolloverBackground)
                .addColumnSpecs(columnSpecs);
    }

    public static class Builder {
        private final SimpleTable table = new SimpleTable();
        private final List<ColumnSpec> columnSpecs = new ArrayList<>();

        public Builder model(TableModel arg) {
            table.setModel(arg);
            return this;
        }

        public Builder rowHeight(int arg) {
            table.setRowHeight(arg);
            return this;
        }

        public Builder alternateBackground(Color arg) {
            table.setAlternateBackground(arg);
            return this;
        }

        public Builder rolloverBackground(Color arg) {
            table.setRolloverBackground(arg);
            return this;
        }

        public ColumnSpecBuilder addColumnSpec(ColumnSpec arg) {
            var columnSpecBuilder = new ColumnSpecBuilder(arg);
            columnSpecs.add(columnSpecBuilder.columnSpec);
            return columnSpecBuilder;
        }

        public ColumnSpecBuilder addColumnSpec(ColumnType arg0, String arg1, int arg2) {
            var columnSpecBuilder = new ColumnSpecBuilder(arg0, arg1, arg2);
            columnSpecs.add(columnSpecBuilder.columnSpec);
            return columnSpecBuilder;
        }

        public ColumnSpecBuilder addColumnSpec(ColumnType arg0, String arg1) {
            var columnSpecBuilder = new ColumnSpecBuilder(arg0, arg1);
            columnSpecs.add(columnSpecBuilder.columnSpec);
            return columnSpecBuilder;
        }

        public ColumnSpecBuilder addColumnSpec(ColumnType arg) {
            var columnSpecBuilder = new ColumnSpecBuilder(arg);
            columnSpecs.add(columnSpecBuilder.columnSpec);
            return columnSpecBuilder;
        }

        public Builder addColumnSpecs(ColumnSpec... arg) {
            Stream.of(arg).forEach(this::addColumnSpec);
            return this;
        }

        public Builder addColumnSpecs(ColumnType... arg) {
            Stream.of(arg).forEach(this::addColumnSpec);
            return this;
        }

        public SimpleTable build() {
            table.setColumnSpecs(columnSpecs.toArray(ColumnSpec[]::new));
            return table;
        }

        public class ColumnSpecBuilder {
            private final ColumnSpec columnSpec;

            public ColumnSpecBuilder(ColumnSpec columnSpec) {
                this.columnSpec = columnSpec;
            }

            public ColumnSpecBuilder(ColumnType columnType, String headerText, int width) {
                this.columnSpec = new ColumnSpec(columnType, headerText, width);
            }

            public ColumnSpecBuilder(ColumnType columnType, String headerText) {
                this.columnSpec = new ColumnSpec(columnType, headerText);
            }

            public ColumnSpecBuilder(ColumnType columnType) {
                this.columnSpec = new ColumnSpec(columnType, null);
            }

            public ColumnSpecBuilder headerText(String arg) {
                columnSpec.setHeaderText(arg);
                return this;
            }

            public ColumnSpecBuilder width(int arg) {
                columnSpec.setWidth(arg);
                return this;
            }

            public ColumnSpecBuilder resizable(boolean arg) {
                columnSpec.setResizable(arg);
                return this;
            }

            public ColumnSpecBuilder sortable(boolean arg) {
                columnSpec.setSortable(arg);
                return this;
            }

            public ColumnSpecBuilder headerFormat(CellFormat arg) {
                columnSpec.setHeaderFormat(arg);
                return this;
            }

            public ColumnSpecBuilder cellFormat(CellFormat arg) {
                columnSpec.setCellFormat(arg);
                return this;
            }

            public ColumnSpecBuilder extraData(Object arg) {
                columnSpec.setExtraData(arg);
                return this;
            }

            public Builder end() {
                return Builder.this;
            }
        }
    }
}
