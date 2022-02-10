package com.itt.tds.client;

/**
 *
 * @author nitin.jangid
 */
public class Utils {

    public static void showMessage(String message) {
        System.out.println(message);
    }

    public static boolean isClientRegistered() {
        String clientId = new ClientIdFileProcessor().read();
        return !clientId.isEmpty();
    }

}
