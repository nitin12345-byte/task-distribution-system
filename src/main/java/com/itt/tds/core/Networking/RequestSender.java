/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itt.tds.core.Networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author nitin.jangid
 */
public class RequestSender {

    public static TDSResponse sendRequest(TDSRequest tdsRequest) throws IOException, ClassNotFoundException {
        Socket socket = new Socket(tdsRequest.getDestinationIp(), tdsRequest.getDestinationPort());
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

        TDSSerializer tdsSerializer = SerializerFactory.getSerializer("Json");
        String tdsRequestString = tdsSerializer.serialize(tdsRequest);
        dataOutputStream.writeUTF(tdsRequestString);

        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());

        String tdsResponseString;

        System.out.print("request sent");
        while ((tdsResponseString = dataInputStream.readUTF()) == null) {
        }

        TDSResponse tdsResponse = (TDSResponse) tdsSerializer.deserialize(tdsResponseString);

        return tdsResponse;
    }

}
