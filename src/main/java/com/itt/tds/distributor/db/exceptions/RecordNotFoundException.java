package com.itt.tds.distributor.db.exceptions;

public class RecordNotFoundException extends DBException {

    public RecordNotFoundException(String message) {
        super(message);
    }

    public RecordNotFoundException() {
        super("Record with given is not found");
    }
}
