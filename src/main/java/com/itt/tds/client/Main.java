package com.itt.tds.client;

public class Main {

    public static void main(String[] command) {

        
        if (command.length > 0) {
            try {
                CommandExecutor commandExecutor = CommandExecutorFactory.getCommandExecutor(command[0]);
                String[] commandParameters = getCommandParameters(command);
                commandExecutor.executeCommand(commandParameters);
            } catch (InvalidCommandException exception) {
                Utils.showMessage(exception.getMessage());
            }
        }
    }

    private static String[] getCommandParameters(String[] command) {
        String[] commandParameters = new String[command.length - 1];
        int parameterIndex = 0;

        for (int cmdIndex = 1; cmdIndex < command.length; cmdIndex++) {
            commandParameters[parameterIndex++] = command[cmdIndex];
        }

        return commandParameters;
    }

}
