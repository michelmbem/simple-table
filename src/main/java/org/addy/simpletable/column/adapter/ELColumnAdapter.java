package org.addy.simpletable.column.adapter;

import de.odysseus.el.ExpressionFactoryImpl;
import de.odysseus.el.util.SimpleContext;

import javax.el.ExpressionFactory;
import javax.el.ValueExpression;

public class ELColumnAdapter extends AssociativeColumnAdapter {
    private final ExpressionFactory factory = new ExpressionFactoryImpl();
    private final SimpleContext context = new SimpleContext();
    private ValueExpression[] columnExpressions;
    private ValueExpression currentItemExpression = null;

    public ELColumnAdapter(String... expressions) {
        super(expressions);
        createColumnExpressions();
    }

    @Override
    public void setColumnNames(String[] columnNames) {
        super.setColumnNames(columnNames);
        createColumnExpressions();
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

    private void createColumnExpressions() {
        columnExpressions = new ValueExpression[this.columnNames.length];

        for (int i = 0; i < columnNames.length; i++) {
            String expression = String.format("${%s}", columnNames[i]);
            columnExpressions[i] = factory.createValueExpression(context, expression, Object.class);
        }
    }

    private void bindItemToContext(Object item) {
        if (currentItemExpression == null)
            currentItemExpression = factory.createValueExpression(context, "${$}", item.getClass());

        currentItemExpression.setValue(context, item);
    }
}
