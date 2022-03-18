package com.itt.tds.node;

import com.itt.tds.core.model.TDSDistributorConfiguration;

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

    public static boolean isDistributorConfigured(TDSDistributorConfiguration configuration) {
        return !(configuration.getDistributorIpAddress().isEmpty() || configuration.getDistributorPortNumber() == 0);
    }
}
