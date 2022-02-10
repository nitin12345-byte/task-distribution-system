package com.itt.tds.client;

public class InvalidCommandException extends Exception {

    public InvalidCommandException(String message) {
        super(message);
    }

    public InvalidCommandException() {
        super("!Command is invalid please check the command once.");
    }
}
