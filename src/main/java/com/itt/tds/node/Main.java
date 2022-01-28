package com.itt.tds.node;

import com.itt.tds.client.InvalidCommandException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    public static void main(String args[]) {

        String arr[] = new String[2];
        arr[0] = "add-capability";
        arr[1] = "C";
        args = arr;

        if (args.length > 0) {
            CommandExecutor commandExecutor = new CommandExecutor(args);
            try {
                commandExecutor.execute();
            } catch (InvalidCommandException | ClassNotFoundException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
