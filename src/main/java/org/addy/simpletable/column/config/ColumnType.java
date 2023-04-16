package org.addy.simpletable.column.config;

import org.addy.simpletable.column.editor.ButtonTableCellEditor;
import org.addy.simpletable.column.editor.DateTimeTableCellEditor;
import org.addy.simpletable.column.editor.HyperLinkTableCellEditor;
import org.addy.simpletable.column.renderer.*;
import org.addy.simpletable.event.TableCellActionListener;
import org.addy.simpletable.model.ButtonModel;
import org.addy.simpletable.model.ProgressModel;
import org.addy.simpletable.model.Range;
import org.addy.simpletable.util.DateFormats;
import org.addy.simpletable.util.NumberFormats;
import org.addy.swing.SizeMode;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.Collection;
import java.util.Vector;

public abstract class ColumnType {
    public TableCellRenderer getCellRenderer(CellFormat cellFormat, Object extraData) {
        return applyCellFormat(new DefaultTableCellRenderer(), cellFormat);
    }

    public TableCellEditor getCellEditor(CellFormat cellFormat, Object extraData) {
        return applyCellFormat(new DefaultCellEditor(new JTextField()), cellFormat);
    }

    public static final ColumnType DEFAULT = new ColumnType() {};

    public static final ColumnType NUMBER = new ColumnType() {
        @Override
        public TableCellRenderer getCellRenderer(CellFormat cellFormat, Object extraData) {
            return applyCellFormat(new NumberTableCellRenderer(NumberFormats.of(extraData)), cellFormat);
        }
    };

    public static final ColumnType DATETIME = new ColumnType() {
        @Override
        public TableCellRenderer getCellRenderer(CellFormat cellFormat, Object extraData) {
            return applyCellFormat(new DateTimeTableCellRenderer(DateFormats.of(extraData)), cellFormat);
        }

        @Override
        public TableCellEditor getCellEditor(CellFormat cellFormat, Object extraData) {
            return new DateTimeTableCellEditor();
        }
    };

    public static final ColumnType CHECKBOX = new ColumnType() {
        @Override
        public TableCellRenderer getCellRenderer(CellFormat cellFormat, Object extraData) {
            return applyCellFormat(new CheckBoxTableCellRenderer(), cellFormat);
        }

        @Override
        public TableCellEditor getCellEditor(CellFormat cellFormat, Object extraData) {
            return applyCellFormat(new DefaultCellEditor(new JCheckBox()), cellFormat);
        }
    };

    public static final ColumnType COMBOBOX = new ColumnType() {
        @Override
        public TableCellEditor getCellEditor(CellFormat cellFormat, Object extraData) {
            ComboBoxModel<?> comboBoxModel = getComboBoxModel(extraData);
            JComboBox<?> comboBox = new JComboBox<>(comboBoxModel);
            return applyCellFormat(new DefaultCellEditor(comboBox), cellFormat);
        }

        private ComboBoxModel<?> getComboBoxModel(Object data) {
            if (data == null)
                return new DefaultComboBoxModel<>();

            Class<?> dataType = data.getClass();

            if (ComboBoxModel.class.isAssignableFrom(dataType))
                return (ComboBoxModel<?>) data;

            if (dataType.isArray())
                return new DefaultComboBoxModel<>((Object[]) data);

            if (Collection.class.isAssignableFrom(dataType)) {
                Vector<?> vector = new Vector<>((Collection<?>) data);
                return new DefaultComboBoxModel<>(vector);
            }

            throw new IllegalArgumentException("Could not create a combobox model with the given data");
        }
    };

