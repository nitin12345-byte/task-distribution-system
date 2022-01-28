package com.itt.tds.core.Networking;

import java.util.HashMap;

public class TDSProtocol {

    private String destinationIp;
    private int destinationPort;
    private HashMap<String, Object> headers;
    private String protocolType;

    public String getProtocolType() {
        return protocolType;
    }

    protected void setProtocolType(String protocolType) {
        this.protocolType = protocolType;
    }

    public TDSProtocol() {
        this.headers = new HashMap<>();
    }

    public String getDestinationIp() {
        return destinationIp;
    }

    public void setDestinationIp(String destinationIp) {
        this.destinationIp = destinationIp;
    }

    public int getDestinationPort() {
        return destinationPort;
    }

    public void setDestinationPort(int destinationPort) {
        this.destinationPort = destinationPort;
    }

    public Object getHeader(String key) {
        return headers.get(key);
    }

    public void setHeader(String key, String value) {
        headers.put(key, value);
    }

}
