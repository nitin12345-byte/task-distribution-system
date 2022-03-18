package com.itt.tds.client;

import com.itt.tds.core.model.TDSDistributorConfiguration;

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

    public static boolean isDistributorConfigured(TDSDistributorConfiguration configuration) {
        return !(configuration.getDistributorIpAddress().isEmpty() || configuration.getDistributorPortNumber() == 0);
    }
}
