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
            if (data instanceof ComboBoxModel) return (ComboBoxModel<?>) data;
            if (data instanceof Collection) return new SimpleComboBoxModel<>((Collection<?>) data);
            if (data.getClass().isArray()) return new SimpleComboBoxModel<>((Object[]) data);
            return new SimpleComboBoxModel<>(Collections.singleton(data));
        }
    };

    public static final ColumnType BUTTON = new ColumnType() {
        @Override
        public TableCellRenderer getRenderer(Object configObject) {
            ButtonTableCellRenderer renderer;

            if ((configObject == null) || (configObject instanceof TableCellActionListener)) {
                renderer = new ButtonTableCellRenderer();
            } else if (configObject instanceof ButtonModel) {
                ButtonModel buttonModel = (ButtonModel) configObject;
                renderer = new ButtonTableCellRenderer(buttonModel.getText(), buttonModel.getIcon());
            } else if (configObject instanceof Icon) {
                renderer = new ButtonTableCellRenderer((Icon) configObject);
            } else {
                renderer = new ButtonTableCellRenderer(configObject.toString());
            }

            return renderer;
        }

        @Override
        public TableCellEditor getEditor(Object configObject) {
            ButtonTableCellEditor editor;

            if (configObject == null) {
                editor = new ButtonTableCellEditor(null);
            } else if (configObject instanceof ButtonModel) {
                ButtonModel buttonModel = (ButtonModel) configObject;
                editor = new ButtonTableCellEditor(buttonModel.getText(), buttonModel.getIcon(), buttonModel.getActionListener());
            } else if (configObject instanceof TableCellActionListener) {
                editor = new ButtonTableCellEditor((TableCellActionListener) configObject);
            } else if (configObject instanceof Icon) {
                editor = new ButtonTableCellEditor((Icon) configObject, null);
            } else {
                editor = new ButtonTableCellEditor(configObject.toString(), null);
            }

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

            if (configObject == null) {
                editor = new HyperLinkTableCellEditor(null);
            } else if (configObject instanceof ButtonModel) {
                ButtonModel buttonModel = (ButtonModel) configObject;
                editor = new HyperLinkTableCellEditor(buttonModel.getText(), buttonModel.getActionListener());
            } else if (configObject instanceof TableCellActionListener) {
                editor = new HyperLinkTableCellEditor((TableCellActionListener) configObject);
            } else {
                editor = new HyperLinkTableCellEditor(configObject.toString(), null);
            }

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

            if (configObject instanceof ProgressModel) {
                ProgressModel progressModel = (ProgressModel) configObject;
                renderer = new ProgressTableCellRenderer(
                        progressModel.getRange().getMinimum(),
                        progressModel.getRange().getMaximum(),
                        progressModel.isStringPainted(),
                        progressModel.getNumberFormat());
            } else if (configObject instanceof Range) {
                Range range = (Range) configObject;
                renderer = new ProgressTableCellRenderer(range.getMinimum(), range.getMaximum());
            } else {
                renderer = new ProgressTableCellRenderer();
            }

            return renderer;
        }

        @Override
        public TableCellEditor getEditor(Object configObject) {
            TableCellEditor editor;

            if (configObject instanceof ProgressModel) {
                ProgressModel progressModel = (ProgressModel) configObject;
                editor = new RangeTableCellEditor(
                        progressModel.getRange().getMinimum(),
                        progressModel.getRange().getMaximum());
            } else if (configObject instanceof Range) {
                Range range = (Range) configObject;
                editor = new RangeTableCellEditor(range.getMinimum(), range.getMaximum());
            } else {
                editor = new RangeTableCellEditor();
            }

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
