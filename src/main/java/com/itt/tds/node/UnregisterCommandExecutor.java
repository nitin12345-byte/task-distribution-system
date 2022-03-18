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
public class UnregisterCommandExecutor implements CommandExecutor {

    @Override
    public void executeCommand(String[] parameters) throws InvalidCommandException {
        if (parameters.length == 0) {
            NodeIdFileProcessor fileProcessor = new NodeIdFileProcessor();
            String nodeId = fileProcessor.read();
            if (Utils.isNodeRegistered()) {
                TDSDistributorConfiguration configuration = new TDSDistributorConfigurationFileProcessor().read();
                TDSRequest tdsRequest = new TDSRequest();
                tdsRequest.setDestinationIp(configuration.getDistributorIpAddress());
                tdsRequest.setDestinationPort(configuration.getDistributorPortNumber());
                tdsRequest.setMethod(Constants.NODE_UNREGISTER);
                tdsRequest.setParameter(Constants.NODE_ID, nodeId);
                try {
                    TDSResponse tdsResponse = RequestSender.sendRequest(tdsRequest);
                    if (tdsResponse.getStatus() == ResponseStatus.OK.getValue()) {
                        fileProcessor.delete();
                        Utils.showMessage("Node unregistered successfully");
                    } else {
                        Utils.showMessage(tdsResponse.getErrorMessage());
                    }
                } catch (IOException | ClassNotFoundException ex) {
                    Utils.showMessage("Connection with distributor is interrupted");
                }
            } else {
                Utils.showMessage("Node is already not registered");
            }
        } else {
            throw new InvalidCommandException();
        }
    }
}
