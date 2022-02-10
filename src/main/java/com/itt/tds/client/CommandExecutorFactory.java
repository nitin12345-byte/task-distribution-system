package com.itt.tds.client;

public class CommandExecutorFactory {

    public static CommandExecutor getCommandExecutor(String command) throws InvalidCommandException {
        CommandExecutor commandExecutor;

        if (command.equalsIgnoreCase("register")) {
            commandExecutor = new RegisterCommandExecutor();
        } else if (command.equalsIgnoreCase("unregister")) {
            commandExecutor = new UnregisterCommandExecutor();
        } else if (command.equalsIgnoreCase("task")) {
            commandExecutor = new TaskCommandExecutor();
        } else if (command.equalsIgnoreCase("tasks")) {
            commandExecutor = new TasksCommandExecutor();
        } else if (command.equalsIgnoreCase("result")) {
            commandExecutor = new ResultCommandExecutor();
        } else if (command.equalsIgnoreCase("status")) {
            commandExecutor = new StatusCommandExecutor();
        } else if (command.equalsIgnoreCase("help")) {
            commandExecutor = new HelpCommandExecutor();
        } else {
            throw new InvalidCommandException();
        }

        return commandExecutor;
    }
}
