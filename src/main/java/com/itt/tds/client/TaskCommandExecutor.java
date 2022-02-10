package com.itt.tds.client;

import com.itt.tds.core.Constants;
import com.itt.tds.core.Networking.RequestSender;
import com.itt.tds.core.Networking.TDSRequest;
import com.itt.tds.core.Networking.TDSResponse;
import com.itt.tds.core.config.TDSConfiguration;
import com.itt.tds.core.enums.ResponseStatus;
import com.itt.tds.core.enums.TaskStatus;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Base64;

/*
 *
 * @author nitin.jangid
 */
public class TaskCommandExecutor implements CommandExecutor {

    @Override
    public void executeCommand(String parameter) throws InvalidCommandException {
        if (!parameter.isEmpty()) {
            String file = parameter;
            ClientIdFileProcessor fileProcessor = new ClientIdFileProcessor();
            String clientId = fileProcessor.read();
            if (!clientId.isEmpty()) {
                try {
                    String base64String = fileToBase64String(file);
                    TDSRequest tdsRequest = new TDSRequest();
                    tdsRequest.setParameter(Constants.DATA, base64String);
                    tdsRequest.setParameter(Constants.TASK_STATUS, TaskStatus.PENDING.name());
                    tdsRequest.setParameter(Constants.CLIENT_ID, clientId);
                    tdsRequest.setParameter(Constants.FILE_PATH, new File(file).getName());
                    tdsRequest.setMethod(Constants.CLIENT_ADD_TASK);
                    tdsRequest.setDestinationPort(TDSConfiguration.DISTRIBUTOR_PORT_NUMBER);
                    tdsRequest.setDestinationIp(TDSConfiguration.DISTRIBUTOR_IP_ADDRESS);
                    TDSResponse tdsResponse = RequestSender.sendRequest(tdsRequest);
                    if (tdsResponse.getStatus() == ResponseStatus.OK.getValue()) {
                        showMessage("Task is submitted successfuly with task ID : " + tdsResponse.getValue(Constants.TASK_ID));
                    } else {
                        showMessage(tdsResponse.getErrorMessage());
                    }
                } catch (IOException | ClassNotFoundException exp) {
                    showMessage(exp.getMessage());
                }
            } else {
                showMessage("Plese register the client first");
            }
        } else {
            throw new InvalidCommandException();
        }
    }

    private String fileToBase64String(String filePath)
            throws FileNotFoundException, IOException {
        File file = new File(filePath);
        FileInputStream fileInputStream = new FileInputStream(file);
        return Base64.getEncoder().encodeToString(fileInputStream.readAllBytes());
    }

    private void showMessage(String message) {
        System.out.println(message);
    }

}
