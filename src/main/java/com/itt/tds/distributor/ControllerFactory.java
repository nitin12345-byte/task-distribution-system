package com.itt.tds.distributor;

public class ControllerFactory {

    public static TDSController getController(String requestMethod) {

        if (requestMethod.startsWith("node")) {
            return new NodeController();
        }

        return new ClientController();
    }
}
