package com.itt.tds.distributor.db.exceptions;

public class DBException extends Exception {

    public DBException(String message) {
        super(message);
    }

    public DBException() {
        super("There is some problem with the database");
    }
}
