package com.itt.tds.distributor.db;

import java.sql.Connection;
import java.sql.SQLException;

public interface IDBManager {

    public Connection createConnection();

    public void closeConnection() throws SQLException;

}
