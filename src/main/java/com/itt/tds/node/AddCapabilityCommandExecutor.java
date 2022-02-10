package com.itt.tds.node;

import com.itt.tds.client.InvalidCommandException;
import com.itt.tds.core.Constants;
import com.itt.tds.core.Networking.RequestSender;
import com.itt.tds.core.Networking.TDSRequest;
import com.itt.tds.core.Networking.TDSResponse;
import com.itt.tds.core.config.TDSConfiguration;
import com.itt.tds.core.enums.ResponseStatus;
import java.io.IOException;

/**
 *
 * @author nitin.jangid
 */
public class AddCapabilityCommandExecutor implements CommandExecutor {

    @Override
    public void executeCommand(String parameter) throws InvalidCommandException {
        if (!parameter.isEmpty()) {
            String capability = parameter;
            if (isValidCapability(capability)) {
                if (isEnvironmentExist(capability)) {
                    sendRequstToAddCapability(capability);
                } else {
                    showMessage("The System does not have enviroment for " + capability);
                }
            }
        } else {
            throw new InvalidCommandException();
        }
    }

    private void sendRequstToAddCapability(String capability) {
        String nodeId = new NodeIdFileProcessor().read();
        if (!nodeId.isEmpty()) {
            TDSRequest tdsRequest = new TDSRequest();
            tdsRequest.setDestinationIp(TDSConfiguration.DISTRIBUTOR_IP_ADDRESS);
            tdsRequest.setDestinationPort(TDSConfiguration.DISTRIBUTOR_PORT_NUMBER);
            tdsRequest.setMethod(Constants.NODE_ADD_CAPABILITY);
            tdsRequest.setParameter(Constants.NODE_ID, nodeId);
            tdsRequest.setParameter(Constants.CAPABILITY_NAME, capability);

            try {
                TDSResponse tdsResponse = RequestSender.sendRequest(tdsRequest);
                if (tdsResponse.getStatus() == ResponseStatus.OK.getValue()) {
                    System.out.println("Capability added successfuly");
                } else {
                    System.out.print(tdsResponse.getErrorMessage());
                }
            } catch (IOException | ClassNotFoundException ex) {
                System.out.println("Connection failed with distributor");
            }
        } else {
            showMessage("Please register the node first.");
        }
    }

    private boolean isValidCapability(String capabilityName) {
        for (Capability capability : Capability.values()) {
            if (capabilityName.equals(capability.name())) {
                return true;
            }
        }
        return false;
    }

    private boolean isEnvironmentExist(String capability) {
        EnvironmentChecker environmetChecker = new EnvironmentChecker();
        return environmetChecker.checkEnvironmentFor(capability);
    }

    private void showMessage(String message) {
        System.out.println(message);
    }

}
