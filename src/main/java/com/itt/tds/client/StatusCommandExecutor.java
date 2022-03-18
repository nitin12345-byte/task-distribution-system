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
public class StatusCommandExecutor implements CommandExecutor {

    @Override
    public void executeCommand(String[] parameters) throws InvalidCommandException {
        if (parameters.length == 1) {
            String taskId = parameters[0];
            ClientIdFileProcessor fileProcessor = new ClientIdFileProcessor();
            String clientId = fileProcessor.read();

            if (Utils.isClientRegistered()) {
                try {
                    TDSDistributorConfiguration configuration = new TDSDistributorConfigurationFileProcessor().read();
                    TDSRequest tdsRequest = new TDSRequest();
                    tdsRequest.setMethod(Constants.CLIENT_TASK_STATUS);
                    tdsRequest.setDestinationPort(configuration.getDistributorPortNumber());
                    tdsRequest.setDestinationIp(configuration.getDistributorIpAddress());
                    tdsRequest.setParameter(Constants.TASK_ID, taskId);
                    tdsRequest.setParameter(Constants.CLIENT_ID, clientId);
                    TDSResponse tdsResponse = RequestSender.sendRequest(tdsRequest);
                    if (tdsResponse.getStatus() == ResponseStatus.OK.getValue()) {
                        showMessage("Task status is  : " + tdsResponse.getValue(Constants.STATUS));
                    } else {
                        showMessage(tdsResponse.getErrorMessage());
                    }
                } catch (IOException | ClassNotFoundException exp) {
                    showMessage(exp.getMessage());
                }

            } else {
                Utils.showMessage("Please configured the distributor first");
            }
        } else {
            throw new InvalidCommandException();
        }
    }

    private void showMessage(String message) {
        System.out.println(message);
    }
}
