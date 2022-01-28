package com.itt.tds.distributor.db.exceptions;

public class RecordAlreadyExistException extends DBException {

    public RecordAlreadyExistException(String message) {
        super(message);
    }

    public RecordAlreadyExistException() {
        super("Record with given id is already exist");
    }
}
