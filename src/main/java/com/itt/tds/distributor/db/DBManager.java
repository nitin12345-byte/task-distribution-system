package com.itt.tds.distributor.db;

import com.itt.tds.core.config.TDSConfiguration;
import com.itt.tds.core.logging.LogManager;
import com.itt.tds.core.logging.Logger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager implements IDBManager {

    private Connection dbConnection = null;
    private Logger logger;
    private static DBManager dbManager = null;

    private DBManager() {
        logger = LogManager.getLogger(this.getClass().getName());
    }

    public static DBManager getInstance() {
        if (dbManager == null) {
            dbManager = new DBManager();
        }

        return dbManager;
    }

    @Override
    public Connection createConnection() {

        if (dbConnection == null) {

            try {

                Class.forName(TDSConfiguration.DB_DRIVER_STRING);
                dbConnection = DriverManager.getConnection(
                        TDSConfiguration.DB_STRING, "root", "");

            } catch (SQLException | ClassNotFoundException exception) {
                dbConnection = null;
                logger.logError("createConnection", exception.getMessage());
            }

        }
        return dbConnection;
    }

    @Override
    public void closeConnection() throws SQLException {
        dbConnection.close();
        dbConnection = null;

    }

}
