package com.itt.tds.client;

import com.itt.tds.core.model.TDSDistributorConfiguration;
import com.itt.tds.core.Constants;
import com.itt.tds.core.Networking.RequestSender;
import com.itt.tds.core.Networking.TDSRequest;
import com.itt.tds.core.Networking.TDSResponse;
import com.itt.tds.core.enums.ResponseStatus;
import java.io.IOException;

/**
 *
 * @author nitin.jangid
 */
public class UnregisterCommandExecutor implements CommandExecutor {

    @Override
    public void executeCommand(String[] parameters) throws InvalidCommandException {
        if (parameters.length == 0) {
            ClientIdFileProcessor fileProcessor = new ClientIdFileProcessor();
            String clientId = fileProcessor.read();
            if (Utils.isClientRegistered()) {
                TDSDistributorConfiguration configuration = new TDSDistributorConfigurationFileProcessor().read();
                TDSRequest tdsRequest = new TDSRequest();
                tdsRequest.setDestinationIp(configuration.getDistributorIpAddress());
                tdsRequest.setDestinationPort(configuration.getDistributorPortNumber());
                tdsRequest.setMethod(Constants.CLIENT_UNREGISTER);
                tdsRequest.setParameter(Constants.CLIENT_ID, clientId);
                try {
                    TDSResponse tdsResponse = RequestSender.sendRequest(tdsRequest);
                    if (tdsResponse.getStatus() == ResponseStatus.OK.getValue()) {
                        fileProcessor.delete();
                        System.out.println("Client unregistered successfully");
                    } else {
                        Utils.showMessage("Error Code : " + Double.valueOf((double) tdsResponse.getErrorCode()).intValue());
                        Utils.showMessage("Error Message : " + tdsResponse.getErrorMessage());
                    }
                } catch (IOException | ClassNotFoundException exception) {
                    Utils.showMessage(exception.getMessage());
                }

            } else {
                Utils.showMessage("Client is already not registered");
            }
        } else {
            throw new InvalidCommandException();
        }
    }
}
