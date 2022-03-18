package com.itt.tds.node;

import com.itt.tds.client.InvalidCommandException;

/**
 *
 * @author nitin.jangid
 */
public class CommandExecutorFactory {
    
    public static CommandExecutor getCommandExecutor(String command) throws InvalidCommandException {
        CommandExecutor commandExecutor = null;
        if (command.equalsIgnoreCase("register")) {
            commandExecutor = new RegisterCommandExecutor();
        } else if (command.equalsIgnoreCase("add-capability")) {
            commandExecutor = new AddCapabilityCommandExecutor();
        } else if (command.equalsIgnoreCase("remove-capability")) {
            commandExecutor = new RemoveCapabilityCommandExecutor();
        } else if (command.equalsIgnoreCase("start")) {
            commandExecutor = new StartCommandExecutor();
        } else if (command.equalsIgnoreCase("config")) {
            commandExecutor = new ConfigurationCommandExecutor();
        } else if (command.equalsIgnoreCase("unregister")) {
            commandExecutor = new UnregisterCommandExecutor();
        } else if (command.equalsIgnoreCase("help")) {
            commandExecutor = new HelpCommandExecutor();
        } else {
            throw new InvalidCommandException();
        }
        return commandExecutor;
    }
}
