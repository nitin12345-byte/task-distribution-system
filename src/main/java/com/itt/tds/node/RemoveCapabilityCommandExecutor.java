package com.itt.tds.node;

import com.itt.tds.client.InvalidCommandException;
import com.itt.tds.core.Constants;
import com.itt.tds.core.Networking.RequestSender;
import com.itt.tds.core.Networking.TDSRequest;
import com.itt.tds.core.Networking.TDSResponse;
import com.itt.tds.core.model.TDSDistributorConfiguration;
import com.itt.tds.core.enums.ResponseStatus;
import java.io.IOException;

/**
 *
 * @author nitin.jangid
 */
public class RemoveCapabilityCommandExecutor implements CommandExecutor {

    @Override
    public void executeCommand(String[] parameters) throws InvalidCommandException {
        String nodeId = new NodeIdFileProcessor().read();
        if (parameters.length == 1) {

            if (Utils.isNodeRegistered()) {
                String capability = parameters[0];
                if (isValidCapability(capability)) {
                    TDSDistributorConfiguration configuration = new TDSDistributorConfigurationFileProcessor().read();
                    TDSRequest tdsRequest = new TDSRequest();
                    tdsRequest.setDestinationIp(configuration.getDistributorIpAddress());
                    tdsRequest.setDestinationPort(configuration.getDistributorPortNumber());
                    tdsRequest.setMethod(Constants.NODE_REMOVE_CAPABILITY);
                    tdsRequest.setParameter(Constants.NODE_ID, nodeId);
                    tdsRequest.setParameter(Constants.CAPABILITY_NAME, capability);
                    try {
                        TDSResponse tdsResponse = RequestSender.sendRequest(tdsRequest);
                        if (tdsResponse.getStatus() == ResponseStatus.OK.getValue()) {
                            Utils.showMessage("Capability removed successfuly");
                        } else {
                            Utils.showMessage(tdsResponse.getErrorMessage());
                        }
                    } catch (IOException | ClassNotFoundException ex) {
                        Utils.showMessage("Connection failed with distributor");
                    }
                }
            } else {
                Utils.showMessage("Please register the node first.");
            }
        } else {
            throw new InvalidCommandException();
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
}
