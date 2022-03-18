package com.itt.tds.node;

import com.itt.tds.client.InvalidCommandException;

public interface CommandExecutor {

    public void executeCommand(String[] paramters) throws InvalidCommandException;
}
