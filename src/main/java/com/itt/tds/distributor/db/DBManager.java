package com.itt.tds.distributor.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager implements IDBManager {

    private Connection dbConnection = null;
    private static DBManager dbManager = null;

    private DBManager() {
    }

    public static DBManager getInstance() {
        if (dbManager == null) {
            dbManager = new DBManager();
        }

        return dbManager;
    }

    @Override
    public Connection createConnection() throws SQLException, ClassNotFoundException {

        if (dbConnection == null) {

            Class.forName("com.mysql.cj.jdbc.Driver");
            dbConnection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/TDS", "root", "");

        }
        return dbConnection;
    }

    @Override
    public void closeConnection() throws SQLException{
        dbConnection.close();
        dbConnection = null;

    }

}
