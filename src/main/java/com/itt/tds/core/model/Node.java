package com.itt.tds.core.model;

import java.io.Serializable;

public class Node implements Serializable {

    private String id;
    private String name;
    private int portNumber;
    private String ipAddress;
    private String status;

    public Node() {
        id = "";
        name = "";
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
