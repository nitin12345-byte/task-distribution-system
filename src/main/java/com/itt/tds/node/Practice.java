/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itt.tds.node;

import com.itt.tds.core.Networking.TDSRequest;
import com.itt.tds.core.Networking.TDSSerializer;
import com.itt.tds.core.Networking.TDSSerializerFactory;

/**
 *
 * @author nitin.jangid
 */
public class Practice {

    public static void main(String args[]) {

        TDSRequest tdsRequest = new TDSRequest();
        TDSSerializer tdsSerializer = TDSSerializerFactory.getSerializer("Json");
        tdsRequest.setMethod("node-add");
        tdsRequest.setDestinationIp("localhost");
        tdsRequest.setDestinationPort(40);
        tdsRequest.setParameter("name", "ITT-NITINJ");
        String expected = "{\"method\":\"node-add\",\"parameters\":{\"name\":\"ITT-NITINJ\"},\"destinationIp\":\"localhost\",\"destinationPort\":40,\"sourcePort\":0,\"headers\":{},\"protocolType\":\"request\"}";

        //act
        String serializedTDSRequest = tdsSerializer.serialize(tdsRequest);
        System.out.print(serializedTDSRequest);
    }
}
