package com.itt.tds.distributor;

import com.itt.tds.core.config.TDSConfiguration;
import com.itt.tds.core.logging.LogManager;
import com.itt.tds.core.logging.Logger;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TDSServer implements Runnable {

    private ServerSocket serverSocket;
    private Logger logger;

    public TDSServer() {
        logger = LogManager.getLogger(this.getClass().getName());
    }

    @Override
    public void run() {

        logger.logInfo("run", "Server is running......");

        try {
            serverSocket = new ServerSocket(TDSConfiguration.DISTRIBUTOR_PORT_NUMBER);

            while (true) {
                Socket socket = serverSocket.accept();
                logger.logInfo("run", "Request is accepted....");
                SocketHandler socketHandler = new SocketHandler(socket);
                Thread socketHandlerThread = new Thread(socketHandler);
                socketHandlerThread.start();
            }
        } catch (IOException ex) {
            System.out.print(ex.getMessage());
        }

    }

}
