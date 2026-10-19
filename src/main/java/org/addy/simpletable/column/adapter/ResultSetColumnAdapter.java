package org.addy.simpletable.column.adapter;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *  A ColumnAdapter that assumes that the given item is a JDBC ResultSet.
 *  It can map columns by position or by name based on the constructor used.
 *  If the default constructor is used, columns will be mapped by position.
 *  If column names are supplied to the constructor, columns will be mapped by name.
 *
 * @author Michel Mbem
 */
public class ResultSetColumnAdapter extends AssociativeColumnAdapter {
    private final FieldExtractor fieldExtractor;

    public ResultSetColumnAdapter() {
        super();
        fieldExtractor = new ByIndexFieldExtractor();
    }

    public ResultSetColumnAdapter(String... columnNames) {
        super(columnNames);
        fieldExtractor = new ByNameFieldExtractor() ;
    }

    @Override
    public void setColumnNames(String[] columnNames) {
        // column names are only set by the constructor
    }

    @Override
    public Object getValueAt(Object item, int columnIndex) {
        var rs = (ResultSet) item;

        try {
            return fieldExtractor.getValue(rs, columnIndex);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void setValueAt(Object item, int columnIndex, Object value) {
        var rs = (ResultSet) item;

        try {
            fieldExtractor.setValue(rs, columnIndex, value);
            rs.updateRow();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private interface FieldExtractor {
        Object getValue(ResultSet rs, int columnIndex) throws SQLException;
        void setValue(ResultSet rs, int columnIndex, Object value)throws SQLException;
    }

    private static class ByIndexFieldExtractor implements FieldExtractor {
        @Override
        public Object getValue(ResultSet rs, int columnIndex) throws SQLException {
            return rs.getObject(columnIndex + 1);
        }

        @Override
        public void setValue(ResultSet rs, int columnIndex, Object value) throws SQLException {
            rs.updateObject(columnIndex + 1, value);
        }
    }

    private class ByNameFieldExtractor implements FieldExtractor {
        @Override
        public Object getValue(ResultSet rs, int columnIndex) throws SQLException {
            return rs.getObject(columnNames[columnIndex]);
        }

        @Override
        public void setValue(ResultSet rs, int columnIndex, Object value) throws SQLException {
            rs.updateObject(columnNames[columnIndex], value);
        }
    }
}
