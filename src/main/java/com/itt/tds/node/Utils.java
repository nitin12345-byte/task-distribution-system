package com.itt.tds.node;

/**
 *
 * @author nitin.jangid
 */
public class Utils {

    public static boolean isNodeRegistered() {
        String nodeId = new NodeIdFileProcessor().read();
        return !nodeId.isEmpty();
    }

    public static void showMessage(String message) {
        System.out.println(message);
    }
}
