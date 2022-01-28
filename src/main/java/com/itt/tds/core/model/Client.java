package com.itt.tds.core.model;

import java.io.Serializable;

public class Client implements Serializable {

    private String id;
    private String hostName;
    private String ipAddress;

    public void client() {
        id = "";
        ipAddress = "";
        hostName = "";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }
}
