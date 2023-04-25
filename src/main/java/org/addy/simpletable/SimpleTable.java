package org.addy.simpletable;

import org.addy.simpletable.column.specs.ColumnSpecs;
import org.addy.swing.KnownColor;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Objects;

/**
 * An easily configurable JTable.<br>
 * Manages alternated row backgrounds as well as row highlighting on mouse hover and automatic sorting.<br>
 * Automatically generates cell renderers and editors for columns based on the supplied given column definitions.<br>
 *
 * @author Mike
 */
public class SimpleTable extends JTable {
    private Color alternateBackground;
    private Color rolloverBackground;
    private int rolloverRowIndex = -1;
    private ColumnSpecs[] columnSpecs = null;

    public SimpleTable() {
        super();
        setRowHeight(20);
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

    public ColumnSpecs[] getColumnSpecs() {
        return columnSpecs;
    }

    public void setColumnSpecs(ColumnSpecs... columnSpecs) {
        this.columnSpecs = Objects.requireNonNull(columnSpecs);
        applyColumnSpecs();
    }

    public ColumnSpecs getColumnSpecsAt(int index) {
        return columnSpecs != null && index < columnSpecs.length ? columnSpecs[index] : null;
    }

    public void generateColumnSpecs() {
        columnSpecs = new ColumnSpecs[getColumnCount()];
        TableModel model = getModel();

        if (model != null) {
            for (int i = 0; i < columnSpecs.length; ++i) {
                columnSpecs[i] = ColumnSpecs.of(model.getColumnClass(i));
            }

            applyColumnSpecs();
        }
    }

    public void applyColumnSpecs() {
        if (columnSpecs != null) {
            for (int i = 0; i < columnSpecs.length; ++i) {
                columnSpecs[i].applyTo(getColumnModel().getColumn(i), this, i);
            }
        }
    }

    @Override
    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
        JComponent component = (JComponent) super.prepareRenderer(renderer, row, column);
        ColumnSpecs colSpecs = getColumnSpecsAt(column);

        if (colSpecs != null)
            colSpecs.getCellFormat().applyTo(component, false);

        if (!component.getBackground().equals(getSelectionBackground())) {
            if (rolloverBackground != null && row == rolloverRowIndex) {
                component.setForeground(getForeground());
                component.setBackground(rolloverBackground);
            } else if (alternateBackground != null) {
                if (!(colSpecs == null || colSpecs.getCellFormat().getBackground() == null)) {
                    component.setBackground(colSpecs.getCellFormat().getBackground());
                } else {
                    component.setBackground(row % 2 == 1 ? alternateBackground : getBackground());
                }
            }
        }

        return component;
    }

    @Override
    public Component prepareEditor(TableCellEditor editor, int row, int column) {
        JComponent component = (JComponent) super.prepareEditor(editor, row, column);
        ColumnSpecs colSpecs = getColumnSpecsAt(column);

        if (colSpecs != null)
            colSpecs.getCellFormat().applyTo(component, true);

        return component;
    }

    protected void createRolloverListener() {
        RolloverListener rolloverListener = new RolloverListener();
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
}
