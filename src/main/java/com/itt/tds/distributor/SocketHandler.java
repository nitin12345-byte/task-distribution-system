package com.itt.tds.distributor;

import com.itt.tds.core.Networking.TDSSerializerFactory;
import com.itt.tds.core.Networking.TDSRequest;
import com.itt.tds.core.Networking.TDSResponse;
import com.itt.tds.core.Networking.TDSSerializer;
import com.itt.tds.core.config.TDSConfiguration;
import com.itt.tds.core.logging.LogManager;
import com.itt.tds.core.logging.Logger;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class SocketHandler implements Runnable {

    private Socket socket;
    private Logger logger;

    public SocketHandler(Socket socket) {
        this.socket = socket;
        logger = LogManager.getLogger(this.getClass().getName());
    }

    @Override
    public void run() {
        TDSResponse tdsResponse;

        try {
            try {

                logger.logInfo("run", "Socket is handling the request");

                TDSSerializer tdsSerializer = TDSSerializerFactory.getSerializer(TDSConfiguration.SERIALIZATION_FORMAT);

                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

                String tdsRequestString = dataInputStream.readUTF();
                TDSRequest tdsRequest = (TDSRequest) tdsSerializer.deserialize(tdsRequestString);

                TDSController tdsController = ControllerFactory.getController(tdsRequest.getMethod());
                tdsResponse = tdsController.processRequest(tdsRequest);

                String tdsResponseString = tdsSerializer.serialize(tdsResponse);
                dataOutputStream.writeUTF(tdsResponseString);
                logger.logInfo("run", "Response has been sent successfully");

            } catch (IOException exception) {
                System.out.println(exception.getCause().getMessage());
                logger.logError("run", "Connection is closed", exception);
            } finally {
                socket.close();
            }
        } catch (Exception exception) {
            logger.logError("run", "Unknown error", exception);
        }
    }

}
