package com.itt.tds.db;

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
    public Connection createConnection() {

        if (dbConnection == null) {
            try {

                Class.forName("com.mysql.cj.jdbc.Driver");
                dbConnection = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/TDS", "root", "");

            } catch (ClassNotFoundException | SQLException e) {
                System.out.println(e);
            }
        }
        return dbConnection;
    }

    @Override
    public void close() {
        try {
            dbConnection.close();
            dbConnection = null;
        } catch (SQLException exp) {

        }
    }

}
