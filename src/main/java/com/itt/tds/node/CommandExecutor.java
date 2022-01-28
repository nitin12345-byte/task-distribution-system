package com.itt.tds.node;

import com.itt.tds.client.InvalidCommandException;
import com.itt.tds.core.Constants;
import com.itt.tds.core.Networking.RequestSender;
import com.itt.tds.core.Networking.TDSSerializerFactory;
import com.itt.tds.core.Networking.TDSRequest;
import com.itt.tds.core.Networking.TDSResponse;
import com.itt.tds.core.Networking.TDSSerializer;
import com.itt.tds.core.config.TDSConfiguration;
import com.itt.tds.core.enums.TDSResponseErrorCode;
import com.itt.tds.core.logging.LogManager;
import com.itt.tds.core.logging.Logger;
import com.itt.tds.core.model.TaskResult;
import com.itt.tds.core.enums.NodeStatus;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Base64;

public class CommandExecutor {

    private String[] command;
    private TDSSerializer tdsSerializer;
    private String NODE_DIR = "c:\\ExecutorNode\\";
    private Logger logger;
    private String nodeId;

    public CommandExecutor(String[] command) {
        this.command = command;
        logger = LogManager.getLogger(this.getClass().getName());
        tdsSerializer = TDSSerializerFactory.getSerializer(TDSConfiguration.SERIALIZATION_FORMAT);
        nodeId = new NodeIdFileProcessor().read();

    }

    public void execute() throws InvalidCommandException, ClassNotFoundException {
        switch (command[0]) {
            case "register" ->
                registerNode();
            case "start" ->
                startNode();
            case "unregister" ->
                unregisterNode();
            case "add-capability" ->
                addCapability(command);
            case "remove-capability" ->
                removeCapability(command);
            default ->
                throw new InvalidCommandException();
        }

    }

    private void unregisterNode() {
        NodeIdFileProcessor fileProcessor = new NodeIdFileProcessor();

        if (!nodeId.isEmpty()) {
            TDSRequest tdsRequest = new TDSRequest();
            tdsRequest.setDestinationIp(TDSConfiguration.DISTRIBUTOR_IP_ADDRESS);
            tdsRequest.setDestinationPort(TDSConfiguration.DISTRIBUTOR_PORT_NUMBER);
            tdsRequest.setMethod(Constants.NODE_UNREGISTER);
            tdsRequest.setParameter(Constants.NODE_ID, nodeId);
            try {
                TDSResponse tdsResponse = RequestSender.sendRequest(tdsRequest);
                if (tdsResponse.getStatus() == 1) {
                    fileProcessor.delete();

                    System.out.println("Node unregistered successfully");
                } else {
                    System.out.println(tdsResponse.getErrorMessage());
                }
            } catch (IOException | ClassNotFoundException ex) {
                System.out.println("Connection with distributor is interrupted");
            }
        } else {
            System.out.println("Node is already not registered");
        }
    }

    private void startNode() {
        boolean isSent = sendHeartBeat();

        if (isSent == true) {
            try {
                ConnectionListener connectionListener = new ConnectionListener();
                logger.logInfo("startNode", "Node is started to receive the task");
                while (true) {

                    Socket socket = connectionListener.listen();
                    TDSRequest tdsRequest = getRequest(socket);
                    logger.logInfo("startNode", "Node received a task");
                    TDSResponse tdsResponse = processRequest(tdsRequest);
                    sendResponse(socket, tdsResponse);
                }
            } catch (IOException exception) {

            }

        }
    }

