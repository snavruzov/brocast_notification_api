package com.brocast.api.notification.tools;

import com.dgtz.db.api.dao.db.DBUtils;
import com.dgtz.db.api.dao.db.JDBCUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * BroCast.
 * Copyright: Sardor Navruzov
 * 2013-2016.
 */
public class ApiShelf {


    public String getStreamerIP(Long idMedia) throws SQLException {
        String ip = null;
        Connection dbConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;

        try {
            dbConnection = JDBCUtil.getDBConnection();
            preparedStatement = dbConnection.prepareStatement(
                    "SELECT ip WHERE id_media = ? AND action_type in (20, 21) ");

            preparedStatement.setLong(1, idMedia);
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                ip = rs.getString("ip");
            }

        } catch (Exception e) {
            DBUtils.dbRollback(dbConnection);
            e.printStackTrace();
        } finally {
            DBUtils.closeConnections(preparedStatement, dbConnection, rs);
        }

        return ip;
    }
}
