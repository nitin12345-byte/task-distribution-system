package com.itt.tds.client;

import com.google.gson.internal.LinkedTreeMap;
import com.itt.tds.core.Constants;
import com.itt.tds.core.Networking.RequestSender;
import com.itt.tds.core.Networking.TDSRequest;
import com.itt.tds.core.Networking.TDSResponse;
import com.itt.tds.core.enums.TaskStatus;
import com.itt.tds.core.config.TDSConfiguration;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class CommandExecutor {

    private String[] command;

    public CommandExecutor(String[] command) {
        this.command = command;
    }

    public void execute() throws InvalidCommandException, ClassNotFoundException {
        switch (command[0]) {
            case "task" -> {
                if (command.length > 1) {
                    sendTask(command[1]);
                } else {
                    throw new InvalidCommandException("Please provide the name of the file as well");
                }
            }
            case "status" -> {
                if (command.length > 1) {
                    getTaskStatus(command[1]);
                } else {
                    throw new InvalidCommandException("Please provide the task id");
                }
            }

            case "result" -> {
                if (command.length > 1) {
                    getTaskResult(command[1]);
                } else {
                    throw new InvalidCommandException("Please provide the task id");
                }
            }

            case "tasks" ->
                getAllTasks();
            case "register" ->
                registerClient();
            case "unregister" ->
                unregisterClient();
            case "help" ->
                help();

            default ->
                throw new InvalidCommandException("Invalid Command");
        }
    }

    private void sendTask(String file) throws ClassNotFoundException {
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
                if (tdsResponse.getStatus() == 1) {
                    showMessage("Task is submitted successfuly with task ID : " + tdsResponse.getValue(Constants.TASK_ID));
                } else {
                    showMessage(tdsResponse.getErrorMessage());
                }
            } catch (IOException exp) {
                showMessage(exp.getMessage());
            }
        } else {
            showMessage("Plese register the client first");
        }
    }

    private void getTaskStatus(String taskId) throws ClassNotFoundException {
        ClientIdFileProcessor fileProcessor = new ClientIdFileProcessor();
        String clientId = fileProcessor.read();
        if (!clientId.isEmpty()) {
            try {
                TDSRequest tdsRequest = new TDSRequest();
                tdsRequest.setMethod(Constants.CLIENT_TASK_STATUS);
                tdsRequest.setDestinationPort(TDSConfiguration.DISTRIBUTOR_PORT_NUMBER);
                tdsRequest.setDestinationIp(TDSConfiguration.DISTRIBUTOR_IP_ADDRESS);
                tdsRequest.setParameter(Constants.TASK_ID, taskId);
                TDSResponse tdsResponse = RequestSender.sendRequest(tdsRequest);
                if (tdsResponse.getStatus() == 1) {
                    showMessage("Task status is  : " + tdsResponse.getValue(Constants.STATUS));
                } else {
                    showMessage(tdsResponse.getErrorMessage());
                }
            } catch (IOException exp) {
                showMessage(exp.getMessage());
            }
        } else {
            showMessage("Please register the client first");
        }
    }

    private void registerClient() throws ClassNotFoundException {
        String hostName;
        String ipAddress;

        ClientIdFileProcessor fileProcessor = new ClientIdFileProcessor();
        String clientId = fileProcessor.read();

        if (clientId.isEmpty()) {

            try {
                InetAddress inetAddress = InetAddress.getLocalHost();
                hostName = inetAddress.getHostName();
                ipAddress = inetAddress.getHostAddress();
            } catch (UnknownHostException ex) {
                hostName = "localhost";
                ipAddress = "localhost";
            }

            try {
                TDSRequest tdsRequest = new TDSRequest();
                tdsRequest.setMethod(Constants.CLIENT_REGISTER);
                tdsRequest.setDestinationPort(TDSConfiguration.DISTRIBUTOR_PORT_NUMBER);
                tdsRequest.setDestinationIp(TDSConfiguration.DISTRIBUTOR_IP_ADDRESS);
                tdsRequest.setParameter(Constants.IP_ADDRESS, ipAddress);
                tdsRequest.setParameter(Constants.HOST_NAME, hostName);
                TDSResponse response = RequestSender.sendRequest(tdsRequest);
                if (response.getStatus() == 1) {
                    fileProcessor.write((String) response.getValue(Constants.CLIENT_ID));
                    showMessage("client is registered successfully with ID : " + response.getValue(Constants.CLIENT_ID));
                } else {
                    showMessage("Error Code" + Double.valueOf((double) response.getErrorCode()).intValue());
                    showMessage(response.getErrorMessage());
                }
            } catch (IOException exp) {
                showMessage(exp.getMessage());
            }
        } else {
            showMessage("client is already registered");
        }
    }

    private void getTaskResult(String taskId) throws ClassNotFoundException {
        ClientIdFileProcessor fileProcessor = new ClientIdFileProcessor();
        String clientId = fileProcessor.read();
        if (!clientId.isEmpty()) {
            try {
                TDSRequest tdsRequest = new TDSRequest();
                tdsRequest.setMethod(Constants.CLIENT_TASK_RESULT);
                tdsRequest.setDestinationPort(TDSConfiguration.DISTRIBUTOR_PORT_NUMBER);
                tdsRequest.setDestinationIp(TDSConfiguration.DISTRIBUTOR_IP_ADDRESS);
                tdsRequest.setParameter(Constants.TASK_ID, taskId);
                TDSResponse tdsResponse = RequestSender.sendRequest(tdsRequest);

                if (tdsResponse.getStatus() == 1) {
                    if (((String) tdsResponse.getValue(Constants.ERROR_MESSAGE)).isEmpty()) {
                        showMessage("Task result is : " + tdsResponse.getValue("result"));
                    } else {
                        int errorCode = (Double.valueOf((double) tdsResponse.getValue(Constants.ERROR_CODE))).intValue();
                        showMessage("Error Code : " + errorCode);
                        showMessage("Error Message : " + tdsResponse.getValue(Constants.ERROR_MESSAGE));
                    }
                } else {
                    showMessage("Error Code: " + Double.valueOf((double) tdsResponse.getErrorCode()).intValue());
                    showMessage(tdsResponse.getErrorMessage());
                }
            } catch (IOException exception) {
                showMessage(exception.getMessage());
            }
        } else {
            showMessage("Please register the client first");
        }
    }

    private void help() {
        System.out.println("1. [task  file_name] : this command is use to send the task to the distributor ");
        System.out.println("2. [status task_id] : this command is use to get the status of the task");
        System.out.println("3. [result task_id] : this command is use to get result of the task");
        System.out.println("4. [register] : this command is use to register a client");
    }

    private void showMessage(String message) {
        System.out.println(message);
    }

    private String fileToBase64String(String filePath)
            throws FileNotFoundException, IOException {
        File file = new File(filePath);
        FileInputStream fileInputStream = new FileInputStream(file);
        return Base64.getEncoder().encodeToString(fileInputStream.readAllBytes());
    }

    private void unregisterClient() {
        ClientIdFileProcessor fileProcessor = new ClientIdFileProcessor();
        String clientId = fileProcessor.read();

        if (!clientId.isEmpty()) {

            TDSRequest tdsRequest = new TDSRequest();
            tdsRequest.setDestinationIp(TDSConfiguration.DISTRIBUTOR_IP_ADDRESS);
            tdsRequest.setDestinationPort(TDSConfiguration.DISTRIBUTOR_PORT_NUMBER);
            tdsRequest.setMethod(Constants.CLIENT_UNREGISTER);
            tdsRequest.setParameter(Constants.CLIENT_ID, clientId);
            try {
                TDSResponse tdsResponse = RequestSender.sendRequest(tdsRequest);
                if (tdsResponse.getStatus() == 1) {
                    fileProcessor.delete();

                    System.out.println("Client unregistered successfully");
                } else {
                    showMessage("Error Code : " + Double.valueOf((double) tdsResponse.getErrorCode()).intValue());
                    System.out.println(tdsResponse.getErrorMessage());
                }
            } catch (IOException | ClassNotFoundException ex) {
                System.out.println("Connection with database is interrupted");
            }

        } else {
            System.out.println("Client is already not registered");
        }
    }

    private void getAllTasks() {
        ClientIdFileProcessor fileProcessor = new ClientIdFileProcessor();
        String clientId = fileProcessor.read();

        if (!clientId.isEmpty()) {
            TDSRequest tdsRequest = new TDSRequest();
            tdsRequest.setMethod(Constants.CLIENT_TASKS);
            tdsRequest.setParameter(Constants.CLIENT_ID, clientId);
            tdsRequest.setDestinationIp(TDSConfiguration.DISTRIBUTOR_IP_ADDRESS);
            tdsRequest.setDestinationPort(TDSConfiguration.DISTRIBUTOR_PORT_NUMBER);
            try {
                TDSResponse tdsResponse = RequestSender.sendRequest(tdsRequest);
                if (tdsResponse.getStatus() == 1) {
                    List taskList = (ArrayList) tdsResponse.getValue(Constants.TASKS);
                    displayAllTask(taskList);
                } else {
                    System.out.print(tdsResponse.getErrorMessage());
                }
            } catch (IOException | ClassNotFoundException ex) {
                System.out.print("Connection with distributore is interrupted");
            }
        } else {
            showMessage("Please register the client first");
        }
    }

    private void displayAllTask(List<LinkedTreeMap> taskList) {
        taskList.forEach(task -> {
            System.out.println(String.format("[task_id = %s,node_id = %s, status = %s,file = %s]",
                    task.get(Constants.ID), task.get("nodeId"), task.get(Constants.STATUS), task.get("filePath")));

        });
    }
}
