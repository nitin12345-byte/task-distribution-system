package com.itt.tds.client;

import com.itt.tds.core.Constants;
import com.itt.tds.core.Networking.RequestSender;
import com.itt.tds.core.Networking.TDSRequest;
import com.itt.tds.core.Networking.TDSResponse;
import com.itt.tds.core.config.TDSConfiguration;
import com.itt.tds.core.enums.ResponseStatus;
import com.itt.tds.core.enums.TaskResultErrorCode;
import java.io.IOException;

/**
 *
 * @author nitin.jangid
 */
public class ResultCommandExecutor implements CommandExecutor {

    @Override
    public void executeCommand(String parameter) throws InvalidCommandException {
        if (!parameter.isEmpty()) {
            String taskId = parameter;
            ClientIdFileProcessor fileProcessor = new ClientIdFileProcessor();
            String clientId = fileProcessor.read();

            if (Utils.isClientRegistered()) {
                try {
                    TDSRequest tdsRequest = new TDSRequest();
                    tdsRequest.setMethod(Constants.CLIENT_TASK_RESULT);
                    tdsRequest.setDestinationPort(TDSConfiguration.DISTRIBUTOR_PORT_NUMBER);
                    tdsRequest.setDestinationIp(TDSConfiguration.DISTRIBUTOR_IP_ADDRESS);
                    tdsRequest.setParameter(Constants.TASK_ID, taskId);
                    tdsRequest.setParameter(Constants.CLIENT_ID, clientId);
                    TDSResponse tdsResponse = RequestSender.sendRequest(tdsRequest);

                    if (tdsResponse.getStatus() == ResponseStatus.OK.getValue()) {
                        if (((String) tdsResponse.getValue(Constants.ERROR_MESSAGE)).isEmpty()) {
                            Utils.showMessage("Task result is : " + tdsResponse.getValue(Constants.RESULT));
                        } else {
                            int errorCode = (Double.valueOf((double) tdsResponse.getValue(Constants.ERROR_CODE))).intValue();
                            String errorCodeName = getErrorCodeName(errorCode);
                            Utils.showMessage(String.format("Error Code : %d (%s)", errorCode, errorCodeName));
                            Utils.showMessage("Error Message : " + tdsResponse.getValue(Constants.ERROR_MESSAGE));
                        }
                    } else {
                        Utils.showMessage("Error Code: " + Double.valueOf((double) tdsResponse.getErrorCode()).intValue());
                        Utils.showMessage(tdsResponse.getErrorMessage());
                    }
                } catch (IOException | ClassNotFoundException exception) {
                    Utils.showMessage(exception.getMessage());
                }
            } else {
                Utils.showMessage("Please register the client first");
            }
        } else {
            throw new InvalidCommandException();
        }
    }

    private String getErrorCodeName(int errorCode) {
        for (TaskResultErrorCode taskResultErrorCode : TaskResultErrorCode.values()) {
            if (taskResultErrorCode.getValue() == errorCode) {
                return taskResultErrorCode.name();
            }
        }
        return Constants.EMPTY_STRING;
    }

}
