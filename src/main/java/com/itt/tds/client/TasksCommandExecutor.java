package com.itt.tds.client;

import com.itt.tds.core.model.TDSDistributorConfiguration;
import com.google.gson.internal.LinkedTreeMap;
import com.itt.tds.core.Constants;
import com.itt.tds.core.Networking.RequestSender;
import com.itt.tds.core.Networking.TDSRequest;
import com.itt.tds.core.Networking.TDSResponse;
import com.itt.tds.core.enums.ResponseStatus;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nitin.jangid
 */
public class TasksCommandExecutor implements CommandExecutor {

    @Override
    public void executeCommand(String[] parameters) throws InvalidCommandException {

        if (parameters.length == 0) {
            ClientIdFileProcessor fileProcessor = new ClientIdFileProcessor();
            String clientId = fileProcessor.read();
            if (Utils.isClientRegistered()) {
                TDSDistributorConfiguration configuration = new TDSDistributorConfigurationFileProcessor().read();
                TDSRequest tdsRequest = new TDSRequest();
                tdsRequest.setMethod(Constants.CLIENT_TASKS);
                tdsRequest.setParameter(Constants.CLIENT_ID, clientId);
                tdsRequest.setDestinationIp(configuration.getDistributorIpAddress());
                tdsRequest.setDestinationPort(configuration.getDistributorPortNumber());
                try {
                    TDSResponse tdsResponse = RequestSender.sendRequest(tdsRequest);
                    if (tdsResponse.getStatus() == ResponseStatus.OK.getValue()) {
                        List taskList = (ArrayList) tdsResponse.getValue(Constants.TASKS);
                        if (taskList.size() > 0) {
                            displayAllTask(taskList);
                        } else {
                            Utils.showMessage("No task is available");
                        }
                    } else {
                        Utils.showMessage(tdsResponse.getErrorMessage());
                    }
                } catch (IOException | ClassNotFoundException exception) {
                    Utils.showMessage(exception.getMessage());
                }
            } else {
                showMessage("Please register the client first");
            }
        } else {
            throw new InvalidCommandException();
        }
    }

    private void showMessage(String message) {
        System.out.println(message);
    }

    private void displayAllTask(List<LinkedTreeMap> taskList) {
        final String NODE_ID = "nodeId", FILE_PATH = "filePath", DATE_TIME = "dateTime";
        taskList.forEach(task -> {
            Utils.showMessage(String.format("[task_id = %s,node_id = %s, status = %s,file = %s,DateTime : %s]",
                    task.get(Constants.ID),
                    task.get(NODE_ID),
                    task.get(Constants.STATUS),
                    task.get(FILE_PATH),
                    task.get(DATE_TIME))
            );
        }
        );
    }
}
