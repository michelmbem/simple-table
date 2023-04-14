package org.addy.simpletable.column.renderer;

import org.addy.simpletable.column.definition.CellFormat;
import org.addy.simpletable.column.definition.ColumnType;
import org.addy.simpletable.event.TableCellActionListener;
import org.addy.simpletable.model.ButtonModel;
import org.addy.simpletable.model.ProgressModel;
import org.addy.simpletable.model.Range;
import org.addy.simpletable.util.DateFormats;
import org.addy.simpletable.util.NumberFormats;
import org.addy.swing.SizeMode;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public final class TableCellRenderers {
    private TableCellRenderers() {}

    public static TableCellRenderer from(ColumnType columnType, CellFormat cellFormat, Object extraData) {
        TableCellRenderer renderer;

        switch (columnType) {
            case CUSTOM:
                return null;
            case NUMBER:
                renderer = new NumberTableCellRenderer(NumberFormats.of(extraData));
                break;
            case DATETIME:
                renderer = new DateTimeTableCellRenderer(DateFormats.of(extraData));
                break;
            case CHECKBOX:
                renderer = new CheckBoxTableCellRenderer();
                break;
            case BUTTON:
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
                break;
            case HYPERLINK:
                renderer = new HyperLinkTableCellRenderer();
                break;
            case IMAGE:
                renderer = extraData != null
                        ? new ImageTableCellRenderer((SizeMode) extraData)
                        : new ImageTableCellRenderer();
                break;
            case PROGRESS: {
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
                break;
            }
            case LINENUMBER:
                renderer = new LineNumberTableCellRenderer();
                break;
            default:
                renderer =  new DefaultTableCellRenderer();
                break;
        }

        cellFormat.applyTo((Component) renderer);

        return renderer;
    }
}
