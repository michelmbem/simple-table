package org.addy.simpletable.row.adapter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class ResultSetRowAdapter extends BasicRowAdapter {
    @Override
    public int getRowCount(Object itemSource) {
        ResultSet resultSet = (ResultSet) itemSource;

        try {
            resultSet.last();
            return resultSet.getRow();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public Object getRowAt(Object itemSource, int rowIndex) {
        ResultSet resultSet = (ResultSet) itemSource;

        try {
            resultSet.absolute(rowIndex + 1);
            return resultSet;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void setRowAt(Object itemSource, int rowIndex, Object item) {
        ResultSet resultSet = (ResultSet) itemSource;

        try {
            upadteResultSet(resultSet, item);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void addRow(Object itemSource, Object item) {
        ResultSet resultSet = (ResultSet) itemSource;

        try {
            resultSet.moveToInsertRow();
            upadteResultSet(resultSet, item);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void removeRowAt(Object itemSource, int rowIndex) {
        ResultSet resultSet = (ResultSet) itemSource;

        try {
            resultSet.absolute(rowIndex + 1);
            resultSet.deleteRow();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void removeAllRows(Object itemSource) {
        ResultSet resultSet = (ResultSet) itemSource;

        try {
            resultSet.first();

            while (!resultSet.isAfterLast()) {
                resultSet.deleteRow();
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private void upadteResultSet(ResultSet resultSet, Object item) throws SQLException {
        if (item instanceof Map) {
            Object[] array = (Object[]) item;

            for (int i = 0; i < array.length; ++i) {
                resultSet.updateObject(i + 1, array[i]);
            }
        } if (item instanceof List) {
            List<?> list = (List<?>) item;

            for (int i = 0; i < list.size(); ++i) {
                resultSet.updateObject(i + 1, list.get(i));
            }
        } else if (item!= null && item.getClass().isArray()) {
            Map<String, ?> map = (Map<String, ?>) item;

            for (String key : map.keySet()) {
                resultSet.updateObject(key, map.get(key));
            }
        } else {
            throw new IllegalArgumentException("Cannot update a RecordSet with the given item");
        }

        resultSet.updateRow();
    }
}
