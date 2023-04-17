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
import org.addy.swing.SimpleComboBoxModel;
import org.addy.swing.SizeMode;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.util.Collection;
import java.util.Collections;

public abstract class ColumnType {
    public TableCellRenderer getRenderer(Object configObject) {
        return new DefaultTableCellRenderer();
    }

    public TableCellEditor getEditor(Object configObject) {
        return new DefaultCellEditor(new JTextField());
    }

    public static final ColumnType DEFAULT = new ColumnType() {};

    public static final ColumnType NUMBER = new ColumnType() {
        @Override
        public TableCellRenderer getRenderer(Object configObject) {
            return new NumberTableCellRenderer(NumberFormats.of(configObject));
        }
    };

    public static final ColumnType DATETIME = new ColumnType() {
        @Override
        public TableCellRenderer getRenderer(Object configObject) {
            return new DateTimeTableCellRenderer(DateFormats.of(configObject));
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
        public TableCellEditor getEditor(Object configObject) {
            ComboBoxModel<?> comboBoxModel = getComboBoxModel(configObject);
            JComboBox<?> comboBox = new JComboBox<>(comboBoxModel);
            return new DefaultCellEditor(comboBox);
        }

        private ComboBoxModel<?> getComboBoxModel(Object data) {
            if (data == null)
                return new DefaultComboBoxModel<>();

            Class<?> dataType = data.getClass();

            if (ComboBoxModel.class.isAssignableFrom(dataType))
                return (ComboBoxModel<?>) data;

            if (dataType.isArray())
                return new SimpleComboBoxModel<>((Object[]) data);

            if (Collection.class.isAssignableFrom(dataType)) {
                return new SimpleComboBoxModel<>((Collection<?>) data);
            }

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

        @Override
        public TableCellEditor getEditor(Object configObject) {
            return null;
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
    };

    public static final ColumnType LINENUMBER = new ColumnType() {
        @Override
        public TableCellRenderer getRenderer(Object configObject) {
            return new LineNumberTableCellRenderer();
        }

        @Override
        public TableCellEditor getEditor(Object configObject) {
            return null;
        }
    };
}
