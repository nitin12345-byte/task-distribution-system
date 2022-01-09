package com.itt.tds.core.model;

import java.io.Serializable;

public class Client implements Serializable{

    private long id;
    private String ipAddress;
    private String userName;
    private int portNumber;

    public void client() {
        id = 0;
        ipAddress = "";
        userName = "";
        portNumber = 0;
    }

    public int getPortNumber() {
        return portNumber;
    }

    public void setPortNumber(int portNumber) {
        this.portNumber = portNumber;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
