package com.itt.tds.core.Networking;

import java.util.HashMap;

public class TDSProtocol {

    private String destinationIp;
    private int destinationPort;
    private String SourceIp;
    private int sourcePort;
    private String protocolVersion;
    private String protocolFormat;
    private HashMap<String, String> headers;
    private Object body;

    public TDSProtocol() {
        this.headers = new HashMap<>();
    }

    public String getProtocolVersion() {
        return protocolVersion;
    }

    public void setProtocolVersion(String protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public String getProtocolFormat() {
        return protocolFormat;
    }

    public void setProtocolFormat(String protocolFormat) {
        this.protocolFormat = protocolFormat;
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

    public String getSourceIp() {
        return SourceIp;
    }

    public void setSourceIp(String SourceIp) {
        this.SourceIp = SourceIp;
    }

    public int getSourcePort() {
        return sourcePort;
    }

    public void setSourcePort(int sourcePort) {
        this.sourcePort = sourcePort;
    }

    public String getHeader(String key) {
        return headers.get(key);
    }

    public void setHeader(String key, String value) {
        headers.put(key, value);
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }
}
