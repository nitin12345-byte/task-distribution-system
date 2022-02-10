package com.itt.tds.node;

import com.itt.tds.client.InvalidCommandException;
import com.itt.tds.core.Constants;

public class Main {

    public static void main(String command[]) {

        command = new String[1];
        command[0] = "start";
        //command[1] = "";

        if (command.length > 0 && command.length < 3) {
            try {
                CommandExecutor commandExecutor = CommandExecutorFactory.getCommandExecutor(command[0]);
                String commandParameter = getCommandParameter(command);
                commandExecutor.executeCommand(commandParameter);
            } catch (InvalidCommandException exception) {
                Utils.showMessage(exception.getMessage());
            }
        } else {
            Utils.showMessage("invalid command");
        }
    }

    private static String getCommandParameter(String[] command) {
        if (command.length > 1) {
            return command[1];
        }
        return Constants.EMPTY_STRING;
    }
}
