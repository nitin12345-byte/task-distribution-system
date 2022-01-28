package com.itt.tds.client;

public class Main {

    public static void main(String[] args) {

        if (args.length > 0) {
            CommandExecutor commandExecutor = new CommandExecutor(args);
            try {
                commandExecutor.execute();
            } catch (InvalidCommandException | ClassNotFoundException ex) {
                System.out.print(ex.getMessage());
            }
        }

    }
}
