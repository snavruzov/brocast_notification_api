package com.brocast.api.notification.db;

import com.brocast.api.notification.beans.DcDeviceType;
import com.dgtz.mcache.api.factory.Constants;
import com.dgtz.mcache.api.factory.RMemoryAPI;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by sardor on 12/14/15.
 */
public class SQLFactory {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(SQLFactory.class);
    public SQLFactory() {
    }

    public void logoutUser(String device) {

        Connection dbConnection = null;
        ResultSet rs = null;
        PreparedStatement preparedStatement = null;

        try {
            dbConnection = JDBCUtil.getDBConnection();

                preparedStatement = dbConnection.prepareStatement(
                        "DELETE FROM dc_devices WHERE device_id = ? RETURNING id_user");

                preparedStatement.setString(1, device);
                rs = preparedStatement.executeQuery();
                while (rs.next()){
                    logoutRUser( rs.getLong("id_user"), device);
                }

        } catch (SQLException e) {
            DBUtils.dbRollback(dbConnection);
            log.error("ERROR IN DELETING - " + e.getMessage());
        } finally {
            DBUtils.closeConnections(preparedStatement, dbConnection, rs);
        }
    }

    private void logoutRUser(Long idUser, String device) {

        try {

            RMemoryAPI.getInstance().delFromMemory(Constants.DEVICE_KEY + device);
            RMemoryAPI.getInstance().delFromSetElem(Constants.USER_KEY + "device:" + idUser, device);
            log.info("Removed Unregistered device for UserID: {}", idUser);
        } catch (Exception e) {
            log.error("ERROR IN DELETING DEVICE REDIS", e);
        }

    }

    public void updateDeviceID(String newID, String oldID){
        Connection dbConnection = null;
        ResultSet rs = null;
        try {
            dbConnection = JDBCUtil.getDBConnection();
            PreparedStatement preparedStatement = null;
            try {
                preparedStatement = dbConnection.prepareStatement(
                        "UPDATE dc_devices SET device_id=? WHERE device_id=? RETURNING id_user");

                preparedStatement.setString(1, newID);
                preparedStatement.setString(2, oldID);
                rs = preparedStatement.executeQuery();
                while (rs.next()){
                    updateRedisUserDevice(rs.getLong("id_user"), newID, oldID);
                }

            } finally {
                if (rs != null) {
                    rs.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            }

        } catch (Exception e) {
            try {
                dbConnection.rollback();
            } catch (SQLException e1) {
                log.error("", e);
            }
            log.error("ERROR IN DELETING - ", e);
        } finally {
            if (dbConnection != null) {
                try {
                    dbConnection.commit();
                    dbConnection.close();
                } catch (SQLException e){
                    log.error("", e);
                }

            }
        }
    }

    private void updateRedisUserDevice(Long idUser,String newId,String oldId){

        try {
            RMemoryAPI.getInstance().delFromSetElem(Constants.USER_KEY + "device:" + idUser, oldId);
            RMemoryAPI.getInstance().pushSetElemToMemory(Constants.USER_KEY + "device:" + idUser, newId);

            DcDeviceType dev = (DcDeviceType)RMemoryAPI.getInstance()
                    .pullElemFromMemory(Constants.DEVICE_KEY + oldId, DcDeviceType.class);
            if(dev==null){
                dev= new DcDeviceType();
                dev.setDevType(1); //For now only for Android
                dev.setIdUser(idUser);
            }

            RMemoryAPI.getInstance().delFromMemory(Constants.DEVICE_KEY + oldId);
            RMemoryAPI.getInstance().pushElemToMemory(Constants.DEVICE_KEY + newId, -1, dev.toString());



        } catch (Exception e) {
            log.error("ERROR IN DELETING ", e);
        }

    }
}
