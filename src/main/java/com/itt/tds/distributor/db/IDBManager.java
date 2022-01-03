package com.itt.tds.distributor.db;

import java.sql.Connection;
import java.sql.SQLException;

public interface IDBManager {

    public Connection createConnection() throws SQLException, ClassNotFoundException;

    public void closeConnection() throws SQLException;

}
