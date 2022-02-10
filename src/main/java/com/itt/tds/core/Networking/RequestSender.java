package com.itt.tds.core.Networking;

import com.itt.tds.core.config.TDSConfiguration;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class RequestSender {

    public static TDSResponse sendRequest(TDSRequest tdsRequest) throws IOException, ClassNotFoundException {
        Socket socket = new Socket(tdsRequest.getDestinationIp(), tdsRequest.getDestinationPort());
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

        TDSSerializer tdsSerializer = TDSSerializerFactory.getSerializer(TDSConfiguration.SERIALIZATION_FORMAT);
        String tdsRequestString = tdsSerializer.serialize(tdsRequest);
        dataOutputStream.writeUTF(tdsRequestString);
        System.out.println("Request sent");
        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
        String tdsResponseString = dataInputStream.readUTF();
        TDSResponse tdsResponse = (TDSResponse) tdsSerializer.deserialize(tdsResponseString);

        return tdsResponse;
    }

}
