package core;

import com.itt.tds.core.Networking.TDSRequest;
import com.itt.tds.core.Networking.TDSSerializer;
import com.itt.tds.core.Networking.TDSSerializerFactory;
import org.junit.Assert;
import org.junit.Test;

public class TestJsonSeriralizer {

    @Test
    public void testSerialize() {

        //arrange
        TDSRequest tdsRequest = new TDSRequest();
        TDSSerializer tdsSerializer = TDSSerializerFactory.getSerializer("Json");
        tdsRequest.setMethod("node-add");
        tdsRequest.setDestinationIp("localhost");
        tdsRequest.setDestinationPort(40);
        tdsRequest.setParameter("name", "ITT-NITINJ");
        String expected = "{\"method\":\"node-add\",\"parameters\":{\"name\":\"ITT-NITINJ\"},\"destinationIp\":\"localhost\",\"destinationPort\":40,\"sourcePort\":0,\"headers\":{},\"protocolType\":\"request\"}";

        //act
        String serializedTDSRequest = tdsSerializer.serialize(tdsRequest);

        //assert
        Assert.assertEquals(expected, serializedTDSRequest);
    }

    @Test
    public void testDeserialize() {

        //arrange
        String serialzedTDSRequest = "{\"method\":\"node-add\",\"parameters\":{\"name\":\"ITT-NITINJ\"},\"destinationIp\":\"localhost\",\"destinationPort\":40,\"sourcePort\":0,\"headers\":{},\"protocolType\":\"request\"}";
        TDSSerializer tdsSerializer = TDSSerializerFactory.getSerializer("Json");

        //act
        TDSRequest tdsRequest = (TDSRequest) tdsSerializer.deserialize(serialzedTDSRequest);

        //assert
        Assert.assertEquals("node-add", tdsRequest.getMethod());
        Assert.assertEquals("localhost", tdsRequest.getDestinationIp());
        Assert.assertEquals(40, (int) tdsRequest.getDestinationPort());
    }

}
