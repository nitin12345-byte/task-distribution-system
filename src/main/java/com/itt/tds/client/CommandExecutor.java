package com.itt.tds.client;

public interface CommandExecutor {

    public void executeCommand(String parameter) throws InvalidCommandException;
}
