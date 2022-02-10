package com.itt.tds.core.model;

import java.io.Serializable;

public class Node implements Serializable {

    private String id;
    private String hostName;
    private int portNumber;
    private String ipAddress;
    private String status;

    public Node() {
        id = "";
        hostName = "";
        portNumber = 0;
        ipAddress = "";
        status = "";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String name) {
        this.hostName = name;
    }

    public int getPortNumber() {
        return portNumber;
    }

    public void setPortNumber(int portNumber) {
        this.portNumber = portNumber;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
