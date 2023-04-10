package org.addy.simpletable.row.adapter;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultSetRowAdapter extends BasicRowAdapter {
    @Override
    public int getRowCount(Object itemSource) {
        ResultSet resultSet = (ResultSet) itemSource;

        try {
            resultSet.last();
            return resultSet.getRow();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object getRowAt(Object itemSource, int rowIndex) {
        ResultSet resultSet = (ResultSet) itemSource;

        try {
            resultSet.absolute(rowIndex);
            return resultSet;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
