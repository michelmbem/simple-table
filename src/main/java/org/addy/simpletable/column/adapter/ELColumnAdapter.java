package org.addy.simpletable.column.adapter;

import de.odysseus.el.ExpressionFactoryImpl;
import de.odysseus.el.util.SimpleContext;
import org.addy.simpletable.util.ELEngine;

import javax.el.ExpressionFactory;
import javax.el.ValueExpression;

/**
 *  A ColumnAdapter that uses expression language (EL) to bind column values.
 *  It assumes that column names are valid EL expressions (not wrapped in "${...}" or "#{...}").
 *  The dollar-sign ($) is used to reference the current row item.
 *
 * @author Mike
 */
public class ELColumnAdapter extends AssociativeColumnAdapter {
    private final ExpressionFactory factory = new ExpressionFactoryImpl();
    private final SimpleContext context = new SimpleContext();
    private ValueExpression[] columnExpressions;
    private ValueExpression currentItemExpression = null;

    public ELColumnAdapter(String... expressions) {
        super(expressions);
        createColumnExpressions();
        ELEngine.initializeContext(context, factory);
    }

    @Override
    public void setColumnNames(String[] columnNames) {
        super.setColumnNames(columnNames);
        createColumnExpressions();
        ELEngine.initializeContext(context, factory);
    }

    @Override
    public boolean isCellEditable(int columnIndex) {
        return !columnExpressions[columnIndex].isReadOnly(context);
    }

    @Override
    public Object getValueAt(Object item, int columnIndex) {
        bindItemToContext(item);
        return columnExpressions[columnIndex].getValue(context);
    }

    @Override
    public void setValueAt(Object item, int columnIndex, Object value) {
        bindItemToContext(item);
        columnExpressions[columnIndex].setValue(context, value);
    }

    /**
     * Creates EL expressions from the given column names.
     */
    private void createColumnExpressions() {
        columnExpressions = new ValueExpression[this.columnNames.length];

        for (int i = 0; i < columnNames.length; i++) {
            String expression = String.format("#{%s}", columnNames[i]);
            columnExpressions[i] = factory.createValueExpression(context, expression, Object.class);
        }
    }

    /**
     * Binds the given row item to an automatic variable named $,
     * which is referenced by all column expressions.
     *
     * @param item The value associated with the current table row.
     */
    private void bindItemToContext(Object item) {
        if (currentItemExpression == null)
            currentItemExpression = factory.createValueExpression(context, "#{$}", item.getClass());

        currentItemExpression.setValue(context, item);
    }
}
