package org.addy.simpletable.column.spec;

import org.addy.simpletable.column.editor.ButtonTableCellEditor;
import org.addy.simpletable.column.editor.DateTimeTableCellEditor;
import org.addy.simpletable.column.editor.HyperLinkTableCellEditor;
import org.addy.simpletable.column.editor.RangeTableCellEditor;
import org.addy.simpletable.column.renderer.ButtonTableCellRenderer;
import org.addy.simpletable.column.renderer.CheckBoxTableCellRenderer;
import org.addy.simpletable.column.renderer.FormattedTableCellRenderer;
import org.addy.simpletable.column.renderer.HyperLinkTableCellRenderer;
import org.addy.simpletable.column.renderer.ImageTableCellRenderer;
import org.addy.simpletable.column.renderer.LineNumberTableCellRenderer;
import org.addy.simpletable.column.renderer.ProgressTableCellRenderer;
import org.addy.simpletable.event.TableCellActionListener;
import org.addy.simpletable.model.ButtonModel;
import org.addy.simpletable.model.ProgressModel;
import org.addy.simpletable.model.Range;
import org.addy.simpletable.util.DateFormats;
import org.addy.simpletable.util.NumberFormats;
import org.addy.swing.SimpleComboBoxModel;
import org.addy.swing.SizeMode;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public abstract class ColumnType {
    public TableCellRenderer getRenderer(Object configObject) {
        return null;
    }

    public TableCellEditor getEditor(Object configObject) {
        return null;
    }

    public static final ColumnType DEFAULT = new ColumnType() {};

    public static final ColumnType TEXT = new ColumnType() {
        @Override
        public TableCellRenderer getRenderer(Object configObject) {
            return new DefaultTableCellRenderer();
        }

        @Override
        public TableCellEditor getEditor(Object configObject) {
            return new DefaultCellEditor(new JTextField());
        }
    };

    public static final ColumnType NUMBER = new ColumnType() {
        @Override
        public TableCellRenderer getRenderer(Object configObject) {
            return new FormattedTableCellRenderer(NumberFormats.of(configObject));
        }

        @Override
        public TableCellEditor getEditor(Object configObject) {
            return new DefaultCellEditor(new JFormattedTextField(NumberFormats.of(configObject)));
        }
    };

    public static final ColumnType DATETIME = new ColumnType() {
        @Override
        public TableCellRenderer getRenderer(Object configObject) {
            return new FormattedTableCellRenderer(DateFormats.of(configObject), Date.class);
        }

        @Override
        public TableCellEditor getEditor(Object configObject) {
            return configObject != null
                    ? new DateTimeTableCellEditor(configObject.toString())
                    : new DateTimeTableCellEditor();
        }
    };

    public static final ColumnType CHECKBOX = new ColumnType() {
        @Override
        public TableCellRenderer getRenderer(Object configObject) {
            return new CheckBoxTableCellRenderer();
        }

        @Override
        public TableCellEditor getEditor(Object configObject) {
            JCheckBox checkBox = new JCheckBox();
            checkBox.setBorderPainted(true);
            return new DefaultCellEditor(checkBox);
        }
    };

    public static final ColumnType COMBOBOX = new ColumnType() {
        @Override
        public TableCellRenderer getRenderer(Object configObject) {
            return new DefaultTableCellRenderer();
        }

        @Override
        public TableCellEditor getEditor(Object configObject) {
            ComboBoxModel<?> comboBoxModel = getComboBoxModel(configObject);
            JComboBox<?> comboBox = new JComboBox<>(comboBoxModel);
            return new DefaultCellEditor(comboBox);
        }

        private ComboBoxModel<?> getComboBoxModel(Object data) {
            if (data == null) return new SimpleComboBoxModel<>();
            if (data instanceof ComboBoxModel<?> cbm) return cbm;
            if (data instanceof Collection<?> col) return new SimpleComboBoxModel<>(col);
            if (data.getClass().isArray()) return new SimpleComboBoxModel<>((Object[]) data);
            return new SimpleComboBoxModel<>(Collections.singleton(data));
        }
    };

    public static final ColumnType BUTTON = new ColumnType() {
        @Override
        public TableCellRenderer getRenderer(Object configObject) {
            ButtonTableCellRenderer renderer;

            if ((configObject == null) || (configObject instanceof TableCellActionListener))
                renderer = new ButtonTableCellRenderer();
            else if (configObject instanceof ButtonModel bm)
                renderer = new ButtonTableCellRenderer(bm.getText(), bm.getIcon());
            else if (configObject instanceof Icon icon)
                renderer = new ButtonTableCellRenderer(icon);
            else
                renderer = new ButtonTableCellRenderer(configObject.toString());

            return renderer;
        }

        @Override
        public TableCellEditor getEditor(Object configObject) {
            ButtonTableCellEditor editor;

            if (configObject == null)
                editor = new ButtonTableCellEditor(null);
            else if (configObject instanceof ButtonModel bm)
                editor = new ButtonTableCellEditor(bm.getText(), bm.getIcon(), bm.getActionListener());
            else if (configObject instanceof TableCellActionListener tcal)
                editor = new ButtonTableCellEditor(tcal);
            else if (configObject instanceof Icon icon)
                editor = new ButtonTableCellEditor(icon, null);
            else
                editor = new ButtonTableCellEditor(configObject.toString(), null);

            return editor;
        }
    };

    public static final ColumnType HYPERLINK = new ColumnType() {
        @Override
        public TableCellRenderer getRenderer(Object configObject) {
            return new HyperLinkTableCellRenderer();
        }

        @Override
        public TableCellEditor getEditor(Object configObject) {
            HyperLinkTableCellEditor editor;

            if (configObject == null)
                editor = new HyperLinkTableCellEditor(null);
            else if (configObject instanceof ButtonModel bm)
                editor = new HyperLinkTableCellEditor(bm.getText(), bm.getActionListener());
            else if (configObject instanceof TableCellActionListener tcal)
                editor = new HyperLinkTableCellEditor(tcal);
            else
                editor = new HyperLinkTableCellEditor(configObject.toString(), null);

            return editor;
        }
    };

    public static final ColumnType IMAGE = new ColumnType() {
        @Override
        public TableCellRenderer getRenderer(Object configObject) {
            return configObject != null
                    ? new ImageTableCellRenderer((SizeMode) configObject)
                    : new ImageTableCellRenderer();
        }
    };

    public static final ColumnType PROGRESS = new ColumnType() {
        @Override
        public TableCellRenderer getRenderer(Object configObject) {
            TableCellRenderer renderer;

            if (configObject instanceof ProgressModel pm)
                renderer = new ProgressTableCellRenderer(
                    pm.getRange().getMinimum(),
                    pm.getRange().getMaximum(),
                    pm.isStringPainted(),
                    pm.getNumberFormat()
                );
            else if (configObject instanceof Range range)
                renderer = new ProgressTableCellRenderer(range.getMinimum(), range.getMaximum());
            else
                renderer = new ProgressTableCellRenderer();

            return renderer;
        }

        @Override
        public TableCellEditor getEditor(Object configObject) {
            TableCellEditor editor;

            if (configObject instanceof ProgressModel pm)
                editor = new RangeTableCellEditor(pm.getRange().getMinimum(), pm.getRange().getMaximum());
            else if (configObject instanceof Range range)
                editor = new RangeTableCellEditor(range.getMinimum(), range.getMaximum());
            else
                editor = new RangeTableCellEditor();

            return editor;
        }
    };

    public static final ColumnType LINENUMBER = new ColumnType() {
        @Override
        public TableCellRenderer getRenderer(Object configObject) {
            return new LineNumberTableCellRenderer();
        }
    };
}
