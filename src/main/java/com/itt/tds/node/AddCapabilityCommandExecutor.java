package com.itt.tds.node;

import com.itt.tds.client.InvalidCommandException;
import com.itt.tds.core.Constants;
import com.itt.tds.core.Networking.RequestSender;
import com.itt.tds.core.Networking.TDSRequest;
import com.itt.tds.core.Networking.TDSResponse;
import com.itt.tds.core.config.TDSConfiguration;
import com.itt.tds.core.enums.ResponseStatus;
import com.itt.tds.core.model.TDSDistributorConfiguration;
import java.io.IOException;

/**
 *
 * @author nitin.jangid
 */
public class AddCapabilityCommandExecutor implements CommandExecutor {

    @Override
    public void executeCommand(String[] parameters) throws InvalidCommandException {
        if (parameters.length == 1) {
            String capability = parameters[0];
            if (Utils.isNodeRegistered()) {

                if (isValidCapability(capability)) {
                    if (isEnvironmentExist(capability)) {
                        sendRequstToAddCapability(capability);
                    } else {
                        Utils.showMessage("The System does not have enviroment for " + capability);
                    }
                }
            } else {
                Utils.showMessage("Please register the node first");
            }
        } else {
            throw new InvalidCommandException();
        }
    }

    private void sendRequstToAddCapability(String capability) {
        String nodeId = new NodeIdFileProcessor().read();
        if (Utils.isNodeRegistered()) {
            TDSDistributorConfiguration configuration = new TDSDistributorConfigurationFileProcessor().read();
            TDSRequest tdsRequest = new TDSRequest();
            tdsRequest.setDestinationIp(configuration.getDistributorIpAddress());
            tdsRequest.setDestinationPort(configuration.getDistributorPortNumber());
            tdsRequest.setMethod(Constants.NODE_ADD_CAPABILITY);
            tdsRequest.setParameter(Constants.NODE_ID, nodeId);
            tdsRequest.setParameter(Constants.CAPABILITY_NAME, capability);

            try {
                TDSResponse tdsResponse = RequestSender.sendRequest(tdsRequest);
                if (tdsResponse.getStatus() == ResponseStatus.OK.getValue()) {
                    Utils.showMessage("Capability added successfuly");
                } else {
                    Utils.showMessage(tdsResponse.getErrorMessage());
                }
            } catch (IOException | ClassNotFoundException ex) {
                Utils.showMessage("Connection failed with distributor");
            }
        } else {
            Utils.showMessage("Please register the node first.");
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
}
