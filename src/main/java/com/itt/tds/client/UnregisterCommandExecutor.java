package com.itt.tds.client;

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
public class UnregisterCommandExecutor implements CommandExecutor {

    @Override
    public void executeCommand(String parameter) throws InvalidCommandException {
        if (parameter.isEmpty()) {
            ClientIdFileProcessor fileProcessor = new ClientIdFileProcessor();
            String clientId = fileProcessor.read();

            if (Utils.isClientRegistered()) {
                TDSRequest tdsRequest = new TDSRequest();
                tdsRequest.setDestinationIp(TDSConfiguration.DISTRIBUTOR_IP_ADDRESS);
                tdsRequest.setDestinationPort(TDSConfiguration.DISTRIBUTOR_PORT_NUMBER);
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
                } catch (IOException | ClassNotFoundException ex) {
                    Utils.showMessage("Connection with database is interrupted");
                }

            } else {
                Utils.showMessage("Client is already not registered");
            }
        } else {
            throw new InvalidCommandException();
        }
    }
}
