package com.itt.tds.client;

import java.io.Serializable;


public class TDSClientConfiguration implements Serializable{
    private String distributorIpAddress;
    private int distributorPortNumber;

    public String getDistributorIpAddress() {
        return distributorIpAddress;
    }

    public void setDistributorIpAddress(String distributorIpAddress) {
        this.distributorIpAddress = distributorIpAddress;
    }

    public int getDistributorPortNumber() {
        return distributorPortNumber;
    }

    public void setDistributorPortNumber(int distributorPortNumber) {
        this.distributorPortNumber = distributorPortNumber;
    }
}
