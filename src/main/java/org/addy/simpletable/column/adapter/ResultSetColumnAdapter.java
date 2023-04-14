package org.addy.simpletable.column.adapter;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultSetColumnAdapter extends AssociativeColumnAdapter {
    public ResultSetColumnAdapter(String... columnNames) {
        super(columnNames);
    }

    @Override
    public Object getValueAt(Object item, int columnIndex) {
        ResultSet resultSet = (ResultSet) item;

        try {
            return resultSet.getObject(columnNames[columnIndex]);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setValueAt(Object item, int columnIndex, Object value) {
        ResultSet resultSet = (ResultSet) item;

        try {
            resultSet.updateObject(columnNames[columnIndex], value);
            resultSet.updateRow();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
