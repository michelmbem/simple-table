package org.addy.simpletable.column.adapter;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultSetColumnAdapter extends AssociativeColumnAdapter {
    private final FieldExtractor fieldExtractor;

    public ResultSetColumnAdapter(boolean extractByName, String... columnNames) {
        super(columnNames);
        fieldExtractor = extractByName ? new ByNameFieldExtractor() : new ByIndexFieldExtractor();
    }

    public ResultSetColumnAdapter(String... columnNames) {
        this(true, columnNames);
    }

    public ResultSetColumnAdapter() {
        this(false);
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
            rs.updateObject(columnIndex, value);
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
