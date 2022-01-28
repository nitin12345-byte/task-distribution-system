package com.itt.tds.distributor.db.exceptions;

public class DBConnectionException extends DBException {

    public DBConnectionException(String message) {
        super(message);
    }

    public DBConnectionException() {
        super("Database connection failed");
    }
}
