package org.addy.simpletable.row.adapter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class ResultSetRowAdapter implements RowAdapter {
    @Override
    public int getRowCount(Object itemSource) {
        var resultSet = (ResultSet) itemSource;

        try {
            resultSet.last();
            return resultSet.getRow();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public Object getRowAt(Object itemSource, int rowIndex) {
        var resultSet = (ResultSet) itemSource;

        try {
            resultSet.absolute(rowIndex + 1);
            return resultSet;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void setRowAt(Object itemSource, int rowIndex, Object item) {
        var resultSet = (ResultSet) itemSource;

        try {
            updateResultSet(resultSet, item);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void addRow(Object itemSource, Object item) {
        var resultSet = (ResultSet) itemSource;

        try {
            resultSet.moveToInsertRow();
            updateResultSet(resultSet, item);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void removeRowAt(Object itemSource, int rowIndex) {
        var resultSet = (ResultSet) itemSource;

        try {
            resultSet.absolute(rowIndex + 1);
            resultSet.deleteRow();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void removeAllRows(Object itemSource) {
        var resultSet = (ResultSet) itemSource;

        try {
            resultSet.first();

            while (!resultSet.isAfterLast()) {
                resultSet.deleteRow();
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private void updateResultSet(ResultSet resultSet, Object item) throws SQLException {
        if (item instanceof Map<?, ?> map) {
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                resultSet.updateObject(entry.getKey().toString(), entry.getValue());
            }
        } else if (item instanceof List<?> list) {
            int i = 1;
            for (Object value : list) {
                resultSet.updateObject(i++, value);
            }
        } else if (item!= null && item.getClass().isArray()) {
            int length = java.lang.reflect.Array.getLength(item);
            for (int i = 0; i < length; ++i) {
                resultSet.updateObject(i + 1, java.lang.reflect.Array.get(item, i));
            }
        } else {
            throw new IllegalArgumentException("Cannot update a RecordSet with the given item");
        }

        resultSet.updateRow();
    }
}
