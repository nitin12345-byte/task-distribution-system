package com.itt.tds.db;

import java.sql.Connection;

public interface IDBManager {

    public Connection createConnection();

    public void close();
    
}
