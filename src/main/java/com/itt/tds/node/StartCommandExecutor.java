package com.itt.tds.node;

import com.itt.tds.client.InvalidCommandException;
import com.itt.tds.core.Constants;
import com.itt.tds.core.Networking.RequestSender;
import com.itt.tds.core.Networking.TDSRequest;
import com.itt.tds.core.Networking.TDSResponse;
import com.itt.tds.core.Networking.TDSSerializer;
import com.itt.tds.core.Networking.TDSSerializerFactory;
import com.itt.tds.core.config.TDSConfiguration;
import com.itt.tds.core.enums.ResponseStatus;
import com.itt.tds.core.enums.TDSResponseErrorCode;
import com.itt.tds.core.logging.LogManager;
import com.itt.tds.core.logging.Logger;
import com.itt.tds.core.model.TaskResult;
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

/**
 *
 * @author nitin.jangid
 */
public class StartCommandExecutor implements CommandExecutor {

    private String nodeId;
    private Logger logger;
    private TDSSerializer tdsSerializer;
    private String NODE_DIR = "c:\\ExecutorNode\\";

    public StartCommandExecutor() {
        nodeId = new NodeIdFileProcessor().read();
        logger = LogManager.getLogger(this.getClass().getName());
        tdsSerializer = TDSSerializerFactory.getSerializer(TDSConfiguration.SERIALIZATION_FORMAT);
    }

    @Override
    public void executeCommand(String parameter) throws InvalidCommandException {
        if (parameter.isEmpty()) {
            if (Utils.isNodeRegistered()) {
                boolean isSent = sendHeartBeat();
                if (isSent == true) {
                    try {
                        ConnectionListener connectionListener = new ConnectionListener();
                        logger.logInfo("executeCommand", "Node is started to receive the task");
                        while (true) {

                            Socket socket = connectionListener.listen();
                            TDSRequest tdsRequest = getRequest(socket);
                            logger.logInfo("startNode", "Node received a task");
                            TDSResponse tdsResponse = processRequest(tdsRequest);
                            sendResponse(socket, tdsResponse);

                        }
                    } catch (IOException exception) {
                        logger.logError("commadExecutor", "Unknown error : " + exception.getMessage());
                    }
                }
            } else {
                Utils.showMessage("Please register the node first");
            }
        } else {
            throw new InvalidCommandException();
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
        logger.logInfo("sendResponse", "Response has been sent successfuly");
    }

    private TDSResponse processRequest(TDSRequest tdsRequest) {
        logger.logInfo("processrequest", "Node is processing the task");
        TDSResponse tdsResponse = new TDSResponse();

        try {
            TaskResult taskResult;
            tdsResponse.setStatus(ResponseStatus.OK.getValue());

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
            tdsResponse.setStatus(ResponseStatus.FAIL.getValue());
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
                if (tdsResponse.getStatus() == ResponseStatus.OK.getValue()) {
                    return true;
                } else {
                    System.out.print(tdsResponse.getErrorMessage());
                    return false;
                }
            } catch (Exception expception) {
                Utils.showMessage("Unable to reach to the server");
            }

        } catch (UnknownHostException ex) {
            Utils.showMessage("Unable to get the ip address of the system");
        }

        return false;
    }

}
