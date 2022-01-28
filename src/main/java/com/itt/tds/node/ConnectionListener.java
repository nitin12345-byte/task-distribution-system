
package com.itt.tds.node;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

class ConnectionListener {

    private ServerSocket serverSocket;

    public ConnectionListener() throws IOException {
        serverSocket = new ServerSocket(50);
    }

    public Socket listen() throws IOException {

        Socket socket = serverSocket.accept();
        return socket;
    }

}
