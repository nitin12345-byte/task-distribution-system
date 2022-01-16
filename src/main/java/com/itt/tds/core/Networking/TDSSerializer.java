package com.itt.tds.core.Networking;

public interface TDSSerializer {

    public String serialize(TDSProtocol tdsProtocol);

    public TDSProtocol deserialize(String protocolString);

}