    private void registerNode() throws ClassNotFoundException {
        String hostName;
        String ipAddress;

        NodeIdFileProcessor fileProcessor = new NodeIdFileProcessor();

        if (nodeId.isEmpty()) {

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
                tdsRequest.setMethod(Constants.NODE_REGISTER);
                tdsRequest.setDestinationPort(TDSConfiguration.DISTRIBUTOR_PORT_NUMBER);
                tdsRequest.setDestinationIp(TDSConfiguration.DISTRIBUTOR_IP_ADDRESS);
                tdsRequest.setParameter("node_name", hostName);
                tdsRequest.setParameter(Constants.PORT_NUMBER, 50);
                tdsRequest.setParameter(Constants.STATUS, NodeStatus.AVAILABLE.name());
                tdsRequest.setParameter(Constants.IP_ADDRESS, ipAddress);
                TDSResponse response = RequestSender.sendRequest(tdsRequest);
                if (response.getStatus() == 1) {
                    fileProcessor.write((String) response.getValue(Constants.NODE_ID));
                    showMessage("Node is registered successfully with ID : " + response.getValue("id"));
                } else {
                    showMessage(response.getErrorMessage());
                }
            } catch (IOException exp) {
                showMessage(exp.getMessage());
            }
        } else {
            showMessage("Node is already registered");
        }
    }

    private TDSRequest getRequest(Socket socket) throws IOException {
        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
        String tdsRequestString = dataInputStream.readUTF();
        return (TDSRequest) tdsSerializer.deserialize(tdsRequestString);

    }

    private void sendResponse(Socket socket, TDSResponse tdsResponse) throws IOException {
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
        String tdsReponseString = tdsSerializer.serialize(tdsResponse);
        dataOutputStream.writeUTF(tdsReponseString);
        socket.close();
    }

    private TDSResponse processRequest(TDSRequest tdsRequest) {

        logger.logInfo("processrequest", "Node is processing the task");
        TDSResponse tdsResponse = new TDSResponse();

        try {
            TaskResult taskResult;
            tdsResponse.setStatus(1);

            File file = createFile((String) tdsRequest.getParameter(Constants.FILE_PATH), (String) tdsRequest.getParameter(Constants.DATA));
            TaskExecutor taskExecutor = TaskExecutorFactory.getTaskExecutor(file.getName());

            if (taskExecutor != null) {
                taskResult = taskExecutor.execute(file);
                if (taskResult.getErrorMessage().isEmpty()) {
                    tdsResponse.setValue(Constants.RESULT, taskResult.getResult());

                } else {
                    tdsResponse.setValue(Constants.ERROR_MESSAGE, taskResult.getErrorMessage());
                    tdsResponse.setValue(Constants.ERROR_CODE, taskResult.getErrorCode());
                }

            } else {
                tdsResponse.setValue(Constants.ERROR_MESSAGE, "Environment to run the task is not exist");
                tdsResponse.setValue(Constants.ERROR_CODE, TDSResponseErrorCode.EXECUTOR_NOT_AVAILABLE);

            }

        } catch (IOException exception) {
            logger.logError("processRequest", "Unknown error occured", exception);
            tdsResponse.setStatus(0);
            tdsResponse.setErrorMessage("Node  is not able to process this task ");
        }
        return tdsResponse;
    }

    private File createFile(String filePath, String data) throws FileNotFoundException, IOException {
        File file = new File(NODE_DIR + filePath);

        try ( FileOutputStream fout = new FileOutputStream(file)) {
            fout.write(Base64.getDecoder().decode(data));
        }
        return file;
    }

    private void showMessage(String message) {
        System.out.print(message);
    }

    private void addCapability(String command[]) throws InvalidCommandException {
        if (command.length > 1 && command.length < 3) {
            String capability = command[1];
            if (isValidCapability(capability)) {
                if (isEnvironmentExist(capability)) {
                    sendRequstToAddCapability(capability);
                } else {
                    showMessage("The System does not have enviroment for " + capability);
                }
            }
        } else {
            throw new InvalidCommandException();
        }
    }

    private void removeCapability(String command[]) {
        if (command.length > 1 && command.length < 3) {
            String capability = command[1];
            if (isValidCapability(capability)) {

                TDSRequest tdsRequest = new TDSRequest();
                tdsRequest.setDestinationIp(TDSConfiguration.DISTRIBUTOR_IP_ADDRESS);
                tdsRequest.setDestinationPort(TDSConfiguration.DISTRIBUTOR_PORT_NUMBER);
                tdsRequest.setMethod(Constants.NODE_REMOVE_CAPABILITY);
                tdsRequest.setParameter(Constants.NODE_ID, nodeId);
                tdsRequest.setParameter(Constants.CAPABILITY_NAME, capability);

                try {
                    TDSResponse tdsResponse = RequestSender.sendRequest(tdsRequest);
                    if (tdsResponse.getStatus() == 1) {
                        System.out.println("Capability removed successfuly");
                    } else {
                        System.out.print(tdsResponse.getErrorMessage());
                    }
                } catch (IOException | ClassNotFoundException ex) {
                    System.out.println("Connection failed with distributor");
                }

            }
        }
    }

    private boolean sendHeartBeat() {
        try {
            InetAddress address = InetAddress.getLocalHost();
            String ipAddress = address.getHostAddress();
            TDSRequest tdsRequest = new TDSRequest();
            tdsRequest.setMethod(Constants.NODE_HEARTBEAT);
            tdsRequest.setDestinationIp(TDSConfiguration.DISTRIBUTOR_IP_ADDRESS);
            tdsRequest.setDestinationPort(TDSConfiguration.DISTRIBUTOR_PORT_NUMBER);
            tdsRequest.setParameter(Constants.NODE_ID, nodeId);
            tdsRequest.setParameter(Constants.IP_ADDRESS, ipAddress);

            try {
                TDSResponse tdsResponse = RequestSender.sendRequest(tdsRequest);
                if (tdsResponse.getStatus() == 1) {
                    return true;
                } else {
                    System.out.print(tdsResponse.getErrorMessage());
                    return false;
                }
            } catch (Exception expception) {
                System.out.print("Unable to reach to the server");
            }

        } catch (UnknownHostException ex) {
            System.out.print("Unable to get the ip address of the system");
        }

        return false;
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

    private void sendRequstToAddCapability(String capability) {
        TDSRequest tdsRequest = new TDSRequest();
        tdsRequest.setDestinationIp(TDSConfiguration.DISTRIBUTOR_IP_ADDRESS);
        tdsRequest.setDestinationPort(TDSConfiguration.DISTRIBUTOR_PORT_NUMBER);
        tdsRequest.setMethod(Constants.NODE_ADD_CAPABILITY);
        tdsRequest.setParameter(Constants.NODE_ID, nodeId);
        tdsRequest.setParameter(Constants.CAPABILITY_NAME, capability);

        try {
            TDSResponse tdsResponse = RequestSender.sendRequest(tdsRequest);
            if (tdsResponse.getStatus() == 1) {
                System.out.println("Capability added successfuly");
            } else {
                System.out.print(tdsResponse.getErrorMessage());
            }
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("Connection failed with distributor");
        }
    }
}
