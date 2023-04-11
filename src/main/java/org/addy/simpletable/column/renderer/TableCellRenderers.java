package org.addy.simpletable.column.renderer;

import org.addy.simpletable.column.definition.ColumnType;
import org.addy.simpletable.event.TableCellActionListener;
import org.addy.simpletable.util.ButtonModel;
import org.addy.simpletable.util.DateFormats;
import org.addy.simpletable.util.NumberFormats;
import org.addy.simpletable.util.Range;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

public final class TableCellRenderers {
    private TableCellRenderers() {}

    public static TableCellRenderer from(ColumnType columnType, int horizontalAlignment, int verticalAlignment, Object extraData) {
        switch (columnType) {
            case NUMBER: {
                NumberTableCellRenderer numberTableCellRenderer = extraData != null
                        ? new NumberTableCellRenderer(NumberFormats.of(extraData.toString()))
                        : new NumberTableCellRenderer();
                applyAlignment(numberTableCellRenderer, horizontalAlignment, verticalAlignment);
                return numberTableCellRenderer;
            }
            case DATETIME: {
                DateTimeTableCellRenderer dateTimeTableCellRenderer = extraData != null
                        ? new DateTimeTableCellRenderer(DateFormats.of(extraData.toString()))
                        : new DateTimeTableCellRenderer();
                applyAlignment(dateTimeTableCellRenderer, horizontalAlignment, verticalAlignment);
                return dateTimeTableCellRenderer;
            }
            case CHECKBOX:
                return new CheckBoxTableCellRenderer();
            case BUTTON: {
                ButtonTableCellRenderer buttonTableCellRenderer;

                if ((extraData == null) || (extraData instanceof TableCellActionListener)) {
                    buttonTableCellRenderer = new ButtonTableCellRenderer();
                } else if (extraData instanceof ButtonModel) {
                    ButtonModel buttonModel = (ButtonModel) extraData;
                    buttonTableCellRenderer = new ButtonTableCellRenderer(buttonModel.getText(), buttonModel.getIcon());
                } else if (extraData instanceof Icon) {
                    buttonTableCellRenderer = new ButtonTableCellRenderer((Icon) extraData);
                } else {
                    buttonTableCellRenderer = new ButtonTableCellRenderer(extraData.toString());
                }

                return buttonTableCellRenderer;
            }
            case HYPERLINK: {
                HyperLinkTableCellRenderer hyperLinkTableCellRenderer = new HyperLinkTableCellRenderer();
                applyAlignment(hyperLinkTableCellRenderer, horizontalAlignment, verticalAlignment);
                return hyperLinkTableCellRenderer;
            }
            case ICON: {
                IconTableCellRenderer iconTableCellRenderer = new IconTableCellRenderer();
                applyAlignment(iconTableCellRenderer, horizontalAlignment, verticalAlignment);
                return iconTableCellRenderer;
            }
            case PROGRESS: {
                ProgressTableCellRenderer progressTableCellRenderer;

                if (extraData == null) {
                    progressTableCellRenderer = new ProgressTableCellRenderer();
                } else {
                    Range range = (Range) extraData;
                    progressTableCellRenderer = new ProgressTableCellRenderer(range.getMinimum(), range.getMaximum());
                }

                return progressTableCellRenderer;
            }
            default: {
                DefaultTableCellRenderer defaultTableCellRenderer = new DefaultTableCellRenderer();
                applyAlignment(defaultTableCellRenderer, horizontalAlignment, verticalAlignment);
                return  defaultTableCellRenderer;
            }
        }
    }

    private static void applyAlignment(JLabel label, int horizontalAlignment, int verticalAlignment) {
        if (horizontalAlignment >= 0)
            label.setHorizontalAlignment(horizontalAlignment);

        if (verticalAlignment >= 0)
            label.setVerticalAlignment(verticalAlignment);
    }
}