    public static final ColumnType BUTTON = new ColumnType() {
        @Override
        public TableCellRenderer getCellRenderer(CellFormat cellFormat, Object extraData) {
            TableCellRenderer renderer;

            if ((extraData == null) || (extraData instanceof TableCellActionListener)) {
                renderer = new ButtonTableCellRenderer();
            } else if (extraData instanceof ButtonModel) {
                ButtonModel buttonModel = (ButtonModel) extraData;
                renderer = new ButtonTableCellRenderer(buttonModel.getText(), buttonModel.getIcon());
            } else if (extraData instanceof Icon) {
                renderer = new ButtonTableCellRenderer((Icon) extraData);
            } else {
                renderer = new ButtonTableCellRenderer(extraData.toString());
            }

            return applyCellFormat(renderer, cellFormat);
        }

        @Override
        public TableCellEditor getCellEditor(CellFormat cellFormat, Object extraData) {
            ButtonTableCellEditor buttonTableCellEditor;

            if (extraData == null) {
                buttonTableCellEditor = new ButtonTableCellEditor(null);
            } else if (extraData instanceof ButtonModel) {
                ButtonModel buttonModel = (ButtonModel) extraData;
                buttonTableCellEditor = new ButtonTableCellEditor(buttonModel.getText(), buttonModel.getIcon(), buttonModel.getActionListener());
            } else if (extraData instanceof TableCellActionListener) {
                buttonTableCellEditor = new ButtonTableCellEditor((TableCellActionListener) extraData);
            } else if (extraData instanceof Icon) {
                buttonTableCellEditor = new ButtonTableCellEditor((Icon) extraData, null);
            } else {
                buttonTableCellEditor = new ButtonTableCellEditor(extraData.toString(), null);
            }

            return buttonTableCellEditor;
        }
    };

    public static final ColumnType HYPERLINK = new ColumnType() {
        @Override
        public TableCellRenderer getCellRenderer(CellFormat cellFormat, Object extraData) {
            return applyCellFormat(new HyperLinkTableCellRenderer(), cellFormat);
        }

        @Override
        public TableCellEditor getCellEditor(CellFormat cellFormat, Object extraData) {
            HyperLinkTableCellEditor hyperLinkTableCellEditor;

            if (extraData == null) {
                hyperLinkTableCellEditor = new HyperLinkTableCellEditor(null);
            } else if (extraData instanceof ButtonModel) {
                ButtonModel buttonModel = (ButtonModel) extraData;
                hyperLinkTableCellEditor = new HyperLinkTableCellEditor(buttonModel.getText(), buttonModel.getActionListener());
            } else if (extraData instanceof TableCellActionListener) {
                hyperLinkTableCellEditor = new HyperLinkTableCellEditor((TableCellActionListener) extraData);
            } else {
                hyperLinkTableCellEditor = new HyperLinkTableCellEditor(extraData.toString(), null);
            }

            return hyperLinkTableCellEditor;
        }
    };

    public static final ColumnType IMAGE = new ColumnType() {
        @Override
        public TableCellRenderer getCellRenderer(CellFormat cellFormat, Object extraData) {
            TableCellRenderer renderer = extraData != null
                    ? new ImageTableCellRenderer((SizeMode) extraData)
                    : new ImageTableCellRenderer();

            return applyCellFormat(renderer, cellFormat);
        }

        @Override
        public TableCellEditor getCellEditor(CellFormat cellFormat, Object extraData) {
            return null;
        }
    };

    public static final ColumnType PROGRESS = new ColumnType() {
        @Override
        public TableCellRenderer getCellRenderer(CellFormat cellFormat, Object extraData) {
            TableCellRenderer renderer;

            if (extraData instanceof ProgressModel) {
                ProgressModel progressModel = (ProgressModel) extraData;
                renderer = new ProgressTableCellRenderer(
                        progressModel.getRange().getMinimum(),
                        progressModel.getRange().getMaximum(),
                        progressModel.isStringPainted(),
                        progressModel.getNumberFormat());
            } else if (extraData instanceof Range) {
                Range range = (Range) extraData;
                renderer = new ProgressTableCellRenderer(range.getMinimum(), range.getMaximum());
            } else {
                renderer = new ProgressTableCellRenderer();
            }

            return applyCellFormat(renderer, cellFormat);
        }
    };

    public static final ColumnType LINENUMBER = new ColumnType() {
        @Override
        public TableCellRenderer getCellRenderer(CellFormat cellFormat, Object extraData) {
            return applyCellFormat(new LineNumberTableCellRenderer(), cellFormat);
        }

        @Override
        public TableCellEditor getCellEditor(CellFormat cellFormat, Object extraData) {
            return null;
        }
    };

    protected static TableCellRenderer applyCellFormat(TableCellRenderer renderer, CellFormat cellFormat) {
        cellFormat.applyTo((Component) renderer);
        return renderer;
    }

    protected static DefaultCellEditor applyCellFormat(DefaultCellEditor editor, CellFormat cellFormat) {
        cellFormat.applyTo(editor.getComponent());
        return editor;
    }
}
